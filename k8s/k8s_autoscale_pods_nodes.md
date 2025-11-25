End-to-step guide to **implement autoscaling** in a Kubernetes cluster. It covers **Horizontal Pod Autoscaler (HPA)**, **Cluster Autoscaler (node autoscaling)**, **Vertical Pod Autoscaler (VPA)**, custom metrics via Prometheus, verification and troubleshooting. Copy/paste commands and YAMLs and adapt names/values to your environment.

# 1) Quick overview (what you’ll implement)

* **HPA (Horizontal Pod Autoscaler)** — scale application pods horizontally based on CPU/memory or custom metrics.
* **Cluster Autoscaler** — add/remove nodes when pods can’t be scheduled or nodes are underutilized (cloud or on-prem variants).
* **VPA (Vertical Pod Autoscaler)** — optionally recommend or update CPU/memory requests/limits.
* **metrics-server** for basic CPU/memory metrics; **Prometheus + adapter** for custom metrics (requests/sec, latency).

---

# 2) Prerequisites

* A working Kubernetes cluster with `kubectl` configured.
* `kubectl` version compatible with your cluster.
* For Cluster Autoscaler: cloud provider (EKS/GKE/AKS) or an on-prem autoscaler (e.g., Cluster Autoscaler with static node groups or Karpenter).
* RBAC permissions to create CRDs, deploy addons, and create ClusterRole/ClusterRoleBinding.

---

# 3) Install metrics-server (required for HPA based on CPU/memory)

HPA uses metrics API provided by metrics-server for CPU/memory.

```bash
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
# Verify pods
kubectl get pods -n kube-system -l k8s-app=metrics-server
# Test:
kubectl top nodes
kubectl top pods -n <your-namespace>
```

If `kubectl top` returns empty, ensure metrics-server has access and API aggregation is enabled.

---

# 4) Make sure your Deployment has resource requests/limits

HPA using CPU/memory needs the pod's **requests** to be set (HPA compares usage vs request).

Example `deployment.yaml` (minimal):

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: web
  namespace: demo
spec:
  replicas: 1
  selector:
    matchLabels:
      app: web
  template:
    metadata:
      labels:
        app: web
    spec:
      containers:
      - name: web
        image: hashicorp/http-echo:0.2.3
        args: ["-text=hello"]
        ports:
        - containerPort: 5678
        resources:
          requests:
            cpu: "100m"
            memory: "128Mi"
          limits:
            cpu: "500m"
            memory: "256Mi"
```

Apply:

```bash
kubectl create namespace demo
kubectl apply -f deployment.yaml
```

---

# 5) Create Horizontal Pod Autoscaler (HPA) — CPU based (autoscaling/v2)

Use `autoscaling/v2` to specify metrics and behavior.

`hpa-cpu.yaml`:

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: web-hpa
  namespace: demo
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: web
  minReplicas: 1
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 60    # target 60% CPU usage
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 0
      selectPolicy: Max
      policies:
      - type: Percent
        value: 100
        periodSeconds: 60
    scaleDown:
      stabilizationWindowSeconds: 300
      selectPolicy: Min
      policies:
      - type: Percent
        value: 50
        periodSeconds: 60
```

Apply:

```bash
kubectl apply -f hpa-cpu.yaml
```

Verify:

```bash
kubectl get hpa -n demo
kubectl describe hpa web-hpa -n demo
kubectl get deployment web -n demo -o wide
```

---

# 6) Trigger load to test HPA

Use a simple load generator (`hey` or `wrk` images). Example using `busybox` loop (very simple):

```bash
# Create a load pod that continuously requests the service
kubectl run -n demo load-generator --image=plasmicapp/hey --restart=Never --command -- /bin/sh -c "hey -z 120s -q 10 -c 50 http://web.demo.svc.cluster.local:5678/"
```

Or use `kubectl exec` into a pod and run a loop of `curl`.

While load runs, watch:

```bash
kubectl get hpa -n demo --watch
kubectl get pods -n demo --watch
kubectl top pods -n demo
```

You should see replicas increase when average CPU passes the target.

---

# 7) Cluster Autoscaler (scaling nodes)

If HPA increases pods beyond current node capacity, you need Cluster Autoscaler to provision nodes.

**Important:** Cluster Autoscaler configuration depends on the environment.

### A) Cloud-managed (EKS/GKE/AKS) — recommended approach

* Use the provider-specific Cluster Autoscaler or Karpenter. Install via Helm or manifest and pass the node group / node pool identifiers.

Example (AWS EKS minimal args; adapt to your cluster):

```yaml
# deployment args for cluster-autoscaler (example snippet)
--cloud-provider=aws
--nodes=1:10:<your-node-group-name>
--scale-down-enabled=true
--skip-nodes-with-local-storage=false
--expander=least-waste
```

Install via Helm or manifests; ensure the autoscaler has proper IAM permissions (node autoscaling IAM role). Verify with:

```bash
kubectl get deployment cluster-autoscaler -n kube-system
kubectl logs -f deployment/cluster-autoscaler -n kube-system
```

### B) On-prem or custom cloud

* You can still use Cluster Autoscaler but must provide a cloud provider integration or use solutions like **Cluster Autoscaler for custom providers**, or adopt **Karpenter** (AWS-specific but has multi-cloud support in evolution), or scale VMs via external automation (e.g., Terraform + CI) watching the Kubernetes unschedulable pods.

**Verify**:

* When pods are Pending due to insufficient resources, Cluster Autoscaler should log decisions and increment node count. Monitor `kubectl get nodes` and events.

---

# 8) Use custom metrics (requests/sec) with Prometheus Adapter → HPA v2 custom metric

For business metrics (RPS, queue length) you need Prometheus + Prometheus Adapter exposing `metrics.k8s.io/custom.metrics.k8s.io` API.

Steps:

1. Install Prometheus (Helm `kube-prometheus-stack` or Prometheus Operator) and deploy a ServiceMonitor for your app metrics.
2. Install `prometheus-adapter` and configure rules mapping PromQL to custom metric names.
3. Create HPA referencing the custom metric.

Example HPA (custom metric):

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: web-hpa-custom
  namespace: demo
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: web
  minReplicas: 1
  maxReplicas: 20
  metrics:
  - type: Pods
    pods:
      metric:
        name: http_requests_per_second
      target:
        type: AverageValue
        averageValue: "50"  # average RPS per pod
```

`prometheus-adapter` config maps a PromQL rule like `sum(rate(http_requests_total[1m])) by (pod)` to `http_requests_per_second`.

(Setting up Prometheus Adapter requires YAML config; if you want I can provide a sample PrometheusAdapter config next.)

---

# 9) Vertical Pod Autoscaler (VPA) — optional

VPA adjusts pod resource **requests/limits**. It can run in three modes:

* `Off` — only recommendations
* `Auto` — automatically updates pods (may restart them)
* `Initial` — set requests only at pod creation

Install VPA (example using official manifests / operator) and create a `VerticalPodAutoscaler`:

```yaml
apiVersion: autoscaling.k8s.io/v1
kind: VerticalPodAutoscaler
metadata:
  name: web-vpa
  namespace: demo
spec:
  targetRef:
    apiVersion: "apps/v1"
    kind:       Deployment
    name:       web
  updatePolicy:
    updateMode: "Off"   # use "Auto" carefully in production
```

**Note:** Running HPA and VPA together on same resource can conflict. Recommended patterns:

* Use VPA in `Off`/recommendation mode to get sizes, then HPA for scaling.
* Or use VPA for batch jobs / non-horizontal scaled workloads.

---

# 10) Best practices & production tips

* **Always set resource requests** for containers; HPA needs them.
* **Tune HPA behavior** (stabilizationWindowSeconds) to avoid flapping.
* **PodDisruptionBudgets (PDBs)** prevent scale-down from violating availability.
* **Readiness probe** must be accurate: HPA should scale based on real serving capacity.
* **Limit maxReplicas** to a safe upper bound to avoid uncontrolled costs.
* **Use horizontal + cluster autoscaler** so pods get scheduled when HPA scales up.
* **Use autoscaler-safe node groups** (node pools with correct labels and taints).
* **Monitor autoscaler logs** and Prometheus metrics for scaling decisions.
* **Test scale down** behavior (scaleDown uses unneeded timeouts).

---

# 11) Verification & debug commands

* HPA status:

```bash
kubectl get hpa -n demo
kubectl describe hpa web-hpa -n demo
```

* Pod/Deployment:

```bash
kubectl get pods -n demo --watch
kubectl get deploy web -n demo -o wide
kubectl top pods -n demo
```

* Cluster Autoscaler logs:

```bash
kubectl logs -n kube-system deployment/cluster-autoscaler --follow
```

* Prometheus adapter:

```bash
kubectl get --raw "/apis/custom.metrics.k8s.io/v1beta1" | jq .
```

* Events:

```bash
kubectl get events -n demo --sort-by='.lastTimestamp'
```

---

# 12) Common problems & fixes

* **HPA shows `eligible` but no scale up** → check metrics-server (`kubectl top`) or custom metrics availability.
* **Pods remain Pending when HPA increases** → Cluster Autoscaler not installed/configured or no capacity; check node autoscaler logs.
* **Flapping (rapid scale up/down)** → increase stabilizationWindowSeconds, add scaleUp/scaleDown policies.
* **VPA and HPA conflict** → run VPA in recommendation mode or apply only to non-HPA workloads.
* **Custom metrics missing** → confirm Prometheus adapter has correct rules and RBAC and that metrics endpoint is scraped.

---

# 13) Example full flow (quick recap)

1. Install metrics-server.
2. Make sure `Deployment` pods have `requests` set.
3. Apply HPA (CPU/memory).
4. Install Prometheus + adapter for custom metrics (optional).
5. Install Cluster Autoscaler (cloud-specific) so nodes are added when pods cannot be scheduled.
6. Test with synthetic load and observe `kubectl get hpa`, `kubectl get pods`, `kubectl get nodes`.
7. Tune behavior and add PDBs.

---

If you want, I can:

* Generate a full set of ready-to-apply manifests (Deployment + HPA + PrometheusAdapter config + Cluster Autoscaler values for a specific cloud like **EKS/GKE/AKS**).
* Or produce a short troubleshooting checklist (1-page) for on-call responders.

Which one should I produce next?
