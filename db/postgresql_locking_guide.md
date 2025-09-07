# 🔑 Locking in PostgreSQL

PostgreSQL uses locks to **control concurrent access** to tables and rows. Locks prevent conflicts when multiple transactions try to read or modify the same data.

Two common types of locks are:
1. **Exclusive Lock (X Lock)**
2. **Shared Lock (S Lock)**

## ⚙️ 1. **Exclusive Lock**

* An **exclusive lock** is acquired when a transaction **modifies data** (INSERT, UPDATE, DELETE).
* **Only one transaction can hold an exclusive lock** on a resource at a time.
* Prevents **other transactions** from reading or writing the locked resource.

### Example:

```sql
-- Transaction 1
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
-- Exclusive lock acquired on row with id=1

-- Transaction 2
BEGIN;
UPDATE accounts SET balance = balance + 100 WHERE id = 1;
-- Transaction 2 waits until Transaction 1 commits or rolls back
```

✅ **Use case:** Updating, deleting, or inserting rows.  
⚠️ **Effect:** Other transactions cannot modify or sometimes even read the locked rows depending on isolation level.

## ⚙️ 2. **Shared Lock**

* A **shared lock** is acquired when a transaction **reads data** using `SELECT ... FOR SHARE`.
* Multiple transactions can hold a shared lock on the same resource **simultaneously**.
* Prevents **exclusive locks** but allows other shared locks.

### Example:

```sql
-- Transaction 1
BEGIN;
SELECT * FROM accounts WHERE id = 1 FOR SHARE;
-- Shared lock acquired on row with id=1

-- Transaction 2
BEGIN;
SELECT * FROM accounts WHERE id = 1 FOR SHARE;
-- Allowed! Another shared lock can coexist

-- Transaction 3
BEGIN;
UPDATE accounts SET balance = balance + 100 WHERE id = 1;
-- Transaction 3 waits until all shared locks are released
```

✅ **Use case:** Reading rows for consistent snapshot without blocking other readers.  
⚠️ **Effect:** Prevents writes but allows other reads.

## 🔄 Summary Table

| Lock Type | Held During | Conflicts With | Example SQL |
|-----------|-------------|----------------|-------------|
| **Exclusive Lock** | Modifying data (UPDATE/DELETE/INSERT) | All other locks | `UPDATE accounts SET ...` |
| **Shared Lock** | Reading data with `FOR SHARE` | Exclusive locks | `SELECT ... FOR SHARE` |

## 🔄 Analogy

* **Exclusive Lock:** "I'm in the room alone, nobody else can enter until I'm done."
* **Shared Lock:** "Multiple people can read the room's notice board together, but nobody can rearrange it while others are reading."