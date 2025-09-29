# Kubernetes Deployment

[Kubernetes Deployment](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/) is a higher-level abstraction that manages ReplicaSets and Pods to ensure the desired state of your application is maintained. It provides features like rolling updates, rollbacks, and scaling.


- **replicas: 3**
  - Number of pod replicas to run
  - default is 1
  - More replicas = more availability, but also more resource usage
  - Fewer replicas = less resource usage, but also less availability
  - Creates ReplicaSets to manage pods

- What is the difference between Deployment, ReplicaSet, and Pod?
  - **Deployment** 
    - A higher-level abstraction that manages ReplicaSets.
    - Manages the desired state of your application
    - Handles updates, rollbacks, and scaling
    - Creates and manages ReplicaSets
  - **ReplicaSet**
    - Ensures a specified number of pod replicas are running at any given time
    - Created and managed by Deployments
    - If a Pod crashes or is deleted, the ReplicaSet creates a new one
    - Directly manages Pods
  - **Pod**
    - The smallest deployable unit in Kubernetes
    - Represents a single instance of a running process in your cluster
    - Contains one or more containers (usually one) 
    - Managed by ReplicaSets (and thus indirectly by Deployments) 

- **strategy: RollingUpdate**
  - How updates to the Deployment are performed
  - RollingUpdate gradually replaces old pods with new ones
  - Other option is **Recreate**, which kills all old pods before starting new ones. Hence no two versions run simultaneously. No downtime, but less availability during update.
  - **maxSurge: 1** allows one extra pod above the desired number of replicas during the update
  - **maxUnavailable: 1** allows one less pod than the desired number of replicas during the update

- what is **serviceAccount**?
  - A service account is a type of non-human account that, in Kubernetes, provides a distinct identity in a Kubernetes cluster
  - Used to authenticate to the Kubernetes API server and other services
  - By default, Pods run with the "default" service account in their namespace
  - You can create custom service accounts with specific permissions

- **revisionHistoryLimit: 5**
  - How many old ReplicaSet revisions are kept in history
  - default is 10
  - Keeping too few (**like 0**) means you lose rollback safety.
  - Keeping too many old ReplicaSets can clutter the cluster.

- **restartPolicy**
    - **Always**: Always restart the container if it stops (default for Deployments)
    - **OnFailure**: Restart the container only if it exits with a non-zero status
    - **Never**: Never restart the container, regardless of its exit status

- **terminationGracePeriodSeconds**
    - Time in seconds given to a pod to terminate gracefully before being forcefully killed
    - Default is 30 seconds
    - If your application needs more time to clean up resources, you can increase this value

- **dnsPolicy: ClusterFirst**
    - Determines how DNS is configured for the pod
    - **ClusterFirst**: Use the **cluster's DNS** service (default for most pods)
    - **Default**: Use the **node's DNS** settings
    - **None**: No DNS configuration is applied to the pod

- **securityContext**
    - Defines security settings for the pod or containers
    - **runAsUser**: Specifies the user ID to run the container process
      1000 # Run as user ID 1000 meaning non-root user
      3000 # Run as group ID 3000
      2000 # All files in volumes mounted by the pod will be owned by group ID
    - **runAsGroup**: Specifies the group ID to run the container process
    - **fsGroup**: Specifies a group ID for all files in volumes mounted by the pod

- **tolerations**
    - Allows the pod to be scheduled on nodes with matching taints
    - key: "env" - The taint key to match
    - operator: "Equal" - The operator to use for matching (Equal, Exists)
    - value: "prod" - The taint value to match
    - effect: "NoSchedule" - The effect of the taint (**NoSchedule, PreferNoSchedule, NoExecute**)

      - **NoSchedule** - The pod will not be scheduled on nodes with this taint.
      - **PreferNoSchedule** - The pod will be scheduled on nodes without this taint if possible.
      - **NoExecute** - The pod will be evicted from nodes with this taint.

- **affinity**
    - Defines rules for pod scheduling based on labels and topology
    - **podAntiAffinity**: Ensures that pods with the same label do not run on the same node
    - **requiredDuringSchedulingIgnoredDuringExecution**: Hard requirement for scheduling
    - **labelSelector**: Selects pods with the specified labels
    - **topologyKey**: "kubernetes.io/hostname" - Ensures pods are spread across different nodes

- **topologySpreadConstraints**
    - Ensures even distribution of pods across specified topology domains (e.g., zones)
    - **maxSkew**: 1 - Maximum allowed difference in the number of pods between topology domains
    - **topologyKey**: "zone" - The key to use for topology domains (e.g., availability zones)

- **imagePullSecrets**
    - Specifies secrets for pulling images from private registries
    - regcred is the name of the secret created using `kubectl create secret docker-registry`

- **imagePullPolicy**
    - Specifies when to pull images for the container
    - **Always**: Always pull the image (even if it is cached)
    - **IfNotPresent**: Pull the image only if it is not already present (default)
    - **Never**: Never pull the image

- **initContainers**
    - Containers that run before the main containers in a pod
    - Used for initialization tasks, such as setting up files or waiting for services
    - Can share the same network namespace as the main containers

- **resources**
    - Defines resource requests and limits for containers
    - **requests**: Minimum resources required for the container to run
      - cpu: "250m" - 250 millicores of CPU
      - memory: "256Mi" - 256 MiB of memory
    - **limits**: Maximum resources the container can use
      - cpu: "500m" - 500 millicores of CPU
      - memory: "512Mi" - 512 MiB of memory
    
    - Setting appropriate resource requests and limits helps the Kubernetes scheduler make informed decisions about pod placement and ensures that containers do not consume excessive resources, which could lead to resource contention and instability in the cluster.

- **livenessProbe**
    - Checks if the container is running
    - If the liveness probe fails, the container is restarted
    - httpGet: Performs an HTTP GET request to the specified path and port
    - initialDelaySeconds: Time to wait before starting the probe
    - periodSeconds: Frequency of the probe 
- **readinessProbe**
    - Checks if the container is ready to serve traffic
    - If the readiness probe fails, the pod is removed from service endpoints
    - httpGet: Performs an HTTP GET request to the specified path and port
    - initialDelaySeconds: Time to wait before starting the probe
    - periodSeconds: Frequency of the probe
- **startupProbe**
    - Checks if the application within the container has started
    - If the startup probe fails, the container is restarted
    - Useful for applications that take a long time to start
    - httpGet: Performs an HTTP GET request to the specified path and port
    - failureThreshold: Number of consecutive failures before considering the probe failed
    - periodSeconds: Frequency of the probe 
- **volumeMounts**
    - Defines where volumes are mounted inside the container
    - name: Name of the volume to mount
    - mountPath: Path inside the container where the volume is mounted
    - readOnly: If true, the volume is mounted as read-only
- **volumes**
    - Defines the volumes that can be mounted by containers in the pod
  - **configMap**: Mounts a ConfigMap as a volume
      - name: Name of the ConfigMap to mount
  - **secret**: Mounts a Secret as a volume
      - secretName: Name of the Secret to mount
  - **persistentVolumeClaim**: Mounts a PersistentVolumeClaim as a volume
      - claimName: Name of the PersistentVolumeClaim to mount
  - **emptyDir**: Creates an empty directory volume that is deleted when the pod is removed

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: sample-deployment
  namespace: demo-namespace
  labels:
    app: sample-app
    tier: backend
  annotations:
    description: "Comprehensive Deployment YAML with multiple options"
spec:
  replicas: 3
  revisionHistoryLimit: 5       
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
  selector:
    matchLabels:
      app: sample-app
  template:
    metadata:
      labels:
        app: sample-app
        version: v1
      annotations:
        sidecar.istio.io/inject: "true"
    spec:
      serviceAccountName: sample-sa
      restartPolicy: Always
      terminationGracePeriodSeconds: 30
      dnsPolicy: ClusterFirst
      securityContext:
        runAsUser: 1000
        runAsGroup: 3000
        fsGroup: 2000
      nodeSelector:
        disktype: ssd
      tolerations:
        - key: "env"
          operator: "Equal"
          value: "prod"
          effect: "NoSchedule"
      affinity:
        podAntiAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            - labelSelector:
                matchExpressions:
                  - key: app
                    operator: In
                    values: ["sample-app"]
              topologyKey: "kubernetes.io/hostname"
      topologySpreadConstraints:
        - maxSkew: 1
          topologyKey: "zone"
          whenUnsatisfiable: DoNotSchedule
          labelSelector:
            matchLabels:
              app: sample-app
      imagePullSecrets:
        - name: regcred

      # Init Containers
      initContainers:
        - name: init-db
          image: busybox
          command: ["sh", "-c", "echo Waiting for DB; sleep 5"]
          resources:
            requests:
              cpu: "50m"
              memory: "64Mi"
            limits:
              cpu: "100m"
              memory: "128Mi"

      containers:
        # Main container
        - name: app-container
          image: nginx:1.21
          imagePullPolicy: IfNotPresent
          ports:
            - name: http
              containerPort: 80
              protocol: TCP
          env:
            - name: ENVIRONMENT
              value: "production"
            - name: CONFIG_VALUE
              valueFrom:
                configMapKeyRef:
                  name: sample-config
                  key: config-key
            - name: SECRET_VALUE
              valueFrom:
                secretKeyRef:
                  name: sample-secret
                  key: password
          volumeMounts:
            - name: config-volume
              mountPath: /etc/config
            - name: secret-volume
              mountPath: /etc/secret
              readOnly: true
            - name: data-volume
              mountPath: /var/data
          resources:
            requests:
              cpu: "250m"
              memory: "256Mi"
            limits:
              cpu: "500m"
              memory: "512Mi"
          livenessProbe:
            httpGet:
              path: /healthz
              port: 80
            initialDelaySeconds: 10
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: /ready
              port: 80
            initialDelaySeconds: 5
            periodSeconds: 5
          startupProbe:
            httpGet:
              path: /startup
              port: 80
            failureThreshold: 30
            periodSeconds: 10

        # Sidecar container
        - name: log-sidecar
          image: busybox
          command: ["sh", "-c", "tail -n+1 -f /var/log/app.log"]
          volumeMounts:
            - name: data-volume
              mountPath: /var/log

      # Volumes
      volumes:
        - name: config-volume
          configMap:
            name: sample-config
        - name: secret-volume
          secret:
            secretName: sample-secret
        - name: data-volume
          persistentVolumeClaim:
            claimName: sample-pvc
        - name: temp-volume
          emptyDir: {}
```


---
