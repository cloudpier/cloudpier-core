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
import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.foaf.Document;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vincenzo
 */
@XmlRootElement()
@XmlType(name = "paaSProviderInstance", namespace="eu.cloud4soa.api.datamodel.core.utilBeans")
public class PaaSProviderInstance {
    private PaaSProvider paasProvider;
    
    public PaaSProviderInstance(PaaSProvider paasProvider) {
        this.paasProvider=paasProvider;
    }
    
    public PaaSProviderInstance() {
        this(new PaaSProvider());
    }
      
    public PaaSProviderInstance(String title, String homepage) {
        this();
        paasProvider.setTitle(title);
        Document document = new Document();
        try {
            document.setLink(new URI(homepage));
        } catch (URISyntaxException ex) {
            Logger.getLogger(PaaSProviderInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
        paasProvider.setHomepage(document);
    }
    
    public String getUriId(){
        return paasProvider.getUriId();
    }
    
    public void setUriId(String uriId){
       paasProvider.setUriId(uriId);
    }
    
    public java.lang.String getTitle() {
        return paasProvider.getTitle();
    }

    public void setTitle( java.lang.String title ) {
        paasProvider.setTitle(title);
    }
    
    public String getHomePage() {
        if(paasProvider.getHomepage()==null){
//            Document document = new Document();
//            document.setLink(new URI(""));
//            paasProvider.setHomepage(document);
            return null;
        }
        return paasProvider.getHomepage().getLink().toString();
    }

    public void setHomePage(String homepage) {
        if(paasProvider.getHomepage()==null){
            Document document = new Document();
            try {
                document.setLink(new URI(homepage));
            } catch (URISyntaxException ex) {
                Logger.getLogger(PaaSProviderInstance.class.getName()).log(Level.SEVERE, null, ex);
            }
            paasProvider.setHomepage(document);
        }
        else try {
            paasProvider.getHomepage().setLink(new URI(homepage));
        } catch (URISyntaxException ex) {
            Logger.getLogger(PaaSProviderInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //First Level of Indirection  
    /*---------- PaaSProvider ----------*/
    public PaaSProvider getPaaSProvider(){
        return paasProvider;
    }
    
    @SemanticRelation(semanticClass = PaaSProvider.class, methodName = "getUser")
    public PaaSUserInstance getPaaSUser(){
        return new PaaSUserInstance(getPaaSProvider().getUser());
    }
    
    @SemanticRelation(semanticClass = PaaSProvider.class, methodName = "getProvidePaaS")
    public List<PaaSInstance> getProvidedPaaS(){
        List<PaaSInstance> paasInstances = new ArrayList<PaaSInstance>();
        List<PaaSOffering> paaSOfferings = getPaaSProvider().getProvidePaaS();
        for (PaaSOffering paaSOffering : paaSOfferings) {
            paasInstances.add(new PaaSInstance(paaSOffering));
        }
        return paasInstances;
    }
    
    public void setProvidedPaaS(List<PaaSInstance> pasSInstanceList){
        for (PaaSInstance paaSInstance : pasSInstanceList) {
            paasProvider.getProvidePaaS().add(paaSInstance.getPaaSOffering());
        }
    }
    
    
    
}
