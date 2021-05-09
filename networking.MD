##Isolate Docker pods

![docker_network_isolation](https://github.com/ysree/awesome_notes/blob/b5821157d4662cda0a3e6aa4d8ac083c6bc12f7d/images/docker_network_isolation.png)

```
# Allow communication between the NGINX proxy and the Apache
firewall-cmd --direct --add-rule ipv4 filter DOCKER-ISOLATION 0 -s <wordpress_php_ip> -d <wordpress_nginx_ip> -j ACCEPT
firewall-cmd --direct --add-rule ipv4 filter DOCKER-ISOLATION 0 -s <wordpress_nginx_ip> -d <wordpress_php_ip> -j ACCEPT
# Allow communication between the Apache and the MySQL
firewall-cmd --direct --add-rule ipv4 filter DOCKER-ISOLATION 0 -s <wordpress_php_ip> -d <wordpress_db_ip> -j ACCEPT
firewall-cmd --direct --add-rule ipv4 filter DOCKER-ISOLATION 0 -s <wordpress_db_ip> -d <wordpress_php_ip> -j ACCEPT
# Drop all outgoing communication from the Apache and the MySQL
firewall-cmd --direct --add-rule ipv4 filter DOCKER-ISOLATION 2 -s <wordpress_db_ip> -j DROP
firewall-cmd --direct --add-rule ipv4 filter DOCKER-ISOLATION 2 -s <honeypot_php_ip> -j DROP
```

## IP Tables Natting & Forwarding between Interfaces
```
iptables --table nat --append POSTROUTING --out-interface ens160 -j MASQUERADE
iptables --append FORWARD --in-interface ens192 -j ACCEPT
```

## Adding routes in Linux 
```
route add -net 192.168.185.0/24 gw 30.30.30.2 dev ens192
route add -net 192.168.186.0/24 gw 30.30.30.2 dev ens192
route add -net 192.168.195.0/24 gw 30.30.30.2 dev ens192
route add -net 192.168.196.0/24 gw 30.30.30.2 dev ens192
```
