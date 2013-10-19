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
import eu.cloud4soa.adapter.rest.request.CreateApplicationRequest;
import eu.cloud4soa.adapter.rest.request.CreateDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.CreateDeploymentRequest;
import eu.cloud4soa.adapter.rest.request.CreateSSHKeyRequest;
import eu.cloud4soa.adapter.rest.response.CreateApplicationResponse;
import eu.cloud4soa.adapter.rest.response.CreateDatabaseResponse;
import eu.cloud4soa.adapter.rest.response.CreateDeploymentResponse;
import eu.cloud4soa.adapter.rest.response.CreateSSHKeyResponse;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de) 
 */
@Ignore
public class AdapterClientTestCreate extends AbstractAdapterClientTest{

	/*
	 * create POST /ems/application/${applicationName} POST
	 * /ems/application/${applicationName}/deployment/${deploymentName} POST
	 * /ems
	 * /application/${applicationName}/deployment/${deploymentName}/database/
	 * ${databaseName}
	 */
	@Test
	public void testCreateApplicationRequest() throws Exception {
		CreateApplicationRequest r = new CreateApplicationRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName("c4s2applicationname");
		r.setLanguage("java");
		
//		for(int i = 0 ; i < 5 ; i ++){
			CreateApplicationResponse s = getAdapter().send(r, getCredentials());
			System.out.println(s);
//		}
	}

	@Test
//	@Ignore
	public void testCreateDeploymentRequest() throws Exception {
		CreateDeploymentRequest r = new CreateDeploymentRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);
		CreateDeploymentResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	@Test
//	@Ignore
	public void testCreateDatabaseRequest() throws Exception {
		CreateDatabaseRequest r = new CreateDatabaseRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);
		r.setDatabaseName("db");

		r.setDatabaseUser("databaseUser");
		r.setDatabasePassword("databasePassword");
		r.setDatabaseType("redis");
		
		CreateDatabaseResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}
	
	@Test
	public void testCreateSSHKeyRequest() throws Exception{
		String sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDJpPjRV5uis4vJoHmmZP/Ynpq16L+fE0aReiCM9mi7+urnVNJLiwka12+TEM7y9hbfr7NBia9+x6+1eLC3yi/cuVOcDAxewN48HBrr1JMmcnxDqIHEObaAcn5NorQTvx8ondhB2kNAcg3zTFUCnUhYhVGMKmPQ/On/orMvESBh5IBpQfBv9gMGoABqauuKAP2KdcufY89GaKPsuwtCJJNOWt5DBOtORui58smcTC+JEtwzkdZlPIzYiQpwPQabgaFO2OECbVE0tWvaatcJ3RdD9S1mxP+2I//W77fddfZt+aTts4YNVWxB1a9oIowugty4TUvybhFx8j+tvb01tlHh dn@dnX1930";
		
		CreateSSHKeyRequest r = new CreateSSHKeyRequest();
		r.setBaseUrl(getTarget());
		r.setSshKey(sshKey);
		r.setApplicationName(applicationName);
		
		CreateSSHKeyResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}
	
	
	
}
