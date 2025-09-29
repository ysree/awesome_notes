## Why Sharding?

As data grows, a single database server may struggle with:

* **Storage limits** (too much data for one machine)
* **Performance issues** (slow queries, heavy traffic)
* **Scalability problems** (hard to add more resources beyond a certain point)

Sharding helps by distributing the load across multiple servers.

---

## How Sharding Works

* **Data is partitioned** based on a sharding key (like `user_id` or `region`).
* **Example:** All users with `user_id` 1–1000 go to Shard 1, 1001–2000 to Shard 2, etc.
* **Each shard is stored** on a separate database/server.
* An **application or middleware layer** keeps track of which shard holds which data.

---

## Types of Sharding

### 1. Horizontal Sharding

* Rows are split across shards.
* **Example:** Users A–M in one shard, N–Z in another.
* ✅ Most common type.

#### Horizontal Sharding (done on nodes)
Data rows are distributed across different nodes.
- **Example:** Suppose you have a users table with millions of rows:
    - Node 1 (Shard 1): users with user_id 1–1M
    - Node 2 (Shard 2): users with user_id 1M+1–2M
    - Node 3 (Shard 3): users with user_id 2M+1–3M

- 👉 Each node stores a portion of the table’s rows.
- 👉 This reduces the load on any single node.

### 2. Vertical Sharding

* Tables or columns are split across shards.
* **Example:** User profile info in one shard, user activity logs in another.

#### Vertical Sharding (done on nodes)
- **Example:** For the same users table:
    - Node 1: User profile info (user_id, name, email)
    - Node 2: User authentication (user_id, password, roles)
    - Node 3: User activity logs (user_id, last_login, actions)

- 👉 Each node stores different parts of the schema.
- 👉 Queries are directed to the node that holds the relevant part.

---

## Benefits

* **Scalability:** Add more servers as data grows.
* **Performance:** Queries run on smaller datasets.
* **Reliability:** If one shard fails, others may still work.

---

## Challenges

* **Complexity:** More difficult to design and maintain.
* **Joins across shards:** Harder to query data that lives in different shards.
* **Rebalancing:** Moving data when shards become uneven can be tricky.

---


## Fault Tolerance in Sharded Databases

One of the biggest challenges in sharded databases is **fault tolerance**.

When you run a query in a sharded database and one of the nodes (shards) is down, the outcome depends on the query type and the database’s design.

---

## Scenarios When a Shard Node is Down

### 1. Query against only one shard (targeted query)

* **Example:** `SELECT * FROM users WHERE user_id = 1050`
* The system knows (via the shard key) that `user_id = 1050` lives in Shard 2.
* If Shard 2 is down, the query fails because the data is unavailable.

**How to manage it:**

* Use replication inside each shard (primary–replica setup).
* If the primary node is down, a replica can take over automatically.
* Systems like MongoDB, Cassandra, CockroachDB use this approach.

### 2. Query spans multiple shards (scatter-gather query)

* **Example:** `SELECT COUNT(*) FROM users`
* Needs results from all shards.
* If one shard is down, the result will be incomplete or blocked.

**How to manage it:**

* Some systems fail fast and return an error.
* Some return partial results with a warning.
* Some retry until the shard comes back online.
* Analytics systems (BigQuery, Elasticsearch) may allow tolerance for partial results.

### 3. Write queries

* **Example:** `INSERT INTO orders VALUES (...)` where the responsible shard is down.
* Write will fail unless there’s replication.

**How to manage it:**

* Write to replicas if available.
* Use write-ahead logs or message queues (Kafka, Pulsar) to retry later.
* Buffer writes until the shard is healthy.

---

## General Strategies to Handle Shard Failures

* **Replication within each shard:** Every shard is a replicated cluster, ensuring high availability.
* **Automatic failover:** If a shard’s primary fails, replicas promote a new primary; clients reroute queries automatically.
* **Distributed transaction manager / coordinator:** Tracks which shards are available and retries or reroutes queries.
* **Graceful degradation:** For reads, return partial results; for writes, queue until shard is back.
* **Monitoring + Alerts:** Systems like Zookeeper, etcd, Consul track shard/node health; applications reroute queries accordingly.
