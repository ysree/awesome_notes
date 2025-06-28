# Kubernetes Master Resources

The four categories are:

    - Advanced Workload Management
    - Security Enhancements
    - Efficient Cluster Management
    - Networking & Service Discovery
# 1. Advanced Workload Management
1. Affinity and Anti-Affinity
    - **Description**: Control pod placement based on labels and node characteristics.
    - **Use Case**: Ensure that certain pods run on specific nodes or avoid co-locating certain pods.
    - **Example**:
      ```yaml
      apiVersion: v1
      kind: Pod
      metadata:
         name: example-pod
      spec:
         affinity:
            podAffinity:
              requiredDuringSchedulingIgnoredDuringExecution:
                 - labelSelector:
                  matchExpressions:
                     - key: app
                        operator: In
                        values:
                          - frontend
                topologyKey: "kubernetes.io/hostname"
      ```
2. Taints and Tolerations
    - **Description**: Allow nodes to repel certain pods unless they tolerate the taint.
    - **Use Case**: Ensure that only specific pods can be scheduled on certain nodes.
    - **Example**:
      ```yaml
      apiVersion: v1
      kind: Pod
      metadata:
         name: example-pod
      spec:
         tolerations:
         - key: "example-key"
           operator: "Exists"
           effect: "NoSchedule"
      ```
3. Pod Disruption Budgets (PDB)
    - **Description**: Limit the number of concurrent disruptions (e.g., voluntary evictions) to a set of pods.
    - **Use Case**: Ensure a certain level of availability during maintenance or upgrades.
    - **Example**:
      ```yaml
      apiVersion: policy/v1beta1
      kind: PodDisruptionBudget
      metadata:
         name: example-pdb
      spec:
         minAvailable: 1
         selector:
            matchLabels:
               app: frontend
      ```
4. Using Init Containers for Setup Scripts
    - **Description**: Init containers are specialized containers that run before app containers in a pod.
    - **Use Case**: Perform setup tasks such as database migrations or configuration before the main application starts.
    - **Example**:
      ```yaml
      apiVersion: v1
      kind: Pod
      metadata:
         name: example-pod
      spec:
         initContainers:
         - name: init-myservice
           image: myservice:latest
           command: ['sh', '-c', 'setup.sh']
         containers:
         - name: myservice
           image: myservice:latest
      ```
5. Pod Topology Spread Constraints
    - **Description**: Ensure that pods are evenly distributed across failure domains (e.g., nodes, zones).
    - **Use Case**: Improve availability by preventing too many pods from being scheduled on the same node or zone.
    - **Example**:
      ```yaml
      apiVersion: apps/v1
      kind: Deployment
      metadata:
         name: example-deployment
      spec:
         template:
            spec:
               topologySpreadConstraints:
               - maxSkew: 1
                 topologyKey: "kubernetes.io/hostname"
                 whenUnsatisfiable: DoNotSchedule
                 labelSelector:
                    matchLabels:
                       app: frontend
      ```
6. Custom Scheduler
    - **Description**: A custom scheduler allows you to implement your own scheduling logic for pods.
    - **Use Case**: When the default Kubernetes scheduler does not meet your specific requirements.
    - **Example**:
      ```yaml
      apiVersion: scheduling.k8s.io/v1
      kind: PriorityClass
      metadata:
         name: high-priority
      spec:
         value: 1000000
         globalDefault: false
         description: "This priority class should be used for high-priority pods."
      ```
7. Horizontal Pod Autoscaler (HPA) Based on Custom Metrics
    - **Description**: Automatically scale the number of pod replicas based on custom metrics (e.g., CPU usage, memory usage).
    - **Use Case**: Dynamically adjust the number of pods in a deployment based on real-time demand.
    - **Example**:
      ```yaml
      apiVersion: autoscaling/v2beta2
      kind: HorizontalPodAutoscaler
      metadata:
         name: example-hpa
      spec:
         scaleTargetRef:
            apiVersion: apps/v1
            kind: Deployment
            name: example-deployment
         minReplicas: 1
         maxReplicas: 10
         metrics:
         - type: Pods
           pods:
            metric:
               name: http_requests
            target:
               average: 100
      ```
8. Volume Snapshotting for Stateful Applications
    - **Description**: Create snapshots of persistent volumes to back up stateful applications.
    - **Use Case**: Backup and restore stateful applications like databases.
    - **Example**:
      ```yaml
      apiVersion: snapshot.storage.k8s.io/v1
      kind: VolumeSnapshot
      metadata:
         name: example-snapshot
      spec:
         source:
            persistentVolumeClaimName: example-pvc
         volumeSnapshotClassName: example-snapshot-class
      ```


