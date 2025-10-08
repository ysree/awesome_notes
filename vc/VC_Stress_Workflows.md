Workflows
----------
- AlarmOps
- ContentLibraryLoadOps
- CreateDeleteOps
- DeployVmsSpbm
- DVSOps
- FTSanity
- GlobalAuthOps
- GuestCustomization
- HostFailoverMaxVms
- HostMaintenanceMode
- IcvmScale
- InstantCloneVmLifeCycle
- InventoryOps
- LargeFileOps
- LinkedCloneOps
- LocalContentLibraryOps
- MaxHostProfileAssociations
- MaxHostProfiles
- MigrateOps
- OnlinePromoteDisk
- ProvisionOps
- PublishSubscribeContentLibraryOps
- ReconfigVm
- RegisterOps
- ResourcePoolOpsByVim
- SaturatedDatastorePowerOnClones
- StorageVMotion
- TaggingLoad
- VapiResourcePoolOps
- VapiVmOps
- VaryingLoad
- VirtualApplianceOps
- VmCryptVmLifeCycle
- VsanVmOps
- XvMotionEnterpriseApps
- 


New Workflows to implement in 9.0 features

VPC Ops - virtual private cloud
Create VPC
Get VPC
create vpc IP cidr
create subnet
get subnet
delete subnet
delete vpc

GuestCustomization -
Perform live Guest Customization
Get Customization Status

Online Promote Disk -
Perform Instant & linked clone in parallel
perform promote disk on both linked & instant cloned VMs 
power off & delete VMs 

Content Library Datastore Migration -
Migrate content library Datastore from one ds to another ds

Single API ovf deploy -

(New WF is not required for single API, modified deploy_ovf worker such that when VC is > 9 , ovf deployment will go via single API & if VC < 9 ovf deployment will be using NFC (N/W File Copy SOAP calls))

GlobalAuthOps-
Create role
Create user
Associate role with user
create VM with Administrator user 
Power on VM with newly created user 
Power off VM with newly created user 
Delete VM with Administrator user 
delete user
delete role

VstatsAcqspec-
Create VM AcqSpec
Create Host AcqSpec