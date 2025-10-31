# Cloud-Native OLTP Database Design

## 1. High-Level Architecture

**Goal:** A distributed OLTP database that ensures:

- ACID transactions  
- Horizontal scalability  
- High availability (HA) across AZs  
- Low-latency read via caching  
- Fault-tolerant storage and networking  

**Architecture Layers:**

```
Clients
│
▼
API / Query Router / Proxy Layer (SQL / gRPC)
│
▼
Distributed Transaction Coordinator
│
├── Storage Nodes (Block Storage for Hot Data)
│ ├── WAL / Logs
│ └── Data Pages
│
├── Object Storage (for cold data / backups / snapshots)
│
├── Distributed Cache (e.g., Redis, Memcached)
│
└── Cluster Management & Monitoring (Leader Election, Health)
```

---

## 2. Storage Layer Design

### a) Cloud Block Storage
- Use block storage for **hot transactional data**.
- Store **data pages**, **indexes**, and **WAL (Write-Ahead Logs)**.
- Replicate across **multiple AZs** for HA.

### b) Cloud Object Storage
- Store **cold data, snapshots, backups, and logs**.
- Supports **versioning** and **point-in-time recovery**.
- Highly scalable for distributed architecture.

### c) Distributed Cache
- Cache frequently read data at **query router** or **node level**.
- Use **consistent hashing** for horizontal scaling.
- Can be **write-through** or **read-through** for cache consistency.

---

## 3. Transaction Management

### Distributed Transactions
- Implement **2-phase commit (2PC)** or **3-phase commit (3PC)**.
- **Transaction Coordinator**:
  - **Phase 1: Prepare** — storage nodes lock resources, write tentative changes to WAL.
  - **Phase 2: Commit** — commit if all nodes respond OK, else rollback.

### Lock Management
- Use **distributed lock manager** (Raft, ZooKeeper, etcd).
- Fine-grained locks per row, key, or shard.
- Handle timeouts and deadlocks.

### ACID Guarantees
- **Atomicity:** WAL + distributed commit ensures all-or-nothing.  
- **Consistency:** Constraints enforced at storage nodes.  
- **Isolation:** MVCC or strict locks.  
- **Durability:** WAL persisted on block storage and asynchronously replicated.

---

## 4. Query Processing & Optimization
- **Query Router Layer:** Receives SQL/NoSQL queries and locates data nodes.
- **Shard Awareness:** Use hashing or range partitioning.
- **Query Optimization:**
  - Cost-based optimizer for distributed queries.
  - Pushdown predicates to storage nodes to reduce network traffic.
- **Read Optimization:**
  - Cache for hot queries.
  - Replicated read nodes in multiple AZs.

---

## 5. Replication & High Availability
- **Synchronous replication** within or across AZs.
- **Asynchronous replication** for cold or less critical data.
- **Leader-Follower Nodes:** Leader handles writes, followers serve reads.
- **Cluster Manager:** Monitors health, triggers failover, balances shards.

---

## 6. Backup & Recovery
- Continuous WAL shipping to object storage.
- Periodic snapshots for fast recovery.
- Restore:
  - Replay WALs after snapshot.
  - Restore to any AZ or region if disaster occurs.

---

## 7. Handling Transaction Failures
- **Node Crash / AZ Failure:** WAL ensures redo/undo; coordinator triggers rollback.
- **Network Partition:** Consensus (Raft) prevents split-brain; transactions fail fast.
- **Partial Commit:** Coordinator tracks 2PC state, ensures rollback of incomplete transactions.
- **Cache Invalidation:** Update or invalidate cache on commit/failure.

---

## 8. Communication & Protocols
- Use **gRPC / custom binary protocol** for low-latency node communication.
- **TLS** for secure cross-AZ transport.
- **Client-Node Communication:** Queries go through router, which handles shard lookup, transaction coordination, cache hits/misses.

---

## 9. Cloud-Native Disaggregated Architecture
- Separate **compute (query nodes)** and **storage**:
  - Storage nodes scale independently.
  - Compute nodes scale horizontally.
- Leverage **managed services** for cache and storage where possible.

---

## 10. Summary of Key Features

| Feature | Implementation |
|---------|----------------|
| Scalability | Sharding, cache, compute nodes scale independently |
| Resilience | Multi-AZ replication, leader-follower, WAL, snapshots |
| Fault Tolerance | Distributed locks, 2PC/3PC, RAFT for consensus |
| ACID | WAL, MVCC/locking, distributed commit |
| Performance | Distributed cache, query optimization, pushdown predicates |
| Cloud Storage | Block storage for hot data, object storage for cold/backup |
| HA & Disaster Recovery | Multi-AZ replication, snapshots, WAL replay |