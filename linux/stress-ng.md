Here’s a detailed guide on **`stress-ng` commands** along with explanation of their usage, options, and practical examples.

---

# **`stress-ng` Notes**

`stress-ng` is a **Linux stress testing tool** that can stress **CPU, memory, I/O, filesystem, and other system components** to test stability, performance, or thermal limits. It’s more advanced than the older `stress` command.

---

## **1️⃣ Installing `stress-ng`**

```bash
sudo apt-get install stress-ng    # Debian/Ubuntu
sudo yum install stress-ng        # RHEL/CentOS
sudo dnf install stress-ng        # Fedora
```

---

## **2️⃣ Basic Syntax**

```bash
stress-ng [OPTIONS] [--class <class>] [--task <tasks>] ...
```

**Common Options:**

* `-c N` → Spawn **N CPU stressors**
* `-m N` → Spawn **N memory stressors**
* `-i N` → Spawn **N I/O stressors**
* `--timeout <time>` → Run for a specific duration (e.g., `--timeout 60s`)
* `--verbose` → Print detailed output

---

## **3️⃣ CPU Stress**

### **Command:**

```bash
stress-ng -c 4 --timeout 60s --verbose
```

**Explanation:**

* `-c 4` → Run 4 CPU stressors (one per CPU core ideally)
* `--timeout 60s` → Run for 60 seconds
* `--verbose` → Show detailed progress

**Output Example:**

```
stress-ng: info: [12345] dispatching hogs: 4 cpu
stress-ng: info: [12345] successful run completed in 60.03s
```

* Simulates heavy CPU load for testing scheduler, performance, or thermal limits.

---

## **4️⃣ Memory Stress**

### **Command:**

```bash
stress-ng -m 2 --vm-bytes 512M --timeout 60s --verbose
```

**Explanation:**

* `-m 2` → Spawn 2 memory stressors
* `--vm-bytes 512M` → Each stressor allocates 512 MB of memory
* Useful for testing **memory allocation, swap, and system stability**

---

## **5️⃣ I/O Stress**

### **Command:**

```bash
stress-ng -i 2 --timeout 60s --verbose
```

**Explanation:**

* `-i 2` → Run 2 I/O stressors
* Generates read/write operations to **stress disks and file systems**

---

## **6️⃣ Combined Stress (CPU + Memory + I/O)**

```bash
stress-ng -c 2 -m 1 -i 1 --vm-bytes 256M --timeout 60s --verbose
```

**Explanation:**

* `-c 2` → CPU stressors
* `-m 1` → Memory stressor allocating 256 MB
* `-i 1` → I/O stressor
* Simulates **real-world high load scenarios** for testing system robustness.

---

## **7️⃣ Stressing Specific Subsystems**

| Subsystem | Example Command                                | Explanation                                           |
| --------- | ---------------------------------------------- | ----------------------------------------------------- |
| CPU       | `stress-ng -c 4 --cpu-method matrixprod`       | Uses a **matrix multiplication method** to stress CPU |
| Memory    | `stress-ng -m 1 --vm-bytes 1G --vm-method all` | Allocates 1GB memory using all memory stress methods  |
| Disk      | `stress-ng --hdd 2 --hdd-bytes 1G`             | Writes 1GB files to disk to stress filesystem         |
| Network   | `stress-ng --sock 1`                           | Stress socket usage (network I/O)                     |
| Locks     | `stress-ng --locks 2`                          | Stress kernel locks                                   |

---

## **8️⃣ Timeout and Repeated Runs**

* Run stress for **a fixed time**:

```bash
stress-ng -c 2 -m 1 --timeout 120s
```

* Run stress **repeatedly** with intervals:

```bash
stress-ng -c 2 --timeout 60s --times
```

* Shows **statistics about each stressor** after each run.

---

## **9️⃣ Verbose and Logging**

* Verbose mode gives **real-time output**:

```bash
stress-ng -c 2 --timeout 60s --verbose
```

* To log to file:

```bash
stress-ng -c 2 --timeout 60s --log-file stress.log
```

* Helps in **analysis or debugging**.

---

## **10️⃣ Practical Use Cases**

1. **Performance Testing:** Check how the system behaves under max CPU/memory load.
2. **Thermal Testing:** Measure CPU/GPU temperature under heavy load.
3. **System Stability Testing:** Test **servers, VMs, or containers** for resilience.
4. **Load Testing for Cloud:** Simulate high resource consumption in cloud environments.
5. **Benchmarking:** Compare hardware performance under stress.

---

## **11️⃣ Safety Precautions**

* Avoid running on **production systems** unless intended.
* Use **timeout** to prevent indefinite stress.
* Monitor **CPU, memory, disk, and temperature** while stressing:

  ```bash
  top
  vmstat 5
  iostat 5
  ```

---

## **Summary of Common Commands**

| Command                                        | Purpose                                |
| ---------------------------------------------- | -------------------------------------- |
| `stress-ng -c 4 --timeout 60s`                 | Stress CPU 4 cores for 60s             |
| `stress-ng -m 2 --vm-bytes 512M --timeout 60s` | Stress memory 2 stressors, 512MB each  |
| `stress-ng -i 2 --timeout 60s`                 | Stress I/O 2 stressors                 |
| `stress-ng -c 2 -m 1 -i 1 --vm-bytes 256M`     | Combined CPU+Memory+I/O stress         |
| `stress-ng --hdd 2 --hdd-bytes 1G`             | Stress filesystem/disk with 1GB writes |
| `stress-ng -c 2 --timeout 60s --verbose`       | Show detailed progress                 |

---

If you want, I can also create a **diagram showing which `stress-ng` options affect which system resource**, so it’s easy to **visualize CPU, memory, I/O, and disk stress**.

Do you want me to do that?
