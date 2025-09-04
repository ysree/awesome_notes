# Docker Debugging Commands Reference

## Container Debugging

### Basic Container Inspection
```bash
# View container logs
docker logs CONTAINER
docker logs -f CONTAINER                    # Follow logs in real-time
docker logs --tail 50 CONTAINER            # Show last 50 lines
docker logs --since 1h CONTAINER           # Logs from last hour
docker logs --timestamps CONTAINER         # Include timestamps

# Get detailed container information
docker inspect CONTAINER
docker inspect CONTAINER | jq '.[0].State'  # Using jq for JSON parsing
docker inspect --format='{{.State.Status}}' CONTAINER
docker inspect --format='{{.NetworkSettings.IPAddress}}' CONTAINER

# Container resource usage
docker stats CONTAINER
docker stats --no-stream CONTAINER         # Single snapshot
docker stats $(docker ps -q)               # All running containers
```

### Interactive Debugging
```bash
# Enter running container
docker exec -it CONTAINER /bin/bash
docker exec -it CONTAINER /bin/sh          # If bash not available
docker exec -it CONTAINER sh

# Run commands in container
docker exec CONTAINER ps aux
docker exec CONTAINER netstat -tulpn
docker exec CONTAINER df -h
docker exec CONTAINER cat /etc/hosts

# Debug with different user
docker exec -it --user root CONTAINER /bin/bash
docker exec -it --user 0 CONTAINER /bin/sh

# Run container with debugging shell
docker run -it --rm IMAGE /bin/bash        # Temporary debug container
docker run -it --rm --entrypoint /bin/bash IMAGE
```

### Process and System Information
```bash
# View processes in container
docker exec CONTAINER ps aux
docker exec CONTAINER top
docker top CONTAINER                        # View from host

# System information inside container
docker exec CONTAINER uname -a
docker exec CONTAINER cat /etc/os-release
docker exec CONTAINER env                   # Environment variables
docker exec CONTAINER mount                 # Mounted filesystems
```

## Network Debugging

### Network Inspection
```bash
# List networks
docker network ls

# Inspect network details
docker network inspect NETWORK
docker network inspect bridge              # Default bridge network

# Container network information
docker exec CONTAINER ip addr show
docker exec CONTAINER ip route
docker exec CONTAINER netstat -rn
docker exec CONTAINER cat /etc/resolv.conf

# Check port bindings
docker port CONTAINER
docker inspect CONTAINER | grep -i port
```

### Connectivity Testing
```bash
# Test connectivity from container
docker exec CONTAINER ping google.com
docker exec CONTAINER nslookup google.com
docker exec CONTAINER curl -I http://example.com
docker exec CONTAINER telnet HOST PORT

# Test connectivity to container
curl -I http://localhost:PORT
telnet localhost PORT
nc -zv localhost PORT                       # Using netcat

# DNS debugging
docker exec CONTAINER nslookup SERVICE_NAME
docker exec CONTAINER dig SERVICE_NAME
docker exec CONTAINER cat /etc/hosts
```

## Volume and Storage Debugging

### Volume Inspection
```bash
# List volumes
docker volume ls
docker volume inspect VOLUME

# Check container mounts
docker inspect CONTAINER | grep -A 10 "Mounts"
docker inspect --format='{{range .Mounts}}{{.Type}}: {{.Source}} -> {{.Destination}}{{"\n"}}{{end}}' CONTAINER

# Disk usage in container
docker exec CONTAINER df -h
docker exec CONTAINER du -sh /path/to/directory
docker system df                            # Docker disk usage
```

### File System Debugging
```bash
# Copy files for debugging
docker cp CONTAINER:/path/to/file ./local/path
docker cp ./local/file CONTAINER:/path/to/destination

# Check file permissions
docker exec CONTAINER ls -la /path/to/file
docker exec CONTAINER stat /path/to/file

# Find files in container
docker exec CONTAINER find / -name "*.log" 2>/dev/null
docker exec CONTAINER find /var/log -type f -exec ls -la {} \;
```

## Image Debugging

### Image Inspection
```bash
# Inspect image details
docker inspect IMAGE
docker history IMAGE                        # Show image layers
docker history --no-trunc IMAGE            # Full layer commands

# Check image size and layers
docker images IMAGE
docker inspect --format='{{.Size}}' IMAGE

# Scan image for vulnerabilities (if available)
docker scan IMAGE
```

### Dockerfile Debugging
```bash
# Build with debugging
docker build --no-cache -t IMAGE .
docker build --progress=plain -t IMAGE .   # Show detailed output
docker build --target debug -t IMAGE .     # Multi-stage debug target

# Debug specific build step
docker build -t temp-debug --target=step2 .
docker run -it --rm temp-debug /bin/bash
```

## Performance Debugging

### Resource Monitoring
```bash
# Real-time resource usage
docker stats
docker stats --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}"

# Container resource limits
docker inspect CONTAINER | grep -i -A 10 "resources"
docker exec CONTAINER cat /sys/fs/cgroup/memory/memory.limit_in_bytes
docker exec CONTAINER cat /proc/meminfo
docker exec CONTAINER cat /proc/cpuinfo
```

### Process Debugging
```bash
# Process tree in container
docker exec CONTAINER ps auxf
docker exec CONTAINER pstree -p

# Memory usage by process
docker exec CONTAINER ps aux --sort=-%mem
docker exec CONTAINER ps aux --sort=-%cpu

# Check for zombie processes
docker exec CONTAINER ps aux | grep -i zombie
```

## Error Debugging

### Container Start Issues
```bash
# Check why container exited
docker logs CONTAINER
docker inspect --format='{{.State.ExitCode}}' CONTAINER
docker inspect --format='{{.State.Error}}' CONTAINER

# Run container with debugging
docker run -it --rm IMAGE /bin/bash        # Override entrypoint
docker run -it --entrypoint="" IMAGE /bin/bash

# Check container events
docker events --filter container=CONTAINER
docker events --since 1h --filter type=container
```

### Application Debugging
```bash
# Application logs
docker exec CONTAINER tail -f /var/log/app.log
docker exec CONTAINER journalctl -f        # If systemd available

# Check application configuration
docker exec CONTAINER cat /etc/app/config.ini
docker exec CONTAINER env | grep APP_

# Debug application startup
docker run -it --rm IMAGE strace -f /app/binary
docker run -it --rm IMAGE ldd /app/binary  # Check library dependencies
```

## Advanced Debugging

### System-level Debugging
```bash
# Docker daemon debugging
docker system events
docker system events --filter type=container
docker system info
docker version

# Check Docker daemon logs (varies by system)
sudo journalctl -fu docker.service         # systemd
sudo tail -f /var/log/docker.log          # traditional logging

# Docker daemon configuration
docker info | grep -i root
cat /etc/docker/daemon.json
```

### Multi-container Debugging
```bash
# Debug Docker Compose services
docker-compose logs SERVICE
docker-compose logs -f --tail 100
docker-compose ps
docker-compose top

# Service connectivity in compose
docker-compose exec SERVICE ping OTHER_SERVICE
docker-compose exec SERVICE nslookup OTHER_SERVICE
```

### Security Debugging
```bash
# Check container security context
docker exec CONTAINER id
docker exec CONTAINER whoami
docker exec CONTAINER cat /proc/self/status | grep -i cap

# Check SELinux/AppArmor context
docker exec CONTAINER cat /proc/self/attr/current
docker exec CONTAINER aa-status            # AppArmor

# Audit container capabilities
docker run --rm -it --cap-add SYS_ADMIN IMAGE /bin/bash
docker run --rm -it --privileged IMAGE /bin/bash
```

## Useful Shell Selection for Debugging

### Bulk Debugging Operations
```bash
# Get logs from all containers
for container in $(docker ps -q); do
    echo "=== Logs for $container ==="
    docker logs --tail 20 $container
done

# Check resource usage of all containers
docker stats $(docker ps --format={{.Names}} | tr '\n' ' ')

# Get IP addresses of all running containers
docker inspect $(docker ps -q) --format='{{.Name}} {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'

# Find containers with high memory usage
docker stats --no-stream --format "table {{.Name}}\t{{.MemUsage}}" | sort -k2 -h

# Debug all containers that exited with error
for container in $(docker ps -aq --filter "exited=1"); do
    echo "=== Debug container $container ==="
    docker logs $container
    docker inspect --format='{{.State.Error}}' $container
done
```

## Common Debugging Scenarios

### Container Won't Start
1. Check logs: `docker logs CONTAINER`
2. Check exit code: `docker inspect --format='{{.State.ExitCode}}' CONTAINER`
3. Try interactive mode: `docker run -it --rm IMAGE /bin/bash`
4. Check image: `docker history IMAGE`

### Network Issues
1. Check container network: `docker exec CONTAINER ip addr`
2. Test connectivity: `docker exec CONTAINER ping TARGET`
3. Check ports: `docker port CONTAINER`
4. Inspect network: `docker network inspect NETWORK`

### Performance Issues
1. Monitor resources: `docker stats CONTAINER`
2. Check processes: `docker exec CONTAINER ps aux`
3. Check disk usage: `docker exec CONTAINER df -h`
4. Review limits: `docker inspect CONTAINER | grep -i resources`

### Application Not Working
1. Check application logs: `docker logs -f CONTAINER`
2. Verify environment: `docker exec CONTAINER env`
3. Check configuration: `docker exec CONTAINER cat /etc/app/config`
4. Test manually: `docker exec -it CONTAINER /bin/bash`