# Kubernetes Multi-Container Debugging Guide

## Table of Contents
- [Overview](#overview)
- [Core Debugging Commands](#core-debugging-commands)
- [Common Multi-Container Issues](#common-multi-container-issues)
- [Advanced Debugging Techniques](#advanced-debugging-techniques)
- [Debugging Tools](#debugging-tools)
- [Multi-Container Patterns](#multi-container-patterns)
- [Troubleshooting Checklist](#troubleshooting-checklist)
- [Best Practices](#best-practices)

## Overview

Multi-container pods in Kubernetes present unique debugging challenges since multiple processes must coordinate within the same pod. Containers share the same network namespace, storage volumes, and lifecycle, making inter-container communication and dependency management critical.

## Core Debugging Commands

### Pod Status and Information
```bash
# Get basic pod information
kubectl get pods -o wide
kubectl get pods <pod-name> -o yaml

# Detailed pod description with events
kubectl describe pod <pod-name>

# Get events sorted by timestamp
kubectl get events --sort-by='.lastTimestamp'
kubectl get events --field-selector involvedObject.name=<pod-name>
```

### Container Logs
```bash
# Logs from specific container
kubectl logs <pod-name> -c <container-name>

# Previous container instance logs
kubectl logs <pod-name> -c <container-name> --previous

# All containers in pod
kubectl logs <pod-name> --all-containers=true

# Follow logs in real-time
kubectl logs <pod-name> -c <container-name> -f

# Logs with timestamps
kubectl logs <pod-name> -c <container-name> --timestamps=true
```

### Container Access
```bash
# Execute into specific container
kubectl exec -it <pod-name> -c <container-name> -- /bin/bash
kubectl exec -it <pod-name> -c <container-name> -- sh

# Run single commands
kubectl exec <pod-name> -c <container-name> -- ps aux
kubectl exec <pod-name> -c <container-name> -- env
```

## Common Multi-Container Issues

### 1. Init Container Failures
Init containers must complete successfully before app containers start.

**Debugging Steps:**
```bash
# Check init container status
kubectl describe pod <pod-name>

# View init container logs
kubectl logs <pod-name> -c <init-container-name>

# Common issues:
# - Network connectivity problems
# - Missing permissions
# - Dependency services not ready
# - Volume mount issues
```

### 2. Sidecar Communication Problems
Containers share localhost but may have communication issues.

**Debugging Steps:**
```bash
# Test connectivity between containers
kubectl exec -it <pod-name> -c <main-container> -- curl localhost:<sidecar-port>

# Check what's listening on ports
kubectl exec -it <pod-name> -c <sidecar-container> -- netstat -tlnp

# Verify sidecar is ready
kubectl exec -it <pod-name> -c <sidecar-container> -- ps aux
```

### 3. Resource Contention
Multiple containers competing for limited resources.

**Debugging Steps:**
```bash
# Check resource usage
kubectl top pod <pod-name> --containers

# View resource limits and requests
kubectl describe pod <pod-name> | grep -A 5 "Limits\|Requests"

# Check for OOMKilled containers
kubectl describe pod <pod-name> | grep -i oom
```

### 4. Volume Sharing Issues
Problems with shared volumes between containers.

**Debugging Steps:**
```bash
# Check volume mounts in each container
kubectl describe pod <pod-name>

# Verify files in shared volumes
kubectl exec -it <pod-name> -c <container-1> -- ls -la /shared-volume
kubectl exec -it <pod-name> -c <container-2> -- ls -la /shared-volume

# Check permissions
kubectl exec -it <pod-name> -c <container-1> -- ls -la /shared-volume
```

## Advanced Debugging Techniques

### Network Debugging
```bash
# Test inter-container communication
kubectl exec -it <pod-name> -c <container-1> -- ping localhost
kubectl exec -it <pod-name> -c <container-1> -- curl localhost:8080/health

# Check DNS resolution
kubectl exec -it <pod-name> -c <container-name> -- nslookup kubernetes.default

# View network interfaces
kubectl exec -it <pod-name> -c <container-name> -- ip addr show
```

### Process and Service Debugging
```bash
# List all processes in the pod
kubectl exec -it <pod-name> -c <container-name> -- ps auxf

# Check listening ports
kubectl exec -it <pod-name> -c <container-name> -- netstat -tlnp
kubectl exec -it <pod-name> -c <container-name> -- ss -tlnp

# Monitor system resources
kubectl exec -it <pod-name> -c <container-name> -- top
kubectl exec -it <pod-name> -c <container-name> -- free -h
```

### File System Debugging
```bash
# Check disk usage
kubectl exec -it <pod-name> -c <container-name> -- df -h

# Verify volume mounts
kubectl exec -it <pod-name> -c <container-name> -- mount | grep volume

# Check file permissions
kubectl exec -it <pod-name> -c <container-name> -- ls -la /path/to/shared/files
```

## Debugging Tools

### Ephemeral Debug Containers (K8s 1.23+)
```bash
# Create debug container
kubectl debug <pod-name> -it --image=busybox --target=<container-name>

# Debug with network utilities
kubectl debug <pod-name> -it --image=nicolaka/netshoot
```

### Port Forwarding
```bash
# Forward pod ports to local machine
kubectl port-forward <pod-name> 8080:8080

# Forward specific container port
kubectl port-forward pod/<pod-name> 8080:8080
```

### File Operations
```bash
# Copy files from pod
kubectl cp <pod-name>:/path/to/file /local/path -c <container-name>

# Copy files to pod
kubectl cp /local/file <pod-name>:/path/to/destination -c <container-name>
```

### Resource Monitoring
```bash
# Monitor resource usage
kubectl top pod <pod-name> --containers

# Get detailed resource info
kubectl describe pod <pod-name> | grep -A 10 "Containers:"
```

## Multi-Container Patterns

### 1. Sidecar Pattern
Main application with helper container (logging, monitoring, proxy).

**Common Issues:**
- Sidecar not ready when main container starts
- Communication failures between containers
- Resource competition

**Debug Commands:**
```bash
# Check startup order
kubectl describe pod <pod-name>

# Verify sidecar health
kubectl logs <pod-name> -c <sidecar-container>
kubectl exec -it <pod-name> -c <sidecar-container> -- curl localhost:8080/health
```

### 2. Ambassador Pattern
Proxy container handling external service communication.

**Common Issues:**
- Proxy configuration errors
- Network connectivity problems
- Service discovery failures

**Debug Commands:**
```bash
# Test proxy connectivity
kubectl exec -it <pod-name> -c <main-container> -- curl localhost:<proxy-port>

# Check proxy logs
kubectl logs <pod-name> -c <ambassador-container>
```

### 3. Adapter Pattern
Container that transforms/adapts data for the main application.

**Common Issues:**
- Data transformation errors
- Volume sharing problems
- Timing issues

**Debug Commands:**
```bash
# Check adapter processing
kubectl logs <pod-name> -c <adapter-container>

# Verify shared data
kubectl exec -it <pod-name> -c <adapter-container> -- ls -la /shared/data
```

## Troubleshooting Checklist

### Basic Checks
- [ ] Pod is in Running state
- [ ] All containers are ready
- [ ] No recent restarts
- [ ] Resource limits are adequate
- [ ] Events show no errors

### Container-Specific Checks
- [ ] Container logs show no errors
- [ ] Container processes are running
- [ ] Required ports are listening
- [ ] Environment variables are correct
- [ ] Volume mounts are successful

### Inter-Container Communication
- [ ] Containers can reach each other via localhost
- [ ] Shared volumes are accessible
- [ ] No port conflicts
- [ ] Startup order is correct
- [ ] Dependencies are ready

### Network and DNS
- [ ] DNS resolution works
- [ ] External services are reachable
- [ ] Service discovery functions
- [ ] Network policies allow traffic
- [ ] Firewall rules are correct

## Best Practices

### Design Principles
1. **Single Responsibility**: Each container should have one primary responsibility
2. **Loose Coupling**: Minimize dependencies between containers
3. **Health Checks**: Implement proper readiness and liveness probes
4. **Graceful Shutdown**: Handle termination signals properly

### Monitoring and Observability
```bash
# Add health check endpoints
# Use structured logging
# Implement metrics collection
# Set up distributed tracing
```

### Resource Management
```yaml
# Example resource configuration
resources:
  requests:
    memory: "64Mi"
    cpu: "250m"
  limits:
    memory: "128Mi"
    cpu: "500m"
```

### Configuration Management
- Use ConfigMaps for non-sensitive configuration
- Use Secrets for sensitive data
- Implement configuration validation
- Use init containers for setup tasks

### Error Handling
- Implement proper error logging
- Use appropriate exit codes
- Handle container dependencies gracefully
- Implement retry mechanisms where appropriate

---

## Quick Reference Commands

```bash
# Essential debugging commands
kubectl get pods -o wide
kubectl describe pod <pod-name>
kubectl logs <pod-name> -c <container-name>
kubectl exec -it <pod-name> -c <container-name> -- /bin/bash
kubectl port-forward <pod-name> 8080:8080
kubectl debug <pod-name> -it --image=busybox
```

Remember: Multi-container debugging requires understanding both individual container health and inter-container relationships. Always check logs, resource usage, and communication paths between containers.