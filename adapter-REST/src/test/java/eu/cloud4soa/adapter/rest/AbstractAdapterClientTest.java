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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import eu.cloud4soa.adapter.rest.auth.Credentials;
import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.adapter.rest.impl.AdapterClientCXF;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public abstract class AbstractAdapterClientTest {
	
	//cloudcontrol
//	private final String target = "http://c4s.localhost";
//	private final String target = "http://c4s.cloudcontrolled.com";
	
	//heroku
//	private final String target = "http://localhost:5000";
	private final String target = "http://c4s.herokuapp.com";
	
	//cloudbees
//	private final String target = "http://c4sadnewyosujavaapp.testaccountname.cloudbees.net";
	
	protected final String applicationName = "c4sadnewyosujavaapp";
//	protected final String applicationName = "c4s4";

	public String getTarget() {
		return target;
	}

	private AdapterClient adapterClient = new AdapterClientCXF();

	public AdapterClient getAdapter() {
		return adapterClient;
	}

	private Credentials credentials;

	public Credentials getCredentials() {
		return credentials;
	}

	@Before
	public void setUp() {
		credentials = new CustomerCredentials(
				"your api key",
				"1d6459840547b1e0ef83f463b5eaf2bf0248d1ae68cbb3e84234af08db3337136ecfd741a0a6a74b1f81c75be44dc921");
		
//		credentials = new CustomerCredentials("86168c87685da592b857cdf1b5379b73b967b31c59516c93d824f1e734163e062441af21f4445c9b84599d10befd258d",
//				"1d6459840547b1e0ef83f463b5eaf2bf0248d1ae68cbb3e84234af08db3337136ecfd741a0a6a74b1f81c75be44dc921");
	}

	@Test
	public void testInstantiation() {
		Assert.assertNotNull(adapterClient);
	}
}
