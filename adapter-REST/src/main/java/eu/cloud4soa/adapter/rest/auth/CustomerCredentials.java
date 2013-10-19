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


package eu.cloud4soa.adapter.rest.auth;


/**
 * Wrapper to provide a keyPair to access the adapter 
 * that lies on the PaaS
 * 
 * Implements the interface {@link #Credentials}
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class CustomerCredentials implements Credentials{

	private String apiKey;
	private String secretKey;
	
	public CustomerCredentials(){
	}

	public CustomerCredentials(String apiKey, String secretKey){
		this.apiKey = apiKey;
		this.secretKey = secretKey;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
