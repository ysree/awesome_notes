# Subnetting
- Subnetting is the process of dividing a large IP network into smaller, more manageable networks called subnets.
- Helps with efficient IP address usage.
- Improves network performance and security.
### Why Subnetting?
- Reduce broadcast traffic.
- Organize networks by department, location, or purpose.
- Efficient use of limited IPv4 addresses.

### IP Address Basics
- 8 bits = 1 byte = 1 octet
- Each octet = 8 bits → 255 max value (0-255)
- IPv4: 32 bits, written as 4 octets: 192.168.1.10

#### Two parts in an IP address:
- Network ID → identifies the network
- Host ID → identifies the host in that network
#### Classes of IP Addresses
Here’s a table summarizing the classes of IP addresses, their ranges, default subnet masks, and typical usage:

# IPv4 Address Classes

| Class | Range                     | Reserved Bits | Default Subnet Mask | Address Length | Number of Networks | Number of Hosts per Network | Usage                       |
|-------|---------------------------|---------------|-------------------|----------------|------------------|----------------------------|-----------------------------|
| A     | 1.0.0.0 – 126.255.255.255 | 1 bit (0)     | 255.0.0.0 (/8)    | 32 bits        | 128              | 16,777,214                | Large networks, ISPs        |
| B     | 128.0.0.0 – 191.255.255.255 | 2 bits (10)   | 255.255.0.0 (/16) | 32 bits        | 16,384           | 65,534                     | Medium networks             |
| C     | 192.0.0.0 – 223.255.255.255 | 3 bits (110)  | 255.255.255.0 (/24)| 32 bits        | 2,097,152        | 254                        | Small networks / LAN        |
| D     | 224.0.0.0 – 239.255.255.255 | 4 bits (1110) | N/A               | 32 bits        | N/A              | N/A                        | Multicast                   |
| E     | 240.0.0.0 – 255.255.255.255 | 4 bits (1111) | N/A               | 32 bits        | N/A              | N/A                        | Experimental / Research     |



### Subnet Mask
- Defines which part of the IP address is the network and which part is the host.
- Example:
    - 255.255.255.0 → /24 → first 24 bits are network, last 8 bits are host
    - 255.255.0.0 → /16 → first 16 bits are network, last 16 bits are host
### CIDR Notation
- Classless Inter-Domain Routing (CIDR) notation represents the subnet mask as a suffix
- Example:
    - 192.168.1.0/24
### Subnetting Example
- Given IP: 192.168.1.10
- Subnet Mask: 255.255.255.0
- Network ID: 192.168.1.0
- Host ID: 10
- Total Hosts: 2^8 - 2 = 254 (subtracting network and broadcast addresses)
### Subnetting Steps
1. Determine the number of subnets needed.
2. Calculate the number of bits to borrow from the host portion.
3. Update the subnet mask accordingly.
4. Calculate the new subnet ranges and broadcast addresses.
### Example: Create 4 subnets from 192.168.1.0/24
- Subnet 1: 192.168.1.0/26 (Network ID: 192.168.1.0, Broadcast ID: 192.168.1.63)
- Subnet 2: 192.168.1.64/26 (Network ID: 192.168.1.64, Broadcast ID: 192.168.1.127)
- Subnet 3: 192.168.1.128/26 (Network ID: 192.168.1.128, Broadcast ID: 192.168.1.191)
- Subnet 4: 192.168.1.192/26 (Network ID: 192.168.1.192, Broadcast ID: 192.168.1.255)
### Subnetting Formulas
- Number of Subnets = 2^n (n = number of bits borrowed)
- Number of Hosts per Subnet = 2^h - 2 (h = number of host bits)
### Common Subnet Masks
| CIDR | Subnet Mask       | Total Hosts | Usable Hosts |
|------|-------------------|-------------|---------------|
| /24  | 255.255.255.0     | 256         | 254           |  
| /25  | 255.255.255.128   | 128         | 126           |
| /26  | 255.255.255.192   | 64          | 62            |
| /27  | 255.255.255.224   | 32          | 30            |
| /28  | 255.255.255.240   | 16          | 14            |
| /29  | 255.255.255.248   | 8           | 6             |
| /30  | 255.255.255.252   | 4           | 2             |
### Subnetting Practice Questions
1. Given IP: 192.168.1.10/24, create 4 subnets.
2. Calculate the number of hosts in a /26 subnet.
3. What is the broadcast address for 192.168.1.10/24?       192.168.1.255
4. How many subnets can be created from a /24 network if 3 bits are borrowed? 8 subnets
5. What is the subnet mask for a /27 network? 255.255.255.224

==============

# Understanding 192.168.128.0/22

## 1. Subnet Basics
- **IP:** 192.168.128.0  
- **Subnet Mask:** /22 → 255.255.252.0  
- **Network Bits:** 22  
- **Host Bits:** 32 – 22 = 10 bits for hosts  

✅ **Number of addresses in /22:**  
2¹⁰ = 1024 addresses  

- **Usable hosts:** 1024 – 2 = 1022  

---

## 2. Network Range
- **Subnet increment:** The block size = 256 × (2^(8 – subnet bits in last octet))  
- **Last octet bits used for subnet:** 8 – 2 (since /22 uses 22 bits total → last 2 bits of 3rd octet used for subnet)  
- **Increment:** 4 in 3rd octet  

- **Network address:** 192.168.128.0  
- **Broadcast address:** 192.168.131.255  
- **Usable host range:** 192.168.128.1 → 192.168.131.254  

---

## 3. Why /22?
A /22 combines **4 consecutive /24 networks**:

1. 192.168.128.0/24  
2. 192.168.129.0/24  
3. 192.168.130.0/24  
4. 192.168.131.0/24  

- **Total usable hosts:** 4 × 254 = 1016 (slightly less than 1022 due to network/broadcast).  

---

## 4. Summary Table

| Feature               | Value                        |
|-----------------------|------------------------------|
| Network Address       | 192.168.128.0/22             |
| Subnet Mask           | 255.255.252.0                |
| Total IPs             | 1024                          |
| Usable Hosts          | 1022                          |
| First Usable IP       | 192.168.128.1                |
| Last Usable IP        | 192.168.131.254              |
| Broadcast Address     | 192.168.131.255              |
| /24 Networks Covered  | 192.168.128.0 → 192.168.131.0 |

---

✅ **In short:**  
- /22 subnet combines **4 /24 networks**, giving **1022 usable hosts**.  
- **Network increment** = 4 in the 3rd octet.  

### Practice Questions
# 192.168.128.0/22 Subnet Questions & Answers

1. **Broadcast Address:**  
   192.168.131.255

2. **Usable Hosts:**  
   1022 (Total 1024 addresses minus 2 for network and broadcast)

3. **Subnet Mask for /22:**  
   255.255.252.0

4. **Usable IP Addresses:**  
   192.168.128.1 → 192.168.131.254

5. **Number of /24 subnets in /22:**  
   4 (/22 covers 4 consecutive /24 subnets: 192.168.128.0/24 → 192.168.131.0/24)

6. **First Usable IP Address:**  
   192.168.128.1

7. **Last Usable IP Address:**  
   192.168.131.254

8. **Network Address:**  
   192.168.128.0

9. **Total IP Addresses in /22:**  
   2¹⁰ = 1024 addresses

10. **New subnet mask for /23 from /22:**  
    255.255.254.0 (/23)

11. **Increment value for the 3rd octet in /22:**  
    4

12. **Bits used for host portion in /22:**  
    10 bits (32 – 22)

13. **CIDR Notation for 255.255.252.0:**  
    /22

14. **Total subnets if 2 bits are borrowed from /22:**  
    4 subnets (2² = 4)

15. **Range of IP Addresses in 192.168.128.0/22:**  
    - **Network:** 192.168.128.0  
    - **Usable:** 192.168.128.1 → 192.168.131.254  
    - **Broadcast:** 192.168.131.255
