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

## 📊 Status Codes (Common)  
- **1xx Informational** → Request received (e.g., 100 Continue).  
- **2xx Success** → Request successful.  
  - 200 OK → Success  
  - 201 Created → Resource created  
- **3xx Redirection** → Further action needed.  
  - 301 Moved Permanently  
  - 302 Found (temporary redirect)  
- **4xx Client Error** → Issue with request.  
  - 400 Bad Request  
  - 401 Unauthorized  
  - 403 Forbidden  
  - 404 Not Found  
- **5xx Server Error** → Server failed to process request.  
  - 500 Internal Server Error  
  - 502 Bad Gateway  
  - 503 Service Unavailable  

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
