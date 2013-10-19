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

import java.util.List;
import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import eu.cloud4soa.api.datamodel.core.qos.CPULoadInstance;
import eu.cloud4soa.api.datamodel.core.qos.CloudResponseTimeInstance;
import eu.cloud4soa.api.datamodel.core.qos.ContainerResponseTimeInstance;
import eu.cloud4soa.api.datamodel.core.qos.LatencyInstance;
import eu.cloud4soa.api.datamodel.core.qos.MemoryLoadInstance;
import eu.cloud4soa.api.datamodel.core.qos.ServiceQualityInstance;
import eu.cloud4soa.api.datamodel.core.qos.UptimeInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.APIInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.HttpRequestsHandlerInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.CLIInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ChannelInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ChannelType;
import eu.cloud4soa.api.datamodel.core.utilBeans.ComputationalCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ComputeInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DBStorageComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.HardwareCategoryType;
import eu.cloud4soa.api.datamodel.core.utilBeans.HardwareComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.NetworkCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.NetworkResourceInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.OperationInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSProviderInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.StorageCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.StorageResourceInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.WebInterfaceInstance;
import eu.cloud4soa.api.datamodel.semantic.ea.CPULoad;
import eu.cloud4soa.api.datamodel.semantic.ea.CloudResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.ContainerResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.Latency;
import eu.cloud4soa.api.datamodel.semantic.ea.MemoryLoad;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.ea.Uptime;
import eu.cloud4soa.api.datamodel.semantic.paas.*;
import eu.cloud4soa.api.datamodel.semantic.other.*;
import eu.cloud4soa.api.datamodel.semantic.ent.*;
import eu.cloud4soa.api.datamodel.semantic.inf.*;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author zeginis
 * modified by vincenzo
 */
@XmlRootElement()
@XmlType(name = "paasInstance", namespace = "eu.cloud4soa.api.datamodel.core")
public class PaaSInstance {

    private PaaSOffering paasOffering;

    public PaaSInstance() {
        paasOffering = new PaaSOffering();
        paasOffering.setPaaSProvider(new PaaSProvider());
    }

    public PaaSInstance(PaaSOffering paasOffering) {
        this.paasOffering = paasOffering;
    }

    public PaaSOffering getPaaSOffering() {
        return paasOffering;
    }

    //First Level of Indirection  
    @SemanticRelation(semanticClass = PaaSOffering.class, methodName = "getUriId")
    public String getUriId() {
        return paasOffering.getUriId();
    }

    public void setUriId(String uriId) {
        paasOffering.setUriId(uriId);
    }

    @SemanticRelation(semanticClass = PaaSOffering.class, methodName = "getTitle")
    public String getTitle() {
        return paasOffering.getTitle();
    }

    public void setTitle(String title) {
        paasOffering.setTitle(title);
    }

    @SemanticRelation(semanticClass = PaaSOffering.class, methodName = "getURL")
    public String getURL() {
        return paasOffering.getURL();
    }

    public void setURL(String url) {
        paasOffering.setURL(url);
    }

    @SemanticRelation(semanticClass = PaaSOffering.class, methodName = "getStatus")
    public String getStatus() {
        return paasOffering.getStatus();
    }

    public void setStatus(String status) {
        paasOffering.setStatus(status);
    }

    @SemanticRelation(semanticClass = PaaSOffering.class, methodName = "gethasAdapter")
    public Boolean getHasAdapter() {
        return paasOffering.gethasAdapter();
    }

    public void setHasAdapter(Boolean hasAdapter) {
        paasOffering.sethasAdapter(hasAdapter);
    }

    //Second Level of Indirection  
    /*---------- PaaSProvider ----------*/
    private PaaSProvider getPaaSProvider() {
        return paasOffering.getPaaSProvider();
    }

    public PaaSProviderInstance getPaaSProviderInstance() {
        return new PaaSProviderInstance(paasOffering.getPaaSProvider());
    }

    public void setPaaSProviderInstance(PaaSProviderInstance paaSProviderInstance) {
        paasOffering.setPaaSProvider(paaSProviderInstance.getPaaSProvider());
    }

    @SemanticRelation(semanticClass = PaaSProvider.class, methodName = "getTitle")
    public String getProviderTitle() {
        return getPaaSProvider().getTitle();
    }

    public void setProviderTitle(String title) {
        getPaaSProvider().setTitle(title);
    }

    //Second Level of Indirection  
    /*---------- ProgrammingLanguage ----------*/
    private ProgrammingLanguage getProgrammingLanguage() {
        if (paasOffering.getSupportedLanguage() == null) {
            paasOffering.setSupportedLanguage(new ProgrammingLanguage());
        }
        return paasOffering.getSupportedLanguage();
    }

    @SemanticRelation(semanticClass = ProgrammingLanguage.class, methodName = "getTitle")
    public String getSupportedProgrammingLanguage() {
        return getProgrammingLanguage().getTermsTitle();
    }

    public void setSupportedProgrammingLanguage(String programminglanguage) {
        getProgrammingLanguage().setTermsTitle(programminglanguage);
    }

    @SemanticRelation(semanticClass = ProgrammingLanguage.class, methodName = "getVersion")
    public String getSupportedProgrammingLanguageVersion() {
        return getProgrammingLanguage().getVersion();
    }

    public void setSupportedProgrammingLanguageVersion(String version) {
        getProgrammingLanguage().setVersion(version);
    }

    //Second Level of Indirection  
    /*---------- CommunicationChannels ----------*/
    private List<Channel> getCommunicationChannels() {
        return paasOffering.getCommunicationChannels();
    }

    //Second Level of Indirection  
    /*---------- OfferedSoftware ----------*/
    private List<SoftwareComponent> getOfferedSoftwareComponents() {
        return paasOffering.getOfferedSoftware();
    }

    //Second Level of Indirection  
    /*---------- OfferedHardwareComponents ----------*/
    private List<HardwareComponent> getOfferedHardwareComponents() {
        return paasOffering.getOfferedHardwareComponents();
    }
    
       /*---------- OfferedHardwareComponents ----------*/
    private List<Technology_Service_Quality> getQoSMetrics() {
        return paasOffering.getProvidesServiceQuality();
    }

//    //Third Level of Indirection  
//    @SemanticRelation(semanticClass = Document.class, methodName = "getLink")
//    public String getProviderURL() {
//        return getPaaSProviderInstance().getHomePage();
//    }
    @SemanticRelation(semanticClass = OperationType.class, methodName = "getTitle")
    public List<String> getSupportedOperations() {
        List<Channel> channels = getCommunicationChannels();
        Iterator<Channel> channelIter = channels.iterator();
        List<String> operations = new ArrayList<String>();
        while (channelIter.hasNext()) {
            Channel myChannel = channelIter.next();
            Iterator<Operation> channelOperationsIter = myChannel.getSupportedOperations().iterator();
            while (channelOperationsIter.hasNext()) {
                Operation myOperation = channelOperationsIter.next();
                operations.add(myOperation.getOperationType().getTitle());
            }
        }
        return operations;
    }

    @SemanticRelation(semanticClass = SoftwareComponent.class, methodName = "getTitle")
    public List<String> getSoftwareComponentsName() {
        Iterator<SoftwareComponent> softwareComponentsIter = getOfferedSoftwareComponents().iterator();
        List<String> softwareComponentsName = new ArrayList<String>();
        while (softwareComponentsIter.hasNext()) {
            SoftwareComponent mySoftwareComponent = softwareComponentsIter.next();
            softwareComponentsName.add(mySoftwareComponent.getTitle());
        }
        return softwareComponentsName;
    }

//    New methods
    public List<HardwareComponentInstance> getHardwareComponents() {
//        List<HardwareComponent> offeredHardwareComponents = getOfferedHardwareComponents();
//        List<HardwareComponentInstance> hardwareComponentInstances = new ArrayList<HardwareComponentInstance>();
//        for (HardwareComponent hardwareComponent : offeredHardwareComponents) {
//            hardwareComponentInstances.add(new HardwareComponentInstance(hardwareComponent));
//        }
//        return hardwareComponentInstances;
        List<HardwareComponent> offeredHardwareComponents = getOfferedHardwareComponents();
        List<HardwareComponentInstance> hardwareComponentInstances = new ArrayList<HardwareComponentInstance>();
        for (HardwareComponent hardwareComponent : offeredHardwareComponents) {
            HardwareComponentInstance hardwareComponentInstance = null;
            hardwareComponentInstance = new HardwareComponentInstance(hardwareComponent);
            hardwareComponentInstances.add(hardwareComponentInstance);
            if (hardwareComponent instanceof NetworkResource) {
                hardwareComponentInstance = new NetworkResourceInstance((NetworkResource) hardwareComponent);
                hardwareComponentInstance.setRelatedhwcategoryInstance(new NetworkCategoryInstance((CommunicationalCategory) hardwareComponent.getRelatedhwcategory()));
            } else if (hardwareComponent instanceof HttpRequestsHandler) {
                hardwareComponentInstance = new HttpRequestsHandlerInstance((HttpRequestsHandler) hardwareComponent);
                hardwareComponentInstance.setRelatedhwcategoryInstance(new ComputationalCategoryInstance((ComputationalCategory) hardwareComponent.getRelatedhwcategory()));
            } else if (hardwareComponent instanceof Compute) {
                hardwareComponentInstance = new ComputeInstance((Compute) hardwareComponent);
                hardwareComponentInstance.setRelatedhwcategoryInstance(new ComputationalCategoryInstance((ComputationalCategory) hardwareComponent.getRelatedhwcategory()));
            } else if (hardwareComponent instanceof StorageResource) {
                hardwareComponentInstance = new StorageResourceInstance();
                hardwareComponentInstance.setRelatedhwcategoryInstance(new StorageCategoryInstance((StorageCategory) hardwareComponent.getRelatedhwcategory()));
            }
        }
        return hardwareComponentInstances;
    }

    public void setHardwareComponents(List<HardwareComponentInstance> hardwareComponentInstances) {
        for (HardwareComponentInstance hardwareComponentInstance : hardwareComponentInstances) {
            getOfferedHardwareComponents().add(hardwareComponentInstance.getHardwareComponent());
        }
    }

    public HardwareComponentInstance createAndAddHardwareComponent(HardwareCategoryType hardwareCategoryType) {
        HardwareComponent hardwareComponent = null;
        HardwareComponentInstance hardwareComponentInstance = null;
        if (hardwareCategoryType.compareTo(HardwareCategoryType.NetworkCategory) == 0) {
            hardwareComponent = new NetworkResource();
            CommunicationalCategory communicationalCategory = new CommunicationalCategory();
            hardwareComponent.setRelatedhwcategory(communicationalCategory);
            hardwareComponentInstance = new NetworkResourceInstance((NetworkResource) hardwareComponent);
            hardwareComponentInstance.setRelatedhwcategoryInstance(new NetworkCategoryInstance(communicationalCategory));
        } else if (hardwareCategoryType.compareTo(HardwareCategoryType.HttpRequestHandlerCategory) == 0) {
            hardwareComponent = new HttpRequestsHandler();
            ComputationalCategory computationalCategory = new ComputationalCategory();
            hardwareComponent.setRelatedhwcategory(computationalCategory);
            hardwareComponentInstance = new HttpRequestsHandlerInstance((HttpRequestsHandler) hardwareComponent);
            hardwareComponentInstance.setRelatedhwcategoryInstance(new ComputationalCategoryInstance(computationalCategory));
        } else if (hardwareCategoryType.compareTo(HardwareCategoryType.ComputationalCategory) == 0) {
            hardwareComponent = new Compute();
            ComputationalCategory computationalCategory = new ComputationalCategory();
            hardwareComponent.setRelatedhwcategory(computationalCategory);
            hardwareComponentInstance = new ComputeInstance((Compute) hardwareComponent);
            hardwareComponentInstance.setRelatedhwcategoryInstance(new ComputationalCategoryInstance(computationalCategory));
        } else if (hardwareCategoryType.compareTo(HardwareCategoryType.StorageCategory) == 0) {
            hardwareComponent = new StorageResource();
            StorageCategory storageCategory = new StorageCategory();
            hardwareComponent.setRelatedhwcategory(storageCategory);
            hardwareComponentInstance = new StorageResourceInstance((StorageResource) hardwareComponent);
            hardwareComponentInstance.setRelatedhwcategoryInstance(new StorageCategoryInstance(storageCategory));
        }
        getOfferedHardwareComponents().add(hardwareComponent);
        return hardwareComponentInstance;
    }

    public boolean removeHardwareComponent(HardwareComponentInstance hardwareComponentInstance) {
        if (getOfferedHardwareComponents().contains(hardwareComponentInstance.getHardwareComponent())) {
            getOfferedHardwareComponents().remove(hardwareComponentInstance.getHardwareComponent());
            return true;
        }
        return false;
    }

    public List<SoftwareComponentInstance> getSoftwareComponents() {
        List<SoftwareComponent> offeredSoftwareComponents = getOfferedSoftwareComponents();
        List<SoftwareComponentInstance> softwareComponentInstances = new ArrayList<SoftwareComponentInstance>();
        for (SoftwareComponent softwareComponent : offeredSoftwareComponents) {
            SoftwareComponentInstance softwareComponentInstance = null;
            if (softwareComponent instanceof DBStorageComponent) {
                softwareComponentInstance = new DBStorageComponentInstance((DBStorageComponent) softwareComponent);
            } else {
                softwareComponentInstance = new SoftwareComponentInstance(softwareComponent);
            }
            softwareComponentInstances.add(softwareComponentInstance);
        }
//        return Collections.unmodifiableList(softwareComponentInstances);
        return softwareComponentInstances;
    }

    public void setSoftwareComponents(List<SoftwareComponentInstance> softwareComponentInstances) {
        for (SoftwareComponentInstance softwareComponentInstance : softwareComponentInstances) {
            getOfferedSoftwareComponents().add(softwareComponentInstance.getSoftwareComponent());
        }
    }

    public SoftwareComponentInstance createAndAddSoftwareComponent(SoftwareCategoryInstance softwareCategoryInstance) {
        SoftwareComponentInstance softwareComponentInstance = new SoftwareComponentInstance();
        softwareComponentInstance.setSoftwareCategoryInstance(softwareCategoryInstance);
        getOfferedSoftwareComponents().add(softwareComponentInstance.getSoftwareComponent());
        return softwareComponentInstance;
    }

    public SoftwareComponentInstance createAndAddSoftwareComponent(String title, String description, String version, String licensetype, SoftwareCategoryInstance softwareCategoryInstance) {
        SoftwareComponentInstance softwareComponentInstance = new SoftwareComponentInstance(title, description, version, licensetype);
        softwareComponentInstance.setSoftwareCategoryInstance(softwareCategoryInstance);
        getOfferedSoftwareComponents().add(softwareComponentInstance.getSoftwareComponent());
        return softwareComponentInstance;
    }

    public boolean removeSoftwareComponent(SoftwareComponentInstance softwareComponentInstance) {
        if (getOfferedSoftwareComponents().contains(softwareComponentInstance.getSoftwareComponent())) {
            getOfferedSoftwareComponents().remove(softwareComponentInstance.getSoftwareComponent());
            return true;
        }
        return false;
    }

    public ChannelInstance createAndAddChannel(ChannelType channelType) {
        ChannelInstance channelInstance = null;
        Channel channel = null;
        if (channelType.compareTo(ChannelType.API) == 0) {
            channel = new API();
            channelInstance = new APIInstance((API) channel);
        } else if (channelType.compareTo(ChannelType.CLI) == 0) {
            channel = new CLI();
            channelInstance = new CLIInstance((CLI) channel);
        } else if (channelType.compareTo(ChannelType.WebInterface) == 0) {
            channel = new WebInterface();
            channelInstance = new WebInterfaceInstance((WebInterface) channel);
        }
        getCommunicationChannels().add(channel);
        return channelInstance;
    }

    public List<ChannelInstance> getChannels() {
        List<Channel> offeredCommunicationChannels = getCommunicationChannels();
        List<ChannelInstance> communicationChannelsInstances = new ArrayList<ChannelInstance>();
        for (Channel channel : offeredCommunicationChannels) {
            if (channel instanceof API) {
                communicationChannelsInstances.add(new APIInstance((API) channel));
            }
            if (channel instanceof CLI) {
                communicationChannelsInstances.add(new CLIInstance((CLI) channel));
            }
            if (channel instanceof WebInterface) {
                communicationChannelsInstances.add(new WebInterfaceInstance((WebInterface) channel));
            }
        }
//        return communicationChannelsInstances.iterator();
        return communicationChannelsInstances;
    }

    public void setChannels(List<ChannelInstance> channels) {
        for (ChannelInstance channelInstance : channels) {
            getCommunicationChannels().add(channelInstance.getChannel());
        }
    }

    public boolean removeChannel(ChannelInstance channelInstance) {
        if (getCommunicationChannels().contains(channelInstance.getChannel())) {
            getCommunicationChannels().remove(channelInstance.getChannel());
            return true;
        }
        return false;
    }

    public boolean getGITsupport() {
        List<ChannelInstance> channelInstances = getChannels();
        for (ChannelInstance channel : channelInstances) {
            List<OperationInstance> operationInstances = channel.getOperations();
            for (OperationInstance operation : operationInstances) {
                if (operation.getOperation().getOperationType().getTitle().equals("GIT deployment")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getArchiveSupport() {
         List<ChannelInstance> channelInstances = getChannels();
        for (ChannelInstance channel : channelInstances) {
            List<OperationInstance> operationInstances = channel.getOperations();
            for (OperationInstance operation : operationInstances) {
                if (operation.getOperation().getOperationType().getTitle().equals("Code archive deployment")) {
                    return true;
                }
            }
        }
        return false;
    }
    
      public List<ServiceQualityInstance> getSupportedMetrics() {
        List<Technology_Service_Quality> supportedMetrics = getQoSMetrics();
        List<ServiceQualityInstance> serviceQualityInstances = new ArrayList<ServiceQualityInstance>();
        for (Technology_Service_Quality supportedMetric : supportedMetrics) {
           if (supportedMetric instanceof CPULoad){
               serviceQualityInstances.add(new CPULoadInstance((CPULoad) supportedMetric));
           }else if(supportedMetric instanceof CloudResponseTime){
               serviceQualityInstances.add(new CloudResponseTimeInstance((CloudResponseTime) supportedMetric));
           }else if (supportedMetric instanceof ContainerResponseTime){
               serviceQualityInstances.add(new ContainerResponseTimeInstance((ContainerResponseTime) supportedMetric));
           }else if (supportedMetric instanceof Latency){
               serviceQualityInstances.add(new LatencyInstance((Latency) supportedMetric));
           }else if (supportedMetric instanceof MemoryLoad){
               serviceQualityInstances.add(new MemoryLoadInstance((MemoryLoad) supportedMetric));
           }else if (supportedMetric instanceof Uptime){
               serviceQualityInstances.add(new UptimeInstance((Uptime) supportedMetric));
           }          
        }   
            
        return serviceQualityInstances;
    }

    public void setSupportedMetrics(List<ServiceQualityInstance> supportedQoSmetrics) {
        for (ServiceQualityInstance supportedQoSmetric : supportedQoSmetrics) {
            getQoSMetrics().add(supportedQoSmetric.getServiceQuality());
        }
    }

   //    TEMPORARY slaId
    @SemanticRelation(semanticClass = PaaSOffering.class, methodName = "getSlaId")
    public String getSlaId() {
        return paasOffering.getSlaId();
    }

    public void setSlaId(String slaId) {
        paasOffering.setSlaId(slaId);
    }

    @Override
    public String toString() {
        return "[PaaSInstance: {" + this.getTitle() + "}]";
    }
}
