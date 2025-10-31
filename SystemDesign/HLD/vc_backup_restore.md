# VMware Backup & Restore – Detailed Explanation

## 1. Key Components Involved

| Component | Description |
|-----------|-------------|
| **vCenter Server** | Central management platform for ESXi hosts and VMs. Stores inventory, configuration, and metadata. |
| **ESXi Hosts** | Hypervisors that run virtual machines. Host the VM disk files (VMDKs) on datastores. |
| **VMware Tools / Guest OS** | Optional agents inside VMs for application-consistent snapshots and quiescing. |
| **Datastores** | Storage layer (VMFS, NFS, vSAN) where VM files (VMDK, VMX, logs) are stored. |
| **Backup Solution / BRS** | VMware Backup and Restore Service or third-party solutions (Veeam, NetBackup). Orchestrates backups and restores. |
| **Snapshots** | Point-in-time copies of VM state (disk + memory optionally). Used for consistent backups. |
| **WAL / Journals** | Tracks incremental changes in cloud-managed backups (e.g., BRS in VMware Cloud). |
| **Object Storage / Cloud Storage** | Target location for storing backups, snapshots, and long-term retention. |

---

## 2. Backup Workflow

### Step 1: Pre-Check & Preparation
- Host and cluster availability check.  
- VM powered-on status check.  
- Storage capacity and I/O validation.  
- vCenter and ESXi health check.  
- Snapshot space validation.  

For VMware Cloud (VMC on AWS):
- Checks vCenter, NSX Manager, HCX Manager, vSAN datastore health.  
- Ensures API connectivity to object storage.  
- Validates credentials and permissions.  

---

### Step 2: Snapshot Creation
1. **Quiescing**  
   - VMware Tools ensures application-consistent snapshot by pausing I/O or flushing file systems.  
   - Without quiescing, snapshot is crash-consistent.  

2. **Delta Disk**  
   - ESXi creates a `-delta.vmdk` file.  
   - VM continues writing to delta disk while backup reads base disk.  

---

### Step 3: Data Transfer to Backup Target
1. **Block Storage / SAN-based Backup**  
   - Uses storage-level snapshots and array integration.  
   - Offloads backup from ESXi host.  

2. **Host/VM-level Backup**  
   - Reads VM disk files (VMDKs) over network.  
   - Supports **full** or **incremental** backup.  

3. **Incremental Backups**  
   - Uses **Changed Block Tracking (CBT)**.  
   - Transfers only modified blocks.  

---

### Step 4: Metadata & Cataloging
- Backup solution records VM UUID, snapshot ID, and backup type.  
- Catalog maintained for fast restore.  
- Encryption applied for security.  

---

## 3. Restore Workflow

### Step 1: Select Backup Point
- Choose backup snapshot:  
  - Full VM  
  - Individual disk (VMDK)  
  - Application-level files  

- Restore target options:  
  - Original location  
  - Alternate host/cluster/AZ  
  - Cross-cloud (different vCenter or SDDC)  

---

### Step 2: Prepare Target Environment
- Verify compute, storage, and network availability.  
- Create temporary staging if required.  
- Match VM configuration with backup metadata.  

---

### Step 3: Restore VM Disk(s)
- Copy **VMDKs** from backup to datastore.  
- Merge incrementals into full disk if needed.  
- Replay **WAL/journals** for point-in-time recovery.  

---

### Step 4: Snapshot/Quiescing Cleanup
- Remove backup snapshots on VM.  
- Merge delta disks into base disk.  
- Release locks and temporary files.  

---

### Step 5: Power-On & Validation
- Boot restored VM.  
- Perform disk consistency checks.  
- Validate OS and applications.  
- Notify admin of success/failure.  

---

## 4. Handling Failures During Backup/Restore

| Failure Type | VMware Handling |
|--------------|-----------------|
| **Node/ESXi crash** | Retry backup; snapshot delta ensures no data loss. |
| **Network failure** | Resume from last checkpoint or incremental block. |
| **Snapshot creation failure** | Backup aborted safely; admin notified. |
| **Storage full** | Backup fails; logs captured; retry after cleanup. |
| **Transaction failure** | WAL/journals roll back to last consistent state. |

---

## 5. Backup Types Supported
- **Full Backup** → Complete copy of VM.  
- **Incremental Backup** → Changed blocks only (via CBT).  
- **Differential Backup** → Changes since last full backup.  
- **Application-Consistent Backup** → Uses VMware Tools for quiescing.  
- **Crash-Consistent Backup** → Fast snapshot without app quiescing.  

---

## 6. Key VMware Backup & Restore Mechanisms
- **CBT (Changed Block Tracking):** Efficient incremental backup mechanism.  
- **Snapshots:** Capture VM state without downtime.  
- **Delta Disks:** Temporary writes during snapshot period.  
- **Object Storage Integration:** Long-term backup and DR.  
- **Distributed Backup for SDDC:** Multi-AZ replication, failover orchestration.  

---

## 7. Best Practices
- Keep snapshot chains short (long chains affect performance).  
- Test restores regularly to validate backup integrity.  
- Use offsite/object storage for DR.  
- Monitor backup jobs, windows, and storage usage.  

---
# How Backups Rely on VM Snapshots

## 1. What is a VM Snapshot?
A **VM snapshot** is a **point-in-time copy** of a virtual machine’s state. It captures:

- **Disk state** (VMDKs)  
- **Memory state** (optional)  
- **VM configuration state (VMX file)**  

When a snapshot is created:
- The **base disk (VMDK)** is frozen.  
- A **delta disk (`-delta.vmdk`)** is created to store new writes.  
- A **redo log** tracks changes.  

This allows backups to read the frozen base disk while the VM keeps running.

---

## 2. Backup Workflow Using Snapshots

### Step 1: Snapshot Creation
- Backup software (e.g., Veeam, BRS, NetBackup) triggers a **snapshot via vSphere APIs (VADP)**.  
- The VM continues running without downtime.  
- If VMware Tools is installed, it performs **quiescing** for application-consistent backup.  

### Step 2: Redirect Writes to Delta Disk
- After snapshot, **all new writes are redirected to the delta disk**.  
- The base VMDK remains unchanged (frozen).  

### Step 3: Backup Reads from Base Disk
- Backup solution copies the **frozen base disk** safely.  
- **Changed Block Tracking (CBT)** identifies only modified blocks for incremental backups.  
- Ensures consistent backup images.  

### Step 4: Snapshot Removal
- Once backup finishes:
  - VMware merges delta disk changes back into the base VMDK.  
  - Snapshot files are deleted.  
- VM continues normally, now using the updated base disk.  

---

## 3. Why Snapshots Are Critical for Backup

| Purpose | Benefit of Snapshots |
|---------|----------------------|
| **Non-disruptive backups** | VM keeps running while backup reads frozen base disk. |
| **Data consistency** | Snapshots capture point-in-time state. |
| **Incremental backups** | CBT + snapshots allow efficient backups of only changed blocks. |
| **Isolation** | Backup tools never touch live writes, only snapshot copies. |

---

## 4. Application-Consistent vs Crash-Consistent Snapshots

- **Crash-Consistent**  
  - Snapshot taken instantly.  
  - Like pulling the power plug: VM will boot, but apps may need recovery.  

- **Application-Consistent**  
  - VMware Tools coordinates with the Guest OS and applications.  
  - Flushes I/O, pauses apps, ensures databases/filesystems are consistent.  
  - Recommended for databases and critical OLTP systems.  

---

## 5. Risks & Best Practices

- Too many snapshots degrade performance (I/O traverses snapshot chain).  
- Snapshots should be **temporary** — used only during backup windows.  
- Always **delete or consolidate snapshots** after backup completes.  
- For large VMs, prefer **storage-level snapshots** integrated with VMware APIs.  

---

## ✅ Summary
Backups in VMware **depend on snapshots** because they freeze the VM state while the VM keeps running. The backup tool copies data from the frozen disk image, not the live one, which ensures:

- **Consistency**  
- **Non-disruptive backups**  
- **Support for incremental (CBT-based) backups**  

After backup completes, the snapshot is removed and changes are merged back into the main disk.

---
# How Veeam Takes Backup and Stores It

Veeam Backup & Replication is a **data protection and disaster recovery solution** that provides image-based backups for virtual, physical, and cloud workloads.  
It ensures efficient storage, fast recovery, and minimal impact on production systems.

---

## 1. **Backup Process**

1. **Snapshot Creation**
   - Veeam integrates with **VMware vSphere (via VADP)** or **Microsoft Hyper-V (via VSS)** to create VM snapshots.
   - Snapshots capture the exact state of the VM at a point in time.
   - This allows backups to be taken without disrupting running workloads.

2. **Data Reading**
   - After the snapshot is taken, Veeam reads the **VM disk data (VMDK, VHDX, etc.)** directly from the underlying datastore.
   - Veeam can use multiple transport modes:
     - **Direct SAN Access** – Reads data directly from SAN storage.
     - **Hot-Add** – Mounts VM disks to the Veeam proxy VM for backup.
     - **NBD (Network Block Device)** – Reads data over the management network (slower).

3. **Data Processing**
   - Data is compressed, deduplicated, and optionally encrypted before leaving the source.
   - Change Block Tracking (CBT) is used to identify only changed blocks since the last backup, enabling **incremental backups**.

4. **Snapshot Removal**
   - Once the backup is complete, the VM snapshot is consolidated back into the original virtual disk to maintain performance.

---

## 2. **Backup Storage**

1. **Backup Repositories**
   - Veeam stores backup data in **backup repositories**. These can be:
     - Local disk
     - Network-attached storage (NAS)
     - Deduplication appliances
     - Object storage (Amazon S3, Azure Blob, etc.)

2. **Backup File Types**
   - **Full Backup (VBK)**: A complete image of the VM.
   - **Incremental Backup (VIB)**: Contains only the changed data since the last backup.
   - **Metadata File (VBM)**: Contains backup job metadata for restore operations.

3. **Storage Optimization**
   - Deduplication: Eliminates duplicate data across backups.
   - Compression: Reduces storage footprint.
   - Encryption: Ensures data security at rest and in transit.

---

## 3. **Backup Methods**

1. **Forward Incremental**
   - First backup: Full backup (VBK).
   - Subsequent backups: Incremental (VIB).
   - Periodic synthetic or active full backups are created.

2. **Reverse Incremental**
   - Always maintains a full backup file (VBK).
   - New restore points are injected into the full backup, and previous data blocks are moved to VIB files.

3. **Forever Forward Incremental**
   - Keeps a chain of one full backup plus incrementals.
   - Old restore points are merged as retention policy dictates.

---

## 4. **Advanced Features**

- **Scale-out Backup Repository (SOBR)**: Pools multiple repositories for scalability.
- **Backup Copy Jobs**: Copies backups to another location (secondary site, cloud).
- **WAN Acceleration**: Optimizes backup transfer to remote sites.
- **Cloud Tier**: Offloads backups to object storage for long-term retention.
- **Instant VM Recovery**: Runs a VM directly from the backup file for near-instant recovery.

---

## 5. **Restore & Recovery**

- File-level restore
- VM-level restore (instant recovery or full recovery)
- Application-aware restore (Exchange, SQL, Active Directory, etc.)
- Bare-metal recovery for physical workloads

---

## 6. **Transaction Failure Handling**

- If a snapshot or backup fails:
  - Veeam retries using alternative transport modes.
  - Logging and alerts are generated.
  - Transaction-consistent backups (via VSS) ensure applications remain consistent.
- Backup job integrity is verified with **health checks**.

---

✅ **Summary:**  
Veeam takes backups by leveraging VM snapshots, reading VM disk data using efficient transport modes, processing it with deduplication and compression, and storing it in repositories (block, file, or object storage). It provides multiple backup formats, incremental strategies, and recovery options, ensuring data availability, scalability, and security.
---
✅ **Key Takeaways**  
- SalesforceDB = **Postgres-based, cloud-native, immutable, multitenant DB**.  
- Combines **durability (immutable cloud storage)**, **availability (multi-AZ)**, **scalability (horizontal, cache+compute separation)**, and **multitenancy (tenant-per-row model with per-tenant features)**.  