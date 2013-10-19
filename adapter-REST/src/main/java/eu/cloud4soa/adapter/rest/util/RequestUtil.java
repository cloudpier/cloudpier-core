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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.cloud4soa.adapter.rest.aop.Default;
import eu.cloud4soa.adapter.rest.aop.NotNull;
import eu.cloud4soa.adapter.rest.aop.Path;
import eu.cloud4soa.adapter.rest.aop.Path.Component;
import eu.cloud4soa.adapter.rest.aop.UrlPath;
import eu.cloud4soa.adapter.rest.request.Request;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class RequestUtil {
	
	public static <T> boolean isValid(Request<T> request){
		return isValid(request, true);
	}
	
	public static <T> boolean isValid(Request<T> request, boolean validateSuperclass){
		Class<?> clazz = request.getClass();		
		Class<?> superClass = clazz.getSuperclass();
		if(validateSuperclass && superClass!=null && superClass.equals(Request.class)){
			if(!isValid(superClass, request)){
				return false;
			}
		}
		return isValid(clazz, request);
	}
	
	private static <T> boolean isValid(Class<?> clazz, Request<T> request){
		Field[] declaredFields = clazz.getDeclaredFields();
		
		for(Field property : declaredFields){
			if(null != property.getAnnotation(NotNull.class)){
				property.setAccessible(true);
				try {
					Object value = property.get(request);
					if(value==null || ( (value instanceof String ? ((String)value).isEmpty() : false) )){
						return false;
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return true;
	}
	
	/**
	 * Replace null values by annotated default values if possible.
	 * 
	 * @param request
	 */
	public static <T> void infixPotentialDefaults(Request<T> request){
		infixPotentialDefaults(request, request.getClass(), true);
	}
	
	public static <T> void infixPotentialDefaults(Request<T> request, Class<?> targetClazz, boolean infixPotentialValuesOfSuperClass){
		Class<?> superClass = targetClazz.getSuperclass();
		if(infixPotentialValuesOfSuperClass && superClass!=null && superClass.equals(Request.class)){
			infixPotentialDefaults(request, superClass, infixPotentialValuesOfSuperClass);
		}
		
		Field[] declaredFields = targetClazz.getDeclaredFields();
		for(Field property : declaredFields){
			Default defaultValue = property.getAnnotation(Default.class);
			if(null != defaultValue){
				property.setAccessible(true);
				try {
					Object value = property.get(request);
					if(value==null || ( (value instanceof String ? ((String)value).isEmpty() : false) )){
						property.set(request, defaultValue.value());
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static <T> String resolveResourcePath(Request<T> request){
		Class<?> clazz = request.getClass();
		
		Component component = ClassUtil.getClassAnnotationValue(clazz, Path.class, "component", Component.class);
		String componentPathElement = component.el();
		
		String unresolvedPath = ClassUtil.getClassAnnotationValue(clazz, Path.class, "path", String.class);
		
		Map<String, String> patternMap = new HashMap<String, String>();
		for(Field field : clazz.getDeclaredFields()){
			UrlPath part = field.getAnnotation(UrlPath.class);
			if(part!=null){
				try {
					String pattern = part.pattern();
					field.setAccessible(true);
					String value = (String) field.get(request);
					patternMap.put(pattern, value);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		/*
		 * (non-javadoc)
		 * we are looking for patterns like this ${*} 
		 */
		Pattern pattern = Pattern.compile("\\$\\{(.*?)*\\}(.*?)");
		Matcher matcher = pattern.matcher(unresolvedPath);
		
		LinkedHashSet<String> placeholder = new LinkedHashSet<String>(); 
		while(matcher.find()){
			placeholder.add(matcher.group());
		}
				
		for(String key : placeholder){
			String found = patternMap.get(key);
			if(found!=null){
				unresolvedPath = unresolvedPath.replace((String)key, found);		
			}
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(componentPathElement);
		buffer.append(unresolvedPath);
		
		return buffer.toString();
	}
}
