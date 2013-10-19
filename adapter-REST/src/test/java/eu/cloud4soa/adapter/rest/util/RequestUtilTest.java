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


package eu.cloud4soa.adapter.rest.util;

import junit.framework.Assert;

import org.junit.Test;

import eu.cloud4soa.adapter.rest.request.CreateApplicationRequest;
import eu.cloud4soa.adapter.rest.request.CreateDeploymentRequest;
import eu.cloud4soa.adapter.rest.request.DeploymentRequest;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class RequestUtilTest {

	@Test
	public void testResolveResourcePath() {
		DeploymentRequest r = new DeploymentRequest();
		RequestUtil.infixPotentialDefaults(r);
		
		String resolved = RequestUtil.resolveResourcePath(r);
		Assert.assertTrue(resolved.contains("{"));
		Assert.assertTrue(resolved.contains("}"));

		r.setApplicationName("meineApplication");

		resolved = RequestUtil.resolveResourcePath(r);
		Assert.assertFalse(resolved.contains("{"));
		Assert.assertFalse(resolved.contains("}"));
	}

	@Test
	public void testIsValid(){
		CreateDeploymentRequest createDeploymentRequest = new CreateDeploymentRequest();
		Assert.assertFalse(RequestUtil.isValid(createDeploymentRequest));

		createDeploymentRequest.setApplicationName("myApplication");
		createDeploymentRequest.setDeploymentName("myDeployment");
		Assert.assertFalse(RequestUtil.isValid(createDeploymentRequest));

		CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
		Assert.assertFalse(RequestUtil.isValid(createApplicationRequest));

		createApplicationRequest.setApplicationName("myApplication");
		Assert.assertFalse(RequestUtil.isValid(createApplicationRequest));

		createApplicationRequest.setLanguage("php");
		Assert.assertFalse(RequestUtil.isValid(createApplicationRequest));
				
		createApplicationRequest.setApiKey("kiasudzadh");
		createApplicationRequest.setHash("asjdhaksjdhkdd");
		Assert.assertTrue(RequestUtil.isValid(createApplicationRequest));
	}
	
	@Test
	public void testInfixPotentialDefault(){
		CreateDeploymentRequest createDeploymentRequest = new CreateDeploymentRequest();
		
		RequestUtil.infixPotentialDefaults(createDeploymentRequest);
		
		Assert.assertEquals("default", createDeploymentRequest.getDeploymentName());
		
		createDeploymentRequest.setDeploymentName("anothervalue");
		RequestUtil.infixPotentialDefaults(createDeploymentRequest);
		
		Assert.assertEquals("anothervalue", createDeploymentRequest.getDeploymentName());
	}
}
