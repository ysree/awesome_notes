# Kubernetes Debugging Commands Reference

## Pod Debugging Commands

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl get pods` | List all pods in current namespace | Quick overview of pod status |
| `kubectl get pods -A` | List all pods across all namespaces | Cluster-wide pod overview |
| `kubectl get pods -o wide` | List pods with additional details (node, IP) | See pod placement and networking |
| `kubectl describe pod <pod-name>` | Get detailed pod information | Investigate pod configuration and events |
| `kubectl logs <pod-name>` | View pod logs | Check application output and errors |
| `kubectl logs <pod-name> -f` | Follow/tail pod logs in real-time | Monitor live application behavior |
| `kubectl logs <pod-name> --previous` | View logs from previous container instance | Debug crashed containers |
| `kubectl logs <pod-name> -c <container-name>` | View logs from specific container | Multi-container pod debugging |
| `kubectl exec -it <pod-name> -- /bin/bash` | Interactive shell into pod | Debug inside the container |
| `kubectl exec <pod-name> -- <command>` | Execute command in pod | Run diagnostic commands |
| `kubectl port-forward <pod-name> 8080:80` | Forward local port to pod | Access services locally for testing |

## Deployment & ReplicaSet Debugging

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl get deployments` | List all deployments | Check deployment status |
| `kubectl describe deployment <deployment-name>` | Get deployment details | Investigate deployment configuration |
| `kubectl rollout status deployment <deployment-name>` | Check rollout status | Monitor deployment progress |
| `kubectl rollout history deployment <deployment-name>` | View rollout history | Check previous deployment versions |
| `kubectl rollout undo deployment <deployment-name>` | Rollback to previous version | Quick recovery from bad deployments |
| `kubectl get rs` | List ReplicaSets | Check replica set status |
| `kubectl scale deployment <deployment-name> --replicas=3` | Scale deployment | Adjust pod count for testing |

## Service & Networking Debugging

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl get services` | List all services | Check service configuration |
| `kubectl get svc -o wide` | List services with additional details | See service endpoints and selectors |
| `kubectl describe service <service-name>` | Get service details | Debug service configuration |
| `kubectl get endpoints` | List service endpoints | Verify service-to-pod connections |
| `kubectl get ingress` | List ingress resources | Check ingress routing |
| `kubectl describe ingress <ingress-name>` | Get ingress details | Debug ingress configuration |

## Node Debugging

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl get nodes` | List all nodes | Check node status |
| `kubectl get nodes -o wide` | List nodes with additional details | See node IPs and OS info |
| `kubectl describe node <node-name>` | Get node details | Check node resources and conditions |
| `kubectl top nodes` | Show node resource usage | Monitor CPU/memory consumption |
| `kubectl cordon <node-name>` | Mark node as unschedulable | Prevent new pods on problematic node |
| `kubectl drain <node-name>` | Safely evict pods from node | Prepare node for maintenance |

## Resource Monitoring

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl top pods` | Show pod resource usage | Monitor CPU/memory per pod |
| `kubectl top pods --sort-by=cpu` | Sort pods by CPU usage | Find resource-hungry pods |
| `kubectl top pods --sort-by=memory` | Sort pods by memory usage | Identify memory leaks |
| `kubectl get events` | List recent cluster events | See what happened recently |
| `kubectl get events --sort-by='.lastTimestamp'` | List events sorted by time | Chronological event view |
| `kubectl get events --field-selector involvedObject.name=<pod-name>` | Events for specific object | Debug specific resource issues |

## ConfigMap & Secret Debugging

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl get configmaps` | List ConfigMaps | Check configuration resources |
| `kubectl describe configmap <configmap-name>` | Get ConfigMap details | Verify configuration data |
| `kubectl get secrets` | List secrets | Check secret resources |
| `kubectl describe secret <secret-name>` | Get secret details (without values) | Verify secret metadata |

## Storage Debugging

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl get pv` | List Persistent Volumes | Check storage availability |
| `kubectl get pvc` | List Persistent Volume Claims | Check storage requests |
| `kubectl describe pvc <pvc-name>` | Get PVC details | Debug storage binding issues |

## Namespace Operations

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl get namespaces` | List all namespaces | See available namespaces |
| `kubectl config set-context --current --namespace=<namespace>` | Switch default namespace | Change working context |
| `kubectl get pods -n <namespace>` | List pods in specific namespace | Debug specific namespace |

## Advanced Debugging

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl debug <pod-name> -it --image=busybox` | Create debug container in running pod | Debug running pods without modifying them |
| `kubectl debug <pod-name> -it --image=nicolaka/netshoot --target=<container>` | Debug with network tools targeting specific container | Network debugging in multi-container pods |
| `kubectl debug node/<node-name> -it --image=busybox` | Create debug pod on specific node | Debug node-level issues |
| `kubectl debug <pod-name> -it --copy-to=<new-pod-name>` | Create copy of pod for debugging | Debug without affecting original pod |
| `kubectl debug <pod-name> -it --copy-to=<new-pod-name> --container=<container-name> --image=busybox` | Create pod copy with different container image | Test with different debugging tools |
| `kubectl alpha events <resource-name>` | Get events for specific resource | Enhanced event viewing (alpha feature) |
| `kubectl get all` | List all common resources | Quick cluster overview |
| `kubectl get all -A` | List all resources across namespaces | Full cluster resource view |
| `kubectl apply --dry-run=client -f <file.yaml>` | Validate YAML without applying | Test configurations |
| `kubectl diff -f <file.yaml>` | Show differences before apply | Preview changes |
| `kubectl get pods --field-selector=status.phase=Failed` | List failed pods | Find problematic pods |
| `kubectl delete pod <pod-name> --grace-period=0 --force` | Force delete stuck pod | Remove unresponsive pods |
| `kubectl get pods -o jsonpath='{.items[*].status.containerStatuses[*].restartCount}'` | Get restart counts | Find frequently crashing pods |

## Kubectl Debug Advanced Usage

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl debug <pod-name> -it --image=ubuntu --share-processes` | Debug with process namespace sharing | Access all processes in pod |
| `kubectl debug <pod-name> -it --image=alpine --profile=general` | Use debugging profile | Apply predefined debugging configurations |
| `kubectl debug <pod-name> -it --image=busybox --env="DEBUG=true"` | Add environment variables to debug container | Set debug flags |
| `kubectl debug <pod-name> -it --image=nicolaka/netshoot --set-env="[DEBUG=1]"` | Debug with network troubleshooting tools | Advanced network diagnostics |

## Resource Analysis Commands

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl api-resources` | List all available resource types | Discover available resources |
| `kubectl explain <resource>` | Get resource documentation | Understand resource structure |
| `kubectl explain <resource>.<field>` | Get field-specific documentation | Understand specific configurations |
| `kubectl get <resource> -o custom-columns=<columns>` | Custom output formatting | Create tailored views |
| `kubectl get pods -o jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.status.phase}{"\n"}{end}'` | Custom pod status output | Extract specific information |

## Cluster State Analysis

| Command | Description | Use Case |
|---------|-------------|----------|
| `kubectl cluster-info` | Display cluster information | Check cluster endpoints |
| `kubectl cluster-info dump` | Dump cluster state for debugging | Comprehensive cluster diagnosis |
| `kubectl get componentstatuses` | Check cluster component health | Verify control plane health |
| `kubectl get --raw /healthz` | Check cluster health endpoint | API server health check |
| `kubectl get --raw /metrics` | Get cluster metrics | Performance monitoring |
| `kubectl auth can-i <verb> <resource>` | Check permissions | Debug RBAC issues |
| `kubectl auth can-i --list` | List allowed actions | View current permissions |

## Useful Flags & Options

| Flag | Description | Example |
|------|-------------|---------|
| `-o wide` | Show additional columns | `kubectl get pods -o wide` |
| `-o yaml` | Output in YAML format | `kubectl get pod <name> -o yaml` |
| `-o json` | Output in JSON format | `kubectl get pod <name> -o json` |
| `--watch` | Watch for changes | `kubectl get pods --watch` |
| `-l <label-selector>` | Filter by labels | `kubectl get pods -l app=nginx` |
| `--sort-by=<field>` | Sort output by field | `kubectl get pods --sort-by=.metadata.creationTimestamp` |
| `-n <namespace>` | Specify namespace | `kubectl get pods -n kube-system` |
| `--all-namespaces` or `-A` | All namespaces | `kubectl get pods -A` |

## Common Debugging Workflows

### 1. Pod Not Starting
```bash
kubectl get pods                           # Check pod status
kubectl describe pod <pod-name>            # Look for events and conditions
kubectl logs <pod-name>                    # Check application logs
```

### 2. Service Not Accessible
```bash
kubectl get svc                            # Check service exists
kubectl describe svc <service-name>        # Verify selectors and endpoints
kubectl get endpoints <service-name>       # Confirm pod connections
```

### 3. High Resource Usage
```bash
kubectl top nodes                          # Check node resources
kubectl top pods --sort-by=memory          # Find memory-heavy pods
kubectl describe node <node-name>          # Check node capacity
```

### 4. Networking Issues
```bash
kubectl get pods -o wide                   # Check pod IPs and nodes
kubectl exec -it <pod-name> -- nslookup <service-name>  # Test DNS
kubectl exec -it <pod-name> -- wget -qO- <service-name>:<port>  # Test connectivity
kubectl debug <pod-name> -it --image=nicolaka/netshoot  # Advanced network debugging
```

### 5. Using kubectl debug for Troubleshooting
```bash
# Debug a running pod without modifying it
kubectl debug <pod-name> -it --image=busybox

# Debug with network tools
kubectl debug <pod-name> -it --image=nicolaka/netshoot

# Debug node issues
kubectl debug node/<node-name> -it --image=busybox

# Create a copy of problematic pod for debugging
kubectl debug <pod-name> -it --copy-to=debug-pod --container=app --image=ubuntu
```

### 6. Container and Image Issues
```bash
kubectl describe pod <pod-name>            # Check image pull status and events
kubectl get pods -o jsonpath='{.items[*].spec.containers[*].image}'  # List all container images
kubectl debug <pod-name> -it --image=busybox --target=<container>  # Debug specific container
```