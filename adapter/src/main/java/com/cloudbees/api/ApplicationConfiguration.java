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
 * Copyright 2010-2011, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.api;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("ApplicationConfiguration")
public class ApplicationConfiguration {

	private String applicationId;
	
	private String defaultEnvironment;
	
	private List<String> appliedEnvironments = new ArrayList<String>();
	
	public ApplicationConfiguration() {
		
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getDefaultEnvironment() {
		return defaultEnvironment;
	}
	public void setDefaultEnvironment(String defaultEnvironment) {
		this.defaultEnvironment = defaultEnvironment;
	}
	public List<String> getAppliedEnvironments() {
		return appliedEnvironments;
	}
	public void setAppliedEnvironments(List<String> appliedEnvironments) {
		this.appliedEnvironments = appliedEnvironments;
	}

    @Override
    public String toString() {
        return "ApplicationConfiguration{" +
                "applicationId='" + applicationId + '\'' +
                ", defaultEnvironment='" + defaultEnvironment + '\'' +
                ", appliedEnvironments=" + appliedEnvironments +
                '}';
    }
}
