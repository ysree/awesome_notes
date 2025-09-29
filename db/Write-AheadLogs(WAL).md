## Write-Ahead Logs (WAL)

Write-Ahead Logs (WAL) are a fundamental concept in databases to ensure **durability** and **recovery** when crashes or failures happen.

---

## What is a Write-Ahead Log?

A **Write-Ahead Log (WAL)** is a sequential log file where every change (insert, update, delete) is written **before** it’s applied to the actual database.

**Idea:** “Record it in the log first, then update the database.” This ensures that if the system crashes midway, the log can be replayed to recover.

---

## How WAL Works (Step by Step)

1. A transaction starts.
2. Before modifying the actual database (data pages on disk), the system writes an entry into the WAL.

   * **Example:** `UPDATE account SET balance = balance - 100 WHERE id = 1;`
   * Log entry: `T1: Subtract 100 from account 1`
3. Once the log entry is written (and flushed to disk), the database applies the change to memory/disk pages.
4. On commit, another log entry (`T1: Commit`) is written.
5. If a crash happens:

   * On restart, the database replays the WAL to **redo committed transactions** and **undo uncommitted ones**.

---

## Why WAL is Important

* **Durability (ACID D):** Once a commit is written to the log, it won’t be lost even if the system crashes.
* **Crash Recovery:** WAL allows the database to restore a consistent state by replaying logs.
* **Performance:** Sequential log writes are faster than random writes to database files.

---

## Example in Real Databases

* **PostgreSQL:** Uses WAL for crash recovery and replication.
* **MySQL (InnoDB engine):** Uses a redo log (similar to WAL).
* **Cassandra / HBase:** Use commit logs (WAL-like) for durability.
* **Kafka:** Distributed log conceptually similar to WAL.

---

## Simple Analogy

Think of it like writing in a notebook before doing work:

* Before cooking, you note down the steps in a diary (WAL).
* If you forget or something goes wrong, you check your diary to continue from where you left.
* Ensures no step is permanently lost.

---

✅ **In short:** Write-Ahead Log = a sequential log where all changes are recorded before being applied to the database, ensuring durability and recovery after crashes.
