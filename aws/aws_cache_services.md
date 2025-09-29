# AWS Caching Services

AWS provides managed caching services to improve application performance by reducing latency and offloading database workloads.

---

## 1. **Amazon ElastiCache**
Fully managed in-memory caching service supporting **Redis** and **Memcached**.

### 🔹 Features:
- In-memory data storage for fast access
- Reduces database load and improves response time
- Automatic failover, backups, and scaling
- Security: VPC, IAM, encryption in transit and at rest

### 🔹 Supported Engines:
1. **Redis**
   - Persistent or non-persistent
   - Supports replication, clustering, and pub/sub
   - Advanced data structures (lists, sets, hashes)
2. **Memcached**
   - Simple key-value caching
   - Multi-threaded, horizontally scalable
   - Best for simple caching without persistence

---

## 2. **Amazon DynamoDB Accelerator (DAX)**
- Fully managed, highly available **in-memory cache** for DynamoDB
- Reduces response times from milliseconds to microseconds
- Compatible with DynamoDB APIs; minimal code changes required
- Supports write-through caching with automatic invalidation

---

## 3. **Amazon CloudFront (Edge Caching)**
- Global content delivery network (CDN) with caching at edge locations
- Caches static and dynamic content closer to users
- Reduces latency and offloads origin servers
- Supports caching rules, TTLs, and invalidation

---

## 4. **AWS Global Accelerator + Caching**
- Not a direct cache but improves performance by routing traffic to optimal AWS endpoints
- Can work alongside CloudFront for latency reduction

---

## 5. **Comparison Table**

| Service | Type | Use Case | Protocol/Engine | Persistence | Scale |
|---------|------|----------|----------------|------------|-------|
| ElastiCache | In-memory cache | General purpose caching | Redis / Memcached | Redis: optional | Automatic |
| DynamoDB DAX | In-memory cache | DynamoDB acceleration | DynamoDB API | Managed write-through | Clustered |
| CloudFront | Edge cache / CDN | Web content delivery | HTTP/HTTPS | Temporary cache | Global edge locations |

---

### Notes:
- **ElastiCache** is ideal for fast, low-latency application-level caching.
- **DAX** is specific for DynamoDB workloads.
- **CloudFront** is for caching content at the edge to reduce latency for global users.
