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


package eu.cloud4soa.adapter.rest;

import org.junit.Ignore;
import org.junit.Test;

import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.request.ApplicationRequest;
import eu.cloud4soa.adapter.rest.request.DatabaseRequest;
import eu.cloud4soa.adapter.rest.request.DeploymentRequest;
import eu.cloud4soa.adapter.rest.request.MonitorDetailRequest;
import eu.cloud4soa.adapter.rest.request.MonitorRequest;
import eu.cloud4soa.adapter.rest.response.ApplicationResponse;
import eu.cloud4soa.adapter.rest.response.DatabaseResponse;
import eu.cloud4soa.adapter.rest.response.DeploymentResponse;
import eu.cloud4soa.adapter.rest.response.MonitorDetailResponse;
import eu.cloud4soa.adapter.rest.response.MonitorResponse;
import java.net.UnknownHostException;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de) 
 */
@Ignore
public class AdapterClientTestDetails extends AbstractAdapterClientTest {

	/*
	 * details GET /ems/application/${applicationName} GET
	 * /ems/application/${applicationName}/deployment/${deploymentName} GET
	 * /ems/
	 * application/${applicationName}/deployment/${deploymentName}/database/
	 * ${databaseName}
	 */
	@Test
	public void testApplicationRequest() throws AdapterClientException,UnknownHostException {
		ApplicationRequest r = new ApplicationRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		ApplicationResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s.getApplication());
	}

	@Test
	public void testDeploymentRequest() throws AdapterClientException,UnknownHostException {
		DeploymentRequest r = new DeploymentRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		DeploymentResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s.getDeployment());
	}

	@Test
	public void testDatabaseRequest() throws AdapterClientException,UnknownHostException {
		DatabaseRequest r = new DatabaseRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		DatabaseResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s.getDatabase());
	}
	
	@Test
	public void testMonitorRequest() throws AdapterClientException,UnknownHostException {
		MonitorRequest r = new MonitorRequest();
		r.setBaseUrl(getTarget());

		MonitorResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}
	
	@Test
	public void testMonitorDetailRequest() throws AdapterClientException,UnknownHostException {
		MonitorDetailRequest r = new MonitorDetailRequest();
		r.setBaseUrl(getTarget());

		MonitorDetailResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}
}
