1. Install Multi pass on Mac

`brew install --cask multipass`

2. Create a VM with multipass

`multipass launch --name k8s-vm --cpus 2 --mem 4G --disk 20G`

3. Access the VM

`multipass shell k8s-vm`

4. Install minikube

`minikube start --driver=docker`

5. Check the status of minikube

`minikube status`

6. Install kubectl

`curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/darwin/amd64/kubectl"`

`chmod +x ./kubectl`
`sudo mv ./kubectl /usr/local/bin/kubectl`

7. Verify kubectl installation

`kubectl version --client`
`kubectl cluster-info`