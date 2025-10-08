# Kafka on Kubernetes: Reloaded for Fault Tolerance — Summary

# Kafka on Kubernetes – Grab Engineering

## Problems / Challenges

1. Grab ran Kafka on Kubernetes (EKS) with one Kafka cluster per EKS cluster, each broker on its own node.
2. They used NVMe instance store volumes attached to EC2 nodes for Kafka storage (for high I/O).
3. There was a “bootstrap” Kubernetes service plus per-broker services to route client traffic via NLB (Network Load Balancer).
4. When a broker’s node is terminated, the NVMe volume is lost, and the PVC remains bound to a non-existent backing store.
5. Kafka clients in-flight to that broker get connection errors (no graceful demotion) when the broker disappears abruptly.
6. NLB’s target groups remain pointing to the old node (dead) so traffic cannot reach new broker.
7. The PVC binding cannot autorebind to new instance store volume because local volumes are static and tied to original node.
8. Manual intervention is required to delete zombie PVCs, reconfigure NLB target groups, etc.
9. During rolling updates, the NLB may mark new pods unhealthy too quickly or not fast enough, leading to traffic to unavailable pods.
10. The speed of NLB target health checks and NLB update latency causes transient availability issues.
11. EBS was not used initially; NVMe gave performance but lacked portability across node replacement.
12. EBS volume resizing has cooldown periods; shrinking PVs is not supported, causing storage management challenges.

## Solutions / Improvements

13. **AWS Node Termination Handler (NTH)** is used to cordon and drain nodes before they are terminated, triggering graceful shutdown of Kafka pods with SIGTERM.
14. Kafka brokers enable **controlled.shutdown** so leadership is migrated before shutting down.
15. Set **terminationGracePeriodSeconds = 180 seconds** in Strimzi so brokers have sufficient time to migrate partition leadership.
16. For manual instance termination or AWS maintenance events, NTH listens to events (via SQS / EventBridge) and executes the same workflow.
17. Adopt **cluster autoscaler** and double ASG max size so that when a node is drained and broker is Pending, a new node is spun up to host the broker.
18. Use **PodDisruptionBudgets** to limit how many Kafka pods can be evicted simultaneously.
19. Use **AWS Load Balancer Controller (LBC)** with **TargetGroupBinding CRs** to dynamically update NLB target groups to match pod IPs (targetType = ip).
20. Use **Pod Readiness Gates** so Strimzi will not mark pods ready until NLB health checks pass.
21. Tune NLB health check parameters (HealthCheckIntervalSeconds, HealthyThresholdCount) to reduce detection delays.
22. Move from NVMe local volumes to **EBS gp3 volumes** with dynamic provisioning using CSI driver and storage class (volumeBindingMode: WaitForFirstConsumer).
23. EBS volumes can detach and reattach across node replacements, enabling PVCs to bind properly to new broker nodes in the same AZ.
24. Benchmark and verify that EBS gp3 performance is adequate (IOPS/throughput) for workloads.
25. For future improvements: add webhooks to NTH, use Karpenter(Karpenter offers faster, more flexible node provisioning with workload-specific instance selection, but it's primarily for AWS) instead of cluster autoscaler, and autoscale EBS throughput & IOPS based on metrics.


# References
https://engineering.grab.com/kafka-on-kubernetes