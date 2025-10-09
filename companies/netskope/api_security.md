# 🔒 API Security

**API Security** refers to the **practices, tools, and protocols** used to **protect APIs** from malicious attacks, misuse, or unauthorized access. APIs are the backbone of modern applications and microservices, so securing them is critical to protecting **data, services, and infrastructure**.

---

## 🎯 Objectives of API Security

| Objective | Description |
|-----------|-------------|
| **Authentication** | Verify the identity of the client accessing the API. |
| **Authorization** | Ensure the client has permission to perform specific actions. |
| **Data Protection** | Encrypt data in transit to prevent interception. |
| **Integrity** | Ensure that requests and responses are not tampered with. |
| **Availability** | Prevent API abuse (rate limiting, throttling, DDoS protection). |
| **Audit & Monitoring** | Track usage patterns, anomalies, and potential breaches. |

---

## ⚙️ 1. Key API Security Mechanisms

### A. Authentication
Ensures that the caller is who they claim to be.  
**Methods:**
- **API Keys:** Simple tokens identifying the client.  
- **OAuth 2.0:** Token-based authentication for delegated access.  
- **JWT (JSON Web Tokens):** Signed tokens carrying authentication info.  
- **mTLS (Mutual TLS):** Both client and server verify each other.  

---

### B. Authorization
Controls **what authenticated users can do**.  
**Methods:**
- **Role-Based Access Control (RBAC):** Permissions based on roles.  
- **Attribute-Based Access Control (ABAC):** Permissions based on attributes like user, location, device.  
- **Scopes (OAuth 2.0):** Limit actions for a token to certain resources.  

---

### C. Encryption
- **HTTPS / TLS:** All API requests and responses must use TLS for **data-in-transit security**.  
- **Payload encryption:** Encrypt sensitive fields inside request/response bodies if necessary.  
- **Key management:** Rotate API keys, secrets, and certificates regularly.  

---

### D. Input Validation & Threat Prevention
- Validate all inputs to prevent injection attacks (SQLi, XSS, XML External Entity attacks).  
- Reject requests with malformed or unexpected data.  
- Use **WAFs (Web Application Firewalls)** for automated threat detection.  

---

### E. Rate Limiting & Throttling
- Limit the number of requests a client can make per minute/hour.  
- Protect APIs from **DDoS attacks** or excessive usage.  
- Example: Allow 1000 requests per hour per user or API key.  

---

### F. Logging, Monitoring & Auditing
- Log all API calls, including IP, user, timestamp, and endpoint.  
- Detect unusual patterns or anomalies.  
- Maintain audit trails for compliance and forensic analysis.  
**Tools:** Datadog, Splunk, ELK stack, Cloud-native logging.  

---

### G. API Gateway & Security Policies
- Use an **API Gateway** to centralize security, routing, and monitoring.  
- Features:
  - Authentication and authorization enforcement.
  - Rate limiting and throttling.
  - Logging, metrics, and analytics.
  - Payload validation and transformation.  

**Examples:** AWS API Gateway, Apigee, Kong, NGINX, Azure API Management

---

## 🧱 2. Common API Security Threats

| Threat | Description |
|--------|-------------|
| **Injection Attacks** | Malicious data (SQL, NoSQL, command injection) through API inputs. |
| **Broken Authentication** | Weak or compromised credentials. |
| **Excessive Data Exposure** | Returning sensitive data unnecessarily. |
| **DDoS / Rate Abuse** | Overwhelming API with requests. |
| **Man-in-the-Middle (MITM)** | Intercepting unencrypted API traffic. |
| **Replay Attacks** | Reusing intercepted requests to perform malicious actions. |
| **Broken Object Level Authorization (BOLA)** | Accessing resources not belonging to the user. |

---

## ⚡ 3. Best Practices for API Security

1. **Always use HTTPS / TLS** for all API communications.  
2. **Authenticate every request** using API keys, OAuth 2.0, or JWT.  
3. **Implement fine-grained authorization** for all endpoints.  
4. **Validate and sanitize inputs** to prevent injection attacks.  
5. **Encrypt sensitive data** both in transit and, if needed, at rest.  
6. **Limit request rates** using throttling and quotas.  
7. **Log and monitor all API activity**.  
8. **Version your APIs** and retire old versions securely.  
9. **Use API gateways** to centralize security policies.  
10. **Rotate credentials and tokens regularly**.  

---

## 🧠 4. Example: Secure API Flow

