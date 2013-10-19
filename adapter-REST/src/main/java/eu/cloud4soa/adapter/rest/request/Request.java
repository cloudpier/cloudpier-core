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


package eu.cloud4soa.adapter.rest.request;

import java.io.Serializable;

import eu.cloud4soa.adapter.rest.aop.Ignore;
import eu.cloud4soa.adapter.rest.aop.Method;
import eu.cloud4soa.adapter.rest.aop.Method.HttpMethod;
import eu.cloud4soa.adapter.rest.aop.NotNull;
import eu.cloud4soa.adapter.rest.aop.Path;
import eu.cloud4soa.adapter.rest.aop.Path.Component;
import eu.cloud4soa.adapter.rest.aop.Version;

/**
 * Superclass of all request objects
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Version("1.0")
@Method(HttpMethod.GET)
@Path(component=Component.ROOT)
public abstract class Request<T> implements Serializable{
	private static final long serialVersionUID = -8271055145789777669L;
	
	@Ignore
	private int PORT;
	
	@Ignore
	private String BASEURL;
	
	@NotNull @Ignore
	private String apiKey;
	
	@Ignore
	private String hash;

	public String getBaseUrl() {
		return BASEURL;
	}

	public void setBaseUrl(String baseUrl) {
		this.BASEURL = baseUrl;
	}

	public int getPort() {
		return PORT;
	}

	public void setPort(int port) {
		this.PORT = port;
	}

	/* hash and api key*/
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	/* hash and api key eom*/
}
