# Terraform Notes

## 1. What is Terraform

* Terraform is an **Infrastructure as Code (IaC)** tool developed by HashiCorp.
* Allows **provisioning, managing, and versioning cloud and on-premises resources** using declarative configuration files.
* Supports multiple providers: AWS, Azure, GCP, Kubernetes, VMware, etc.

## 2. Key Concepts

| Term        | Description                                                             |
| ----------- | ----------------------------------------------------------------------- |
| Provider    | Plugin to interact with cloud or external APIs (e.g., AWS, Azure).      |
| Resource    | Infrastructure object you want to create/manage (e.g., EC2, S3 bucket). |
| Data Source | Read-only access to external resources (e.g., existing VPC).            |
| Module      | Reusable Terraform code block to group resources logically.             |
| Variable    | Input values that can be customized for reusability.                    |
| Output      | Values exported by Terraform to be used elsewhere.                      |
| State       | File storing mapping between configuration and real infrastructure.     |
| Backend     | Storage location for the Terraform state (local, S3, Consul, etc.).     |
| Plan        | Execution plan showing what Terraform will do.                          |
| Apply       | Command that creates, updates, or destroys resources.                   |
| Destroy     | Command to delete all resources managed by Terraform.                   |

## 3. Terraform Workflow

1. **Write Configuration**: Use `.tf` files to define providers, resources, variables, modules.
2. **Initialize**: `terraform init` to install provider plugins and set up backend.
3. **Plan**: `terraform plan` to preview changes without applying.
4. **Apply**: `terraform apply` to create/update resources.
5. **Destroy**: `terraform destroy` to remove resources.
6. **State Management**: Keep track of deployed resources via `terraform.tfstate`.

## 4. Terraform Configuration Example

```hcl
provider "aws" {
  region = "us-east-1"
}

variable "instance_type" {
  default = "t2.micro"
}

resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = var.instance_type
  tags = {
    Name = "TerraformDemo"
  }
}

output "instance_id" {
  value = aws_instance.web.id
}
```

## 5. Modules

* **Purpose:** Reusable, composable Terraform code.
* **Structure:**

  ```
  module/
    main.tf
    variables.tf
    outputs.tf
  ```
* **Usage:**

```hcl
module "network" {
  source = "./modules/network"
  vpc_cidr = "10.0.0.0/16"
}
```

## 6. State Management

* **Local State:** Stored as `terraform.tfstate`.
* **Remote State:** Stored in S3, Consul, etc., with locking support.
* **Commands:**

  * `terraform state list` – list resources.
  * `terraform state show <resource>` – show details.
  * `terraform import <resource> <id>` – import existing resources into state.

## 7. Terraform Commands

| Command            | Purpose                        |
| ------------------ | ------------------------------ |
| terraform init     | Initialize project & providers |
| terraform plan     | Show execution plan            |
| terraform apply    | Apply changes                  |
| terraform destroy  | Delete all managed resources   |
| terraform fmt      | Format configuration files     |
| terraform validate | Validate configuration syntax  |
| terraform graph    | Generat                        |
