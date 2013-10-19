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
package eu.cloud4soa.api.datamodel.governance;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author frarav
 */
public class DeployApplicationParameters {
    
    File applicationArchive; 
    ApplicationInstance applicationInstance;
    PaaSInstance paaSInstance;
    String publicKey; 
    String secretKey; 
    String accountName; 
    String slaTemplateID;
    List<SlaPolicy> penalties = new ArrayList<SlaPolicy>();

    
    
    
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public File getApplicationArchive() {
        return applicationArchive;
    }

    public void setApplicationArchive(File applicationArchive) {
        this.applicationArchive = applicationArchive;
    }

    public ApplicationInstance getApplicationInstance() {
        return applicationInstance;
    }

    public void setApplicationInstance(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    public PaaSInstance getPaaSInstance() {
        return paaSInstance;
    }

    public void setPaaSInstance(PaaSInstance paaSInstance) {
        this.paaSInstance = paaSInstance;
    }

    public List<SlaPolicy> getPenalties() {
        if (this.penalties!=null) { 
            return penalties;
        } else {
            return new ArrayList<SlaPolicy>();
        }
    }

    public void setPenalties(List<SlaPolicy> penalties) {
        this.penalties = penalties;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSlaTemplateID() {
        return slaTemplateID;
    }

    public void setSlaTemplateID(String slaTemplateID) {
        this.slaTemplateID = slaTemplateID;
    }
    
    
    
    
}
