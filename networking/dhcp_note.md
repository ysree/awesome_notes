# 🧠 1. What is DHCP?

**DHCP (Dynamic Host Configuration Protocol)** is a network management protocol used to **automatically assign IP addresses and other network configuration parameters** to devices (clients) on a network.

It eliminates the need for manual IP configuration on each device.

---

## 🌐 2. Purpose of DHCP

Without DHCP, each device on a network must be configured manually with:

- IP address  
- Subnet mask  
- Default gateway  
- DNS server  

**DHCP automates this process**, ensuring:  
✅ Unique IPs  
✅ Faster configuration  
✅ Easier management  
✅ Reduced human error  

---

## ⚙️ 3. How DHCP Works (The DORA Process)

When a device connects to the network, it goes through 4 main steps:

| Step | Name | Description |
|------|------|-------------|
| D | Discover | The client broadcasts a DHCP Discover message to find available DHCP servers. |
| O | Offer | A DHCP server responds with an IP Offer message (suggested IP address + configuration). |
| R | Request | The client requests that IP address from the server. |
| A | Acknowledge | The DHCP server confirms and assigns the IP address to the client. |

### 🔁 DORA Message Flow

```
Client (Discover)  →  Broadcast: "Who can give me an IP?"
Server (Offer)     →  Unicast/Broadcast: "Here’s an IP you can use."
Client (Request)   →  Broadcast: "I want to use that offered IP."
Server (Acknowledge) → "OK, that IP is now yours."
```

---

## 🧩 4. Key DHCP Concepts

| Term | Meaning |
|------|----------|
| Scope / Pool | Range of IP addresses DHCP can assign. |
| Lease Time | Duration an IP address is valid for a client. |
| Reservation | Permanent IP assignment to a specific device (by MAC address). |
| DHCP Relay Agent | Forwards DHCP messages between clients and servers on different subnets. |
| DHCP Options | Extra configuration parameters (e.g., DNS, gateway, domain name). |

---

## 🏗️ 5. DHCP Architecture

### Components:

**DHCP Server:**  
Provides and manages IP addresses.  
Example: Windows Server DHCP, Cisco Router DHCP, ISC DHCP server (Linux).

**DHCP Client:**  
The device requesting IP configuration (PC, phone, IoT device).

**DHCP Relay Agent:**  
Used when the DHCP server is on a different network/subnet.  
It forwards requests using the **giaddr (gateway IP address)** field.

---

## 📡 6. DHCP Message Types

| Message | Direction | Purpose |
|----------|------------|----------|
| DHCPDISCOVER | Client → Broadcast | Client looking for DHCP server |
| DHCPOFFER | Server → Broadcast/Unicast | Offer of IP configuration |
| DHCPREQUEST | Client → Broadcast | Client requests IP address |
| DHCPACK | Server → Client | Server confirms lease assignment |
| DHCPNAK | Server → Client | Invalid request or address conflict |
| DHCPDECLINE | Client → Server | Client rejects IP (conflict detected) |
| DHCPRELEASE | Client → Server | Client releases IP address |
| DHCPINFORM | Client → Server | Client requests additional info (already has IP) |

---

## 🔒 7. DHCP Lease Process

1. **Lease Assignment** — IP given to client for a specific lease time.  
2. **Lease Renewal (T1 Timer)** — Client renews lease halfway through.  
3. **Rebinding (T2 Timer)** — If renewal fails, client broadcasts request to any DHCP server.  
4. **Lease Expiry** — IP returned to pool if client does not renew.

---

## 🧰 8. DHCP Configuration Example (Linux ISC DHCP Server)

**File:** `/etc/dhcp/dhcpd.conf`

```bash
subnet 192.168.1.0 netmask 255.255.255.0 {
    range 192.168.1.100 192.168.1.200;
    option routers 192.168.1.1;
    option subnet-mask 255.255.255.0;
    option domain-name-servers 8.8.8.8, 8.8.4.4;
    default-lease-time 600;
    max-lease-time 7200;
}
```

**Explanation:**  
- IP range: 192.168.1.100 – 192.168.1.200  
- Default gateway: 192.168.1.1  
- DNS: Google DNS  
- Lease time: 10 mins (600s)

---

## 🖥️ 9. Common DHCP Ports

| Protocol | Port | Direction |
|-----------|------|------------|
| UDP | 67 | Server listens on this port |
| UDP | 68 | Client uses this port |

---

## ⚡ 10. Advantages of DHCP

✅ Centralized IP management  
✅ Avoids IP conflicts  
✅ Reduces configuration errors  
✅ Supports mobile clients  
✅ Simplifies large network management  

---

## ⚠️ 11. Disadvantages / Risks

❌ DHCP server failure → devices can’t get IPs  
❌ IP spoofing if DHCP not secured  
❌ Rogue (unauthorized) DHCP servers can cause conflicts  
❌ Not ideal for static devices (like routers, servers, printers)

---

## 🧱 12. DHCP vs Static IP

| Feature | DHCP | Static |
|----------|-------|--------|
| IP Assignment | Automatic | Manual |
| Setup Effort | Easy | Time-consuming |
| IP Changes | Can change | Fixed |
| Use Case | PCs, mobile devices | Servers, network printers |

---

## 🔐 13. Security Best Practices

- Use **DHCP Snooping** on switches to block rogue servers.  
- Reserve IPs for critical devices using MAC-based reservations.  
- Combine with **802.1X authentication** for client verification.

---

## 🧾 14. Example DHCP Workflow (Summary Diagram)

```
Client              DHCP Server
   |                     |
   |--DHCPDISCOVER------>|
   |<------DHCPOFFER-----|
   |--DHCPREQUEST------->|
   |<------DHCPACK-------|
   |   (IP Lease Active) |
```

---

## ✅ 15. Summary Table

| Parameter | DHCP |
|------------|------|
| Full Form | Dynamic Host Configuration Protocol |
| Port | UDP 67 (Server), UDP 68 (Client) |
| Purpose | Automatic IP configuration |
| Core Steps | Discover → Offer → Request → Acknowledge |
| Key Components | Client, Server, Relay |
| Message Types | DISCOVER, OFFER, REQUEST, ACK, NAK, RELEASE, INFORM |
| Alternative | Static IP Assignment |
