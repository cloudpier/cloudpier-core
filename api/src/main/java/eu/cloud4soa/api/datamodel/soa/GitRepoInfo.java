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
@XmlType(name = "gitRepoInfo", namespace="eu.cloud4soa.api.datamodel.soa")
public class GitRepoInfo {
    
    private String url;
    private String repositoryName;
    private String userId;
    private String applicationId;
    private String applicationUrl;
    private String adapterUrl;   

    public GitRepoInfo() { }

    public GitRepoInfo(String url, String repositoryName, String userId, String applicationId, String applicationUrl, String adapterUrl) {
        this.url = url;
        this.repositoryName = repositoryName;
        this.userId = userId;
        this.applicationId = applicationId;
        this.applicationUrl = applicationUrl;
        this.adapterUrl = adapterUrl;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the repositoryName
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * @param repositoryName the repositoryName to set
     */
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the applicationId
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    /**
     * Get the value of applicationUrl
     *
     * @return the value of applicationUrl
     */
    public String getApplicationUrl() {
        return applicationUrl;
    }

    /**
     * Set the value of applicationUrl
     *
     * @param adapterUrl new value of applicationUrl
     */
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
    
    /**
     * Get the value of adapterUrl
     *
     * @return the value of adapterUrl
     */
    public String getAdapterUrl() {
        return adapterUrl;
    }

    /**
     * Set the value of adapterUrl
     *
     * @param adapterUrl new value of adapterUrl
     */
    public void setAdapterUrl(String adapterUrl) {
        this.adapterUrl = adapterUrl;
    }



}
