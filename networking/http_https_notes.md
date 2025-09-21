# 🌐 HTTP & HTTPS Notes  

## 🔹 HTTP (HyperText Transfer Protocol)  
- Application-layer protocol for communication between browser (client) and web server.  
- **Stateless**: Each request is independent.  
- Uses **port 80** by default.  
- Data is sent as plain text → not secure.  

## 🔹 HTTPS (HTTP Secure)  
- Secure version of HTTP.  
- Uses **SSL/TLS encryption** to protect data (confidentiality, integrity, authentication).  
- Default port **443**.  
- Prevents eavesdropping & man-in-the-middle attacks.  

---

## 📑 HTTP Methods  
- **GET** → Retrieve data (e.g., fetch webpage).  
- **POST** → Submit data (e.g., form submission).  
- **PUT** → Update/replace resource.  
- **PATCH** → Partially update resource.  
- **DELETE** → Remove resource.  
- **HEAD** → Retrieve headers only.  
- **OPTIONS** → Check available methods for a resource.  

---

# HTTP Status Codes

HTTP status codes are **three-digit codes returned by a server** to indicate the **result of a client’s request**.  
They are grouped into **five categories**.

---

## 1. Informational (1xx)
- **Meaning:** Request received, continuing process.  
- **Common Codes:**
  - **100 Continue** – Client should continue sending the request.  
  - **101 Switching Protocols** – Server agrees to change protocols (e.g., HTTP → WebSocket).  

---

## 2. Success (2xx)
- **Meaning:** Request successfully processed.  
- **Common Codes:**
  - **200 OK** – Request succeeded; response contains requested data.  
  - **201 Created** – Resource successfully created (e.g., POST).  
  - **202 Accepted** – Request accepted but not yet processed.  
  - **204 No Content** – Request succeeded but no content to return.  

---

## 3. Redirection (3xx)
- **Meaning:** Further action needed to complete the request.  
- **Common Codes:**
  - **301 Moved Permanently** – Resource permanently moved to a new URL.  
  - **302 Found** – Temporary redirect.  
  - **304 Not Modified** – Resource has not changed; use cached version.  

---

## 4. Client Errors (4xx)
- **Meaning:** Client made a bad request.  
- **Common Codes:**
  - **400 Bad Request** – Malformed syntax or invalid request.  
  - **401 Unauthorized** – Authentication required or failed.  
  - **403 Forbidden** – Server refuses to fulfill request.  
  - **404 Not Found** – Resource does not exist.  
  - **405 Method Not Allowed** – HTTP method not supported for resource.  
  - **429 Too Many Requests** – Client sent too many requests in a given time.  

---

## 5. Server Errors (5xx)
- **Meaning:** Server failed to fulfill a valid request.  
- **Common Codes:**
  - **500 Internal Server Error** – Generic server error.  
  - **501 Not Implemented** – Server does not support requested functionality.  
  - **502 Bad Gateway** – Invalid response from upstream server.  
  - **503 Service Unavailable** – Server overloaded or down for maintenance.  
  - **504 Gateway Timeout** – Upstream server did not respond in time.  

---

## Summary Table

| Code | Category          | Meaning                        |
|------|-----------------|--------------------------------|
| 100  | Informational    | Continue                       |
| 101  | Informational    | Switching Protocols            |
| 200  | Success          | OK                             |
| 201  | Success          | Created                        |
| 202  | Success          | Accepted                       |
| 204  | Success          | No Content                     |
| 301  | Redirection      | Moved Permanently              |
| 302  | Redirection      | Found                          |
| 304  | Redirection      | Not Modified                   |
| 400  | Client Error     | Bad Request                    |
| 401  | Client Error     | Unauthorized                   |
| 403  | Client Error     | Forbidden                      |
| 404  | Client Error     | Not Found                      |
| 405  | Client Error     | Method Not Allowed             |
| 429  | Client Error     | Too Many Requests              |
| 500  | Server Error     | Internal Server Error          |
| 501  | Server Error     | Not Implemented                |
| 502  | Server Error     | Bad Gateway                    |
| 503  | Server Error     | Service Unavailable            |
| 504  | Server Error     | Gateway Timeout                |


---

## 📬 HTTP Headers  
- Provide **metadata** about requests/responses.  
- **Common Request Headers**:  
  - `Host: example.com` → Target server.  
  - `User-Agent: Chrome/118` → Browser info.  
  - `Authorization: Bearer <token>` → Auth credentials.  
- **Common Response Headers**:  
  - `Content-Type: text/html` → Type of content.  
  - `Set-Cookie: sessionid=12345` → Store cookies.  
  - `Cache-Control: no-cache` → Caching rules.  

---

## 🔐 HTTPS and SSL/TLS  
- **SSL (Secure Sockets Layer)** → Original encryption protocol (deprecated).  
- **TLS (Transport Layer Security)** → Modern standard.  
- Provides:  
  - **Encryption** (data confidentiality)  
  - **Authentication** (server identity via digital certificates)  
  - **Integrity** (prevents data tampering)  
- Uses **Public Key Infrastructure (PKI)** with digital certificates (issued by Certificate Authorities like DigiCert, Let’s Encrypt).  

---

## 👉 In short  
- **HTTP = plain, insecure. HTTPS = encrypted with TLS.**  
- **Methods = CRUD operations.**  
- **Status codes = response meaning.**  
- **Headers = extra info.**  
- **SSL/TLS = backbone of secure web.**  
