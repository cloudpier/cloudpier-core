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

import org.junit.Assert;
import org.junit.Test;

import eu.cloud4soa.adapter.rest.common.HttpStatus;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class HttpStatusTest {

	/*
	 * (non-javadoc) is there any code between 100 and 599 which is not defined
	 * by the enum?
	 * 
	 * There will be a ArrayIndexOutOfBoundsException thrown if the enum is
	 * incomplete. (means: a field of the array of values is null)
	 */
	@Test
	public void testCompleteness() {
		/* HttpStatus (100 - 599) */
		for (int i = 0; i < 600; i++) {
			@SuppressWarnings("unused")
			HttpStatus current = HttpStatus.getStatus(i);
		}
	}

	@Test
	public void testCorrectness() {
		HttpStatus s;

		s = HttpStatus.getStatus(1);
		Assert.assertEquals("Unassigned", s.toString());

		s = HttpStatus.getStatus(10);
		Assert.assertEquals("Unassigned", s.toString());

		s = HttpStatus.getStatus(100);
		Assert.assertEquals("Continue (100)", s.toString());

		s = HttpStatus.getStatus(101);
		Assert.assertEquals("Switching Protocols (101)", s.toString());

		s = HttpStatus.getStatus(102);
		Assert.assertEquals("Processing (102)", s.toString());

		s = HttpStatus.getStatus(200);
		Assert.assertEquals("OK (200)", s.toString());

		s = HttpStatus.getStatus(207);
		Assert.assertEquals("Multi Status (207)", s.toString());

		s = HttpStatus.getStatus(208);
		Assert.assertEquals("Already Reported (208)", s.toString());

		s = HttpStatus.getStatus(209);
		Assert.assertEquals("Unassigned", s.toString());

		s = HttpStatus.getStatus(500);
		Assert.assertEquals("Internal Server Error (500)", s.toString());

		s = HttpStatus.getStatus(510);
		Assert.assertEquals("Not Extended (510)", s.toString());

		s = HttpStatus.getStatus(400);
		Assert.assertEquals("Bad Request (400)", s.toString());

		s = HttpStatus.getStatus(410);
		Assert.assertEquals("Gone (410)", s.toString());

		s = HttpStatus.getStatus(0);
		Assert.assertEquals("Unknown (0)", s.toString());
	}
}
