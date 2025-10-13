
# Table of content
- [GROK](#grok)
- [LoadBalancer testing](#loadbalancer-testing)

Inducing **latency** (artificial delay) on a **network interface** is a common technique for testing application behavior under suboptimal network conditions — such as slow links, WAN simulation, or packet delay scenarios.

Below are the **commands and explanations** for inducing latency on **Linux**, **Windows**, and **macOS** systems.

---

## 🐧 **1. On Linux (Using `tc` command from iproute2)**

### **Command:**

```bash
sudo tc qdisc add dev eth0 root netem delay 100ms
```

### **Description:**

* `tc`: Traffic Control utility (part of `iproute2` package)
* `qdisc`: Queueing discipline
* `add dev eth0 root`: Apply rule to the root of the network interface `eth0`
* `netem`: Network emulator module for simulating delay, loss, duplication, etc.
* `delay 100ms`: Adds a **100 milliseconds latency** to all packets going out on `eth0`

---

### **To add jitter (variable delay):**

```bash
sudo tc qdisc add dev eth0 root netem delay 100ms 20ms
```

**Explanation:**

* Adds 100ms delay on average
* ±20ms random variation (jitter)

---

### **To combine delay with packet loss:**

```bash
sudo tc qdisc add dev eth0 root netem delay 100ms loss 2%
```

**Explanation:**

* Adds 100ms delay and randomly drops **2%** of packets.

---

### **To verify current latency settings:**

```bash
tc qdisc show dev eth0
```

---

### **To remove (reset) latency:**

```bash
sudo tc qdisc del dev eth0 root
```

---

## 🪟 **2. On Windows (Using PowerShell with NetEm or Clumsy)**

Windows does not have `tc`, but you can use **Clumsy** (GUI tool) or **PowerShell with Network Emulator (NetEm)** from the **Windows Assessment and Deployment Kit (ADK)**.

### **Example (using NetEm cmd):**

```powershell
New-NetQosPolicy -Name "LatencyTest" -AppPathNameMatchCondition "*" -NetworkProfile All
Add-NetEmRule -Name "LatencyRule" -PolicyName "LatencyTest" -Delay 100
```

**Description:**

* Creates a QoS policy (`LatencyTest`)
* Adds an emulator rule introducing **100 ms latency** on all traffic

To remove:

```powershell
Remove-NetQosPolicy -Name "LatencyTest"
```

---

## 🧹 **To Restore Original Settings**

When done testing:

```bash
sudo tc qdisc del dev eth0 root
```

---

Would you like me to show how to **simulate latency for both inbound and outbound traffic** on a specific Linux interface (e.g., `eth0`)? I can include commands for both directions using `ifb` (Intermediate Functional Block).



# GROK

## 1. Using **tc (Traffic Control)** - Most Common Method

### Basic Latency Introduction
```bash
# Add 100ms latency to eth0 interface
sudo tc qdisc add dev eth0 root netem delay 100ms

# Add latency with variation (jitter)
sudo tc qdisc add dev eth0 root netem delay 100ms 10ms

# Add latency with correlation
sudo tc qdisc add dev eth0 root netem delay 100ms 10ms 25%
```

**Description:**
- `tc qdisc add` - Add queuing discipline
- `dev eth0` - Target network interface
- `root` - Apply at root level
- `netem` - Network emulator
- `delay 100ms` - Base latency of 100 milliseconds
- `10ms` - Variation/Jitter (±10ms)
- `25%` - Correlation between packets (25% chance next packet has similar delay)

### 2. **Advanced Latency Configurations**

#### Asymmetric Latency (Different upload/download)
```bash
# Outgoing traffic (egress) - 150ms
sudo tc qdisc add dev eth0 root netem delay 150ms

# Incoming traffic (ingress) - requires ifb interface
sudo modprobe ifb
sudo ip link set dev ifb0 up
sudo tc qdisc add dev eth0 handle ffff: ingress
sudo tc filter add dev eth0 parent ffff: protocol ip u32 match u32 0 0 action mirred egress redirect dev ifb0
sudo tc qdisc add dev ifb0 root netem delay 50ms
```

#### Rate Limiting with Latency
```bash
# Combine latency with bandwidth limitation
sudo tc qdisc add dev eth0 root handle 1: netem delay 100ms
sudo tc qdisc add dev eth0 parent 1: handle 2: tbf rate 1mbit burst 32kbit latency 400ms
```

### 3. **Using iptables with tc**

#### Selective Latency for Specific Traffic
```bash
# Add latency only for HTTP traffic (port 80)
sudo tc qdisc add dev eth0 root handle 1: prio
sudo tc qdisc add dev eth0 parent 1:1 handle 10: netem delay 200ms
sudo tc filter add dev eth0 protocol ip parent 1: prio 1 u32 match ip dport 80 0xffff flowid 1:1
```

#### Latency for Specific IP Range
```bash
# Add latency for traffic to 192.168.1.0/24 network
sudo tc qdisc add dev eth0 root handle 1: prio
sudo tc qdisc add dev eth0 parent 1:1 handle 10: netem delay 150ms
sudo tc filter add dev eth0 protocol ip parent 1: prio 1 u32 match ip dst 192.168.1.0/24 flowid 1:1
```

## 4. **Using Wondershaper** (Simplified Tool)

### Install and Use Wondershaper
```bash
# Install wondershaper (Ubuntu/Debian)
sudo apt install wondershaper

# Add latency and limit bandwidth
sudo wondershaper eth0 1024 1024 delay 100

# Clear settings
sudo wondershaper clear eth0
```

## 5. **Using iproute2 with netem**

### Complex Delay Distributions
```bash
# Normal distribution delay
sudo tc qdisc add dev eth0 root netem delay 100ms 20ms distribution normal

# Pareto distribution (for more realistic network modeling)
sudo tc qdisc add dev eth0 root netem delay 100ms 30ms distribution pareto
```

## 6. **Temporary Network Interface Down/Up** (Crude Method)

```bash
# Briefly disrupt connectivity (not true latency but similar effect)
sudo ifconfig eth0 down && sleep 0.5 && sudo ifconfig eth0 up
```

## Monitoring and Verification Commands

### Check Current tc Rules
```bash
# Show all traffic control rules
sudo tc qdisc show dev eth0

# Show detailed statistics
sudo tc -s qdisc show dev eth0

# Show filters
sudo tc filter show dev eth0
```

### Test Latency
```bash
# Test latency to a target
ping -c 5 google.com

# More detailed network testing
mtr google.com

# Test with specific packet size
ping -s 1472 -c 5 google.com
```

## Removal and Cleanup Commands

### Remove All tc Rules
```bash
# Remove all queuing disciplines
sudo tc qdisc del dev eth0 root

# If above fails, try deleting specific handles
sudo tc qdisc del dev eth0 handle 1: root
```

### Clear Specific Rules
```bash
# List current rules first
sudo tc qdisc show dev eth0

# Then delete specific qdisc
sudo tc qdisc del dev eth0 parent 1:1 handle 10:
```

## Practical Use Cases and Examples

### 1. **Testing Application Performance**
```bash
# Simulate WAN conditions (50ms latency)
sudo tc qdisc add dev eth0 root netem delay 50ms 10ms 25%

# Test your application, then remove
sudo tc qdisc del dev eth0 root
```

### 2. **Load Balancer Testing**
```bash
# Different latency for different servers
sudo tc qdisc add dev eth0 root handle 1: prio
sudo tc qdisc add dev eth0 parent 1:1 handle 10: netem delay 50ms
sudo tc qdisc add dev eth0 parent 1:2 handle 20: netem delay 200ms

# Route specific IPs to different latency classes
sudo tc filter add dev eth0 parent 1: protocol ip prio 1 u32 match ip dst 10.1.1.10 flowid 1:1
sudo tc filter add dev eth0 parent 1: protocol ip prio 2 u32 match ip dst 10.1.1.11 flowid 1:2
```

### 3. **Database Replication Testing**
```bash
# Simulate cross-region latency for database traffic
sudo tc qdisc add dev eth0 root handle 1: prio
sudo tc qdisc add dev eth0 parent 1:1 handle 10: netem delay 200ms
sudo tc filter add dev eth0 parent 1: protocol ip prio 1 u32 match ip dport 5432 0xffff flowid 1:1
```

## Advanced Scenarios

### 4. **Combining Multiple Network Impairments**
```bash
# Latency + Packet loss + Duplication
sudo tc qdisc add dev eth0 root netem delay 100ms loss 2% duplicate 1%

# Latency + Reordering
sudo tc qdisc add dev eth0 root netem delay 100ms reorder 25% 50%
```

### 5. **Time-based Latency Changes**
```bash
#!/bin/bash
# Script to vary latency over time
for latency in 50 100 200 500 1000; do
    echo "Setting latency to ${latency}ms"
    sudo tc qdisc replace dev eth0 root netem delay ${latency}ms
    sleep 30
done
sudo tc qdisc del dev eth0 root
```

## Important Notes and Best Practices

### Safety Precautions
```bash
# Always work on a test interface first
# Use virtual machines or test environments
# Have removal commands ready
# Test on non-production systems
```

### Persistent Rules (if needed)
```bash
# Save rules to reload on boot (systemd)
sudo systemctl enable systemd-networkd
sudo tc qdisc add dev eth0 root netem delay 100ms
sudo tc -s qdisc show dev eth0 > /etc/systemd/network/tc-rules.conf
```

### Verification Script
```bash
#!/bin/bash
# Verify latency is applied
interface="eth0"
expected_delay="100ms"

current_delay=$(sudo tc qdisc show dev $interface | grep -o "delay [0-9]*[a-z]*" | cut -d' ' -f2)

if [ "$current_delay" = "$expected_delay" ]; then
    echo "✓ Latency of $expected_delay successfully applied to $interface"
else
    echo "✗ Failed to apply latency. Current: $current_delay"
fi
```

These commands provide comprehensive ways to induce and manage network latency for testing, development, and troubleshooting purposes. Always remember to clean up after testing to restore normal network operation.


# LoadBalancer testing


This set of commands configures **network latency simulation** on a Linux machine using `tc` (Traffic Control), typically used for **load balancer testing**. It's simulating different network delays for different backend servers based on their IP addresses.

Let me break it down:

---

## 🔧 Objective:

You're testing a **load balancer's behavior when backend servers have different response latencies**. The idea is to **simulate network delay** to certain IP addresses using `tc`, so you can see how the load balancer reacts—whether it favors faster servers, distributes traffic evenly, etc.

---

## 📘 Concepts:

* **`tc` (Traffic Control):** Linux command to configure traffic shaping, delay, loss, etc.
* **`qdisc` (Queueing Discipline):** Controls how packets are enqueued/dequeued.
* **`netem`:** A qdisc that allows you to simulate delay, loss, duplication, etc.
* **`filter`:** Routes specific traffic to certain rules (based on IP, port, etc.).

---

## 🔍 Step-by-step Explanation:

### 1. **Create a root qdisc with priority bands:**

```bash
sudo tc qdisc add dev eth0 root handle 1: prio
```

* Adds a **priority queue** (`prio`) to the interface `eth0`.
* The priority queue has 3 bands by default: 1:1 (high), 1:2 (medium), 1:3 (low).
* This is the base structure that will route packets by priority.

---

### 2. **Simulate different delays on different bands:**

#### Add 50ms delay on band 1:1 (low latency)

```bash
sudo tc qdisc add dev eth0 parent 1:1 handle 10: netem delay 50ms
```

#### Add 200ms delay on band 1:2 (high latency)

```bash
sudo tc qdisc add dev eth0 parent 1:2 handle 20: netem delay 200ms
```

* You’re adding a delay of 50ms to traffic routed to band 1:1.
* A delay of 200ms to traffic on band 1:2.
* Each band simulates a different server response time.

---

### 3. **Route traffic based on destination IP:**

#### Send traffic to 10.1.1.10 through the fast path (50ms)

```bash
sudo tc filter add dev eth0 parent 1: protocol ip prio 1 u32 match ip dst 10.1.1.10 flowid 1:1
```

#### Send traffic to 10.1.1.11 through the slow path (200ms)

```bash
sudo tc filter add dev eth0 parent 1: protocol ip prio 2 u32 match ip dst 10.1.1.11 flowid 1:2
```

* These `filters` match outgoing traffic based on **destination IP**.
* Traffic going to `10.1.1.10` will be delayed **50ms**.
* Traffic going to `10.1.1.11` will be delayed **200ms**.
* This simulates different latency for different backend servers.

---

## 🧪 Use Case:

Say you have a load balancer with 2 backend servers:

* Server A: `10.1.1.10` → fast (50ms latency)
* Server B: `10.1.1.11` → slow (200ms latency)

You want to test whether your load balancer:

* Detects the latency difference
* Adjusts traffic accordingly
* Handles slow backends gracefully

---

## 🧼 Clean up (Reset rules):

To remove the qdisc setup:

```bash
sudo tc qdisc del dev eth0 root
```

---

## ✅ Summary:

| Destination IP | Queue Band | Simulated Delay |
| -------------- | ---------- | --------------- |
| 10.1.1.10      | 1:1        | 50ms            |
| 10.1.1.11      | 1:2        | 200ms           |

This is a **controlled environment** to observe how your load balancer or application behaves when facing real-world network latency variations.

Let me know if you want to simulate **packet loss**, **jitter**, or use **port-based rules** too.
