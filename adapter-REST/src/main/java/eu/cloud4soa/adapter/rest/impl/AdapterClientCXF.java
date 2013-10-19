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


package eu.cloud4soa.adapter.rest.impl;

//import org.springframework.stereotype.Component;

import eu.cloud4soa.adapter.rest.AdapterClient;
import eu.cloud4soa.adapter.rest.auth.Credentials;
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.exception.RequestValidationException;
import eu.cloud4soa.adapter.rest.request.Request;
import eu.cloud4soa.adapter.rest.util.RequestUtil;
import java.net.UnknownHostException;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 *
 */
//@Component
public class AdapterClientCXF extends AbstractAdapterClientCXF implements AdapterClient{
	
	/*
	 * @(non-javadoc)
	 * the place where the adapter lies
	 * 
	 * /application1/
	 * 			    |`- c4s/
	 * 				|       `- monitor/
	 * 				|	    `- ems/	
	 * 				|
	 * 			     `- index		
	 * 
	 */
//	@Value("ADA{eu.cloud4soa.adapter.rest.root.target}")
	private String adapterRoot = "/c4s";
	
	@Override
	public <T> T send(Request<T> t, Credentials credentials) throws AdapterClientException,UnknownHostException {
		t.setApiKey(credentials.getApiKey());
		
		if(validateAndInfixDefaultsIfPossible(t)){
			return this.request(t, credentials);
		}else{
			throw new RequestValidationException(t.getClass().getSimpleName()+" is invalid. "+ 
						"There are fields, which may not be null or empty");
		}
	}
	
	@Override
	public <T> boolean validateAndInfixDefaultsIfPossible(Request<T> request){
		RequestUtil.infixPotentialDefaults(request);
	    return RequestUtil.isValid(request, true);
	}
	
	@Override
	public String getAdapterRootPath() {
		return adapterRoot;
	}
}
