# Docker Commands Reference Guide

## Container Management

### Basic Container Operations
- `docker run [OPTIONS] IMAGE` - Create and start a new container
- `docker start CONTAINER` - Start an existing container
- `docker stop CONTAINER` - Stop a running container
- `docker restart CONTAINER` - Restart a container
- `docker rm CONTAINER` - Remove a container
- `docker ps` - List running containers
- `docker ps -a` - List all containers (including stopped)
- `docker exec -it CONTAINER COMMAND` - Execute a command in a running container
- `docker logs CONTAINER` - View container logs

## Image Management

### Basic Image Operations
- `docker pull IMAGE` - Download an image from a registry
- `docker build -t NAME .` - Build an image from a Dockerfile
- `docker images` - List local images
- `docker rmi IMAGE` - Remove an image
- `docker tag SOURCE_IMAGE TARGET_IMAGE` - Tag an image
- `docker push IMAGE` - Upload an image to a registry

## Commands with Shell Selection ($)

### Stop/Remove Multiple Containers

```bash
# Stop all running containers
docker stop $(docker ps -q)

# Remove all stopped containers
docker rm $(docker ps -aq)

# Stop and remove all containers
docker stop $(docker ps -q) && docker rm $(docker ps -aq)

# Remove containers by pattern
docker rm $(docker ps -aq --filter "name=test")
```

### Image Operations with Selection

```bash
# Remove all images
docker rmi $(docker images -q)

# Remove dangling images
docker rmi $(docker images -f "dangling=true" -q)

# Remove images by pattern
docker rmi $(docker images --format "table {{.Repository}}:{{.Tag}}" | grep "myapp")
```

### Advanced Container Operations

```bash
# Get container IDs by name pattern
docker ps -q --filter "name=web"

# Stop containers older than 24 hours
docker stop $(docker ps --filter "status=running" --format "{{.ID}} {{.CreatedAt}}" | awk '$2 < "'$(date -d '1 day ago' -Iso)'"' | cut -d' ' -f1)

# Copy files from multiple containers
docker cp $(docker ps -q --filter "name=app"):/app/logs ./logs/

# Execute command in all containers with specific label
docker ps -q --filter "label=env=production" | xargs -I {} docker exec {} /app/healthcheck.sh
```

### Volume and Network Selection

```bash
# Remove all unused volumes
docker volume rm $(docker volume ls -qf dangling=true)

# List volume names only
docker volume ls --format "{{.Name}}"

# Remove networks by pattern
docker network rm $(docker network ls -q --filter "name=test")
```

### Inspection with Selection

```bash
# Get IP addresses of running containers
docker inspect $(docker ps -q) --format='{{.Name}} - {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'

# Get container names and status
docker ps --format "table {{.Names}}\t{{.Status}}"

# Get image sizes
docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"
```

### Cleanup Operations

```bash
# Complete cleanup (use with caution)
docker stop $(docker ps -q) && docker rm $(docker ps -aq) && docker rmi $(docker images -q)

# Remove containers exited with error
docker rm $(docker ps -aq --filter "exited=1")

# Remove containers older than a week
docker container prune --filter "until=168h"
```

## System and Cleanup

### Basic Cleanup Commands
- `docker system prune` - Remove unused containers, networks, and images
- `docker system prune -a` - Remove all unused images (not just dangling)
- `docker container prune` - Remove stopped containers
- `docker image prune` - Remove unused images
- `docker volume prune` - Remove unused volumes
- `docker network prune` - Remove unused networks

## Volume and Network Management

### Basic Volume and Network Operations
- `docker volume ls` - List volumes
- `docker volume create VOLUME` - Create a volume
- `docker network ls` - List networks
- `docker network create NETWORK` - Create a network

## Inspection and Information

### Basic Information Commands
- `docker inspect CONTAINER/IMAGE` - Display detailed information
- `docker stats` - Display live resource usage statistics
- `docker version` - Show Docker version information
- `docker info` - Display system-wide information

## Docker Compose Commands

### Docker Compose Operations (if using docker-compose)
- `docker-compose up` - Start services defined in docker-compose.yml
- `docker-compose down` - Stop and remove services
- `docker-compose ps` - List running services
- `docker-compose logs` - View service logs

## Useful Combinations

### Advanced Shell Integration Examples

```bash
# One-liner to clean everything
docker system prune -af --volumes

# Backup container data before cleanup
docker run --rm -v $(docker inspect CONTAINER --format='{{range .Mounts}}{{.Source}}:{{.Destination}}{{end}}') -v $(pwd):/backup alpine tar czf /backup/container-backup.tar.gz /data

# Monitor resource usage of specific containers
watch docker stats $(docker ps --format={{.Names}} | tr '\n' ' ')
```

## Quick Reference Notes

- The `$()` syntax allows you to use the output of one Docker command as input to another
- `-q` flag returns only container/image IDs for easy piping
- `--filter` allows filtering results by various criteria
- `--format` enables custom output formatting
- Always be cautious with cleanup commands that use `$(docker ps -q)` or `$(docker images -q)`
- Use `docker ps -a` to see stopped containers
- Use `docker images -a` to see all images including intermediate layers

## Most Essential Commands to Memorize

1. `docker run`
2. `docker ps`
3. `docker exec`
4. `docker build`
5. `docker logs`
6. `docker stop $(docker ps -q)` - Stop all containers
7. `docker system prune` - Basic cleanup