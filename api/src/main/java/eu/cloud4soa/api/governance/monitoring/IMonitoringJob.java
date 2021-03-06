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
package eu.cloud4soa.api.governance.monitoring;

import java.util.Date;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 *
 */
public interface IMonitoringJob {

	public abstract Long getId();
	public abstract void setId(Long id);
	
	public abstract String getApiKey();
	public abstract String getSecretKey();
	
	public abstract String getCheckUrl();
	public abstract void setCheckUrl(String url);
	
	public abstract Date getLastExecuted();
	public abstract void setLastExecuted(Date lastExecuted);
	
	public abstract boolean isEnabled();
	public abstract void setEnabled(boolean enabled);
	
	public abstract void setApplicationInstanceUriId(String uriId);
	public abstract String getApplicationInstanceUriId();

}