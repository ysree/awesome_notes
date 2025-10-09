# Table of Contents
- [System Design Key Categories](#system-design-key-categories)
- [Scalability Terms](#scalability-terms)
- [Availability & Reliability](#availability--reliability)
- [Performance Terms](#performance-terms)
- [Consistency & Data Terms](#consistency--data-terms)
- [Architecture Patterns](#architecture-patterns)
- [Load Balancing](#load-balancing)
- [Caching](#caching)
- [Database Terms](#database-terms)
- [Security Terms](#security-terms)
- [Messaging & Communication](#messaging--communication)
- [Monitoring & Observability](#monitoring--observability)
- [Data Processing](#data-processing)
- [Networking](#networking)
- [Deployment & DevOps](#deployment--devops)

# System Design Key Categories 
- **Scalability**: Horizontal/vertical scaling, auto-scaling concepts
- **Availability**: High availability, fault tolerance, disaster recovery
- **Performance**: Latency, throughput, response time metrics
- **Data Consistency**: ACID, CAP theorem, eventual consistency
- **Architecture Patterns**: Microservices, monolithic, event-driven
- **Infrastructure**: Load balancing, caching, databases
- **Security**: Authentication, authorization, rate limiting
- **Communication**: Message queues, APIs, pub-sub patterns
- **Operations**: Monitoring, deployment strategies, DevOps practices
- **Throughput**: Number of requests processed per unit of time.
- **Latency**: Time taken to process a request and return a response.
- **Fault Tolerance**: Ability of a system to continue operating despite failures.
- **Durability**: Once data is stored, it remains intact even after failures.


## Scalability Terms

### Horizontal Scaling (Scale Out)
- **Definition**: Adding more servers/instances to handle increased load
- **Example**: Adding more web servers behind a load balancer
- **Pros**: Better fault tolerance, theoretically unlimited scaling
- **Cons**: More complex, network overhead

### Vertical Scaling (Scale Up)
- **Definition**: Adding more power (CPU, RAM) to existing servers
- **Example**: Upgrading server from 8GB to 32GB RAM
- **Pros**: Simpler to implement, no architecture changes
- **Cons**: Hardware limits, single point of failure

### Auto-scaling
- **Definition**: Automatically adjusting resources based on demand
- **Triggers**: CPU utilization, request count, queue length
- **Types**: Reactive (after load increases) vs Predictive (based on patterns)

## Availability & Reliability

### High Availability (HA)
- **Definition**: System remains operational most of the time
- **Measurement**: Uptime percentage (99.9% = 8.76 hours downtime/year)
- **Techniques**: Redundancy, failover, load balancing

### Fault Tolerance
- **Definition**: System continues operating despite component failures
- **Implementation**: Redundant components, graceful degradation
- **Example**: RAID storage, multiple data centers

### Disaster Recovery (DR)
- **Definition**: Process of restoring systems after major failures
- **Metrics**: RTO (Recovery Time Objective), RPO (Recovery Point Objective)
- **Strategies**: Hot/warm/cold standby sites

### Circuit Breaker
- **Definition**: Prevents cascading failures by stopping calls to failing services
- **States**: Closed (normal), Open (failure), Half-Open (testing recovery)
- **Use Case**: Microservices calling external APIs

## Performance Terms

### Latency
- **Definition**: Time taken to process a single request
- **Measurement**: Milliseconds, response time
- **Types**: Network latency, processing latency, database latency

### Throughput
- **Definition**: Number of requests processed per unit time
- **Measurement**: Requests per second (RPS), transactions per second (TPS)
- **Relationship**: Often inverse relationship with latency

### Response Time
- **Definition**: Total time from request initiation to response completion
- **Components**: Network time + processing time + queue time
- **Percentiles**: P50, P95, P99 (50th, 95th, 99th percentile)

### Quality of Service (QoS)
- **Definition**: Performance guarantees for different types of traffic
- **Parameters**: Bandwidth, latency, jitter, packet loss
- **Implementation**: Traffic prioritization, resource reservation

## Consistency & Data Terms

### ACID Properties
- **Atomicity**: All operations in transaction succeed or all fail
- **Consistency**: Data remains valid according to rules
- **Isolation**: Concurrent transactions don't interfere
- **Durability**: Committed transactions survive system failures

### CAP Theorem
- **Consistency**: All nodes see the same data simultaneously
- **Availability**: System remains operational
- **Partition Tolerance**: System continues despite network failures
- **Rule**: Can only guarantee 2 out of 3 properties

### Eventual Consistency
- **Definition**: System will become consistent over time
- **Use Cases**: Distributed databases, DNS, social media feeds
- **Trade-off**: Immediate consistency for availability and performance

### Strong Consistency
- **Definition**: All reads receive the most recent write immediately
- **Implementation**: Synchronous replication, distributed locks
- **Cost**: Higher latency, reduced availability

### BASE Properties
- **Basically Available**: System remains available
- **Soft State**: Data may change over time
- **Eventual Consistency**: System becomes consistent eventually
- **Alternative**: To ACID for distributed systems

## Architecture Patterns

### Microservices
- **Definition**: Application as suite of small, independent services
- **Benefits**: Independent deployment, technology diversity, fault isolation
- **Challenges**: Network complexity, data consistency, debugging

### Monolithic Architecture
- **Definition**: Single deployable unit containing all functionality
- **Benefits**: Simple to develop, test, and deploy initially
- **Challenges**: Scaling bottlenecks, technology lock-in

### Service-Oriented Architecture (SOA)
- **Definition**: Services communicate through well-defined interfaces
- **Components**: Service provider, consumer, registry
- **Protocols**: SOAP, REST, messaging

### Event-Driven Architecture
- **Definition**: Components communicate through events
- **Components**: Event producers, consumers, event bus/broker
- **Benefits**: Loose coupling, scalability, real-time processing

### Serverless Architecture
- **Definition**: Code runs in stateless compute containers
- **Benefits**: No server management, automatic scaling, pay-per-use
- **Challenges**: Cold starts, vendor lock-in, debugging

## Load Balancing

### Load Balancer Types
- **Layer 4 (Transport)**: Routes based on IP and port
- **Layer 7 (Application)**: Routes based on content (HTTP headers, URLs)
- **Hardware vs Software**: Dedicated appliances vs software solutions

### Load Balancing Algorithms
- **Round Robin**: Requests distributed evenly in sequence
- **Weighted Round Robin**: Servers receive requests based on weight
- **Least Connections**: Route to server with fewest active connections
- **IP Hash**: Route based on client IP hash
- **Geographic**: Route based on client location

### Health Checks
- **Definition**: Monitoring server health to route traffic appropriately
- **Types**: HTTP checks, TCP checks, custom application checks
- **Actions**: Remove unhealthy servers from rotation

## Caching

### Cache Levels
- **Browser Cache**: Client-side caching in web browsers
- **CDN (Content Delivery Network)**: Geographic distribution of cached content
- **Reverse Proxy**: Cache between clients and servers
- **Application Cache**: In-memory caching within applications
- **Database Cache**: Query result caching

### Cache Patterns
- **Cache-Aside**: Application manages cache manually
- **Cache-On-Read**: Read from cache first, then database if not found
- **Write-Through**: Write to cache and database simultaneously
- **Write-Behind/Write-Back**: Write to cache first, database later
- **Refresh-Ahead**: Proactively refresh cache before expiration

### Cache Invalidation
- **TTL (Time To Live)**: Cache entries expire after set time
- **Cache Stampede**: Multiple requests for same expired data
- **Cache Warming**: Pre-loading cache with expected data

## Database Terms

### OLTP vs OLAP
- **OLTP (Online Transaction Processing)**: Real-time transaction processing
- **OLAP (Online Analytical Processing)**: Complex queries for analytics
- **Differences**: OLTP optimized for writes, OLAP for reads

### Database Sharding
- **Definition**: Horizontal partitioning of database across multiple servers
- **Strategies**: Range-based, hash-based, directory-based
- **Challenges**: Cross-shard queries, rebalancing

### Database Replication
- **Master-Slave**: One write node, multiple read replicas
- **Master-Master**: Multiple nodes accept writes
- **Synchronous vs Asynchronous**: Trade-off between consistency and performance

### Database Partitioning
- **Definition**: Horizontal partitioning of database across multiple servers
- **Strategies**: Range-based, hash-based, directory-based
- **Challenges**: Cross-partition queries, rebalancing

### Database Clustering
- **Definition**: Vertical partitioning of database across multiple servers
- **Strategies**: Range-based, hash-based, directory-based
- **Challenges**: Cross-cluster queries, rebalancing

### NoSQL Database Types
- **Document**: MongoDB, CouchDB (JSON-like documents)
- **Key-Value**: Redis, DynamoDB (simple key-value pairs)
- **Column-Family**: Cassandra, HBase (wide column stores)
- **Graph**: Neo4j, Amazon Neptune (nodes and relationships)

## Security Terms

### Authentication vs Authorization
- **Authentication**: Verifying user identity ("who are you?")
- **Authorization**: Determining user permissions ("what can you do?")
- **Implementation**: Login systems vs access control lists

### OAuth 2.0
- **Definition**: Authorization framework for third-party access
- **Flow**: Authorization code, implicit, client credentials, password
- **Components**: Client, authorization server, resource server

### JWT (JSON Web Tokens)
- **Definition**: Self-contained tokens for secure information transmission
- **Structure**: Header.Payload.Signature
- **Use Cases**: API authentication, information exchange

### Rate Limiting
- **Definition**: Controlling request frequency from clients
- **Algorithms**: Token bucket, leaky bucket, fixed window, sliding window
- **Purpose**: Prevent abuse, ensure fair usage, protect resources

## Messaging & Communication

### Message Queues
- **Definition**: Asynchronous communication through message storage
- **Benefits**: Decoupling, reliability, scalability
- **Examples**: RabbitMQ, Apache Kafka, AWS SQS

### Publish-Subscribe Pattern
- **Definition**: Publishers send messages to topics, subscribers receive them
- **Benefits**: Loose coupling, multiple consumers
- **Use Cases**: Event notifications, data streaming

### Message Broker
- **Definition**: Intermediary program that routes messages between systems
- **Features**: Message routing, transformation, persistence
- **Examples**: Apache Kafka, Apache ActiveMQ, Redis Pub/Sub

### API Gateway
- **Definition**: Entry point for all client requests to microservices
- **Features**: Request routing, authentication, rate limiting, monitoring
- **Benefits**: Centralized cross-cutting concerns, protocol translation

## Monitoring & Observability

### Metrics
- **Definition**: Numerical measurements of system behavior over time
- **Types**: Business metrics, application metrics, infrastructure metrics
- **Examples**: Response time, error rate, CPU utilization

### Logging
- **Definition**: Recording events and messages for debugging and audit
- **Levels**: ERROR, WARN, INFO, DEBUG, TRACE
- **Structured Logging**: JSON format for better parsing and analysis

### Tracing
- **Definition**: Tracking requests across multiple services
- **Distributed Tracing**: Following requests through microservices
- **Tools**: Jaeger, Zipkin, AWS X-Ray

### SLI, SLO, SLA
- **SLI (Service Level Indicator)**: Quantitative measure of service level
- **SLO (Service Level Objective)**: Target value for SLI
- **SLA (Service Level Agreement)**: Contractual agreement with consequences

## Data Processing

### Batch Processing
- **Definition**: Processing large volumes of data in scheduled batches
- **Characteristics**: High throughput, high latency, scheduled execution
- **Examples**: ETL jobs, financial reconciliation, log analysis

### Stream Processing
- **Definition**: Real-time processing of continuous data streams
- **Characteristics**: Low latency, continuous processing, event-driven
- **Examples**: Real-time analytics, fraud detection, monitoring alerts

### ETL vs ELT
- **ETL (Extract, Transform, Load)**: Transform data before loading
- **ELT (Extract, Load, Transform)**: Load raw data then transform
- **Trade-offs**: Processing location, storage requirements, flexibility

### Data Lake vs Data Warehouse
- **Data Lake**: Store raw data in native format (structured/unstructured)
- **Data Warehouse**: Store processed, structured data optimized for queries
- **Use Cases**: Data lake for exploration, warehouse for reporting

## Networking

### CDN (Content Delivery Network)
- **Definition**: Geographically distributed servers for content delivery
- **Benefits**: Reduced latency, decreased server load, improved availability
- **Use Cases**: Static content, video streaming, software distribution

### DNS (Domain Name System)
- **Definition**: Translates domain names to IP addresses
- **Types**: A records, CNAME, MX, TXT records
- **Strategies**: DNS load balancing, geographic routing

### Proxy vs Reverse Proxy
- **Proxy (Forward Proxy)**: Client-side intermediary for outbound requests
- **Reverse Proxy**: Server-side intermediary for inbound requests
- **Use Cases**: Caching, load balancing, SSL termination

## Deployment & DevOps
### Rolling Update
- **Definition**: Gradually replace old pods with new ones
- **Benefits**: Zero downtime, easy rollback, reduced risk
- **Process**: Deploy to subset, monitor metrics, gradually increase

### Recreate Deployment
- **Definition**: Kill all old pods before starting new ones
- **Benefits**: No downtime, but less availability during update
- **Process**: Deploy to subset, monitor metrics, gradually increase

### Blue-Green Deployment
- **Definition**: Two identical production environments, switch traffic between them
- **Benefits**: Zero downtime, easy rollback, reduced risk
- **Process**: Deploy to idle environment, test, switch traffic

### Canary Deployment
- **Definition**: Gradual rollout to small percentage of users first
- **Benefits**: Risk mitigation, real-world testing, gradual confidence building
- **Process**: Deploy to subset, monitor metrics, gradually increase

### Feature Flags/Toggles
- **Definition**: Conditional code execution based on configuration
- **Benefits**: Decouple deployment from release, A/B testing, emergency rollback
- **Types**: Release toggles, experiment toggles, ops toggles

### Container Orchestration
- **Definition**: Automated deployment, scaling, and management of containers
- **Examples**: Kubernetes, Docker Swarm, Apache Mesos
- **Features**: Service discovery, load balancing, auto-scaling, health management