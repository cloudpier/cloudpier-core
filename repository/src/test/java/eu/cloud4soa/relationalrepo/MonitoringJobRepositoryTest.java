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

import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.relational.datamodel.MonitoringJob;
import eu.cloud4soa.relational.persistence.MonitoringJobRepository;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 *
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:repository-context.xml"})
public class MonitoringJobRepositoryTest {
 
	final Logger logger = LoggerFactory.getLogger(MonitoringJobRepositoryTest.class);
	
	private IMonitoringJob monitoringJob;
	
	@Autowired
	private MonitoringJobRepository monitoringJobRepository;
	
	@Before
	public void setUp(){
		monitoringJob = new MonitoringJob();
	}
	
	@Test
	public void testWireing(){
		Assert.assertNotNull(monitoringJobRepository);
	}
	
	@Test
	public void testStoreMonitoringJob(){
		try{
			monitoringJobRepository.store((MonitoringJob)monitoringJob);
			Assert.assertTrue(monitoringJobRepository.retrieveAll().size() > 0);
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testUpdateIMonitoringJob(){
		try{
			//save
			monitoringJobRepository.store((MonitoringJob) monitoringJob);
			monitoringJobRepository.refresh(monitoringJob);
			List <MonitoringJob> jobs = monitoringJobRepository.retrieveAll();
			Assert.assertTrue(jobs.size()>0);
			//update
			monitoringJob.setEnabled(true);
			monitoringJobRepository.update(monitoringJob);
			
			MonitoringJob copy = monitoringJobRepository.findById(monitoringJob.getId());
			Assert.assertEquals(monitoringJob, copy);			
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testDeleteMonitoringJob(){
		try{
			monitoringJobRepository.store((MonitoringJob)monitoringJob);
			int size = monitoringJobRepository.retrieveAll().size();
			Assert.assertTrue(0<size);
			monitoringJobRepository.delete(monitoringJob);
			int nsize = monitoringJobRepository.retrieveAll().size();
			Assert.assertEquals(size-1, nsize);
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
}
