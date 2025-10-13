# OS & Linux System Concepts

Linux is a powerful, open-source operating system widely used for servers, desktops, and embedded systems. Below are its key concepts and components.

---

#### 1. Linux Kernel
- **Definition:** The core of the operating system managing hardware and system resources.
- **Functions:**
  - Process management (scheduling, creation, termination)
  - Memory management (virtual memory, paging, swapping)
  - Device management (drivers for storage, network, peripherals)
  - System call interface for user-space applications
- **Kernel Types:** Monolithic (Linux), Microkernel, Hybrid

---

#### 2. Linux File System
- **Hierarchy:** Root (`/`) -> `/home`, `/etc`, `/var`, `/usr` etc.
- **File Types:**
  - Regular files, directories, symbolic links, device files, sockets, pipes
- **File Permissions:** Read (r), Write (w), Execute (x) for Owner, Group, Others
- **Key Commands:** `ls`, `chmod`, `chown`, `stat`, `df`, `du`
- **Mounting:** `mount`, `umount` to attach/detach filesystems

---

#### 3. Processes
- **Definition:** A running instance of a program
- **Attributes:** PID (Process ID), PPID (Parent Process ID), UID, GID
- **Types:** Foreground, Background, Daemons
- **Commands:** `ps`, `top`, `htop`, `kill`, `nice`, `renice`, `jobs`, `fg`, `bg`

---

#### 4. Users and Groups
- **Users:** Individual accounts to log in and run programs
- **Groups:** Collections of users for permission management
- **Configuration Files:** `/etc/passwd`, `/etc/group`, `/etc/shadow`
- **Commands:** `useradd`, `usermod`, `groupadd`, `passwd`, `id`, `whoami`

---

#### 5. Package Management
- **Purpose:** Install, update, remove software packages
- **Package Managers:**
  - Debian/Ubuntu: `apt`, `dpkg`
  - RedHat/CentOS: `yum`, `dnf`, `rpm`
- **Example Commands:** `apt install package`, `yum update`, `dpkg -i package.deb`

---

#### 6. Networking
- **Concepts:** IP addressing, subnetting, routing
- **Interfaces:** `eth0`, `lo`, `wlan0`
- **Tools:** `ifconfig`, `ip`, `ping`, `netstat`, `ss`, `traceroute`
- **Services:** SSH, FTP, HTTP, DNS
- **Firewalls:** `iptables`, `ufw`

---

## 7. Shell and Scripting
- **Shell:** Interface between user and kernel (`bash`, `zsh`, `sh`)
- **Features:** Command execution, variables, loops, conditional statements, functions
- **Scripting:** Automates repetitive tasks, monitoring, backups
- **Example:** Bash scripts for system monitoring

---

#### 8. Storage and Disk Management
- **Concepts:** Partitions, mount points, logical volumes (LVM)
- **File Systems:** ext4, xfs, btrfs, ntfs
- **Commands:** `fdisk`, `lsblk`, `mount`, `umount`, `df`, `du`, `mkfs`

---

#### 9. Logging and Monitoring
- **Logs:** Stored in `/var/log` (system, kernel, application logs)
- **Commands:** `journalctl`, `dmesg`, `tail -f /var/log/syslog`
- **Monitoring Tools:** `top`, `htop`, `iotop`, `vmstat`, `sar`

---

#### 10. Security
- **Concepts:** File permissions, sudo access, SELinux/AppArmor, key-based SSH
- **Commands:** `chmod`, `chown`, `sudo`, `ssh-keygen`
- **Best Practices:** Regular updates, firewall rules, restricted root access

---

#### 11. Virtualization and Containers
- **Virtualization:** KVM, VMware, VirtualBox
- **Containers:** Docker, LXC, Kubernetes
- **Commands:** `docker run`, `docker ps`, `lxc-create`

---

#### 12. System Services and Daemons
- **Init Systems:** SysV, Upstart, systemd
- **Commands:** `systemctl start/stop/restart`, `service status`
- **Examples:** Web servers, database servers, cron jobs

---

#### 13. Cron Jobs
- **Purpose:** Automate repetitive tasks at scheduled times
- **Files:** `/etc/crontab`, `/var/spool/cron`
- **Commands:** `crontab -e`, `cron.d`, `at`

---

#### 14. Summary
- Linux is **modular, multi-user, multitasking**, and secure
- Core components: **Kernel, Shell, File System, Processes, Users, Networking**
- Linux provides **flexibility, automation, and reliability** for servers and applications

---

# OS Concepts

#### a. Processes
- Program in execution.  
- Managed using **Process Control Blocks (PCB)**.  
- Supports **multitasking** and **multiprogramming**.  

#### b. Threads
- Lightweight processes sharing the same memory space.  
- Supports **parallel execution** within a process.  

#### c. CPU Scheduling
- Determines which process gets **CPU next**.  
- Algorithms: **FCFS, SJF, Round Robin, Priority, MLFQ**.  

#### d. Synchronization
- Ensures **orderly execution** of processes accessing shared resources.  
- Mechanisms: **Mutex, Semaphores, Monitors**.  

#### e. Deadlocks
- Occurs when **two or more processes wait indefinitely** for resources held by each other.  
- Handling: **Prevention, Avoidance, Detection, Recovery**.  

#### f. Paging & Segmentation
- **Paging** → Divides memory into fixed-size pages.  
- **Segmentation** → Divides memory into variable-size logical segments.  

#### g. Virtual Memory
- Allows processes to use **more memory than physically available** using disk as extension of RAM.  

#### h. Preemption
- Temporarily suspending a running process to allocate CPU to another process (**used in preemptive scheduling**).  

---

# What is context switching in operating systems

**Definition:** Context switching is the process by which an operating system (OS) saves the state of a currently running process and restores the state of the next scheduled process so that CPU can switch from executing one process to another.

#### Why Context Switching is Needed

- **Multitasking**: Multiple processes share the CPU. Context switching allows the OS to switch between processes efficiently.
- **Time-sharing:** Ensures that each process gets CPU time, providing responsiveness in interactive systems.
- **Interrupt Handling:** When an interrupt occurs, the CPU needs to switch context to handle it.

#### Components of Process State

During context switching, the OS saves/restores the following information:

- **Program Counter (PC):** Points to the next instruction to execute.
- **CPU Registers:** Accumulators, index registers, stack pointer, etc.
- **Memory Management Info:** Page tables, segment tables.
- **Process Control Block (PCB):** Contains process metadata like process ID, priority, status, CPU state

---


 CPU scheduling algorithms and the important parameters to decide the most efficient algorithms


# CPU Scheduling Algorithms

CPU scheduling decides which process in the **ready queue** gets CPU time when the CPU is free.

---

#### Important Parameters

- **CPU Utilization** – Keep CPU as busy as possible.  
- **Throughput** – Number of processes completed per unit time.  
- **Turnaround Time** – Time from submission to completion.  
  - `Turnaround Time = Completion Time – Arrival Time`  
- **Waiting Time** – Total time spent in ready queue.  
  - `Waiting Time = Turnaround Time – Burst Time`  
- **Response Time** – Time from submission until first response.  
- **Fairness** – Every process gets a fair share (avoid starvation).  

---

#### Scheduling Algorithms

##### 1. First Come First Serve (FCFS)
- **Type:** Non-preemptive  
- **Pros:** Simple, fair  
- **Cons:** Convoy effect (long jobs delay short ones)  
- **Best for:** Batch systems  

---

##### 2. Shortest Job Next (SJN) / Shortest Job First (SJF)
- **Type:** Preemptive (SRTF) or Non-preemptive  
- **Pros:** Minimum average waiting time  
- **Cons:** Burst time must be known; starvation for long jobs  
- **Best for:** Predictable jobs  

---

##### 3. Priority Scheduling
- **Type:** Preemptive or Non-preemptive  
- **Pros:** Important jobs handled first  
- **Cons:** Starvation possible (fix: aging)  
- **Best for:** Real-time systems  

---

##### 4. Round Robin (RR)
- **Type:** Preemptive  
- **Pros:** Fair, good response time  
- **Cons:** Quantum size critical – too small = overhead, too big = FCFS  
- **Best for:** Time-sharing systems  

---

##### 5. Multilevel Queue (MLQ)
- **Type:** Multiple queues with different scheduling  
- **Pros:** Differentiates process types  
- **Cons:** Rigid; starvation possible  
- **Best for:** Mixed workload systems  

---

##### 6. Multilevel Feedback Queue (MLFQ)
- **Type:** Processes can move between queues  
- **Pros:** Flexible, avoids starvation  
- **Cons:** Complex to implement  
- **Best for:** General-purpose OS (Linux, Windows)  

---

##### 7. Highest Response Ratio Next (HRRN)
- **Type:** Non-preemptive  
- **Formula:**  
  `Response Ratio = (Waiting Time + Burst Time) / Burst Time`  
- **Pros:** Balances short and long jobs, avoids starvation  
- **Cons:** More complex than SJF  
- **Best for:** Interactive systems  

---

#### Comparison Table

| Algorithm | Type | Pros | Cons | Best Use-Case |
|-----------|------|------|------|---------------|
| **FCFS** | Non-preemptive | Simple, fair | Convoy effect | Batch systems |
| **SJF/SRTF** | Preemptive / Non-preemptive | Optimal waiting time | Starvation, need burst time | Predictable jobs |
| **Priority** | Preemptive / Non-preemptive | Critical jobs first | Starvation (without aging) | Real-time tasks |
| **Round Robin** | Preemptive | Fair, responsive | Depends on time quantum | Time-sharing systems |
| **MLQ** | Mixed | Separates process types | Rigid, starvation | Mixed workloads |
| **MLFQ** | Mixed | Flexible, fair | Complex | General OS |
| **HRRN** | Non-preemptive | Balanced, avoids starvation | Complex | Interactive systems |

---
# Deadlocks in Operating Systems

#### Definition
A **deadlock** occurs when **two or more processes wait indefinitely** for resources held by each other, preventing any of them from proceeding.  
It is a state of permanent **blocking** in a system.

---

#### Conditions for Deadlock (Coffman’s Conditions)
1. **Mutual Exclusion** – At least one resource is non-shareable.  
2. **Hold and Wait** – A process holds at least one resource and waits for others.  
3. **No Preemption** – Resources cannot be forcibly taken from a process.  
4. **Circular Wait** – A set of processes exists such that each process is waiting for a resource held by the next process in the chain.

---

#### Handling Deadlocks

#### 1. Prevention
- Ensure at least **one of the four conditions** never occurs.  
- Example: Force processes to request all resources at once (eliminates Hold & Wait).

#### 2. Avoidance
- Dynamically analyze requests and ensure the system remains in a **safe state**.  
- Example: **Banker’s Algorithm**.

#### 3. Detection
- Allow deadlocks to occur, then **detect them** using algorithms like **Wait-for Graphs**.

#### 4. Recovery
- Once detected, recover by:  
  - **Killing processes**  
  - **Preempting resources**  
  - **Rolling back processes**  

---

#### Summary
- Deadlocks = **processes stuck forever waiting for each other**.  
- Requires **Mutual Exclusion, Hold & Wait, No Preemption, Circular Wait**.  
- Can be **Prevented, Avoided, Detected, or Recovered**.


---

# Paging in Operating Systems

#### What is Paging
Paging is a **memory management scheme** that eliminates external fragmentation by dividing logical memory into **pages** and physical memory into **frames** (both of fixed size).  
A **page table** keeps track of the mapping between pages and frames.  

When a page needed by the CPU is **not in RAM**, a **page fault** occurs, and the OS loads it from disk.  

---

#### Effective Paging Techniques

1. **FIFO (First In, First Out)**
   - Replaces the page that entered memory earliest (oldest).  
   - Easy to implement but can cause *Belady’s Anomaly* (more frames can lead to more faults).  

2. **LRU (Least Recently Used)**
   - Replaces the page that hasn’t been used for the longest time.  
   - Works well since it uses the principle of *locality of reference*.  
   - More complex than FIFO but generally more efficient.  

3. **Optimal (OPT) Page Replacement**
   - Replaces the page that will not be used for the longest time in the future.  
   - Theoretically the best, but requires future knowledge (used for comparison, not practical in real-time systems).  

---

#### Counting Page Faults

Page faults are counted by **tracking the number of times a required page is not in memory** and must be fetched from disk.  

- **Step 1:** Take the reference string (sequence of memory accesses).  
- **Step 2:** Simulate frame allocation and page replacement algorithm.  
- **Step 3:** Each time the required page is missing → **Page Fault +1**.  
- **Step 4:** At the end, total faults = efficiency measure.  

**Example:**  
Reference string: `1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5`  
Frames = 3  

- **FIFO** → 9 page faults  
- **LRU** → 8 page faults  
- **OPT** → 7 page faults  

---

#### Summary
- Paging divides memory into pages/frames for efficient allocation.  
- Effective paging techniques = FIFO, LRU, Optimal.  
- Page faults are counted whenever a required page is missing from memory and must be loaded from disk.

# Preemption in Operating Systems
**Definition:** Preemption is the ability of the operating system (OS) to interrupt a currently running process in order to allocate the CPU to another process.
It is essential in preemptive scheduling to ensure fairness and responsiveness.

---
# Synchronization in Operating Systems

#### Definition
**Synchronization** is the coordination of processes to ensure **orderly execution** when accessing **shared resources** like memory, files, or devices.  
It prevents **race conditions**, **data inconsistency**, and ensures **correctness** in concurrent execution.

---

#### Key Points
- Multiple processes may need to access **shared resources** simultaneously.  
- Without synchronization, results may be unpredictable (race condition).  
- Synchronization enforces **mutual exclusion** and proper **sequencing**.

---

#### Mechanisms for Synchronization

##### 1. Mutex (Mutual Exclusion)
- A **lock** that allows only one process to access a resource at a time.  
- Provides **exclusive access** and prevents race conditions.  
- Example: A single-threaded access to a printer.

##### 2. Semaphores
- Integer variable used to **control access** to shared resources.  
- Two types:  
  - **Binary Semaphore** → similar to a mutex (0 or 1).  
  - **Counting Semaphore** → allows multiple processes (count > 1).  
- Provides **wait() / signal()** operations to manage resource access.

##### 3. Monitors
- High-level **synchronization construct** that combines **mutex and condition variables**.  
- Ensures **safe access** to shared data by only allowing one process inside the monitor at a time.  
- Example: Java’s `synchronized` methods or blocks.

---

#### Benefits
- Prevents **data corruption**.  
- Avoids **race conditions**.  
- Ensures **consistent results** in concurrent systems.  

---

#### Summary
Synchronization is essential in OS to coordinate **concurrent processes** and protect **shared resources** using mechanisms like **Mutex, Semaphores, and Monitors**.
