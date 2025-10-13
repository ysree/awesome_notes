# 🌐 VLAN (Virtual Local Area Network) - Quick Notes

## 🔹 What is a VLAN?
- A **Virtual LAN** is a logical subgroup within a physical network.
- It segments network traffic **without requiring physical separation**.
- Allows devices to be grouped together logically, even if they’re not on the same physical switch.

---

## 🔹 Purpose of VLANs
- **Improves security**: Devices in one VLAN can't directly communicate with others unless routed.
- **Reduces broadcast traffic**: Each VLAN is a separate broadcast domain.
- **Enhances network performance** and management.
- **Supports logical groupings** (e.g., by department: HR, IT, Finance).

---

## 🔹 Types of VLANs

| Type              | Description                                                                 |
|-------------------|-----------------------------------------------------------------------------|
| **Default VLAN**  | All switch ports belong to it by default (usually VLAN 1).                  |
| **Data VLAN**     | Carries user-generated traffic (a.k.a. user VLAN).                          |
| **Voice VLAN**    | Carries VoIP traffic; prioritizes voice over data.                          |
| **Management VLAN** | Used for switch management traffic (e.g., SSH, SNMP).                   |
| **Native VLAN**   | Used in trunk links for untagged traffic (usually VLAN 1, but configurable).|

---

## 🔹 VLAN ID Range

- **Normal Range**: 1 to 1005 (Cisco switches)
  - 1 = default
  - 1002–1005 = reserved
- **Extended Range**: 1006 to 4094 (requires VTP in transparent mode on Cisco)

---

## 🔹 VLAN Trunking

- Allows VLAN traffic to travel across multiple switches.
- Uses **802.1Q (dot1q)** tagging to identify VLANs on trunk links.
- **Trunk Port**: Carries multiple VLANs.
- **Access Port**: Belongs to only one VLAN.

---

## 🔹 VLAN Configuration (Cisco IOS Example)

```bash
# Create VLAN
Switch(config)# vlan 10
Switch(config-vlan)# name HR

# Assign port to VLAN
Switch(config)# interface fa0/1
Switch(config-if)# switchport mode access
Switch(config-if)# switchport access vlan 10

# Configure trunk port
Switch(config)# interface fa0/24
Switch(config-if)# switchport mode trunk
Switch(config-if)# switchport trunk native vlan 99
Switch(config-if)# switchport trunk allowed vlan 10,20,99

```

## 🔹 Inter-VLAN Routing

VLANs are logically isolated, so they need a Layer 3 device (router or Layer 3 switch) to communicate.

Two main methods:

Router-on-a-Stick: One physical interface, sub-interfaces per VLAN.

Multilayer Switch: Enables routing within the switch.