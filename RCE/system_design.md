# Table of Contents
- [Sample System Desing template](#sample-system-desing-template)
- [3-Tire Architecture](#3-tire-architecture)
- [API Gateway role](#api-gateway-role)
- [Arora db fault tolerance](#arora-db-fault-tolerance)
- [Cache with fault tolerance](#cache-with-fault-tolerance)
- [Security](#security)
- [Disaster Recovery Plan](#disaster-recovery-plan)
- [Backup Strategy](#backup-strategy)
- [Arora db failure](#arora-db-failure)
- [Explain complete architecture](#explain-complete-architecture)
---



![Architecture](./images/aws_architecture-1.png)
![Architecture](./images/architecture-with-cache.png)
![arora-db](./images/arora-db-az-region.png)
![cache-scaling](./images/cache-scaling.png)



---

# Sample System Desing template

I am fully prepared to assist you. To provide the best possible simulation of a real interview, I need to know **which specific system** you would like to design (e.g., "Design Twitter," "Design a URL Shortener," "Design Uber," or "Design a Web Crawler").

However, since you haven't specified the topic yet, I will demonstrate this framework using a classic, high-frequency interview question: **Design a Photo Sharing System (like Instagram).**

This example allows us to touch upon every single technical requirement you listed (CDN, Blob storage, Sharding, Caching, and Consistency).

-----

### **System Design: Photo Sharing Service (e.g., Instagram)**

#### **1. Assumptions**

Before we start, let's establish the boundaries of the system to clarify the scope.

  * **Traffic:** Heavy read, heavy write (Read-to-Write ratio is likely 10:1).
  * **Users:** 500 Million total users, 100 Million Daily Active Users (DAU).
  * **Content:** Users upload photos only (no videos for this MVP to keep it focused).
  * **Timeline:** We need a newsfeed generation service.

#### **2. Functional Requirements**

  * Users can upload photos.
  * Users can view photos (newsfeed and user profile).
  * Users can follow other users.
  * Users can search for photo metadata (titles/tags).

#### **3. Non-Functional Requirements**

  * **High Availability:** The system must be always up (CAP theorem implications later).
  * **Low Latency:** Newsfeed generation must be under 200ms.
  * **Reliability:** No uploaded photo should ever be lost.
  * **Durability:** Data must persist across failures.
  * **Scalability:** Must handle increasing user loads across geographies.

#### **4. Back of the Envelope - Capacity Planning**

  * **Traffic Estimates:**
      * 100M DAU.
      * If 10% upload 1 photo/day: 10M writes/day.
      * Reads: 100M users \* 20 views = 2 Billion reads/day.
      * **QPS (Writes):** $10,000,000 / 86400 \approx 115$ requests/sec.
      * **QPS (Reads):** $2,000,000,000 / 86400 \approx 23,000$ requests/sec.
  * **Storage Estimates:**
      * Average photo size: 500KB.
      * Daily Storage: $10M \times 500KB = 5TB/day$.
      * 10 Years Storage: $5TB \times 365 \times 10 \approx 18.25 PB$.
      * **Implication:** We definitely need object storage (S3) and sharding for metadata.

#### **5. API Design**

We will use RESTful APIs.

  * `POST /v1/photos`
      * Request: API\_KEY, Image\_Data, Caption, Location.
      * Response: 202 Accepted (Processing async).
  * `GET /v1/feed/{userId}`
      * Request: API\_KEY, Pagination\_Token.
      * Response: JSON list of photo URLs and Metadata.
  * `POST /v1/follow/{targetId}`
      * Request: API\_KEY, FollowerID.

#### **6. High-Level Design (HLD)**

**Architecture Flow:**

1.  **Client:** Mobile/Web.
2.  **CDN (Content Delivery Network):** Caches static images closer to the user to reduce latency.
3.  **Load Balancer:** Distributes incoming API traffic to application servers.
4.  **API Gateway:** Handles Authentication (OAuth), Rate Limiting, and SSL Termination.
5.  **Application Services (Microservices):**
      * *User Service:* Profile management.
      * *Content Service:* Handles photo upload processing.
      * *Feed Service:* Generates newsfeeds.
6.  **Data Layer:**
      * *Blob Storage (S3/GCS):* Stores the actual image files.
      * *SQL/NoSQL DB:* Stores metadata (User info, Photo paths, Likes).
      * *Cache (Redis):* Stores "Hot" feeds and user sessions.

-----

#### **Detailed Component Analysis & Trade-offs**

**A. Database Scaling (SQL vs. NoSQL)**

  * **Decision:** We need a hybrid approach.
      * **SQL (PostgreSQL/MySQL):** Used for User Data and Relationships (Who follows whom). Relational data is strict and benefits from ACID properties.
      * **NoSQL (Cassandra/DynamoDB):** Used for the Feed Generation or Activity Logs. We need extremely high write throughput and flexible schema.
  * **Scaling - Sharding:**
      * We will shard the *Photo Metadata* database.
      * *Option 1: Shard by UserID.* Keep all a user's photos on one shard.
          * *Pro:* Fast retrieval for "My Profile."
          * *Con:* "Hot User" problem (Celebrities with millions of followers create uneven load).
      * *Option 2: Shard by PhotoID.*
          * *Pro:* Even distribution.
          * *Con:* Aggregating a feed requires hitting multiple shards.
      * *Trade-off Choice:* Shard by **UserID** but handle celebrities (Hot Users) with a dedicated cache/resource (Hybrid approach).

**B. Caching & Strategies**

  * **Technology:** Redis or Memcached.
  * **Strategy:** **Cache-Aside**. The application looks in the cache; if missing, reads from DB, populates cache, and returns to user.
  * **What to Cache:** User sessions, metadata for hot photos, and the pre-computed newsfeed for active users.
  * **Eviction:** **LRU (Least Recently Used)** to remove old data when memory is full.

**C. Availability & CAP Theorem**

  * **CAP Analysis:** In a social network, if a network partition occurs, users would prefer to see a *slightly outdated* feed rather than an error message.
  * **Choice:** **AP (Availability & Partition Tolerance)** over Consistency. We accept **Eventual Consistency** (it's okay if a user sees a photo 2 seconds later than another user).

**D. Replication & Failover**

  * **Database:** Master-Slave Replication.
      * *Master:* Handles all Writes (Insert photo metadata).
      * *Slaves:* Handle all Reads (View feed).
      * *Failover:* If Master dies, a Slave is promoted (Automated via tools like ZooKeeper).
  * **Disaster Recovery:** **Active-Passive** Geo-Redundancy. Data is replicated to a different region asynchronously. If Region A sinks, we flip the DNS to Region B.

-----

#### **6. Low-Level Design (LLD): Feed Generation Service**

This is the most complex part. Two main approaches:

1.  **Pull Model (Fan-out-on-load):**
      * When User A requests their feed, the system queries all people User A follows, retrieves recent photos, and merges them.
      * *Trade-off:* Slow reads, fast writes. Bad for users following 5000+ people.
2.  **Push Model (Fan-out-on-write):**
      * When User B uploads a photo, the system immediately pushes this photo ID to the pre-computed feed of all User B's followers.
      * *Trade-off:* Fast reads (feed is ready), slow writes (processing intense for celebrities).
3.  **Hybrid Approach (Design Pattern):**
      * Use **Push** for normal users (fast read experience).
      * Use **Pull** for celebrities (Justin Bieber uploads -\> we don't push to 100M users; instead, we fetch his posts only when users actually load their feed).

#### **7. Design Patterns**

  * **Circuit Breaker:** Applied at the API Gateway. If the "Photo Service" is failing, stop sending requests immediately to prevent cascading failures across the system.
  * **Saga Pattern:** For distributed transactions (e.g., Upload photo -\> Update Metadata -\> Update Feed). If one fails, we execute compensating transactions to undo changes.
  * **CQRS (Command Query Responsibility Segregation):** Separate the Read APIs (Feed) from Write APIs (Upload) to scale them independently.

#### **8. Database Schema Design**

**Table: Users**

```sql
UserID (PK, Int64)
Name (VarChar)
Email (VarChar)
CreationDate (DateTime)
LastLogin (DateTime)
```

**Table: UserFollows**

```sql
FollowerID (PK, FK)
FolloweeID (PK, FK)
-- Compound Index on both for fast lookup
```

**Table: Photos**

```sql
PhotoID (PK, Int64) -- Generated via Twitter Snowflake for time-sortability
UserID (FK, Int64)
PhotoPath (VarChar) -- S3 URL
Latitude (Float)
Longitude (Float)
CreationDate (DateTime)
```

-----

#### **Additions based on your checklist:**

  * **Observability/Logs:**
      * Use **ELK Stack (Elasticsearch, Logstash, Kibana)** for aggregating server logs.
      * Use **Prometheus + Grafana** for metrics (CPU, Memory, Request Latency).
      * *Alerting:* PagerDuty alerts if Error Rate \> 1% for 5 minutes.
  * **Resiliency:**
      * **Retry Mechanisms:** Exponential backoff on failed uploads.
      * **Rate Limiting:** Prevent DDoS attacks by limiting users to X requests per minute via Redis counters at the Gateway.
  * **Global Scale:**
      * Use a **Geo-DNS** routing policy to route users to the nearest data center.

### **Next Step**

Would you like to try this framework with a specific system of your choice, or should I deep dive into the **"Fan-out-on-write"** architecture for the feed generation?

[Table of Contents](#table-of-contents)

---
# 3 Tire Architecture

To achieve a **99.99% SLA (Four Nines)**, which allows for only **52 minutes of downtime per year**, a standard single-region 3-tier architecture is insufficient. You must implement an **Active-Active (or Active-Warm Standby) Multi-Region Architecture**.

This design leverages **Global Traffic Management**, **Multi-AZ distribution**, and **Cross-Region Replication**.

### High-Level Architecture Diagram



### Architectural Components & Communication

Here is the detailed breakdown of each component, how they communicate, and their role in resilience.

---

#### 1. Global Traffic Manager (DNS / GTM)
* **Role:** This is the entry point. It uses **Geo-DNS** and **Latency-Based Routing** to direct users to the closest available region (e.g., US-East vs. EU-West). It also acts as the primary failover mechanism.
* **Communication:** It does not handle application traffic directly. Instead, it responds to DNS queries from the user's client. It communicates with the Regional Load Balancers via **Health Checks** (pings/HTTP probes) every 10–30 seconds.
* **Resiliency:** If Region A fails (returns unhealthy checks), the GTM updates the DNS records to remove Region A IPs. The TTL (Time To Live) is set low (e.g., 60 seconds) so client traffic shifts to Region B almost immediately.
* **Protocols:** DNS (UDP/53), HTTPS (Health Checks).

#### 2. Content Delivery Network (CDN) & WAF
* **Role:** Sits at the Edge (Points of Presence globally). It caches static assets (images, CSS, JS) from the Web Tier to reduce load on the backend. The Web Application Firewall (WAF) filters malicious traffic (SQLi, XSS, DDoS) before it hits the origin.
* **Communication:** The User connects to the CDN via HTTPS. The CDN communicates with the origin (your Regional Load Balancers) only on a **Cache Miss** or for dynamic content. It uses persistent TCP connections to the origin to reduce handshake overhead.
* **Resiliency:** CDNs are naturally distributed. If one Edge location goes down, Anycast routing shifts the user to the next closest POP. If the Origin is down, the CDN can serve a "Stale-While-Revalidate" version of the site to keep the UI functional.

#### 3. Regional Load Balancers (L7 - Application Layer)
* **Role:** The gateway to the specific region. It distributes incoming traffic across multiple Availability Zones (AZs) to ensure no single data center failure stops the app. It performs SSL Termination to offload CPU work from the application servers.
* **Communication:** Receives HTTPS requests from the CDN/GTM. It talks to the Web/App Tier (Internal Private Subnet) via HTTP/2 or gRPC. It maintains a **Connection Pool** to backend instances to ensure low latency. It strictly polls backend instances via `/health` endpoints.
* **Resiliency:** It employs **Cross-Zone Load Balancing**. If AZ-A goes offline, the Load Balancer instantly detects the timeout and routes 100% of the traffic to instances in AZ-B and AZ-C.

#### 4. Compute Tier (Web & App Auto-Scaling Groups)
* **Role:** Stateless servers running your application logic. These are deployed in **Auto-Scaling Groups (ASG)** spanning at least 3 Availability Zones per region. They are ephemeral—servers are created and destroyed based on CPU/RAM load.
* **Communication:** The Web Tier talks to the App Tier, and the App Tier talks to the Database. This communication should be protected by **Mutual TLS (mTLS)** for security. They use **Connection Pooling** to the database to prevent exhausting DB connections during traffic spikes.
* **Resiliency:** If an application crashes (Software Failure), the local health check fails, and the ASG terminates the instance and spins up a fresh one. If an entire Hardware Rack fails, the instances in the other AZs handle the load while the ASG provisions new capacity in healthy zones.

#### 5. Data Tier (Global Database & Caching)
* **Role:** The source of truth. For 99.99%, you typically use a setup like **Amazon Aurora Global Database** or **CockroachDB**. It consists of a Primary Writer in one region and Read Replicas in other regions. A distributed cache (Redis/Memcached) sits in front to absorb read traffic.
* **Communication:** The App Tier talks to the Cache (millisecond latency). On a miss, it hits the DB. The DB handles **Cross-Region Replication** asynchronously (usually storage-layer replication) to ensure data written in US-East exists in EU-West within < 1 second.
* **Resiliency:**
    * *Read Replica Promotion:* If the Master Region fails, a Cross-Region Read Replica is promoted to Master (RTO < 1 min).
    * *Cluster Endpoint:* The application uses a generic DNS endpoint (e.g., `db-writer.service`) which automatically points to the new Master after failover.

---

### Failure Scenarios & Recovery Strategies

To guarantee the SLA, the system must handle the following failures automatically:

#### 1. Application / Process Failure (Local)
* **Scenario:** A memory leak causes the Java/Node.js process on `Server-A` to crash.
* **Recovery:** The Regional Load Balancer misses 2 consecutive health checks. It stops sending traffic to `Server-A`. The Auto-Scaling Group detects the EC2/VM is "Unhealthy," terminates it, and launches a new one.
* **Impact:** Zero user impact (other servers absorb load).

#### 2. Availability Zone (Hardware/Datacenter) Failure
* **Scenario:** A fire or power outage takes out `Zone-A` in the US-East region.
* **Recovery:** The Regional Load Balancer detects all nodes in Zone-A are unreachable. It shifts traffic to Zone-B and Zone-C. The Database (Multi-AZ) detects the Primary writer in Zone-A is gone and performs a **standby failover** to Zone-B (sync replication guarantees zero data loss).
* **Impact:** < 30 seconds of write errors; reads continue uninterrupted.

#### 3. Region Failure (Network/Disaster)
* **Scenario:** A fiber cut isolates the entire US-East region.
* **Recovery:**
    1.  **Detection:** Global Traffic Manager (GTM) health checks to US-East fail.
    2.  **Traffic Shift:** GTM updates DNS to route all global traffic to the **Secondary Region (EU-West)**.
    3.  **Data Failover:** The Database in EU-West is promoted from "Read-Only" to "Writer" (Cross-Region Promotion).
* **Impact:** Users experience high latency (routing to Europe) and potentially 1-2 minutes of downtime during the DNS propagation and DB promotion.

### SLA Calculation (The Math)

To prove this architecture meets 99.99%:
* **Region Availability:** AWS/Azure standard Region SLA is 99.99%.
* **Probability of Simultaneous Region Failure:**
    $P(\text{fail}) = (1 - 0.9999) \times (1 - 0.9999) = 0.00000001$
* **Theoretical Availability:** $1 - 0.00000001 = 99.999999\%$
* However, effective SLA is limited by the **Failover Time (RTO)** and **Software Bugs**. With a properly configured automatic DNS failover and DB promotion, achieving the required **52 minutes of allowed downtime** per year is well within reach using this Multi-Region design.

[Table of Contents](#table-of-contents)

---

# API Gateway role

In a robust system design (especially Microservices or Service-Oriented Architecture), the **API Gateway** sits between your **Load Balancer** and your **Backend Services**.

Think of it this way:
* **Load Balancer (Traffic Cop):** "I distribute traffic evenly to keep servers alive."
* **API Gateway (Receptionist):** "I check your ID, make sure you aren't asking for too much, and guide you to the exact room you need."

### The Placement

In the architecture flow we discussed earlier, the API Gateway comes into the picture **immediately after the Regional Load Balancer** and **before the Application Servers**.



[Image of API Gateway Architecture Pattern]


### Why do we need it here? (The 5 Core Responsibilities)

We insert the Gateway at this specific point to offload "Cross-Cutting Concerns" from your application code. Your developers should focus on business logic (e.g., "calculate tax"), not infrastructure logic.

1.  **Authentication & Authorization (Security):**
    * Instead of every single microservice (User Service, Order Service, Inventory Service) implementing logic to validate a JWT or check a Session ID, the **Gateway does it once**.
    * It blocks unauthenticated requests before they ever touch your backend servers.

2.  **Rate Limiting & Throttling (Protection):**
    * The Gateway tracks how many requests User A has made.
    * If User A exceeds 100 requests/minute, the Gateway returns `429 Too Many Requests` instantly. This protects your database from being DDOS’d by a single script.

3.  **Request Routing (The Router):**
    * The client just knows `api.facebook.com`.
    * The Gateway looks at the path:
        * `/api/v1/feed` $\rightarrow$ routes to **NewsFeed Service**.
        * `/api/v1/friend` $\rightarrow$ routes to **Graph Service**.
        * `/api/v1/image` $\rightarrow$ routes to **Media Service**.

4.  **Protocol Translation:**
    * Your internal services might speak fast binary protocols like **gRPC** or **Thrift** for performance.
    * Web browsers speak **HTTP/JSON**.
    * The Gateway translates JSON requests from the outside into gRPC for the inside.

5.  **Response Aggregation (GraphQL / BFF Pattern):**
    * Sometimes a user asks for "Profile + Recent Posts + Friend Count".
    * Instead of the mobile app making 3 separate calls over the internet (slow), it makes 1 call to the Gateway.
    * The Gateway calls all 3 internal services, stitches the JSON together, and sends back one response.

### Updated Flow with Gateway

1.  **User** sends HTTPS request.
2.  **Global DNS** routes to nearest Region.
3.  **Regional Load Balancer** accepts connection and forwards to the **API Gateway Cluster**.
4.  **API Gateway**:
    * Decrypts SSL.
    * Validates User Token (Auth).
    * Checks Rate Limit (Redis).
    * Routes to **Order Service**.
5.  **Order Service** processes and returns data.

### Failure & Resilience

Since the Gateway is a "Choke Point" (all traffic must pass through it), it is high risk.
* **How to make it resilient?** You never run a single Gateway instance. You run a **Gateway Cluster** (e.g., 3+ nodes of Kong, Nginx, or AWS API Gateway) behind the Regional Load Balancer.
* **Circuit Breaking:** The Gateway can detect if the "Order Service" is down. Instead of letting requests pile up and hang, the Gateway instantly returns a "Service Unavailable" error or a cached fallback response, protecting the rest of the system.

[Table of Contents](#table-of-contents)

---
# Arora db fault tolerance

The above architecture relies heavily on **Amazon Aurora's** built-in fault tolerance mechanisms to achieve high availability for the core **Social GraphDB**.

Aurora is not just a database software on a server; it is a **Distributed Storage System** designed for fault tolerance.

Here is how Aurora handles faults in this specific architecture.

---

### **1. The Foundation: Aurora Distributed Storage (The 6-Way Replica)**

The single most important concept is that Aurora decouples compute from storage. The data does not live on the EC2 instance running the MySQL process.

* **Data is Replicated 6 Times:** When the `Post Service` writes a new post, Aurora writes that data to **6 independent storage nodes** spread across **3 Availability Zones (AZs)** in a single region (e.g., US-East).
* **Quorum Consistency:** A write is considered successful only after it is acknowledged by at least **4 out of 6** storage nodes. A read is successful if it gets data from **3 out of 6** nodes.

**Fault Tolerance Outcome:** This storage layer can handle the loss of:
* An entire Availability Zone (AZ).
* Up to **2 storage nodes** without impacting write availability.
* Up to **3 storage nodes** without impacting read availability.

---

### **2. Compute Instance Failure (Auto-Recovery)**

* **Scenario:** The primary writer EC2 instance in US-East crashes (e.g., hardware failure).
* **Detection:** Aurora's control plane detects the loss of heartbeat.
* **Recovery:**
    * Aurora automatically spins up a new compute instance.
    * Because the storage is shared and independent, the new instance simply attaches to the existing data volume.
    * The new instance is usually up and running in **under 60 seconds**.
* **Architecture Impact:** The `Graph Service` and `Post Service` will experience dropped connections for less than a minute. Their connection pools will automatically reconnect to the new instance.

---

### **3. Availability Zone Failure (Rapid Failover)**

* **Scenario:** A power outage takes down the entire `us-east-1a` data center where the primary writer was running.
* **Prerequisite:** Our architecture deploys at least one **Aurora Read Replica** in a different AZ (e.g., `us-east-1b`).
* **Recovery:**
    1.  Aurora detects the primary is down.
    2.  It instantly promotes the Read Replica in `us-east-1b` to become the new Writer.
    3.  The cluster DNS endpoint is automatically updated to point to the new IP.
* **Architecture Impact:** This failover is faster than an instance restart, typically taking **30 seconds or less**. The services continue writing to the new zone automatically.

---

### **4. Region Failure (Cross-Region Resilience)**

* **Scenario:** The entire US-East region is offline.
* **Mechanism:** **Aurora Global Database** (as shown by the "Async Cross-Region Replication" arrow in the diagram).
* **How it works:**
    * The storage layer in US-East automatically replicates all data changes to the storage layer in the EU region.
    * This is storage-level replication, so it has **very low latency (typically < 1 second lag)** and zero impact on the primary's compute resources.
* **Recovery (DR):**
    * If US-East fails, an operator (or automated script) issues a command to promote the EU-West cluster to become a standalone Primary.
    * Route 53 fails over global traffic to Europe.
    * The EU `Graph Service` now writes to its local, newly promoted Aurora writer.
* **Fault Tolerance Outcome:** The system survives the loss of a continent. We might lose the last second of data that was in flight, but the service remains operative.

---

### **Summary of Aurora Fault Tolerance in this Architecture**

| Failure Type | Aurora Healing Mechanism | Impact on Architecture | Recovery Time (RTO) |
| :--- | :--- | :--- | :--- |
| **Storage Disk/Node Failure** | **6-way Replication** across 3 AZs. | None. Transparent self-healing. | Zero |
| **Database Instance Crash** | **Auto-restart** compute node on shared storage. | Brief connection errors. | < 1 Minute |
| **Entire Zone (AZ) Failure** | **Automatic Promotion** of a Read Replica in another AZ. | Brief write pause. Reads continue from other replicas. | < 30 Seconds |
| **Entire Region Failure** | **Global Database** Cross-Region Replication. | Requires manual/scripted promotion of Secondary Region. | Minutes |

[Table of Contents](#table-of-contents)

---

# Cache with fault tolerance

The implementation of fault tolerance in Amazon ElastiCache (Redis) relies on Replication Groups and Multi-AZ (Availability Zone) deployment. This ensures that if a cache node fails, another is ready to take its place immediately without manual intervention.

The Fault-Tolerant Caching Architecture
Instead of a single Redis node, we deploy a Replication Group.

Primary Node (Read/Write): There is one primary node that handles all write operations and can also serve reads.

Read Replicas (Read-Only): We attach up to 5 read replicas to the primary. These nodes asynchronously replicate data from the primary.

Multi-AZ Deployment: Crucially, the Primary node and its Read Replicas are deployed across different Availability Zones (AZs) within the same region. For example, the Primary in us-east-1a, Replica 1 in us-east-1b, and Replica 2 in us-east-1c.

How Failover Works (Automatic Recovery)
The ElastiCache service actively monitors the health of the primary node.

Failure Detection: If the primary node fails (e.g., due to hardware failure or a full AZ outage), ElastiCache detects the loss of heartbeat.

Automatic Failover:

The service selects the replica with the lowest replication lag.

It promotes that replica to become the new Primary.

It automatically updates the cluster's DNS endpoint to point to the IP address of the new primary.

Client Transparency: Your application doesn't need to change its configuration. It continues trying to connect to the same DNS endpoint, and within a few seconds (typically under a minute), it automatically connects to the new primary node

![Elastic cache fault tolerance](./images/elastic-cache-fault-tolerance.png)

[table of Contents](#table-of-contents)

---

# Security

This is a fundamental requirement. At a Staff/Principal level, security is not an add-on plugin; it is baked into every layer of the architecture from day one.

We will implement a **Defense-in-Depth** strategy, adopting a **Zero Trust** mindset. We assume the network is hostile and verify every interaction.

Here is the security implementation broken down by architectural layer, using the AWS services from our previous design.

---

### **High-Level Security Architecture View**

Before diving into details, here is how security controls are applied across the flow.



[Image of Layered Security Architecture Diagram showing Edge, App, Network, and Data security layers]


---

### **Layer 1: Edge Security (The Perimeter)**

**Goal:** Stop attacks before they reach our servers. Filter out noise, bots, and DDoS attempts.

1.  **DDoS Protection (AWS Shield Advanced):**
    * We enable Shield Advanced on Route 53 and CloudFront. This provides near-instant mitigation against massive Layer 3/4 (network/transport) volumetric attacks without us lifting a finger.

2.  **Web Application Firewall (AWS WAF):**
    * We attach WAF rules to both **CloudFront** (for global protection) and the Regional **API Gateways** (for specific API protection).
    * **Rules implemented:**
        * **AWS Managed Rules:** Core rule sets (OWASP Top 10 protection against SQL Injection, XSS).
        * **Rate Limiting:** E.g., "Block IPs sending > 2000 requests per 5 minutes" to stop brute-force login attacks or scraping.
        * **Geo-Blocking:** If Starbucks doesn't operate in certain high-risk countries, block traffic from those ISO codes at the edge.

3.  **Secure Content Delivery (CloudFront OAC):**
    * The S3 buckets holding static assets must **not** be public.
    * We implement **Origin Access Control (OAC)**. This ensures that S3 only accepts requests that are signed by CloudFront. If someone tries to access the S3 URL directly, they get `403 Forbidden`.

---

### **Layer 2: Application Security (AuthN & AuthZ)**

**Goal:** Ensure only legitimate users can do what they are allowed to do.

1.  **Authentication (AuthN - "Who are you?"):**
    * We do not roll our own crypto. We use **Amazon Cognito** (or integrate with an external OIDC provider like Auth0/Okta for enterprise SSO).
    * The mobile app authenticates against Cognito and receives a **JWT (JSON Web Token)**.

2.  **Authorization (AuthZ - "What can you do?"):**
    * The **API Gateway** is the enforcement point. It uses a **Lambda Authorizer** to validate the incoming JWT signature on every request.
    * It checks **Scopes/Claims** inside the token. (e.g., A barista user has scope `order:write`, a regular customer has scope `order:read`).
    * If the token is invalid or lacks the scope, API Gateway returns `401 Unauthorized` or `403 Forbidden` before the request hits the backend.

---

### **Layer 3: Network & Compute Isolation (The "Zero Trust" Network)**

**Goal:** Minimizing the "Blast Radius". If one server is compromised, it shouldn't be able to talk to the database or other servers freely.

1.  **VPC Design (Segmentation):**
    * **Public Subnets:** Only contain the ALBs and NAT Gateways.
    * **Private Subnets:** Contain App Servers, Redis, and Aurora. *These have no routes to the internet.* They patch via NAT Gateways using tightly scoped egress rules.

2.  **Security Groups (The "Instance Firewall"):**
    * We use the principle of least privilege chaining.
    * **ALB SG:** Inbound 443 from `0.0.0.0/0`. Outbound 8080 to `App SG`.
    * **App SG:** Inbound 8080 **ONLY from `ALB SG ID`**. (Crucial: Don't allow IP ranges, allow SG IDs).
    * **Database SG:** Inbound 3306/5432 **ONLY from `App SG ID`**.

3.  **Secure Administration:**
    * Port 22 (SSH) is **never** open to the internet.
    * We use **AWS Systems Manager (SSM) Session Manager** for shell access to EC2 instances. This is managed via IAM policies and logged to CloudTrail, requiring no open inbound ports.

---

### **Layer 4: Data Security (Protecting the Crown Jewels)**

**Goal:** Data must be unreadable if physical disks are stolen or network traffic is snooped.

1.  **Encryption at Rest (Using AWS KMS):**
    * We use **Customer Managed Keys (CMKs)** in KMS for auditability.
    * **Aurora:** Storage encryption enabled at cluster creation.
    * **ElastiCache Redis:** At-rest encryption enabled.
    * **S3:** Bucket policies enforce SSE-KMS (Server-Side Encryption).
    * **EBS Volumes:** The root volumes of EC2 instances are encrypted.

2.  **Encryption in Transit:**
    * **TLS 1.2/1.3 everywhere.**
    * Client -> CloudFront (HTTPS).
    * CloudFront -> ALB (HTTPS).
    * ALB -> App Server (mTLS or HTTPS).
    * App Server -> Aurora (Enforce SSL mode on the DB connection driver).
    * App Server -> Redis (In-transit encryption enabled).

3.  **Secrets Management:**
    * Database credentials, API keys for 3rd parties (e.g., payment gateways) are **never hardcoded** in git.
    * We use **AWS Secrets Manager**.
    * The EC2 instance has an IAM role attached that allows it to call `GetSecretValue` at runtime to retrieve the DB password. Secrets Manager automatically rotates these passwords every 30 days.

---

### **Summary Table of Security Controls**

| Layer | AWS Service / Feature | Security Function |
| :--- | :--- | :--- |
| **Edge** | WAF & Shield Advanced | DDoS blocking, Rate limiting, Geo-blocking. |
| **Edge** | CloudFront OAC | Secures S3 origin so it's not public. |
| **App** | Cognito & API Gateway | Authentication (JWT) and Authorization (Scopes). |
| **Network**| VPC Security Groups | Least privilege firewalling between tiers using SG IDs. |
| **Network**| Systems Manager (SSM) | Secure, no-SSH access to servers for admins. |
| **Data** | KMS (Key Management Service)| Centralized management of encryption keys. |
| **Data** | Secrets Manager | Rotating DB credentials at runtime; removing secrets from code. |
| **Audit** | CloudTrail & GuardDuty | Logging all API actions and detecting anomalous behavior (threat detection). |

[Table of Contents](#table-of-contents)

---

# Disaster Recovery Plan

This is a definitive "Staff Engineer" level question. A Disaster Recovery (DR) plan at this level is not just about having backups; it is a comprehensive strategy involving architecture, business alignment, automation, and rigorous testing.

Here is my DR plan for the global, multi-region architecture we have designed.

---

### **The Core Philosophy**

Our DR strategy is built on the premise that **failure is inevitable**. We do not design to prevent failure entirely; we design to recover from it rapidly and, ideally, transparently.

We shift from a traditional "Disaster Recovery" mindset (reacting to an event) to a **"Cyber Resilience"** mindset (continuous operation despite adversity).

---

### **1. Defining Success: The Metrics (RTO & RPO)**

Before talking about technology, we must align with the business on tolerances. We categorize systems into tiers.

| Tier | Service Example | Recovery Point Objective (RPO) - Max Data Loss | Recovery Time Objective (RTO) - Max Downtime | Strategy |
| :--- | :--- | :--- | :--- | :--- |
| **Tier 0 (Critical)** | Core Transaction DB (Aurora), Payment Processing, Login. | **Near Zero (< 5 seconds)** | **< 15 Minutes** | **Warm Standby / Active-Passive** across regions. Automated failover logic. |
| **Tier 1 (Important)**| User Profiles, Order History (DynamoDB). | **< 15 Minutes** | **< 4 Hours** | Cross-Region Replication with manual promotion if needed. |
| **Tier 2 (Analytics)**| Data Warehouse (Redshift), Historical Logs (S3). | **< 24 Hours** (Last daily backup) | **< 24 Hours** | Restore from cold backups in S3 Glacier. |

*For the remainder of this plan, I will focus on protecting our **Tier 0** services.*

---

### **2. Architectural Prerequisites**

The foundation of our DR plan is baked into the architecture itself. We cannot scramble to set these up during an incident.

1.  **Infrastructure as Code (IaC):**
    * Every single component (VPCs, Subnets, ALBs, Security Groups) is defined in **Terraform** or **AWS CloudFormation**.
    * *Why:* If a region is completely wiped out, we do not manually rebuild it via the console. We execute scripts to re-hydrate the infrastructure in a new region identically.

2.  **Active Data Replication:**
    * **Database:** Amazon Aurora Global Database is running with asynchronous replication from Primary (e.g., US-East) to Secondary (e.g., EU-West). Latency is typically < 1 second.
    * **Object Storage:** S3 buckets holding critical assets have **Cross-Region Replication (CRR)** enabled to a DR region bucket with **Object Lock** (WORM) enabled to prevent accidental deletion in both locations.

3.  **Global Traffic Management:**
    * **Amazon Route 53** is configured with health checks pointing to our regional entry points (API Gateways/ALBs). It is the ultimate switch to redirect global traffic.

---

### **3. Execution Playbooks (The Scenarios)**

We have distinct playbooks for different levels of failure.

#### **Scenario A: Availability Zone (AZ) Failure (The "Non-Event")**
* *Example:* A data center fire in `us-east-1a`.
* **Response:** **Automatic.**
    * **Compute:** Auto Scaling Groups detect unhealthy instances in AZ-a and launch replacements in AZ-b and AZ-c.
    * **Database:** Aurora automatically detects the Primary Writer failure in AZ-a and promotes a Read Replica in AZ-b to be the new Writer (approx. 30s pause in writes).
* **RTO:** < 1 minute. **RPO:** Zero.

#### **Scenario B: Full Region Failure (The "Disaster")**
* *Example:* A catastrophic network event isolates the entire US-East region.
* **Response:** **Automated Detection, Scripted Execution.**

    * **Step 1: Traffic Shift (Immediate Mitigation)**
        * Route 53 health checks fail for US-East.
        * DNS automatically updates to route 100% of global traffic to the EU-West Load Balancers.
        * *Current State:* Users are hitting Europe, but the application is Read-Only because the DB Master is down in the US.

    * **Step 2: Data Layer Failover (The Critical Path)**
        * An on-call engineer (or automated Lambda) executes the "Promote Region" runbook.
        * Command is issued to Aurora: `detach-from-global-cluster` on the EU-West replica.
        * Command is issued: `promote-to-primary` on the EU-West cluster.
        * *Result:* EU-West is now accepting writes.

    * **Step 3: Application Reconfiguration & Scale-Up**
        * The application in EU-West automatically detects the local DB is now writable (via cluster endpoint DNS resolution).
        * **Crucial Step:** The EU-West region was likely running at 50% capacity (Warm Standby). The Auto Scaling Groups will immediately see a spike in CPU as it takes global load. We rely on aggressive scaling policies or pre-warming scripts to handle this surge.

* **RTO:** < 15 minutes (mostly DNS propagation and DB promotion time).
* **RPO:** < 1-5 seconds of data trapped in the dead US region.

#### **Scenario C: Logical Corruption / Ransomware (The "Worst Case")**
* *Example:* A bad deployment drops a critical table, or an attacker encrypts the database. High availability mechanisms immediately replicate this corruption globally.
* **Response:** **Manual Restoration.**

    * **Immediate Action:** "Stop the bleeding." Shut down write access to the database to prevent further corruption.
    * **Option 1 (Fastest): Aurora Backtrack.** If configured, we dial back the database state to a time 5 minutes before the event. This takes seconds.
    * **Option 2 (Robust): Point-in-Time Recovery (PITR).** We spin up a *new* Aurora cluster from the continuous backups at a timestamp just prior to the corruption.
    * **Option 3 (Nuclear): AWS Backup Vault.** If the live backups are also compromised, we restore from our isolated, account-locked Backup Vault where snapshots are immutable for 30 days.

---

### **4. Testing and Validation (Game Days)**

A plan that isn't tested is a hallucination. We validate this plan through **quarterly Game Days**.

1.  **Simulated Failure:** In production (during off-peak hours) or a prod-like staging environment, we deliberately simulate failures.
    * *Chaos Monkey:* Randomly terminate EC2 instances to test ASG recovery.
    * *Region Cut:* We block network traffic to a region's load balancers to trigger Route 53 failover.
2.  **Measure Results:** We time how long it takes for alerts to fire, how long for traffic to shift, and how long until the database is writable again. We compare actuals vs. our target RTO/RPO.
3.  **Human Element:** We test if the on-call engineers know where the runbooks are, if they have the right access permissions, and if the communication channels (e.g., PagerDuty to Slack bridge) work when corporate VPNs might be down.


[Table of Contents](#table-of-contents)

---

# Backup Strategy

Here is your content formatted cleanly in **Markdown**:

---

# 🚀 Modern PB-Scale Backup Strategy (AWS)

At **petabyte scale**, “backing up” data is no longer about taking a nightly snapshot of a single database.
The data volume is too large, and the downtime required for a consistent snapshot is unacceptable.

Instead, the approach combines:

* **Continuous Data Protection (CDP)**
* **Point-in-Time Recovery (PITR)**
* **Tiered Archival**

Below is the strategy, broken down by **data type** and **AWS services**.

---

## 1. The Strategy: Modern Backup Principles for PB Scale

### 🔹 Never Stop the World

Backups must not impact the performance of the live production system.
We use **asynchronous mechanisms** like streaming logs or storage-level snapshots.

### 🔹 Granular Recovery

We must be able to restore **a single table or even a single row** without restoring the entire PB dataset.

### 🔹 Regulatory Retention

We must keep data for **X years** for compliance (GDPR, HIPAA, financial regulations) in **immutable, tamper-proof formats**.

### 🔹 Cost Efficiency

We cannot afford to keep 5 years of backups on high-performance SSDs.
We must use **cheaper storage tiers** as data ages.

---

## 2. Retention Policy: How Long Data Stays on Running Services

This is a **business decision** balanced by cost vs performance.

A typical **tiered retention policy** for PB-scale workloads looks like:

| Tier     | Where Data Lives                                   | Retention Period | Purpose                                                |
| -------- | -------------------------------------------------- | ---------------- | ------------------------------------------------------ |
| **Hot**  | Live Production DB (Aurora / DynamoDB / Cassandra) | 30–90 days       | Instant access for user queries; active business logic |
| **Warm** | Data Warehouse / Lakehouse (Redshift / Athena)     | 1–2 years        | BI analytics, monthly reporting, YoY analysis          |
| **Cold** | Archival Storage (S3 Glacier)                      | 5–10+ years      | Compliance, legal hold, last-resort disaster recovery  |

---

### Implementation Mechanisms

#### 🔸 TTL (Time-To-Live)

For DynamoDB or Cassandra, configure TTL on records.
The database engine automatically deletes data older than the threshold (e.g., 90 days).

#### 🔸 S3 Lifecycle Policies

For data stored in S3, configure automatic transition:

```
S3 Standard → S3 Standard-IA → S3 Glacier → Glacier Deep Archive
```

This reduces storage cost as the data ages.

---

## 3. AWS Services for PB-Scale Backup & Recovery

Different data types require different approaches.

---

### A. Transactional Relational Data (Amazon Aurora)

**Challenge:** Backing up a 64TB Aurora cluster without locking tables.

**AWS Solution:**
✔️ Aurora Automated Backups
✔️ Continuous Redo Log Streaming
✔️ Aurora Backtrack

#### How It Works

* Aurora storage is **continuous**.
* AWS automatically streams **redo logs to S3**.
* **Daily storage-level snapshots** are taken.
* These operations **do not impact compute performance**.

#### Point-in-Time Recovery (PITR)

Restore a **new cluster to any second** within the retention window (up to 35 days).

#### Aurora Backtrack

Instant rollback from user mistakes without full restore—e.g.:

* `DROP TABLE`
* Corrupted ingestion pipelines
* Bad batch writes

Backtrack rewinds the database state **within seconds**.

#### Long-Term Retention

For beyond 35 days, use **AWS Backup** to export snapshots to cold storage tiers.

---

### B. NoSQL / High-Velocity Data (DynamoDB)

**Challenge:** Backup a table handling **100k writes/s** without consuming WCUs.

**AWS Solution:**
✔️ DynamoDB PITR
✔️ On-demand Backup
✔️ AWS Backup for compliance

#### PITR

* Once enabled, DynamoDB is backed up **continuously**.
* Restore to **any second in the last 35 days**.
* **No impact on provisioned throughput** (WCUs/RCUs).

#### AWS Backup Integration

For compliance workloads:

* Create **immutable snapshots**
* Schedule automated backups
* Tier snapshots to **low-cost cold storage**

---

### C. File & Object Data (Amazon S3 — Data Lake)

**Challenge:** You don’t “backup” S3 like a database; it already provides **11 nines durability (99.999999999%)**.
The real risk is **accidental deletion or overwrite**.

**AWS Solution:**
✔️ Versioning
✔️ Cross-Region Replication
✔️ Object Lock (WORM)

#### S3 Versioning

Every overwrite or delete creates a **new version**.
Recovery is as simple as restoring an older version.

#### S3 Cross-Region Replication (CRR)

Asynchronously replicate objects to a bucket in **another region** for disaster recovery.

#### S3 Object Lock (WORM)

Write-Once-Read-Many mode:

* Prevents deletion or overwrite
* Even root account cannot modify
* Required for legal compliance (e.g., 7-year retention)

---

If you’d like, I can format this into a **presentation slide deck**, **Confluence page**, or **architecture diagram**.

[Table of Contents](#table-of-contents)

---

# Arora db failure

This is a pivotal question in any system design interview. Handling database failure is the single most critical aspect of maintaining high availability.

When designing with Amazon Aurora, the strategy for handling failure depends entirely on the **"Blast Radius"** of the failure. Aurora has different self-healing mechanisms for different levels of outages.

Here is how we handle Aurora DB failures, organized by severity from least severe to catastrophic.

---

### **Core Concept: Separation of Compute and Storage**

Before diving into scenarios, remember this key architectural advantage of Aurora: **Compute and Storage are decoupled.**

* **Storage is inherently durable:** Your data is replicated 6 times across 3 Availability Zones (AZs) in the storage layer. Even if a compute instance completely disappears, the data is safe.
* **Compute is ephemeral:** The database instances (the "servers" you see) are just processing heads sitting on top of that storage.

This separation is why Aurora recovers faster than traditional RDS.

---

### **Scenario 1: A Single Compute Instance Fails (Minor)**

**Situation:** The underlying EC2 host running the Primary Writer instance crashes due to a hardware fault, or the database process itself restarts due to a memory issue.

**Handling Mechanism: Automatic Instance Recovery**

1.  **Detection:** The Aurora control plane performs continuous health checks. It detects the Writer instance is unresponsive.
2.  **Recovery Action:**
    * Aurora attempts to restart the database process on the existing host.
    * If the host is dead, Aurora terminates the instance and spins up a *new* compute instance.
    * Because the storage is shared, the new instance simply attaches to the existing storage volume. No data restoration is needed.
3.  **Impact (RTO):** Typically **under 60 seconds**.
4.  **Client Side Handling:** The application will see dropped connections. It must have **robust retry logic with exponential backoff** to reconnect automatically once the new instance is up.

---

### **Scenario 2: An Entire Availability Zone (AZ) Fails (Major)**

**Situation:** A fire or power event takes out an entire data center (e.g., `us-east-1a`). Your Primary Writer was in that AZ.

**Handling Mechanism: Automatic Multi-AZ Failover**

1.  **Prerequisite:** You must deploy Aurora with at least one **Read Replica** in a different AZ within the same region.
2.  **Detection:** The cluster detects the Primary Writer is unreachable.
3.  **Recovery Action (Promotion):**
    * Aurora identifies the "healthiest" Read Replica in a different AZ (e.g., `us-east-1b`).
    * It automatically **promotes** that Replica to be the new Primary Writer.
    * Aurora automatically flips the DNS entry of the **Cluster Endpoint** to point to the new Writer's IP address.
4.  **Impact (RTO):** Typically **30 to 60 seconds**.
5.  **Client Side Handling:** The application doesn't need to change its configuration because it uses the abstract Cluster Endpoint DNS name. It just needs retry logic to handle the brief window during DNS propagation.

---

### **Scenario 3: An Entire AWS Region Fails (Catastrophic)**

**Situation:** A massive event (like the 2012 Hurricane Sandy or a rare Route 53 global issue) makes the entire primary region (e.g., US-East) unavailable. This is where **Aurora Global Database** comes in.

**Handling Mechanism: Managed Cross-Region Failover**

*Unlike within-region failover, cross-region failover is typically a manual or scripted process initiated by you, not fully automatic by AWS.*

1.  **Prerequisite:** Aurora Global Database is configured with a Secondary region (e.g., EU-West).
2.  **Detection:** Your external monitoring (or Route 53 health checks) indicates the primary region is down.
3.  **Recovery Action (Promotion):**
    * **Step 1 (Detach):** You issue an AWS CLI command or API call to remove the Secondary cluster (EU-West) from the Global Database.
    * **Step 2 (Promote):** You promote the EU-West cluster to become a standalone, Read/Write primary cluster.
    * **Step 3 (Re-route Traffic):** Your Global Geo-DNS (Route 53) fails over traffic to the EU-West application stack.
    * **Step 4 (Re-configure App):** The application servers in EU-West, which previously pointed to a read-only endpoint, must update their configuration to point to the new, local Read/Write endpoint.
4.  **Impact (RTO/RPO):**
    * **RTO (Time to Recover):** Can take several minutes, depending on how fast your operations team or automation scripts trigger the failover.
    * **RPO (Data Loss):** Because cross-region replication is asynchronous, you might lose the last few milliseconds or seconds of transaction data that were committed in US-East but hadn't yet shipped to EU-West before the crash.

---

### **Scenario 4: Logical Data Corruption (User Error)**

**Situation:** A developer accidentally runs `DROP TABLE users;` in production. The architecture is perfectly healthy, but the data is gone.

**Handling Mechanism: Aurora Backtrack or Point-in-Time Recovery (PITR)**

This is not an infrastructure failure, but a data failure. High availability won't save you here; in fact, it will immediately replicate the deletion to all regions.

1.  **Action:**
    * **Aurora Backtrack (If enabled):** You can "rewind" the DB cluster state to a specific time (e.g., 5 minutes ago) almost instantly without restoring from a backup.
    * **PITR (Standard):** You restore a new DB cluster from continuous backups to a specific second before the error occurred.
2.  **Impact:** The application is down while the data is being restored or rewound.

---

### **Summary Table for Interview**

| Failure Scenario | Healing Mechanism | Estimated Recovery Time (RTO) | Potential Data Loss (RPO) |
| :--- | :--- | :--- | :--- |
| **Instance Crash** | Auto-restart compute node on shared storage. | < 1 Minute | Zero |
| **AZ Failure** | Automatic promotion of a Read Replica in another AZ. | < 1 Minute | Zero |
| **Region Failure** | Manual/Scripted promotion of Secondary Region in Global DB. | Minutes (depends on ops speed) | Low (< 1 second) |
| **User Error (Data Deletion)** | Backtrack or Restore from Backup (PITR). | Minutes to Hours | Data since last backup/checkpoint |

[Table of Contents](#table-of-contents)

---

# Explain complete architecture

This is the complete, deep-dive walkthrough of the global system architecture we have designed. We will use a concrete example: **A customer ordering a coffee via the mobile app.**

---

### **The End-to-End Workflow: "Ordering a Coffee"**

#### **1. The Edge (Global Ingress & Routing)**

* **User Action:** A user in **New York City** opens the Starbucks mobile app. The app tries to connect to `api.starbucks.com`.
* **DNS Resolution (Amazon Route 53):**
    * The DNS query hits Route 53.
    * **Geo-DNS Policy:** Route 53 detects the user's IP is in North America. It returns the IP address of the **US-East CloudFront distribution**.
    * **Health Check:** Before returning the IP, Route 53 confirms the US-East endpoint is healthy. If US-East was down (a Disaster scenario), it would automatically return the EU-West IP.
* **Content Delivery (Amazon CloudFront & WAF):**
    * The app connects to CloudFront.
    * **AWS WAF (Web Application Firewall)**, attached to CloudFront, inspects the request. It checks for malicious patterns (e.g., SQL injection attempts) and blocks bot IPs.
    * CloudFront terminates the SSL/TLS connection close to the user for speed. It forwards the validated request over the AWS backbone to the Regional Load Balancer.

#### **2. The API Layer (Security & Routing)**

* **Public Load Balancing (Application Load Balancer - ALB):**
    * The ALB receives the request from CloudFront.
    * It sits in a public subnet but its security group only allows inbound traffic from CloudFront's IP ranges.
    * It forwards the traffic to the **API Gateway**.
* **API Management (Amazon API Gateway):**
    * **Authentication:** The Gateway validates the JSON Web Token (JWT) in the request header (issued by Amazon Cognito upon login). If invalid, it returns `401 Unauthorized`.
    * **Throttling:** It checks if the user has exceeded their rate limit (e.g., 10 orders/minute).
    * **Routing:** It looks at the path `/order` and routes the request to the internal "Order Service" via the **Internal ALB**.
    * **Caching:** For read requests (like fetching the menu), the API Gateway returns a cached response instantly if available.

#### **3. The Service Layer (Business Logic & Compute)**

* **Internal Load Balancing (Internal ALB):**
    * This ALB sits in a private subnet, completely inaccessible from the internet.
    * It uses a **round-robin** algorithm to distribute traffic across healthy App Server instances in different Availability Zones (AZs).
* **Compute (Auto Scaling Group - ASG):**
    * The request lands on an EC2 instance running the "Order Service" application (Java/Go/Node.js).
    * **Scaling:** If the CPU load across the ASG exceeds 70% due to a morning rush, the ASG automatically launches new EC2 instances to handle the load.
    * **Fault Tolerance:** If the underlying hardware of this EC2 instance fails, the Internal ALB stops sending it traffic, and the ASG replaces it automatically.

#### **4. The Data Layer (Caching & Persistence)**

* **Low-Latency Lookup (Amazon ElastiCache Redis):**
    * Before creating the order, the App Server needs to validate item availability and price. It first checks the Redis cache.
    * **Strategy (Cache-Aside):** If the data is in Redis (**Cache Hit**), it's retrieved in sub-millisecond time. If not (**Cache Miss**), the app fetches it from the database and updates the cache.
* **Transactional Write (Amazon Aurora Global Database):**
    * The App Server creates the order record and writes it to the **Primary Writer endpoint** of the Aurora DB in the US-East region.
    * **Consistency:** This write is strongly consistent within the region.
    * **Replication:** Aurora's storage layer asynchronously replicates this data to the EU-West region in under a second for disaster recovery.

#### **5. The Asynchronous Event Layer (Decoupling & Processing)**

Once the order is saved to the DB, the work isn't done. We need to notify the kitchen and the user. This happens asynchronously.

* **Event Generation:** The App Server publishes an event `OrderCreated` containing the order ID to **Amazon EventBridge**.
* **Event Routing (EventBridge):**
    * EventBridge has rules to route this event to multiple targets based on its content.
* **Target 1: Kitchen Display Service (KDS):**
    * The event is pushed to an **Amazon SQS** queue.
    * The KDS application (running on its own ASG) polls this queue, picks up the order, and displays it on the screen in the correct store.
* **Target 2: User Notification:**
    * The event triggers an **AWS Lambda function**. This function calls **Amazon SNS** to send a push notification to the user's phone: "Your order is confirmed!"
* **Target 3: Analytics:**
    * The event is streamed via **Kinesis Data Firehose** into **Amazon S3** (Data Lake) for future business analysis.

---

### **Deep Dive into Key System Properties**

#### **1. Scalability**
* **Compute (App Tier):** **Horizontal Scaling** via Auto Scaling Groups. We add more servers, not bigger ones.
* **Database (Read):** We can add up to 15 Aurora Read Replicas per region to handle massive read traffic.
* **Database (Write):** Aurora Serverless v2 can automatically scale compute capacity up and down instantly based on write load.
* **Caching:** ElastiCache Redis is deployed in **Cluster Mode**, allowing it to partition data across multiple shards to scale memory and throughput endlessly.

#### **2. Fault Tolerance & High Availability (HA)**
* **No Single Point of Failure:** Every component is redundant.
* **Multi-AZ:** App Servers, NAT Gateways, ALBs, Redis nodes, and DB instances are spread across 3 AZs. The loss of an entire data center (AZ) results in only a brief blip as traffic shifts to the remaining two AZs.
* **Database Self-Healing:** If the Aurora Primary Writer fails, a Read Replica is automatically promoted to Writer in under 30 seconds without changing application config.

#### **3. Disaster Recovery (DR)**
* **Scenario:** The entire US-East region goes offline.
* **Detection:** Route 53 health checks fail.
* **Failover:**
    1.  Route 53 redirects all global traffic to EU-West.
    2.  An automated script promotes the EU-West Aurora replica to be the new Global Primary Writer.
    3.  The EU-West application stack, which was running in a scaled-down "pilot light" mode, automatically scales up to handle full global traffic.

#### **4. Security (Defense in Depth)**
* **Perimeter:** Shield Advanced protects against DDoS. WAF filters malicious web requests.
* **Network:** VPC Security Groups act as stateful firewalls, allowing only specific communication paths (e.g., ALB can talk to App SG on port 8080, but nothing else can).
* **Identity:** All API calls are authenticated via OAuth2/JWT using Cognito.
* **Data:** All data is encrypted at rest (KMS) in S3, EBS, Aurora, and ElastiCache. All data in transit is encrypted via TLS 1.2+.

#### **5. Observability (Monitoring, Logging, Tracing)**
* **Metrics (CloudWatch):** We collect CPU, memory, latency, and error rates from all services. Alarms trigger (via SNS to PagerDuty) if, for example, 5xx error rates on the ALB exceed 1% for 5 minutes.
* **Logs (CloudWatch Logs):** All application logs, API Gateway access logs, and VPC flow logs are centralized for debugging.
* **Tracing (AWS X-Ray):** Every request receives a unique trace ID at the ALB. This ID is passed through API Gateway, App Server, DB, and Lambda. We can visualize the entire call chain to pinpoint exactly where latency occurred in a slow request.

[Table of Contents](#table-of-contents)

---