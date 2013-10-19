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

import org.junit.Ignore;
import org.junit.Test;

import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.request.ApplicationRequest;
import eu.cloud4soa.adapter.rest.request.EMSRequest;
import eu.cloud4soa.adapter.rest.request.MonitorDetailRequest;
import eu.cloud4soa.adapter.rest.request.MonitorRequest;
import eu.cloud4soa.adapter.rest.response.ApplicationResponse;
import eu.cloud4soa.adapter.rest.response.EMSResponse;
import eu.cloud4soa.adapter.rest.response.MonitorDetailResponse;
import eu.cloud4soa.adapter.rest.response.MonitorResponse;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Ignore
public class AdapterClientTestCommon extends AbstractAdapterClientTest {

	/*
	 * (non-javadoc) tests run in debug mode thats why stacktraces will be
	 * dumped to stdout -> ugly
	 */
	@Test
	public void testAdapterExpected_throwsException() {
		MonitorRequest r = new MonitorRequest();
		r.setBaseUrl("thisisjustinvented.com");
		MonitorResponse s;
		try {
			s = getAdapter().send(r, getCredentials());
			System.err.println(s);
			Assert.fail("Expected AdapterClientCommunicationException was not thrown");
		} catch (Exception e) {
			// good stuff
		}
	}

	// GET /ems/
	@Test
	public void testEMSRequest() throws Exception {
		EMSRequest r = new EMSRequest();
		r.setBaseUrl(getTarget());
		EMSResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	// GET /monitor/
	@Test
	public void testMonitorRequest() throws Exception {
		MonitorRequest r = new MonitorRequest();
		r.setBaseUrl(getTarget());
		MonitorResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	// GET /monitor/detail
	@Test
	public void testMonitorDetailsRequest() throws Exception {
		MonitorDetailRequest r = new MonitorDetailRequest();
		r.setBaseUrl(getTarget());
		MonitorDetailResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

//	@Ignore
	@Test
	public void testApplicationRequest_does_not_exist()
			throws Exception {
		ApplicationRequest r = new ApplicationRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName("mockApplication");
		ApplicationResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
		Assert.assertEquals(410, s.getStatusCode().ordinal());
	}
}
