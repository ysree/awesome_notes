# Storage, Linux, and Testing Concepts

## Table of Contents

- [Erasure Coding and RAID Concepts](#erasure-coding-and-raid-concepts)
- [Storage Device Types: HDD, SSD, NVMe](#storage-device-types-hdd-ssd-nvme)
- [Block Sizes in vSAN and OS](#block-sizes-in-vsan-and-os)
- [I/O Types and Performance Measurement](#io-types-and-performance-measurement)
- [Inodes in Storage](#inodes-in-storage)
- [Deduplication](#deduplication)
- [Compression](#compression)
- [Global Deduplication](#global-deduplication)
- [vSAN and VM Backups](#vsan-and-vm-backups)
- [Changed Block Tracking (CBT)](#changed-block-tracking-cbt)
- [RPO vs RTO](#rpo-vs-rto)
- [vSphere Replication](#vsphere-replication)
- [Array-Based Replication](#array-based-replication)
- [Raw Device Mapping (RDM)](#raw-device-mapping-rdm)
- [vSCSI Controller](#vscsi-controller)
- [HTTP, FTP, and SFTP](#http-ftp-and-sftp)
- [SnapTree](#snaptree)
- [Hydrated Disk](#hydrated-disk)
- [VADP](#vadp)
- [VSS Snapshot](#vss-snapshot)
- [Linux Queuesing](#linux-queuesing)
- [Golden 3-2-1 Rule](#golden-3-2-1-rule)
- [NFS Mounts](#nfs-mounts)
- [Overcommit Techniques](#overcommit-techniques)
- [Thick and Thin Provisioning](#thick-and-thin-provisioning)
- [SSL and TLS](#ssl-and-tls)
- [Linux Fundamentals](#linux-fundamentals)
- [HTTP Status Codes](#http-status-codes)
- [Interview Problems](#interview-problems)
- [TestNG Notes](#testng-notes)
- [Google Drive System Test Use Cases](#google-drive-system-test-use-cases)
- [Files and Folders Test Scenarios](#files-and-folders-test-scenarios)
- [Webpage Troubleshooting](#webpage-troubleshooting)
- [Stress and Scale for Data Protection](#stress-and-scale-for-data-protection)
- [Coding Problem: Longest Substring with k Distinct Characters](#coding-problem-longest-substring-with-k-distinct-characters)
- [Comprehensive Test Plan for Distributed File System](#comprehensive-test-plan-for-distributed-file-system)
- [Nutanix Interview Experience](#nutanix-interview-experience)

## Erasure Coding and RAID Concepts

Erasure coding? How it works?
Data is split into chunks and then encoded with parity information.
These chunks (data + parity) are spread across multiple disks or nodes.
If some chunks are lost (because of disk/node failure), the missing data can be reconstructed from the remaining ones using the parity math.

Raid concepts (Raid 5 and Raid 6 is implemented using erasure coding )

Raid 0 - Striping (data is distributed ,no protection)
Raid 1 - Mirroring ( data is duplicated , 2 copies of data is always written, you will have data protection)
Raid 1 - 100 GB file ,200 GB is needed
Raid 5 - min of 4 hosts is required , Data reconstruction happens using parity
Eg. For 100 GB data will require only 133.33 GB because of parity occupies
Raid6 - 2 failures can be tolerated ,but required min 6 hosts to implemented. Its a combination of raid 0 and raid 1
eg. 100 GB will require 150 GB

## Storage Device Types: HDD, SSD, NVMe

HDD < SSD < Nvme
🔹 1. HDD (Hard Disk Drive)
    Technology: Mechanical — spinning magnetic platters with a moving read/write head.
    Speed: Slowest (typically 100–200 MB/s).
    Latency: High (milliseconds).
    Durability: Prone to mechanical wear and tear.
    Cost: Cheapest per GB.
    Use case: Bulk storage, backups, archives.

🔹 2. SSD (Solid State Drive)
    Technology: Flash memory (no moving parts).
    Speed: Faster than HDD (typically 500–600 MB/s for SATA SSD).
    Latency: Much lower (microseconds).
    Durability: More reliable, shock-resistant.
    Cost: More expensive than HDD, but cheaper than NVMe.
    Use case: OS boot drives, apps, general performance storage.

🔹 3. NVMe (Non-Volatile Memory Express SSD)
    Technology: Flash memory but connected via PCIe instead of SATA.
    Speed: Fastest (3,000–7,000 MB/s, even higher with PCIe Gen 4/5).
    Latency: Extremely low (tens of microseconds).
    Durability: Same as SSD, but with advanced controllers.
    Cost: Most expensive per GB.
    Use case: Databases, AI/ML, gaming, high-performance workloads.

## Block Sizes in vSAN and OS

Vsan block size is 32kb
o/s level - 4 KB in both linux and windows
apps - 4 MB blocks 

1GB of data -- 4kb blocks -- 5 mins (100 mb/s
32kb - 1 min -- (500 seconds)
4MB perf is good but it will waste the space 

Number of i/os will reduce when you increase block size 

[0,1,2]

## I/O Types and Performance Measurement

Random Read vs write 
Sequential Read vs write

Measuring storage performance in linux - fio /iometer
    iometer - windows and linux 
        - Iometer (I/O meter) is an open-source storage benchmarking tool.
        - It is used to measure I/O performance (read/write speed, latency, IOPS) of storage systems like HDDs, SSDs, NVMe, SAN, and NAS.
        Workload Simulation
            - Can simulate different types of workloads (database, web server, file server, mail server).
            - You can configure random vs sequential, read vs write, and block sizes.
        Performance Metrics
            - IOPS (Input/Output Operations per Second) – how many read/write operations per second.
            - Throughput (MB/s) – how much data is read/written per second.
            - Latency (ms/µs) – time taken to complete an I/O operation.
            - CPU utilization – how much CPU the workload consumes.
        Custom Workloads
            - You can define your own mix (e.g., 70% read, 30% write, 4K block size, random access).
            - This helps mimic real-world application behavior (like databases or VMs).
    fio - linux
        What is fio?
        - fio = Flexible I/O Tester, a widely used benchmarking and stress-testing tool for disks and filesystems on Linux (and other OSes).
        - Similar to Iometer (Windows), but command-line based and more flexible.
        - Used to test IOPS, throughput, latency, and queue depth under different workloads.
        Key Features
        - Supports random/sequential reads & writes.
        - Flexible I/O patterns: synchronous, asynchronous, direct I/O.
        - Can simulate workloads like databases, mail servers, web servers.
        - Reports IOPS, bandwidth, latency distribution.
        - Scriptable for automation (JSON output).

## Inodes in Storage

🔹 What is an Inode? 
    - command to check inode usage : df -i
    - command to see inode number : ls -i
    - An inode (index node) is a data structure used in Linux/Unix file systems (like ext3, ext4, XFS) to store metadata about a file.
    - Every file, directory, or object on the disk has an inode.
    - Think of an inode as the “ID card” of a file.
🔹 What’s inside an inode?
    - An inode stores metadata, but not the file name.
    - Typical inode fields include:
    - File type (regular file, directory, symlink, etc.)
    - Permissions (read/write/execute bits)
    - Owner (UID, GID)
    - File size
    - Number of links (hard links pointing to it)
    - Timestamps (created, modified, accessed)
    - Pointers to the actual data blocks on disk
    📌 The file name is stored separately in the directory entry, which maps a name → inode number.
🔹 How inodes work
    - When you create a file:
    - The filesystem assigns it a free inode.
    - Metadata goes into the inode, data goes into blocks.
    - When you open a file:
    - The OS looks up the inode number from the directory.
    - Then it loads the inode to find where the data blocks are.
🔹 Inode Limits
    - A filesystem has a fixed number of inodes created at formatting time.
    - Even if there is free space on disk, you cannot create new files if inodes run out.
    - Example: 100,000 tiny files (1 KB each) on a disk with only 100,000 inodes → disk is “full” even if space remains.
🔹 Inodes in Storage Systems
    - HDD/SSD: inodes work the same, they’re part of the filesystem.
    - Enterprise storage (Nutanix, NetApp, etc.): inodes still exist but are often abstracted behind the distributed filesystem.
    - Object storage (S3, Ceph, etc.): doesn’t use traditional inodes; metadata is handled differently.
🔹 Quick Analogy
    - File = Book
    - Inode = Library catalog card (author, title, location)
    - File name = Label on the shelf
    - The catalog (inode) tells you where to find the actual content (blocks on disk).

## Deduplication

Deduplication or Dedupe - Remove multiple identical books, keep one copy.
    Deduplication eliminates duplicate data and stores only unique chunks, saving space and improving efficiency.
    
    🔹 How It Works
    Identify duplicate blocks or files
        The system breaks data into chunks (fixed-size or variable-size).
        A fingerprint will be calculated for each chunk. Uses hashing (like SHA-1, MD5) to detect identical chunks.
    Store only one copy
        If a chunk already exists, instead of writing it again, the system references the existing one.
    Rebuild on access
        When you read the file, the system reconstructs it using unique chunks and fingerprints + references.
    🔹 Benefits
        - Saves storage space (sometimes up to 70–90%).
        - Reduces backup time and network bandwidth (less data to transfer).
        - Cuts costs on large storage systems.

## Compression

Compression -- reduce the size of data by encoding it more efficiently
    🔹 How It Works
    Identify patterns
        Compression algorithms look for repeating patterns or sequences in the data.
    Encode efficiently
        They replace these patterns with shorter representations (like symbols or codes).
    Store compressed data
        The result is a smaller file that takes up less space.
    Rebuild on access
        When you read the file, the system decompresses it back to its original form.
    🔹 Types of Compression
        - Lossless: No data is lost (e.g., ZIP, PNG). Used for text, code, databases.
        - Lossy: Some data is lost for higher compression (e.g., JPEG, MP3). Used for images, audio, video.
    🔹 Benefits
        - Saves storage space (typically 20–60% reduction).
        - Reduces transfer time over networks (smaller files).
        - Improves performance in some cases (less I/O).

## Global Deduplication

What is Global Deduplication?
    - Deduplication is the process of removing duplicate copies of data blocks to save storage space.
    - Global Deduplication extends this concept across multiple volumes, datastores, or even across the entire storage system, instead of deduplicating only within a single volume.
    - The system keeps a central index of all unique blocks and ensures duplicates are stored only once globally.

## vSAN and VM Backups

Vsan -- enable 

VM - backup of the vm 
1. Snapshot-based (not a real backup)
    - Create a VM snapshot in VMware, Hyper-V, or Nutanix AHV.
    - Saves the VM’s state at a point in time.
    - ⚠️ Not recommended as a long-term backup (snapshots grow and impact performance).

2. Image-level Backup (Preferred)
    - Backup software integrates with the hypervisor API (VMware VADP, Hyper-V VSS, Nutanix APIs).
    - Captures the entire VM disk image (VMDK, VHDX, QCOW2, etc.).
    - Can restore the whole VM or individual files.
    - Tools: Veeam, Commvault, Rubrik, NetBackup, Nutanix Mine.

3. Data backup solutions - veam ,emc avamar ,cohesity ,rubrik

## Changed Block Tracking (CBT)

What is CBT? VMware vSphere APIs for Data Protection (VADP)
    - Changed Block Tracking (CBT) is a VMware feature that keeps track of which disk blocks have changed since the last backup.
    - Instead of scanning the entire virtual disk (VMDK), the backup software only reads the changed blocks.
    - Works with VMware vSphere APIs for Data Protection (VADP).

## RPO vs RTO

RPO vs RTO (often confused)
    - RPO (Recovery Point Objective): How much data you can lose (measured in time). Acceptable data loss (backup age). Backup every 15 min = RPO 15 min
    - RTO (Recovery Time Objective): How quickly you must restore service after failure (measured in duration). Acceptable downtime (restore speed). Service must be up in 1 hour

## vSphere Replication

What is vSphere Replication? Software replication
    - vSphere Replication (VR) is VMware’s built-in feature for replicating VMs from one site to another.
    - It provides disaster recovery (DR) by keeping a copy of VMs at a secondary site.
    - Replication happens at the hypervisor level (not storage array level).
    - from multiple sources sites to single target site also possible ,but every site should have a VR appliance ,leverages CBT tracking technique and all esxi hosts would be pusehd with VR agents and NFC copy is performed from source to destination site for changed blocks

## Array-Based Replication

What is Array-Based Replication?
    - Array-Based Replication (ABR) is storage-level replication performed by the storage array itself, not by VMware or hypervisor.
    - The storage vendor’s array controller copies data from one storage array (source) to another array (target), typically across sites.
    - Used for high-performance disaster recovery (DR), business continuity, and zero/near-zero data loss.

## Raw Device Mapping (RDM)

What is Raw Device Mapping (RDM)? 
    - Whenever a vm is created , vmdk vm files are created on the vmfs file system
    - RDM is a method in VMware vSphere that allows a VM to directly access a LUN (logical unit number) on the storage array.
    - Instead of storing the VM’s virtual disk as a VMDK file inside a datastore, the VM is given a pointer file that maps straight to the raw LUN.

## vSCSI Controller

What is vscsi (Virtual SCSI Controller)
    - vSCSI controller is the virtual SCSI interface inside a VM that connects virtual disks (VMDKs) or RDMs to the VM.
    - VMware supports multiple types:
        - LSI Logic SAS/Parallel → older workloads.
        - VMware Paravirtual (PVSCSI) → high-performance, low CPU overhead.

## HTTP, FTP, and SFTP

HTTP, FTP, and SFTP in a clear and structured way.
    1. HTTP (Hypertext Transfer Protocol)
        Purpose: Transfer web content (HTML, images, videos) between a client (browser) and a web server.
        Port: 80 (default), HTTPS uses 443.
        Protocol Type: Application layer over TCP/IP.
        Security: HTTP is unencrypted, HTTPS is encrypted with SSL/TLS.
        Authentication: Usually via usernames/passwords, cookies, or tokens.
        Use Cases: Browsing websites, REST APIs, downloading files via browser.
        Pros: Widely supported, easy to use, can traverse firewalls easily.
        Cons: HTTP alone is unencrypted (HTTPS recommended).

    2. FTP (File Transfer Protocol)
        Purpose: Transfer files between a client and server.
        Port: 21 (control), 20 (data) in active mode.
        Protocol Type: Application layer, uses separate control and data channels.
        Security: FTP is unencrypted (credentials and data sent in plain text).
        Authentication: Username and password required.
        Use Cases: Upload/download files to a remote server, legacy file sharing.
        Pros: Simple, widely supported for file transfers.
        Cons: Insecure (passwords and files in plain text), firewalls can block data port.

    3. SFTP (SSH File Transfer Protocol)
        Purpose: Securely transfer files over SSH (Secure Shell).
        Port: 22 (default, same as SSH).
        Protocol Type: Application layer over encrypted SSH connection.
        Security: Encrypted by default (both credentials and data).
        Authentication: Username/password or SSH keys.
        Use Cases: Secure file transfers, automated backup scripts, server administration.
        Pros: Secure, encrypted, works through firewalls easily, single port (22).
        Cons: Slightly slower than FTP due to encryption overhead.

## SnapTree

snaptree 
    - SnapTree is a snapshot management technology used by some storage systems (e.g., NetApp, Nutanix, Pure Storage).
    - It allows creating space-efficient snapshots of data while supporting fast creation and deletion.
    - Snapshots are organized in a tree-like structure with pointers to data blocks rather than copying the data itself.
    Benefits of SnapTree
        - Fast snapshot creation → milliseconds, even for large volumes.
        - Space efficient → only changed blocks are stored.
        - Fast deletion → unlike linear snapshot chains.
        - Supports multiple branches → useful for backup, testing, and cloning.
        - Non-disruptive → snapshots don’t impact ongoing I/O significantly.

## Hydrated Disk

hydrated disk
    -  A fully hydrated disk or VM has all its storage blocks physically allocated, unlike thin-provisioned disks where storage is allocated only as data is written. It provides better performance and predictable storage usage.

## VADP

What is VADP?
    - VADP stands for vSphere APIs for Data Protection.
    - It is a VMware-provided API framework that allows backup and restore software to interact with VMware vSphere VMs efficiently.
    - It enables agentless backups (no need to install backup agents inside every VM).
    - VADP is VMware’s API framework for efficient, agentless backup and restore of VMs. It leverages snapshots and changed block tracking, supports multiple transport modes, and is widely used by third-party backup solutions.

## VSS Snapshot

vss snapshot ? - volume shadow copy service windows proprietary 
    - VSS Snapshot is a Windows mechanism to create application-consistent point-in-time copies of data. It ensures that applications like SQL or Exchange are in a consistent state when backup is taken, and works via requestors, writers, and providers.
    - volume shadow copy service -- For Consistent backups ,Requestor(3rd party backup solutions) ,they request the vss for a volume shadow copy ,then vss asks the write (app like sql server) to complete the pending transaction ,flush the cache ,discard the redo logs etc and then freezes I/o for few seconds and then takes a backup ,during this process I/o is queued and once the backup is completed I/0 is completed

## Linux Queuesing

Linux quescing is equivalent to windows vss 

## Golden 3-2-1 Rule

What is the golden 3-2-1 Rule?
    The 3-2-1 rule is a best practice guideline for data backup to ensure reliability and disaster recovery.
    It states:
    - 3 copies of your data
        Keep at least 3 copies: the original + 2 backups.
        Reduces risk of data loss due to corruption or hardware failure.
    - 2 different media types
        Store backups on at least 2 different types of storage media:
            Examples: disk, tape, cloud, NAS, external HDD.
        Protects against media-specific failures.
    - 1 offsite copy
        Keep at least 1 backup offsite (physically or in the cloud).
        Protects against disasters like fire, flood, or theft.
        Example: local backup on NAS + offsite backup in cloud storage.

## NFS Mounts

NFS Mounts Overview
    - NFS (Network File System) allows Linux/Unix clients to access remote file systems over the network.
    - When mounting an NFS share, you can choose hard or soft mount options.
    - Hard mount: 
        If the server is unreachable, the client will keep retrying indefinitely. Good for critical data.
    - Soft mount: 
        If the server is unreachable, the client will timeout and return an error

## Overcommit Techniques

CPU, memory and disk overcommit techniques
    - CPU Overcommit: Allocating more virtual CPUs (vCPUs) to VMs than the physical CPU cores available. Hypervisor schedules vCPUs on physical cores as needed.
    - Memory Overcommit: Allocating more virtual memory to VMs than the physical RAM available. Hypervisor uses techniques like ballooning, swapping, and compression to manage memory.
    - Disk Overcommit: Allocating more virtual disk space to VMs than the physical storage available. Thin provisioning is a common technique where storage is allocated on-demand as data is written.

## Thick and Thin Provisioning

Thick and Thin Provisioning
    - Thin Provisioning: Allocates storage space on-demand as data is written. Initial allocation may be small, and space is increased as needed.
    - Thick Provisioning: Allocates the entire specified storage space upfront, regardless of actual usage. This can lead to wasted space if not all allocated space is used.
    
## SSL and TLS

SSL, TLS

    SSL (Secure Sockets Layer) and TLS (Transport Layer Security) are cryptographic protocols designed to provide secure communication over a computer network. TLS is the successor to SSL.

    what is TLS?
        - TLS is the modern, secure version of SSL. It provides encryption, data integrity, and authentication for secure communication over networks.
        - TLS is widely used in web browsers, email, messaging apps, and other applications to ensure data privacy and security.

    how tls works?
        - Handshake: Client and server exchange messages to agree on encryption algorithms and keys.
        - Certificate Exchange: Server sends its digital certificate to the client for authentication.
        - Key Exchange: Both parties generate session keys for symmetric encryption.
        - Secure Communication: Data is encrypted using the session keys for confidentiality and integrity.

    Key Points:
    - SSL is now deprecated; use TLS.
    - Both protocols encrypt data in transit, ensuring confidentiality and integrity.
    - They use a combination of symmetric and asymmetric encryption.
    - Commonly used in HTTPS for secure web browsing.

    Procedure for SSL/TLS Implementation:
        1. Obtain a digital certificate from a trusted Certificate Authority (CA).
        2. Install the certificate on the web server.
        3. Configure the web server to use HTTPS and the installed certificate.
        4. Redirect HTTP traffic to HTTPS.
        5. Regularly update and renew the SSL/TLS certificate.

    command to generare tls certificate 
        openssl req -new -newkey rsa:2048 -days 365 -nodes -x509 -keyout mycert.pem -out mycert.pem

    install tls certificate on nginx
        1. Copy the certificate and private key files to the server.
        2. Edit the Nginx configuration file (nginx.conf or a specific site config).
        3. Add or update the following directives:
            ssl_certificate /path/to/your_certificate.crt;
            ssl_certificate_key /path/to/your_private.key;
            ssl_protocols TLSv1.2 TLSv1.3;
            ssl_ciphers 'HIGH:!aNULL:!MD5';
            ssl_prefer_server_ciphers on;
        4. Save the configuration file.
        5. Restart Nginx to apply changes: sudo systemctl restart nginx.

## Linux Fundamentals

-------------------------------

Linux things to read

-------------------------------------

Shell
conditions
loops while and for
awk sed cut grep
top command
sar command
find command
tail 
head
memory consumption
memory leaks ?
chmod ,chown

## HTTP Status Codes

HTTP Status Codes Cheat Sheet
    HTTP status codes tell the client (browser, API, app) what happened when it made a request to a server.

    🔹 1xx — Informational
        Request received, continuing process.
        100 Continue → Client should continue with the request.
        101 Switching Protocols → Server is switching protocols (e.g., HTTP to WebSocket).
        102 Processing → Server has received and is processing, but no response yet.

    🔹 2xx — Success
        The request was successfully received, understood, and accepted.
        200 OK → Request succeeded (most common).
        201 Created → A new resource was created (POST/PUT).
        202 Accepted → Request accepted but processing not finished.
        204 No Content → Request succeeded, but no body in response.

    🔹 3xx — Redirection
        Client must take additional action to complete the request.
        301 Moved Permanently → Resource permanently moved to a new URL.
        302 Found → Temporary redirection (resource temporarily moved).
        304 Not Modified → Cached version is still valid, no need to re-download.
        307 Temporary Redirect → Like 302, but method (POST/GET) must not change.
        308 Permanent Redirect → Like 301, but method must not change.

    🔹 4xx — Client Errors
        The problem is with the request sent by the client.
        400 Bad Request → Invalid request syntax.
        401 Unauthorized → Authentication required (login needed).
        403 Forbidden → Server understood request, but refuses to authorize it.
        404 Not Found → Resource doesn’t exist at given URL.
        405 Method Not Allowed → Request method (GET, POST, etc.) not supported.
        408 Request Timeout → Client took too long to send the request.
        409 Conflict → Request conflicts with current state of resource.
        429 Too Many Requests → Rate limit exceeded.

    🔹 5xx — Server Errors
        The request was valid, but the server failed to fulfill it.
        500 Internal Server Error → Generic error on server side.
        501 Not Implemented → Feature not supported by the server.
        502 Bad Gateway → Server acting as a gateway got invalid response.
        503 Service Unavailable → Server temporarily overloaded or down.
        504 Gateway Timeout → Upstream server didn’t respond in time.
        505 HTTP Version Not Supported → Server doesn’t support requested HTTP version.

## Interview Problems

Couple of interview problems 

Longest common prefix for a list of words
Find largest non-repeating character string from given

Interview problems
 
 1. Given two lists find the common elements or intersections.( sorting and without using sorting)
2. multiply two long string integers
3. Longest non-repeating substring
4. create nth sequence given n-1 th sequence ( Read a number)
Derive 312211 from 13112221
And from 13112221 to 312211
5.Regular expression for ipv4 address
6. spiral matrix problem

205 -- reset content
what is user agent

## TestNG Notes

TestNG Notes -- Almost everything is similar even in pytest framework ,All the below features are available even in pytest framework 

-------------------------------------------

Can generate xml and HTML report
can be installed from eclipse market place
in setup ,we can do things like initializing variables and doing prereq like registering a kms
testng.xml can be used to create and organize test suites ,we can also pass params to test classes or methods
Data -Driven Testing ,execute same test with different set of data
@Test ,to indicate it is a test method and this will execute it even without any main,main will be leveraged from testng
@Test(dependsonMenthods={"start"})
Suite --> Test Folder --> Class name --> If we just provide the class names all of the tests under a class will get executed
a Test case can be disabled either by modifying the xml or the @test tag @Test(enabled=false ) or in xml under methods tag add <exclude name = "Method name ">
If we have multiple tests and run only few of them ,then we can use include tag instead of excluding few tests
exclude name and include name tags can have regex like say we have 2 sets of tests templates and scgs ,we can say exclude templates.*
we can define groups and also groups with in groups

We can have the groups tag inside the suite level or test folder level
A test can belong to multiple groups ,now when we execute by group and if we run both groups ,the test runs 2 times
Follows Alphabetical order for execution
we can tag priority to test cases , lowest priority value will run first ,default priority is 0
we can set a timeout value for the test and we can also set a description for the test
we can annotate like this @parameters({"a","b"}) and pass paramneter name =a ,value =5 in xml

TestNG can have listeners like onTestStart ,onTestSuccess ,onTestFail et,onTest skipped etc ,we have to write a  listener class implementing itestlistener
2 ways to consume this --> in side the test case class ,annnotate the class with @Listeners("listener class name")
or in the testng xml add listener tag and provide listener class name

let's say we have 2 groups ,named functional and system ,then in testng xml we can define a new group with name full and include both functional and system under it and in the run tag provide this name full 

## Google Drive System Test Use Cases

---------------------------------------------------- 3rd round ----------------------------------------------

Interview question :

Come up Google Drive system Test use cases

## Files and Folders Test Scenarios

-------------------------------------------
Files and Folders 

## Answer

Answer 

Scale 
users -- > 
A single user --> 4GB Movie 
User --> 1000 files
Folders --> 1000 folders , max files
Across different folders ,push the max possible files
Saving of too many small files 

upload and download aspects 
Stress
--> users 
--> multiple devices ,phone ,laptop ,TV 
--> Parallel users trying to upload files in parallel 

Performance

--> 4KB block size ,32KB ,4MB
--> APIs response is coming in a reasonable time 

1000 files ,query for the files ,render in a reasonable time 

Resiliency

1.uploading file , internet outage , resume from where we stopped
2. slower internet
3. u.s india ,200 ms 
4. Bring down one of the node contributing to the HCI clusters storage
5. Redundancy is working or not
6. FC switch level redundancy

## Webpage Troubleshooting

webpage on linux server hosted in office 

website cant be reached

workday.vmware.com -- Ping workday.vmware.com 
nslookup  workday.vmware.com
ping ip 
curl call -- 
our gateway could be down
firewall is enabled and 80 is blocked on the server side 
Load balancer could be down 

## Stress and Scale for Data Protection

----------------------------4th round ------------------------------------------------------------

stress and scale scenarios for Data Protection of VMs managed by Vcenter below 

  Long running backups -- a vm with huge disk and running several applications
  backups in parallel - 100 vms backup in parallel
  opt in for backup for a large number of vms
  opt out of the backup for a large number of vms
  configure a backup at the smallest possible RPO for the max supported vms
   archive older backups of a large number of vms in parallel
   Have a  vcenter inventory with large number of vms (say 10k)
    remove a large number of vms from backup configuration

## Coding Problem: Longest Substring with k Distinct Characters

"""Given an integer k and a string s, find the length of the longest substring that contains at most k distinct characters.
For example, given s = "abcba" and k = 2, the longest substring with k distinct characters is "bcb".""

----------------------coding round -----------------------------

#get all substrings
#substrings with k distinct chars
#longest of them

def get_substrings(input_str):
    output_list = []
    temp_str=""
    for i in range(0,len(input_str)):
        for j in range(1,len(input_str)):
            temp_str = temp_str+input_str[j]
            output_list.append(temp_str)
        temp_str = ""

    return output_list

def substrings_k_distinct(input_str,k):
    all_substrings = get_substrings(input_str)
    substrings_k_dist=[]
    final_out=""
    for i in all_substrings:
        temp_list =[]
        temp_set= set(temp_list)
        for j in i :
            temp_set.add(j)
        if len(temp_set)==k:
            substrings_k_dist.append(i)
    for k in substrings_k_dist:
        if len(final_out)>len(k):
            pass
        else :
            final_out=k
    return final_out

output = substrings_k_distinct("abcba",2)
print (output)
output2 = substrings_k_distinct("abcbacd",3)
print (output2)
output3 = substrings_k_distinct("abcbbbbcccbdddadacb",2)
print (output3)

## Comprehensive Test Plan for Distributed File System

Test plan 

-------------------------------------------Last Round -----------------------------------------------------

Question : Come up with a comprehensive test plan for the below product

----------------------------------
multiple users can login to this product and browse what is present on the remote system

product is a distributed system running on multiple nodes

download files over wan link

on the destination server ,we can have multiple files of large size  and the download is async

---------------------------------------
Comprehensive Test Plan for Distributed File Browsing and Download System
    1. Introduction
        1.1 Product Overview
        The product is a distributed system running on multiple nodes, enabling multiple users to log in and browse content on remote systems. Users can download large files asynchronously over WAN links. The destination server supports multiple large files, and the system operates in a clustered environment (e.g., 2-node, 4-node, 8-node, up to 64-node clusters). Key components include:

        User Authentication: Local users, Active Directory (AD) integration, and federated identity providers (e.g., ADFS, Okta, Ping, vIDM).
        Browsing Protocols: SMB, NFS (v3, v4.1), S3/object storage/file storage across multi-cloud platforms (e.g., AWS, Azure), VMware/Citrix NFS file shares, vSAN file services.
        Networking: Overlay/underlay networks for node-to-node communication, NIC teaming for bandwidth enhancement (1 Gbps, 25 Gbps, 100 Gbps).
        Management Layer: UI and API layers on an overlay/underlay network with high availability (HA), backup/restore of DB entries.
        Storage: Supports RAID levels (RAID0, RAID1, RAID5, RAID6), mixed disk types (e.g., 1 SSD + 1 HDD), and high-latency disks.

    1.2 Test Objectives

        Verify functional correctness of user login, browsing, and async downloads.
        Ensure scalability, performance, and resiliency under various loads and failure scenarios.
        Validate upgrades, backups, and automation frameworks.
        Achieve high test coverage with minimal defects.

    1.3 Scope

        In Scope: Functional testing, stress/scale testing, performance testing, resiliency/chaos testing, UI/API testing, upgrade testing, and automation.
        Out of Scope: Hardware procurement, third-party vendor-specific deep dives (e.g., internal cloud provider implementations).

    1.4 Assumptions and Dependencies

        Test environments mimic production (e.g., multi-region clusters: 4 nodes with 2 in APAC and 2 in US-East).
        Access to tools like Jenkins for CI/CD, HPQC for reporting.

2. Test Strategy
    The test plan covers functional and non-functional aspects across layers: UI, API, Management (Mgmt) layer, and cluster nodes.
    2.1 Functional Testing
        Focus on core workflows: user login, browsing remote systems, and async file downloads.

        User Authentication:

        Create and manage local users (local to the product).
        Integrate with AD.
        Federated login via ADFS, Okta, Ping, vIDM.


        Browsing Remote Systems:

        Protocols: SMB, NFS (v3, v4.1), S3 (object/file storage across multi-cloud), VMware/Citrix NFS shares, vSAN file services.
        Multi-protocol support: Verify seamless switching/browsing across SMB, NFS, and S3 shares.
        Concurrent browsing: 20 users browsing the same NFS/SMB/S3 share simultaneously.
        Cover all share types, including max concurrency limits.


        File Downloads:

        Async downloads of large files (e.g., 4TB max file size) over WAN.
        Multi-file downloads from destination servers with large file counts.
        Node communication: Overlay/underlay networks for distributed cluster ops.
        Mgmt Layer: Backup/restore DB entries; ensure HA.


        Cluster Configurations:

        Test on 2-node, 4-node, 8-node clusters.
        Multi-region: 4 nodes (2 in APAC, 2 in US-East; within APAC, 1 node failover scenarios).
        Max scale: 64-node cluster (32 in Region1/AZ1-AZ2, 32 in Region2/AZ1-AZ2).


        Configuration Testing:

        Functional configs via files/UI/API.
        RAID levels: RAID0, RAID1, RAID5, RAID6.
        Disk mixes: 1 SSD + 1 HDD; max supported disk latency.



    2.2 Stress Testing
        Simulate high loads to identify bottlenecks, memory leaks, and resource exhaustion.

        User Load:

        100 users with 100 concurrent logins.
        Phased: 20 concurrent logins (1-min delay), downloads (5-min delay), repeat.


        Duration and Monitoring:

        5 days of uptime; analyze memory leaks.
        Plot graphs: CPU, memory, file descriptors.


        Browsing/Download Stress:

        20 users concurrently browsing the same NFS share.
        Max concurrency for all share types (NFS, SMB, S3).
        8-node cluster: Total files supported.
        64-node cluster: Total files supported at max scale.
        Download 4TB file under stress.


        Resource Stress:

        Cluster at 90% utilization; perform concurrent ops.
        NIC teaming: Test at 100 Gbps/25 Gbps/1 Gbps bandwidths.

    2.3 Scale Testing
        Validate horizontal/vertical scaling.

        Cluster Scaling:

        Scale-out: Add nodes to reach 100% capacity; replace failed node.
        Scale-in: Add disks to each node.
        Max: 64-node cluster; test total files/bandwidth.


        Load Scaling:

        Ramp from 20 to 100 concurrent users/downloads.
        Multi-region scaling: 32 nodes/region.



    2.4 Upgrade Testing
        Ensure seamless upgrades without data loss.

        Pre-Upgrade: Functional validation, backups.
        During Upgrade: Monitor ongoing downloads; Mgmt layer HA.
        Post-Upgrade: Restore DB entries; verify all protocols/users.

    2.5 Performance Testing
        Measure baselines and SLAs.

        Download Performance:

        Day 0: Download 4TB file in t seconds.
        Day 1: ≤ t seconds (or better).


        Login Performance:

        100 users concurrent login: Total time for all.
        Individual user: ≤ 120 seconds.


        API/UI Performance:

        Browse 1K files in NFS: ≤ 10 seconds.
        100 concurrent downloads from AWS S3 (constant parameters).


        Service Latencies:

        Mgmt layer internal latencies.
        WAN link latencies (200-250 ms).



    2.6 Resiliency Testing
        Inject failures to ensure fault tolerance.

        Cluster/Node Failures:

        64-node: Fail 32 nodes (Region1/Region2); AZ splits (16 nodes/AZ1, 16/AZ2).
        One node down/replace; OOM on Mgmt/Agent VM.
        APD/PDL on drives.


        Network Failures:

        Network partition between nodes.
        NIC teaming: Break one NIC.
        Router down; BGP peer down.
        Temporary NFS share access loss during browse.


        External Dependencies:

        AD/ADFS/Okta down/up; ensure downloads continue.
        Mgmt layer down (5-min recovery); downloads persist.
        Latency injection: 200-250 ms WAN.


        Storage/Chaos:

        Filesystem corruption; file corruption.
        Chaos in nodes/Mgmt layer (e.g., low-perf disks).
        Induce failure: Mgmt layer to nodes connectivity.



    2.7 UI Testing (Stress/Scale/Perf)

        Stress: 100 concurrent UI sessions browsing/downloads.
        Scale: Multi-region UI access.
        Perf: Page loads ≤ 10 seconds; API calls via UI.

3. Test Environment

    Hardware: Multi-node clusters (2-64 nodes); mixed disks/RAID; NIC teaming.
    Software: Product versions (current + upgrade paths); protocols enabled.
    Networks: Overlay/underlay; WAN simulation (latency tools).
    Monitoring: Tools for CPU/memory/file descriptors; graphs for leaks.

4. Test Automation
    Shared framework across teams (Functional, DevTest, Stress/Scale).

    Coverage:

    Functional: All APIs; config files.
    Stress/Scale: Multiple configs (e.g., cluster sizes).
    Upgrade: Pre/during/post hooks.


    Tools:

    Jenkins: Groovy pipelines (stages: Env setup, Build/Install, Trigger tests, Reporting, Update HPQC).
    Git: Versioning/branching (Main branch for 3-month product cycles; feature branches).


    Implementation:

    Automate all exposed APIs.
    Cross-team: Functional team owns API scripts; Stress team owns load generators.



5. Entry and Exit Criteria
    5.1 Entry Criteria

    Stable build available.
    Test environment provisioned (min 4-node cluster).
    Automation pipeline green on smoke tests.

    5.2 Exit Criteria

    100% of test cases executed.
    95% pass rate for Functional Verification Tests (FVT).
    95% pass rate for System Tests.
    No critical/severe defects open.
    Performance meets SLAs (e.g., ≤ t seconds for 4TB download).
    Resiliency: 100% recovery from injected failures within SLAs (e.g., 5-min Mgmt downtime).

## Nutanix Interview Experience

https://www.geeksforgeeks.org/interview-experiences/nutanix-interview-experience-for-mts-qa-4-year-experience-language-python/