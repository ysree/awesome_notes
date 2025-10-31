# Nutanix Interview and Related Concepts

## Table of Contents

- [Nutanix Interview Experiences](#nutanix-interview-experiences)
- [Debugging Ping Command](#debugging-ping-command)
- [Searching a String in a File](#searching-a-string-in-a-file)
- [Finding Files](#finding-files)
- [AWK vs grep vs sed](#awk-vs-grep-vs-sed)
- [Mounting and Debugging NFS Filesystem](#mounting-and-debugging-nfs-filesystem)
  - [Mounting an NFS Filesystem](#mounting-an-nfs-filesystem)
  - [Debugging NFS Mount Issues](#debugging-nfs-mount-issues)
- [Checking Process Performance in UNIX](#checking-process-performance-in-unix)
  - [Basic Process Monitoring](#basic-process-monitoring)
  - [CPU Performance](#cpu-performance)
  - [Memory Usage](#memory-usage)
  - [I/O Performance](#io-performance)
  - [Process-specific Performance](#process-specific-performance)
  - [Network Performance](#network-performance)
- [How Files are Stored in a UNIX System](#how-files-are-stored-in-a-unix-system)
  - [Filesystem Structure](#filesystem-structure)
  - [Inodes](#inodes)
  - [Data Blocks](#data-blocks)
  - [Directories](#directories)
  - [File Access Flow](#file-access-flow)
  - [Example](#example)
  - [ASCII Diagram](#ascii-diagram)
- [Linux File System Commands](#linux-file-system-commands)
- [DHCP (Dynamic Host Configuration Protocol)](#dhcp-dynamic-host-configuration-protocol)
  - [What is DHCP?](#what-is-dhcp)
  - [How DHCP Works (Basic Flow)](#how-dhcp-works-basic-flow)
  - [Components of DHCP](#components-of-dhcp)
  - [DHCP Lease](#dhcp-lease)
  - [Key Features of DHCP](#key-features-of-dhcp)
  - [Common DHCP Commands (Linux)](#common-dhcp-commands-linux)
  - [Release IP (Linux)](#release-ip-linux)
  - [Renew IP (Linux)](#renew-ip-linux)
  - [Check DHCP leases](#check-dhcp-leases)
- [Difference Between Symlink and Hardlink](#difference-between-symlink-and-hardlink)
  - [Hard Link](#hard-link)
  - [Symbolic Link (Symlink)](#symbolic-link-symlink)
- [Storage & File Sharing Protocols: SMB, NFS, FC](#storage--file-sharing-protocols-smb-nfs-fc)
  - [SMB (Server Message Block)](#smb-server-message-block)
  - [NFS (Network File System)](#nfs-network-file-system)
  - [FC (Fibre Channel)](#fc-fibre-channel)
  - [Comparison Table](#comparison-table)
- [BIOS (Basic Input/Output System)](#bios-basic-inputoutput-system)
  - [What is BIOS?](#what-is-bios)
  - [Key Functions of BIOS](#key-functions-of-bios)
  - [Types of BIOS](#types-of-bios)
  - [Accessing BIOS](#accessing-bios)
  - [Common BIOS Settings](#common-bios-settings)
  - [BIOS vs UEFI Summary](#bios-vs-uefi-summary)
  - [Notes](#notes)
- [Linux Boot Process](#linux-boot-process)
- [Troubleshooting Linux Bootloader Issues](#troubleshooting-linux-bootloader-issues)
  - [Common Bootloader Issues](#common-bootloader-issues)
  - [Troubleshooting Steps](#troubleshooting-steps)
- [Troubleshooting Blue Screen (BSOD) Errors](#troubleshooting-blue-screen-bsod-errors)
  - [Common Causes](#common-causes)
  - [Initial Steps](#initial-steps)
  - [Driver Issues](#driver-issues)
- [How Many ARP Packets Are Exchanged?](#how-many-arp-packets-are-exchanged)
  - [Total ARP Packets](#total-arp-packets)
- [PCIe Protocol (Peripheral Component Interconnect Express)](#pcie-protocol-peripheral-component-interconnect-express)
  - [What is PCIe?](#what-is-pcie)
  - [Key Features](#key-features)
  - [Use Cases](#use-cases)
- [Virtualization Concepts and Basics](#virtualization-concepts-and-basics)
  - [What is Virtualization?](#what-is-virtualization)
  - [Types of Virtualization](#types-of-virtualization)
  - [Hypervisors](#hypervisors)
  - [Virtual Machine (VM)](#virtual-machine-vm)
  - [Containers vs VMs](#containers-vs-vms)
  - [Virtualization Benefits](#virtualization-benefits)
  - [Virtualization Challenges](#virtualization-challenges)
  - [Key Virtualization Tools & Technologies](#key-virtualization-tools--technologies)
  - [Virtualization Benefits (Detailed)](#virtualization-benefits-detailed)
  - [Summary](#summary)
- [Docker Engine and Docker Containers](#docker-engine-and-docker-containers)
  - [Docker Engine](#docker-engine)
  - [Docker Containers](#docker-containers)
  - [Docker Engine vs Docker Containers](#docker-engine-vs-docker-containers)
  - [Summary](#summary-1)
- [Linux/Docker Control Groups (Cgroups)](#linuxdocker-control-groups-cgroups)
  - [Definition](#definition)
  - [Purpose](#purpose)
  - [Features](#features)
  - [Resource Controllers (Subsystems)](#resource-controllers-subsystems)
  - [How Cgroups Work](#how-cgroups-work)
  - [Example Use Case](#example-use-case)
  - [Summary](#summary-2)
- [Linux Namespaces](#linux-namespaces)
  - [What is a Namespace?](#what-is-a-namespace)
  - [Why Use Namespaces?](#why-use-namespaces)
  - [Types of Namespaces](#types-of-namespaces)
  - [How Namespaces Work](#how-namespaces-work)
  - [Example](#example)
- [Networking Concepts: Layers 2, 3, and 4](#networking-concepts-layers-2-3-and-4)
  - [Layer 2: Data Link Layer](#layer-2-data-link-layer)
  - [Layer 3: Network Layer](#layer-3-network-layer)
  - [Layer 4: Transport Layer](#layer-4-transport-layer)
  - [Summary Table](#summary-table)
  - [Real-World Example](#real-world-example)
- [BGP (Border Gateway Protocol)](#bgp-border-gateway-protocol)
  - [Definition](#definition-1)
  - [Types](#types)
  - [Why Needed](#why-needed)
  - [Key Attributes](#key-attributes)
  - [Advantages](#advantages)
  - [Challenges](#challenges)
  - [Security Measures](#security-measures)
- [ARP (Address Resolution Protocol)](#arp-address-resolution-protocol)
  - [Definition](#definition-2)
  - [Why ARP is Needed](#why-arp-is-needed)
  - [How ARP Works](#how-arp-works)
  - [ARP Message Types](#arp-message-types)
  - [Problems with ARP](#problems-with-arp)
- [STP (Spanning Tree Protocol)](#stp-spanning-tree-protocol)
  - [Definition](#definition-3)
  - [Why STP is Needed](#why-stp-is-needed)
- [VLAN (Virtual Local Area Network)](#vlan-virtual-local-area-network)
  - [Definition](#definition-4)
  - [Why VLAN is Needed](#why-vlan-is-needed)
  - [VLAN Components](#vlan-components)
- [How a Clustered System Works](#how-a-clustered-system-works)
  - [How it Works](#how-it-works)
  - [Reasons for Needing a Cluster](#reasons-for-needing-a-cluster)
  - [Common Use Cases](#common-use-cases)
  - [Example Cluster Types](#example-cluster-types)
  - [Interview Questions](#interview-questions)
- [Debugging Network Connectivity](#debugging-network-connectivity)
- [Range of IP Address Mapping to City](#range-of-ip-address-mapping-to-city)
- [Parameters of IOSTAT in Linux](#parameters-of-iostat-in-linux)
- [Finding the Gateway in Linux](#finding-the-gateway-in-linux)
- [How DNS Works](#how-dns-works)
- [Why NAT is Needed](#why-nat-is-needed)
- [General Test Cases for Login Function](#general-test-cases-for-login-function)
- [Software Development Life Cycle (SDLC)](#software-development-life-cycle-sdlc)
  - [Phases of SDLC](#phases-of-sdlc)
  - [SDLC Models](#sdlc-models)
  - [Benefits of SDLC](#benefits-of-sdlc)
  - [Summary](#summary-3)
- [Checking Dictionary, List, Tuple, Set in Python](#checking-dictionary-list-tuple-set-in-python)
- [File Operations](#file-operations)
- [Handling REST APIs, Authentication, and Security Testing](#handling-rest-apis-authentication-and-security-testing)
- [Basics of File Systems](#basics-of-file-systems)
  - [Key Components of a File System](#key-components-of-a-file-system)
  - [Types of File Systems](#types-of-file-systems)
  - [File System Operations](#file-system-operations)
  - [Data Blocks](#data-blocks-1)
  - [Inodes](#inodes-1)
  - [How it Works](#how-it-works-1)
  - [FAT](#fat)
- [Debugging EIO Error](#debugging-eio-error)
- [Kubernetes Primitives](#kubernetes-primitives)
- [Packet Travel in Network](#packet-travel-in-network)
- [DevOps Cycle](#devops-cycle-1)
- [Firewalls](#firewalls)
- [Additional Interview Problems](#additional-interview-problems)

## Nutanix Interview Experiences

Ntanix interview
https://www.geeksforgeeks.org/interview-experiences/nutanix-interview-experience-for-mts-qa-4-year-experience-language-python/
https://medium.com/@pavanboro/pytest-cheat-sheet-377964bbfab1

## Debugging Networking using Ping Command

# How to debug ping command. If it is not working how you will debug to find out the root cause for the same.
- Test local interface → 127.0.0.1 / self IP.
    ```
    # ifconfig  # or `ip addr`
    # ping 127.0.0.1  # test loopback
    ```
- Test DNS resolution if hostname used.
    ```
    # nslookup google.com  
    # dig google.com
    ```
- Check routing table and default gateway.
    ```
    # ip route show  
    # route -n
    ```
- Ping intermediate devices to isolate failure.
    ```
    # traceroute example.com  # Linux/macOS
    # tracert example.com     # Windows
    ```
- Check firewall / security rules on local and remote.
    - ICMP might be blocked by:
    - Local firewall (iptables, ufw)
    - Network firewall / security group
    - On Linux:
        ```
        # sudo iptables -L -v -n
        # sudo ufw status
        ```
- Try direct IP instead of hostname.
    ```
    # ping <gateway_ip>
    ```
- Use alternative tools to confirm network connectivity.
    - **curl, telnet, nc** → test TCP connectivity if ICMP is blocked.
    - **mtr** → combines ping + traceroute for continuous monitoring.
- Verify target host status.
---

## Searching a String in a File

# How will you search the given string in a given file.
```
$ $grep "string" file                    # Basic search
$ $grep -i "string" file                 # Case insensitive
$ $grep -n "string" file                 # With line numbers
$ $grep -r "string" directory/           # Recursive
$ $grep -v "string" file                 # Exclude pattern
$ $grep -c "string" file                 # Count matches
$ $grep -l "string" *.txt                # Show filenames only
$ $grep -A 5 "string" file               # Show 5 lines after match
$ $grep -B 5 "string" file               # Show 5 lines Before match
```

## Finding Files

# find files 
```
# Find all files
find /path/to/directory -type f

# Find file by exact name
find /path/to/directory -type f -name "file.txt"

# Case-insensitive search
find /path/to/directory -type f -iname "file.txt"

# Find files by extension
find /path/to/directory -type f -name "*.log"

# Find files modified in last 7 days
find /path/to/directory -type f -mtime -7

# Execute a command on found files
find /path/to/directory -type f -name "*.log" -exec cat {} \;

# Find empty files
find /path/to/directory -type f -empty
```

## AWK vs grep vs sed

# 🔍 AWK vs grep vs sed

| Feature/Tool     | grep                            | sed                                   | AWK                                                        |
| ---------------- | ------------------------------- | ------------------------------------- | ---------------------------------------------------------- |
| Purpose          | Search for patterns in text     | Stream editor for text transformation | Text processing, pattern matching, and reporting           |
| Operates On      | Lines of text                   | Lines of text                         | Lines of text split into fields                            |
| Field Support    | No                              | Limited                               | Yes (`$1, $2, ...`)                                        |
| Basic Usage      | `grep "pattern" file`           | `sed 's/old/new/' file`               | `awk '{print $1}' file`                                    |
| Pattern Matching | Regex-based                     | Regex-based                           | Regex-based                                                |
| Modify File      | No (just prints matching lines) | Yes (substitute, delete, insert)      | Usually prints output; can modify with shell redirection   |
| Use Cases        | Find matching lines             | Replace text, delete lines            | Extract columns, calculate sums/averages, generate reports |
| Complexity       | Simple                          | Medium                                | More complex, powerful for structured data                 |
| Example          | `grep "error" logfile`          | `sed 's/error/ERROR/g' logfile`       | `awk '{sum+=$2} END {print sum}' file`                     |


#### 🔹 Notes

* Use **grep** for simple searches.
* Use **sed** for quick in-line text edits.
* Use **AWK** for structured data extraction and reporting.
* All three support regular expressions and can be combined in pip

---

## Mounting and Debugging NFS Filesystem

# Mounting and Debugging an NFS Filesystem

### Mounting an NFS Filesystem
To mount a filesystem from an NFS server (e.g., IP `192.168.1.100` exporting `/data`) to a local directory (e.g., `/mnt/nfs`), use:

```bash
sudo mount -t nfs 192.168.1.100:/data /mnt/nfs
```

This mounts the remote directory to the local mount point. To make it permanent, add to `/etc/fstab`:
- FSTAB(file system table)
```bash
192.168.1.100:/data /mnt/nfs nfs defaults 0 0
```

Files in the NFS share can then be accessed as if local.

### Debugging NFS Mount Issues
If the mount fails, follow these steps:

1. **Check Server Reachability**:
   ```bash
   ping 192.168.1.100
   ```

2. **Verify Exported Directories**:
   ```bash
   showmount -e 192.168.1.100
   ```
   If not listed, check the server's `/etc/exports` or NFS service status:
   ```bash
   sudo systemctl status nfs-server
   ```

3. **Check Network and Firewall**:
   Ensure TCP/UDP ports 111 and 2049 are open:
   ```bash
   nc -zv 192.168.1.100 2049
   ```

4. **Verify Server Permissions**:
   Ensure the client’s IP is allowed in `/etc/exports` on the server, e.g.:
   ```bash
   /data 192.168.1.0/24(rw,sync,no_root_squash)
   ```
   Reload exports after changes:
   ```bash
   sudo exportfs -ra
   ```

5. **Check Client Logs**:
   View errors in system logs:
   ```bash
   dmesg | tail -n 20
   journalctl -xe
   ```

6. **Mount with Debug**:
   Use verbose mode for more details:
   ```bash
   sudo mount -v -t nfs 192.168.1.100:/data /mnt/nfs
   ```

7. **Ensure NFS Utilities**:
   Verify `nfs-utils` is installed on the client and the server’s hostname resolves if using DNS.

---

## Checking Process Performance in UNIX

How to check the performance of processes in a UNIX system?

# Checking Process Performance in UNIX

This guide explains how to monitor and check the performance of processes in a UNIX system.

---

### Basic Process Monitoring
- **List processes by CPU usage:**
  ```bash
  ps aux --sort=-%cpu | head -10
  ```
- **List processes by Memory usage:**
  ```bash
  ps aux --sort=-%mem | head -10
  ```
- **Real-time monitoring (CPU, Memory, Processes):**
  ```bash
  top
  ```
- **Interactive process viewer (if installed):**
  ```bash
  htop
  ```

---

### CPU Performance
- **Check system load average:**
  ```bash
  uptime uptime or w → Shows system load average (1, 5, 15 minutes).
  ```
- **Historical CPU usage (requires sysstat):**
  ```bash
  sar -u 1 5
  ```

---

### Memory Usage
- **Check memory and swap:**
  ```bash
  free -m
  ```
- **Monitor memory, CPU, I/O, and processes:**
  ```bash
  vmstat 1
  ```

---

### I/O Performance
- **Disk I/O and latency:**
  ```bash
  iostat -x 1
  ```
- **I/O per process (requires iotop):**
  ```bash
  iotop
  ```

---

### Process-specific Performance
- **Monitor a specific process (replace PID):**
  ```bash
  pidstat -p <PID> 1
  ```
- **Trace system calls of a process:**
  ```bash
  strace -p <PID>
  ```

---

### Network Performance
- **List process network usage:**
  ```bash
  netstat -tulnp
  ```
  or
  ```bash
  ss -tulnp
  ```
- **Monitor bandwidth usage (requires iftop or nload):**
  ```bash
  iftop
  ```
  ```bash
  nload
  ```

---

### Summary
- Use **top/htop** for real-time process overview.  
- Use **ps** for snapshots of CPU and memory usage.  
- Use **vmstat, iostat, pidstat** for detailed monitoring.  
- Use **sar** for historical performance.  

---

## How Files are Stored in a UNIX System

# How files stored in a UNIX system? File structure
In a **UNIX system**, files are stored using a structured model that combines **inodes, data blocks, and directories**.

---

### Filesystem Structure
A UNIX filesystem is divided into:
- **Boot block** → Boot information (used for system startup).
- **Superblock** → Metadata about the filesystem (size, free blocks, free inodes).
- **Inode table** → Stores metadata about files.
- **Data blocks** → Actual file content.
- **Directories** → Special files that map filenames to inode numbers.

---

### Inodes
Each file has an **inode**, which contains:
- File type (regular file, directory, symlink, etc.)
- Permissions (read/write/execute)
- Owner and group
- File size
- Timestamps (created, modified, accessed)
- **Pointers to data blocks** (where the actual file data is stored on disk)

👉 **Inodes do not store the filename**, only metadata + pointers.  
Filenames are stored in directories, which map **filename → inode number**.

---

### Data Blocks
The actual file content (text, binary, etc.) is stored in **blocks** on disk.  
- Small files fit into a few blocks.  
- Large files may need **direct, indirect, double-indirect, and triple-indirect pointers** from the inode to reach all their blocks.  

---

### Directories
Directories are just **special files** that contain entries:
`filename → inode number`

When you `ls -l`, the system looks up filenames in the directory, finds the inode number, then uses the inode to fetch metadata and locate data blocks.

---

### File Access Flow
When you open a file:
1. The system finds the **directory entry** → gets the inode number.
2. Reads the **inode** → finds metadata + block pointers.
3. Fetches **data blocks** → gives you file contents.

---

### Example
Suppose you create a file `hello.txt`:
1. A new inode is allocated (say inode #1234).
2. The filename `hello.txt` is stored in the directory, pointing to inode #1234.
3. The file’s content ("Hello World") is stored in data blocks.
4. Inode #1234 keeps pointers to those blocks and metadata.

---

### ASCII Diagram

```
Directory Entry
+---------------------+
|  hello.txt -> 1234  |  (Filename maps to inode number)
+---------------------+

Inode #1234 (Metadata + Pointers)
+------------------------------------------+
| Permissions: rw-r--r--                   |
| Owner: user                              |
| Size: 11 bytes                           |
| Timestamps: created, modified, accessed  |
| Data Block Pointers: [200, 201]          |
+------------------------------------------+

Data Blocks
+-------------+     +-------------+
| Block 200   |     | Block 201   |
| "Hello "    |     | "World"     |
+-------------+     +-------------+
```

---

### Summary
In UNIX, files are stored as **inodes + data blocks**, where:
- **Inode = metadata + pointers**
- **Data blocks = file content**
- **Directories = mapping of filenames to inodes**

---

## Linux File System Commands

# Linux File System Commands

- **pwd** → Print working directory  
- **ls** → List directory contents (`-l` long, `-a` all, `-h` human-readable)  
- **cd** → Change directory  
- **mkdir** → Create directory (`-p` create parent dirs)  
- **rmdir** → Remove empty directory  
- **touch** → Create empty file / update timestamp  
- **cp** → Copy files/directories (`-r` recursive)  
- **mv** → Move or rename files/directories  
- **rm** → Remove files/directories (`-r` recursive, `-f` force)  
- **cat** → View file contents  
- **less / more** → View large files page by page  
- **chmod** → Change file permissions  
- **chown** → Change file owner/group  
- **stat** → Show detailed file info  
- **df** → Show disk usage of filesystems (`-h` human-readable)  
- **du** → Show disk usage of directories/files (`-sh` summary)  
- **ln** → Create links (`-s` symbolic)  
- **mount** → Mount a filesystem  
- **umount** → Unmount a filesystem  
- **lsblk** → List block devices  
- **blkid** → Show filesystem UUIDs and types  
- **find** → Search for files/directories  
- **locate** → Quickly find files (requires updatedb)  
- **grep** → Search text inside files (`-r` recursive)  
- **tar** → Archive files (`-c` create, `-x` extract, `-z` gzip)  
- **gzip / gunzip** → Compress / uncompress files  
- **zip / unzip** → Compress / uncompress files  
- **fsck** → Check and repair filesystem  
- **tune2fs** → View / adjust ext2/ext3/ext4 filesystem parameters  
- **/dev/null** → Discard command output  
- **/proc** → Virtual filesystem for process and system info

---

## DHCP (Dynamic Host Configuration Protocol)

# DHCP (Dynamic Host Configuration Protocol)

#### 1. What is DHCP?
- DHCP is a network protocol used to **automatically assign IP addresses** and other network configuration parameters (subnet mask, gateway, DNS) to devices on a network.
- Eliminates the need for manual IP configuration on each device.
- Works in both IPv4 and IPv6 networks.

---

#### 2. How DHCP Works (Basic Flow)
1. **DHCPDISCOVER** → Client broadcasts to discover DHCP servers.  
2. **DHCPOFFER** → Server responds with an available IP address and configuration.  
3. **DHCPREQUEST** → Client requests the offered IP address.  
4. **DHCPACK** → Server acknowledges and assigns the IP address to the client.

---

#### 3. Components of DHCP
- **DHCP Server** → Manages IP address pool and leases.  
- **DHCP Client** → Device requesting IP configuration.  
- **DHCP Relay** → Forwards DHCP requests across different subnets.  

---

#### 4. DHCP Lease
- IP address assignment is temporary; client must renew before lease expires.  
- Lease duration can be configured on the DHCP server.

#### 5. Key Features of DHCP
- Automatic IP assignment: No need to manually configure IP addresses.
- IP reuse: IPs are leased temporarily, so they can be reused.
- Centralized management: All IPs are managed from the DHCP server.
- Support for large networks: Useful for dynamic environments like offices, data centers, and cloud networks.

---

#### 5. Common DHCP Commands (Linux)
- **View current IP info (DHCP assigned):**`ip addr show`

#### 6. Release IP (Linux):
- `sudo dhclient -r`

#### 7. Renew IP (Linux): 
- `sudo dhclient`

#### 8. Check DHCP leases: 
- `cat /var/lib/dhcp/dhclient.leases`

---

## Difference Between Symlink and Hardlink

# What is the difference between symlink and hardlink? Explain with inodes?

### 1. Hard Link
- A hard link is another name (alias) for the same file on disk.
- Both the original file and hard link point to the same inode.
- Inode: Stores metadata (permissions, owner, timestamps) and pointers to the data blocks.
- Effect: Both names are equal; deleting one does not delete the data until all hard links are removed

    **Command**: `ln file.txt hardlink.txt`

### 2. Symbolic Link (Symlink)
- A symlink is a special file that points to the path of another file.
- It has its own inode, separate from the target file.
- Effect: Acts like a shortcut. If the target file is deleted, the symlink becomes dangling.
- Symlinks can cross filesystems and can point to directories.

    **Command**: `ln -s file.txt hardlink.txt`

---

## Storage & File Sharing Protocols: SMB, NFS, FC

# Storage & File Sharing Protocols: SMB, NFS, FC

## 1. SMB (Server Message Block)
- **Type:** Network file sharing protocol.  
- **Purpose:** Allows computers to share files, printers, and serial ports over a network.  
- **Platform:** Commonly used in **Windows environments**; supported on Linux via Samba.  
- **Key Features:**
  - File and printer sharing.
  - Supports authentication and access control.
  - Operates over TCP/IP.
- **Ports:** 445 (modern SMB over TCP), 139 (NetBIOS).  

---

## 2. NFS (Network File System)
- **Type:** Distributed file system protocol.  
- **Purpose:** Enables clients to access files on a remote server as if they are local.  
- **Platform:** Mainly used in **UNIX/Linux environments**.  
- **Key Features:**
  - Transparent access to remote files.
  - Supports stateless and stateful protocols (NFSv3 vs NFSv4).  
  - Can mount remote directories.
- **Ports:** 2049 (NFS), plus portmapper (111) for older versions.  

---

## 3. FC (Fibre Channel)
- **Type:** High-speed network technology for storage.  
- **Purpose:** Connects servers to storage area networks (SANs) for block-level storage.  
- **Platform:** Enterprise storage; works at block level rather than file level.  
- **Key Features:**
  - High throughput (up to 128 Gbps in modern FC).  
  - Low latency, reliable for storage access.  
  - Supports zoning and multipathing.  
- **Cabling:** Uses fibre optic or copper cabling.  
- **Protocol:** Works over SCSI commands encapsulated in FC frames.  

---

## 4. Comparison Table

| Feature             | SMB                  | NFS                  | FC (Fibre Channel)   |
|--------------------|--------------------|--------------------|--------------------|
| Type               | File-level network protocol | File-level network protocol | Block-level storage protocol |
| Typical OS         | Windows, Linux (Samba) | UNIX/Linux           | Enterprise servers & storage |
| Access             | Files & printers    | Files only          | Raw block storage  |
| Performance        | Medium              | Medium              | High               |
| Protocol/Port      | TCP 445, 139        | TCP/2049            | Fibre Channel (FC frames) |
| Use Case           | Shared folders, printers | Shared directories | SAN storage, databases |

---

## BIOS (Basic Input/Output System)

# BIOS (Basic Input/Output System)

#### 1. What is BIOS?
- BIOS is firmware stored on a motherboard chip that **initializes hardware** during the boot process.  
- It provides a **basic interface between the operating system and hardware**.  
- Precedes the OS boot loader.

---

#### 2. Key Functions of BIOS
1. **POST (Power-On Self Test)**  
   - Checks CPU, memory, keyboard, storage devices, and other peripherals.
2. **Bootstrap Loader**  
   - Locates and loads the operating system from bootable devices.
3. **BIOS Setup Utility**  
   - Configures hardware settings (boot order, clock, CPU features, security settings).
4. **Hardware Abstraction**  
   - Provides low-level drivers for keyboard, display, and storage before OS takes over.

---

#### 3. Types of BIOS
- **Legacy BIOS**  
  - Original BIOS standard; 16-bit processor mode; supports MBR partition table (up to 2TB).  
- **UEFI (Unified Extensible Firmware Interface)**  
  - Modern replacement for BIOS; 32/64-bit mode; supports GPT partition table (>2TB), secure boot, and faster boot times.

---

#### 4. Accessing BIOS
- Usually during system startup by pressing keys like:
  - `Del`, `F2`, `F10`, `Esc` (varies by manufacturer).  

---

#### 5. Common BIOS Settings
- **Boot Order** → Choose which device to boot first.  
- **Date & Time** → Set system clock.  
- **CPU/Memory Configuration** → Enable virtualization, adjust frequency.  
- **Security** → Set BIOS password, enable/disable Secure Boot.  
- **Power Management** → Enable sleep modes, wake-on-LAN.  

---

#### 6. BIOS vs UEFI Summary

| Feature          | BIOS (Legacy)        | UEFI                     |
|-----------------|-------------------|-------------------------|
| Processor Mode   | 16-bit             | 32/64-bit               |
| Boot Method      | MBR                | GPT                     |
| Max Boot Disk    | 2 TB               | >2 TB                   |
| Secure Boot      | No                 | Yes                     |
| GUI / Features   | Text-based         | GUI, mouse support      |
| Boot Speed       | Slower             | Faster                  |

---

#### 7. Notes
- BIOS settings are stored in **CMOS memory** (powered by a small battery).  
- Updating BIOS (flashing) can fix bugs or add support for new hardware but must be done carefully.
 

---

## Linux Boot Process

# Linux Boot Process 
- **BIOS/UEFI** initializes hardware and finds bootloader.
- **Bootloader** loads the kernel and initramfs.
- **Kernel** sets up memory, drivers, and mounts root filesystem.
- **Init/Systemd** starts user space processes and services.
- **Login prompt** signals that the system is ready for user interaction.

- `Power ON → BIOS/UEFI → Bootloader (GRUB) → Kernel → Init (systemd) → Services → Login Prompt/GUI`
---

## Troubleshooting Linux Bootloader Issues

# Troubleshooting Linux Bootloader Issues

Bootloader problems prevent the system from loading the Linux kernel. The most common bootloaders are **GRUB** and **LILO**.  

### Common Bootloader Issues
1. **GRUB Rescue / Missing GRUB**  
   - Error messages like `GRUB rescue>` or `error: no such device`.
2. **Kernel Not Found**  
   - GRUB cannot find the kernel or initramfs image.
3. **Incorrect Boot Order / MBR issues**  
   - BIOS/UEFI points to the wrong disk.
4. **Corrupt GRUB Configuration**  
   - `grub.cfg` missing or misconfigured.
5. **Dual-boot conflicts**  
   - Installing another OS may overwrite GRUB.

### Troubleshooting Steps

- `Boot Error → Live CD/Rescue → Mount & Chroot → Reinstall/Update GRUB → Verify Config → Reboot`

---

## Troubleshooting Blue Screen (BSOD) Errors

# Troubleshooting Blue Screen (BSOD) Errors

A **Blue Screen of Death (BSOD)** occurs when Windows encounters a critical system error it cannot recover from. It usually displays a **stop code** and may include a memory dump.

---

### Common Causes
- Faulty or incompatible **device drivers**.
- Hardware issues (RAM, HDD/SSD, GPU).
- Corrupted **system files** or Windows updates.
- Malware or software conflicts.
- Overheating or power supply problems.

---

### Initial Steps
1. **Note the Stop Code**
   - Example: `0x0000007B (INACCESSIBLE_BOOT_DEVICE)`
   - Helps identify the cause.

2. **Safe Mode Boot**
   - Press **F8 / Shift + F8** (older Windows) or use Recovery Options.
   - Boot into **Safe Mode** to troubleshoot without loading all drivers.

3. **Disconnect New Hardware**
   - Remove recently installed devices.
   - Boot again to see if the BSOD persists.

---

### Driver Issues
- Use **Device Manager** to check for conflicts:

- `Start → Run → devmgmt.msc`

---

## How Many ARP Packets Are Exchanged?

# Two PCs are connected through three switches and a router, and they belong to different subnets. If one PC wants to communicate with the other for the first time, how many ARP packets will be exchanged?
#### Total ARP packets
- PC1 → router: 1 request + 1 reply = 2
- Router → PC2: 1 request + 1 reply = 2
- Total ARP packets = 4

---

## PCIe Protocol (Peripheral Component Interconnect Express)

# PCIe Protocol (Peripheral Component Interconnect Express)

#### 1. What is PCIe?
- **PCIe (PCI Express)** is a high-speed **serial computer expansion bus standard**.
- Used to connect **internal components** like graphics cards, SSDs, network cards, and other peripherals to the motherboard.
- Replaces older parallel PCI and PCI-X standards.

#### 2. Key Features
- **Low latency** due to point-to-point links.
- **Scalable bandwidth** by increasing lanes.
- **Hot-plug support** for some devices.
- **Backward and forward compatible** with different PCIe versions.

#### 3. Use Cases
- Graphics cards (GPU)
- NVMe SSDs
- Network cards (10/25/100GbE)
- RAID controllers
- Accelerator cards (AI/FPGA)

---

## Virtualization Concepts and Basics

# Virtualization Concepts and Basics

#### 1. What is Virtualization?
- **Virtualization** is the process of creating a **virtual version of a physical resource**, such as a server, storage device, network, or operating system.
- It allows multiple virtual instances to run on a **single physical machine**, sharing hardware resources efficiently.

---

#### 2. Types of Virtualization

##### a. Server Virtualization
- Multiple virtual servers run on one physical server.
- Hypervisors allocate CPU, memory, and storage dynamically.
- Examples: VMware ESXi, Microsoft Hyper-V, KVM.

##### b. Desktop Virtualization
- Run multiple desktop environments from a centralized server.
- Users access virtual desktops over the network.
- Examples: VDI (Virtual Desktop Infrastructure), Citrix Virtual Apps.

##### c. Application Virtualization
- Applications run in isolated containers or virtual environments.
- Does not require installation on host OS.
- Examples: Docker, VMware ThinApp.

##### d. Storage Virtualization
- Combines multiple storage devices into a single logical storage unit.
- Provides better utilization and management.
- Examples: SAN virtualization, NAS aggregation.

##### e. Network Virtualization
- Abstracts network resources to create virtual networks.
- Includes VLANs, virtual switches, and software-defined networking (SDN).

---

#### 3. Hypervisors
 **Hypervisor** is the software layer that enables virtualization.
- **Key Functions:**
    - Resource allocation - CPU, memory, storage, network
    - VM lifecycle management - create, start, stop, delete VMs
    - Hardware abstraction - presents virtual hardware to guest OS
    - Isolation - ensures VMs don't interfere with each other
    - Performance optimization - efficient resource utilization
- **Types:**
  1. **Type 1 (Bare-metal)**: Runs directly on hardware. Higher performance.
     - Examples: VMware ESXi, Microsoft Hyper-V, Xen
    ```
    ┌─────────────────────────────────┐
    │    VM1     │    VM2     │  VM3  │
    │  Guest OS  │  Guest OS  │Guest OS│
    ├─────────────────────────────────┤
    │         Hypervisor              │
    ├─────────────────────────────────┤
    │      Physical Hardware          │
    └─────────────────────────────────┘
    ```


  2. **Type 2 (Hosted)**: Runs on top of a host OS. Easier to set up, lower performance.
     - Examples: VMware Workstation, VirtualBox
    ```
    ┌─────────────────────────────────┐
    │    VM1     │    VM2     │  VM3  │
    │  Guest OS  │  Guest OS  │Guest OS│
    ├─────────────────────────────────┤
    │         Hypervisor              │
    ├─────────────────────────────────┤
    │        Host OS (Windows/Linux)  │
    ├─────────────────────────────────┤
    │      Physical Hardware          │
    └─────────────────────────────────┘
    ```
---

#### 4. Virtual Machine (VM)
- **VM** is an isolated guest operating system running on a hypervisor.
- Characteristics:
  - Virtual CPU, memory, storage, and network interfaces.
  - Runs as if on a physical machine.
- Benefits:
  - Isolation
  - Easy backup & migration
  - Resource consolidation

---

#### 5. Containers vs VMs
| Feature           | VM                  | Container            |
|------------------|-------------------|--------------------|
| OS Layer          | Full guest OS      | Shares host OS      |
| Boot Time         | Minutes            | Seconds             |
| Resource Usage    | High               | Low                 |
| Isolation         | Strong             | Moderate            |
| Examples          | VMware, KVM        | Docker, Podman      |

---

#### 6. Virtualization Benefits
- **Resource Utilization**: Better CPU, memory, and storage usage.
- **Cost Savings**: Fewer physical servers.
- **Isolation**: VMs or containers don’t affect each other.
- **Flexibility**: Easy to create, clone, and migrate workloads.
- **Disaster Recovery**: VMs can be backed up and restored quickly.

---

#### 7. Virtualization Challenges
- **Performance Overhead**: Hypervisors consume resources.
- **Complex Management**: Multiple VMs require monitoring.
- **Security Risks**: Misconfigured virtualization can expose hosts.
- **Licensing Costs**: Some hypervisors and VM OS require licenses.

---

#### 8. Key Virtualization Tools & Technologies
- **Hypervisors**: VMware ESXi, KVM, Hyper-V, Xen
- **Container Platforms**: Docker, Podman, Kubernetes
- **Management Tools**: vCenter, OpenStack, Proxmox
- **Storage Virtualization**: SAN, NAS, Ceph

#### 9. Virtualization Benefits
##### a. Resource Optimization
- Server consolidation - multiple VMs per physical server
- Higher utilization - 70-80% vs 15-20% physical
- Dynamic resource allocation

##### b. Operational Benefits

- Rapid provisioning - minutes vs days/weeks
- Easy backup/restore - snapshot functionality
- Live migration - move VMs without downtime
- Disaster recovery - replicate VMs to remote sites

##### c. Cost Benefits

- Reduced hardware costs
- Lower power consumption
- Reduced datacenter space
- Simplified management

#### 10. Summary
- Virtualization allows running **multiple isolated environments** on a single physical host.
- It improves **efficiency, scalability, and flexibility**.
- Types include **server, desktop, application, storage, and network virtualization**.
- Hypervisors manage virtual machines; containers provide lightweight virtualization.

---

## Docker Engine and Docker Containers

# Docker Engine and Docker Containers

#### 1. Docker Engine
- **Definition:** Docker Engine is the **core runtime** that enables building, running, and managing containers.
- **Components:**
  1. **Docker Daemon (`dockerd`)**  
     - Runs in the background and manages containers, images, networks, and volumes.
  2. **Docker CLI (`docker`)**  
     - Command-line tool to interact with Docker Daemon.
  3. **REST API**  
     - Enables programmatic control over Docker resources.

- **Features:**
  - Container lifecycle management
  - Image management
  - Networking and storage support
  - Works on Linux, Windows, macOS

---

#### 2. Docker Containers
- **Definition:** Docker Containers are **lightweight, portable, and isolated runtime environments** for applications.
- **Key Concepts:**
  - **Images:** Immutable templates used to create containers.
  - **Namespaces:** Provide isolation for processes, network, and file system.
  - **Cgroups:** Control resource allocation (CPU, memory, I/O) for containers.
  - **Union File System (UnionFS):** Enables layered images for efficient storage.

- **Benefits of Docker Containers:**
  - Lightweight and fast startup
  - Consistent environment across dev, test, and production
  - Easy portability and scaling
  - Isolation from host system and other containers

---

#### 3. Docker Engine vs Docker Containers

| Feature                  | Docker Engine                     | Docker Container                  |
|--------------------------|----------------------------------|----------------------------------|
| Definition               | Runtime that manages containers   | Lightweight isolated environment  |
| Function                 | Builds, runs, and manages images and containers | Runs applications based on images |
| Lifecycle                | Starts and stops containers       | Created, started, stopped, destroyed |
| Resource Usage           | Uses host OS resources to manage containers | Uses allocated CPU, memory, and storage within engine |
| Isolation                | Manages namespaces and cgroups   | Uses namespaces and cgroups for isolation |
| Portability              | Supports multiple platforms      | Highly portable across systems running Docker Engine |

---

#### 4. Summary
- **Docker Engine** is the core service that enables containerization.  
- **Docker Containers** are the runtime instances of Docker images, providing isolated, consistent environments.  
- Together, they simplify deployment, scaling, and management of applications.

---

## Linux/Docker Control Groups (Cgroups)

# Linux/Docker Control Groups (Cgroups) 

#### 1. Definition
- **Cgroups** (Control Groups) are a Linux kernel feature that **limits, isolates, and monitors resource usage** of process groups.
- Resources managed include **CPU, memory, disk I/O, and network bandwidth**.
- Cgroups are widely used in **containers** (Docker, Kubernetes) for resource management.

---

#### 2. Purpose
- Prevent a process or group of processes from consuming excessive resources.
- Ensure fair allocation of CPU, memory, and I/O among multiple processes or containers.
- Enable monitoring and accounting of resource usage.

---

#### 3. Features
1. **Resource Limiting**
   - Limit maximum CPU, memory, or disk usage for a group of processes.
2. **Prioritization**
   - Assign CPU shares to give higher priority to certain processes.
3. **Accounting**
   - Track resource usage for billing, monitoring, or debugging.
4. **Isolation**
   - Combine with namespaces to create isolated container environments.
5. **Control**
   - Dynamically adjust resources without stopping processes.

---

#### 4. Resource Controllers (Subsystems)
| Controller | Description |
|------------|-------------|
| `cpu`      | Limit CPU usage or allocate CPU shares. |
| `cpuacct`  | Track CPU usage for processes. |
| `cpuset`   | Assign specific CPU cores and memory nodes. |
| `memory`   | Limit and monitor memory usage. |
| `blkio`    | Control and monitor disk I/O. |
| `devices`  | Control access to devices (block/character). |
| `net_cls`  | Classify network traffic for bandwidth control. |
| `pids`     | Limit the number of processes. |

---

#### 5. How Cgroups Work
- Processes are grouped into **hierarchies**.
- Each hierarchy is associated with one or more **controllers**.
- Kernel enforces **resource limits and usage tracking** per hierarchy.
- Commands like `cgcreate`, `cgexec`, and `cgset` manage cgroups.

---

#### 6. Example Use Case
- Docker containers use cgroups to:
  - Limit container memory: `docker run --memory="500m" ubuntu`
  - Limit CPU shares: `docker run --cpus="1.5" ubuntu`
- Prevent one container from starving others of resources.

---

#### 7. Summary
- Cgroups provide **resource control and isolation** for process groups.
- Key for **containerization**, enabling multiple workloads to share hardware safely.
- Combined with **namespaces**, they provide **full isolation and resource management**.


---

## Linux Namespaces

# Linux Namespaces

#### 1. What is a Namespace?
- A **namespace** is a Linux kernel feature that provides **isolation of system resources**.
- Each namespace gives processes a separate view of the resource it manages (like process IDs, network, or mount points).
- Widely used in **containers (Docker, LXC, Kubernetes)** to create lightweight isolation.

---

#### 2. Why Use Namespaces?
- Provide **process isolation** (one container cannot see another’s processes).
- Enable **multi-tenancy** on the same host.
- Allow containers to act as if they have their **own OS resources**, while sharing the same kernel.

---

#### 3. Types of Namespaces

| Namespace | Isolates | Example Usage |
|-----------|-----------|---------------|
| **PID**   | Process IDs | Each container has its own process tree (`ps -ef` shows only its own processes). |
| **UTS**   | Hostname & domain | Containers can have different hostnames. |
| **Mount (mnt)** | Filesystem mount points | Each container can mount its own root filesystem. |
| **IPC**   | Inter-process communication | Separate message queues, semaphores, shared memory. |
| **Network (net)** | Network interfaces, IP addresses, routing | Each container can have its own IP address, routing table. |
| **User**  | User & group IDs | Root in a container is not root on the host. |
| **Cgroup**| Resource management | Isolates CPU, memory, and I/O usage. |

---

#### 4. How Namespaces Work
- When a process is created with `clone()` or `unshare()`, it can be placed in a new namespace.
- Processes in the same namespace share that resource’s view.
- Processes in different namespaces are **invisible to each other** for that resource.

---

#### 5. Example
```bash
# Create a new UTS namespace and set hostname
unshare -u /bin/bash
hostname container1
```

---

## Networking Concepts: Layers 2, 3, and 4

# Networking Concepts: Layers 2, 3, and 4

Networking is often explained using the **OSI Model** or the simplified **TCP/IP Model**.  
Here we will focus on **Layer 2 (Data Link), Layer 3 (Network), and Layer 4 (Transport)** in detail.

---

#### Layer 2: Data Link Layer
- **Function:** Responsible for node-to-node data transfer on the same local network (LAN).
- **Key Identifiers:** **MAC Address** (Media Access Control).
- **Protocols & Technologies:** Ethernet, Wi-Fi (802.11), PPP, ARP, VLAN (802.1Q).
- **Devices Working at L2:** Switches, Bridges.
- **Operations:**
  - Encapsulates packets into **frames**.
  - Adds **MAC addresses** for source & destination.
  - Provides **error detection** using CRC (Cyclic Redundancy Check).
  - Manages access to the physical medium (CSMA/CD in Ethernet).
- **Example:** Sending a file between two PCs connected to the same switch uses **Layer 2 communication**.

---

#### Layer 3: Network Layer
- **Function:** Responsible for end-to-end delivery of data across **different networks**.
- **Key Identifiers:** **IP Address** (IPv4 / IPv6).
- **Protocols:** IP, ICMP, ARP, OSPF, BGP, RIP, IS-IS.
- **Devices Working at L3:** Routers, Layer 3 Switches.
- **Operations:**
  - Handles **routing**: selecting the best path across multiple networks.
  - Encapsulates data into **packets**.
  - Adds **source and destination IP addresses**.
  - Can fragment packets if needed to match MTU size.
- **Example:** Sending data from a laptop in one subnet to a server in another subnet requires **Layer 3 routing**.

---

#### Layer 4: Transport Layer
- **Function:** Ensures **reliable or fast delivery of data** between applications across devices.
- **Key Identifiers:** **Port Numbers** (e.g., 80 for HTTP, 443 for HTTPS, 22 for SSH).
- **Protocols:** TCP, UDP, SCTP.
- **Operations:**
  - **Segmentation and Reassembly:** Breaks data into segments and reassembles at destination.
  - **TCP (Transmission Control Protocol):**
    - Reliable, connection-oriented.
    - Error detection, retransmission, flow control, congestion control.
    - Example: Web browsing (HTTP/HTTPS).
  - **UDP (User Datagram Protocol):**
    - Unreliable, connectionless.
    - Low latency, no retransmissions.
    - Example: Video streaming, VoIP, DNS.
- **Example:** When you open a webpage:
  - TCP establishes a connection (3-way handshake).
  - Data (HTTP packets) are transferred reliably.
  - Port numbers (80, 443) ensure the data reaches the right application.

---

### Summary Table

| Layer | OSI Name      | Key Identifier | Unit of Data | Devices       | Protocols & Examples |
|-------|---------------|----------------|--------------|---------------|----------------------|
| L2    | Data Link     | MAC Address    | Frame        | Switches      | Ethernet, ARP, VLAN  |
| L3    | Network       | IP Address     | Packet       | Routers       | IP, ICMP, OSPF, BGP  |
| L4    | Transport     | Port Number    | Segment      | Host Devices  | TCP, UDP, SCTP       |

---

### Real-World Example
When you access `https://example.com`:
1. **Layer 2 (Data Link):** Your NIC sends Ethernet frames to the local switch, using MAC addresses.
2. **Layer 3 (Network):** The router forwards packets based on destination IP (`93.184.216.34`).
3. **Layer 4 (Transport):** TCP (port 443) ensures reliable delivery of HTTPS traffic to the web server.

---

## BGP (Border Gateway Protocol)

# 🌍 BGP (Border Gateway Protocol) - Short Notes

- **Definition**: Path-vector routing protocol (RFC 4271), runs over TCP (port 179), used to exchange routes between Autonomous Systems (AS).  

- **Types**:  
  - **eBGP** → Between different ASes (ISP ↔ ISP).  
  - **iBGP** → Within same AS (internal route sharing).  

- **Why Needed**:  
  - Connects different networks globally.  
  - Supports policies, scalability, redundancy.  
  - Enables the Internet to function as a collection of ASes.  

- **Key Attributes**:  
  - **AS-Path** → Route history.  
  - **Next-Hop** → Next router to reach destination.  
  - **Local Preference** → Preferred exit.  
  - **MED** → Entry suggestion into an AS.  
  - **Weight** → Cisco-specific, higher is better.  

- **Advantages**:  
  - Scalable (handles >1M routes).  
  - Policy-based routing.  
  - Enables multi-homing with ISPs.  

- **Challenges**:  
  - Slow convergence.  
  - Misconfiguration can cause route leaks/hijacks.  
  - Security vulnerabilities.  

- **Security Measures**:  
  - Prefix filtering.  
  - RPKI (Resource Public Key Infrastructure).  
  - TTL Security (GTSM).  

---

## ARP (Address Resolution Protocol)

# 📘 ARP (Address Resolution Protocol)

#### 🔹 Definition
- ARP is a network protocol used to map an **IP address (Layer 3)** to a **MAC address (Layer 2)** in a local network.  
- Defined in **RFC 826**.  
- Works only within the same **LAN / broadcast domain**.  

---

#### 🔹 Why ARP is Needed
- Devices communicate using **MAC addresses** at the **data link layer**.  
- Applications use **IP addresses** at the **network layer**.  
- **ARP provides the translation** between IP and MAC, enabling communication.  

---

#### 🔹 How ARP Works
1. Device wants to send data to IP `192.168.1.10`.  
2. It checks its **ARP cache** for the MAC address.  
3. If not found → It sends a **broadcast ARP request**:  
   - “Who has 192.168.1.10? Tell me.”  
4. The target device replies with its **MAC address (unicast)**.  
5. Sender stores **IP-MAC mapping** in the ARP cache for future use.  

---

#### 🔹 ARP Message Types
- **ARP Request** → Broadcast asking for the MAC of an IP.  
- **ARP Reply** → Unicast response with the MAC address.  
- **Gratuitous ARP** → A device announces its own IP–MAC mapping (used for updates, IP conflict detection).  

---

#### 🔹 Problems with ARP
- **ARP Spoofing / Poisoning** → Attacker sends fake ARP replies → traffic redirection → Man-in-the-Middle (MITM).  
- **Broadcast Traffic Overhead** → Too many ARP requests in large networks can affect performance.  

---

## STP (Spanning Tree Protocol)

# STP (Spanning Tree Protocol)
#### 🔹 Definition

- STP (Spanning Tree Protocol) is a Layer 2 network protocol that prevents loops in a switched (bridged) network.
- Standard: IEEE 802.1D.
- STP ensures there is always a loop-free logical topology, even if redundant paths exist.

#### 🔹 Why STP is Needed
- In a network with redundant links, loops can occur.
    ##### Loops cause:
    - Broadcast storms (infinite broadcast circulation).
    - Multiple frame copies (same frame delivered many times).
    - MAC table instability (switches keep updating MAC addresses).
    - STP blocks redundant paths while keeping one active path → prevents loops.

## VLAN (Virtual Local Area Network)

 # Notes on VLAN (Virtual Local Area Network)
#### 🔹 Definition
- VLAN (Virtual LAN) is a logical subdivision of a physical network.
- It allows devices to be grouped into separate networks even if they are connected to the same - physical switch.
- Standard: IEEE 802.1Q (uses **VLAN tagging**).

#### 🔹 Why VLAN is Needed
- Segmentation – divides a large LAN into smaller, manageable networks.
- Security – isolates sensitive departments (e.g., HR vs. Finance).
- Reduced Broadcast Domain – limits broadcast traffic only within a VLAN.
- Performance – reduces unnecessary traffic, improving efficiency.
- Flexibility – group devices logically, not based on physical location.
- Scalability – supports large enterprise networks.

#### 🔹 VLAN Components
- Access Port: Connects to end devices (PCs, printers). Belongs to one VLAN.
- Trunk Port: Carries multiple VLANs between switches using VLAN tags.
- VLAN Tagging: Process of marking Ethernet frames with VLAN IDs (802.1Q).
- Inter-VLAN Routing: Required for communication between different VLANs (done by router or Layer 3 switch).

---

## How a Clustered System Works

# How a clustered system works.
#### How it works:
- Multiple Nodes – Two or more machines are connected through a high-speed network.
- Shared Workload – Tasks are distributed among nodes (load balancing).
- Shared Storage/State – Nodes may access the same database or file system to stay consistent.
- Failover Mechanism – If one node fails, another takes over its workload (high availability).
- Cluster Management Software – Coordinates communication, monitors health, and ensures transparent operation to end users.
#### Reasons for Needing a Cluster
- High Availability (HA):
    - If one server fails, another takes over (failover).
    - Ensures minimal downtime for critical applications.
- Scalability:
    - Add more nodes to handle growing workloads.
    - Supports more users, data, or traffic without replacing hardware.
- Performance:
    - Workload is distributed across multiple servers.
    - Improves speed and response time (parallel processing).
- Fault Tolerance:
    - No single point of failure.
    - Cluster keeps running even if one or more nodes fail.
- Cost Efficiency:
    - Use multiple inexpensive servers instead of one high-end, costly machine.
#### Common Use Cases
- Web Hosting: Distributing web traffic across multiple servers.
- Databases: Replicating data across nodes for reliability.
- High-Performance Computing (HPC): Running complex computations in parallel.
#### Example Cluster Types
- Load-Balancing Clusters: Distribute incoming requests (e.g., web servers).
- High-Availability Clusters: Ensure service continuity (e.g., database clusters).
#### Interview Questions
1. What is a clustered system and why is it used?
    - A clustered system is a group of interconnected computers that work together as a single system to provide high availability, scalability, and performance. It is used to ensure continuous service, handle increased workloads, and improve fault tolerance.
2. How does failover work in a clustered environment?
    - When a node fails, the cluster management software detects it and redirects the workload to another healthy node.
3. What are the differences between a clustered system and a distributed system?
   - A clustered system is a group of interconnected computers that work together as a single system, while a distributed system is a network of independent computers that communicate and coordinate their actions by passing messages.

## Debugging Network Connectivity

# given a network of devices, with an router and internet access is there but one of devices is not working , how would you fix and report the error ?
- Check power, cables, or Wi-Fi.
- Verify IP config (ipconfig/ifconfig).
- Run ping tests (self → router → internet).
- Check router DHCP and firewall.
- Swap cable/port or reconnect Wi-Fi.
- Reboot device and router.

## Range of IP Address Mapping to City

# There is range of ip address which is map to particular city and you are also given with one ip address identify whether the given ip address map to which particular city
```
IP addresses are easier to compare if converted to integers (32-bit).

Example: 192.168.1.1 

          → 192×256³ + 168×256² + 1×256 + 1.

Range:
-----
10.0.0.0 - 10.0.0.255 → Hyderabad
192.168.1.0 - 192.168.1.255 → Bangalore

- Check Target IP
- Convert target IP to integer.
- For each range, check:
  
  start≤target≤end

If true → return mapped city.
```

```
import ipaddress

def ip_to_int(ip):
    return int(ipaddress.IPv4Address(ip))

def find_city(ip, ranges):
    ip_val = ip_to_int(ip)
    for start, end, city in ranges:
        if ip_to_int(start) <= ip_val <= ip_to_int(end):
            return city
    return "Unknown City"

# Example IP ranges
ranges = [
    ("10.0.0.0", "10.0.0.255", "Hyderabad"),
    ("192.168.1.0", "192.168.1.255", "Bangalore"),
    ("172.16.0.0", "172.16.255.255", "Chennai")
]

# Test
print(find_city("10.0.0.5", ranges))        # Hyderabad
print(find_city("192.168.1.100", ranges))  # Bangalore
print(find_city("8.8.8.8", ranges))        # Unknown City
```

## Parameters of IOSTAT in Linux

# What are the parameters of IOSTAT in Linux?
- Parameters include: 
```
  -c (CPU statistics) - shows CPU usage statistics.
  -d (device statistics) - shows I/O statistics for each device.
  -x (extended device statistics) - shows detailed statistics like utilization and average wait time.
    -k (display in kilobytes) - shows statistics in KB instead of blocks.
    -m (display in megabytes) - shows statistics in MB.
    -p (display for specific devices) - shows statistics for specified devices only.
    -t (timestamp) - adds a timestamp to each line of output.
    -N (no headers) - suppresses the header line in the output.
    -h (human-readable) - shows sizes in human-readable format (e.g., 1K, 234M, 2G).
    -r (reset counters) - resets all I/O statistics counters.
```

## Finding the Gateway in Linux

# How do you find the gateway in Linux?
- You can find the gateway in Linux using commands like 'ip route' or 'route -n'.
- Use 'ip route' command: This displays the routing table, including the default gateway.

    **Example:** 'ip route show' will show output like 'default via 192.168.1.1 dev eth0'.
- Use 'route -n': This command also shows the routing table with the gateway information.

    **Example:** 'route -n' will display a table with 'UG' flag indicating the default gateway.

## How DNS Works

----------

# How does DNS work?
- DNS translates human-readable domain names into IP addresses for web access.
- DNS stands for Domain Name System, functioning like a phonebook for the internet.
- When you enter a URL, a DNS query is sent to resolve the domain name.
- The DNS resolver checks its cache; if not found, it queries DNS servers.
- Root DNS servers direct the query to TLD (Top-Level Domain) servers, e.g., .com.
- TLD servers point to authoritative DNS servers for the specific domain.
- Authoritative servers return the IP address, allowing the browser to connect.

----------

## Why NAT is Needed

# Why is NAT needed?
- NAT is needed to allow multiple devices on a private network to share a single public IP address.
- NAT helps conserve public IP addresses by allowing multiple devices on a private network to communicate with the internet using a single public IP address.
- Internet Access for Private Resources
    - Allows servers in private subnets (like databases, backend apps) to download patches, updates, or reach external APIs.
- Security
    - Blocks incoming traffic from the internet (since private IPs are not directly exposed).
- IP Address Conservation
    - Multiple private instances can share a single public IP through NAT.
- Use in Cloud (AWS, Azure, GCP)
    - In AWS, a NAT Gateway is a managed service for outbound internet access from private subnets.

    **Examples**: NAT is commonly used in home networks to allow multiple devices to access the internet through a single public IP address provided by the ISP.

---

## General Test Cases for Login Function

# What general test cases would you consider for a login function in a coding test?

- Positive Tests: Valid username & password, session/token creation, "Remember Me" functionality.
- Negative Tests: Invalid credentials, empty fields, SQL injection, XSS attacks.
- Boundary Tests: Max/min length, special characters in username/password.
- Security Tests: Account lockout, password hashing, HTTPS, session expiry.
- UI/UX Tests: Clear error messages, responsive design, "Forgot Password" link.

## Software Development Life Cycle (SDLC)

# Software Development Life Cycle (SDLC)

The **Software Development Life Cycle (SDLC)** is a structured process used to design, develop, test, and deploy software efficiently and effectively. It ensures high-quality software that meets or exceeds customer expectations.

### Phases of SDLC

#### 1.1 Requirement Gathering & Analysis
- Understand and document what the users need.
- Identify functional and non-functional requirements.
- Deliverable: Requirement Specification Document (SRS).

#### 1.2 System Design
- Convert requirements into a blueprint for software.
- High-level design (architecture) and low-level design (modules, database schema, interfaces).
- Deliverable: Design Documents (HLD, LLD).

#### 1.3 Implementation / Coding
- Developers write code based on design documents.
- Follow coding standards and best practices.
- Deliverable: Source Code.

#### 1.4 Testing
- Verify and validate the software against requirements.
- Types of testing: Unit, Integration, System, Acceptance.
- Deliverable: Test Reports, Bug Reports.

#### 1.5 Deployment
- Deploy the software to the production environment.
- Can be done in stages (staging, production) or fully.
- Deliverable: Live Software.

#### 1.6 Maintenance
- Post-deployment support: bug fixes, updates, and enhancements.
- Ensure software continues to meet business needs.

---

### SDLC Models

| Model                  | Key Features |
|------------------------|--------------|
| Waterfall              | Sequential, simple, early requirement clarity |
| Agile                  | Iterative, flexible, continuous feedback |
| Spiral                 | Risk-driven, iterative development |
| V-Model                | Testing-focused, each phase has corresponding test |
| Iterative              | Build in small parts, gradually improve |
| Big Bang               | Quick prototyping, less planning, higher risk |

---

### Benefits of SDLC
- Structured approach reduces project risks.
- Ensures high-quality software.
- Improves project planning and scheduling.
- Enhances communication among stakeholders.
- Supports documentation and future maintenance.

---

✅ **Summary:**
SDLC provides a roadmap for **software development**, from gathering requirements to maintenance, ensuring predictable, efficient, and quality-driven software delivery.

## Checking Dictionary, List, Tuple, Set in Python

# Can you explain how to check a dictionary, list, tuple, set in Python?
```
## Check the type
my_list = [1, 2, 3]
my_tuple = (1, 2, 3)
my_set = {1, 2, 3}
my_dict = {"name": "Alice", "age": 25, "city": "New York"}

print(isinstance(my_list, list))   # True
print(isinstance(my_tuple, tuple)) # True
print(isinstance(my_set, set))     # True
print(isinstance(my_dict, dict))   # True

## check for empty
if not my_list:
    print("List is empty")

## Check if an item exists
if "name" in my_dict:
    print("Name exists in dictionary")
if 2 in my_list:
    print("2 exists in list")  
if 3 in my_set:

## Iterate through items
for key, value in my_dict.items():
    print(f"{key}: {value}")

for item in my_list:
    print(item)

## Length
print(len(my_dict))  # 3
print(len(my_list))  # 3

```

## File Operations

# file operations 

- Open → "r", "w", "a", "x"
- Read → read(), readline(), readlines()
- Write / Append → write()
- File pointer → seek(), tell()
- Check/Delete → os.path.exists(), os.remove()
- Directory ops → os.listdir(), os.mkdir(), os.rmdir()
- Encryption/Decryption

## Handling REST APIs, Authentication, and Security Testing

# How do you handle REST APIs, authentication, and security testing?
#### 1.  Authentication
- Verify the identity of users or clients accessing your API.

Methods:
- API Keys → Simple key for each client.
- OAuth 2.0 / JWT → Token-based, stateless, scalable.
- Basic Auth over HTTPS → Username/password for simple scenarios.
Ensure tokens/keys are short-lived and revocable.       

#### 2.  Authorization
- Ensure users can only access allowed resources.
- Role-Based Access Control (RBAC):
- Assign roles (admin, user, guest) and enforce - permissions per endpoint.
- Attribute-Based Access Control (ABAC):
- Fine-grained access based on user attributes, resource, and action.

#### 3.  Input Validation
- Validate all inputs (query params, JSON payloads).
- Prevent injection attacks:
  - SQL/NoSQL injection
  - Command injection
- Reject unexpected/malformed requests.

#### 4. Data Protection
- Use HTTPS to encrypt data in transit.
- Encrypt sensitive data at rest (database encryption).
- Avoid exposing sensitive info in responses (passwords, keys, internal IDs).

#### 5. Rate Limiting & Throttling
- Prevent abuse and DoS attacks.
- Limit number of requests per client/IP per time window.
Example: 1000 requests/hour per API key.

#### 6. Security Headers
- Implement HTTP security headers:
- Content-Security-Policy
- X-Content-Type-Options
- X-Frame-Options
- Strict-Transport-Security

## Basics of File Systems

# Can you explain the basics of file systems?
#### 1. Key Components of a File System

- **Files:** Basic units of storage containing data or programs.  
- **Directories (Folders):** Containers for organizing files hierarchically.  
- **Metadata:** Information about a file, such as size, type, creation date, and permissions.  
- **Inodes (in Unix/Linux):** Data structure storing metadata and pointers to file blocks.  
- **Data Blocks:** Actual blocks on the disk where the file’s content is stored.  
- **File Allocation Table (FAT) or Journals:** Tracks which blocks are used/free.  

---

#### 2. Types of File Systems

#### FAT (File Allocation Table)
- Simple, used in older Windows systems and USB drives.  
- Versions: **FAT12, FAT16, FAT32**  

#### NTFS (New Technology File System)
- Used in modern Windows systems.  
- Supports permissions, encryption, large files, journaling.  

#### ext (Extended File System)
- Linux/Unix systems.  
- Versions: **ext2** (no journaling), **ext3** (journaling), **ext4** (modern)  

#### HFS+ / APFS
- Used in macOS.  
- APFS is optimized for SSDs, snapshots, and encryption.  

#### Network File Systems
- Examples: **NFS, SMB/CIFS**  
- Allow file sharing over a network.  

---

#### 3. File System Operations

- Create/Delete files or directories  
- Read/Write file data  
- Rename or move files  
- Set permissions  
- Search and list directory contents
- Mount/Unmount file systems

---
#### 4. Data Blocks
- Data blocks are the **actual units of storage** on the disk.  
- Every file’s content is stored in **one or more data blocks**.  
- Fixed size (e.g., 4 KB per block in ext4).  
- The OS needs a way to know **which blocks belong to which file**.

---

#### 5. Inodes (used in Unix/Linux file systems)
- **Inode** = Index Node, a data structure that stores **metadata of a file**:
  - File type, permissions, owner, timestamps  
  - File size  
  - Pointers to the **data blocks** of the file
- **Key point:** The inode **does NOT store the file name**; names are stored in the directory entry.  
- Inode numbers are unique within a file system.
- Inodes are typically stored in a fixed-size table on the disk.
- Fixed number of inodes created when the file system is formatted.
- If all inodes are used, no new files can be created even if there is free space.

#### How it works:
- Directory maps **filename → inode number**  
- Inode maps **inode number → data blocks**

**Example:**
- File: report.txt
- Directory entry: report.txt → inode 101
- Inode 101 → points to data blocks: 500, 501, 502

#### 6. FAT (File Allocation Table, used in FAT file systems)
- FAT is a table that tracks which blocks are used by which file.
- Each file has a starting block. FAT table links all blocks of the file like a chain.
- Unlike inodes, FAT stores block mapping in one centralized table.
- Useful for small/simple file systems like USB drives.

Example:
- File starts at block 500
- FAT[500] → 501
- FAT[501] → 502
- FAT[502] → -1 (end of file)
---

## Debugging EIO Error

# Debugging: NodeA → NodeB works, NodeB → NodeA fails

## Connectivity Tools
- `ping`, `traceroute` from both sides to check packet flow.  
- Use `tcpdump`/`wireshark` on NodeA to see if ICMP packets arrive.  

## Firewall / Security Rules
- Check `iptables`, `ufw`, `firewalld` on NodeA.  
- In cloud: verify **security groups / network ACLs** allow inbound ICMP.  

## IP & Interface Config
- `ip addr show` → confirm correct IP & subnet mask.  
- `ip route show` → verify valid routes back to NodeB.  

## Routing Issues
- Ensure **default gateway** is correct.  
- Check intermediate routers for asymmetric routes.  

## NAT / Load Balancer
- If NAT is in path, confirm **return translation** exists.  
- Load balancers may allow **one-way health check traffic only**.  
    NAT (Network Address Translation) is a method used by routers/firewalls to translate private IP addresses into public IP addresses (and vice versa).
    It allows devices in a private network (like your home Wi-Fi) to access the internet using a single public IP.

## ARP / Layer 2 Issues
- On NodeB: run `arp -n` for NodeA’s entry.  
- On NodeA: confirm ARP requests reach (`tcpdump arp`).  
- Check VLAN / port-security misconfig that could drop replies.  
    ARP (Address Resolution Protocol) is a network protocol used to map an IP address to a MAC address in a local network (LAN).
    IP address = Logical address (used for routing at Layer 3).
    MAC address = Physical address (used for actual delivery at Layer 2).
    Since devices need the MAC address to send packets inside a LAN, ARP helps them resolve “Who has this IP? Tell me your MAC.”

## Resource / OS Issues
- ICMP disabled on NodeA OS.  
- Network interface down or NIC bonding misconfigured.  

## Cloud / Infra Policies
- Cloud provider security policies blocking inbound ICMP.

## Packet Capture
- Capture on NodeA: `tcpdump -i <interface> icmp` to see if packets arrive.

---

## ✅ Summary Answer
Check in order →  
1. Firewall / Security rules  
2. IP & routes  
3. Traceroute & tcpdump  
4. NAT / ACLs  
5. ARP & L2 issues  
6. Cloud or infra policies  

# Firewalls
1. Packet Filtering Firewall
    - Inspects packets and allows or denies them based on predefined rules.
    - Operates at the network layer (Layer 3) of the OSI model.
    - Commonly used in routers and switches.
    - Simple and fast, but lacks deep inspection capabilities.
    - Checks source/destination IP, port, protocol
2. Stateful Inspection Firewall
    - Monitors the state of active connections and makes decisions based on the context of the traffic.
    - Operates at multiple layers (Layer 3 and Layer 4) of the OSI model.
    - More secure than packet filtering as it tracks connection states.
    - Can block unsolicited incoming traffic while allowing responses to outgoing requests.
3. Application/Proxy Firewall
    - Operates at the application layer (Layer 7) of the OSI model.
    - Inspects the content of the traffic, not just headers.
    - Can filter traffic based on specific applications or services (e.g., HTTP, FTP). HTTP/HTTPS filtering
    - Provides more granular control and can block specific types of content.
    - Often used in conjunction with other firewall types for enhanced security.
4. Next-Generation Firewall (NGFW)
    - Combines traditional firewall capabilities with advanced features like intrusion prevention, deep packet inspection, and application awareness.
    - Can identify and block sophisticated threats by analyzing traffic patterns and behaviors.
    - Often includes features like SSL/TLS inspection, antivirus, and anti-malware.
    - Provides comprehensive security for modern networks.
5. Software Firewall
    - Installed on individual devices (e.g., PCs, servers) to monitor and control incoming/outgoing traffic.
    - Can be more easily customized and updated than hardware firewalls.
    - Often includes features like application control, intrusion detection, and VPN support.
6. Hardware Firewall
    - Dedicated physical devices that provide firewall functionality.
    - Typically placed at the network perimeter to protect internal networks from external threats.
    - Can handle high traffic volumes and provide robust security features.
    - Often used in enterprise environments for comprehensive network protection.
7. Cloud Firewall   
    - Firewall services provided by cloud providers to protect cloud-based resources.
    - Can be configured to control traffic to and from cloud instances, applications, and services.
    - Often includes features like DDoS protection, web application firewall (WAF), and traffic monitoring.
    - Scalable and flexible to meet the needs of dynamic cloud environments.

--------------

## Additional Interview Problems

- including test automation frameworks (Selenium, Robot Framework, PyTest), 
- API testing (HTTP methods, response codes), 
- Python programming (data structures, error handling, coding challenges), 
- Git commands, 
- networking concepts (TCP vs. UDP), 
- software testing methodologies (unit, integration, test strategy, test bed), - virtualization (bare metal, VMs, IPMI), 
- and QA best practices. 
- The interview also included scenario-based problem-solving, 
- log analysis, 
- and test case design.
longest substring from a given text.

Design rate limiter and resource manager

Can you explain TestNG & Selenium?

Discuss TestNG execution orders and priorities.

What is the longest substring with no repeated characters?

Algorithm for Zig Zag traversal of a tree

Find middle element of a linked list in O(n) time

Can you provide an example of a program to find duplicates?

Can you explain how to implement 2 Arrays in the context of coding?

Explain the concept of 0/1 knapsack in dynamic programming.

How would you write a logic to generate your own encryption key?

 Find max Subarray element Create stack using list Mapping

  make dequeue without queue data type

1. Swap Linked list in pairs without modifying the values in the list's nodes. 
2. Using Binary search find square root of a number accurate upto 2 decimal digits 

1. Container with most water.
2. Maximum size rectangle binary sub-matrix 

Trapping rain water and 
rotten oranges leetcode. 

Generate Valid IP Addresses from String: Examples: Input: "255678166" Output: [“25.56.78.166”, “255.6.78.166”, "255.67.8.166", "255.67.81.66"] Explanation: These are the only valid possible IP addresses. Input: "25505011535" Output: [] Explanation: We cannot generate a valid IP address with this string.

concepts like greedy algorithms (similar to Jump Game), 
sliding window techniques (like finding the longest subarray with a sum constraint), 
and dynamic programming. 

searching and sorting

Design Snake and Ladders Low Level Design

Longest palindrome substring. 2. Parking lot design question 3. Debug python code.

dining philpsoper problem

 3. Longest common prefix 
 4. Find pivot element 
 5. String operations 
 6. Lambda function writing 
 7. Decorators usage 
  9. Selenium basic xpaths.

Find pairs of numbers whose sum equals 8
  How to check if a string contains a given substring.

Find a string in a given list.
Example
Input : list l1 = ['aaa', 'a', 'xyz']
        string = 'a' 
Output : True

Find email address in a given string. [use regex]

Merge given two list

Print occurrences of words in a given file.
Example
Input: a given file having some words -
       e.g.  cat cat dog cat hen hen
Output: a file - cat4 dog1 hen2

Shift all zero in the right side in a given list
Example
Input: [5, 6, 0, 1, 0, 10, 0, 2, 0]
Output: [5, 6, 1, 10, 2, 0, 0, 0, 0]

merge-k-sorted-arrays-set-2-different-sized-arrays
merging-intervals

Basics of trees and understanding about heap tree.

Given a string, find the count of unique characters present in the string.
Example
Input: FUNNY
OUTPUT: 4
Now remove the characters till one character is left.
FUNNY -> FUNY -> FUY -> FU -> F
OUTPUT: F 

You have one saree of size (n*m), fold this with the maximum number of folds ("x") and check if it is able to fit in the p*q box.

1. Check if a linkedlist is palindrome
Eg. head: 1->2->1->1->2->1 output: True
 head: 1->2->3->4 output: False

2. Given a string S containing only 0’s and 1’s, and given an integer N > 0, find the number of ways the string S can be split into N parts, with each part having at least 1 character, such that each part will have an equal number of 1’s in it.

Eg. S = 101, N = 2
Answer: 2
Explanation: 2 possible ways to divide: “1” and  “01”, “10” and “1”



Question on Xpath (Selenium)

Q2) Given two sorted list
ar1 =[1,2,3,4,7,7,12,18,19]
ar2 = [3, 4, 7, 7,14, 18, 19

Hands-on experience with testing tools like Selenium, Postman, and JMeter.


 transfer between multiple systems

 Multithreading questions
 multiple thread synchronization, lock etc

You can add and find largest sum but you can switch when ar1 and ar2 have same numbers.
Solution: you can base your solution on merge algo and recursively call your function to return largest sum possible.
test case automation, regression execution, performance testing, and integration testing.

 https://leetcode.com/discuss/post/4815315/nutanix-mts-3-bengaluru-feb-2024-by-anon-fimq/


 Q1. Zeros matrix from matrix
Ans. Transform a matrix by setting entire rows and columns to zero if an element is zero.
Identify all positions of zeros in the matrix.

Use two sets to track rows and columns that need to be zeroed.

Iterate through the matrix and set the identified rows and columns to zero.

Example: For matrix [[1,0,3],[4,5,6],[7,8,9]], the result is [[0,0,0],[4,0,6],[7,0,9]].

Maximise profit in stock selling

Min Stack implementaion with O1

dynamic programming

LRU, kv store instead of those popular ones on the lists such as lc 150 and grind 169. 

## Kubernetes Primitives

# 🔹 Kubernetes primitives

| Primitive       | Purpose                                      |
|-----------------|----------------------------------------------|
| Pod             | Smallest deployable unit, one or more containers |
| ReplicaSet      | Ensures N pod replicas are running          |
| Deployment      | Declarative updates & rolling deployments  |
| Service         | Stable network endpoint & load balancing   |
| ConfigMap       | Store configuration data                    |
| Secret          | Store sensitive data                        |
| Volume          | Pod storage                                 |
| PV / PVC        | Persistent storage allocation               |
| Namespace       | Virtual cluster / isolation                 |
| Job / CronJob   | Run tasks to completion / scheduled jobs   |
| StatefulSet     | Pods with stable IDs & storage              |
| DaemonSet       | Run pod on every node                        |
| Ingress         | External HTTP/S routing                     |
| Node            | Worker machine for running pods             |
| Endpoint        | IP addresses of pods backing a service      |

## Packet Travel in Network

# 🌐 How a Packet Travels in a Network

- **Application Layer (User action)**
    - You type a URL in your browser or send a message.  
    - The application (e.g., browser) generates data and hands it to the **transport layer**.

---
- **Transport Layer (TCP/UDP)**
    - **TCP** (connection-oriented, reliable) or **UDP** (faster, connectionless) adds a **port number**.  
    - Example: HTTP → Port 80, HTTPS → Port 443.  
    - Creates a **segment** (TCP) or **datagram** (UDP).

---

- **Network Layer (IP)**
    - Adds **IP addresses**:
    - **Source IP** = Computer A’s IP.  
    - **Destination IP** = Computer B’s IP.  
    - Creates an **IP packet**.

---

- **Data Link Layer (Ethernet/Wi-Fi)**
    - Adds **MAC addresses**:
    - **Source MAC** = Computer A’s NIC.  
    - **Destination MAC** = Next hop (e.g., router/gateway).  
    - If the MAC is unknown → **ARP** (Address Resolution Protocol) is used.  
    - Forms a **frame**.

---

- **Physical Layer (Transmission)**
    - Frame is converted into electrical signals, radio waves, or light pulses.  
    - Sent over copper cable, Wi-Fi, or fiber optic.

---

- **Switches (Within LAN)**
    - Switch reads the **MAC address** in the frame.  
    - Forwards it to the correct port toward the router.  
    - If unknown, it floods the frame until MAC is learned.

---

- **Router (Leaving the LAN)**
    - Router removes the data link layer info.  
    - Looks at the **destination IP**.  
    - Checks **routing table** for the next hop.  
    - Adds a new MAC address (towards next router).  
    - Sends packet on its way.

---

- **Across the Internet**
    - Packet hops across multiple routers.  
    - Each router:
    - Looks at **destination IP**.  
    - Forwards to the next router based on its routing table (using BGP, OSPF, etc.).  
    - At each hop, **MAC changes**, but **IP stays the same**.

---

- **Destination Network**
    - Packet reaches the final router (close to Computer B).  
    - Router sends packet into B’s LAN with **Computer B’s MAC address**.
---

- **Destination Machine**
    - NIC of Computer B receives the frame (MAC matches).  
    - Strips off Ethernet header.  
    - IP layer sees that the **destination IP matches** its own.  
    - Passes payload up to **transport layer**.  
    - TCP/UDP delivers data to the correct **application (via port number)**.  
    - Application (e.g., browser) processes data → shows the webpage.

---

## DevOps Cycle

# DevOps Cycle

DevOps is a set of practices combining **Development (Dev)** and **Operations (Ops)** to shorten the system development lifecycle while delivering features, fixes, and updates frequently and reliably.

---

#### 1. Plan
- Define project requirements, objectives, and deliverables.
- Tools: Jira, Trello, Azure DevOps Boards
- Activities:
  - Requirements gathering
  - User stories and backlog creation
  - Sprint planning

---

#### 2. Code
- Develop application features, enhancements, and fixes.
- Tools: Git, GitHub, GitLab, Bitbucket
- Activities:
  - Source code management
  - Version control
  - Peer code reviews
  - Branching strategies (GitFlow, Trunk-based)

---

#### 3. Build
- Compile and package code for deployment.
- Tools: Maven, Gradle, Jenkins, TeamCity
- Activities:
  - Compile code
  - Build artifacts (JAR, WAR, Docker images)
  - Static code analysis

---

#### 4. Test
- Validate application functionality, performance, and security.
- Tools: Selenium, JUnit, PyTest, TestNG
- Activities:
  - Unit testing
  - Integration testing
  - Automated regression testing
  - Continuous testing as part of CI/CD pipeline

---

#### 5. Release
- Prepare software for deployment into production or staging.
- Tools: Jenkins, Bamboo, GitLab CI/CD
- Activities:
  - Deployment packaging
  - Approval workflows
  - Artifact storage

---

#### 6. Deploy
- Deploy application to production or pre-production environments.
- Tools: Ansible, Chef, Puppet, Kubernetes, Docker
- Activities:
  - Infrastructure provisioning
  - Configuration management
  - Automated deployments
  - Canary releases or blue-green deployments

---

#### 7. Operate
- Ensure system runs smoothly in production.
- Tools: Nagios, Prometheus, ELK Stack, Grafana
- Activities:
  - Monitoring system performance
  - Logging and alerting
  - Incident management
  - Scaling and resource management

---

#### 8. Monitor
- Continuous observation and analysis of application and infrastructure.
- Tools: Splunk, Datadog, New Relic, Prometheus
- Activities:
  - Performance monitoring
  - Application health checks
  - Metrics and logs collection
  - Feedback loop for improvement

---

#### 9. Feedback Loop
- Insights from monitoring and operations guide future planning and development.
- Enables continuous improvement and faster issue resolution.

---

#### 10. Summary
- DevOps is **continuous, automated, and collaborative**.
- Key components: **Plan → Code → Build → Test → Release → Deploy → Operate → Monitor → Feedback**
- Tools integrate seamlessly to **ensure CI/CD pipelines, automation, monitoring, and faster delivery**.

---

## Firewalls

# Firewalls
1. Packet Filtering Firewall
    - Inspects packets and allows or denies them based on predefined rules.
    - Operates at the network layer (Layer 3) of the OSI model.
    - Commonly used in routers and switches.
    - Simple and fast, but lacks deep inspection capabilities.
    - Checks source/destination IP, port, protocol
2. Stateful Inspection Firewall
    - Monitors the state of active connections and makes decisions based on the context of the traffic.
    - Operates at multiple layers (Layer 3 and Layer 4) of the OSI model.
    - More secure than packet filtering as it tracks connection states.
    - Can block unsolicited incoming traffic while allowing responses to outgoing requests.
3. Application/Proxy Firewall
    - Operates at the application layer (Layer 7) of the OSI model.
    - Inspects the content of the traffic, not just headers.
    - Can filter traffic based on specific applications or services (e.g., HTTP, FTP). HTTP/HTTPS filtering
    - Provides more granular control and can block specific types of content.
    - Often used in conjunction with other firewall types for enhanced security.
4. Next-Generation Firewall (NGFW)
    - Combines traditional firewall capabilities with advanced features like intrusion prevention, deep packet inspection, and application awareness.
    - Can identify and block sophisticated threats by analyzing traffic patterns and behaviors.
    - Often includes features like SSL/TLS inspection, antivirus, and anti-malware.
    - Provides comprehensive security for modern networks.
5. Software Firewall
    - Installed on individual devices (e.g., PCs, servers) to monitor and control incoming/outgoing traffic.
    - Can be more easily customized and updated than hardware firewalls.
    - Often includes features like application control, intrusion detection, and VPN support.
6. Hardware Firewall
    - Dedicated physical devices that provide firewall functionality.
    - Typically placed at the network perimeter to protect internal networks from external threats.
    - Can handle high traffic volumes and provide robust security features.
    - Often used in enterprise environments for comprehensive network protection.
7. Cloud Firewall   
    - Firewall services provided by cloud providers to protect cloud-based resources.
    - Can be configured to control traffic to and from cloud instances, applications, and services.
    - Often includes features like DDoS protection, web application firewall (WAF), and traffic monitoring.
    - Scalable and flexible to meet the needs of dynamic cloud environments.

--------------

## Additional Interview Problems

- including test automation frameworks (Selenium, Robot Framework, PyTest), 
- API testing (HTTP methods, response codes), 
- Python programming (data structures, error handling, coding challenges), 
- Git commands, 
- networking concepts (TCP vs. UDP), 
- software testing methodologies (unit, integration, test strategy, test bed), - virtualization (bare metal, VMs, IPMI), 
- and QA best practices. 
- The interview also included scenario-based problem-solving, 
- log analysis, 
- and test case design.
longest substring from a given text.

Design rate limiter and resource manager

Can you explain TestNG & Selenium?

Discuss TestNG execution orders and priorities.

What is the longest substring with no repeated characters?

Algorithm for Zig Zag traversal of a tree

Find middle element of a linked list in O(n) time

Can you provide an example of a program to find duplicates?

Can you explain how to implement 2 Arrays in the context of coding?

Explain the concept of 0/1 knapsack in dynamic programming.

How would you write a logic to generate your own encryption key?

 Find max Subarray element Create stack using list Mapping

  make dequeue without queue data type

1. Swap Linked list in pairs without modifying the values in the list's nodes. 
2. Using Binary search find square root of a number accurate upto 2 decimal digits 

1. Container with most water.
2. Maximum size rectangle binary sub-matrix 

Trapping rain water and 
rotten oranges leetcode. 

Generate Valid IP Addresses from String: Examples: Input: "255678166" Output: [“25.56.78.166”, “255.6.78.166”, "255.67.8.166", "255.67.81.66"] Explanation: These are the only valid possible IP addresses. Input: "25505011535" Output: [] Explanation: We cannot generate a valid IP address with this string.

concepts like greedy algorithms (similar to Jump Game), 
sliding window techniques (like finding the longest subarray with a sum constraint), 
and dynamic programming. 

searching and sorting

Design Snake and Ladders Low Level Design

Longest palindrome substring. 2. Parking lot design question 3. Debug python code.

dining philpsoper problem

 3. Longest common prefix 
 4. Find pivot element 
 5. String operations 
 6. Lambda function writing 
 7. Decorators usage 
  9. Selenium basic xpaths.

Find pairs of numbers whose sum equals 8
  How to check if a string contains a given substring.

Find a string in a given list.
Example
Input : list l1 = ['aaa', 'a', 'xyz']
        string = 'a' 
Output : True

Find email address in a given string. [use regex]

Merge given two list

Print occurrences of words in a given file.
Example
Input: a given file having some words -
       e.g.  cat cat dog cat hen hen
Output: a file - cat4 dog1 hen2

Shift all zero in the right side in a given list
Example
Input: [5, 6, 0, 1, 0, 10, 0, 2, 0]
Output: [5, 6, 1, 10, 2, 0, 0, 0, 0]

merge-k-sorted-arrays-set-2-different-sized-arrays
merging-intervals

Basics of trees and understanding about heap tree.

Given a string, find the count of unique characters present in the string.
Example
Input: FUNNY
OUTPUT: 4
Now remove the characters till one character is left.
FUNNY -> FUNY -> FUY -> FU -> F
OUTPUT: F 

You have one saree of size (n*m), fold this with the maximum number of folds ("x") and check if it is able to fit in the p*q box.

1. Check if a linkedlist is palindrome
Eg. head: 1->2->1->1->2->1 output: True
 head: 1->2->3->4 output: False

2. Given a string S containing only 0’s and 1’s, and given an integer N > 0, find the number of ways the string S can be split into N parts, with each part having at least 1 character, such that each part will have an equal number of 1’s in it.

Eg. S = 101, N = 2
Answer: 2
Explanation: 2 possible ways to divide: “1” and  “01”, “10” and “1”



Question on Xpath (Selenium)

Q2) Given two sorted list
ar1 =[1,2,3,4,7,7,12,18,19]
ar2 = [3, 4, 7, 7,14, 18, 19

Hands-on experience with testing tools like Selenium, Postman, and JMeter.


 transfer between multiple systems

 Multithreading questions
 multiple thread synchronization, lock etc

You can add and find largest sum but you can switch when ar1 and ar2 have same numbers.
Solution: you can base your solution on merge algo and recursively call your function to return largest sum possible.
test case automation, regression execution, performance testing, and integration testing.

 https://leetcode.com/discuss/post/4815315/nutanix-mts-3-bengaluru-feb-2024-by-anon-fimq/


 Q1. Zeros matrix from matrix
Ans. Transform a matrix by setting entire rows and columns to zero if an element is zero.
Identify all positions of zeros in the matrix.

Use two sets to track rows and columns that need to be zeroed.

Iterate through the matrix and set the identified rows and columns to zero.

Example: For matrix [[1,0,3],[4,5,6],[7,8,9]], the result is [[0,0,0],[4,0,6],[7,0,9]].

Maximise profit in stock selling

Min Stack implementaion with O1

dynamic programming

LRU, kv store instead of those popular ones on the lists such as lc 150 and grind 169.