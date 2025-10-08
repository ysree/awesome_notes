# vCenter Services – Explanation and Responsibilities

## Running Services

| Service                          | Explanation / Responsibility                                                                                                  |
| -------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **applmgmt**                     | Manages appliance-level services and configurations. Responsible for starting/stopping vCenter appliance management services. |
| **lookupsvc**                    | VMware Lookup Service – provides authentication, certificate, and service discovery for SSO-enabled vCenter environments.     |
| **lwsmd**                        | License Watchdog / management daemon – monitors licensing and compliance.                                                     |
| **observability**                | Provides telemetry, metrics, and logging for monitoring vCenter and its services.                                             |
| **observability-vapi**           | vAPI endpoint for observability – exposes metrics and telemetry via REST APIs.                                                |
| **pschealth**                    | Provides health monitoring for vCenter's scheduled tasks and processes.                                                       |
| **vicm**                         | VMware Infrastructure Component Manager – manages internal appliance services.                                                |
| **vmafdd**                       | VMware Appliance File Distribution Daemon – handles configuration and updates for vCenter appliance files.                    |
| **vmcad**                        | VMware Certificate Authority Daemon – issues, renews, and validates certificates for vCenter SSO and services.                |
| **vmdird**                       | VMware Directory Service – manages SSO directory, users, groups, and roles.                                                   |
| **vmonapi**                      | VMware Service Monitor API – monitors and provides API access to service status.                                              |
| **vmware-analytics**             | Analytics service for vCenter – collects and aggregates data for insights and dashboards.                                     |
| **vmware-certificateauthority**  | Manages certificates, signing, and trust within vCenter environment.                                                          |
| **vmware-certificatemanagement** | Provides certificate management tasks like renewal, replacement, and revocation.                                              |
| **vmware-cis-license**           | Handles license key management, compliance checks, and reporting for vCenter and ESXi hosts.                                  |
| **vmware-content-library**       | Manages content library items like VM templates, ISO images, and scripts.                                                     |
| **vmware-eam**                   | VMware ESX Agent Manager – manages agent lifecycle on ESXi hosts.                                                             |
| **vmware-envoy**                 | Acts as a system proxy for CAP appliances; handles inter-service communication securely.                                      |
| **vmware-hvc**                     | Core vCenter service (handles basic appliance host operations).                                                               |
| **vc**                           | vCenter Server main process – coordinates all vCenter services and operations.                                                |
| **vmware-infraprofile**          | Manages infrastructure profiles and configuration compliance for ESXi hosts.                                                  |
| **vmware-perfcharts**            | Provides performance charts and metrics in vSphere Client.                                                                    |
| **vmware-pod**                   | Manages resource pools and pod-level operations (clusters or nested clusters).                                                |
| **vmware-postgres-archiver**     | Archives and maintains PostgreSQL database logs for vCenter.                                                                  |
| **vmware-rhttpproxy**            | Reverse proxy service for HTTP(s) traffic routing between services and the UI/API endpoints.                                  |
| **vmware-sca**                   | Service Control Agent – responsible for starting/stopping services and monitoring health.                                     |
| **vmware-sps**                   | Storage Policy Service – manages SPBM policies and compliance.                                                                |
| **vmware-statsmonitor**          | Collects statistics for monitoring and health dashboards.                                                                     |
| **vmware-stsd**                  | Service Trust Service Daemon – manages security and trust relationships for vCenter services.                                 |
| **vmware-topologysvc**           | Maintains vCenter object topology, inventory, and relationships.                                                              |
| **vmware-trustmanagement**       | Manages certificates, trust chains, and certificate authorities.                                                              |
| **vmware-updatengr**             | Next-generation update manager – applies patches and updates to vCenter and ESXi hosts.                                       |
| **vmware-vapi-endpoint**         | Endpoint for vAPI – provides REST API access to vCenter services.                                                             |
| **vmware-vdtc**                  | VMware Distributed Transaction Coordinator – handles database transactions reliably across services.                          |
| **vmware-vmon**                  | Core service monitor for vCenter appliance.                                                                                   |
| **vmware-vm**                    | Base service for managing virtual machine operations.                                                                         |
| **vmware-vpostgres**             | PostgreSQL database server for vCenter appliance.                                                                             |
| **vmware-vpxd**                  | Main vCenter Server process – coordinates inventory, workflow, and operations.                                                |
| **vmware-vpxd-svcs**             | Support services for vpxd – includes background tasks and workflows.                                                          |
| **vmware-vsan-health**           | Monitors vSAN cluster health, performance, and compliance.                                                                    |
| **vmware-vsm**                   | VMware vSphere Storage Management service – manages storage policies and operations.                                          |
| **vsphere-ui**                   | vSphere Client – HTML5 web UI for managing vCenter and ESXi infrastructure.                                                   |
| **vstats**                       | Collects performance statistics for hosts and VMs.                                                                            |
| **vtsdb**                        | Timeseries database for storing vCenter metrics.                                                                              |
| **wcp**                          | Workload Control Plane – enables vSphere with Tanzu (Kubernetes) features.                                                    |

## Stopped Services

| Service                 | Explanation / Responsibility                                                |
| ----------------------- | --------------------------------------------------------------------------- |
| **vincam**              | Integration service for CAM – likely related to VM monitoring/analytics.    |
| **vmware-imagebuilder** | Builds ESXi images (custom ISO builds for hosts).                           |
| **vmware-netdumper**    | Network dump service – collects network diagnostic logs.                    |
| **vmware-rbd-watchdog** | Monitors RBD (Remote Block Device) services.                                |
| **vmware-vcha**         | VMware vCenter High Availability – manages HA active/passive/witness nodes. |
