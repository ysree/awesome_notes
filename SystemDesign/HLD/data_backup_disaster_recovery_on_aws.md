# AWS Database Backup & DR Design — Resiliency, Fault Tolerance, Availability

A production-grade **Database Backup & Disaster Recovery (DR)** design for AWS focused on **resiliency**, **fault tolerance**, and **high availability**. This document covers architecture, backup types, retention, RTO/RPO, automation, security, testing, restore runbooks, and practical implementation details.

---

## 1. High-level architecture

**Flow (text form):**

Clients / Apps → Load balancer / API tier → Primary DB Cluster (Multi-AZ) — e.g., Amazon RDS / Aurora (writes to leader) → Read replicas (Multi-AZ or cross-AZ) for reads

**Backup & DR pipeline:**

* WAL/transaction shipping / PITR pipeline → AWS Backup / S3 (continuous or near-continuous)
* Block snapshots (EBS / RDS snapshots) → copied to secondary Region/Account
* Long-term archive → S3 Glacier / Glacier Deep Archive (lifecycle)
* Immutable copy → S3 with Versioning + Object Lock for retention/immutability (ransomware protection)

Cluster management & monitoring: CloudWatch, AWS Backup Audit Manager, AWS Config, automated Lambda orchestrations for snapshot copy and tagging.

---

## 2. Backup types & coverage

1. **Continuous (WAL / Binlog) / PITR**

   * Finest RPO (minutes). Use DB native PITR or ship WAL to S3. Best for transactional DBs.

2. **Block snapshots (EBS / RDS snapshots)**

   * Crash-consistent images; fast restores. Automate and copy cross-region.

3. **Logical backups (pg\_dump / mysqldump / export)**

   * Useful for schema migrations, exports, and long-term retention.

4. **Periodic full backups**

   * Snapshots exported to S3 + archival.

5. **Immutable backups**

   * S3 Versioning + Object Lock for tamper protection and compliance.

---

## 3. Recovery objectives & DR strategy options

* **RPO (Recovery Point Objective):** minutes with WAL/PITR; hours for snapshots; days for archives.
* **RTO (Recovery Time Objective):** minutes for failover (Aurora Global / Multi-AZ), minutes–hours for warm standby, hours for cold restore.

DR patterns:

* **Multi-AZ active/passive** (default for RDS): automatic AZ failover with minimal RTO.
* **Warm-standby cross-region:** replica cluster in secondary region kept warm; promote on failover.
* **Pilot-light:** minimal resources in DR region and rapid scale-up on failover.
* **Multi-site active/active:** full active clusters in multiple regions — lowest RTO but highest cost/complexity.

---

## 4. Concrete AWS service mapping & recommendations

* **Primary DB:** Amazon Aurora (MySQL/Postgres) or RDS with Multi-AZ. Consider Aurora Global DB for low-RPO cross-region replication.
* **Backups:** AWS Backup for centralized plans + native RDS automated backups (PITR).
* **Block snapshots:** EBS snapshots automated via Data Lifecycle Manager; copy cross-region.
* **Archive & immutability:** S3 with Versioning + Object Lock; lifecycle to Glacier/Deep Archive.
* **WAL shipping / Archive logs:** For self-managed DBs, ship WAL/binlogs to S3. For RDS/Aurora rely on native PITR or AWS Backup continuous backup.
* **Encryption & keys:** Use KMS CMKs for S3, EBS, and backup vaults.
* **Orchestration:** AWS Backup, Step Functions + Lambda, Systems Manager Automation.
* **Monitoring & auditing:** CloudWatch, AWS Backup Audit Manager, AWS Config, CloudTrail.

---

## 5. Backup lifecycle & automation (practical pattern)

1. **Classification & policy:** classify DBs (Critical / Important / Non-critical) and map RPO/RTO and retention.
2. **Automate:**

   * Use **AWS Backup** plans to schedule snapshots + PITR retention for RDS/Aurora.
   * For EC2/EBS use Data Lifecycle Manager for incremental snapshots and cross-region copy.
   * WAL/binlog → push to S3 continuously (or rely on RDS PITR).
3. **Cross-region & cross-account copies:** automatically copy snapshots to secondary region and optionally to a separate AWS account for isolation.
4. **Immutability & retention:** store critical backups in S3 with Versioning + Object Lock (governance/compliance mode).
5. **Tagging & cost controls:** tag backups (env, app, owner, retention) and use lifecycle rules to move older backups to archive.

---

## 6. Transaction consistency: crash-consistent vs application-consistent

* **Crash-consistent (snapshots):** good if you can quiesce IO or rely on managed DBs which handle consistency.
* **Application-consistent:** quiesce DB I/O (e.g., `FLUSH TABLES WITH READ LOCK` in MySQL) or use DB-native snapshot mechanisms + WAL/binlog shipping. PITR + WAL shipping is best for transactional consistency.

---

## 7. Failures & transaction recovery handling

**Common failures and handling:**

* **Instance crash / AZ failure:** Multi-AZ RDS/Aurora will auto-failover; verify WAL continuity after failover.
* **Partial writes/in-flight tx at snapshot time:** persist WAL/binlogs and use PITR to roll-forward or rollback as needed.
* **Region failure:** promote cross-region replica (Aurora Global) or restore from cross-region snapshots; update DNS and endpoints.
* **Ransomware / accidental deletion:** recover from immutable S3/Object Lock versions or cross-account copies.
* **Backup corruption/incomplete backups:** validate snapshots after creation (automated restore) and keep multiple copies.

---

## 8. Testing, validation & runbooks

1. **Automated restore drills:** monthly/quarterly restores into isolated accounts/VPCs; verify data integrity and app behavior.
2. **Smoke tests post-restore:** run schema checksums, sample queries, and application sanity tests.
3. **Chaos testing:** simulate AZ/region/network failures and validate failover + restore flows.
4. **Backup validation pipeline:** Step Functions/Lambda to create snapshot → copy → test restore → checksums → report.

---

## 9. Security & compliance

* **Encryption:** TLS in transit; KMS CMKs for S3/EBS/backup vaults.
* **Separate backup account:** copy critical backups to a separate AWS account with limited access.
* **Immutability:** S3 Object Lock in governance/compliance mode.
* **Access controls:** least-privilege IAM roles; Organizations SCPs for backup accounts.
* **Audit:** CloudTrail, AWS Backup Audit Manager, AWS Config rules.

---

## 10. Costs & tradeoffs (summary)

* **Continuous WAL/PITR:** higher cost but best RPO.
* **Cross-region copies:** increases durability & security but costs more (storage & data transfer).
* **Immutable S3 + long retention:** expensive long-term; use Glacier tiers for cost savings.

---

## 11. Example implementation (critical RDS/Aurora DB)

1. Enable **RDS automated backups** with retention N days and enable PITR.
2. Create an **AWS Backup plan** targeting the DB and EBS volumes; schedule daily snapshots and cross-region copies to a separate backup vault/account.
3. Enable **Aurora Global DB** or configure logical replication to cross-region read replica.
4. Configure **S3 bucket** for WAL/binlog shipping with Versioning + Object Lock; lifecycle to Glacier.
5. Automate validation: Lambda + Step Functions to restore weekly in a test cluster and run checksum queries.
6. Configure IAM + KMS policies and enable CloudTrail and Backup Audit Manager.

---

## 12. Runbook: Restore from snapshot (example)

1. Incident: Region A failing or data corrupted. Decide to restore from cross-region copy or promote replica.
2. If Aurora Global: promote secondary region cluster → update application endpoint.
3. If restoring from snapshot: in secondary region, create DB instance from copied snapshot → apply latest WAL (if available) → wait for availability.
4. Run smoke tests and application checks; update DNS/load-balancer.
5. Post-incident: root cause analysis and ensure immutable copies exist.

---

## 13. Checklist (actionable)

* [ ] Classify DBs (critical/important/non-critical) and set RPO/RTO.
* [ ] Enable native PITR or implement WAL shipping for self-managed DBs.
* [ ] Use AWS Backup for centralized scheduling and cross-region copies.
* [ ] Copy snapshots cross-region & cross-account.
* [ ] Use S3 Versioning + Object Lock for immutable copies.
* [ ] Automate restore drills and validation.
* [ ] Harden IAM, KMS, and enable logging (CloudTrail, AWS Config).
* [ ] Maintain runbooks for failover, restore, and rollback.

---

## 14. Next steps (suggested)

* Create detailed runbooks with exact AWS CLI/Console commands for the chosen DB engine (Postgres / MySQL / Aurora).
* Build an automated Step Functions + Lambda pipeline to orchestrate snapshots, cross-region copies, and validation.
* Implement scheduled restore drills and automated smoke tests.