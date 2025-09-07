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