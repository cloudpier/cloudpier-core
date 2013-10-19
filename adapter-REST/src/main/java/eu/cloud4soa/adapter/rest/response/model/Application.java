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


package eu.cloud4soa.adapter.rest.response.model;

import java.io.Serializable;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class Application implements Serializable{
	private static final long serialVersionUID = 7631011793022763875L;

	private String applicationName;
	
	private String created;
	private String modified;
	private String url;
	
	private String repository;
	private String language;
	
	public Application(){
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	@Override
	public String toString() {
		return "Application [applicationName=" + applicationName + ", created=" + created + ", modified=" + modified + ", repository=" + repository
				+ ", language=" + language + "]";
	}    
}