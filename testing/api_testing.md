# 🔍 API Testing Guide (With Detailed Examples)

## 🧩 Overview
API testing ensures that back-end services function correctly, efficiently, and securely — without relying on a user interface.  
It validates **request/response behavior**, **data integrity**, **error handling**, and **inter-service communication**.

---

## ⚙️ Why API Testing Matters
- Detects integration bugs before UI layers are built.  
- Ensures **scalability**, **security**, and **performance** of back-end systems.  
- Speeds up automation by avoiding fragile UI-based tests.  
- Guarantees **consistency across microservices** and releases.

---

## 🧪 Types of API Testing (with Examples)

### 1. **Functional Testing**
Validates API endpoints against expected input-output behavior.

**Examples:**
- `POST /login` → Valid credentials return `200 OK` and a token.
- `GET /user/{id}` → Returns correct user details from the database.
- `POST /cart` → Adds an item to cart and increases total price.
- `DELETE /user/{id}` → Returns `204 No Content` after deletion.
- `GET /orders` → Must return only orders belonging to the authenticated user.

---

### 2. **Integration Testing**
Checks how different APIs or services interact.

**Examples:**
- Creating a user in `UserService` should automatically create a wallet in `PaymentService`.
- Updating product inventory via `InventoryService` should reflect in `OrderService` availability.
- Payment failure in `PaymentGateway` should roll back order creation in `OrderService`.
- API Gateway correctly routes requests to appropriate microservices.
- Webhook callbacks from a third-party API (e.g., Stripe) update internal order status.

---

### 3. **Contract Testing**
Ensures request/response formats match agreed schemas between producer and consumer services.

**Examples:**
- Validate that all `GET /customer` responses include fields: `id`, `name`, `email`, `status`.
- JSON field `totalAmount` must be a float — not a string.
- Removing a deprecated field doesn’t break consumer services.
- Response status codes must conform to OpenAPI/Swagger specs.
- Verify Pact contract files between `OrderService` (consumer) and `InventoryService` (provider).

---

### 4. **Load & Performance Testing**
Assesses how APIs behave under varying levels of stress and concurrency.

**Examples:**
- Simulate 10K parallel `GET /search` requests to test throughput.
- Measure average response time for 500 concurrent checkout requests.
- Validate API latency remains < 200ms under moderate load.
- Identify bottlenecks using Locust or JMeter when scaling horizontally.
- Validate system auto-scales correctly when requests exceed threshold.

---

### 5. **Security Testing**
Ensures APIs are protected from unauthorized access, data leaks, and vulnerabilities.

**Examples:**
- Send requests without tokens → Expect `401 Unauthorized`.
- Attempt SQL injection via query params → Must be rejected.
- Verify expired JWT tokens are not accepted.
- Test user `A` cannot access data belonging to user `B`.
- Check HTTPS-only access and secure header policies (`Strict-Transport-Security`, `X-Content-Type-Options`).
- Test rate-limiting and brute-force prevention on login endpoints.
- Validate sensitive data (passwords, tokens) are masked in logs.

---

### 6. **Negative Testing**
Tests invalid, missing, or malformed inputs to verify error handling.

**Examples:**
- Send malformed JSON body → Expect `400 Bad Request`.
- Omit required field `email` in `POST /register` → Expect validation error.
- Pass non-existent `userId` → Expect `404 Not Found`.
- Provide invalid date format → Expect `422 Unprocessable Entity`.
- Submit request exceeding max payload size → Expect `413 Payload Too Large`.
- Send unsupported HTTP method like `PUT` on a read-only endpoint → Expect `405 Method Not Allowed`.

---

### 7. **Validation Testing**
Ensures correctness of data types, schema, and business logic rules.

**Examples:**
- Field `age` must always be numeric and non-negative.
- Validate timestamps are ISO 8601 formatted (`YYYY-MM-DDThh:mm:ssZ`).
- Ensure list responses are sorted by default.
- Validate response arrays contain unique IDs.
- Schema must exactly match OpenAPI/Swagger spec (using JSON schema validator).

---

### 8. **Regression Testing**
Verifies that recent code changes didn’t break existing API behavior.

**Examples:**
- Re-run test suite after changing API authentication logic.
- Verify all endpoints still return the same JSON structure post-upgrade.
- Ensure existing clients still function after API version bump (`v1` → `v2`).
- Compare historical response snapshots to detect unintended changes.
- Validate new optional fields don’t affect existing mandatory responses.

---

## 🧰 Common Tools for API Testing

| Tool | Description | Use Case |
|------|--------------|----------|
| **Postman** | GUI tool for designing and testing REST APIs | Manual + exploratory testing |
| **Newman** | CLI runner for Postman | CI/CD automation |
| **REST Assured** | Java library for REST API testing | Automated test frameworks |
| **Karate DSL** | BDD testing for REST/SOAP APIs | End-to-end test automation |
| **SoapUI** | Enterprise tool for SOAP and REST | Integration testing |
| **JMeter / K6 / Locust** | Load testing tools | Performance validation |
| **Pact** | Consumer-driven contract testing | Microservices validation |
| **HTTPie / Curl** | CLI-based testing | Quick functional tests |

---

## 🧠 Key Test Scenarios

| Scenario | Example |
|-----------|----------|
| **Authentication** | Test login/logout, token expiry, role-based access. |
| **CRUD Operations** | Verify Create, Read, Update, Delete behavior for entities. |
| **Pagination & Filtering** | Validate API supports `page`, `limit`, and `sort` parameters. |
| **Rate Limiting** | Exceed allowed API call rate and check throttling. |
| **Error Codes** | Validate correct HTTP status codes and error messages. |
| **Header Validation** | Ensure required headers like `Authorization` and `Content-Type` are present. |
| **Dependency Simulation** | Use mock servers when a service dependency is unavailable. |

---

## 🚀 Example CI/CD API Test Flow

1. Developers push code to repo.  
2. CI triggers automated API tests (Postman/Newman or REST Assured).  
3. Failures block merge or deployment.  
4. Reports generated in HTML/JUnit format.  
5. Scheduled nightly runs validate staging environment.

---

## 📊 Example Postman Command
```bash
newman run collection.json -e staging_env.json --reporters cli,html --reporter-html-export report.html
