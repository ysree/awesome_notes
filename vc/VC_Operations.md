# vCenter Operations Categorization

## 📚 Table of Contents
- [🧩 1. VM Lifecycle Management](#vm-lifecycle-management)
- [🧱 2. vApp Operations](#vapp-operations)
- [🖥️ 3. VM Configuration / Reconfiguration](#vm-configuration-reconfiguration)
- [🗂️ 4. Inventory & Folder Management](#inventory-folder-management)
- [☁️ 5. Cluster & Host Operations](#cluster-host-operations)
- [🧰 6. Datastore & Storage Pod Operations](#datastore-storage-pod-operations)
- [🧮 7. Resource Pool Management](#resource-pool-management)
- [🔗 8. Networking (DVS, DVPG, Host networking)](#networking-dvs-dvpg-host-networking)
- [📚 9. Content Library Operations](#content-library-operations)
- [🔑 10. Security, Tags, and Roles](#security-tags-and-roles)
- [🧬 11. vSphere Profiles & SPBM](#vsphere-profiles-spbm)
- [🌐 12. vSphere with Tanzu / VPC Operations](#vsphere-tanzu-vpc-operations)
- [⚙️ 13. Dependency / Custom Field / Automation](#dependency-custom-field-automation)
- [🔔 14. Monitoring, Alarms, and Scheduled Tasks](#monitoring-alarms-scheduled-tasks)
- [🧭 15. Affinity & DRS Rules](#affinity-drs-rules)
- [🧩 16. OVF / Template Deployment](#ovf-template-deployment)
- [🧾 17. Miscellaneous / Validation / Utility](#miscellaneous-validation-utility)

---

<a id="vm-lifecycle-management"></a>
## 🧩 1. VM Lifecycle Management
create_basic_vm  
create_customized_vm  
create_vm_on_vapp  
create_linked_clone_vm  
clone_vm  
clone_vm_vapi  
instant_clone_vm  
instant_clone_vm_vapi  
convert_vm_to_template  
convert_template_to_vm  
register_vm  
register_vm_vapi  
unregister_vm  
unregister_vm_vapi  
destroy_vms_loopTask  
delete_vm  
delete_vm_vapi  
generate_random_vm_name  
migrate_vm  
xvmotion  
relocate_vm_vapi  
power_on_vm  
power_on_vm_vapi  
power_off_vm  
power_off_vm_vapi  
suspend_vm  
suspend_vm_vapi  
schedule_power_on_vm_task  
restart_ft_secondary_vm  
turn_on_FT  
turn_off_FT  
create_snapshot  
revert_snapshot  
remove_snapshot  
consolidate_vm_disks  
upgrade_vm_tools  
upgrade_vm_hardware  
guest_live_customization  
decrypt_vm  
encrypt_vm  
set_encrypted_vmotion  
assert_vm_powered_on  
assert_vm_das_protected  
assert_vm_ft_protected  

<a id="vapp-operations"></a>
## 🧱 2. vApp Operations
create_vapp  
delete_vapp  
power_on_vapp  
power_off_vapp  
clone_vapp  
reconfigure_vapp  
deploy_vmtx_lib_item  
destroy_vapp  

<a id="vm-configuration-reconfiguration"></a>
## 🖥️ 3. VM Configuration / Reconfiguration
reconfig_vm_add_disk  
reconfig_vm_add_cdrom  
reconfig_vm_remove_cdrom  
reconfig_vm_add_ethernet  
reconfig_vm_remove_ethernet  
reconfig_vm_add_floppy  
reconfig_vm_remove_floppy  
reconfig_vm_add_serialport  
reconfig_vm_remove_serialport  
reconfig_vm_add_parallelport  
reconfig_vm_remove_parallelport  
resize_vm_memory  
resize_vm_cpu  
reconfig_vm_network  
reconfig_vm_add_vgpu  
reconfig_dvs_set_mtu  
reconfig_dv_pg_update_security  

<a id="inventory-folder-management"></a>
## 🗂️ 4. Inventory & Folder Management
create_vm_folder  
delete_vm_folder  
create_host_folder  
delete_host_folder  
create_datacenter_folder  
delete_datacenter_folder  
create_datacenter  
delete_datacenter  

<a id="cluster-host-operations"></a>
## ☁️ 5. Cluster & Host Operations
create_cluster  
delete_cluster  
add_host_to_cluster  
remove_host  
disconnect_host  
enter_host_mm  
exit_host_mm  
perform_host_psod  
enable_drs  
enable_das  
enable_ha  
enable_evc  
associate_host_profile  
disassociate_host_profile  
extract_host_profile  
delete_host_profile  
create_host_acqspec  

<a id="datastore-storage-pod-operations"></a>
## 🧰 6. Datastore & Storage Pod Operations
create_storage_pod  
delete_storage_pod  
move_datastores_into_pod  
move_datastores_out_of_pod  
enable_sdrs_on_pod  
apply_sdrs_recommendation  
refresh_recommendation_for_pod  
fill_datastore_space  
put_datastore_to_maintenace_mode  
exit_datastore_from_maintenace_mode  

<a id="resource-pool-management"></a>
## 🧮 7. Resource Pool Management
create_resource_pool  
create_resource_pool_by_vim  
delete_resource_pool  
delete_resource_pool_by_vim  
update_resource_pool  
update_resource_pool_by_vim  

<a id="networking-dvs-dvpg-host-networking"></a>
## 🔗 8. Networking (DVS, DVPG, Host networking)
create_dvs  
delete_dvs  
upgrade_d_v_s  
add_host_to_dvs  
remove_host_from_dvs  
add_dvpg_to_host  
remove_dvpg_from_dvs  
remove_dvport_from_dvpg  
bind_vm_to_dvs  
unbind_vm_from_dvs  
reconfig_dvs_set_mtu  

<a id="content-library-operations"></a>
## 📚 9. Content Library Operations
create_local_library  
create_published_library  
create_subscribed_library  
delete_content_library  
delete_subscribed_library  
loop_delete_content_library  
loop_local_content_library  
loop_publisher_content_library  
migrate_library_storage  
wait_for_library_sync  
sync_subscribed_library  
clone_lib_item  
upload_file_to_library_item  
download_file_from_library_item  
create_content_library_item  
export_vm_to_lib_item  
export_vm_to_vmtx_lib_item  
deploy_vm_from_library_item  
mount_content_lib_iso_to_vm  

<a id="security-tags-and-roles"></a>
## 🔑 10. Security, Tags, and Roles
create_role  
delete_role  
create_users  
delete_user  
associate_user_role  
create_tag  
associate_tag  
create_category  
delete_category  
create_native_key_provider  
register_kmip_server  

<a id="vsphere-profiles-spbm"></a>
## 🧬 11. vSphere Profiles & SPBM
create_spbm_profile  
delete_spbm_profile  

<a id="vsphere-tanzu-vpc-operations"></a>
## 🌐 12. vSphere with Tanzu / VPC Operations
create_vpc  
delete_vpc  
create_vpc_subnet  
delete_vpc_subnet  
get_vpc  
get_vpc_subnet  
create_vpc_cidr  

<a id="dependency-custom-field-automation"></a>
## ⚙️ 13. Dependency / Custom Field / Automation
create_dependency  
delete_dependency  
update_dependency  
destroy_dependency  
bind_dependency  
unbind_dependency  
add_custom_field  
remove_custom_field  
generic_worker  
stuf_worker  
command_line_executor  
run_program  
run_scheduled_task  

<a id="monitoring-alarms-scheduled-tasks"></a>
## 🔔 14. Monitoring, Alarms, and Scheduled Tasks
create_alarm  
remove_alarm  
delete_scheduled_task  

<a id="affinity-drs-rules"></a>
## 🧭 15. Affinity & DRS Rules
create_affinity_rules  
remove_affinity_rules  
create_inter_vm_vmdk_anti_affinity_rule  
remove_storage_drs_rules  

<a id="ovf-template-deployment"></a>
## 🧩 16. OVF / Template Deployment
deploy_ovf  
deploy_vm_from_ovf_url  
export_vm_to_vmtx_lib_item  
convert_template_to_vm  

<a id="miscellaneous-validation-utility"></a>
## 🧾 17. Miscellaneous / Validation / Utility
verify_v_service_environment  
check_host_vms_failed_over  
check_utilization_percentage  
promote_disks  
IcvmScaleInstance  
vm_names  
