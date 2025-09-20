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

#### 6. Routing and Switching
- **Switching:** Forwarding packets within the same network using MAC addresses
- **Routing:** Forwarding packets between different networks using IP addresses
- **Protocols:**
  - Static routing
  - Dynamic routing: OSPF, RIP, BGP
- **Devices:**
  - Switches operate at Layer 2
  - Routers operate at Layer 3

---

#### 7. Protocols
- **TCP (Transmission Control Protocol):** Reliable, connection-oriented
- **UDP (User Datagram Protocol):** Fast, connectionless
- **HTTP/HTTPS:** Web communication
- **FTP/SFTP:** File transfer
- **SMTP/IMAP/POP3:** Email
- **DNS:** Domain resolution
- **ICMP:** Network diagnostics (ping, traceroute)

---

#### 8. Wireless Networks
- **Wi-Fi:** IEEE 802.11 standards
- **Bluetooth:** Short-range communication
- **Cellular Networks:** 4G, 5G for mobile data
- **Access Points:** Connect wireless clients to the network

---

#### 9. Network Security
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

#### 10. Network Troubleshooting Tools
- **ping:** Check reachability
- **traceroute:** Trace packet path
- **netstat / ss:** Show connections
- **tcpdump / Wireshark:** Packet capture and analysis
- **nslookup / dig:** DNS queries

---

#### 11. Summary
- Networks connect devices to share **data and resources**.
- Key components: **IP addressing, routing, switching, protocols, and security**.
- Knowledge of OSI/TCP-IP models is essential for troubleshooting and designing networks.
