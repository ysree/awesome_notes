# 🐳 Reducing Container Size (3 GB Example)

Reducing the size of a **3 GB container image** can be done using best practices in Docker/OCI image creation, layering, and cleanup.

---

## 1️⃣ Start with a Minimal Base Image
- Use a **slim or alpine-based image** instead of full distributions.
```dockerfile
# Instead of this:
FROM ubuntu:22.04

# Use this:
FROM ubuntu:22.04-slim
# Or even:
FROM alpine:3.20
```
> Alpine images are very small (~5 MB) vs Ubuntu (~70-100 MB).

---

## 2️⃣ Minimize Layers
- Each `RUN`, `COPY`, or `ADD` instruction creates a new layer.
- Combine commands to reduce layers:
```dockerfile
# Bad: multiple layers
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get clean

# Good: single layer
RUN apt-get update &&     apt-get install -y curl &&     apt-get clean &&     rm -rf /var/lib/apt/lists/*
```

---

## 3️⃣ Remove Unnecessary Packages & Files
```dockerfile
RUN apt-get update &&     apt-get install -y build-essential curl &&     apt-get remove --purge -y build-essential &&     apt-get clean &&     rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
```
- For Node.js or Python:
```dockerfile
RUN pip install --no-cache-dir -r requirements.txt
RUN npm install --production
```

---

## 4️⃣ Use Multi-Stage Builds
- Separate build environment from runtime:
```dockerfile
# Stage 1: Build
FROM golang:1.21 AS builder
WORKDIR /app
COPY . .
RUN go build -o myapp

# Stage 2: Runtime
FROM alpine:3.20
COPY --from=builder /app/myapp /usr/local/bin/myapp
CMD ["myapp"]
```
> Only `myapp` ends up in the final image. Build tools stay in the first stage.

---

## 5️⃣ Avoid Adding Large Files / Data
- Don’t `COPY` unnecessary files like `.git`, docs, tests, datasets.
- Use `.dockerignore`:
```
.git
*.md
tests/
docs/
node_modules/
```

---

## 6️⃣ Use Compressed Layers / Smaller Artifacts
- Strip binaries (remove debug symbols):
```dockerfile
RUN strip /usr/local/bin/myapp
```

---

## 7️⃣ Use Smaller Runtimes / Language-specific Minimal Images
- Python: `python:3.12-slim` or `python:3.12-alpine`  
- Node.js: `node:20-slim`  
- Java: `openjdk:20-jdk-slim` or `distroless/java`  

---

## 8️⃣ Analyze Image Size
```bash
docker image ls
docker history <image_name>
docker image inspect <image_name>
```
- Identify which layer is causing the bulk size.

---

## 9️⃣ Optional: Use Distroless or Scratch Images
- **Distroless images** include only your app and minimal runtime libraries.  
- **Scratch image** is empty; only your compiled binary goes in.
```dockerfile
FROM gcr.io/distroless/base
COPY myapp /
CMD ["/myapp"]
```

---

## ✅ Summary of Steps
1. Start with **slim/alpine base images**  
2. **Combine RUN instructions** to reduce layers  
3. **Remove unnecessary packages & files**  
4. Use **multi-stage builds** to separate build & runtime  
5. Avoid adding large unnecessary files  
6. Strip binaries / use minimal runtimes  
7. Analyze image size with `docker history`  

> Following these steps can **reduce your 3 GB container to a few hundred MB or even less** depending on the application.
