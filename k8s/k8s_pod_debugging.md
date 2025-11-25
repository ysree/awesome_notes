Pod is **continuously restarting** *and* you can’t get useful info from `kubectl describe` (or `describe` appears to “fail” / gives little). Run the commands in order and read the short explanation under each — I show alternatives to `describe` and node-level approaches if Kubernetes APIs aren’t helping.

---

## 0) Preparation — get the exact pod name & namespace

(You almost certainly know these — if not, list pods)

```bash
kubectl get pods -n <ns> -o wide
```

Replace `<ns>` with your namespace (or `--all-namespaces`).

---

## 1) First: try logs (the most helpful step)

Pods that restart often have useful **previous** logs.

```bash
kubectl logs <pod> -n <ns> -c <container> --previous
# If you don't know container name:
kubectl logs <pod> -n <ns> --previous
# Tail recent log lines:
kubectl logs <pod> -n <ns> -c <container> --previous --tail=200
```

**Look for:** stack traces, exceptions, fatal errors, crash reasons printed by the app.

If `--previous` returns nothing, try logs without `--previous` (in case container is currently running long enough):

```bash
kubectl logs <pod> -n <ns> -c <container>
```

---

## 2) If `kubectl describe` is not showing events — get events explicitly for the pod

Sometimes `describe` truncates or you need the events resource directly:

```bash
kubectl get events -n <ns> --field-selector involvedObject.name=<pod> --sort-by='.lastTimestamp'
# Or:
kubectl get events -n <ns> | grep <pod>
```

**Look for:** `Back-off restarting failed container`, `Failed to pull image`, `MountVolume.SetUp failed`, `Liveness probe failed`, `OOMKilled`, etc.

---

## 3) Inspect the pod object (yaml/json) — containerStatuses is key

Even if `describe` is poor, the raw object contains state info:

```bash
kubectl get pod <pod> -n <ns> -o yaml > pod.yaml
# Inspect relevant fields quickly:
kubectl get pod <pod> -n <ns> -o jsonpath='{.status.containerStatuses[*]}' | sed 's/}{/}\n{/g'
# Or extract last termination reason / exitCode:
kubectl get pod <pod> -n <ns> -o jsonpath='{.status.containerStatuses[*].lastState.terminated.reason}{"\n"}{.status.containerStatuses[*].lastState.terminated.exitCode}{"\n"}{.status.containerStatuses[*].state.waiting.reason}{"\n"}'
```

**Key fields:**
`.status.containerStatuses[].lastState.terminated.reason`
`.status.containerStatuses[].lastState.terminated.exitCode`
`.status.containerStatuses[].restartCount`
`.status.conditions`

These will tell you if the container was `OOMKilled`, `Error`, or waiting with `CrashLoopBackOff` reason.

---

## 4) If `kubectl describe` returns an API error (e.g., Forbidden / NotFound)

* **Forbidden** → RBAC issue: `kubectl auth can-i get events -n <ns>`
* **NotFound** → maybe pod recreated with new name; check `kubectl get pods` again.

If `kubectl` itself errors or is slow, check cluster health:

```bash
kubectl cluster-info
kubectl get componentstatuses
kubectl get nodes
```

---

## 5) Check for probe failures (readiness/liveness)

Probe failures often cause restarts immediately:

```bash
kubectl get pod <pod> -n <ns> -o jsonpath='{.spec.containers[*].livenessProbe}{"\n"}{.spec.containers[*].readinessProbe}{"\n"}'
# Or check events for liveness/readiness failures:
kubectl get events -n <ns> --sort-by='.lastTimestamp' | grep -E "<pod>|liveness|readiness"
```

**If probes fail:** either tune `initialDelaySeconds` / `timeoutSeconds` or fix the health endpoint.

---

## 6) Check resource issues (OOMKilled)

```bash
kubectl describe pod <pod> -n <ns>   # look for OOMKilled in Events or containerStatuses
kubectl top pod <pod> -n <ns>        # requires metrics-server
```

If OOMKilled → increase memory limits or fix memory leak in app.

---

## 7) Image pull / startup failures

If events show `ImagePullBackOff`, `ErrImagePull`, or `ErrImageInspect`:

```bash
kubectl describe pod <pod> -n <ns>   # events show the image error message
# inspect image name
kubectl get pod <pod> -n <ns> -o jsonpath='{.spec.containers[*].image}{"\n"}'
```

Common fixes: wrong image tag, missing `imagePullSecrets`, private registry auth, or network/DNS.

---

## 8) If pod restarts too fast to `kubectl exec` or describe, run a debug replica manually

Create a debug pod using the same image but with a sleep command so it stays up:

```bash
kubectl run debug-<name> -n <ns> --image=<the-same-image> --restart=Never --command -- sleep 1d
kubectl exec -it debug-<name> -n <ns> -- /bin/sh
```

This lets you reproduce startup steps interactively (inspect env, files, run app start command manually).

Alternatively, edit the Deployment to change the container command to `sleep` temporarily:

```bash
kubectl -n <ns> scale deployment <deployment> --replicas=0
kubectl -n <ns> run debug --image=<image> --restart=Never --command -- sleep 1d
```

---

## 9) Use ephemeral containers to debug a live pod (attach a debug toolbox)

If the pod is restarting and you need to inspect the container namespace while it’s running:

```bash
kubectl debug -it <pod> -n <ns> --image=busybox --target=<container-name> -- /bin/sh
```

(Requires cluster & kubectl support for ephemeral containers.)

---

## 10) Node-level debugging (when Kubernetes-level info is insufficient)

If pod events/logs are missing, check the node where the pod ran:

1. Get node name:

```bash
kubectl get pod <pod> -n <ns> -o wide
```

2. SSH to node (if you have access) and check kubelet and container runtime logs:

```bash
# kubelet logs (systemd)
sudo journalctl -u kubelet -n 500 --no-pager

# container runtime (containerd/cri)
# list containers:
sudo crictl ps -a | grep <pod-or-image>
# fetch container logs:
sudo crictl logs <container-id>
# For docker:
sudo docker ps -a
sudo docker logs <container-id>
```

Node conditions (DiskPressure/MemoryPressure) can cause evictions/restarts:

```bash
kubectl describe node <node>
```

If node shows DiskPressure/MemoryPressure, free disk or scale down.

---

## 11) Check ReplicaSet / Deployment / Pod template for obvious config issues

```bash
kubectl get deploy <deployment> -n <ns> -o yaml
kubectl get rs -n <ns>
kubectl rollout status deployment/<deployment> -n <ns>
```

Look for wrong env vars, missing secrets/configmaps referenced in `envFrom` or `volumeMounts`, bad command args, and wrong image.

---

## 12) If `kubectl describe` itself times out — increase verbosity to see client/server requests

```bash
kubectl describe pod <pod> -n <ns> --v=8
```

This can reveal API errors or timeouts.

---

## 13) Common root causes checklist

* Application crash on startup (code bug) → check `kubectl logs --previous`.
* Liveness/readiness probe failing → tune or fix endpoint.
* Missing Secret/ConfigMap → `CreateContainerConfigError`.
* OOMKilled → increase memory.
* Image pull error → image/tag/registry auth.
* Volume mount failure → PVC/PV binding or permissions.
* Node resource pressure or kubelet crash → check node logs.
* Admission webhook side-effects (mutating/validating) → check webhook logs and events.

---

## 14) Quick recovery trick: stop the restart loop to inspect container

If the pod keeps restarting, you can **prevent restarts** to inspect the broken image/run locally:

* Scale Deployment `replicas=0` to stop churn:

```bash
kubectl scale deployment <deployment> -n <ns> --replicas=0
```

* Then run a one-off pod with the same image (but sleep) to debug, as shown earlier.

---

## 15) When to escalate to infra / node admins

* If you see `Failed to create pod sandbox` or runtime errors from containerd/cri — ask node admins.
* If kubelet or kube-apiserver is unhealthy.
* If PVC provisioning is failing due to cloud provider/storage class permissions.

---

## 16) If you prefer: paste outputs and I’ll analyze

If you paste the outputs of these (remove any secrets):

* `kubectl get pod <pod> -n <ns> -o yaml`
* `kubectl logs <pod> -n <ns> --previous --tail=200`
* `kubectl get events -n <ns> --field-selector involvedObject.name=<pod> --sort-by='.lastTimestamp'`
  I’ll read them and tell you the exact cause and recommended fix.

---

If you want, I can give a short **one-page checklist** you can keep on your terminal — or produce the exact commands to run on your cluster now. Which do you want next?
