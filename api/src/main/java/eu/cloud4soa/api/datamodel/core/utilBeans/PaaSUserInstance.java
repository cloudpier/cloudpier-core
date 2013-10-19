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

import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;
import eu.cloud4soa.api.datamodel.semantic.user.PaaSUser;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vincenzo
 */
@XmlRootElement()
@XmlType(name = "paaSUserInstance", namespace="eu.cloud4soa.api.datamodel.core.utilBeans")
public class PaaSUserInstance extends UserInstance{

    public PaaSUserInstance(PaaSUser paasUser) {
        super(paasUser);
    }
    
    public PaaSUserInstance() {
        this(new PaaSUser());
        cloud4SoaAccountInstance = new Cloud4SoaAccountInstance();
        user.setHoldsaccount(cloud4SoaAccountInstance.getCloud4SoaAccount());
    }
    
    private PaaSUser getPaaSUser(){
        return (PaaSUser)this.getUser();
    }
    
    public String getUriId(){
        return getPaaSUser().getUriId();
    }
    
    public void setUriId(String uriId){
       getPaaSUser().setUriId(uriId);
    }
    
    public PaaSProviderInstance getPaaSProviderInstance(){
        return new PaaSProviderInstance(getPaaSUser().getPaaSProvider());
    }
    
    public void setPaaSProviderInstance(PaaSProviderInstance paaSProviderInstance){
        getPaaSUser().setPaaSProvider(paaSProviderInstance.getPaaSProvider());
        paaSProviderInstance.getPaaSProvider().setUser(getPaaSUser());
    }
    
    @SemanticRelation(semanticClass = PaaSProvider.class, methodName = "getProvidePaaS")
    public List<PaaSInstance> getOfferings(){
        List<PaaSInstance> paasInstances = new ArrayList<PaaSInstance>();
        List<PaaSOffering> paaSOfferings = getPaaSUser().getPaaSProvider().getProvidePaaS();
        for (PaaSOffering paaSOffering : paaSOfferings) {
            paasInstances.add(new PaaSInstance(paaSOffering));
        }
        return paasInstances;
    }
    
}
