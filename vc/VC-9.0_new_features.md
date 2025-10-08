# vCenter / vSphere 9.0 Features

## 🎯 Key Confirmed Features in vSphere / vCenter 9.0

Below are features publicly documented in VMware vSphere 9.0 / VCF 9.0 releases.

| Feature                                       | What It Does / Why It Matters                          | Notes / Limitations / Confirmations                               |
| --------------------------------------------- | ------------------------------------------------------ | ----------------------------------------------------------------- |
| **TLS 1.3**                                   | vSphere 9.0 supports TLS 1.3 by default.               | Improves security, reduces handshake latency, better cipher sets. |
| **Advanced Memory Tiering (NVMe)**            | Enables using NVMe devices as a secondary memory tier. | DRAM:NVMe ratio configurable. Some VMs may not be supported.      |
| **Native VPC / VPC APIs in vCenter**          | Manage VPCs/subnets via vCenter APIs/UI.               | More seamless network management.                                 |
| **Improved GPU / vMotion performance**        | GPU workloads have faster vMotion, reducing downtime.  | Important for AI / ML / graphics-heavy VM migrations.             |
| **Support for Monster VMs**                   | Up to 16 TB memory, 960 vCPUs per VM.                  | Helps large workloads like SAP HANA.                              |
| **FIPS 140‑2 / secure defaults**              | Supports FIPS 140‑2 compliant crypto modules.          | Certificate auto-renewal, secure defaults.                        |
| **Unified APIs / OpenAPI / SDK enhancements** | Provides a more programmable interface for automation. | Easier scripting and integrations.                                |
| **Local plug-ins deprecated / removed**       | Local plug-ins removed in vSphere Client SDK 9.0.      | Encourages central / plugin model.                                |

---

## 🧩 Mapping User-Listed Features: Confirmed vs Speculative

| Feature                                          | Status / Evidence                 | Commentary                                                        |
| ------------------------------------------------ | --------------------------------- | ----------------------------------------------------------------- |
| Single API – OVF, vodka manual upload, lease     | Partial / Aspirational            | Unified APIs exist; OVF supported. "Vodka manual upload" unclear. |
| Global Authorization – AD, user roles            | Speculative / Partial             | Role-based access control exists. 30 roles API not documented.    |
| Admission control failover capacity improvements | Speculative                       | HA admission control enhancement plausible, not documented.       |
| System Proxy via Envoy for CAP appliances        | Speculative / Internal            | No public docs; possibly internal Cloud Admin Platform feature.   |
| VPC APIs in vCenter                              | Confirmed                         | Supported natively in vCenter 9.0.                                |
| TLS 1.3                                          | Confirmed                         | Publicly documented.                                              |
| Guest Live Customization                         | Largely existing; may be enhanced | Customization for IP/DNS/etc. already supported.                  |
| NVME Tiering                                     | Confirmed                         | Publicly documented.                                              |
| VM Encryption Key Lifecycle Improvements         | Likely                            | No explicit public doc for 9.0; possible internal improvements.   |
| Custom EVC                                       | Possible / Enhancement            | Granular CPU baseline selection plausible, no public doc.         |
| SEV‑SNP                                          | Possible / Partial                | AMD SNP feature plausible; no definitive public confirmation.     |
| Online Promote Disk                              | Speculative / Preview             | For VDI linked clone promotion; not documented publicly.          |
| vStats                                           | Probable / Partial                | Metrics/telemetry improvements plausible.                         |
| Content Library Storage Migration                | Likely / Partial                  | Logical extension; not explicitly documented.                     |
| License Protection – subscription vs perpetual   | Likely                            | Subscription licensing evolution documented.                      |
| Break ELM / tag & user sync across VC            | Speculative / Internal            | Not publicly documented; internal vmdird service.                 |
| Local Backup                                     | Possibly                          | No explicit public reference.                                     |
| NDC                                              | Speculative / unclear             | Abbreviation unclear in docs.                                     |

---

## 🧠 Detailed Explanations of Highlighted Features

### Memory Tiering over NVMe

* ESXi treats NVMe devices as a secondary memory tier.
* Configurable DRAM:NVMe ratio.
* Not all workloads supported; performance tradeoffs exist.

### TLS 1.3

* Modern, secure encryption by default.
* Reduces handshake latency and improves security.

### Native VPC / VPC APIs in vCenter

* Manage VPCs/subnets directly from vSphere UI.
* Simplifies network management for vSphere admins.

### Monster VM / Scale Increases

* Supports VMs up to 16 TB RAM and 960 vCPUs.
* Useful for large databases, HPC, big data.

### Licensing / Subscription vs Perpetual

* vSphere supports subscription licenses alongside perpetual licenses.
* Improves flexibility for enterprise licensing models.

---

*Note: Some features listed are internal, preview, or speculative and may not appear in public VMware documentation.*
