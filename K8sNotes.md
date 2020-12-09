# K8s Notes

* **Kubernetes Ingress** - Kubernetes ingress is a collection of routing rules that govern how external users access services running in a Kubernetes cluster.

<img src="https://user-images.githubusercontent.com/1214953/101633900-f14a4000-3a4d-11eb-88d3-f541b9c89375.png" width="400" height="250" />

* **NodePort** - A NodePort is an open port on every node of a cluster. Kubernetes transparently routes incoming traffic on the NodePort to your service, even if the application is running on a different node.

* **Load Balancer** - Load Balancer service type automatically deploys an external load balancer. This external load balancer is associated with a specific IP address and routes external traffic to a Kubernetes service in cluster.

* **Ingress Controllers** - Kubernetes supports a high level abstraction called Ingress, which allows simple host or URL based HTTP routing. An ingress controller is responsible for reading the Ingress Resource information and processing that data accordingly

## Ingress Controller/Edge Proxy/L7 proxy

**Ingress Controller** - A Proxy Powers the Ingress Controller and can assume that Ingress controller is a car and proxy is a engine

<img src="https://user-images.githubusercontent.com/1214953/101635933-a3830700-3a50-11eb-813c-b8bdf428c6b9.png" width="400" height="250" />

### When an Ingress Controller Isn't Enough 
  - The cloud presents new challenges for cloud native applications. Whether you are building a greenfield app or migrating a legacy app, your cloud native application will have many more microservices at the edge. These microservices will typically be managed by different teams and therefore have diverse requirements. Envoy Proxy and Ambassador were created to address these exact challenges.
  - The Kubernetes documentation and many other resources will recommend a simple ingress controller to get you started getting traffic into your Kubernetes cluster. However, as your application becomes more advanced there is a lot of additional functionality you will expect from your ingress controller including: 

**Security**
  - Transport Layer Security (TLS) Termination
  - Authentication
  - Rate Limiting
  - WAF Integration

**Availability**
  - Different Load Balancing Algorithms
  - Circuit Breakers
  - Timeouts
  - Automatic Retries

**Progressive Delivery**
  - Canary Releases
  - Traffic Shadowing

**Self-Service Configuration**
  - Declarative Configuration
  - Advanced Policy Specification
  - GitOps-Friendly

### Ingress Controllers

1)  **NGINX-Powered Ingress Controllers**
    - NGINX Ingress Controller
    - Kong Ingress Controller
2)  **HAProxy-Powered Ingress Controllers**
    - HAProxy Ingress
3)  **Envoy-Powered Ingress Controllers**
    - Ambassador Edge Stack
    - Istio Gateway
4)  **Custom Proxy Ingress Controllers**
    - Traefik
    - Skipper
5)  **Other Ingress Controllers**
    - AWS ALB Ingress Controller
    - Citrix Ingress Controller
    
