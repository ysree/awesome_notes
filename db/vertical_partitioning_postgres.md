# Vertical Partitioning in PostgreSQL (SSD + HDD Example)

## 🔑 Step 1: Original Table
Suppose you have this table:

```sql
CREATE TABLE documents (
    doc_id SERIAL PRIMARY KEY,
    title TEXT,
    author TEXT,
    created_at TIMESTAMP,
    category TEXT,
    tags TEXT[],
    version INT,
    is_active BOOLEAN,
    last_modified TIMESTAMP,
    data BYTEA   -- BLOB column (large)
);
```

**Problem:** The `data` column (BLOB) is huge, and storing it on SSD is costly.

---

## 🔑 Step 2: Vertical Partitioning
We split into **two tables**:

1. **Metadata table** → frequently accessed columns (on SSD).  
2. **Blob table** → large BLOB column (on HDD).  

```sql
-- Table on SSD
CREATE TABLE documents_metadata (
    doc_id SERIAL PRIMARY KEY,
    title TEXT,
    author TEXT,
    created_at TIMESTAMP,
    category TEXT,
    tags TEXT[],
    version INT,
    is_active BOOLEAN,
    last_modified TIMESTAMP
);

-- Table on HDD
CREATE TABLE documents_blob (
    doc_id INT PRIMARY KEY REFERENCES documents_metadata(doc_id) ON DELETE CASCADE,
    data BYTEA   -- big BLOB column
);
```

---

## 🔑 Step 3: Tablespace Placement (SSD vs HDD)
Postgres lets you assign different tables to different **tablespaces** (mapped to storage devices).  

### 1. Create tablespaces:
```sql
-- Path to SSD and HDD mount points (directories owned by postgres)
CREATE TABLESPACE ssd_space LOCATION '/mnt/ssd1';
CREATE TABLESPACE hdd_space LOCATION '/mnt/hdd1';
```

### 2. Place tables accordingly:
```sql
-- Metadata on SSD
CREATE TABLE documents_metadata (
    doc_id SERIAL PRIMARY KEY,
    title TEXT,
    author TEXT,
    created_at TIMESTAMP,
    category TEXT,
    tags TEXT[],
    version INT,
    is_active BOOLEAN,
    last_modified TIMESTAMP
) TABLESPACE ssd_space;

-- Blob data on HDD
CREATE TABLE documents_blob (
    doc_id INT PRIMARY KEY REFERENCES documents_metadata(doc_id) ON DELETE CASCADE,
    data BYTEA
) TABLESPACE hdd_space;
```

---

## 🔑 Step 4: Querying
### When you query **just metadata** (common case):
```sql
SELECT title, author, created_at
FROM documents_metadata
WHERE is_active = TRUE;
```
👉 Fast, SSD-backed query, no BLOB overhead.  

### When you need the BLOB:
```sql
SELECT m.title, b.data
FROM documents_metadata m
JOIN documents_blob b ON m.doc_id = b.doc_id
WHERE m.doc_id = 101;
```
👉 Slower (reads HDD), but happens only when needed.

---

## 🔑 Step 5: Benefits
- **Performance**: Most queries only hit SSD (small table).  
- **Cost optimization**: HDD used for heavy BLOB storage.  
- **Flexibility**: You can back up / archive blob table separately.  

---

## ✅ Analogy
Think of it like storing your **bookshelf index on your desk (SSD)** while keeping the **actual books in the basement (HDD)**. You only go downstairs when you really need the full book.
