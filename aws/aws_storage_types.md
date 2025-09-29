# AWS Storage Types

AWS provides a variety of storage options optimized for different use cases like performance, durability, access patterns, and cost.

---

## 1. Object Storage
Used to store unstructured data like images, videos, backups, and logs.

- **Amazon S3 (Simple Storage Service)**
  - Highly durable (11 9’s durability)
  - Scalable and virtually unlimited
  - Access via API, SDK, or console
  - Storage classes:
    - **S3 Standard**: frequently accessed data
    - **S3 Intelligent-Tiering**: automatically moves data between frequent/infrequent tiers
    - **S3 Standard-IA / One Zone-IA**: infrequently accessed
    - **S3 Glacier / Glacier Deep Archive**: archival storage
  - Features: Versioning, Lifecycle policies, Encryption, Cross-region replication

---

## 2. Block Storage
Used for high-performance workloads like databases or OS storage.

- **Amazon EBS (Elastic Block Store)**
  - Persistent block storage for EC2
  - Types:
    - **General Purpose SSD (gp3/gp2)**: balanced performance & cost
    - **Provisioned IOPS SSD (io2/io1)**: high-performance databases
    - **Throughput Optimized HDD (st1)**: large, sequential workloads
    - **Cold HDD (sc1)**: low-cost, infrequently accessed
  - Features: Snapshots, encryption, scalable performance

- **Instance Store**
  - Temporary storage physically attached to the EC2 host
  - Data lost when instance stops or terminates
  - High IOPS and low latency

---

## 3. File Storage
Used when you need a file system interface with shared access.

- **Amazon EFS (Elastic File System)**
  - Fully managed, scalable, shared file storage
  - NFS interface
  - Storage classes:
    - **Standard**: frequently accessed
    - **Infrequent Access (IA)**
  - Can be mounted on multiple EC2 instances

- **Amazon FSx**
  - **FSx for Windows File Server**: Windows-based workloads, SMB protocol
  - **FSx for Lustre**: High-performance, compute-intensive workloads
  - Fully managed, supports backups, encryption, and scaling

---

## 4. Archive Storage
Low-cost storage for long-term retention.

- **Amazon S3 Glacier / Glacier Deep Archive**
  - Extremely low cost
  - Retrieval times: minutes to hours
  - Use for compliance or backup

---

## 5. Hybrid / On-Prem Integration
For integrating on-prem storage with AWS cloud.

- **AWS Storage Gateway**
  - Connects on-prem systems to S3 or Glacier
  - Types:
    - **File Gateway**: NFS/SMB to S3
    - **Volume Gateway**: block storage to EBS/S3
    - **Tape Gateway**: virtual tape library for backups

---

## Key Comparison

| Storage Type | Use Case | Access | Cost | Durability |
|-------------|---------|--------|------|------------|
| S3 | Object storage, backups, static content | API/HTTP | Low | 99.999999999% |
| EBS | EC2 block storage, DBs | Attached to EC2 | Medium | 99.999% |
| EFS | Shared file system | NFS | Medium | 99.99% |
| FSx | Windows/Lustre workloads | SMB/NFS | High | 99.9%+ |
| Glacier | Archive | API | Very low | 99.999999999% |
