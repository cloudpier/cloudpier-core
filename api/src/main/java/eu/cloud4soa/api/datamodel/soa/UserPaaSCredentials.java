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


package eu.cloud4soa.api.datamodel.soa;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vinlau
 */
@XmlRootElement()
@XmlType(name = "userPaaSCredentials", namespace="eu.cloud4soa.api.datamodel.soa")
public class UserPaaSCredentials {
    
    private String userInstanceUriId;
    private String paaSInstanceUriId;
    
    private String publicKey; 
    private String secretKey;
    private String accountName;

    public UserPaaSCredentials() { }

    public UserPaaSCredentials(String userInstanceUriId, String paaSInstanceUriId, String publicKey, String secretKey, String accountName) {
        this.userInstanceUriId = userInstanceUriId;
        this.paaSInstanceUriId = paaSInstanceUriId;
        this.publicKey = publicKey;
        this.secretKey = secretKey;
        this.accountName = accountName;
    }
    
    /**
     * @return the userInstanceUriId
     */
    public String getUserInstanceUriId() {
        return userInstanceUriId;
    }

    /**
     * @param userInstanceUriId the userInstanceUriId to set
     */
    public void setUserInstanceUriId(String userInstanceUriId) {
        this.userInstanceUriId = userInstanceUriId;
    }

    /**
     * @return the paaSInstanceUriId
     */
    public String getPaaSInstanceUriId() {
        return paaSInstanceUriId;
    }

    /**
     * @param paaSInstanceUriId the paaSInstanceUriId to set
     */
    public void setPaaSInstanceUriId(String paaSInstanceUriId) {
        this.paaSInstanceUriId = paaSInstanceUriId;
    }
    
    
    /**
     * @return the publicKey
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return the secretKey
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey the secretKey to set
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

}
