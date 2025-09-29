# Kubernetes Security Best Practices

## 1. Cluster Security

* **Use Role-Based Access Control (RBAC):** Grant least-privilege access to users and service accounts. Define roles and role bindings to control resource access.
* **Secure API Server:** Enable HTTPS with valid TLS certificates. Use API auditing to log requests. Disable anonymous access.
* **Enable Authentication & Authorization:** Use tokens, OIDC, or client certificates. Combine with RBAC.
* **Patch & Upgrade Regularly:** Keep Kubernetes versions and components updated.
* **Limit Access to etcd:** Ensure etcd is accessible only from control plane nodes. Encrypt etcd data at rest.

---

## 2. Node Security

* Run minimal OS (Ubuntu, Bottlerocket, Container-Optimized OS).
* Restrict SSH access; use bastion hosts or VPN.
* Ensure container runtime (Docker, containerd) is up to date.
* Enable kernel hardening: seccomp, AppArmor, SELinux.

---

## 3. Workload Security

* **Use Namespaces** to isolate workloads.
* **Limit Pod Privileges:** Run containers as non-root, avoid privileged containers, use `securityContext`.
* **Network Policies:** Use Kubernetes NetworkPolicies. Implement default deny-all and allow only required traffic.
* **Resource Limits:** Set `requests` and `limits` for CPU and memory.
* **Secrets Management:** Use Kubernetes Secrets or integrate with Vault/cloud secret managers.
* **Pod Security Admission:** Use Pod Security Standards (Privileged, Baseline, Restricted).

---

## 4. Image Security

* Use trusted images; avoid `latest` tags in production.
* Scan images for vulnerabilities (Trivy, Clair, Anchore, Aqua Security).
* Ensure container images are immutable.
* Use minimal base images (slim or distroless).

---

## 5. Network Security

* Limit ingress and egress traffic to essential ports.
* Use Ingress controllers with TLS.
* Implement mTLS between services via service mesh (Istio, Linkerd, Consul).
* Control node-level and cluster-level access with firewall policies.

---

## 6. Logging and Monitoring

* **Centralized Logging:** ELK, Fluentd, Loki.
* **Monitoring & Alerts:** Prometheus/Grafana for CPU, memory, network, and security metrics.
* **Audit Policies:** Enable Kubernetes audit logging to track access and changes.

---

## 7. Backup and Disaster Recovery

* Backup etcd and persistent volumes regularly.
* Test cluster and application recovery processes periodically.

---

## 8. Operational Best Practices

* Separate dev, QA, and prod environments using namespaces.
* Use Pod Disruption Budgets to ensure high availability during upgrades.
* Limit API rate to protect against abuse or DoS attacks.
* Use Admission Controllers to validate requests and enforce policies at creation time.
