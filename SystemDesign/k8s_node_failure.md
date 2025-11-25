Nice — below is a compact, interview-ready answer **plus** an actionable runbook you can cite if they ask for details. It covers detection, immediate mitigation, recovery (both for stateless and stateful workloads), AWS/EKS-specific steps, and follow-up.

# Short answer (30–60 seconds)

If a node is lost I first detect and isolate it, let Kubernetes reschedule workloads (or force it), replace the node automatically via the cloud provider (ASG/managed nodegroup), and restore any stuck stateful workloads (PV/EBS reattachment or restore from snapshot). I run health checks and smoke tests, capture logs and metrics for RCA, and update runbooks. Automation (ASG + Cluster Autoscaler + proper StorageClass/CSI) and PodDisruptionBudgets keep RTO low and prevent user impact.

# Actionable runbook (step-by-step)

## 1) Detect & confirm

* Check cluster health and node status:

  ```
  kubectl get nodes
  kubectl describe node <node-name>
  kubectl get pods -o wide --all-namespaces | grep <node-name>
  ```
* Check cloud/infra alarms (CloudWatch, Prometheus alert).

## 2) Isolate / prevent further scheduling (if node reachable)

* If node is flapping or partially healthy, cordon it to stop new pods:

  ```
  kubectl cordon <node-name>
  ```
* Drain safely to move pods off (respect PDBs):

  ```
  kubectl drain <node-name> --ignore-daemonsets --delete-local-data --force
  ```

  *If drain hangs because pods are stuck, consider `--grace-period=0 --force` only after evaluating risk.*

## 3) Let Kubernetes reschedule pods (stateless)

* For Deployments/ReplicaSets/DaemonSets, Kubernetes will create new pods on other nodes. Verify:

  ```
  kubectl get pods -n <ns> -o wide
  ```
* If pods don't come up, check scheduling reasons:

  ```
  kubectl describe pod <pod-name>
  ```

## 4) Handle stateful workloads (PVC / EBS / PV)

* Check PV/PVC states:

  ```
  kubectl get pvc -A
  kubectl describe pvc <pvc>
  kubectl get pv
  ```
* For EBS-backed PVs: if volume remains attached to dead node, Kubernetes CSI may fail to attach. Remedy:

  * Use AWS console or CLI to **force-detach** EBS volume from the terminated/flaky node (careful: ensure node truly unreachable).
  * Wait for CSI attach to complete; the PV will move to `Bound` and Pod will start.
  * If using EBS CSI snapshots or volume snapshots, restore from snapshot if volume corrupted.
* If using ReadWriteOnce and cluster couldn’t reattach, consider:

  * Ensuring `volumeBindingMode: WaitForFirstConsumer` to avoid binding to lost node initially.
  * Use VolumeSnapshot restore if necessary.

## 5) Replace the node (cloud/EKS specifics)

* For managed nodegroups (EKS) or ASG:

  * Terminate the problematic EC2 instance (AWS will auto-launch a replacement per ASG).
  * Or increase desired capacity to prompt new nodes.
  * For self-managed nodes, run your bootstrap script on a replacement EC2 or use Terraform/CloudFormation to reprovision.
* Example: terminate EC2 instance from console or CLI:

  ```
  aws ec2 terminate-instances --instance-ids i-0123456789abcdef0
  ```
* After replacement, confirm node joins:

  ```
  kubectl get nodes
  kubectl describe node <new-node>
  ```

## 6) Cluster Autoscaler / scaling considerations

* If pods couldn’t schedule due to resource shortage, cluster-autoscaler should spin new nodes. Check autoscaler logs and increase limits if necessary.
* Ensure autoscaler has IAM permissions to manage ASG.

## 7) Handle DaemonSets, Static Pods, & system pods

* DaemonSets run on each node; confirm they come up on new node.
* For static pods (kubelet-managed), ensure kubelet configuration/bootstrap scripts run properly on the replacement node.

## 8) Post-recovery validation

* Run smoke tests and health checks for the service endpoints.
* Confirm metrics return to baseline and no throttled errors to downstream services.
* Check logs for errors during failover.

## 9) Capture artifacts & RCA

* Collect:

  * `kubectl describe node` output, pod logs (`kubectl logs`), kubelet logs on node (if accessible), CloudWatch and EC2 instance events, CloudTrail for any infra changes.
* Perform RCA: node hardware/spot interruption, disk full, kernel panic, OOM, kubelet crash, CNI problems, etc.
* Update runbook with root cause and corrective actions.

# Common edge cases & how to handle them

* **EBS volume stuck in "in-use"**: Force detach via AWS and let CSI reattach, or restore from snapshot if corrupted.
* **Spot instance termination**: Use mixed instance types + fallback on on-demand, use node-termination handler to gracefully drain nodes.
* **Pods with local emptyDir data**: Data lost if node dies — design to avoid storing critical state on `emptyDir`.
* **PVCs with RWO and single AZ clusters**: Ensure PVs are replicated or design for multi-AZ/Regional storage (e.g., Aurora for DB, S3 for objects).
* **Network plugin (CNI) failure**: Node may be unreachable but instance alive; check CNI logs and restart kubelet/CNI plugin on node.

# Prevention & hardening (what I implement to reduce impact)

* Auto-healing: ASG/managed nodegroup + Cluster Autoscaler.
* Use PodDisruptionBudgets to limit availability loss during maintenance.
* Prefer stateless services or externalize state to replicated services (RDS/Aurora, S3).
* Storage: use CSI drivers that support snapshots and multi-AZ failover; use `volumeBindingMode: WaitForFirstConsumer`.
* Monitoring/alerts for node CPU, disk pressure, kubelet restarts.
* Periodic chaos testing (node kill) to validate runbook and autoscaling behavior.

# Short example commands recap

```
kubectl get nodes
kubectl cordon <node>
kubectl drain <node> --ignore-daemonsets --delete-local-data
kubectl get pods -o wide --all-namespaces
kubectl describe pod <pod>
kubectl get pvc -A
# In AWS: terminate the EC2 instance (ASG will replace)
aws ec2 terminate-instances --instance-ids i-....
```

---

If you want, I can turn this into:

* a one-page runbook in Markdown with exact commands for your environment (EKS nodegroup names, ASG names, storage class names), or
* a short spoken answer you can use during an interview focused on stateful vs stateless handling.

Which would you like next?
