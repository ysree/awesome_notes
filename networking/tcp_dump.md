I'll provide you with comprehensive information about tcpdump commands and their output.

## Basic tcpdump Commands

### 1. **Capture on Specific Interface**
```bash
# Capture on eth0 interface
tcpdump -i eth0

# Capture on any available interface
tcpdump -i any
```

### 2. **Limit Number of Packets**
```bash
# Capture only 10 packets
tcpdump -c 10

# Capture 100 packets on eth0
tcpdump -i eth0 -c 100
```

### 3. **Save to File and Read**
```bash
# Save capture to file
tcpdump -i eth0 -w capture.pcap

# Read from saved file
tcpdump -r capture.pcap
```

### 4. **Filter by Host/IP**
```bash
# Capture traffic to/from specific IP
tcpdump host 192.168.1.100

# Capture traffic from source IP
tcpdump src 192.168.1.100

# Capture traffic to destination IP
tcpdump dst 192.168.1.100
```

### 5. **Filter by Port**
```bash
# Capture HTTP traffic
tcpdump port 80

# Capture traffic to destination port
tcpdump dst port 443

# Capture traffic from source port
tcpdump src port 22
```

### 6. **Protocol Filters**
```bash
# Capture only TCP traffic
sudo tcpdump -i eth0 tcp

# Capture only UDP traffic
tcpdump -i eth0 udp

# Capture ICMP traffic (ping)
tcpdump -i eth0 icmp
```

## Advanced Filters

### 7. **Complex Filters**
```bash
# Capture HTTP traffic to/from specific host
tcpdump host 192.168.1.100 and port 80

# Capture non-HTTP traffic from host
tcpdump host 192.168.1.100 and not port 80

# Capture traffic in specific network
tcpdump net 192.168.1.0/24

# Combine filters
sudo tcpdump -i eth0 tcp and port 443 and host 10.0.0.5

Captures only TCP traffic to/from 10.0.0.5 on port 443.
```



### 8. **Packet Content Inspection**
```bash
# Show packet contents in ASCII
tcpdump -A

# Show packet contents in HEX and ASCII
tcpdump -XX

# Show less verbose output
tcpdump -q
```

### 9. Capture with a time limit
```bash
# Captures traffic for 30 seconds only.

sudo timeout 30 tcpdump -i eth0
```
## Understanding tcpdump Output

### Sample Output and Explanation

**Basic TCP Connection:**
```
15:30:25.123456 IP 192.168.1.100.54321 > 93.184.216.34.80: Flags [S], seq 1234567890, win 64240, options [mss 1460,sackOK,TS val 100 ecr 0,nop,wscale 7], length 0
```

- **Timestamp**: `15:30:25.123456`
- **Source**: `192.168.1.100.54321` (IP:port)
- **Destination**: `93.184.216.34.80` (IP:port)
- **Flags**: `[S]` = SYN (connection request)
- **Sequence number**: `1234567890`
- **Window size**: `64240`

**TCP Flags in Output:**
- `[S]` - SYN (Synchronize)
- `[.]` - ACK (Acknowledgment)
- `[P]` - PSH (Push)
- `[F]` - FIN (Finish)
- `[R]` - RST (Reset)

### 9. **DNS Query Example**
```bash
tcpdump -i any port 53
```
**Output:**
```
15:31:10.654321 IP 192.168.1.100.43210 > 8.8.8.8.53: 12345+ A? google.com. (28)
15:31:10.678901 IP 8.8.8.8.53 > 192.168.1.100.43210: 12345 1/0/0 A 172.217.16.206 (44)
```

### 10. **HTTP Traffic**
```bash
tcpdump -A -s 0 port 80
```
**Output:**
```
GET / HTTP/1.1
Host: www.example.com
User-Agent: Mozilla/5.0...
```

## Useful Command Combinations

### 11. **Real-time Monitoring**
```bash
# Monitor HTTP traffic with host display
tcpdump -i any -l -A port 80 | grep --line-buffered -E '(GET|POST|Host:)'
```

### 12. **Network Troubleshooting**
```bash
# Capture all traffic on subnet with verbose output
tcpdump -i any -n -v net 192.168.1.0/24

# Capture and display in HEX for binary protocols
tcpdump -i eth0 -XX -c 20 port 25
```

### 13. **Performance Monitoring**
```bash
# Don't convert addresses to names (faster)
tcpdump -n

# Don't print timestamps (faster)
tcpdump -t

# Combine for maximum performance
tcpdump -n -t -q
```

## Practical Examples

### 14. **Monitor SSH Connections**
```bash
tcpdump -i any -n port 22
```

### 15. **Capture VoIP Traffic (SIP/RTP)**
```bash
tcpdump -i any -s 0 -A port 5060 or udp portrange 10000-20000
```

### 16. **Detect Network Scans**
```bash
tcpdump -i any -n "tcp[tcpflags] & (tcp-syn) != 0 and tcp[tcpflags] & (tcp-ack) == 0"
```

## Output Interpretation Tips

- **Sequence numbers** help track data flow
- **Window size** indicates buffer availability
- **TTL** values can help identify operating systems
- **TCP flags** show connection state
- **Packet length** indicates data transferred

## Common Options Summary

| Option | Description |
|--------|-------------|
| `-i` | Specify interface |
| `-c` | Packet count limit |
| `-n` | Don't resolve names |
| `-v` | Verbose output |
| `-vv` | More verbose |
| `-X` | HEX output |
| `-A` | ASCII output |
| `-w` | Write to file |
| `-r` | Read from file |
| `-s` | Snaplength (0 = full packet) |

These commands and examples should give you a solid foundation for using tcpdump effectively for network analysis and troubleshooting.