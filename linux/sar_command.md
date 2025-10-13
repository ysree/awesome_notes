Here’s a **comprehensive guide on `sar` (System Activity Reporter)** for Linux, including commands, explanations, and sample output.

---

# **`sar` (System Activity Reporter) Notes**

`sar` is part of the **sysstat package** and is used to **collect, report, and save system activity metrics** over time. It helps in **performance monitoring, capacity planning, and troubleshooting**.

---

## **1️⃣ Installing sysstat (if not already installed)**

```bash
sudo apt-get install sysstat     # Ubuntu/Debian
sudo yum install sysstat         # RHEL/CentOS
sudo dnf install sysstat         # Fedora
```

Enable data collection:

```bash
sudo systemctl enable sysstat
sudo systemctl start sysstat
```

---

## **2️⃣ Basic Syntax**

```bash
sar [options] [interval] [count]
```

* **interval** → seconds between samples
* **count** → number of samples to display

Example:

```bash
sar 5 3
```

* Collects **CPU usage every 5 seconds**, 3 times.

---

## **3️⃣ CPU Usage Monitoring**

### **Command:**

```bash
sar -u 5 3
```

**Sample Output:**

```
12:00:01     CPU     %user     %nice   %system   %iowait   %steal    %idle
12:00:06     all      5.00      0.00      2.00      1.00     0.00     92.00
12:00:11     all      7.00      0.00      3.00      1.50     0.00     88.50
```

**Explanation of columns:**

| Column    | Meaning                                 |
| --------- | --------------------------------------- |
| `%user`   | CPU time spent on user processes        |
| `%nice`   | CPU time on nice processes              |
| `%system` | CPU time spent in kernel                |
| `%iowait` | Time CPU waits for I/O                  |
| `%steal`  | Time stolen by hypervisor (virtualized) |
| `%idle`   | CPU idle time                           |

---

## **4️⃣ Memory Usage**

```bash
sar -r 5 3
```

**Sample Output:**

```
12:00:01 kbmemfree  kbmemused  %memused  kbbuffers  kbcached
12:00:06   100000    300000     75.00     5000       20000
```

| Column      | Meaning                |
| ----------- | ---------------------- |
| `kbmemfree` | Free memory in KB      |
| `kbmemused` | Used memory in KB      |
| `%memused`  | Percentage memory used |
| `kbbuffers` | Memory used by buffers |
| `kbcached`  | Memory used by cache   |

---

## **5️⃣ Swap Usage**

```bash
sar -S 5 3
```

* Shows **swap utilization**:

| Column      | Meaning      |
| ----------- | ------------ |
| `kbswpfree` | Free swap    |
| `kbswpused` | Used swap    |
| `%swpused`  | Swap usage % |
| `kbswpcad`  | Cached swap  |

---

## **6️⃣ Device I/O Statistics**

```bash
sar -d 5 3
```

**Sample Output:**

```
12:00:01   DEV       tps    rd_sec/s   wr_sec/s   avgrq-sz   avgqu-sz  await   %util
12:00:06   sda      10.0      100.0     200.0      30.0       0.1       5.0     2.0
```

| Column     | Meaning                        |
| ---------- | ------------------------------ |
| `DEV`      | Device name                    |
| `tps`      | Transfers per second           |
| `rd_sec/s` | Read KB/s                      |
| `wr_sec/s` | Write KB/s                     |
| `avgrq-sz` | Average request size           |
| `avgqu-sz` | Avg queue length               |
| `await`    | Avg wait time per request (ms) |
| `%util`    | Device utilization %           |

---

## **7️⃣ Network Statistics**

```bash
sar -n DEV 5 3
```

* Shows **network interface usage**:

| Column     | Meaning                        |
| ---------- | ------------------------------ |
| `IFACE`    | Interface name                 |
| `rxpck/s`  | Packets received per second    |
| `txpck/s`  | Packets transmitted per second |
| `rxkB/s`   | KB received per second         |
| `txkB/s`   | KB transmitted per second      |
| `rxcmp/s`  | Compressed packets received    |
| `txcmp/s`  | Compressed packets transmitted |
| `rxmcst/s` | Multicast packets received     |

---

## **8️⃣ Run sar for Historical Data**

* `sar` collects data automatically (if sysstat enabled) and stores in `/var/log/sysstat/`:

```bash
sar -u -f /var/log/sysstat/sa13
```

* `-f` reads historical log file for day 13.

---

## **9️⃣ CPU per Core**

```bash
sar -P ALL 5 3
```

* Shows **CPU utilization for each core** individually.

---

## **10️⃣ Combine Options**

```bash
sar -u -r -d -n DEV 5 3
```

* Monitors **CPU, memory, disk I/O, and network** together.

---

## **11️⃣ Advantages of sar**

* Real-time and historical system metrics
* Helps in **capacity planning**
* Identifies **CPU, memory, I/O, and network bottlenecks**
* Can be automated with **cron** to collect data periodically

---

## **12️⃣ Common Commands Summary**

| Command                        | Purpose            |
| ------------------------------ | ------------------ |
| `sar`                          | CPU + device stats |
| `sar -u`                       | CPU usage          |
| `sar -r`                       | Memory usage       |
| `sar -S`                       | Swap usage         |
| `sar -d`                       | Disk I/O stats     |
| `sar -n DEV`                   | Network stats      |
| `sar -P ALL`                   | CPU per core       |
| `sar -f /var/log/sysstat/saXX` | Historical data    |

---

If you want, I can also **make a quick diagram linking `sar` metrics to system bottlenecks** (CPU, memory, disk, network), which is extremely useful for **interview explanations and troubleshooting**.

Do you want me to create that diagram?
