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

import java.util.Arrays;

import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.Logger;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.cloud4soa.adapter.rest.aop.Ignore;
import eu.cloud4soa.adapter.rest.aop.UrlPath;
import eu.cloud4soa.adapter.rest.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 *
 */
public abstract class AbstractJsonSerializer {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private ExclusionStrategy responseExclusionStrategy = new ExclusionStrategy(){
		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
		@Override
		public boolean shouldSkipField(FieldAttributes attribute) {
			return false;
		}
	};
	
	private ExclusionStrategy requestExclusionStrategy = new ExclusionStrategy(){
		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
		@Override
		public boolean shouldSkipField(FieldAttributes attribute) {
			if(null != attribute.getAnnotation(Ignore.class)){
				return true;
			}
			return false;
		}
	};
	
	protected String toJSON(Object source) throws com.google.gson.JsonSyntaxException{
		GsonBuilder builder = new GsonBuilder()
			.setExclusionStrategies(requestExclusionStrategy);
		
		Gson gson = builder.create();
		
		String json = gson.toJson(source);
		return json;
	}
	
	protected <T> Response<T> fromJSON(String response, Response<T> target) throws com.google.gson.JsonSyntaxException{
		GsonBuilder builder = new GsonBuilder()
			.setExclusionStrategies(responseExclusionStrategy);
		
		Gson gson = builder.create();
		
		@SuppressWarnings("unchecked")
		Response<T> fromJson = gson.fromJson(response, target.getClass());
		if(fromJson==null){
			fromJson = target;
		}
		return fromJson;
	}
}
