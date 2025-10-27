# table of contents
- [vCenter AI RAG Chatbot Example](#vcenter-ai-rag-chatbot-example)
- [Chatbot for VMware vCenter](#chatbot-for-vmware-vcenter)

# vCenter AI RAG Chatbot Example
This document provides example prompts for interacting with a vCenter AI RAG (Retrieval-Augmented Generation) chatbot. The chatbot is designed to assist with queries related to vCenter environments by retrieving relevant information from private documents and generating responses using an LLM (Large Language Model).

Here are 20 example prompts you can use to interact with a vCenter AI RAG chatbot:
1. Which host is running out of resources?
2. Which host license is expired or expiring soon?
3. List all the VMs in Cluster 1.
4. Which hosts have GPU configured?
5. List all VMs not backed up in the last 7 days.
6. Which datastores are approaching capacity?
7. Which hosts have outdated VMware Tools?
8. List VMs powered off for more than 30 days.
9. Which VMs are consuming high network bandwidth?
10. Which VMs have snapshots older than 7 days?
11. Identify hosts not compliant with security policies.
12. List all VMs in a specific resource pool.
13. Which VMs are consuming excessive disk I/O or storage?
14. Which hosts have HA configured?
15. Which VMs are using high CPU and memory?
16. Which hosts have vMotion enabled and which do not?
17. List hosts with datastore access issues.
18. Which VMs are configured with Fault Tolerance (FT)?
19. Which hosts are in a DRS-enabled cluster?
20. List all VMs running on unsupported hardware versions.


Great — that’s a smart call.

### Chatbot for VMware vCenter

You want a **chatbot for VMware vCenter** that can:

* Accept **natural language questions**
* Automatically **generate SQL queries** (without maintaining hardcoded prompt-query mappings)
* Query the **vCenter PostgreSQL database**
* Return **readable answers**

No manual mapping = less maintenance, more flexibility.

---

## ✅ Best Approach (No Manual Mappings):

### 🔧 Use **LLM + Schema-Aware Text-to-SQL Generator**

Let the chatbot:

1. Read the **PostgreSQL database schema** dynamically (e.g., via `information_schema`)
2. Accept user’s natural language question
3. Use **LLM** to generate the appropriate SQL query *on the fly*, based on:

   * The question
   * The real-time schema
4. Run the query
5. Format and return the result

---

## 🛠️ How to Build It (No Mapping)

### ✅ 1. Connect to vCenter PostgreSQL (Read-only)

Create a read-only DB user and connect securely.

```sql
CREATE USER chatbot_read WITH PASSWORD 'your_secure_password';
GRANT CONNECT ON DATABASE VCDB TO chatbot_read;
GRANT USAGE ON SCHEMA public TO chatbot_read;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO chatbot_read;
```

Use `psycopg2` or `asyncpg` in Python.

---

### ✅ 2. Auto-Fetch Database Schema

You need to dynamically fetch schema metadata to guide SQL generation.

```sql
SELECT table_name, column_name, data_type
FROM information_schema.columns
WHERE table_schema = 'public';
```

Store this in memory or cache — it will serve as context for SQL generation.

---

### ✅ 3. Use LLM (OpenAI or Local) to Generate SQL Based on Schema + Question

You can do this with OpenAI’s GPT-4 using **function calling** or a tool like **LangChain SQL agent**.

Example prompt:

```plaintext
Schema:
Table: vm
Columns: id, name, power_state, cpu_usage, memory_usage, cluster_id

Table: host
Columns: id, name, cpu_total, cpu_used, memory_total, memory_used, license_expiry, gpu_model

User Question:
"Which hosts have GPU configured?"

Return a safe SQL query only.
```

Expected output:

```sql
SELECT name, gpu_model FROM host WHERE gpu_model IS NOT NULL;
```

---

### ✅ 4. Run the Generated SQL Safely

Use parameterized queries or validate output SQL.

```python
def run_sql(query: str):
    conn = psycopg2.connect(...)
    with conn.cursor() as cur:
        cur.execute(query)
        return cur.fetchall()
```

---

### ✅ 5. Format the Answer for the User

Turn raw SQL results into clean, readable responses.

```python
def format_results(results):
    if not results:
        return "No matching records found."
    
    response = openai.ChatCompletion.create(
        model="gpt-4",
        messages=[
            {"role": "system", "content": "You are a VMware vCenter assistant who answers questions using SQL results in human-readable format."},
            {"role": "user", "content": results}
        ],
        temperature=0.3
    )
    return response.choices[0].message.content.strip()
```

---

### ✅ 6. Backend Flow (Python – FastAPI)

```python
from fastapi import FastAPI, Request
from pydantic import BaseModel
import psycopg2
import openai

app = FastAPI()

class QueryRequest(BaseModel):
    question: str

@app.post("/ask")
def ask_vcenter(req: QueryRequest):
    schema = get_db_schema()
    prompt = build_prompt(schema, req.question)
    sql = call_llm(prompt)
    results = run_sql(sql)
    return format_results(results)
```

---

### 🧠 Optional: Use LangChain SQL Agent (No Mapping)

LangChain supports dynamic, schema-aware, Text-to-SQL agents.

Sample config:

```python
from langchain_experimental.sql import SQLDatabaseChain
from langchain.chat_models import ChatOpenAI
from langchain.sql_database import SQLDatabase

db = SQLDatabase.from_uri("postgresql://chatbot_read:password@host/VCDB")
llm = ChatOpenAI(model="gpt-4", temperature=0)

chain = SQLDatabaseChain.from_llm(llm, db, verbose=True)
response = chain.run("Which hosts have expired licenses?")
```

---

### ✅ Summary: Why This Works Well

| Feature             | Benefit                                           |
| ------------------- | ------------------------------------------------- |
| ❌ No manual mapping | No brittle query mapping logic                    |
| ✅ Schema-aware      | Automatically adapts to DB changes                |
| ✅ Scalable          | Supports hundreds of query types                  |
| ✅ LLM-powered       | Can generalize new questions without code changes |
| ✅ Secure            | Works with read-only DB access                    |

---

## 🧪 Example Questions It Can Handle

* "Which hosts are out of memory?"
* "List all VMs powered off for more than 30 days."
* "Which datastores are nearly full?"
* "Which hosts have NVIDIA GPUs?"
* "List all VMs in cluster `Prod-1`."
* "Which VMs have snapshots older than 10 days?"

---

## 👉 Want a working starter code template with:

* ✅ FastAPI
* ✅ OpenAI GPT-4 SQL generation
* ✅ PostgreSQL schema fetch
* ✅ Secure SQL execution?

Let me know and I’ll generate it for you.
