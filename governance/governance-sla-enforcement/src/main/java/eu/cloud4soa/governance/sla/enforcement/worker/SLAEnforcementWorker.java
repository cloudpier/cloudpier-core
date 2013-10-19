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
package eu.cloud4soa.governance.sla.enforcement.worker;

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
import org.springframework.transaction.annotation.Transactional;

import eu.cloud4soa.governance.monitoring.MonitoringModule;
import eu.cloud4soa.governance.sla.enforcement.task.SLAEnforcementTask;
import eu.cloud4soa.relational.datamodel.ISLAEnforcementJob;
import eu.cloud4soa.relational.datamodel.SLAEnforcementJob;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import eu.cloud4soa.relational.persistence.BreachRepository;
import eu.cloud4soa.relational.persistence.GuaranteeTermRepository;
import eu.cloud4soa.relational.persistence.RecoveryActionRepository;
import eu.cloud4soa.relational.persistence.SLAContractRepository;
import eu.cloud4soa.relational.persistence.SLAEnforcementJobRepository;
import eu.cloud4soa.relational.persistence.SLAPolicyRepository;
import eu.cloud4soa.relational.persistence.SLAViolationRepository;

/**
 * 
 * Following properties must have been set before running in production mode
 * 
 * eu.cloud4soa.governance.slaenforcement.spawnlookup.cron=*\/5 * * * * *
 * eu.cloud4soa.governance.slaenforcement.poll.interval.mseconds=30000
 * 
 * 
 */
@Component
@Transactional
public class SLAEnforcementWorker implements InitializingBean {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MonitoringModule monitoringModule;
    
    @Autowired
	private ApplicationInstanceRepository applicationInstanceRepository;
    
	@Autowired
	private SLAEnforcementJobRepository slaEnforcementJobRepository;
	
	@Autowired
	private SLAViolationRepository slaViolationRepository;

	@Autowired
	private RecoveryActionRepository recoveryActionRepository;	
	
	@Autowired
	private SLAContractRepository slaContractRepository;
	
	@Autowired
	private GuaranteeTermRepository guaranteeTermRepository;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private BreachRepository breachRepository;
	
	@Autowired
	private SLAPolicyRepository slaPolicyRepository;
	
	private ThreadPoolTaskScheduler scheduler;
	
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	
	@Value("ENF{eu.cloud4soa.governance.slaenforcement.spawnlookup.cron}")
	private String cron;
	
	@Value("ENF{eu.cloud4soa.governance.slaenforcement.poll.interval.mseconds}")
	private String pollIntervalString;
	private long pollInterval;

	boolean ready = false;
	
	@Scheduled(cron = "ENF{eu.cloud4soa.governance.slaenforcement.spawnlookup.cron}")
	public void spawnMonitors(){		
		List<SLAEnforcementJob> unresolved =
				slaEnforcementJobRepository.retrieveAllUnresolved(computeOffsetDate());	
		logger.debug("spawning {"+unresolved.size()+"} future tasks");
		
		for(ISLAEnforcementJob slaEnforcementJob : unresolved){
			Date delay = computeDelay(slaEnforcementJob);
			SLAEnforcementTask slaEnforcementTask =
					new SLAEnforcementTask(slaViolationRepository,
										   recoveryActionRepository,
										   applicationInstanceRepository,
										   slaContractRepository,
										   guaranteeTermRepository,
										   slaEnforcementJob,
										   monitoringModule,
										   slaEnforcementJob.getLastExecuted(),
										   breachRepository,
										   slaPolicyRepository);
			scheduler.schedule(slaEnforcementTask, delay);			
			slaEnforcementJob.setLastExecuted(delay);
			slaEnforcementJobRepository.update(slaEnforcementJob);
		}
	}
	
	private Date computeDelay(ISLAEnforcementJob slaEnforcementJob){		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(slaEnforcementJob.getLastExecuted());
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
			throw new RuntimeException("Can not parse ENF{eu.cloud4soa.governance.slaenforcement.poll.interval.mseconds} value{"+pollIntervalString+"}. Is a number?");
		}
		
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.initialize();
		
		logger.debug("SLAEnforcementWorker registered, cron{ " + cron + " }.");
	}
}
