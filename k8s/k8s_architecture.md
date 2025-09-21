# Kubernetes (K8s) Architecture

Kubernetes (K8s) is an open-source container orchestration platform that automates the deployment, scaling, and management of containerized applications. Its architecture is designed for high availability, scalability, and extensibility.

---

## 🔹 High-Level Components

Kubernetes architecture follows a **master-worker** (control plane – data plane) model.

### 1. Control Plane (Master Components)

Responsible for managing the overall cluster state and decisions.

* **API Server (kube-apiserver)**

  * Central management point.
  * Exposes Kubernetes API over REST.
  * All kubectl / UI / CLI interactions go through it.

* **etcd**

  * Distributed key-value store.
  * Stores cluster configuration, state, secrets, and metadata.
  * Acts as the single source of truth.

* **Controller Manager (kube-controller-manager)**

  * Runs controllers that regulate the state of the cluster.
  * Examples: Node controller, ReplicaSet controller, Job controller.

* **Scheduler (kube-scheduler)**

  * Assigns pods to nodes based on resource requirements and constraints.
  * Considers CPU, memory, affinity, taints/tolerations, and policies.

---

### 2. Node Components (Worker Nodes)

Each node runs the necessary services to host and manage pods.

* **Kubelet**

  * Agent running on each worker node.
  * Communicates with the API Server.
  * Ensures containers are running in pods.

* **Kube-proxy**

  * Handles cluster networking and services.
  * Provides load balancing and network proxying for pods.

* **Container Runtime**

  * Responsible for running containers.
  * Examples: containerd, CRI-O, Docker (deprecated).

---

## 🔹 Pod and Service Layer

* **Pod**: The smallest deployable unit in K8s, containing one or more containers with shared storage and networking.
* **ReplicaSet**: Ensures a specified number of pod replicas are running.
* **Deployment**: Higher-level abstraction that manages ReplicaSets and pod lifecycle.
* **Service**: Provides stable networking and load balancing for pods.
* **Ingress**: Manages external access (HTTP/HTTPS) to services.

---

## 🔹 Additional Components

* **ConfigMaps & Secrets**: Store configuration data and sensitive information.
* **Namespaces**: Logical partitions for multi-tenancy.
* **Volumes & Persistent Volumes (PV/PVC)**: For stateful workloads and storage persistence.
* **DaemonSets, StatefulSets, Jobs, CronJobs**: Special controllers for workload patterns.

---

## 🔹 Kubernetes Cluster Flow

1. User submits request via **kubectl / API**.
2. **API Server** authenticates and validates the request.
3. Request stored in **etcd**.
4. **Scheduler** assigns pod to a node.
5. **Kubelet** on node ensures containers are created via container runtime.
6. **Kube-proxy** manages networking for service discovery.
7. Controllers continuously reconcile desired vs actual state.

---

## 🔹 Diagram (Textual)

```
            +-----------------------+
            |   Control Plane       |
            |                       |
   +--------+  API Server           |
   |        |  etcd (DB)            |
   |        |  Scheduler            |
   |        |  Controller Manager   |
   |        +-----------------------+
   |
   |                (REST API)
   v
+-------------------------------+
|           Worker Nodes        |
|                               |
|  +---------+   +---------+    |
|  | Kubelet |   | Kube-proxy|  |
|  +----+----+   +----+-----+   |
|       |            |           |
|  +----v-----+  +---v------+    |
|  | Container|  | Pod(s)   |    |
|  | Runtime  |  |          |    |
|  +----------+  +----------+    |
+-------------------------------+
```

---

✅ In summary: Kubernetes architecture is based on a **control plane** for decision-making and **worker nodes** for running workloads, all connected through the Kubernetes API and backed by etcd as the source of truth.


![K8s Architectir](images/kubernetes-architecture.jpg)