# Envoy Proxy

## Definition
**Envoy Proxy** is a high-performance **open-source edge and service proxy** designed for **cloud-native applications**.  
It acts as a **communication bus** for microservices, providing advanced **traffic management, observability, and security**.

---

## Key Features

1. **Layer 7 (Application Layer) Proxy**
   - Supports HTTP/HTTPS, gRPC, and WebSocket protocols.  
   - Performs **routing, retries, and load balancing** at the application layer.

2. **Service Discovery**
   - Integrates with service registries like **Consul, Kubernetes, and DNS**.  
   - Automatically discovers services for dynamic routing.

3. **Load Balancing**
   - Supports **round-robin, least request, random, and weighted load balancing**.  
   - Provides **active health checking** to remove unhealthy instances.

4. **Traffic Management**
   - Fine-grained **routing, retries, timeouts, rate limiting**.  
   - Supports **circuit breaking** to prevent cascading failures.

5. **Observability**
   - Metrics, logging, and distributed **tracing** (integration with Prometheus, Jaeger).  
   - Real-time visibility into service-to-service communication.

6. **Security**
   - TLS termination, mutual TLS (mTLS), and **authentication**.  
   - Can enforce access control and service-to-service encryption.

7. **Extensibility**
   - Uses **filters** to extend functionality (HTTP filters, network filters).  
   - Configurable dynamically via **xDS APIs**.

---

## Use Cases
- **Edge Proxy / API Gateway** – Routes external traffic to services.  
- **Service-to-Service Proxy (Sidecar)** – Used in **service mesh architectures** like Istio.  
- **Load Balancer** – Distributes traffic efficiently across services.  
- **Observability Platform** – Monitors and traces microservice communications.

---

## Advantages
- High-performance, low-latency proxy.  
- Rich **traffic control** and observability features.  
- Cloud-native and **Kubernetes-friendly**.  
- Extensible and configurable dynamically without restarts.

---

## Envoy proxy provides the following features, and more, out of the box:
  - HTTP2
  - Transparent HTTP/1.1 to HTTP/2 proxy
  - Service discovery
  - Adaptive routing / client side load balancing
  - Automatic retries
  - Circuit breakers
  - Timeout controls
  - Back pressure
  - Rate limiting
  - Metrics/stats collection
  - Tracing
  - request shadowing
  - Service refactoring / request shadowing
  - TLS between services
  - Forced service isolation / outlier detection

---
## Summary
Envoy Proxy is a **modern, programmable proxy** used in microservices and cloud-native architectures for **routing, security, load balancing, and observability**.  
It is widely adopted in service meshes like **Istio** and other distributed systems.


