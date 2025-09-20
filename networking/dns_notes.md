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

---

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

---

## 👉 In short  
- **DNS translates names ↔ IP addresses.**  
- **A, AAAA** → IP addresses  
- **CNAME** → Aliases  
- **MX** → Email routing  
- **TXT** → Security & verification  
- **NS, SOA** → Authority info  
- **PTR, SRV, CAA** → Special use cases  
