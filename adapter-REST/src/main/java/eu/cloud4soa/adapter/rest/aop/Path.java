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


package eu.cloud4soa.adapter.rest.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

/**
 * Describes the <code>url path</code> on what the request has to be sent. <br>
 * <strong>Note:</strong> this @interface does not need an interceptor yet.
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface Path {
	
	/**
	 * component ROOT -> /
	 * component ems -> /ems
	 * component monitor -> /monitor
	 */
	public enum Component {

		ROOT,
		EMS,
		MONITOR;
		
		public String el(){
			if(this == ROOT){
				return "/";
			}
			return ("/"+this.name().toLowerCase(Locale.ENGLISH));
		}
	}
	
	/* the adapter component */
	Component component() default Component.ROOT;
	
	/* the request path */
	String path() default "";
}
