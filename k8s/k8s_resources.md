# Kubernetes Resources Overview

## 1. Workload Resources (Run applications)
- **Pod** → Smallest deployable unit, runs one or more containers.  
- **ReplicaSet** → Ensures a fixed number of pod replicas are running.  
- **Deployment** → Declarative updates for pods/replica sets (most common).  
- **StatefulSet** → Manages stateful apps (stable network ID, storage).  
- **DaemonSet** → Ensures one pod per node (e.g., logging, monitoring agents).  
- **Job** → Run a pod until completion (batch processing).  
- **CronJob** → Run pods on a schedule (like cron).  

---

## 2. Service & Networking Resources
- **Service** → Expose a set of pods (types: ClusterIP, NodePort, LoadBalancer, ExternalName).  
- **Endpoints** → Internal IPs of pods backing a Service.  
- **Ingress** → HTTP/HTTPS routing from outside to services.  
- **NetworkPolicy** → Control network traffic between pods/namespaces.  

---

## 3. Configuration & Secrets
- **ConfigMap** → Store non-sensitive configuration as key/value (e.g., app configs).  
- **Secret** → Store sensitive data (passwords, tokens, certs).  
- **ResourceQuota** → Limit resource usage in a namespace.  
- **LimitRange** → Default/min/max for CPU and memory requests/limits.  
- **HPA (HorizontalPodAutoscaler)** → Auto-scale pods based on CPU/memory/custom metrics.  
- **VPA (VerticalPodAutoscaler)** → Auto-adjust pod resource requests/limits.  
- **PDB (PodDisruptionBudget)** → Limits voluntary disruptions during maintenance.  

---

## 4. Storage Resources
- **PersistentVolume (PV)** → Actual storage (EBS, NFS, GCE PD, etc.).  
- **PersistentVolumeClaim (PVC)** → Request for PV by a pod.  
- **StorageClass** → Defines how storage is dynamically provisioned.  
- **Volume** → Storage attached to a pod (configmap, secret, PVC, emptyDir, hostPath, etc.).  

---

## 5. Cluster & Node Resources
- **Namespace** → Virtual cluster within a cluster (multi-tenancy).  
- **Node** → Worker machine in the cluster.  
- **Resource types**: `cpu`, `memory`, `ephemeral-storage`, `hugepages`.  
- **ClusterRole / Role** → Define permissions.  
- **ClusterRoleBinding / RoleBinding** → Bind users/service accounts to roles.  
- **ServiceAccount** → Identity for processes inside pods.  
- **PriorityClass** → Define pod scheduling priority.  

---

## 6. Observability & Policy
- **Event** → Object that records what happened in the cluster.  
- **Lease** → Coordination and leader election.  
- **PodSecurityPolicy (deprecated, replaced by Pod Security Admission)**.  
- **CustomResourceDefinition (CRD)** → Extend Kubernetes API with custom resources.  
- **Operator** → Controller + CRD that automates app lifecycle.  

---

## Command Cheatsheet
- List all resources:  
  ```bash
  kubectl api-resources
