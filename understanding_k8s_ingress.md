# Understanding Kubernetes Ingress

* **Kubernetes Ingress** - Kubernetes ingress is a collection of routing rules that govern how external users access services running in a Kubernetes cluster.

<img src="https://user-images.githubusercontent.com/1214953/101633900-f14a4000-3a4d-11eb-88d3-f541b9c89375.png" width="400" height="250" />

* **ClusterIP** - The default Kubernetes ServiceType is ClusterIp. ClusterIp exposesthe Service on a cluster-internal IP. To reach the ClusterIp from an external source, you can open a Kubernetes proxy between the external source and the cluster. You can use kubectl to create such a proxy. When the proxy is up, you’re directly connected to the cluster, and you can use the internal IP (ClusterIp) for thatService. This method isn’t suitable for a production environment, but it’s useful for development, debugging, and other quick-and-dirty operations.

<img src="https://user-images.githubusercontent.com/1214953/101642988-a8988400-3a59-11eb-99ba-f8a168a1d1e6.jpeg" width="300" height="400" />

* **NodePort** - NodePort exposes the Service on each Node’s IP at the NodePort (a fixed port for that Service, in the default range of 30000-32767). You can access the Service from outside the cluster by requesting <NodeIp>:<NodePort>. Every service you deploy as NodePort will be exposed in its own port, on every Node. It’s rather cumbersome to use NodePortfor Servicesthat are in production. As you are using non-standard ports, you often need to set-up an external load balancer that listens to the standard ports and redirects the traffic to the <NodeIp>:<NodePort>. Kubernetes transparently routes incoming traffic on the NodePort to your service, even if the application is running on a different node. 

<img src="https://user-images.githubusercontent.com/1214953/101643518-3bd1b980-3a5a-11eb-9c83-420770e789e5.jpeg" width="300" height="400" />

* **Load Balancer** -  LoadBalancer exposes it externally, using a cloud provider’s load balancer solution. The cloud provider will provision a load balancer for the Service, and map it to its automatically assigned NodePort. The traffic from that external load balancer is routed to the Service pods depends on the cluster provider. This load balancer routes external traffic to a Kubernetes service in cluster.

  The LoadBalancer is the best option for a production environment, with two caveats:
    - Every Service that you deploy as LoadBalancer will get it’s own IP.
    - The LoadBalancer is usually billed based on the number of exposed services, which can be expensive.
    
<img src="https://user-images.githubusercontent.com/1214953/101643971-aedb3000-3a5a-11eb-90cb-08aa872b4f53.jpeg" width="400" height="250" />

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
    
----
##### This notes is for learning purpose
**Referance**
- courtesy 
  - https://www.getambassador.io/learn/kubernetes-ingress/
  - https://www.ovh.com/blog/getting-external-traffic-into-kubernetes-clusterip-nodeport-loadbalancer-and-ingress/

- youtube 
  - https://www.youtube.com/watch?v=GhZi4DxaxxE
