# PostgreSQL Replication

Great question 👌 — PostgreSQL supports **replication** to provide **high availability, scalability, and fault tolerance**. Let me break it down step by step:

---

# 🔑 What is Replication in PostgreSQL?
Replication = keeping a **copy of your database** (or parts of it) on another server (or multiple servers).  

- **Primary (Master)** → accepts **read + write** operations.  
- **Standby (Replica/Slave)** → usually accepts **read-only** queries.  

The standby continuously applies changes from the primary so both stay in sync.

---

# ⚙️ Types of Replication in PostgreSQL

### 1. **Streaming Replication** (Most common)
- Introduced in PostgreSQL 9.0+.  
- Uses **WAL (Write-Ahead Log)** files to replicate changes.  
- Process:
  1. Client sends `INSERT/UPDATE/DELETE` to **Primary**.  
  2. Primary writes changes to **WAL logs**.  
  3. WAL logs are **streamed** to the Standby.  
  4. Standby **replays** the WAL changes to keep data identical.  

👉 The standby can be:
- **Synchronous** → Transaction on primary waits until WAL is confirmed on standby (strong consistency, but higher latency).  
- **Asynchronous** → Primary doesn’t wait; standby may lag behind slightly (better performance, risk of data loss if primary crashes).  

---

### 2. **Logical Replication**
- Introduced in PostgreSQL 10+.  
- Replicates **data changes (DML)** at a logical level (not raw WAL blocks).  
- Allows replication of **specific tables**, not the entire database.  
- Supports **bi-directional replication** (multi-master setups).  

Example:  
- `publisher` (primary) publishes changes from certain tables.  
- `subscriber` (standby) subscribes and applies those changes.  

---

### 3. **Physical Replication**
- The entire **database cluster** is replicated byte-for-byte.  
- Includes all databases, schemas, tables, indexes, etc.  
- Standby is always an **exact physical copy** of primary.  
- Usually implemented via **streaming replication** or **file-based WAL shipping**.

---

# 🖼 Example Setup (Streaming Replication)

1. **On Primary (Master):**
   ```sql
   -- Enable WAL archiving and replication
   wal_level = replica
   max_wal_senders = 10
   wal_keep_size = 256MB
    archive_mode = on
    archive_command = 'cp %p /path/to/archive/%f'
    ```

2. **On Standby (Replica):**
    ```bash
    pg_basebackup -h primary_host -D /var/lib/postgresql/data -U replication_user -P --wal-method=stream
    ```
3. **Start Standby:**
    ```bash
    pg_ctl start -D /var/lib/postgresql/data
    ```
4. **Configure `recovery.conf` on Standby:**
    ```ini
    standby_mode = 'on'
    primary_conninfo = 'host=primary_host port=5432 user=replication_user password=your_password'
    trigger_file = '/tmp/failover.trigger'
    ```
5. **Start both servers** and monitor replication status:
    ```sql
    SELECT * FROM pg_stat_replication;
    ```   


# Master-Master Replication (Multi-Master Replication)

---

# 🔑 What is Master-Master Replication?
- **Master-Master Replication** (also called **Multi-Master Replication**) is when **two or more database servers act as masters**, meaning:  
  - Each master can **accept read and write operations**.  
  - Changes made on one master are **replicated** to all other masters.  

So, instead of a single **Primary → Replica** model, you have **Primary ↔ Primary** (and possibly more).

---

# ⚙️ How it Works
1. **Both servers are writable.**  
   - Application can connect to any master for read/write queries.  

2. **Replication flow is bi-directional.**  
   - If Master A updates a row, the change is sent to Master B.  
   - If Master B updates a row, the change is sent to Master A.  

3. **Conflict detection & resolution** is required.  
   - If the same row is updated differently on both masters at the same time → conflict.  
   - Strategies: last-write-wins, timestamps, or application-defined rules.  

---

# 📊 Example
Imagine two PostgreSQL nodes in **different regions**:  

- **Master A** in India  
- **Master B** in US  

- A customer in India places an order → data written to Master A.  
- That change is replicated to Master B.  
- A US customer updates their profile on Master B → replicated back to Master A.  

👉 Both masters eventually stay in sync.

---

# ✅ Benefits
- **High Availability:** If one master fails, the other can still accept writes.  
- **Geographic Distribution:** Users connect to the closest master → lower latency.  
- **Load Balancing:** Spreads write load across multiple servers.  

---

# ⚠️ Challenges
- **Conflict Resolution:** Hard when the same record is modified on two masters at once.  
- **Consistency Issues:** Risk of data divergence if replication lags.  
- **Complex Setup:** More moving parts compared to Master-Slave replication.  

---

# 🔄 Comparison

| Feature              | Master-Slave Replication      | Master-Master Replication  |
|----------------------|-------------------------------|----------------------------|
| **Writes**           | Only on Master                | On all Masters             |
| **Reads**            | On Slave(s) or Master         | On all Masters             |
| **High Availability**| Limited (failover needed)     | Better (multiple writable nodes) |
| **Conflict Handling**| Not required                  | Required                   |

---

# ✅ Summary
**Master-Master Replication = multiple writable databases that keep each other in sync**.  
It’s powerful for **availability and global scaling**, but adds complexity in **conflict handling**.  


---------
# Synchronous vs Asynchronous Replication

---

# 🔑 What is Replication?
Replication = copying changes from a **Primary (master)** database to one or more **Standby (replica)** databases.  

Replication can be done in two ways: **Synchronous** or **Asynchronous**.

---

# ⚙️ 1. **Synchronous Replication**
- In **synchronous replication**, when a client performs a `COMMIT`, the transaction is **not considered successful** until **both:**
  1. The Primary writes the transaction to its WAL (Write-Ahead Log).  
  2. The Standby confirms that it has also received and written the same WAL entry.  

- This guarantees **zero data loss** (strong consistency).  

✅ **Advantages:**  
- Strong durability → No committed transaction is lost, even if the primary crashes.  
- Good for financial systems, banking, critical apps.  

⚠️ **Disadvantages:**  
- Slower, because the primary waits for acknowledgment from the standby.  
- Higher latency if standby is geographically distant.  

---

# ⚙️ 2. **Asynchronous Replication**
- In **asynchronous replication**, the primary **does not wait** for acknowledgment from the standby.  
- The client gets success as soon as the primary writes to its WAL.  
- The standby applies the changes later.  

✅ **Advantages:**  
- Faster transaction commits (low latency).  
- Better performance under high load.  

⚠️ **Disadvantages:**  
- Risk of **data loss** if the primary fails before the standby catches up.  
- Standby may lag behind the primary.  

---

# 📊 Example (PostgreSQL Streaming Replication)

- **Synchronous Mode:**  
  ```sql
    synchronous_standby_names = 'replica1'

    synchronous_commit = 'on'
    ```
    → Primary waits until replica1 confirms WAL write.

- **Asynchronous Mode:**  
  ```sql
    synchronous_standby_names = ''

    synchronous_commit = 'off'
  ```
    → Primary never waits; just ships WAL as fast as possible.

👉 **In short:**

- **Synchronous replication** = safer, but slower.

- **Asynchronous replication** = faster, but may risk data loss.



