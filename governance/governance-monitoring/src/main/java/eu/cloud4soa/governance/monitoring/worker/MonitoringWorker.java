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
package eu.cloud4soa.governance.monitoring.worker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import eu.cloud4soa.adapter.rest.AdapterClient;
import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.governance.monitoring.task.MonitoringTask;
import eu.cloud4soa.governance.monitoring.worker.util.TaskUtils;
import eu.cloud4soa.relational.datamodel.MonitoringJob;
import eu.cloud4soa.relational.persistence.MonitoringJobRepository;
import eu.cloud4soa.relational.persistence.MonitoringMetricRepository;
import eu.cloud4soa.relational.persistence.MonitoringStatisticRepository;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;


/**
 * 
 * Following properties must have been set before running in production mode
 * 
 * eu.cloud4soa.governance.monitoring.spawnlookup.cron=*\/5 * * * * *
 * eu.cloud4soa.governance.monitoring.poll.interval.seconds=10
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 * 
 */
@Component
public class MonitoringWorker implements InitializingBean {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AdapterClient adapterClient;
	
	@Autowired
	private MonitoringJobRepository monitoringJobRepository;
	
	@Autowired
	private MonitoringStatisticRepository monitoringStatisticRepository;
        
	@Autowired
	private MonitoringMetricRepository monitoringMetricRepository;
        
	@Autowired
	private ApplicationInstanceRepository applicationInstanceRepository;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	private ThreadPoolTaskScheduler scheduler;
	
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Value("MON{eu.cloud4soa.governance.monitoring.spawnlookup.cron}")
	private String cron;
	
	@Value("MON{eu.cloud4soa.governance.monitoring.poll.interval.mseconds}")
	private String pollIntervalString;
	private long pollInterval;

	boolean ready = false;

	/* (non-Javadoc)
	 * @see eu.cloud4soa.governance.monitoring.worker.IMonitoringWorker#spawnMonitors()
	 */
	@Scheduled(cron = "MON{eu.cloud4soa.governance.monitoring.spawnlookup.cron}")
	public void spawnMonitors(){		
		List<MonitoringJob> unresolved = monitoringJobRepository.retrieveAllUnresolved(computeOffsetDate());	
		logger.debug("spawning {"+unresolved.size()+"} future tasks");
		
		for(IMonitoringJob monitoringJob : unresolved){
		
			Date delay = computeDelay(monitoringJob);
			MonitoringTask monitoringTask = TaskUtils.createMonitoringTask(monitoringStatisticRepository, monitoringMetricRepository, applicationInstanceRepository, adapterClient, monitoringJob);
			scheduler.schedule(monitoringTask, delay);
			
			monitoringJob.setLastExecuted(delay);
			monitoringJobRepository.update(monitoringJob);
		}
	}
	
	private Date computeDelay(IMonitoringJob monitoringJob){		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, (int)pollInterval/1000);
		
		return calendar.getTime();
	}
	
	private Date computeOffsetDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MILLISECOND, -((int)(pollInterval)));
		return calendar.getTime();
	}

	public void afterPropertiesSet() throws Exception {
		
		try {
			pollInterval = Long.parseLong(pollIntervalString);
		}catch(NumberFormatException npe){
			throw new RuntimeException("Can not parse MON{eu.cloud4soa.governance.monitoring.poll.interval.seconds} value{"+pollIntervalString+"}. Is a number?");
		}
		
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.initialize();
		
		logger.debug("MonitoringWorker registered, cron{ " + cron + " }.");
	}
}
