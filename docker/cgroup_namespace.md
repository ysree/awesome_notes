# Docker: Namespaces and cgroups

## 1. Namespaces in Docker

Namespaces provide **isolation** for processes, giving each container its own “view” of the system.  
They make containers appear as separate machines while sharing the host kernel.

### Key Namespaces:

| Namespace | Purpose |
|-----------|---------|
| **PID**   | Isolates process IDs; containers can have the same PIDs as host without conflict. |
| **NET**   | Isolates network interfaces, IPs, and routing tables. |
| **MNT**   | Isolates filesystem mounts; containers see their own filesystem. |
| **UTS**   | Isolates hostname and domain name. |
| **IPC**   | Isolates inter-process communication (message queues, semaphores). |
| **USER**  | Isolates user and group IDs. |
| **CGROUP**| Connects to cgroups for resource limits. |

**Example:**  
Two containers can each run `/bin/bash` with PID `1` because their PID namespaces are isolated.

---

## 2. cgroups in Docker

cgroups (Control Groups) provide **resource management**.  
They limit, prioritize, and monitor CPU, memory, I/O, and network usage for containers.

### Key Features:

| Resource | Example Limit |
|----------|---------------|
| CPU      | Limit to 50% usage |
| Memory   | Limit to 512 MB RAM |
| Block I/O| Limit disk read/write speeds |
| Network  | Limit bandwidth per container |

### Example:

```bash
docker run -it --memory="512m" --cpus="1.5" ubuntu
--memory="512m" → limits RAM to 512 MB

--cpus="1.5" → limits CPU to 1.5 cores
```