Sure! Here’s a **comprehensive guide to `iostat` commands**, along with sample output and explanation for each field.

---

# **`iostat` Commands in Linux**

The `iostat` command (part of the **sysstat** package) is used to **monitor CPU and I/O statistics** for devices and partitions. It helps identify **bottlenecks in disk I/O**.

---

## **1️⃣ Basic Command**

```bash
iostat
```

### **Sample Output:**

```
Linux 5.15.0-73-generic (server01)  10/13/2025  _x86_64_  (4 CPU)

avg-cpu:  %user   %nice %system %iowait  %steal  %idle
           5.00    0.00    2.00    1.00    0.00   92.00

Device            tps    kB_read/s    kB_wrtn/s    kB_read    kB_wrtn
sda              10.00       100.00       200.00     10000      20000
sdb               5.00        50.00       100.00      5000      10000
```

### **Explanation:**

**CPU Section (`avg-cpu`):**

| Column    | Meaning                                               |
| --------- | ----------------------------------------------------- |
| `%user`   | CPU time spent on user processes                      |
| `%nice`   | CPU time spent on user processes with nice priority   |
| `%system` | CPU time spent on kernel processes                    |
| `%iowait` | CPU time waiting for I/O completion                   |
| `%steal`  | Time stolen by hypervisor in virtualized environments |
| `%idle`   | CPU time spent idle                                   |

**Device Section:**

| Column      | Meaning                             |
| ----------- | ----------------------------------- |
| `Device`    | Name of the device                  |
| `tps`       | Transfers per second (read + write) |
| `kB_read/s` | KB read per second                  |
| `kB_wrtn/s` | KB written per second               |
| `kB_read`   | Total KB read                       |
| `kB_wrtn`   | Total KB written                    |

---

## **2️⃣ Show Extended Statistics**

```bash
iostat -x
```

### **Sample Output:**

```
Device            r/s    w/s   rkB/s   wkB/s  rrqm/s  wrqm/s  %util
sda               2      8    50      200       0       0   2.00
sdb               1      4    25      100       0       0   1.00
```

### **Explanation:**

| Column   | Meaning                                                      |
| -------- | ------------------------------------------------------------ |
| `r/s`    | Read requests per second                                     |
| `w/s`    | Write requests per second                                    |
| `rkB/s`  | KB read per second                                           |
| `wkB/s`  | KB written per second                                        |
| `rrqm/s` | Merged read requests per second                              |
| `wrqm/s` | Merged write requests per second                             |
| `%util`  | Percentage of time device was active (100% = fully utilized) |

---

## **3️⃣ Show Statistics with Interval and Count**

```bash
iostat 5 3
```

* **5** → interval in seconds between reports
* **3** → number of reports

**Sample Output:**

```
avg-cpu:  %user   %nice %system %iowait  %steal  %idle
           4.50    0.00    1.50    0.50    0.00   93.50

Device            tps    kB_read/s    kB_wrtn/s
sda              12.00      120.00      250.00
sdb               6.00       60.00      120.00

... (updated every 5 seconds for 3 times)
```

**Explanation:**

* Useful for **monitoring I/O over time**, not just cumulative averages.

---

## **4️⃣ Show CPU Statistics Only**

```bash
iostat -c
```

**Sample Output:**

```
avg-cpu:  %user   %nice %system %iowait  %steal  %idle
           5.00    0.00    2.00    1.00    0.00   92.00
```

* Only shows **CPU utilization** without device I/O stats.

---

## **5️⃣ Show Device Statistics Only**

```bash
iostat -d
```

**Sample Output:**

```
Device            tps    kB_read/s    kB_wrtn/s
sda              10.00       100.00       200.00
sdb               5.00        50.00       100.00
```

* Shows only **disk I/O statistics**.

---

## **6️⃣ Display JSON Output (for automation)**

```bash
iostat -o JSON
```

* Useful for **parsing metrics with scripts**.

---

## **7️⃣ Common Usage Scenarios**

1. **Detect I/O Bottlenecks**

   * High `%iowait` or high `%util` indicates disk saturation.

2. **Monitor Disk Performance**

   * `tps` shows activity rate
   * `rkB/s` and `wkB/s` show throughput

3. **CPU vs I/O Wait Analysis**

   * `%iowait` + `%idle` helps understand if CPU is idle because of disk waiting.

4. **Real-time Monitoring**

   ```bash
   iostat -x 5
   ```

   * Updates extended I/O stats every 5 seconds.

---

## **Summary of Useful Commands**

| Command          | Purpose                     |
| ---------------- | --------------------------- |
| `iostat`         | Show CPU & device stats     |
| `iostat -x`      | Extended device stats       |
| `iostat -d`      | Device stats only           |
| `iostat -c`      | CPU stats only              |
| `iostat 5 3`     | Update every 5 sec, 3 times |
| `iostat -o JSON` | Output in JSON for scripts  |

---

If you want, I can also create a **diagram showing CPU vs Disk I/O metrics** and how to interpret `iostat` results to identify bottlenecks—it makes it easier to understand practically.

Do you want me to do that?
