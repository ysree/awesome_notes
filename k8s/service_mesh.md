
# Service Mesh

## 🔹 What is a Service Mesh?
A **service mesh** is a dedicated infrastructure layer that controls **service-to-service communication** in a microservices architecture.  
It handles **network traffic, observability, security, and reliability** without requiring changes in application code.  

---

## 🔹 Why Service Mesh?
As microservices grow, managing **service discovery, retries, security (mTLS), tracing, and traffic policies** becomes complex.  
A service mesh solves this by **abstracting these concerns** away from developers.

---

## 🔹 Key Components

### 1. Data Plane
- Made up of **sidecar proxies** (usually Envoy) that run alongside each microservice instance (pod or container).  
- Handles:

  - Service discovery  
  - Load balancing  
  - Health checks  
  - Traffic routing  
  - mTLS encryption/decryption  
  - Metrics collection  

### 2. Control Plane
- Manages and configures the proxies in the data plane.  
- Provides:

  - Policy management (traffic rules, retries, failovers)  
  - Security enforcement (mTLS, RBAC)  
  - Telemetry collection and aggregation  

Examples:  
- **Istio** (Istiod)  
- **Linkerd Control Plane**  

---

## 🔹 Service Mesh Features

- **Traffic Management**: routing, canary releases, blue-green deployments.  
- **Security**: mTLS for service-to-service encryption, authentication, RBAC.  
- **Observability**: tracing, metrics, and logs for every request (using Envoy).  
- **Reliability**: retries, circuit breaking, rate limiting, fault injection.  
- **Service Discovery**: auto-detects healthy service endpoints.  

---

## 🔹 How It Works (Flow)

1. Each service instance runs with a **sidecar proxy** (e.g., Envoy).  
2. Service A → Service B traffic is routed through the **sidecar proxies**.  
3. Proxies handle **load balancing, security, observability**.  
4. The **control plane** configures the proxies dynamically.  

---

## 🔹 Example: Istio Service Mesh

- **Data Plane**: Envoy proxies as sidecars with each pod.  
- **Control Plane**: Istiod handles service discovery, configuration, and certificates.  
- Integrates with **Prometheus, Grafana, Jaeger/Kiali** for observability.  

---

## 🔹 Diagram (Textual)

```
         +------------------+
         |   Control Plane  |
         | (Istiod / etc.)  |
         +------------------+
                 |
   ---------------------------------
   |                               |
+-------+       +-------+       +-------+
| App A |       | App B |       | App C |
| Side- |<----->| Side- |<----->| Side- |
| car   |       | car   |       | car   |
|Proxy  |       |Proxy  |       |Proxy  |
+-------+       +-------+       +-------+
```

---

✅ **In summary**:  
A **service mesh** is a layer of **sidecar proxies** + **control plane** that makes service-to-service communication **secure, reliable, and observable** without touching app code.

---

## Difference between API Gatewau & Service Mesh
**API gateways** are for north-south traffic, **service meshes** are for east-west.