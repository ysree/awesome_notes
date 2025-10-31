# Transactional Database – SalesforceDB Notes

## Overview
- **SalesforceDB**: Modern, cloud-native relational database built for Salesforce’s **multitenant workloads**.  
- Extends **PostgreSQL**, separates **compute and storage**, uses **Kubernetes** + **cloud storage**.  
- Handles **CRM transactional data** (700B+ transactions/month) and **metadata for Data Cloud & related services**.  
- Goals: **Durability, Availability, Performance, Security, Scalability**, and **Simplified Operations**.  

---

## Architecture
- Runs across **3 Availability Zones (AZs)**.  
- **Compute & storage replicated** across AZs → survives node/zone failures.  
- **Kubernetes orchestration** → automated failover, recovery, and deployments.  
- **System of record** = Cloud storage (e.g., AWS S3).  
  - Immutable storage objects → easy replication & archiving.  
  - Cross-region replication at storage level.  
- **Storage caches** (for low-latency access):  
  - Transaction log cache.  
  - Data file cache.  

---

## Compute Tier
- **Primary cluster** (handles modifications).  
- **Two standby clusters** (read-only queries).  
- SQL-based operations.  

---

## LSM Storage & Immutable Data
- **Log-Structured Merge Tree (LSM)**:  
  - Updates → transaction log + in-memory buffer.  
  - Flushed to **key-ordered immutable files**.  
  - Periodic **merge/compaction** for efficiency.  
- Benefits:  
  - Avoids concurrent update conflicts.  
  - Immutable storage → reliability & scalability.  
  - Append-only logs simplify reads, backups, scaling, virtualization.  

---

## Availability & Durability
- **Transactions committed across multiple AZs** → no committed data loss.  
- Failures → abort in-flight transactions, recover committed ones.  
- Automated failover via **cluster management software** (quorum-based).  
- Used for both failures & **routine patching** (proven reliability).  
- **Zero-downtime schema changes**:  
  - 3 major + weekly minor updates/year without customer impact.  

---

## System of Record & Security
- **Immutable checksums** per data block.  
- Lineage tracking + consistency checks.  
- **Backups**: full + incremental, stored separately in different accounts.  
- **Ransomware protection**: pre-configured cloud infra for restore, regular validation via restore testing.  

---

## Scalability
- Each **Salesforce org** runs in a **Hyperforce cell** (with SalesforceDB).  
- Horizontal scaling:  
  - **Storage** → virtually unlimited via cloud storage.  
  - **Cache layers** → auto-scale.  
  - **Compute** → add more nodes (shared immutable storage = no coordination needed).  
- Comparable or better scalability than commercial cluster DBs.  

---

## Multitenancy
- **Single DB hosts multiple tenants**.  
- Each record has a **tenant ID**.  
- **Tenant isolation** via auto query predicates in Salesforce app layer.  
- Tenant-specific:  
  - **DDL, metadata, runtime processes**.  
  - **Per-tenant encryption**.  
- Efficient **tenant-per-row model** with compact metadata.  
- Tenant data **clustering in LSM structure** → fast access.  
- Tenants can be **copied/migrated** easily with minimal metadata updates.  

---