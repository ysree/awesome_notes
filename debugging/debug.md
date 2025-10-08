
# Table of Contents
- [How to debug slow file transfer between server and client?](#how-to-debug-slow-file-transfer-between-server-and-client)
  
---
## How to debug slow file transfer between server and client?

### Suppose you are moving a file from one server to client and file transfer speed is very slow. How will you troubleshoot for the same?

#### 1. Check the File Transfer Method
- Identify how the file is being moved: scp, rsync, sftp, NFS mount, HTTP, FTP, etc.
- **Some tools/protocols add overhead** (e.g., encryption in scp/sftp). For testing, try a lighter protocol (e.g., rsync --inplace --whole-file or plain nc).
#### 2. Check Network Performance
- Use ping to check latency: `ping <server_ip>`
- Use `traceroute` or `mtr` to detect hops causing delay.
- Measure raw network bandwidth with tools like `iperf3`:
    - `iperf3 -s   # On server`
    - `iperf3 -c <server_ip>   # On client`
- Look for packet loss or very high latency.
#### 3. Check Server and Client Resources
- CPU and memory usage (e.g., `top`, `htop`, `vmstat`).
- Disk I/O bottlenecks: `iostat -x 1`
- Network interface utilization: `ifstat` `sar -n DEV 1`
#### 4. Ensure NIC isn’t stuck at 100 Mbps instead of 1 Gbps/10 Gbps: 
-  `ethtool eth0`
#### 5. Check File Size and Disk Throughput
- If file is very large, the disk’s read/write speed can be a bottleneck.
- Test raw disk performance with `dd` or `fio`:
    - `dd if=/dev/zero of=testfile bs=1G count=1 oflag=direct`
#### 6. Check Protocol-Level Bottlenecks
- For SSH-based transfers, encryption can slow things down:
    - Try weaker cipher for testing:  `scp -c aes128-ctr file user@host:/path/`
- For NFS, check mount options (`rsize`, `wsize`, `tcp/udp`).
- For HTTP/FTP, confirm server process limits (connection limits, throttling).
#### 7. Tune protocol/TCP buffers and settings