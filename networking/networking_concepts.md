# Table of contents
- [Computer Networks Concepts](#computer-networks-concepts)
- [1. Networking Basics](#1-networking-basics)
- [2. Types of Networks](#2-types-of-networks)
- [3. Network Topologies](#3-network-topologies)
- [4. OSI and TCP/IP Models](#4-osi-and-tcpip-models)
- [5. IP Addressing and Subnetting](#5-ip-addressing-and-subnetting)
- [6. CIDR](#6-cidr)
- [7. Routing and Switching](#7-routing-and-switching)
- [8. Protocols](#8-protocols)
- [9. Wireless Networks](#9-wireless-networks)
- [10. Network Security](#10-network-security)
- [11. Network Troubleshooting Tools](#11-network-troubleshooting-tools)

# Computer Networks Concepts


Computer networking enables devices to communicate and share resources. Below are key concepts and components.

---

#### 1. Networking Basics
- **Definition:** Interconnection of two or more devices to share data and resources.
- **Key Terms:**
  - **Node:** Any device connected to a network (PC, server, router)
  - **Link:** Physical or logical connection between nodes
  - **Packet:** Small unit of data transmitted over the network
  - **Bandwidth:** Maximum data rate of a network connection
  - **Latency:** Delay in data transmission

---

#### 2. Types of Networks
- **LAN (Local Area Network):** Small geographic area (office, home)
- **WAN (Wide Area Network):** Large geographic area (Internet)
- **MAN (Metropolitan Area Network):** City-level networks
- **PAN (Personal Area Network):** Short-range network (Bluetooth)
- **CAN (Campus Area Network):** Covers university or corporate campuses

---

#### 3. Network Topologies
- **Bus:** Single backbone; simple but collision-prone
- **Star:** Central switch/hub connects all nodes; easy to manage
- **Ring:** Nodes form a closed loop; data flows in one direction
- **Mesh:** Nodes are interconnected; fault-tolerant but expensive
- **Hybrid:** Combination of topologies

---

#### 4. OSI and TCP/IP Models
##### OSI Model (7 Layers)
1. Physical – Electrical/optical signals
2. Data Link – MAC addresses, frames
3. Network – IP addresses, routing
4. Transport – TCP/UDP, reliability
5. Session – Connection management
6. Presentation – Encryption, formatting
7. Application – End-user services (HTTP, FTP)

##### TCP/IP Model (4 Layers)
- Link – Physical + Data Link
- Internet – Network
- Transport – TCP/UDP
- Application – Session + Presentation + Application

---

#### 5. IP Addressing and Subnetting
- **IP Address:** Unique identifier for a device
  - IPv4: 32-bit (e.g., 192.168.1.1)
  - IPv6: 128-bit (e.g., 2001:0db8::1)
- **Subnetting:** Dividing network into smaller subnets
- **CIDR Notation:** e.g., 192.168.1.0/24
- **Private vs Public IPs:** RFC1918 defines private ranges

---

#### 6. CIDR** 
(Classless Inter-Domain Routing) is a method for allocating IP addresses and routing Internet Protocol packets.  
It replaces the old class-based system (Class A, B, C) with a more flexible approach

- **IP_address:** IPv4 address (e.g., 192.168.1.0)  
- **prefix_length:** Number of bits used for the network portion (e.g., /24)

**Example:**  192.168.1.0/24
This means the first 24 bits are the network portion, leaving 8 bits for hosts.


#### Why CIDR is Important

- Efficient IP address allocation  
- Reduces wastage of IP addresses  
- Simplifies routing tables with **route aggregation**  
- Supports **variable-length subnet masking (VLSM)**  


#### How CIDR Works

- Network portion is defined by the prefix length (/n)  
- Host portion is the remaining bits  
- Number of hosts in a network = 2^(32 - prefix_length) - 2 (for IPv4)  

**Example:**  192.168.1.0/26

- Prefix length = 26 bits → Network bits = 26  
- Host bits = 32 - 26 = 6 bits  
- Number of hosts = 2^6 - 2 = 62 usable IPs  

#### CIDR Notation Examples

| CIDR        | Subnet Mask        | Number of Hosts |
|------------|------------------|----------------|
| /24        | 255.255.255.0     | 254            |
| /25        | 255.255.255.128   | 126            |
| /26        | 255.255.255.192   | 62             |
| /27        | 255.255.255.224   | 30             |
| /28        | 255.255.255.240   | 14             |
| /29        | 255.255.255.248   | 6              |
| /30        | 255.255.255.252   | 2              |

#### Advantages of CIDR

- Efficient use of IP addresses  
- Reduces the size of routing tables  
- Allows flexible subnetting  
- Enables route aggregation (supernetting)

---

#### 7. Routing and Switching
- **Switching:** Forwarding packets within the same network using MAC addresses
- **Routing:** Forwarding packets between different networks using IP addresses
- **Protocols:**
  - Static routing
  - Dynamic routing: OSPF, RIP, BGP
- **Devices:**
  - Switches operate at Layer 2
  - Routers operate at Layer 3

---

#### 8. Protocols
- **TCP (Transmission Control Protocol):** Reliable, connection-oriented
- **UDP (User Datagram Protocol):** Fast, connectionless
- **HTTP/HTTPS:** Web communication
- **FTP/SFTP:** File transfer
- **SMTP/IMAP/POP3:** Email
- **DNS:** Domain resolution
- **ICMP:** Network diagnostics (ping, traceroute)

---

#### 9. Wireless Networks
- **Wi-Fi:** IEEE 802.11 standards
- **Bluetooth:** Short-range communication
- **Cellular Networks:** 4G, 5G for mobile data
- **Access Points:** Connect wireless clients to the network

---

#### 110. Network Security
- **Concepts:**
  - Firewalls: Filter traffic based on rules
  - VPN: Secure remote access
  - Encryption: SSL/TLS, IPsec
  - IDS/IPS: Intrusion detection/prevention systems
- **Best Practices:**
  - Strong passwords
  - Regular software updates
  - Network segmentation

---

#### 11. Network Troubleshooting Tools
- **ping:** Check reachability
- **traceroute:** Trace packet path
- **netstat / ss:** Show connections
- **tcpdump / Wireshark:** Packet capture and analysis
- **nslookup / dig:** DNS queries

---

#### 12. Summary
- Networks connect devices to share **data and resources**.
- Key components: **IP addressing, routing, switching, protocols, and security**.
- Knowledge of OSI/TCP-IP models is essential for troubleshooting and designing networks.
