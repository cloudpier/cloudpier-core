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
import eu.cloud4soa.adapter.rest.request.UpdateApplicationRequest;
import eu.cloud4soa.adapter.rest.request.UpdateDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.UpdateDeploymentRequest;
import eu.cloud4soa.adapter.rest.response.UpdateApplicationResponse;
import eu.cloud4soa.adapter.rest.response.UpdateDatabaseResponse;
import eu.cloud4soa.adapter.rest.response.UpdateDeploymentResponse;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Ignore
public class AdapterClientTestUpdate extends AbstractAdapterClientTest {

	/*
	 * update PUT /ems/application/${applicationName} PUT
	 * /ems/application/${applicationName}/deployment/${deploymentName} PUT
	 * /ems/
	 * application/${applicationName}/deployment/${deploymentName}/database/
	 * ${databaseName}
	 */

	/**
	 * FIXME find workaround
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateApplicationRequest() throws Exception {
		UpdateApplicationRequest r = new UpdateApplicationRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		UpdateApplicationResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	/**
	 * FIXME find workaround
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateDeploymentRequest() throws Exception {
		UpdateDeploymentRequest r = new UpdateDeploymentRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		UpdateDeploymentResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	/**
	 * FIXME find workaround
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateDatabaseRequest() throws Exception {
		UpdateDatabaseRequest r = new UpdateDatabaseRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);
		r.setDatabaseName("db");

		UpdateDatabaseResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}
}
