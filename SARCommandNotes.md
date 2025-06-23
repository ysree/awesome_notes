SAR Command Notes
The sar (System Activity Reporter) command, part of the sysstat package in Linux, is used to collect, report, and analyze system performance metrics such as CPU, memory, disk I/O, network, and more. Below are detailed notes on its syntax and available options.
Syntax
sar [options] [interval [count]] [-f filename | -o filename] [-s hh:mm:ss] [-e hh:mm:ss]


interval: Time in seconds between reports.
count: Number of reports to generate (continues until interrupted if omitted).
-f filename: Read data from a file (e.g., /var/log/sa/sadd where dd is the day).
-o filename: Save data to a file in binary format.
-s hh:mm:ss: Start time for data extraction (24-hour format).
-e hh:mm:ss: End time for data extraction.

Key Notes

Data Collection: Relies on sadc (System Activity Data Collector) to gather data, stored in /var/log/sa/sadd or /var/log/sysstat/sadd (e.g., Ubuntu).
Default Behavior: Displays CPU utilization for the current day from /var/log/sa/sadd without options.
Historical Data: Use -f for past data or -1, -2, etc., for previous days (e.g., sar -u -1 for yesterday’s CPU stats).
Real-Time vs. Historical: Primarily for historical analysis but supports real-time with interval and count.
Cron Integration: Collects data every 10 minutes via /etc/cron.d/sysstat and generates daily reports with sa2 at 23:53.
Performance Impact: Lightweight with minimal resource usage.
Compatibility: Some options may not work on older sysstat versions or kernels. Check with sar -V and man sar.

Available Options
General Options

-A: All statistics (-bBdqrRSuvwWy -I SUM -I XALL -n ALL -u ALL -P ALL).
-f filename: Read from a file (e.g., sar -f /var/log/sa/sa10).
-o filename: Save to a file (e.g., sar -o datafile 2 5).
-s hh:mm:ss: Start time (e.g., sar -u -s 16:00:00).
-e hh:mm:ss: End time (e.g., sar -u -e 17:00:00).
-i interval: Interval for reading file data (e.g., sar -f sa10 -i 60).
-t: Timestamps in file’s local time.
-V: Display version (sar -V).
--sadc: Show sadc path.
-j {ID | LABEL | PATH | UUID}: Device identification method.
-P {cpu | ALL}: Per-processor stats (e.g., sar -P 0 2 3 or -P ALL).
-h: Help (sar --help).

CPU-Related Options

-u [ALL]: CPU utilization (%user, %nice, %system, %iowait, %steal, %idle). ALL for all fields.
Example: sar -u 2 5


-m {keyword | ALL}: Power management stats (e.g., sar -m CPU 5 2 for CPU clock frequency in MHz). Keywords: CPU, MEMS, FREAN, FREQ, IN, USED, TEMP, USB.
Example: sar -m TEMP 2 3 for device temperature.



Memory-Related Options

-r [ALL]: Memory stats (kbmemfree, kbmemused, %memused, kbcached). ALL for all fields.
Example: sar -r 1 3


-R: Memory page stats.
--S: All memory usage stats.-e.g., sar -S -r 1 3)
-H: HugePages stats.
-B: Paging stats (pgpgin/s, pgpgout/s, fault/s, majflt/s).
Example: sar -B 5 2



Disk and I/O-Related Options

-b: I/O stats (tps, bread/s, bwrtn/s).
-d [-p]: Block device stats (tps, rd_sec/s, wr_sec/s). -p for readable names.
Example: sar -d -p 1 3


-F: Filesystem stats.

Network-Related Options

-n {keyword | ALL}: Network stats. Keywords:
DEV: Device stats (rxpck/s, txpck/s).
EDEV: Device error stats.
NFS, NFSD: NFS client/server.
SOCK: Socket stats.
IP, EIP, ICMP, EICMP, TCP, ETCP, UDP: Protocol stats.
SOCK6, IP6, EIP6, ICMP6, UDP6: IPv6 stats.
Example: sar -n DEV 2 3


ALL: All network activities.

Process and Task-Related Options

-q: Run queue and load averages (runq-sz, ldavg-1, ldavg-5, ldavg-15, blocked).
-w: Task creation and switching (proc/s, cswch/s).
-W: Swapping stats (pswpin/s, pswpout/s).

Interrupt-Related Options

-I {int | SUM | ALL | XALL}: Interrupt stats.
int: Specific interrupt (e.g., sar -I 14).
SUM: Total interrupts.
ALL: First 16 interrupts.
XALL: All interrupts.
Example: sar -I SUM 2 3



Other Options

-v: Inode, file, kernel table stats.
-y: TTY device stats.
-C: Display comments in data file.

Examples

CPU Usage (Real-Time):sar -u 2 5


Memory Usage (Historical):sar -r -f /var/log/sa/sa10


Disk I/O (Pretty-Printed):sar -d -p 1 3


Network Stats (Time Frame):sar -n DEV -s 16:00:00 -e 17:00:00 -f /var/log/sa/sa15


All Stats:sar -A 2 3


Save Data:sar -o output.bin 1 10



Configuration Notes

Enable Collection:systemctl start sysstat
systemctl enable sysstat


Cron Setup: Edit /etc/cron.d/sysstat for collection frequency.
Log Location: /var/log/sa/ or /var/log/sysstat/ (e.g., sa23).
Retention: Configure in /etc/sysstat/sysstat (default: 28 days).

Limitations

Reports only local activities (no remote I/O wait).
Requires sysstat (sudo apt-get install sysstat or yum install sysstat).
Some stats depend on kernel modules.
Older sysstat versions may lack certain flags.

Additional Tools

sadf: Convert data to CSV, XML, etc.
ksar/isag: Graphical visualization.
man sar: Full documentation.

For more details, see man sar or http://sebastien.godard.pagesperso-orange.fr/.