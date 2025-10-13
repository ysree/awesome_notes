# 🐳 Docker Notes

## 1️⃣ What is Docker?

Docker is an open-source platform that automates the deployment, scaling, and management of applications using containerization.

Containers are lightweight, portable, and self-contained environments that include everything needed to run an application: code, runtime, libraries, and dependencies.

Unlike virtual machines, containers share the host OS kernel, making them faster and smaller.

---

## 2️⃣ Key Docker Concepts

| Concept       | Description |
|---------------|------------|
| Image         | A read-only template with application code and dependencies. |
| Container     | A runnable instance of an image. Can be started, stopped, or deleted. |
| Docker Engine | Runtime that runs containers. Includes server (`dockerd`) and CLI (`docker`). |
| Dockerfile    | Text file containing instructions to build a Docker image. |
| Docker Hub    | Public registry to store and share images. |
| Volume        | Persistent storage for containers. |
| Network       | Connect containers internally or externally. |

---

## 3️⃣ Benefits of Docker

- **Portability:** Runs the same on any system with Docker.  
- **Isolation:** Applications and dependencies are isolated.  
- **Resource-efficient:** Lightweight compared to VMs.  
- **Fast deployment:** Start containers in seconds.  
- **Version control & CI/CD friendly:** Integrates with DevOps pipelines.

---

## 4️⃣ Docker Architecture

- **Docker Client:** CLI for users (`docker run`, `docker build`).  
- **Docker Daemon:** Runs on the host machine; builds, runs, and manages containers.  
- **Docker Registries:** Stores images (Docker Hub, private registries).  
- **Containers:** Running instances of Docker images.  

```
Client <---> Daemon <---> Containers
               |
               +--> Registry (Pull/Push Images)
```


---

## 5️⃣ Docker Commands Cheat Sheet

| Command           | Description                     | Example |
|------------------|---------------------------------|---------|
| docker build      | Build image from Dockerfile     | `docker build -t myapp:1.0 .` |
| docker run        | Run a container from an image   | `docker run -d -p 80:80 nginx` |
| docker ps         | List running containers         | `docker ps` |
| docker ps -a      | List all containers             | `docker ps -a` |
| docker stop       | Stop a running container        | `docker stop <container_id>` |
| docker rm         | Remove container                | `docker rm <container_id>` |
| docker rmi        | Remove image                    | `docker rmi <image_id>` |
| docker logs       | View container logs             | `docker logs <container_id>` |
| docker exec       | Run command inside container    | `docker exec -it <container_id> bash` |
| docker images     | List images                     | `docker images` |
| docker network ls | List Docker networks            | `docker network ls` |
| docker volume ls  | List Docker volumes             | `docker volume ls` |

---

## 6️⃣ Dockerfile Basics

**Example Dockerfile for a Python app:**

```dockerfile
# Use official Python image
FROM python:3.12-slim

# Set working directory
WORKDIR /app

# Copy requirements
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy application code
COPY . .

# Command to run the app
CMD ["python", "app.py"]

```

## Docker Best Practices, Volumes, Networks, Compose, and Optimization

## Best Practices for Dockerfiles

- Use minimal base images (slim or alpine)  
- Combine `RUN` instructions to reduce layers  
- Clean caches after installation  

---

## 7️⃣ Docker Volumes and Networks

**Volumes:** Persistent data independent of container lifecycle.

```bash
docker volume create mydata
docker run -v mydata:/app/data myapp

```

**Networks**: Enable communication between containers.

```
docker network create mynet
docker run --network mynet --name web nginx
docker run --network mynet --name app myapp
```

## 8️⃣ Docker Compose

```
version: '3'
services:
  web:
    image: nginx
    ports:
      - "80:80"
  app:
    build: .
    volumes:
      - .:/app
    depends_on:
      - db
  db:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: root
```

**Commands:**
```
docker-compose up -d
docker-compose down
```

## 9️⃣ Docker vs Virtual Machines

| Feature        | Docker                     | VM                            |
| -------------- | -------------------------- | ----------------------------- |
| Isolation      | Process-level (containers) | OS-level (hypervisor)         |
| Resource usage | Lightweight                | Heavy                         |
| Boot time      | Seconds                    | Minutes                       |
| Portability    | High                       | Lower (depends on hypervisor) |
| Disk space     | Small                      | Large                         |
