Great — below is a compact, practical **debug playbook** for common pod error states (CrashLoopBackOff, ImagePullBackOff, ErrImagePull, CreateContainerConfigError, FailedMount, OOMKilled, etc.) using only `kubectl` (and a few node-level checks where needed). For each error I show **how to identify it**, **which commands to run**, **what to look for**, and **common fixes**. Copy-paste the commands into your terminal.

---

# 1) Start with a quick inventory

```bash
# List pods (all namespaces) and show status
kubectl get pods --all-namespaces -o wide
# Or for a specific namespace
kubectl get pods -n <ns> -o wide
```

Look at the **STATUS** column (CrashLoopBackOff, ImagePullBackOff, ErrImagePull, Completed, Running, Pending).

---

# 2) Get the detailed pod state and events

```bash
kubectl describe pod <pod-name> -n <ns>
```

**What to look for**

* `Events:` section (most helpful) — shows FailedMount, Failed to pull image, Back-off restarting failed container, etc.
* `ContainerStatuses` → `state`, `lastState`, `reason`, `exitCode`, `message`, `started`, `finishedAt`.
* Init containers failures are listed above app containers.

---

# 3) View logs (app container) — CrashLoopBackOff diagnosis

```bash
# Live logs
kubectl logs <pod> -n <ns> -c <container-name>

# If pod is restarted multiple times, view previous logs
kubectl logs <pod> -n <ns> -c <container-name> --previous
```

**What to look for**

* Stack traces, exceptions, immediate crash reasons.
* Common: missing config, connect failure, misconfiguration, fatal startup error.

**Common fixes**

* Fix configuration (ConfigMap/Secret), code bug, dependency endpoint, or resource limits.
* If startup takes long, increase `readiness`/`liveness` probe `initialDelaySeconds`.

---

# 4) Inspect container termination reason (OOMKilled, ExitCode)

```bash
kubectl get pod <pod> -n <ns> -o jsonpath='{.status.containerStatuses[*].state.terminated.reason}{"\n"}'
kubectl describe pod <pod> -n <ns>   # also shows terminated reason and exitCode
```

**If `OOMKilled`**

* Check resource limits/requests in the Deployment/Pod spec.
* Use `kubectl top pod <pod> -n <ns>` (requires metrics-server) to inspect usage.
  **Fix**
* Increase memory limit or reduce memory usage / fix memory leak.

---

# 5) Image pull errors (ImagePullBackOff / ErrImagePull / ErrImageInspect)

```bash
kubectl describe pod <pod> -n <ns>   # Events show: Failed to pull image, rpc error, unauthorized, no such host
```

**What to look for**

* `Failed to pull image "<image>"`: reason message may include `unauthorized`, `manifest unknown`, `no such host`, `x509` TLS error.
* If private registry: check `imagePullSecrets` existence and content.

**Debug steps**

```bash
# Inspect pod spec for image name and imagePullSecrets
kubectl get pod <pod> -n <ns> -o yaml | yq '.spec'      # or just view YAML

# Check secret exists (for Docker registry)
kubectl get secret <secret-name> -n <ns> -o yaml
```

**Node-level check (if you have node access)**:

```bash
# On the node running the pod (get node name from `kubectl get pod -o wide`)
ssh <node>
# For containerd:
ctr images pull <image>     # or crictl pull <image>
# For docker:
docker pull <image>
```

**Common fixes**

* Correct image tag/name (typo).
* Add `imagePullSecrets` pointing to a `kubernetes.io/dockerconfigjson` secret created with `kubectl create secret docker-registry ...`.
* Fix network/DNS or TLS to registry.

---

# 6) FailedMount / volume mount problems

```bash
kubectl describe pod <pod> -n <ns>
# Events show messages like FailedMount, MountVolume.SetUp failed, or permission denied
```

**What to look for**

* PVC unbound or stuck (`PersistentVolumeClaim` status).
* Permission issues (NFS/CSI mount permission).
* CSI driver errors in the events.

**Commands**

```bash
kubectl get pvc -n <ns>
kubectl describe pvc <pvc-name> -n <ns>
kubectl get pv
kubectl describe pv <pv-name>
```

**Common fixes**

* Ensure PV is available and matches storageClass/access modes.
* Fix NFS/CSI server access or node permissions.
* Check CSI driver logs on node / controller.

---

# 7) CreateContainerConfigError / Invalid imagePullPolicy / env var issues

```bash
kubectl describe pod <pod> -n <ns>
```

**What to look for**

* Errors like `CreateContainerConfigError: secret not found`, `MountVolume.SetUp`, or `invalid environment variable reference`.
  **Fixes**
* Ensure referenced Secrets/ConfigMaps exist and are in same namespace.
* Correct invalid syntax in Pod spec.

---

# 8) Running interactive debug (ephemeral container) — attach debug container into broken pod

Use ephemeral containers when the app container exits quickly but you want to inspect filesystem or env.

```bash
# Add an ephemeral debug container to the pod (kubectl >= 1.18)
kubectl debug -it <pod> -n <ns> --image=busybox --target=<container-name> -- /bin/sh
```

If `kubectl debug` isn't available, use:

```bash
kubectl run -it --rm debug --image=busybox -n <ns> --overrides='
{
  "apiVersion":"v1",
  "spec":{
    "containers":[{"name":"debug","image":"busybox","stdin":true,"tty":true}]
  }
}' -- /bin/sh
```

Then you can `nsenter`, curl local sockets, inspect files, logs under `/var/log`, mount points, etc.

---

# 9) Check pod events globally and sort by time

```bash
kubectl get events -n <ns> --sort-by='.lastTimestamp'
# or all namespaces
kubectl get events --all-namespaces --sort-by='.lastTimestamp'
```

Events often show the root cause (image pull failures, mount errors, probe failures).

---

# 10) Probe failures (readiness/liveness)

```bash
kubectl describe pod <pod> -n <ns>
```

**What to look for**

* Events like `Liveness probe failed` or `Readiness probe failed` with HTTP failure codes or timeout.
  **Debug**
* Temporarily disable the probe to confirm the app can stay up.
* `kubectl port-forward` to test endpoint locally:

```bash
kubectl port-forward pod/<pod> 8080:8080 -n <ns>
curl -v http://localhost:8080/health
```

**Fix**

* Tune `initialDelaySeconds`, `periodSeconds`, `timeoutSeconds`, or fix the health endpoint.

---

# 11) Rollout / deployment checks

```bash
# See if a Deployment is healthy
kubectl rollout status deployment/<deployment-name> -n <ns>

# View rollout history & undo if needed
kubectl rollout history deployment/<deployment-name> -n <ns>
kubectl rollout undo deployment/<deployment-name> -n <ns>
```

---

# 12) Node-level issues (DiskPressure, Network)

If many pods are failing across nodes, suspect node conditions:

```bash
kubectl get nodes
kubectl describe node <node-name>
# Check node conditions for DiskPressure/MemoryPressure, and kubelet events
```

On the node (requires access):

```bash
# Check kubelet logs
sudo journalctl -u kubelet -l --no-pager | tail -n 200

# If using containerd/cri:
sudo crictl ps -a
sudo crictl logs <container-id>
```

**Fixes**

* Free disk or increase node disk.
* Restart kubelet if required (be careful in production).

---

# 13) When pods won't start and you need more verbosity

```bash
kubectl get pod <pod> -n <ns> -o yaml    # inspect the full object
kubectl get pod <pod> -n <ns> -o jsonpath='{.status.containerStatuses[*].state}'  # quick state dump
```

---

# 14) Quick decision checklist by status

* **ImagePullBackOff / ErrImagePull** → `kubectl describe pod` → check error message → fix image name, registry auth, network.
* **CrashLoopBackOff** → `kubectl logs --previous` → inspect stacktrace → fix app or adjust probes/resources.
* **OOMKilled** → `kubectl describe pod` shows reason OOMKilled → check `kubectl top pod` → increase memory limits or fix memory usage.
* **CreateContainerConfigError** → `describe` shows missing secret/configmap → create or correct it.
* **FailedMount** → `describe` events show mount error → inspect PVC/PV/CSI driver permissions.
* **ContainerCreating (stuck)** → `describe` events (image pull, mount, node constraints, admission webhook).

---

# 15) Useful one-liners

```bash
# Show pods not in Running/Completed state
kubectl get pods -A --field-selector=status.phase!=Running,status.phase!=Succeeded

# Get pods with CrashLoopBackOff
kubectl get pods -A | grep CrashLoopBackOff

# Show last 100 lines of logs for container
kubectl logs <pod> -n <ns> -c <container> --tail=100

# Show container exit code and reason
kubectl get pod <pod> -n <ns> -o jsonpath='{.status.containerStatuses[*].lastState.terminated.reason}{"\n"}{.status.containerStatuses[*].lastState.terminated.exitCode}{"\n"}'
```

---

# 16) When to involve node/infra team

* Image registry auth/manifest issues (registry side).
* Persistent volume provisioning failures (cloud storage permissions).
* Node-level resource exhaustion (DiskPressure/MemoryPressure).
* CNI/network policy blocking egress to registry or DB.

---

If you want, I can:

* Generate a condensed **cheat-sheet** (printable) with commands per failure type.
* Walk through **one specific pod** in your cluster — paste the output of `kubectl get pod <pod> -n <ns> -o yaml` and I’ll analyze it live and tell you what to fix.
