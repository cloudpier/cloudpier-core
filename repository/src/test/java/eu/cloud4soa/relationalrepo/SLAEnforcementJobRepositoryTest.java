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


package eu.cloud4soa.relationalrepo;


import eu.cloud4soa.relational.datamodel.ISLAEnforcementJob;
import eu.cloud4soa.relational.datamodel.SLAEnforcementJob;
import eu.cloud4soa.relational.persistence.SLAEnforcementJobRepository;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:repository-context.xml"})
public class SLAEnforcementJobRepositoryTest {
 
	final Logger logger = LoggerFactory.getLogger(MonitoringJobRepositoryTest.class);
	
	private ISLAEnforcementJob slaEnforcementJob;
	
	@Autowired
	private SLAEnforcementJobRepository slaEnforcementJobRepository;
	
	@Before
	public void setUp(){
		slaEnforcementJob = new SLAEnforcementJob();
	}
	
	@Test
	public void testWireing(){
		Assert.assertNotNull(slaEnforcementJobRepository);
	}
	
	@Test
	public void testStoreMonitoringJob(){
		try{
			slaEnforcementJobRepository.store((SLAEnforcementJob)slaEnforcementJob);
			Assert.assertTrue(slaEnforcementJobRepository.retrieveAll().size() > 0);
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testUpdateIMonitoringJob(){
		try{
			//save
			slaEnforcementJobRepository.store((SLAEnforcementJob) slaEnforcementJob);
			slaEnforcementJobRepository.refresh(slaEnforcementJob);
			List <SLAEnforcementJob> jobs = slaEnforcementJobRepository.retrieveAll();
			Assert.assertTrue(jobs.size()>0);
			//update
			slaEnforcementJob.setEnabled(true);
			slaEnforcementJobRepository.update(slaEnforcementJob);
			
			SLAEnforcementJob copy = slaEnforcementJobRepository.findById(slaEnforcementJob.getId());
			Assert.assertEquals(slaEnforcementJob, copy);			
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testDeleteMonitoringJob(){
		try{
			slaEnforcementJobRepository.store((SLAEnforcementJob)slaEnforcementJob);
			int size = slaEnforcementJobRepository.retrieveAll().size();
			Assert.assertTrue(0<size);
			slaEnforcementJobRepository.delete(slaEnforcementJob);
			int nsize = slaEnforcementJobRepository.retrieveAll().size();
			Assert.assertEquals(size-1, nsize);
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
}
