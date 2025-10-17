Nice — IRCTC is a richer, more complex variant of the “ticket booking” problem because of **quotas, RAC/WL, charting, multiple allotment rounds, concessions, offline counters, and regulatory constraints**. Below is a complete, interview-ready design write-up you can use: requirements, non-functional goals, back-of-envelope capacity planning (with assumptions), high-level design, low-level design (DB schemas + pseudocode), seat/berth locking and allocation strategies (including RAC/WL handling), caching, quotas, payment & refunds, operations, trade-offs, and edge cases.

I’ll assume you want this in a single Markdown document — I formatted it so you can copy into `irctc_system_design.md` or present directly in interviews.

---

# IRCTC — Scalable Indian Railway Reservation System (Design Notes)

> **Assumptions (state before answering in interview):**
> These numbers and rules are assumptions for capacity planning and flows. Replace with interviewer’s numbers if given.
>
> * Active users: **10–50M monthly** (assume 20M MAU)
> * Peak concurrent sessions: **200k–1M** (even larger during tatkal windows)
> * Average bookings/day: **0.5M–5M** (assume 1M/day)
> * Typical ticket JSON payload ~5–10 KB
> * Strong consistency required for seat allocation / final confirmation

---

## Table of Contents

1. Quick elevator pitch / scope
2. Functional requirements (core IRCTC features)
3. Non-functional requirements (NFRs)
4. Back-of-envelope capacity planning & assumptions
5. High-level architecture (HLD) — components & flow
6. Detailed booking flow (sequence)
7. Seat/berth allocation, RAC, waiting list, charting — core challenges
8. Locking & concurrency strategies (design + pseudocode)
9. Database design and trade-offs (schema + sharding)
10. Cache strategy & consistency (what to cache and how to invalidate)
11. Quotas, concessions, dynamic allotments & special flows
12. Payments, refunds & idempotency
13. APIs (example endpoints + headers)
14. Low-level details (SQL transaction pseudocode, Lua for Redis)
15. Scaling patterns & trade-offs (CAP, CQRS, leader-per-train)
16. Reliability, fault tolerance & reconciliation
17. Observability & ops (metrics to monitor, alerts)
18. Security & anti-fraud (bots, high-demand tatkal)
19. Testing strategy (load, chaos, reconciliation)
20. Real-world edge cases & remedies
21. Interview talking points / cheat-sheet

---

## 1. Quick elevator pitch / scope

Design a highly available, scalable IRCTC-like system that allows users (web/mobile/agents/rail counters) to search trains, check berth availability, reserve (lock) berths, pay, and get confirmed tickets. Must support quotas, RAC/waitlists, tatkal booking windows, dynamic charting, offline counters, and high concurrency at peak times.

---

## 2. Functional requirements

* Search trains between stations (by date, class, quota, direct/with-stops).
* Show seat/berth map & live availability per coach for a selected date/journey.
* Hold (lock) berth(s) for user with TTL while completing payment.
* Confirm booking (PNR generation) with statuses: Confirmed / RAC / Waitlist / Cancelled.
* Special quotas: General, Tatkal, Ladies, Senior Citizen, Defence, Divyang, Premium Tatkal etc.
* RAC (Reservation Against Cancellation): partial berth (sitting) allowed; RAC → CNF on cancellation.
* Waitlist promotion and chart preparation (prepare final reservation chart before departure).
* Booking modification/cancellation & partial refunds based on rules.
* Agent/Office booking with separate rate limits/quotas.
* Admin flows: train schedules, quotas, dynamic seat allotment, block seats.
* Notifications: SMS / email / PDF e-ticket with PNR & QR.
* Reports/analytics: seat utilization, revenue, cancellations, fraud detection.

---

## 3. Non-functional requirements (NFRs)

* **Availability:** very high, especially during tatkal windows. Read-only operations should be extremely available.
* **Consistency:** strong consistency required for final allocation; eventual consistency acceptable for search index and analytics.
* **Latency:** search <200ms, booking flow end-to-end <2s ideally (tatkal may allow slightly higher).
* **Scalability:** handle large spikes (tatkal/day-of-release).
* **Durability:** bookings must be durable (ACID) and auditable.
* **Security & Compliance:** PCI-DSS for payments; encrypt PII; rate limiting & bot mitigation.
* **Observability:** metrics, tracing, logs, and automated reconciliation jobs.

---

## 4. Back-of-envelope capacity planning (assumptions + numbers)

> **Assumptions**: MAU=20M, bookings/day=1M, average seats per booking=2

* Bookings/day: **1,000,000**
* Average bookings/sec = 1,000,000 / 86,400 ≈ **12/s**
* Peak factor (evening / tatkal surge): ×50 → **~600 writes/sec**
* Reads (search/availability) typically 100× writes → **60k RPS** during peak
* Storage: bookings/year ≈ 365M bookings → if each booking 2KB → ≈ 730GB/year; keep ~5 yrs → ~3.7TB (plus indexes, logs, analytics)
* Cache: seat map per train-date-coach small (a few KB), many cached entries → Redis cluster needed

> **Design for headroom**: provision for 5–10× these peak numbers, autoscale.

---

## 5. High-level architecture (text + ASCII)

```
[Users (Web/Mobile/Agent/Counter)]
        │
        ▼
     [CDN] (static assets)
        │
        ▼
[Load Balancer / API Gateway]  <-- Auth, Rate-limit, WAF, TLS
        │
   ┌────┴───────┬──────────┬────────┐
   │            │          │        │
[Search]    [Booking]  [User/Agent] [Admin]
Service     Service     Service     Service
(elastic)   (core)      (auth)      (admin UI)
   │            │          │        │
   │            ▼          ▼        ▼
   │        [Redis Cluster] (locks, seat cache, sessions)
   │            │
   │            ▼
   │        [Primary DB Cluster (Postgres sharded)]
   │            │
   │            ▼
   └────────>[Event Bus / Kafka] ──► [Notif Service / Analytics]
                 ▲
                 │
           [Payment Gateway / PCI Provider]
```

**Key components**:

* API Gateway (Kong/Nginx) — auth, rate-limiting, SSL, bot-detection.
* Search Service — Elasticsearch for train & schedule searches.
* Booking Service — core logic: quota checks, seat allocation, PNR generation.
* Redis Cluster — locks (per berth/coaches), session store, ephemeral caches.
* Relational DB (Postgres) — source of truth (bookings, seat_state, PNRs).
* Event bus (Kafka) — async tasks: notifications, WL promotions, analytics.
* Payment integration — external PCI provider (tokenization).
* Notification service — SMS, Email, e-ticket PDF, QR generation.
* Admin service — manage quotas, schedules, block seats.

---

## 6. Detailed booking flow (sequence)

1. User searches trains → Search Service (Elasticsearch) returns candidates.
2. User selects train/date/class/quota → client requests `GET /trains/{id}/availability?date=...`

   * Try read from Redis cached seat-map; on miss read from DB and populate cache.
3. User selects berths → client calls `POST /trains/{id}/reserve` (seats + passenger details).

   * Booking Service validates journey, quota, age concessions, max seats per PNR, tatkal window, agent/retailer constraints.
4. Booking Service performs **atomic multi-seat lock** in Redis (Lua script) to hold seats for TTL (e.g., 5–10 min). Returns `lock_id`.
5. User provides payment -> `POST /book` with `lock_id` and payment token + idempotency key.

   * Booking Service verifies lock token(s), then calls Payment Gateway (authorize/capture flow).
6. On payment success, Booking Service writes a booking transaction to DB (ACID): mark `show_seat` rows as `booked`, insert `booking`, `passenger` rows, and generate `PNR`.
7. Remove locks from Redis, publish `BookingConfirmed` event to Kafka.
8. Notification Service sends e-ticket with PNR & QR; updates waiting list promotions if needed.
9. If payment fails or TTL expires → release locks; inform user.

**Special**: If not enough confirmed berths but RAC possible → allocate RAC seats (specific seat numbers may not be available until charting or cancellations).

---

## 7. Seat allocation, RAC, Waiting List, Charting — core challenges

### Concepts to cover in interview:

* **Berth types**: Lower/Upper/Side/AC/SL/CC etc. Allocation must respect class & berth preference and concession rules.
* **RAC (Reservation Against Cancellation)**: seat allocation that assures travel but not a full berth. RAC entries are assigned numbers and may be converted to CNF upon cancellations.
* **Waitlist (WL)**: ordered list; promotions occur when cancellations free up berths.
* **Charting**: final reservation chart generated before train departure (e.g., 4 hours prior). Final allotments & coach/seat numbers set at charting time.
* **Quota handling**: seats set aside for quotas (Tatkal, Ladies, Senior, Premium) and sub-quotas across stations.
* **Multiple boarding- and de-boarding stations**: availability is for segments (station A→D where someone else might have A→B). Need segment-level seat availability (interval scheduling problem).

### Segment availability & seat reuse:

* Model seats as intervals across station sequence; a seat is available for a requested segment if it’s not booked for any overlapping segment. Efficient representation: for each seat maintain occupant intervals or use bitmap per leg.

---

## 8. Locking & concurrency strategies

### Requirements:

* Support atomic multi-seat locks for a PNR (either all seats allocated or none).
* Locks must auto-expire (TTL) in case user abandons flow.
* Avoid double booking under high contention (tatkal).

### Approaches:

1. **Redis atomic locks (recommended hybrid)**

   * Use a Lua script to check seat keys and set lock tokens with TTL atomically for all requested seat keys.
   * Lock key format: `lock:{train_id}:{date}:{coach}:{seat_no}` → value `{lock_token, user_id, expiry_ts}`.
   * Use unique lock token (UUID) and require token match when releasing.

2. **Optimistic DB + CAS**

   * Read version number, attempt update with `WHERE version = X` to avoid long DB locks. Works when contention low.

3. **Single-writer (leader) per train-date partition**

   * Partition by `train_id + travel_date` and assign leader service instance that serializes allocation for that partition. Removes distributed locking complexity but requires leader failover handling.

4. **Hybrid**

   * Use Redis locks for ephemeral reservation; on final booking perform DB transaction with `SELECT ... FOR UPDATE` to ensure ACID.

### Example Redis Lua (conceptual) — multi-seat lock

(see Section 14 for pseudocode).

**TTL choices**: 3–15 minutes depending on payment timeouts; tatkal flows may require shorter TTLs to keep throughput.

---

## 9. Database design & trade-offs

### Primary DB: Relational (Postgres / MySQL) — ACID required

**Tables (simplified)**:

* `users(user_id, name, phone, email, ...)`
* `trains(train_id, name, route_id, ...)`
* `routes(route_id, station_sequence_json, ...)`
* `schedules(schedule_id, train_id, date, class, coaches_json, ...)`
* `coaches(coach_id, train_id, coach_type, total_seats, seat_layout_json)`
* `seats(seat_id, coach_id, seat_no, berth_type)`
* `seat_availability(avail_id, schedule_id, coach_id, seat_id, status, booking_id, lock_token, segments_info)`
* `bookings(booking_id, pnr, user_id, schedule_id, status, amount, created_at, idempotency_key)`
* `passengers(passenger_id, booking_id, name, age, gender, berth_preference, concession)`
* `payments(payment_id, booking_id, status, provider_ref, amount, created_at)`

### Modeling segment availability

* `segments` table or bitset per coach-seat representing legs between consecutive stations. For speed: store a bitmask representing legs occupied; availability test is bitwise AND == 0.

### Sharding strategy

* **Shard by `schedule_id` (train+date)** — localizes all seat state for that journey in one shard; reduces cross-shard transactions and simplifies leader-per-train.
* Secondary sharding: by geography or booking hash for user-history.

### Read replicas

* Use async read replicas for search, analytics, and read-heavy endpoints. Writes must go to primary.

### Event sourcing?

* Optionally store booking/seating events to an append-only store (Kafka) for audit, replay and rebuild read models. Increases complexity.

---

## 10. Cache strategy & consistency

### What to cache:

* Search indices (Elasticsearch) — train schedules and route metadata.
* Seat map per `schedule_id + coach` — short TTL (e.g., 30–120s) and invalidated on booking/lock.
* User session & partial PNR/checkout info — Redis.
* Idempotency keys — small TTL.

### Maintaining correctness:

* **Invalidate** seat cache on booking commit and on lock acquisition (or update the cached seat map atomically via pub/sub so cached versions remain consistent).
* Use Redis pub/sub to broadcast `seat_changed:{schedule_id}` so all app nodes invalidate/refresh caches.

---

## 11. Quotas, concessions & special flows

* **Quotas**: allocate seats per quota at creation time (e.g., reserve X seats for Tatkal, Y seats for Ladies). Quotas may be per block of berths, per coach or global.
* **Tatkal**: special booking window with strict rules and higher charges. Implement as separate flow with stricter rate-limiting & captcha/bot checks.
* **Concessions**: aged, disabled, defence; pass the eligibility checks at booking time.
* **Agent/office bookings**: separate authentication with different rate-limits and reporting.
* **Dynamic allotment**: handle quota balancing across stations and cancellation promotions.

---

## 12. Payments & refunds

* Integrate with third-party PCI-compliant payment provider (tokenization). Do not store card data.
* Support **authorize → capture** model for better failure handling (authorize first, capture after commit).
* **Idempotency**: payment + booking operations must be idempotent — use idempotency keys to avoid multiple charges.
* Refunds: async process via queue; refunds may be partial and depend on cancellation policy. Publish events and update booking status.

---

## 13. Example APIs (REST)

```
GET /trains/search?source=XXX&dest=YYY&date=YYYY-MM-DD&class=SL
GET /trains/{train_id}/availability?date=YYYY-MM-DD&class=SL

POST /reserve
Body: { schedule_id, seats: [{coach, seat_no}], passengers: [...], quota, user_id }
Response: { lock_id, expiry_ts }

POST /book
Headers: Idempotency-Key: <uuid>
Body: { lock_id, payment_token, user_id }
Response: { booking_id, pnr, status }

POST /cancel
Body: { booking_id, passenger_ids[] }

GET /user/{user_id}/bookings
```

**Notes**:

* Include `Idempotency-Key` header on write operations.
* API Gateway enforces rate limits, especially during tatkal.

---

## 14. Low-level details (pseudocode)

### Multi-seat Redis lock (Lua) — conceptual

```lua
-- ARGV: lock_token, ttl, seat_key1, seat_key2, ...
for i, key in ipairs(ARGV, 3) do
  if redis.call("EXISTS", key) == 1 then
    return {err="ALREADY_LOCKED"}
  end
end
for i, key in ipairs(ARGV, 3) do
  redis.call("SET", key, ARGV[1], "PX", ARGV[2])
end
return {ok="LOCKED"}
```

Key pattern: `lock:{schedule_id}:{coach}:{seat_no}`

### Booking commit (pseudo-SQL)

```sql
BEGIN;
-- verify seats not booked
SELECT status FROM seat_availability
 WHERE schedule_id = :sched AND seat_id IN (...)
 FOR UPDATE;

-- ensure status='available' or status='locked' with matching lock_token
-- insert booking
INSERT INTO bookings (...) RETURNING booking_id;
INSERT INTO passengers (...)
UPDATE seat_availability
 SET status='booked', booking_id=:booking_id, lock_token=NULL
 WHERE schedule_id = :sched AND seat_id IN (...);
COMMIT;
```

**Important**: the `FOR UPDATE` ensures row-level locking in DB to prevent double-commit concurrently; this is done only after Redis locks validated.

---

## 15. Scaling patterns & trade-offs

### CQRS (Command Query Responsibility Segregation)

* Use separate write path (Booking Service + DB) and read path (materialized read models or caches / ES). Good for heavy read loads.

### Leader-per-train (partitioning)

* Assign a leader (single-writer) for each `schedule_id` partition so all allocations for that train-date are serialized at one place — simplifies locking but requires leader failover.

### CAP choices

* For seat allocation: **Consistency prioritized** (booking correctness > availability). For search and analytics: **Availability prioritized**.

### Trade-offs

* Redis locks are fast but ephemeral → need DB reconciliation.
* DB `FOR UPDATE` is safe but can block and limit concurrency under high contention (e.g., tatkal). Combining Redis for fast pre-check and DB transaction for commit balances the load.

---

## 16. Reliability, fault tolerance & reconciliation

### Failure modes & mitigations

* **Redis failure**: use Redis Cluster with AOF snapshots; if metadata lost, run reconciliation job from DB to rebuild ephemeral locks (may require temporary disable of bookings).
* **DB primary failover**: use automatic failover; queue write requests during failover or route to failover node with caution.
* **Payment provider outage**: degrade gracefully — allow offline bookings (rare) with later reconciliation or block tatkal. Use circuit breakers and retries.
* **Network partitions**: prefer fail-safe (deny booking) instead of risking double-booking.

### Reconciliation

* Periodic job (consumer on Kafka) to reconcile `seat_availability` state vs locks and bookings. Detect leaked locks, orphaned bookings, and double allocations.

---

## 17. Observability & operations

### Metrics & logs

* Booking attempts/sec, booking success rate, lock acquire rate, lock expiry count.
* Waitlist length per schedule, RAC promotions/sec.
* Payment latency & failures, queue backlogs.
* DB replication lag, Redis failover events.

### Tracing & alerts

* Distributed tracing for full booking flow (gateway → booking → payment → DB).
* Alerts: high lock expirations, high payment failures, DB primary down, spike in failed bookings.

### Deployments

* Canary & blue/green deployments for services.
* Maintenance & emergency modes for tatkal windows.

---

## 18. Security & anti-fraud

* **Auth & sessions**: OAuth/JWT for users and agents.
* **Rate limiting**: enforce stricter limits per IP/user/agent during tatkal windows.
* **Bot mitigation**: CAPTCHA, device fingerprinting, challenge-response, per-account velocity checks.
* **Payment security**: redirect to gateway or use tokenization; PCI-DSS compliance.
* **Data encryption**: PII encrypted at rest; TLS1.2+ in transit.
* **Monitoring**: anomaly detection for sudden booking surges from single IP/subnet.

---

## 19. Testing strategy

* **Unit & integration tests** for seat allocation logic, quota rules, concession checks.
* **Load testing**: simulate tatkal traffic bursts (hundreds of thousands RPS read, thousands writes). Tools: k6/Locust/JMeter.
* **Chaos testing**: kill Redis nodes, fail DB primaries, network partitions.
* **Reconciliation tests**: intentionally create orphan locks/bookings and run reconciliation.
* **Security testing**: pen test; bot & fraud simulations.

---

## 20. Real-world edge cases & how to handle them

* **Partial payment success** (bank debited but booking failed): use payment provider settlement reports, reconcile and refund automatically.
* **Simultaneous bookings for same seat**: Redis lock + DB `FOR UPDATE` ensures correctness; tie-break using lock token timestamp.
* **Multiple boarding pairs** (station segments): implement interval availability or leg-bitmask to allow seat reuse for non-overlapping segments.
* **Charting race**: chart generation job must lock schedule, finalize allocations and notify passengers; handle concurrent last-minute cancellations.
* **Agent misuse**: strict monitoring of agent credentials and per-agent quotas; revoke if abuse detected.
* **Tatkal bot attacks**: pre-registration, OTP, stricter device checks, and special captchas.

---

## 21. Interview talking points / cheat-sheet

1. **Start with assumptions** (MAU, bookings/day, peak multipliers).
2. **Explain high-level architecture** (API Gateway → Booking Service + Redis + RDBMS + Kafka + Payment).
3. **Key complexity**: segment-level seat availability, quotas, RAC & waiting list, and charting.
4. **Locking strategy**: hybrid Redis locks for pre-reservation + DB transaction for final commit. Show sample Lua + SQL.
5. **Sharding**: shard by `schedule_id` (train+date) to localize writes. Mention leader-per-train as alternative.
6. **Caching**: seat maps in Redis with pub/sub invalidation; ES for search.
7. **Payments**: tokenized external providers + idempotency keys.
8. **Failure & reconciliation**: periodic jobs and event-driven reconciliation.
9. **Scaling/tatkal**: queue-based admission or lottery for extremely contested allocations; bot mitigation.
10. **Trade-offs**: speed vs correctness (CAP), Redis ephemeral vs DB durable, monolith vs microservices.

---

If you’d like, I can next:

* convert this into a **presentation slide deck** (PowerPoint/Google Slides style),
* draw a **detailed sequence diagram** for the complete booking → payment → PNR flow,
* produce **sample SQL schemas + DDL** for the tables above, or
* generate **code snippets** showing a runnable Redis Lua lock + Postgres transaction example in Python/Node.

Which one should I produce next?
