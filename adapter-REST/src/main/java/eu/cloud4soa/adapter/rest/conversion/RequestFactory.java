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


package eu.cloud4soa.adapter.rest.conversion;

import eu.cloud4soa.adapter.rest.request.DeleteDeploymentRequest;
import eu.cloud4soa.adapter.rest.request.ExtendedMonitorRequest;
import eu.cloud4soa.adapter.rest.request.MonitorDetailRequest;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;

/**
 * This factory is only proof of concept
 * I do not know if the properties i use are correct!
 * I need clarification about c4s datamodels!
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class RequestFactory {

	public static MonitorDetailRequest createMonitorDetailRequest(IMonitoringJob monitoringJob){
		MonitorDetailRequest monitorDetailRequest = new MonitorDetailRequest();
		monitorDetailRequest.setBaseUrl(monitoringJob.getCheckUrl());
		
		return monitorDetailRequest;
	}
        
        public static ExtendedMonitorRequest createExtendedMonitorRequest(IMonitoringJob monitoringJob, String ApplicationURL){
		ExtendedMonitorRequest extendedMonitorRequest = new ExtendedMonitorRequest();
		extendedMonitorRequest.setBaseUrl(monitoringJob.getCheckUrl());
                extendedMonitorRequest.setApplicationUrl(ApplicationURL);
		
		return extendedMonitorRequest;
	}
	
	public static DeleteDeploymentRequest createDeleteDeploymentRequest(ApplicationInstance applicationInstance){
		DeleteDeploymentRequest deleteDeploymentRequest = new DeleteDeploymentRequest();
		
		deleteDeploymentRequest.setBaseUrl(applicationInstance.getDeploymentIP());
		deleteDeploymentRequest.setApplicationName(applicationInstance.getTitle());
		deleteDeploymentRequest.setDeploymentName(applicationInstance.getApplication().getDeployment().getUriId());
		
		return deleteDeploymentRequest;
	}
}
