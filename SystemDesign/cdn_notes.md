# Content Delivery Networks (CDNs)

## What is a CDN?
A **Content Delivery Network (CDN)** is a geographically distributed network of servers that deliver content (web pages, videos, images, scripts, etc.) to users based on their location.  
The goal is to improve **performance, availability, and scalability**.

---

## How CDNs Work
1. **Geographical distribution**: CDN servers (edge servers) are located in multiple regions worldwide.
2. **User request routing**: When a user requests content (e.g., a video or webpage), the request is routed to the **nearest CDN edge server** instead of the origin server.
3. **Content delivery**:
   - If cached, the edge server delivers content directly to the user.
   - If not cached, the CDN fetches it from the origin server, caches it, and serves the user.

**Example**: A user in India accesses a U.S.-hosted website. With a CDN, the content comes from an Indian edge server, reducing latency.

---

## Caching Strategies
CDNs rely heavily on **caching** to reduce load on the origin server.

- **Static caching**: Images, CSS, JavaScript, and videos are cached for faster delivery.
- **Dynamic caching**: Frequently accessed dynamic data can also be cached with rules.
- **Cache invalidation**: Allows purging or updating stale content.
- **Time-to-Live (TTL)**: Determines how long content is cached before re-validation.

**Example**:  
An image on a shopping site may have a TTL of 24 hours. After this, the CDN checks with the origin server for updates.

---

## Edge Computing in CDNs
CDNs are evolving into **edge computing platforms** where processing happens closer to the user.

- **Edge functions/workers**: Run custom code at the edge (e.g., Cloudflare Workers, AWS Lambda@Edge).
- **Use cases**:
  - Real-time personalization
  - API responses caching
  - Security (bot filtering, DDoS protection)
  - IoT and low-latency apps

**Example**: A video streaming platform can deliver localized ads by running personalization logic at the CDN edge, reducing latency.

---

## Benefits of CDNs
- Reduced latency and faster load times
- Lower bandwidth costs for origin servers
- Increased availability and reliability
- Enhanced security with DDoS protection and TLS termination
