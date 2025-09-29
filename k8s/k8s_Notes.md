# K8s notes

###### Wide option to view OS-Image, Kernal Vesion, Container Runtime 
`$ kubectl get nodes -o wide`

```
NAME           STATUS   ROLES    AGE     VERSION   INTERNAL-IP   EXTERNAL-IP   OS-IMAGE             KERNEL-VERSION       CONTAINER-RUNTIME
controlplane   Ready    master   2m42s   v1.19.0   172.17.0.28   <none>        Ubuntu 18.04.5 LTS   4.15.0-122-generic   docker://19.3.13
node01         Ready    <none>   2m8s    v1.19.0   172.17.0.29   <none>        Ubuntu 18.04.5 LTS   4.15.0-122-generic   docker://19.3.13
```




# Deployment

###### Create Deployment
`$ kubectl create -f deployment-definition.yml`

###### Record the command in the revision history
`$ kubectl create -f deployment-definition.yml --record`

###### Get all deployment
`$ kubectl get deployments` 

###### Update exisgin deployment
`$ kubectl apply -f deployment-definition.yml` 

###### Set with new image without modifiying the YML file
`$ kubectl set image deployment/myapp-deployment nginx.nginx:1.9.1` 

###### Deployment Status
`$ kubectl rollout status deployment/myapp-deployment` 

###### Deployment history and revisions
`$ kubectl rollout history deployment/myapp-deployment` 

###### Rollback the deployment to previous version
`$ kubectl rollout undo deployment/myapp-deployment` 

###### Deployment rollback to specific version. 
`$ kubectl rollout undo deployment/deployment-name --to-revision=3`

###### Scale Deployment replicas
`$ kubectl scale deployment nginx --replicas=4`

##### Deployment with replicase using imparative
`$ kubectl create deployment webapp --image=kodekloud/webapp-color --replicas=3 --dry-run=client -o yaml > webapp-deployment.yaml`


## Difference between API Gateway & Service Mesh
**API gateways** are for north-south traffic, **service meshes** are for east-west.

## Taints and Tolerations
- **Taints:** are applied to a node to mark it as undesirable for certain pods. 
- **Tolerations**: are applied to a pod to allow the scheduler to place it on a tainted node. 
- **Purpose:** To prevent pods that are not meant to run on a specific node from being scheduled there. 
- **Example**: You could taint a master node to prevent regular application pods from running on it; control plane pods would have the necessary tolerations. 

## Node Affinity
- **Node Affinity** is a property of a pod that specifies a preference for (or requirement to run on) nodes with certain labels. 
- **Purpose:** To ensure pods are scheduled on nodes with specific characteristics, such as a GPU label or being in a specific zone. 
- **Types:** It can be a required affinity (a hard requirement) or a preferred affinity (a preference). 

## Difference betwenn Node Selector, affinity, anti affinity, node affinity, pod affinity

| Feature                     | Scope      | Based on       | Use Case                                |
| --------------------------- | ---------- | -------------- | --------------------------------------- |
| **Node Selector**           | Node       | Node labels    | Simple, exact match rules               |
| **Node Affinity**           | Node       | Node labels    | Advanced node selection with operators  |
| **Pod Affinity**            | Pod-to-Pod | Pod labels     | Place pods together (colocation)        |
| **Pod Anti-Affinity**       | Pod-to-Pod | Pod labels     | Spread pods apart (HA, fault tolerance) |
| **Affinity (General)**      | Concept    | Node/Pod rules | Attraction placement logic              |
| **Anti-Affinity (General)** | Concept    | Node/Pod rules | Repulsion placement logic               |
