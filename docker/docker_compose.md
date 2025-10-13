# 🐳 Docker Compose Notes

## 1️⃣ What is Docker Compose?

Docker Compose is a tool for defining and running multi-container Docker applications.

You define services, networks, and volumes in a single YAML file (`docker-compose.yml`).

With one command, you can start, stop, or manage the entire application stack.

---

## 2️⃣ Benefits of Docker Compose

- Simplifies multi-container setup (web, app, database)  
- Consistent environments across development, staging, and production  
- Version control for infrastructure as code  
- Service orchestration: define dependencies and startup order  

---

## 3️⃣ Docker Compose File Structure

**Example `docker-compose.yml`:**

```yaml
version: '3.9'

services:
  web:
    image: nginx:latest
    ports:
      - "80:80"
    volumes:
      - ./html:/usr/share/nginx/html
    networks:
      - webnet

  app:
    build: ./app
    depends_on:
      - db
    environment:
      - DATABASE_HOST=db
      - DATABASE_PORT=3306
    networks:
      - webnet

  db:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: mydb
    volumes:
      - db_data:/var/lib/mysql
    networks:
      - webnet

volumes:
  db_data:

networks:
  webnet:
```

## 4️⃣ Key Sections of Docker Compose


| Section    | Description                                                 |
| ---------- | ----------------------------------------------------------- |
| version    | Compose file version (e.g., '3', '3.9')                     |
| services   | Defines the containers (images, build context, environment) |
| volumes    | Persistent storage for containers                           |
| networks   | Defines networks for inter-container communication          |
| depends_on | Ensures services start in order                             |

## 5️⃣ Common Commands
| Command                | Description                  | Example                        |
| ---------------------- | ---------------------------- | ------------------------------ |
| docker-compose up      | Start all services           | `docker-compose up -d`         |
| docker-compose down    | Stop and remove containers   | `docker-compose down`          |
| docker-compose build   | Build images                 | `docker-compose build`         |
| docker-compose ps      | List running containers      | `docker-compose ps`            |
| docker-compose logs    | View logs                    | `docker-compose logs -f`       |
| docker-compose exec    | Run command inside a service | `docker-compose exec app bash` |
| docker-compose restart | Restart services             | `docker-compose restart web`   |


# 6️⃣ Best Practices

- Use .env files for environment variables
- Use volumes for persistent storage
- Keep service definitions modular and clear
- Avoid committing secrets in docker-compose.yml
- Use depends_on wisely; for robust dependency management, consider healthchecks

## 7️⃣ Example: Using .env File
### .env
```bash

MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=mydb
```
### Reference in docker-compose.yml:
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
  MYSQL_DATABASE: ${MYSQL_DATABASE}

## 8️⃣ Docker Compose vs Docker CLI

| Feature                          | Docker Compose | Docker CLI |
| -------------------------------- | -------------- | ---------- |
| Multi-container orchestration    | ✅              | ❌          |
| Version-controlled configuration | ✅              | ❌          |
| Easy startup/teardown            | ✅              | ❌          |
| Single container management      | ❌              | ✅          |
