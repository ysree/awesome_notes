# Microservices Testing Patterns

## Testing Pyramid for Microservices

```
        /\
       /  \      E2E Tests (10%)
      /____\     
     /      \    Integration Tests (30%)
    /________\   
   /          \  Unit Tests (60%)
  /__________  \
```

## 1. Unit Testing Pattern

**Purpose**: Test individual components in isolation

**Key Characteristics**:
- Fast execution (milliseconds)
- No external dependencies
- Mock all collaborators
- High code coverage target (70-80%)

**Example Scenario**:
```
Service: OrderService
Test: validateOrder()
Mocks: Database, PaymentGateway, InventoryService
Focus: Business logic validation
```

**Best Practices**:
- Use test doubles (mocks, stubs, fakes)
- Test one logical unit at a time
- Follow AAA pattern (Arrange, Act, Assert)
- Keep tests independent and deterministic

---

## 2. Component Testing Pattern

**Purpose**: Test a single microservice in isolation with all its layers

**Key Characteristics**:
- Tests entire service boundary
- Real internal components
- Mock external dependencies
- In-memory databases or test containers

**Example Scenario**:
```
Service: User Service (API → Business Logic → Database)
External Mocks: Auth Service, Email Service
Real Components: Controllers, Services, Repositories
Database: In-memory H2 or Test Container
```

**Implementation Approach**:
```
1. Start service with test configuration
2. Mock external HTTP calls (WireMock, MockServer)
3. Use embedded/containerized database
4. Send HTTP requests to service
5. Verify responses and side effects
```

---

## 3. Contract Testing Pattern

**Purpose**: Verify service interactions without needing all services running

**Key Frameworks**: Pact, Spring Cloud Contract, Postman

**Consumer-Driven Contracts**:
```
Producer: Payment Service (API provider)
Consumer: Order Service (API caller)

Process:
1. Consumer defines expected API behavior (contract)
2. Contract is versioned and shared
3. Producer validates against contract
4. Both sides test independently
```

**Benefits**:
- Catch breaking changes early
- Fast feedback loop
- Independent team development
- No need for integrated test environments

**Example Contract**:
```
Consumer: Order Service expects
  POST /payments
  Request: { amount: 100, currency: "USD" }
  Response: 201 Created { transactionId: "xyz" }

Producer: Payment Service must honor this contract
```

---

## 4. Integration Testing Pattern

**Purpose**: Test interactions between multiple services

**Subcategories**:

### A. Service-to-Service Integration
- Test communication between 2-3 services
- Use real services or test containers
- Verify message formats and protocols

### B. Database Integration
- Test data persistence and retrieval
- Verify migrations and schema changes
- Test transaction boundaries

### C. Message Queue Integration
- Test async communication (Kafka, RabbitMQ)
- Verify message production and consumption
- Test error handling and retries

**Example Scenario**:
```
Order Service → Inventory Service → Database
Test: Place order, verify inventory decremented
Environment: Docker Compose with both services
```

---

## 5. End-to-End (E2E) Testing Pattern

**Purpose**: Test complete user journeys across all services

**Key Characteristics**:
- Full environment deployment
- Real user scenarios
- Slow execution (minutes)
- Brittle and expensive to maintain
- Keep minimal (critical paths only)

**Example Scenarios**:
```
1. User Registration → Email Verification → Login
2. Browse Products → Add to Cart → Checkout → Payment
3. Create Order → Process Payment → Update Inventory → Send Notification
```

**Best Practices**:
- Test only critical business flows
- Use production-like environments
- Implement retry mechanisms
- Parallel execution where possible
- Clear test data management strategy

---

## 6. Consumer-Driven Contract Testing (Detailed)

**The Pact Workflow**:

```
Step 1: Consumer writes test with expected interactions
  Test: "Order service can create payment"
  Expected: POST /payments returns 201

Step 2: Generate contract file (pact)
  Output: order-service-payment-service.json

Step 3: Share contract with provider
  Store in Pact Broker or Git

Step 4: Provider verifies contract
  Replays requests from contract
  Validates actual responses match expectations

Step 5: CI/CD Integration
  Block deployments if contracts break
```

---

## 7. Chaos Testing Pattern

**Purpose**: Test resilience and failure scenarios

**Testing Scenarios**:
- Network latency injection
- Service instance failures
- Database connection drops
- Partial network partitions
- Resource exhaustion (CPU, memory)

**Tools**: Chaos Monkey, Gremlin, LitmusChaos

**Example Tests**:
```
1. Kill random service instance → Verify traffic reroutes
2. Inject 5-second delay → Verify timeouts and retries
3. Fill disk space → Verify graceful degradation
4. Simulate zone failure → Verify multi-region failover
```

---

## 8. Performance Testing Patterns

### A. Load Testing
- Simulate expected user load
- Verify SLAs and response times
- Identify bottlenecks

### B. Stress Testing
- Push system beyond normal capacity
- Find breaking points
- Test recovery mechanisms

### C. Spike Testing
- Sudden traffic increases
- Verify auto-scaling
- Test rate limiting

**Tools**: JMeter, Gatling, k6, Locust

---

## 9. Smoke Testing Pattern

**Purpose**: Quick validation after deployment

**Characteristics**:
- Run after every deployment
- Test critical paths only
- Fast execution (< 5 minutes)
- Early warning system

**Example Checks**:
```
✓ All services are healthy
✓ Database connections work
✓ External APIs are reachable
✓ Key endpoints return 200
✓ Message queues are processing
```

---

## 10. Test Data Management Patterns

### A. Test Data Builder Pattern
```
Create reusable builders for test data
orderBuilder()
  .withCustomer(customerId)
  .withItems(items)
  .withStatus("PENDING")
  .build()
```

### B. Database Seeding
- Populate databases with known data
- Use migrations for consistency
- Teardown after tests

### C. Test Isolation
- Each test uses unique data
- Clean state before/after tests
- Avoid test interdependencies

---

## Testing Strategy Recommendations

### For Each Service:
```
Unit Tests:           1000+ tests (fast, focused)
Component Tests:      50-100 tests (service boundary)
Contract Tests:       20-30 tests (per integration)
Integration Tests:    10-20 tests (key flows)
E2E Tests:            5-10 tests (critical paths only)
```

### CI/CD Pipeline:
```
1. Commit → Unit Tests (instant feedback)
2. Pull Request → Unit + Component Tests
3. Merge → Unit + Component + Contract Tests
4. Deploy to Test → Integration Tests
5. Deploy to Staging → E2E + Smoke Tests
6. Deploy to Production → Smoke Tests + Monitoring
```

### Test Environment Strategy:
```
Development:     Mocks + In-memory DBs
Component Test:  Docker Compose
Integration:     Kubernetes Test Cluster
Staging:         Production-like environment
Production:      Monitoring + Canary deployments
```

---

## Anti-Patterns to Avoid

1. **Over-reliance on E2E tests** - Slow, brittle, expensive
2. **Testing through the UI** - Use API tests instead
3. **Shared test environments** - Causes flakiness
4. **No contract testing** - Integration surprises
5. **Ignoring non-functional tests** - Performance, security
6. **Test data coupling** - Tests fail randomly
7. **Manual testing for regressions** - Should be automated

---

## Key Principles

✓ **Test at the appropriate level** - Unit tests for logic, integration for contracts

✓ **Fail fast** - Run fastest tests first in CI/CD

✓ **Independence** - Tests should not depend on each other

✓ **Repeatability** - Same test should always produce same result

✓ **Maintainability** - Tests are code; keep them clean

✓ **Realistic environments** - Test environments mirror production

✓ **Monitoring as testing** - Production observability catches what tests miss