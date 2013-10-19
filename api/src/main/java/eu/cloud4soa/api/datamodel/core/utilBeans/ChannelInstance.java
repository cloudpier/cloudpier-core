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
package eu.cloud4soa.api.datamodel.core.utilBeans;

import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import eu.cloud4soa.api.datamodel.semantic.paas.Channel;
import eu.cloud4soa.api.datamodel.semantic.paas.Operation;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "channelInstance")//, namespace="eu.cloud4soa.api.datamodel.core.utilBeans")
@XmlSeeAlso({APIInstance.class, CLIInstance.class, WebInterfaceInstance.class})
public abstract class ChannelInstance {
    protected Channel channel;

    public Channel getChannel() {
        return channel;
    }
    
    public String getUriId(){
        return channel.getUriId();
    }
    
    public void setUriId(String uriId){
       channel.setUriId(uriId);
    }
    
    @SemanticRelation(semanticClass = Channel.class, methodName = "getTitle")
    public String getTitle() {
        return channel.geTitle();
    }

    public void setTitle(String title) {
        channel.setTitle(title);
    }
    
    @SemanticRelation(semanticClass = Channel.class, methodName = "getDescription")
    public String getDescription(){
        return channel.getDescription();
    }
    
    public void setDescription(String description){
       channel.setDescription(description);
    }
    
    @SemanticRelation(semanticClass = Channel.class, methodName = "getVersion")
    public String getVersion(){
        return channel.getVersion();
    }
    
    public void setVersion(String version){
       channel.setVersion(version);
    }
    
    @SemanticRelation(semanticClass = Channel.class, methodName = "getSupportedOperations")
    private List<Operation> getSupportedOperations(){
        return channel.getSupportedOperations();
    }
    
    public OperationInstance createAndAddOperation(String title, String description, String command, String informationReturned){
        OperationInstance operationInstance = new OperationInstance(title, description, command, informationReturned);
        getSupportedOperations().add(operationInstance.getOperation());
        return operationInstance;
    }
    
    public List<OperationInstance> getOperations() {
        List<Operation> supportedOperations = getSupportedOperations();
        List<OperationInstance> supportedOperationInstances = new ArrayList<OperationInstance>();
        for (Operation operation : supportedOperations) {
            supportedOperationInstances.add(new OperationInstance(operation));
        }
        return supportedOperationInstances;
    }
    
    public void setOperations(List<OperationInstance> supportedOperationInstances) {
        for (OperationInstance operationInstance : supportedOperationInstances) {
            getSupportedOperations().add(operationInstance.getOperation());
        }
    }
    
    public boolean removeOperation(OperationInstance operationInstance){
        return getSupportedOperations().remove(operationInstance.getOperation());
    }

}
