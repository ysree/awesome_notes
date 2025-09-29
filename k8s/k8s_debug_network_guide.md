# Kubernetes Network Debugging Guide
*From User Request to Pod - Complete Network Flow Debugging*

## Overview
This guide walks you through debugging network issues in Kubernetes, following the complete path from external user requests to application pods with practical commands at each layer.

---

## 1. External to Cluster Entry Point

**Check external connectivity to your cluster:**

```bash
# Test external access to NodePort/LoadBalancer
curl -v http://<external-ip>:<port>
nslookup <your-domain>

# Check if DNS resolves correctly
dig <your-domain>
```

---

## 2. Ingress Controller Level

**Debug ingress resources:**

```bash
# List and inspect ingress resources
kubectl get ingress -A
kubectl describe ingress <ingress-name> -n <namespace>

# Check ingress controller pods
kubectl get pods -n ingress-nginx  # or your ingress controller namespace
kubectl logs -f deployment/ingress-nginx-controller -n ingress-nginx

# Test ingress controller service
kubectl get svc -n ingress-nginx
kubectl describe svc ingress-nginx-controller -n ingress-nginx
```

---

## 3. Service Discovery and Load Balancing

**Check service configuration:**

```bash
# List services
kubectl get svc -A
kubectl describe svc <service-name> -n <namespace>

# Check endpoints (actual pod IPs behind service)
kubectl get endpoints <service-name> -n <namespace>
kubectl describe endpoints <service-name> -n <namespace>

# Test service connectivity from within cluster
kubectl run test-pod --image=busybox -it --rm -- /bin/sh
# Inside the test pod:
nslookup <service-name>.<namespace>.svc.cluster.local
wget -qO- http://<service-name>.<namespace>.svc.cluster.local:<port>
```

---

## 4. Network Policies

**Check if network policies are blocking traffic:**

```bash
# List network policies
kubectl get networkpolicy -A
kubectl describe networkpolicy <policy-name> -n <namespace>

# Check if pods have proper labels for network policy selection
kubectl get pods --show-labels -n <namespace>
```

---

## 5. Pod-to-Pod Communication

**Debug pod networking:**

```bash
# Check pod status and IP addresses
kubectl get pods -o wide -n <namespace>
kubectl describe pod <pod-name> -n <namespace>

# Check pod logs
kubectl logs <pod-name> -n <namespace> -f
kubectl logs <pod-name> -n <namespace> --previous  # previous container logs

# Execute into pod for network testing
kubectl exec -it <pod-name> -n <namespace> -- /bin/sh

# Inside the pod, test connectivity:
ping <target-ip>
telnet <target-ip> <port>
curl -v http://<target-ip>:<port>
netstat -tuln  # check listening ports
```

---

## 6. DNS Resolution

**Debug DNS issues:**

```bash
# Test DNS from a test pod
kubectl run test-dns --image=busybox -it --rm -- /bin/sh
# Inside:
nslookup kubernetes.default.svc.cluster.local
nslookup <service-name>.<namespace>.svc.cluster.local

# Check CoreDNS
kubectl get pods -n kube-system | grep coredns
kubectl logs -f deployment/coredns -n kube-system
kubectl describe configmap coredns -n kube-system
```

---

## 7. Node-Level Network Debugging

**Check node networking:**

```bash
# Check node status
kubectl get nodes -o wide
kubectl describe node <node-name>

# Check node's network interfaces (SSH to node)
ip addr show
ip route show
iptables -t nat -L  # check NAT rules

# Check kube-proxy
kubectl get pods -n kube-system | grep kube-proxy
kubectl logs daemonset/kube-proxy -n kube-system
```

---

## 8. CNI and Pod Network Interface

**Debug CNI issues:**

```bash
# Check CNI pods (example for Calico)
kubectl get pods -n kube-system | grep calico
kubectl logs -f daemonset/calico-node -n kube-system

# For other CNIs, check their respective namespaces
kubectl get pods -n kube-system | grep -E "weave|flannel|cilium"

# Check pod's network namespace
kubectl exec <pod-name> -n <namespace> -- ip addr show
kubectl exec <pod-name> -n <namespace> -- ip route show
```

---

## 9. Application-Level Debugging

**Debug the application itself:**

```bash
# Check application logs
kubectl logs <pod-name> -n <namespace> -c <container-name>

# Check application configuration
kubectl describe pod <pod-name> -n <namespace>
kubectl get pod <pod-name> -n <namespace> -o yaml

# Test application health
kubectl exec -it <pod-name> -n <namespace> -- curl localhost:<app-port>/health
```

---

## 10. Comprehensive Network Flow Test

**Create a comprehensive test:**

```bash
# Create a test deployment and service
kubectl create deployment test-app --image=nginx
kubectl expose deployment test-app --port=80 --target-port=80

# Test the full flow
kubectl run curl-test --image=curlimages/curl -it --rm -- /bin/sh
# Inside:
curl -v http://test-app.default.svc.cluster.local
```

---

## 11. Traffic Capture and Analysis

**Use tcpdump for packet analysis:**

```bash
# Capture traffic on a node (requires node access)
sudo tcpdump -i any -n host <pod-ip>

# Or use kubectl plugin like kubectl-sniff (if installed)
kubectl sniff <pod-name> -n <namespace>
```

---

## 12. Common Debugging Commands Summary

```bash
# Quick health check command sequence
kubectl get pods,svc,ingress -A
kubectl get endpoints -A
kubectl describe pod <problematic-pod> -n <namespace>
kubectl logs <problematic-pod> -n <namespace>
kubectl exec -it <pod-name> -n <namespace> -- netstat -tuln

# Network connectivity test from within cluster
kubectl run debug-pod --image=nicolaka/netshoot -it --rm -- /bin/bash
# This provides tools like ping, curl, dig, nslookup, tcpdump, etc.
```

---

## Troubleshooting Checklist

✅ **Service exists and has endpoints**  
✅ **Pod labels match service selectors**  
✅ **Network policies allow traffic**  
✅ **DNS resolution works**  
✅ **Application is listening on correct port**  
✅ **Ingress rules are correct**  
✅ **Firewall/Security groups allow traffic**  
✅ **CNI is functioning properly**

---

## Network Flow Diagram

```
External User → LoadBalancer/NodePort → Ingress Controller → Service → Endpoints → Pod
     ↓              ↓                      ↓               ↓          ↓         ↓
   DNS/HTTP     Node Network        Ingress Rules    kube-proxy   CNI      Application
```

---

## Pro Tips

- **Start from the outside and work inward** - test external connectivity first
- **Use `nicolaka/netshoot` image** for comprehensive network debugging tools
- **Check endpoints** - if a service has no endpoints, the issue is with pod selection
- **Verify labels and selectors** - most service issues stem from label mismatches
- **Test DNS resolution** - many issues are DNS-related in microservices
- **Check both application and access logs** simultaneously
- **Use `kubectl port-forward`** to bypass ingress/service layers for direct pod testing

This systematic approach helps you trace network issues from external requests all the way down to the application pod level.