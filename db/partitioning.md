## Database Partitioning

Partitioning is a database design technique where a single large table is divided into smaller, more manageable pieces called partitions, but all partitions are still part of the same logical table.

---

## Why Partitioning?

Partitioning helps in:

* **Improved query performance:** Queries scan only relevant partitions.
* **Easier maintenance:** Backup, restore, or archive can be done partition-wise.
* **Efficient storage management:** Old data can be moved to cheaper storage.
* **Manageability:** Large tables become easier to handle.

---

## Types of Partitioning

1. **Range Partitioning**

   * Data divided based on a range of values.
   * Example: Orders with `order_date` in Jan go to Partition 1, Feb to Partition 2.

2. **List Partitioning**

   * Data divided based on a predefined list of values.
   * Example: `country = 'US'` in Partition 1, `country = 'UK'` in Partition 2.

3. **Hash Partitioning**

   * A hash function determines the partition.
   * Example: `MOD(customer_id, 4)` decides which of 4 partitions the row belongs to.
   * ✅ Good for even distribution.

4. **Composite / Hybrid Partitioning**

   * Combination of multiple strategies.
   * Example: Range + Hash: first split by date ranges, then hash within each range.

---

## Key Points

* Partitioning is **logical**; all partitions belong to the same table.
* Queries can be optimized using **partition pruning**.
* Each partition can have its own **indexes**.
* Often used in **data warehousing**, **time-series data**, or **large transactional tables**.

---

## Example

```sql
-- Range Partitioning Example in PostgreSQL
CREATE TABLE orders (
    order_id INT,
    order_date DATE,
    customer_id INT
) PARTITION BY RANGE (order_date);

CREATE TABLE orders_jan PARTITION OF orders
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE orders_feb PARTITION OF orders
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');
```

✅ **In short:** Partitioning = splitting a table into smaller, manageable parts within the same database for performance, manageability, and storage efficiency.


# PostgreSQL Partitioning Example

Here's a complete example using **PostgreSQL**, showing creation of a partitioned table and querying it efficiently.

## **1. Create a partitioned table**

Suppose we want to store **sales data** partitioned by **region**:

```sql
-- Create main partitioned table
CREATE TABLE sales (
    sale_id SERIAL PRIMARY KEY,
    sale_date DATE NOT NULL,
    region TEXT NOT NULL,
    amount NUMERIC(10,2)
) PARTITION BY LIST (region);
```

## **2. Create partitions**

We create separate tables for each region:

```sql
CREATE TABLE sales_north PARTITION OF sales
    FOR VALUES IN ('North');

CREATE TABLE sales_south PARTITION OF sales
    FOR VALUES IN ('South');

CREATE TABLE sales_east PARTITION OF sales
    FOR VALUES IN ('East');

CREATE TABLE sales_west PARTITION OF sales
    FOR VALUES IN ('West');
```

## **3. Insert data**

```sql
INSERT INTO sales (sale_date, region, amount) VALUES
('2025-01-10', 'North', 100.50),
('2025-01-11', 'South', 200.00),
('2025-01-12', 'East', 150.75),
('2025-01-13', 'West', 300.20);
```

## **4. Query data efficiently**

Query for sales in the **North region**:

```sql
SELECT *
FROM sales
WHERE region = 'North'
  AND sale_date >= '2025-01-01';
```

* PostgreSQL automatically targets only the `sales_north` partition, skipping others.
* You can also query a specific partition directly:

```sql
SELECT *
FROM sales_north
WHERE sale_date >= '2025-01-01';
```

## ✅ **Key points**

1. Always include the **partition key** (`region`) in the `WHERE` clause for performance.
2. Partitioned tables behave like normal tables; querying is seamless.
3. Use **LIST** partitioning for categorical values and **RANGE** for continuous values like dates.