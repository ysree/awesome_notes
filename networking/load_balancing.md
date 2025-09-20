# ⚖️ Load Balancing

## 🔹 What is Load Balancing?
Load balancing is the process of distributing incoming network or application traffic across multiple servers.  
The goal is to improve **scalability, availability, fault tolerance, and performance**.

---

## 🔹 Types of Load Balancers

1. **Hardware Load Balancers**  
   - Physical appliances.  
   - High performance but expensive.  
   - Example: F5 Big-IP, Citrix NetScaler.

2. **Software Load Balancers**  
   - Installed on commodity hardware or cloud instances.  
   - Flexible and cheaper.  
   - Example: Nginx, HAProxy, Envoy.

3. **DNS Load Balancing**  
   - Uses DNS to distribute requests across multiple IPs.  
   - Basic, no real-time health awareness.

4. **Layer 4 Load Balancing (Transport Layer)**  
   - Distributes traffic based on **IP and TCP/UDP ports**.  
   - Faster but less flexible.  
   - Example: AWS NLB, HAProxy (L4 mode).

5. **Layer 7 Load Balancing (Application Layer)**  
   - Makes decisions based on **HTTP headers, cookies, URLs, content type**.  
   - Supports advanced routing (e.g., path-based, host-based).  
   - Example: AWS ALB, Nginx, Traefik.

---

## 🔹 Load Balancing Algorithms

1. **Round Robin** – Each request is sent to servers in rotation.  
2. **Least Connections** – Chooses server with the fewest active connections.  
3. **Least Response Time** – Picks server with lowest latency.  
4. **IP Hash** – Routes based on client IP (provides stickiness).  
5. **Weighted Round Robin / Weighted Least Connections** – Allocates more traffic to stronger servers.  
6. **Random** – Distributes requests randomly.

- ![Load Balancing Algorithms](/SystemDesign/LoadBalancerAlgos.gif)
---

## 🔹 Health Checks
Load balancers monitor server health to avoid routing traffic to failed or slow servers.

- **Types of Health Checks**:
  - **TCP Check** → Verifies if port is open.  
  - **HTTP/HTTPS Check** → Sends request to a path (e.g., `/health`) and expects `200 OK`.  
  - **Custom Script Check** → Runs custom validation.  

- **Example**: If `/health` does not return `200 OK`, the load balancer removes that server from the pool.

---

## 🔹 Benefits of Load Balancing
- High availability and fault tolerance  
- Scalability (horizontal scaling)  
- Optimized resource utilization  
- Improved user experience through reduced latency  
