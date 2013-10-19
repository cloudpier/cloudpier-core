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

import eu.cloud4soa.adapter.rest.common.Operation;
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.request.OperationRequest;
import eu.cloud4soa.adapter.rest.response.OperationResponse;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Ignore
public class AdapterClientTestOperations extends AbstractAdapterClientTest {

	/*
	 * operations POST
	 * /ems/application/${applicationName}/operation/${operationName}
	 */
	@Test
	public void testNoOperationRequest() throws Exception {
		OperationRequest r = new OperationRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);
		r.setOperation(Operation.nop);

		OperationResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	@Test
	public void testStartOperationRequest() throws Exception {
		OperationRequest r = new OperationRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);
		r.setOperation(Operation.start);

		OperationResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}

	@Test
	public void testStopOperationRequest() throws Exception {
		OperationRequest r = new OperationRequest();
		r.setBaseUrl(getTarget());
		r.setApplicationName(applicationName);
		r.setOperation(Operation.stop);

		OperationResponse s = getAdapter().send(r, getCredentials());
		System.out.println(s);
	}
}
