/*
 *  Copyright 2013 Cloud4SOA, www.cloud4soa.eu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.datamodel.core;

import eu.cloud4soa.api.datamodel.core.qos.UptimeInstance;
import eu.cloud4soa.api.datamodel.core.qos.ServiceQualityInstance;
import eu.cloud4soa.api.datamodel.core.qos.LatencyInstance;
import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import eu.cloud4soa.api.datamodel.core.qos.CPULoadInstance;
import eu.cloud4soa.api.datamodel.core.qos.CloudResponseTimeInstance;
import eu.cloud4soa.api.datamodel.core.qos.ContainerResponseTimeInstance;
import eu.cloud4soa.api.datamodel.core.qos.MemoryLoadInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ComputationalCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.HttpRequestsHandlerInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ComputeInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DBStorageComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.HardwareCategoryType;
import eu.cloud4soa.api.datamodel.core.utilBeans.HardwareComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.NetworkCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.NetworkResourceInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.StatusType;
import eu.cloud4soa.api.datamodel.core.utilBeans.StorageCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.StorageResourceInstance;
import eu.cloud4soa.api.datamodel.semantic.app.Application;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationDeployed;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationDeployment;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationMigrated;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationMigrating;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationRunning;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationStatus;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationStopped;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationUndeployed;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationUnreachable;
import eu.cloud4soa.api.datamodel.semantic.app.DBMigrated;
import eu.cloud4soa.api.datamodel.semantic.app.DBMigrating;
import eu.cloud4soa.api.datamodel.semantic.ea.CPULoad;
import eu.cloud4soa.api.datamodel.semantic.ea.CloudResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.ContainerResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.Latency;
import eu.cloud4soa.api.datamodel.semantic.ea.MemoryLoad;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.ea.Uptime;
import eu.cloud4soa.api.datamodel.semantic.inf.HttpRequestsHandler;
import eu.cloud4soa.api.datamodel.semantic.inf.CommunicationalCategory;
import eu.cloud4soa.api.datamodel.semantic.inf.ComputationalCategory;
import eu.cloud4soa.api.datamodel.semantic.inf.Compute;
import eu.cloud4soa.api.datamodel.semantic.inf.DBStorageComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.NetworkResource;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.StorageCategory;
import eu.cloud4soa.api.datamodel.semantic.inf.StorageResource;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import eu.cloud4soa.api.datamodel.semantic.other.ProgrammingLanguage;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;
import eu.cloud4soa.api.datamodel.semantic.user.Developer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vincenzo
 */
@XmlRootElement()
@XmlType(name = "applicationInstance", namespace="eu.cloud4soa.api.datamodel.core")
public class ApplicationInstance{
    private Application application;
    private String paaSOfferingDeploymentUriId;
    
    public ApplicationInstance() {
        application = new Application();
        application.setReleatedApplicationArchive(new eu.cloud4soa.api.datamodel.semantic.app.ApplicationArchive());
        application.setOwner(new Developer());
        application.setUseProgrammingLanguage(new ProgrammingLanguage());
        application.setSize(new StorageUnit());
    }
    
    public ApplicationInstance(Application application) {
        this.application = application;
    }
    
    public Application getApplication() {
            return application;
    }
    
    //First Level of Indirection  
    @SemanticRelation(semanticClass=Application.class, methodName="getUriId")
    public String getUriId() {
            return application.getUriId();
    }
    
    public void setUriId(String uriId) {
            application.setUriId(uriId);
    }
    
    @SemanticRelation(semanticClass=Application.class, methodName="getTermsTitle")
    public java.lang.String getTitle() {
            return application.getTermsTitle();
    }
    
    public void setTitle( java.lang.String title ) {
            application.setTermsTitle(title);
    }
    
    @SemanticRelation(semanticClass=Application.class, methodName="getAcronym")
    public java.lang.String getAcronym() {
            return application.getAlternative();
    }
    
    public void setAcronym( java.lang.String acronym ) {
            application.setAlternative(acronym);
    }

    @SemanticRelation(semanticClass=Application.class, methodName="getApplicationcode")
    public java.lang.String getApplicationcode() {
            return application.getApplicationcode();
    }

    public void setApplicationcode( java.lang.String applicationcode ) {
            application.setApplicationcode(applicationcode);
    }

    @SemanticRelation(semanticClass=Application.class, methodName="getDigest")
    public java.lang.String getDigest() {
            return application.getDigest();
    }
    
    public void setDigest( java.lang.String digest ) {
            application.setDigest(digest);
    }
    
    @SemanticRelation(semanticClass=ProgrammingLanguage.class, methodName="getitle")
    public java.lang.String getProgramminglanguage() {
        if(application.getUseProgrammingLanguage() != null)
            return application.getUseProgrammingLanguage().getTermsTitle();
        return null;
    }

    public void setProgramminglanguage( java.lang.String programminglanguage ) {
            application.getUseProgrammingLanguage().setTermsTitle(programminglanguage);
    }
    
    @SemanticRelation(semanticClass=ProgrammingLanguage.class, methodName="getVersion")
    public java.lang.String getProgramminglanguageVersion() {
        if(application.getUseProgrammingLanguage() != null)
            return application.getUseProgrammingLanguage().getVersion();
        return null;
    }

    public void setProgramminglanguageVersion( java.lang.String programmingLanguageVersion ) {
            application.getUseProgrammingLanguage().setVersion(programmingLanguageVersion);
    }

    @SemanticRelation(semanticClass=Application.class, methodName="getVersion")
    public java.lang.String getVersion() {
            return application.getVersion();
    }

    public void setVersion( java.lang.String version ) {
            application.setVersion(version);
    }
    
    //Second Level of Indirection  
//---    Remember: catch nullPointerException... ---
//---    Remember: add a getUri for every object obtained by indirection... ---
//---    Remember: add a Designed getter from every object obtained by indirection... ---
    /*---------- ApplicationArchive ----------*/	
    private eu.cloud4soa.api.datamodel.semantic.app.ApplicationArchive getApplicationArchive() {
             return application.getReleatedApplicationArchive();
    }
    
    @SemanticRelation(semanticClass=ApplicationArchive.class, methodName="getFileName")
    public String getArchiveFileName(){
        if(getApplicationArchive()!=null)
            return getApplicationArchive().getFileName();
        return null;
    }
    
    public void setArchiveFileName(String filename){
        getApplicationArchive().setFileName(filename);
    }
    
    @SemanticRelation(semanticClass=ApplicationArchive.class, methodName="getExtensionName")
    public String getArchiveExtensionName(){
        if(getApplicationArchive()!=null)
            return getApplicationArchive().getExtensionName();
        return null;
    }
    
    public void setArchiveExtensionName(String extensionName){
        getApplicationArchive().setExtensionName(extensionName);
    }
    
    
    /*---------- Owner ----------*/	
    private Developer getOwner() {
            return application.getOwner();
    }
    
    @SemanticRelation(semanticClass=Developer.class, methodName="getUriId")
    public String getOwnerUriId() {
            return getOwner().getUriId();
    }

    public void setOwnerUriId( String uri ) {
            getOwner().setUriId(uri);
    }

    /*---------- Capacity ----------*/
    private StorageUnit getSize() {
            return application.getSize();
    }
    
    @SemanticRelation(semanticClass=StorageUnit.class, methodName="getUriId")
    public String getSizeUriId() {
            return getSize().getUriId();
    }
    
    @SemanticRelation(semanticClass=StorageUnit.class, methodName="getQuantity")
    public Float getSizeQuantity() {
        if(getSize() != null)
            return getSize().getValue();
        return null;
    }

    public void setSizeQuantity( Float sizeQuantity ) {
            getSize().setValue(sizeQuantity);
    }

    /*---------- ApplicationStatus ----------*/
//    public ApplicationStatus getStatus() {
//            return application.getStatus();
//    }
    
    @SemanticRelation(semanticClass = Application.class, methodName = "getStatus")
    public StatusType getStatus() {
        if (application.getStatus() instanceof ApplicationDeployed) {
            return StatusType.Deployed;
        } else if (application.getStatus() instanceof ApplicationStopped) {
            return StatusType.Stopped;
        } else if (application.getStatus() instanceof ApplicationRunning) {
            return StatusType.Running;
        } else if (application.getStatus() instanceof ApplicationUndeployed) {
            return StatusType.Undeployed;
        } else if (application.getStatus() instanceof ApplicationUnreachable) {
            return StatusType.Unreachable;
        } else if (application.getStatus() instanceof DBMigrating) {
            return StatusType.DBMigrating;
        } else if (application.getStatus() instanceof DBMigrated) {
            return StatusType.DBMigrated;
        } else if (application.getStatus() instanceof ApplicationMigrating) {
            return StatusType.Migrating;
        } else if (application.getStatus() instanceof ApplicationMigrated) {
            return StatusType.Migrated;
        } else if (application.getStatus() instanceof eu.cloud4soa.api.datamodel.semantic.app.Error) {
            return StatusType.Error;
        }
        return null;
    }
    
    public void setStatus(StatusType status) {
        if(status.compareTo(StatusType.Deployed)==0)
            application.setStatus(new ApplicationDeployed());
        else if(status.compareTo(StatusType.Stopped)==0)
            application.setStatus(new ApplicationStopped()); 
        else if(status.compareTo(StatusType.Running)==0)
            application.setStatus(new ApplicationRunning()); 
        else if(status.compareTo(StatusType.Undeployed)==0)
            application.setStatus(new ApplicationUndeployed()); 
        else if(status.compareTo(StatusType.Unreachable)==0)
            application.setStatus(new ApplicationUnreachable()); 
        else if(status.compareTo(StatusType.DBMigrating)==0)
            application.setStatus(new DBMigrating()); 
        else if(status.compareTo(StatusType.DBMigrated)==0)
            application.setStatus(new DBMigrated()); 
        else if(status.compareTo(StatusType.Migrating)==0)
            application.setStatus(new ApplicationMigrating()); 
        else if(status.compareTo(StatusType.Migrated)==0)
            application.setStatus(new ApplicationMigrated());
        else if(status.compareTo(StatusType.Error)==0)
            application.setStatus(new eu.cloud4soa.api.datamodel.semantic.app.Error());
    }
    
    public ApplicationStatus getApplicationStatus(){
        return application.getStatus();
    }
    public void setApplicationStatus(ApplicationStatus status){
        application.setStatus(status);
    }
        
    /*---------- ApplicationDeployment ----------*/
    
    private ApplicationDeployment getDeployment(){
        return getApplication().getDeployment();
    }

    private void setDeployment(ApplicationDeployment applicationDeployment){
        getApplication().setDeployment(applicationDeployment);
    }
    
    private void checkAppDeploymentAndInstantiate(Object obj){
        if(obj!=null && getDeployment()==null){
            ApplicationDeployment applicationDeployment = new ApplicationDeployment();
            setDeployment(applicationDeployment);
        }
    }
    
    @SemanticRelation(semanticClass=ApplicationDeployment.class, methodName="getUriId")
    public String getApplicationDeploymentUriId() {
        if(getDeployment()!=null)
            return getDeployment().getUriId();
        return null;
    }
    
    public void setApplicationDeploymentUriId(String uriId) {
        checkAppDeploymentAndInstantiate(uriId);
        if(getDeployment()!=null)
            getDeployment().setUriId(uriId);
    }
        
    @SemanticRelation(semanticClass=PaaSOffering.class, methodName="getUriId")
    public String getPaaSOfferingDeploymentUriId() {
        if(getDeployment()!=null && getDeployment().getDeployingLocation()!=null)
            return getDeployment().getDeployingLocation().getUriId();
        else if (paaSOfferingDeploymentUriId != null)
            return paaSOfferingDeploymentUriId;
        return null;
    }
        
    public void setPaaSOfferingDeploymentUriId(String PaaSInstanceUriId) {
        checkAppDeploymentAndInstantiate(PaaSInstanceUriId);
        if(getDeployment()!=null && getDeployment().getDeployingLocation()!=null)
            getDeployment().getDeployingLocation().setUriId(PaaSInstanceUriId);
        else paaSOfferingDeploymentUriId = PaaSInstanceUriId;
            
    }
    
    public void setPaaSOfferingDeployment(PaaSInstance paaSInstance) {
        checkAppDeploymentAndInstantiate(paaSInstance);
        getDeployment().setDeployingLocation(paaSInstance.getPaaSOffering());
    }
    
    @SemanticRelation(semanticClass=PaaSOffering.class, methodName="getTitle")
    public String getPaaSOfferingDeploymentName() {

        if(getDeployment()!=null 
        	&& getDeployment().getDeployingLocation()!=null
        	&& getDeployment().getDeployingLocation().getPaaSProvider()!=null
        )
            return getDeployment().getDeployingLocation().getPaaSProvider().getTitle();
        return null;
    }
    
    @SemanticRelation(semanticClass=ApplicationDeployment.class, methodName="getIP")
    public String getDeploymentIP() {
        if(getDeployment()!=null || paaSOfferingDeploymentUriId!=null)
            return getDeployment().getIP();
        return null;
    }
    
    public void setDeploymentIP(String IP) {
        checkAppDeploymentAndInstantiate(IP);
        if(getDeployment()!=null || paaSOfferingDeploymentUriId!=null)
            getDeployment().setIP(IP);
    }
    
     @SemanticRelation(semanticClass=ApplicationDeployment.class, methodName="getSLAcontractID")
    public String getSLAcontractID() {
        if(getDeployment()!=null || paaSOfferingDeploymentUriId!=null)
            return getDeployment().getSLAcontractID();
        return null;
    }
    
    public void setSLAcontractID(String SLAid) {
        checkAppDeploymentAndInstantiate(SLAid);
        if(getDeployment()!=null || paaSOfferingDeploymentUriId!=null)
            getDeployment().setSLAcontractID(SLAid);
    }
        
    @SemanticRelation(semanticClass=ApplicationDeployment.class, methodName="getAdapterURL")
    public String getAdapterUrl() {
        if(getDeployment()!=null || paaSOfferingDeploymentUriId!=null)
            return getDeployment().getAdapterURL();
        return null;
    }
    
    public void setAdapterUrl(String url) {
        checkAppDeploymentAndInstantiate(url);
        if(getDeployment()!=null || paaSOfferingDeploymentUriId!=null)
            getDeployment().setAdapterURL(url);
    }
    
        //Second Level of Indirection  
    /*---------- RequiresSoftwareComponent ----------*/
    private List<SoftwareComponent> getRequiresSoftwareComponent(){
        return application.getRequiresSoftwareComponent();
    } 
    
    //Second Level of Indirection  
    /*---------- RequiresResource ----------*/
    private List<HardwareComponent> getRequiresResource(){
        return application.getRequiresResource();
    } 
    
    //    New methods
    public List<HardwareComponentInstance> getHardwareComponents() {
        List<HardwareComponent> requiredHardwareComponents = getRequiresResource();
        List<HardwareComponentInstance> hardwareComponentInstances = new ArrayList<HardwareComponentInstance>();
        for (HardwareComponent hardwareComponent : requiredHardwareComponents) {
            HardwareComponentInstance hardwareComponentInstance = null;
//            if(hardwareComponent instanceof DBStorageComponent)
//                hardwareComponentInstance = new DBStorageComponentInstance((DBStorageComponent)hardwareComponent);
//            else
                hardwareComponentInstance = new HardwareComponentInstance(hardwareComponent);
            hardwareComponentInstances.add(hardwareComponentInstance);
            if(hardwareComponent instanceof NetworkResource){
                hardwareComponentInstance = new NetworkResourceInstance((NetworkResource)hardwareComponent);
                hardwareComponentInstance.setRelatedhwcategoryInstance(new NetworkCategoryInstance((CommunicationalCategory)hardwareComponent.getRelatedhwcategory()));
            }
            else if(hardwareComponent instanceof HttpRequestsHandler){
                hardwareComponentInstance = new HttpRequestsHandlerInstance((HttpRequestsHandler)hardwareComponent);
                hardwareComponentInstance.setRelatedhwcategoryInstance(new ComputationalCategoryInstance((ComputationalCategory)hardwareComponent.getRelatedhwcategory()));
            }
            else if(hardwareComponent instanceof Compute){
                hardwareComponentInstance = new ComputeInstance((Compute)hardwareComponent);
                hardwareComponentInstance.setRelatedhwcategoryInstance(new ComputationalCategoryInstance((ComputationalCategory)hardwareComponent.getRelatedhwcategory()));
            }
            else if(hardwareComponent instanceof StorageResource){
                hardwareComponentInstance = new StorageResourceInstance();
                hardwareComponentInstance.setRelatedhwcategoryInstance(new StorageCategoryInstance((StorageCategory)hardwareComponent.getRelatedhwcategory()));
            }
        }
        return hardwareComponentInstances;
    }
    
    public void setHardwareComponents(List<HardwareComponentInstance> hardwareComponentInstances) {
        for (HardwareComponentInstance hardwareComponentInstance : hardwareComponentInstances) {
//            if(hardwareComponentInstance instanceof DBStorageComponentInstance){
//                boolean a = true;
//            }
            getRequiresResource().add(hardwareComponentInstance.getHardwareComponent());
        }
    }
    
    public HardwareComponentInstance createAndAddHardwareComponent(HardwareCategoryType hardwareCategoryType) {
        HardwareComponent hardwareComponent = null;
        HardwareComponentInstance hardwareComponentInstance = null;
        if(hardwareCategoryType.compareTo(HardwareCategoryType.NetworkCategory)==0){
            hardwareComponent =new NetworkResource();
            CommunicationalCategory communicationalCategory = new CommunicationalCategory();
            hardwareComponent.setRelatedhwcategory(communicationalCategory);
            hardwareComponentInstance = new NetworkResourceInstance((NetworkResource)hardwareComponent);
            hardwareComponentInstance.setRelatedhwcategoryInstance(new NetworkCategoryInstance(communicationalCategory));
        }
        else if(hardwareCategoryType.compareTo(HardwareCategoryType.HttpRequestHandlerCategory)==0){
            hardwareComponent = new HttpRequestsHandler();
            ComputationalCategory computationalCategory = new ComputationalCategory();
            hardwareComponent.setRelatedhwcategory(computationalCategory);
            hardwareComponentInstance = new HttpRequestsHandlerInstance((HttpRequestsHandler)hardwareComponent);
            hardwareComponentInstance.setRelatedhwcategoryInstance(new ComputationalCategoryInstance(computationalCategory));
        }
        else if(hardwareCategoryType.compareTo(HardwareCategoryType.ComputationalCategory)==0){
            hardwareComponent = new Compute();
            ComputationalCategory computationalCategory =new ComputationalCategory();
            hardwareComponent.setRelatedhwcategory(computationalCategory);
            hardwareComponentInstance = new ComputeInstance((Compute)hardwareComponent);
            hardwareComponentInstance.setRelatedhwcategoryInstance(new ComputationalCategoryInstance(computationalCategory));
        }
        else if(hardwareCategoryType.compareTo(HardwareCategoryType.StorageCategory)==0){
            hardwareComponent = new StorageResource();
            StorageCategory storageCategory = new StorageCategory();
            hardwareComponent.setRelatedhwcategory(storageCategory);
            hardwareComponentInstance = new StorageResourceInstance((StorageResource)hardwareComponent);
            hardwareComponentInstance.setRelatedhwcategoryInstance(new StorageCategoryInstance(storageCategory));
        }
        getRequiresResource().add(hardwareComponent);
        return hardwareComponentInstance;
    }
    
    public boolean removeHardwareComponent(HardwareComponentInstance hardwareComponentInstance) {
        if(getRequiresResource().contains(hardwareComponentInstance.getHardwareComponent())){
            getRequiresResource().remove(hardwareComponentInstance.getHardwareComponent());
            return true;
        }
        return false;
    }
    
    public List<SoftwareComponentInstance> getSoftwareComponents() {
        List<SoftwareComponent> requiredSoftwareComponents = getRequiresSoftwareComponent();
        List<SoftwareComponentInstance> softwareComponentInstances = new ArrayList<SoftwareComponentInstance>();
        for (SoftwareComponent softwareComponent : requiredSoftwareComponents) {
            SoftwareComponentInstance softwareComponentInstance = null;
            if(softwareComponent instanceof DBStorageComponent){
                softwareComponentInstance = new DBStorageComponentInstance((DBStorageComponent)softwareComponent);
            }
            else {
                softwareComponentInstance = new SoftwareComponentInstance(softwareComponent);
            }
            softwareComponentInstances.add(softwareComponentInstance);
        }
        //        return Collections.unmodifiableList(softwareComponentInstances);
        return softwareComponentInstances;
    }
        
    public void setSoftwareComponents(List<SoftwareComponentInstance> softwareComponentInstances) {
        for (SoftwareComponentInstance softwareComponentInstance : softwareComponentInstances) {
            getRequiresSoftwareComponent().add(softwareComponentInstance.getSoftwareComponent());
        }
    }
    
    public SoftwareComponentInstance createAndAddSoftwareComponent(SoftwareComponentInstance softwareComponentInstance) {
//        SoftwareComponentInstance softwareComponentInstance = new SoftwareComponentInstance();
//        softwareComponentInstance.setSoftwareCategoryInstance(softwareComponentInstance);
        getRequiresSoftwareComponent().add(softwareComponentInstance.getSoftwareComponent());
        return softwareComponentInstance;
    }
    
    public SoftwareComponentInstance createAndAddSoftwareComponent(String title, String description, String version, String licensetype, SoftwareCategoryInstance softwareCategoryInstance) {
        SoftwareComponentInstance softwareComponentInstance = new SoftwareComponentInstance(title, description, version, licensetype);
        softwareComponentInstance.setSoftwareCategoryInstance(softwareCategoryInstance);
        getRequiresSoftwareComponent().add(softwareComponentInstance.getSoftwareComponent());
        return softwareComponentInstance;
    }
    
    public boolean removeSoftwareComponent(SoftwareComponentInstance softwareComponentInstance) {
        if(getRequiresSoftwareComponent().contains(softwareComponentInstance.getSoftwareComponent())){
            getRequiresSoftwareComponent().remove(softwareComponentInstance.getSoftwareComponent());
            return true;
        }
        return false;
    }
    
    //Second Level of Indirection  
    /*---------- RequiresServiceQuality ----------*/
    private List<Technology_Service_Quality> getRequiresServiceQuality(){
        return application.getRequiresServiceQuality();
    } 
    
    //    New methods
    public List<ServiceQualityInstance> getServiceQualities() {
        List<Technology_Service_Quality> requiredServiceQuality = getRequiresServiceQuality();
        List<ServiceQualityInstance> serviceQualityInstances = new ArrayList<ServiceQualityInstance>();
        for (Technology_Service_Quality serviceQuality : requiredServiceQuality) {
            ServiceQualityInstance serviceQualityInstance = null;
            if(serviceQuality instanceof Latency)
                serviceQualityInstance = new LatencyInstance((Latency)serviceQuality);
            else if(serviceQuality instanceof Uptime)
                serviceQualityInstance = new UptimeInstance((Uptime)serviceQuality);
            else if(serviceQuality instanceof CPULoad)
                serviceQualityInstance = new CPULoadInstance((CPULoad)serviceQuality);
            else if(serviceQuality instanceof ContainerResponseTime)
                serviceQualityInstance = new ContainerResponseTimeInstance((ContainerResponseTime)serviceQuality);
            else if(serviceQuality instanceof MemoryLoad)
                serviceQualityInstance = new MemoryLoadInstance((MemoryLoad)serviceQuality);
            else if(serviceQuality instanceof CloudResponseTime)
                serviceQualityInstance = new CloudResponseTimeInstance((CloudResponseTime)serviceQuality);
            else
                    continue;
            serviceQualityInstances.add(serviceQualityInstance);
        }
        return serviceQualityInstances;
    }
    
    public void setServiceQualities(List<ServiceQualityInstance> serviceQualityInstances) {
        for (ServiceQualityInstance serviceQualityInstance : serviceQualityInstances) {
            getRequiresServiceQuality().add(serviceQualityInstance.getServiceQuality());
        }
    }
    
    public boolean removeServiceQualities(ServiceQualityInstance serviceQualityInstance) {
        if(getRequiresServiceQuality().contains(serviceQualityInstance.getServiceQuality())){
            getRequiresServiceQuality().remove(serviceQualityInstance.getServiceQuality());
            return true;
        }
        return false;
    }
    
//    public void setStatus( ApplicationStatus status ) {
//            getStatusUri();
//    }
    
//	public Company getAdoptedbycompany() {
//		return adoptedbycompany;
//	}
//	
//	public void setAdoptedbycompany( Company adoptedbycompany ) {
//		this.adoptedbycompany = adoptedbycompany;
//	}
    
//	public InterfaceBetweenApplications getCalledapplicationininterface() {
//		return calledapplicationininterface;
//	}
//	
//	public void setCalledapplicationininterface( InterfaceBetweenApplications calledapplicationininterface ) {
//		this.calledapplicationininterface = calledapplicationininterface;
//	}
	
//    public InterfaceBetweenApplications getCallerapplicationininterface() {
//            return callerapplicationininterface;
//    }
//
//    public void setCallerapplicationininterface( InterfaceBetweenApplications callerapplicationininterface ) {
//            this.callerapplicationininterface = callerapplicationininterface;
//    }
	
//    public ApplicationComponent getComposedof() {
//            return composedof;
//    }
//
//    public void setComposedof( ApplicationComponent composedof ) {
//            this.composedof = composedof;
//    }
	
//    public ApplicationArchetype getInstanceofapplicationarchetype() {
//            return instanceofapplicationarchetype;
//    }
//
//    public void setInstanceofapplicationarchetype( ApplicationArchetype instanceofapplicationarchetype ) {
//            this.instanceofapplicationarchetype = instanceofapplicationarchetype;
//    }
	
//    public ApplicationManual getManual() {
//            return manual;
//    }
//
//    public void setManual( ApplicationManual manual ) {
//            this.manual = manual;
//    }
	

    
//    public HardwareComponent getRequireshardware() {
//            return requireshardware;
//    }
//
//    public void setRequireshardware( HardwareComponent requireshardware ) {
//            this.requireshardware = requireshardware;
//    }
	

//    public SoftwareComponent getRequiressoftware() {
//            return requiressoftware;
//    }
//
//    public void setRequiressoftware( SoftwareComponent requiressoftware ) {
//            this.requiressoftware = requiressoftware;
//    }

    @Override
    public String toString() {
        return "[ApplicationInstance: {"+this.getTitle()+", "+this.getVersion()+", "+this.getArchiveFileName()+", "+this.getArchiveExtensionName()+", "+this.getDigest()+", "+this.getSizeQuantity()+"}]";
    }

}
