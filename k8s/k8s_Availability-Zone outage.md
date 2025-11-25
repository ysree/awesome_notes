# How to survive an Availability-Zone outage — design, implementation steps, and runbook

Nice — AZ outages happen. The right approach is **avoid single-AZ failure domains**, architect for multi-AZ (or multi-region) redundancy, make your data durable/replicated, and automate failover. Below I give a practical, implementation-focused plan you can apply immediately to a Kubernetes + cloud environment (AWS/GCP/Azure), plus YAML/commands, testing steps, and tradeoffs.

---

# 1) High-level strategy (short)

1. **Run control and compute across multiple AZs** — place nodes in ≥2 AZs (preferably 3).
2. **Spread pods across AZs** (topology-aware scheduling, anti-affinity, topologySpreadConstraints).
3. **Use multi-AZ load balancing** (cloud LB that routes traffic to healthy AZs).
4. **Make storage and databases highly available** — choose multi-AZ or distributed DBs.
5. **Automate failover & health checks** (LB health checks, DNS failover/global load balancers).
6. **Test with chaos** (simulate AZ failure), and add monitoring/alerts for failover.

---

# 2) Concrete implementation steps (Kubernetes + Cloud)

## A — Ensure multi-AZ worker nodes

* Provision node pools / node groups across at least two AZs. In AWS EKS/GKE/AKS you can create node groups that span AZs.
* Cluster autoscaler should be configured per node group so new nodes can be added in the appropriate AZs.

Example: EKS nodegroups in `us-east-1a`, `us-east-1b`, `us-east-1c`.

Check nodes:

```bash
kubectl get nodes -o wide
# verify zone column (or label)
kubectl get nodes --show-labels | grep topology.kubernetes.io/zone
```

## B — Spread your pods across AZs

### 1) Pod anti-affinity (ensure replicas sit in different AZs)

Add anti-affinity to your Deployment to prefer different zones:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: web
  namespace: prod
spec:
  replicas: 3
  selector: { matchLabels: { app: web } }
  template:
    metadata:
      labels: { app: web }
    spec:
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              topologyKey: topology.kubernetes.io/zone
              labelSelector:
                matchLabels:
                  app: web
      containers:
      - name: web
        image: myapp:latest
        resources: ...
```

### 2) topologySpreadConstraints (K8s >=1.18)

```yaml
spec:
  topologySpreadConstraints:
  - maxSkew: 1
    topologyKey: topology.kubernetes.io/zone
    whenUnsatisfiable: ScheduleAnyway
    labelSelector:
      matchLabels:
        app: web
```

These ensure replica distribution so an AZ failure still leaves healthy replicas.

## C — Use a multi-AZ Load Balancer / Global LB

* Use cloud provider LB (ALB/NLB on AWS, GCLB on GCP) that spans multiple AZs and health checks to avoid sending traffic to unhealthy AZ.
* For cross-region resilience, use a global load balancer or Global Accelerator (AWS) / Cloud CDN + GCLB (GCP) and Route53 / Cloud DNS.

Example: Service of type LoadBalancer:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: frontend
  annotations:
    service.beta.kubernetes.io/aws-load-balancer-type: "nlb"
spec:
  type: LoadBalancer
  selector:
    app: web
  ports:
  - port: 80
    targetPort: 8080
```

Cloud LB will spread across AZs automatically when backing nodes/pods exist in each AZ.

**Important:** Set `externalTrafficPolicy: Local` if you want source-IP preservation and to avoid cross-AZ hops — but note this requires pods in all AZs.

## D — Stateful storage: use multi-AZ capable volumes

* **Do not use single-AZ block volumes (EBS/AzureDisk) for cross-AZ HA** unless you're okay with pod rescheduling delays. Use:

  * **ReadWriteMany** StorageClasses (EFS on AWS, Azure Files, GCE Filestore) for shared storage across AZs/regions.
  * Or use application-level replication (see DB section).
* For persistent state with strong availability: use managed file systems (EFS) or object storage (S3/GCS) for assets.

Example: Use EFS CSI driver and a PVC with `ReadWriteMany`.

## E — Databases: ensure multi-AZ / multi-region replication

Choose one of these depending on need:

1. **Managed RDBMS Multi-AZ (RDS, Cloud SQL)** — synchronous or semi-sync failover, minimal RTO. Good for single-region redundancy.
2. **Aurora / Cloud Spanner / Cosmos DB** — provide cross-AZ and often cross-region replicas with fast failover.
3. **Distributed SQL / NoSQL (CockroachDB, YugabyteDB, Cassandra)** — multi-AZ or multi-region clusters: survive AZ/region loss with partition-tolerant designs.
4. **For caches**: use managed clusters with replication (Redis Cluster with replicas across AZs).

**Tradeoffs**: synchronous cross-region replication increases latency; asynchronous can lose recent writes (RPO). Pick based on RTO/RPO.

## F — Control plane & ingress redundancy

* Ensure **control plane (if self-managed)** is multi-AZ; managed Kubernetes control planes are already multi-AZ (EKS/GKE/AKS).
* For Istio: deploy ingress gateways in each zone/cluster and use **multi-cluster or multi-network** Istio if spanning regions/clusters. Use Global LB to forward traffic to gateways in healthy AZs.

## G — Autoscaling + scheduling tie-ins

* Configure **Cluster Autoscaler** to scale node pools in multiple AZs.
* Use `nodeAffinity`/labels to ensure critical pods run in multiple AZ node pools.

---

# 3) Example: end-to-end YAML/commands (summary)

1. Label nodes with zone label (if needed):

```bash
kubectl label node <node-name> topology.kubernetes.io/zone=<zone>
```

2. Deployment with anti-affinity + topologySpread (see snippets above). Deploy 3 replicas.

3. Service type LoadBalancer (cloud LB spans AZs). Apply YAML and confirm EXTERNAL-IP.

4. Use EFS or ReadWriteMany PVC for any pod that needs shared filesystem.

5. For DB, choose Aurora (example):

   * Create Aurora cluster with Multi-AZ & reader replicas.
   * Connect application with failover endpoints (RDS endpoint points to writer).

6. Configure health checks & LB settings so that if AZ unhealthy, LB removes unhealthy targets.

---

# 4) Testing & validation (must do)

1. **Chaos test**: Simulate AZ failure — cordon & drain all nodes in one AZ, or simulate network partition. Example:

```bash
# cordon and drain nodes in AZ-a (do on each node)
kubectl drain <node-in-az-a> --ignore-daemonsets --delete-local-data
```

2. **Observe behavior**:

```bash
kubectl get pods -n prod -o wide   # pods should reschedule to other AZs
kubectl get nodes                 # Cluster autoscaler may add nodes
kubectl get svc frontend          # LB should keep routing to healthy endpoints
# Hit service endpoint and measure latency/errors
curl http://<load-balancer-ip>/health
```

3. **Database failover**: verify writes and reads still succeed after failover and measure RTO/RPO.

4. **Run synthetic traffic** while failing AZ to confirm no downtime.

---

# 5) Monitoring, alerting and automation

* **Metrics & alerts**: monitor node availability, pod distribution per zone, LB unhealthy hosts, error rates, and DB replication lag. Use Prometheus + Grafana + Alertmanager.
* **Route53/Global LB health checks**: auto failover to another region if necessary; use low TTL DNS for faster switchover.
* **Runbooks & playbooks**: automation to promote read replica to writer (if not automatic), scripts for emergency scale out.
* **Automated failover**: prefer managed offerings that failover automatically (RDS Multi-AZ, Aurora).

---

# 6) Operational practices & tradeoffs

* **Prefer multi-AZ before multi-region** for lower latency and simpler failover. Use multi-region when you need to survive region failures or meet legal requirements.
* **Synchronous replication** → zero data loss but higher latency. **Asynchronous** → lower latency but possible data loss.
* **Cross-AZ network egress** may cost more and add latency; measure impact.
* **Shared block storage (EBS) is AZ-local** — can't mount in another AZ. Use shared file/object or DB replication.
* **Testing is mandatory** — regularly run chaos tests (Chaos Mesh/Gremlin/chaos open-source tools).
* **PDBs**: set PodDisruptionBudgets so scale-down or upgrades don’t reduce availability below threshold.

---

# 7) Quick checklist to implement now

* [ ] Ensure node pools across ≥2 AZs (prefer 3).
* [ ] Add pod anti-affinity + topologySpreadConstraints to Deployments.
* [ ] Use Service type=LoadBalancer (cloud LB) that spans AZs; verify health checks.
* [ ] Migrate state to multi-AZ storage (EFS / ReadWriteMany) or use DB multi-AZ.
* [ ] Configure Cluster Autoscaler for AZ-aware node groups.
* [ ] Set up monitoring & alerts for AZ/node/pod health.
* [ ] Run a controlled AZ failure test; validate no downtime.

---

# 8) Example short runbook for an AZ outage

1. Detection: Alert triggers — LB unhealthy host count > 0 or zone-specific error spike.
2. Verify: `kubectl get pods -o wide` and `kubectl get nodes --show-labels` to see zone impact.
3. Confirm LB health: cloud console / `kubectl get svc` / `curl` to VIP.
4. If pods not rescheduling: check Cluster Autoscaler logs and create capacity in other AZs.
5. If DB failed: promote replica (manual if needed), update endpoints.
6. After recovery: re-introduce nodes, ensure pods reschedule back if desired, and reconcile PDBs.

---

# 9) Final notes (recommended architectures)

* For **stateless HTTP services**: deploy multi-AZ pods + LB + autoscaler. Very low downtime risk.
* For **stateful services**: prefer managed multi-AZ DB or distributed DB cluster. Don’t rely on single-AZ volumes.
* For **global availability (zero downtime for region failure)**: use multi-region clusters, global LB, and data replication across regions (higher complexity).

---

If you want, I can:

* Produce a ready-to-apply set of Kubernetes manifests for: Deployment (anti-affinity + topologySpread), Service LoadBalancer, EFS PVC example, and sample Cluster Autoscaler values for **EKS** or **GKE**.
* Or produce a Chaos test plan (exact commands) to simulate AZ failure safely.

Which one should I generate for you right now?
