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
package eu.cloud4soa.adapter.rest.request;

import eu.cloud4soa.adapter.rest.aop.Default;
import eu.cloud4soa.adapter.rest.aop.Ignore;
import java.io.Serializable;

import eu.cloud4soa.adapter.rest.aop.Method;
import eu.cloud4soa.adapter.rest.aop.Method.HttpMethod;
import eu.cloud4soa.adapter.rest.aop.NotNull;
import eu.cloud4soa.adapter.rest.aop.Path;
import eu.cloud4soa.adapter.rest.aop.Path.Component;
import eu.cloud4soa.adapter.rest.aop.UrlPath;
import eu.cloud4soa.adapter.rest.response.ExtendedMonitorResponse;

/**
 * Request for resource <strong>Extended Monitor</strong> 
 * <br><code>htt[p|ps]://baseUrl/monitor/extend</code>.<br>
 * 
 * Response will contain details of that requested resource(where applicable).
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Method(HttpMethod.GET)
@Path(component=Component.MONITOR, path="/extend/${applicationUrl}")
public class ExtendedMonitorRequest extends Request<ExtendedMonitorResponse> implements Serializable{
	private static final long serialVersionUID = 2378456309862621042L;
        
        @Ignore
        @UrlPath(pattern="${applicationUrl}")
        private String applicationUrl;

        
	public ExtendedMonitorRequest(){
	}

    /**
     * @return the applicationUrl
     */
    public String getApplicationUrl() {
        return applicationUrl;
    }

    /**
     * @param applicationUrl the applicationUrl to set
     */
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
}
