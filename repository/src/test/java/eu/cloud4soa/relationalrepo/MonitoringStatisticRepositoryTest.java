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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.relational.datamodel.MonitoringJob;
import eu.cloud4soa.relational.datamodel.MonitoringStatistic;
import eu.cloud4soa.relational.persistence.MonitoringJobRepository;
import eu.cloud4soa.relational.persistence.MonitoringStatisticRepository;


/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:repository-context.xml"})
public class MonitoringStatisticRepositoryTest {
	Logger log = Logger.getRootLogger();

	@Autowired
	private MonitoringStatisticRepository monitoringStatisticRepository;
	
	@Autowired
	@SuppressWarnings("unused")
	private MonitoringJobRepository monitoringJobRepository;
	
	private IMonitoringJob monitoringJob;
	private MonitoringStatistic monitoringStatistic;
	
	@Before()
	public void setUp(){
		monitoringStatistic = new MonitoringStatistic();
	}
	
	@Test
	@Ignore
	public void testStore(){
//		monitoringStatisticRepository.save(monitoringStatistic);
		/*
		 * this is corrupted since we use a database with almost 7 million entries of statistics
		 */
//		Assert.assertTrue(0<monitoringStatisticRepository.retrieveAllMonitoringStatistics().size());
	}
	
	@Test
	@Ignore
	public void testSelect() throws ParseException{
		monitoringJob = new MonitoringJob();
		monitoringJob.setId(new Long(18));
		
		String[] pattern = new String[1];
		pattern[0] = "yyyy-MM-dd HH:mm:ss";
		
		Date start = DateUtils.parseDate("2012-07-02 04:06:56", pattern);
		Date end = DateUtils.parseDate("2012-07-02 16:01:33", pattern);
		
		List<MonitoringStatistic> list = monitoringStatisticRepository.retrieveAllInRangeLimited(monitoringJob, start, end, 500);
		System.out.println(list.size());
	}
	
	@Ignore
	@Test
	@Transactional
	public void testDelete(){
		monitoringStatisticRepository.save(monitoringStatistic);
		int size = monitoringStatisticRepository.retrieveAllMonitoringStatistics().size();
		monitoringStatisticRepository.delete(monitoringStatistic);
		int nsize = monitoringStatisticRepository.retrieveAllMonitoringStatistics().size();
		Assert.assertTrue(size>nsize);
	}
	
	@Test
	@Ignore
	public void testRetrieveAllInRangeLimited(){
		Date start = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		Date end = calendar.getTime();
		
		monitoringJob = new MonitoringJob();
		
		monitoringStatistic = new MonitoringStatistic();
		monitoringStatistic.setDate(new Date());
		monitoringStatistic.setMonitoringJobId(0);
		for(int i = 0; i <20 ; i++){
			monitoringStatisticRepository.save(monitoringStatistic);
		}
		
		Assert.assertEquals(20, monitoringStatisticRepository.retrieveAllMonitoringStatistics().size());
		
		List<MonitoringStatistic> results = monitoringStatisticRepository.retrieveAllInRangeLimited(monitoringJob, start, end, 10);
		Assert.assertEquals(10, results.size());
	}
}
