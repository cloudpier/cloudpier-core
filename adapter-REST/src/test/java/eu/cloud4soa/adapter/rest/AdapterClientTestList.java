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
import eu.cloud4soa.adapter.rest.request.ListApplicationRequest;
import eu.cloud4soa.adapter.rest.request.ListDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.ListDeploymentRequest;
import eu.cloud4soa.adapter.rest.request.ListSSHKeyRequest;
import eu.cloud4soa.adapter.rest.response.ListApplicationResponse;
import eu.cloud4soa.adapter.rest.response.ListDatabaseResponse;
import eu.cloud4soa.adapter.rest.response.ListDeploymentResponse;
import eu.cloud4soa.adapter.rest.response.ListSSHKeyResponse;
import eu.cloud4soa.adapter.rest.response.model.Application;
import eu.cloud4soa.adapter.rest.response.model.Database;
import eu.cloud4soa.adapter.rest.response.model.Deployment;
import eu.cloud4soa.adapter.rest.response.model.SshKey;
import java.net.UnknownHostException;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Ignore
public class AdapterClientTestList extends AbstractAdapterClientTest {

	/*
	 * list GET /ems/application GET
	 * /ems/application/${applicationName}/deployment GET
	 * /ems/application/${applicationName}/deployment/${deploymentName}/database
	 */
	@Test
	public void testListApplicationRequest() throws AdapterClientException, UnknownHostException {
		ListApplicationRequest r = new ListApplicationRequest();
		r.setBaseUrl(getTarget());

		ListApplicationResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
		for(Application application : s.getApplications()){
			System.out.println(application);
		}
	}

	@Test
//	@Ignore
	public void testListDeploymentRequest() throws AdapterClientException, UnknownHostException {
		ListDeploymentRequest r = new ListDeploymentRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		ListDeploymentResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
		for(Deployment deployment : s.getDeployments()){
			System.out.println(deployment);			
		}
	}

	@Test
//	@Ignore
	public void testListDatabaseRequest() throws AdapterClientException, InterruptedException, UnknownHostException {
		ListDatabaseRequest r = new ListDatabaseRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		ListDatabaseResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
		for(Database database : s.getDatabases()){
			System.out.println(database);			
		}
	}
	
	@Test
//	@Ignore
	public void testListSSHKeyRequest() throws AdapterClientException, UnknownHostException {
		ListSSHKeyRequest r = new ListSSHKeyRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		ListSSHKeyResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
		for(SshKey key : s.getSshKeys()){
			System.out.println(key);
		}
	}
}
