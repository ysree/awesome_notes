# Understanding K8s ServiceType, Ingress & Ingress Controller

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
    
<img src="https://user-images.githubusercontent.com/1214953/101643971-aedb3000-3a5a-11eb-90cb-08aa872b4f53.jpeg" width="250" height="400" />

* **Ingress** - According to the official documentation, an Ingress is an API object that manages external access to the services in a cluster (typically HTTP). So what’s the difference between this and LoadBalancer or NodePort?

  Ingress isn’t a type of Service, but rather an object that acts as a reverse proxy and single entry-point to your cluster that routes the request to different services. The most basic Ingress is the NGINX Ingress Controller, where the NGINX takes on the role of reverse proxy, while also functioning as SSL.
  
<img src="https://user-images.githubusercontent.com/1214953/101645355-4b520200-3a5c-11eb-9c40-31ce73a26c27.png" width="250" height="400" />

  Ingress is exposed to the outside of the cluster via ClusterIP and Kubernetes proxy, NodePort, or LoadBalancer, and routes incoming traffic according to the configured rules.
  
<img src="https://user-images.githubusercontent.com/1214953/101645589-8d7b4380-3a5c-11eb-8d1e-d9bf479dd00a.png" width="250" height="400" />
  
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
## Which one should I use?
  Well, that’s the one million dollar question, and one which will probably elicit a different response depending on who you ask!

  You could go 100% LoadBalancer, getting an individual LoadBalancer for each service. Conceptually, it’s simple: every service is independent, with no extra configuration needed. The downside is the price (you will be paying for one LoadBalancer per service), and also the difficulty of managing lots of different IPs.

  You could also use only one LoadBalancer and an Ingress behind it. All your services would be under the same IP, each one in a different path. It’s a cheaper approach, as you only pay for one LoadBalancer, but if your services don’t have a logical relationship, it can quickly become chaotic.

  If you want my personal opinion, I would try to use a combination of the two…

  An approach I like is having a LoadBalancer for every related set of services, and then routing to those services using an Ingressbehind the  LoadBalancer. For example, let’s say you have two different microservice-based APIs, each one with around 10 services. I would put one LoadBalancer in front of one Ingress for each API, the LoadBalancerbeing the single public entry-point, and theIngress routing traffic to the API’s different services.

  But if your architecture is quite complex (especially if you’re using microservices), you will soon find that manually managing everything with LoadBalancer and Ingress is  rather  cumbersome. If that’s the case, the answer could be to delegate those tasks to a service mesh…
  
#### What’s a service mesh?
  You may have heard of Istio or Linkerd, and how they make it easier to build microservice architectures on Kubernetes, adding nifty perks like A/B testing, canary releases, rate limiting, access control, and end-to-end authentication.

  Istio, Linkerd, and similar tools are service meshes, which allow you to build networks of microservices and define their interactions, while simultaneously adding some high-value features that make the setup and operation of microservice-based architectures easier.

  There’s a lot to talk about when it comes to using service meshes on Kubernetes, but as they say, that’s a story for another time…

----
##### This notes is for learning purpose
**Referance**
- courtesy 
  - https://www.getambassador.io/learn/kubernetes-ingress/
  - https://www.ovh.com/blog/getting-external-traffic-into-kubernetes-clusterip-nodeport-loadbalancer-and-ingress/

- youtube 
  - https://www.youtube.com/watch?v=GhZi4DxaxxE
