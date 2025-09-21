## Stress-ng Guide for CPU, Memory, Disk, and Network

`stress-ng` is a versatile Linux tool used for **stress testing CPU, memory, disk, and network subsystems**. It can simulate heavy workloads, resource exhaustion, and help validate **resiliency, fault tolerance, and performance under stress**. This guide provides commands and explanations for CPU, memory, disk, and network stress tests.

---

## 1. Installing stress-ng

```bash
# Ubuntu / Debian
sudo apt-get update
sudo apt-get install stress-ng

# RHEL / CentOS / Fedora
sudo yum install epel-release
sudo yum install stress-ng
```

Check installation:

```bash
stress-ng --version
```

---

## 2. CPU Stress Testing

CPU stress testing helps identify **performance bottlenecks, overheating, thermal throttling, and multi-core behavior**.

### Common CPU Options

* `--cpu N` → Number of CPU workers to spawn.
* `--cpu-load X` → Load per CPU worker (0–100%).
* `--timeout T` → Duration of the stress test.
* `--metrics-brief` → Summarizes CPU utilization metrics.

### Example

```bash
# Stress all 4 CPU cores for 60 seconds at full load
stress-ng --cpu 4 --timeout 60s --metrics-brief
```

**Explanation:**

* Spawns 4 CPU workers performing busy loops.
* Runs for 60 seconds.
* Prints metrics like CPU cycles, context switches, and errors.

Optional CPU tests:

```bash
# Prime number calculation on all cores
stress-ng --cpu 4 --cpu-method matrixprod --timeout 60s
```

---

## 3. Memory Stress Testing

Memory stress tests check **allocation failures, memory leaks, paging behavior, and swapping performance**.

### Common Memory Options

* `--vm N` → Number of virtual memory stressors (workers).
* `--vm-bytes SIZE` → Amount of memory per worker (e.g., 1G, 512M).
* `--vm-method <method>` → Stress method (e.g., `all`, `malloc`, `fill`, `swap`).

### Example

```bash
# Allocate 1GB per worker with 2 workers for 60 seconds
stress-ng --vm 2 --vm-bytes 1G --timeout 60s --metrics-brief
```

**Explanation:**

* Spawns 2 memory workers.
* Each worker allocates 1 GB and performs memory stress operations.
* Detects memory fragmentation, leaks, and swap behavior.

Optional memory tests:

```bash
# Use multiple memory stress methods
stress-ng --vm 2 --vm-bytes 512M --vm-method all --timeout 60s
```

---

## 4. Disk Stress Testing

Disk I/O stress helps validate **file system performance, latency, and resiliency under heavy read/write operations**.

### Common Disk Options

* `--hdd N` → Number of workers performing disk I/O.
* `--hdd-bytes SIZE` → Amount of data per worker (e.g., 1G, 512M).
* `--hdd-method write` → Operation type: `write`, `read`, `seek`, `sync`.
* `--tmpfs` → Use temporary filesystem (RAM disk).

### Example

```bash
# Stress disk by writing 1GB with 2 workers
stress-ng --hdd 2 --hdd-bytes 1G --timeout 60s --metrics-brief
```

**Explanation:**

* Spawns 2 workers writing 1 GB each.
* Tests file system write speed, I/O latency, and caching.
* Can also stress read operations or random seeks.

---

## 5. Network Stress Testing

`stress-ng` has **limited direct network stress testing**, but combined with CPU, memory, and disk stress, it can simulate network-heavy scenarios. For advanced network stress, tools like `iperf3` or `netperf` can be used.

### Simulated Network Load (CPU/Memory bound)

```bash
# CPU-bound network-like load
stress-ng --cpu 2 --io 2 --timeout 60s --metrics-brief
```

**Explanation:**

* `--io` workers simulate I/O tasks that could include network operations.
* Combined CPU + I/O stress indirectly stresses the network stack.

---

## 6. Combining Multiple Subsystems

```bash
# Stress CPU, Memory, and Disk together
stress-ng --cpu 4 --vm 2 --vm-bytes 512M --hdd 2 --hdd-bytes 1G --timeout 120s --metrics-brief
```

**Explanation:**

* 4 CPU workers fully utilized.
* 2 memory workers allocating 512 MB each.
* 2 disk workers writing 1 GB each.
* Runs for 2 minutes.
* Simulates a **full-system load** similar to production environments.

---

## 7. Useful Flags

* `--verbose` → Show detailed progress.
* `--metrics-brief` → Summarized metrics at the end.
* `--rand` → Randomizes operations to simulate unpredictable workloads.
* `--times N` → Repeat the test N times.

---

## Tips for vCenter / VMware Testing

1. Always **monitor metrics** via Prometheus, Grafana, or `esxtop` while stress testing.
2. Start with **low load and short duration**, then scale up to avoid accidental production impact.
3. Use **stress-ng** on VM guests for vCenter workloads or specific service nodes.
4. Combine **stress-ng** with scripts (`bash/python`) to automate **resiliency tests**.

```

This is now a fully **Markdown (.md) ready file** with proper formatting.  

Do you want me to also **add example commands specifically tailored for vCenter environments** for CPU, memory, disk, and network stress?

```
