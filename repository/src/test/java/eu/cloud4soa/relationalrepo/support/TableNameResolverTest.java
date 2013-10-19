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


package eu.cloud4soa.relationalrepo.support;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloud4soa.relational.datamodel.AbstractModel;
import eu.cloud4soa.relational.datamodel.MonitoringJob;
import eu.cloud4soa.relational.persistence.support.TableNameResolver;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:repository-context.xml"})
public class TableNameResolverTest {

	@Autowired
	private TableNameResolver tableNameResolver;
	
	private MonitoringJob entity;
	
	@Before
	public void setUp(){
		entity = new MonitoringJob();
	}
	
	@Test
	public void testWiring(){
		Assert.assertNotNull(tableNameResolver);
	}
	
	@Test
	public void testResolve(){
		String tName = tableNameResolver.resolveTableName(entity);
		Assert.assertEquals("Monitoringjob", tName);
		
		Assert.assertEquals("dummyObject", tableNameResolver.resolveTableName(new DummyObject()));
		
		try {
			Assert.assertEquals("nonEntity", tableNameResolver.resolveTableName(new NonEntity()));
			Assert.fail();
		}catch(RuntimeException ex) {
		}
	}
}

@SuppressWarnings("serial")
class NonEntity extends AbstractModel<NonEntity>{

	@Id
	@GeneratedValue
	private Long id;
	
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
}

@Entity
@SuppressWarnings("serial")
class DummyObject extends AbstractModel<DummyObject> {

	@Id
	@GeneratedValue
	private Long id;
	
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
