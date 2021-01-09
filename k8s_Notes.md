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

