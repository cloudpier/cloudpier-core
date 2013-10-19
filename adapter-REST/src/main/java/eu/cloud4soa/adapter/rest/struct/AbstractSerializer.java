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


package eu.cloud4soa.adapter.rest.struct;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.cloud4soa.adapter.rest.request.Request;
import eu.cloud4soa.adapter.rest.response.Response;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public abstract class AbstractSerializer extends AbstractJsonSerializer{
	private final Logger log =  LoggerFactory.getLogger(this.getClass());

	protected <T> String serialize(Request<T> t){
		return toJSON(t);
	}
	
	protected <T> Response<T> deserialize(String response, Request<T> request){
		log.debug(response);
		
		Response<T> target = getInstanceOfParameterizedType(request);
		try{
			return fromJSON(response, target);
		}catch(com.google.gson.JsonSyntaxException jse){
			return target;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> Response<T> getInstanceOfParameterizedType(Request<T> request){
		Type superclazz = request.getClass().getGenericSuperclass();
		try{
			Type parameterizedTypeClazz = ((ParameterizedType)superclazz).getActualTypeArguments()[0];
			return (Response<T>) ((Class<T>) parameterizedTypeClazz).newInstance();
		} catch (Exception e) {
			return null;
		}
	}
}
