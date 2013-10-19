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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.junit.Test;

import eu.cloud4soa.adapter.rest.auth.Credentials;
import eu.cloud4soa.adapter.rest.request.CreateApplicationRequest;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class EncryptionUtilTest {

	@Test
	public void testGenerateCredentials() throws NoSuchAlgorithmException{
		Credentials credentials = EncryptionUtil.generateCredentials();
			
		Assert.assertNotNull(credentials);
		
		Assert.assertNotNull(credentials.getApiKey());
		Assert.assertNotNull(credentials.getSecretKey());
		
		Assert.assertFalse(credentials.getApiKey().isEmpty());
		Assert.assertFalse(credentials.getSecretKey().isEmpty());
		
		System.out.println(credentials.getApiKey());
		System.out.println(credentials.getSecretKey());
	}
	
	@Test
	public void testEncypher() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		
		Credentials credentials = EncryptionUtil.generateCredentials();
		
		CreateApplicationRequest r = new CreateApplicationRequest();
		r.setApplicationName("myApplication");
		r.setBaseUrl("cloudcontrolled.com");
		
		EncryptionUtil.encipher(r, credentials);
		
		Assert.assertFalse(RequestUtil.isValid(r));
		
		r.setLanguage("php");
		
		Assert.assertTrue(RequestUtil.isValid(r));
	}
}
