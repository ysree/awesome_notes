# 🌐 DNS (Domain Name System) Notes  

## 🔹 What is DNS?  
- DNS = "Phonebook of the Internet."  
- Converts **human-readable domain names** (e.g., `www.example.com`) into **IP addresses** (e.g., `192.0.2.1`).  
- Works in a **hierarchical and distributed** manner.  
- Uses **UDP/TCP on port 53**.  

## 🔹 How DNS Works (Simplified Flow)  
1. User enters `www.example.com` in browser.  
2. Browser checks local cache → OS cache → Resolver (ISP DNS).  
3. If not found, resolver queries **Root → TLD (.com) → Authoritative DNS server**.  
4. IP address returned → Browser connects to server.  

## 🌐 2. Purpose of DNS

- Makes the Internet user-friendly: Humans remember names, not IPs.
- Provides distributed database for mapping domain names to IP addresses.
- Supports email routing, network services, and load balancing.

- **Example:**  
www.example.com → 192.168.1.10
---
## ⚙️ 3. How DNS Works

DNS uses a hierarchical structure:

- **Root Level:** `.` (dot) — points to top-level domains.
- **Top-Level Domain (TLD):** `.com`, `.org`, `.net`, `.in`
- **Second-Level Domain (SLD):** `example` in `example.com`
- **Subdomain:** `www` in `www.example.com`

### DNS Resolution Process

1. User types a URL in the browser.
2. Browser checks local DNS cache.
3. Query sent to recursive resolver (usually your ISP).
4. Resolver queries root server → TLD server → Authoritative server.
5. Resolver returns the IP address to the client.

---

## 🧩 4. Types of DNS Servers

| Type                  | Role                                                   |
|-----------------------|--------------------------------------------------------|
| Root DNS Server        | Maintains information about TLD servers.             |
| TLD DNS Server         | Maintains info about domains under TLD (like .com).  |
| Authoritative DNS Server | Stores actual DNS records for domains.             |
| Recursive Resolver     | Handles queries on behalf of the client and caches results. |
| Caching DNS Server     | Temporarily stores resolved queries to speed up resolution. |

## 📑 Common DNS Record Types (with Examples)  

| Record Type | Purpose | Example |
|-------------|---------|---------|
| **A** (Address) | Maps a domain to an IPv4 address. | `example.com → 93.184.216.34` |
| **AAAA** | Maps a domain to an IPv6 address. | `example.com → 2606:2800:220:1:248:1893:25c8:1946` |
| **CNAME** (Canonical Name) | Alias of one domain to another. | `www.example.com → example.com` |
| **MX** (Mail Exchange) | Defines mail servers for email delivery. | `example.com → mail.example.com (priority 10)` |
| **TXT** | Stores text data (SPF, DKIM, verification). | `v=spf1 include:_spf.google.com ~all` |
| **NS** (Name Server) | Specifies authoritative DNS servers. | `example.com → ns1.exampledns.com` |
| **SOA** (Start of Authority) | Holds admin info, zone serial, refresh times. | Primary NS: `ns1.exampledns.com` |
| **PTR** (Pointer) | Reverse lookup: IP → Domain. | `93.184.216.34 → example.com` |
| **SRV** | Defines services and ports. | `_sip._tcp.example.com → sipserver.example.com:5060` |
| **CAA** | Specifies which CAs can issue SSL certificates. | `example.com → 0 issue "letsencrypt.org"` |


## 📡 6. Types of DNS Queries

| Query Type           | Description |
|---------------------|-------------|
| Recursive Query      | Resolver fully answers client query by querying other DNS servers. |
| Iterative Query      | Resolver provides best answer it has; client continues query to other servers. |
| Non-recursive Query  | Resolver answers from its cache without further queries. |

---

## 🧰 7. Common DNS Tools in Linux

| Command     | Purpose |
|------------|---------|
| `nslookup` | Query DNS records and servers. |
| `dig`      | More advanced query and detailed output. |
| `host`     | Simple DNS lookup for domains/IPs. |
| `ping`     | Verify domain resolves to IP. |

**Examples:**  
```bash
nslookup www.google.com

dig example.com
host 8.8.8.8
```

## 🔄 8. Forward vs Reverse DNS

- **Forward DNS:** Converts domain → IP  
- **Reverse DNS (PTR record):** Converts IP → domain  

**Example:**  
```
Forward: example.com → 93.184.216.34
Reverse: 93.184.216.34 → example.com
```


---

## ⚡ 9. Advantages of DNS

- Human-friendly addressing
- Distributed system avoids single point of failure
- Caching reduces traffic and speeds up resolution
- Supports multiple types of records and services

---

## ⚠️ 10. Common DNS Issues

- DNS cache poisoning
- Misconfigured DNS records
- DNS propagation delays
- Slow resolution due to network issues or high TTL

---

## 📝 11. Summary

| Feature           | DNS |
|------------------|-----|
| Full Form         | Domain Name System |
| Purpose           | Translate domain names to IP addresses |
| Key Components    | Root, TLD, Authoritative, Recursive, Caching |
| Key Records       | A, AAAA, CNAME, MX, NS, PTR, SOA, TXT |
| Resolution Types  | Recursive, Iterative, Non-recursive |
| Tools             | nslookup, dig, host, ping |


## 👉 In short  
- **DNS translates names ↔ IP addresses.**  
- **A, AAAA** → IP addresses  
- **CNAME** → Aliases  
- **MX** → Email routing  
- **TXT** → Security & verification  
- **NS, SOA** → Authority info  
- **PTR, SRV, CAA** → Special use cases  
