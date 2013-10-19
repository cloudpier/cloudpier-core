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


package eu.cloud4soa.relational.persistence.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Table;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import eu.cloud4soa.relational.datamodel.AbstractModel;

@Component
public class TableNameResolver implements InitializingBean {

	@SuppressWarnings("rawtypes")
	private HashMap<Class, String> registry;

	private Table getInstanceOfAnnotatedTable(List<Annotation> annotations) {
		for (Annotation annotation : annotations) {
			if (annotation instanceof javax.persistence.Table) {
				return (javax.persistence.Table) annotation;
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String resolve(Class clazz) {

		Annotation typed = clazz.getAnnotation(javax.persistence.Entity.class);
		if (typed != null) {
			List<Annotation> annotations = CollectionUtils.arrayToList(clazz.getAnnotations());
			Table table;
			if ((table = getInstanceOfAnnotatedTable(annotations)) != null) {
				String name = table.name();
				if (name != null && !name.isEmpty()) {
					return name;
				} else {
					return classToTableName(clazz);
				}
			}
			return classToTableName(clazz);
		} else {
			throw new RuntimeException(clazz.getCanonicalName() + " is not of type javax.persistence.Entity.");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static String classToTableName(Class clazz) {
		return firstCharToLowerCase(clazz.getSimpleName());
	}

	public static String firstCharToLowerCase(String string) {
		if (string == null || string.isEmpty()) {
			return null;
		}

		String first = string.substring(0, 1);
		return first.toLowerCase() + string.substring(1);
	}

	@SuppressWarnings("rawtypes")
	public String resolveTableName(AbstractModel entity) {
		return resolve(entity.getClass());
	}

	public <T> String resolveTableName(Class<T> clazz) {
		String name = registry.get(clazz);
		if(name==null){
			name = resolve(clazz);
			registry.put(clazz, name);
		}
		return name;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void afterPropertiesSet() throws Exception {
		registry = new HashMap<Class, String>();
	}

}
