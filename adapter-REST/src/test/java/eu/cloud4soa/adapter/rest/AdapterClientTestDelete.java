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
import eu.cloud4soa.adapter.rest.request.DeleteApplicationRequest;
import eu.cloud4soa.adapter.rest.request.DeleteDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.DeleteDeploymentRequest;
import eu.cloud4soa.adapter.rest.request.DeleteSSHKeyRequest;
import eu.cloud4soa.adapter.rest.response.DeleteApplicationResponse;
import eu.cloud4soa.adapter.rest.response.DeleteDatabaseResponse;
import eu.cloud4soa.adapter.rest.response.DeleteDeploymentResponse;
import eu.cloud4soa.adapter.rest.response.DeleteSSHKeyResponse;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Ignore
public class AdapterClientTestDelete extends AbstractAdapterClientTest {

	/*
	 * delete DELETE /ems/application/${applicationName} DELETE
	 * /ems/application/${applicationName}/deployment/${deploymentName} DELETE
	 * /ems
	 * /application/${applicationName}/deployment/${deploymentName}/database/
	 * ${databaseName}
	 */
	@Test
	// @Ignore
	public void testDeleteDatabaseRequest() throws Exception {
		DeleteDatabaseRequest r = new DeleteDatabaseRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);
		r.setDatabaseName("db");

		DeleteDatabaseResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	@Test
	// @Ignore
	public void testDeleteDeploymentRequest() throws Exception {
		DeleteDeploymentRequest r = new DeleteDeploymentRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);

		DeleteDeploymentResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	@Test
//	@Ignore
	public void testDeleteApplicationRequest() throws Exception {
		DeleteApplicationRequest r = new DeleteApplicationRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);
		
		DeleteApplicationResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}
	
	@Test
	public void testDeleteSSHKeyRequest() throws Exception{
		String sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDLTF0UgrOqa8gA0TiFRP04bbv7FOnitoHrcmk3EXO6atbwcv0pyEDd9I/NMD58LuKucWIZo75OHiaKcvKnaeBPoikufrU3cKpMsXsMmGQya+cjQqNJDb0FE9ZsZOXnLDnrM8ErucY6a7JZ3ofC2LiDgIadmFCD/1l5SbMBX5Ei9bYAIUcs5/W3DapbSnEx1BzvKXRD0IwJ6vDvv+RY1z4GIqdOU++cWxVGvOsCDUR08RVsZBuwiq7RxfTiq5u1nzHfmJgzGfZnxL3vldkaooHHuRGLHZtkbEhMxoxC0vHEcQ3uB0QxfHtHjPXbb4AC+2z0n9KBKICHU2h4uB0dF79P ska@codeplex";
		
		DeleteSSHKeyRequest r = new DeleteSSHKeyRequest();
		r.setBaseUrl(getTarget());
		r.setSshKey(sshKey);
		r.setApplicationName(applicationName);
		
		DeleteSSHKeyResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}
}
