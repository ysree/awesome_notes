
ifconfig_output = """
lo0: flags=8049<UP,LOOPBACK,RUNNING,MULTICAST> mtu 16384
	options=1203<RXCSUM,TXCSUM,TXSTATUS,SW_TIMESTAMP>
	inet 127.0.0.1 netmask 0xff000000
	inet6 ::1 prefixlen 128
	inet6 fe80::1%lo0 prefixlen 64 scopeid 0x1
	nd6 options=201<PERFORMNUD,DAD>
gif0: flags=8010<POINTOPOINT,MULTICAST> mtu 1280
stf0: flags=0<> mtu 1280
anpi1: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	options=400<CHANNEL_IO>
	ether 8e:5f:a5:50:c0:37
	media: none
	status: inactive
anpi0: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	options=400<CHANNEL_IO>
	ether 8e:5f:a5:50:c0:36
	media: none
	status: inactive
en3: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	options=400<CHANNEL_IO>
	ether 8e:5f:a5:50:c0:16
	nd6 options=201<PERFORMNUD,DAD>
	media: none
	status: inactive
en4: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	options=400<CHANNEL_IO>
	ether 8e:5f:a5:50:c0:17
	nd6 options=201<PERFORMNUD,DAD>
	media: none
	status: inactive
en1: flags=8963<UP,BROADCAST,SMART,RUNNING,PROMISC,SIMPLEX,MULTICAST> mtu 1500
	options=460<TSO4,TSO6,CHANNEL_IO>
	ether 36:14:a1:16:ea:40
	media: autoselect <full-duplex>
	status: inactive
en2: flags=8963<UP,BROADCAST,SMART,RUNNING,PROMISC,SIMPLEX,MULTICAST> mtu 1500
	options=460<TSO4,TSO6,CHANNEL_IO>
	ether 36:14:a1:16:ea:44
	media: autoselect <full-duplex>
	status: inactive
ap1: flags=8822<BROADCAST,SMART,SIMPLEX,MULTICAST> mtu 1500
	options=400<CHANNEL_IO>
	ether 3a:21:08:b0:4d:fc
	media: autoselect (none)
en0: flags=8b63<UP,BROADCAST,SMART,RUNNING,PROMISC,ALLMULTI,SIMPLEX,MULTICAST> mtu 1500
	options=6460<TSO4,TSO6,CHANNEL_IO,PARTIAL_CSUM,ZEROINVERT_CSUM>
	ether a6:0f:8a:4d:36:c6
	inet6 fe80::14b4:5699:898a:8f2f%en0 prefixlen 64 secured scopeid 0xb
	inet6 2405:201:d008:688b:40b:1366:2075:75cb prefixlen 64 autoconf secured
	inet6 2405:201:d008:688b:64c0:b6f2:2f7:c4ee prefixlen 64 autoconf temporary
	inet 192.168.29.77 netmask 0xffffff00 broadcast 192.168.29.255
	nd6 options=201<PERFORMNUD,DAD>
	media: autoselect
	status: active
bridge0: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	options=63<RXCSUM,TXCSUM,TSO4,TSO6>
	ether 36:14:a1:16:ea:40
	Configuration:
		id 0:0:0:0:0:0 priority 0 hellotime 0 fwddelay 0
		maxage 0 holdcnt 0 proto stp maxaddr 100 timeout 1200
		root id 0:0:0:0:0:0 priority 0 ifcost 0 port 0
		ipfilter disabled flags 0x0
	member: en1 flags=3<LEARNING,DISCOVER>
	        ifmaxaddr 0 port 8 priority 0 path cost 0
	member: en2 flags=3<LEARNING,DISCOVER>
	        ifmaxaddr 0 port 9 priority 0 path cost 0
	nd6 options=201<PERFORMNUD,DAD>
	media: <unknown type>
	status: inactive
awdl0: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	options=6460<TSO4,TSO6,CHANNEL_IO,PARTIAL_CSUM,ZEROINVERT_CSUM>
	ether 4a:aa:c7:d7:26:bf
	inet6 fe80::48aa:c7ff:fed7:26bf%awdl0 prefixlen 64 scopeid 0xd
	nd6 options=201<PERFORMNUD,DAD>
	media: autoselect
	status: active
llw0: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	options=400<CHANNEL_IO>
	ether 4a:aa:c7:d7:26:bf
	inet6 fe80::48aa:c7ff:fed7:26bf%llw0 prefixlen 64 scopeid 0xe
	nd6 options=201<PERFORMNUD,DAD>
	media: autoselect (none)
vmenet0: flags=8963<UP,BROADCAST,SMART,RUNNING,PROMISC,SIMPLEX,MULTICAST> mtu 1500
	ether 62:53:8b:2a:98:8d
	media: autoselect
	status: active
bridge100: flags=8a63<UP,BROADCAST,SMART,RUNNING,ALLMULTI,SIMPLEX,MULTICAST> mtu 1500
	options=3<RXCSUM,TXCSUM>
	ether b2:be:83:c6:be:64
	inet 192.168.64.1 netmask 0xffffff00 broadcast 192.168.64.255
	inet6 fe80::b0be:83ff:fec6:be64%bridge100 prefixlen 64 scopeid 0x10
	inet6 fd73:11c2:f285:ac2:413:97c1:e31e:a242 prefixlen 64 autoconf secured
	Configuration:
		id 0:0:0:0:0:0 priority 0 hellotime 0 fwddelay 0
		maxage 0 holdcnt 0 proto stp maxaddr 100 timeout 1200
		root id 0:0:0:0:0:0 priority 0 ifcost 0 port 0
		ipfilter disabled flags 0x0
	member: vmenet0 flags=3<LEARNING,DISCOVER>
	        ifmaxaddr 0 port 15 priority 0 path cost 0
	nd6 options=201<PERFORMNUD,DAD>
	media: autoselect
	status: active
vmenet1: flags=8963<UP,BROADCAST,SMART,RUNNING,PROMISC,SIMPLEX,MULTICAST> mtu 1500
	ether ba:d9:df:c5:81:65
	media: autoselect
	status: active
bridge101: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	options=3<RXCSUM,TXCSUM>
	ether b2:be:83:c6:be:65
	Configuration:
		id 0:0:0:0:0:0 priority 0 hellotime 0 fwddelay 0
		maxage 0 holdcnt 0 proto stp maxaddr 100 timeout 1200
		root id 0:0:0:0:0:0 priority 0 ifcost 0 port 0
		ipfilter disabled flags 0x0
	member: en0 flags=8003<LEARNING,DISCOVER,MACNAT>
	        ifmaxaddr 0 port 11 priority 0 path cost 0
	member: vmenet1 flags=3<LEARNING,DISCOVER>
	        ifmaxaddr 0 port 17 priority 0 path cost 0
	media: autoselect
	status: active
utun0: flags=8051<UP,POINTOPOINT,RUNNING,MULTICAST> mtu 1500
	inet6 fe80::7ec:4e22:ddb6:58de%utun0 prefixlen 64 scopeid 0x13
	nd6 options=201<PERFORMNUD,DAD>
utun1: flags=8051<UP,POINTOPOINT,RUNNING,MULTICAST> mtu 1380
	inet6 fe80::9db:2d7b:35d7:4eaa%utun1 prefixlen 64 scopeid 0x14
	nd6 options=201<PERFORMNUD,DAD>
utun2: flags=8051<UP,POINTOPOINT,RUNNING,MULTICAST> mtu 2000
	inet6 fe80::6811:d796:5a19:3187%utun2 prefixlen 64 scopeid 0x15
	nd6 options=201<PERFORMNUD,DAD>
utun3: flags=8051<UP,POINTOPOINT,RUNNING,MULTICAST> mtu 1000
	inet6 fe80::ce81:b1c:bd2c:69e%utun3 prefixlen 64 scopeid 0x16
	nd6 options=201<PERFORMNUD,DAD>
utun4: flags=8051<UP,POINTOPOINT,RUNNING,MULTICAST> mtu 1380
	inet6 fe80::a349:776f:2d6b:d414%utun4 prefixlen 64 scopeid 0x17
	nd6 options=201<PERFORMNUD,DAD>
utun5: flags=8051<UP,POINTOPOINT,RUNNING,MULTICAST> mtu 1380
	inet6 fe80::fed6:eea8:e346:68e9%utun5 prefixlen 64 scopeid 0x18
	nd6 options=201<PERFORMNUD,DAD>
"""

# Split input string into lines
lines = ifconfig_output.strip().split("\n")

interfaces = []
current = {}

for line in lines:
    line = line.strip()
    
    # Detect start of a new interface
    if not line.startswith('\t') and line:
        if current:
            interfaces.append(current)
            current = {}
        
        iface_name = line.split(":")[0]
        current["name"] = iface_name
        current["ip"] = None
        current["mac"] = None
        current["status"] = "inactive"
    
    # Parse IPv4 address
    elif "inet " in line and "127.0.0.1" not in line:
        parts = line.split()
        current["ip"] = parts[1] if len(parts) > 1 else None
    
    # Parse MAC address
    elif "ether" in line:
        parts = line.split()
        current["mac"] = parts[1] if len(parts) > 1 else None
    
    # Parse interface status
    elif "status:" in line:
        parts = line.split(":")
        current["status"] = parts[1].strip() if len(parts) > 1 else "unknown"

# Append the last interface
if current:
    interfaces.append(current)

# Print formatted result
for iface in interfaces:
    print(f"Interface: {iface['name']}")
    print(f"  IP Address : {iface['ip']}")
    print(f"  MAC Address: {iface['mac']}")
    print(f"  Status     : {iface['status']}")
    print("-" * 40)





"""
# Read the ifconfig output from a file (e.g., ifconfig_output.txt)
with open("ifconfig_output.txt", "r") as file:
    lines = file.readlines()

interfaces = []
current = {}

for line in lines:
    line = line.strip()
    
    # Detect start of new interface block
    if not line.startswith('\t') and line:
        # Save previous interface if any
        if current:
            interfaces.append(current)
            current = {}
        
        # Extract interface name
        iface_name = line.split(":")[0]
        current["name"] = iface_name
        current["ip"] = None
        current["mac"] = None
        current["status"] = "inactive"
    
    # Parse IPv4 address
    elif "inet " in line and "127.0.0.1" not in line:
        parts = line.split()
        current["ip"] = parts[1] if len(parts) > 1 else None
    
    # Parse MAC address
    elif "ether" in line:
        parts = line.split()
        current["mac"] = parts[1] if len(parts) > 1 else None
    
    # Parse status
    elif "status:" in line:
        parts = line.split(":")
        current["status"] = parts[1].strip() if len(parts) > 1 else "unknown"

# Append the last interface
if current:
    interfaces.append(current)

# Print results
for iface in interfaces:
    print(f"Interface: {iface['name']}")
    print(f"  IP Address : {iface['ip']}")
    print(f"  MAC Address: {iface['mac']}")
    print(f"  Status     : {iface['status']}")
    print("-" * 40)
"""