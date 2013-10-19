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
package eu.cloud4soa.governance.sla.enforcement;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import eu.cloud4soa.api.datamodel.core.ApplicationInstance;

import eu.cloud4soa.api.governance.sla.enforcement.ISLAViolation;
import eu.cloud4soa.governance.sla.enforcement.worker.SLAEnforcementWorker;
import eu.cloud4soa.relational.datamodel.ISLAEnforcementJob;
import eu.cloud4soa.relational.datamodel.SLAEnforcementJob;
import eu.cloud4soa.relational.persistence.RecoveryActionRepository;
import eu.cloud4soa.relational.persistence.SLAEnforcementJobRepository;
import eu.cloud4soa.relational.persistence.SLAViolationRepository;

/**
 * Properties which have to been set before running the application
 * 
 * eu.cloud4soa.governance.slaenforcement.spawnlookup.cron=*\/5 * * * * * means that
 * threads will be spawned every 5 seconds if slaenforcement of that application is
 * enabled and the slaenforcement is not polling yet
 * 
 * eu.cloud4soa.governance.slaenforcement.poll.interval.mseconds=30000 means that every
 * 30 seconds metrics/statistics will be polled from the monitoring module
 * 
 * See eu.cloud4soa.governance.sla.enforcement.worker.SLAEnforcementWorker
 * 
 */
@Component
public class SLAEnforcementModule implements eu.cloud4soa.api.governance.SLAEnforcementModule{
	
	final Logger logger = LoggerFactory.getLogger(SLAEnforcementModule.class);
	
	@Autowired
	private SLAViolationRepository slaViolationRepository;

	@Autowired
	private SLAEnforcementJobRepository slaEnforcementJobRepository;

	@Autowired
	private SLAEnforcementWorker  slaEnforcementWorker;

	public void startEnforcement(ApplicationInstance applicationInstance) {
		SLAEnforcementJob retrievedJob = (SLAEnforcementJob) getSLAEnforcementJob(applicationInstance);
		if (retrievedJob == null) {
			retrievedJob = createSLAEnforcementJob(applicationInstance);
			//TODO: do we need this AdapterCredentials logic?
			//updateAdapterCredentials(applicationInstance, retrievedJob);
			addSLAEnforcementJob(retrievedJob);
		}
		retrievedJob.setEnabled(true);
		slaEnforcementJobRepository.saveOrUpdate(retrievedJob);
	}
	
	private SLAEnforcementJob createSLAEnforcementJob(ApplicationInstance applicationInstance) {
		SLAEnforcementJob slaEnforcementJob = new SLAEnforcementJob();
		slaEnforcementJob.setApplicationInstanceUri(applicationInstance.getUriId());
		slaEnforcementJob.setSlaContractId(applicationInstance.getSLAcontractID());
		slaEnforcementJob.setEnabled(true);
		return slaEnforcementJob;
	}

	public ISLAEnforcementJob getSLAEnforcementJob(ApplicationInstance applicationInstance) {
		SLAEnforcementJob retrievedJob = slaEnforcementJobRepository.retrieveByApplicationInstance(applicationInstance);
		//TODO: do we need this AdapterCredentials logic?
		//if (retrievedJob != null) {
		//	updateAdapterCredentials(applicationInstance, retrievedJob);
		//}
		return retrievedJob;
	}
	
	private void addSLAEnforcementJob(SLAEnforcementJob slaEnforcementJob) {
		slaEnforcementJobRepository.store(slaEnforcementJob);
	}

	public void stopEnforcement(ApplicationInstance applicationInstance) {
		SLAEnforcementJob retrievedJob = (SLAEnforcementJob) getSLAEnforcementJob(applicationInstance);
		if (retrievedJob != null) {
			retrievedJob.setEnabled(false);
			slaEnforcementJobRepository.flush();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ISLAViolation> getSLAViolations(ApplicationInstance applicationInstance) {
		SLAEnforcementJob slaEnforcementJob = (SLAEnforcementJob) getSLAEnforcementJob(applicationInstance);
		List<ISLAViolation> slaViolations = new LinkedList<ISLAViolation>();
		if (slaEnforcementJob != null) {
			slaViolations = (List<ISLAViolation>) (List<?>) slaViolationRepository.retrieveAllSLAViolations(slaEnforcementJob);
		}		
		return slaViolations;
	}
	
	public List<ISLAViolation> getSLAViolations(ApplicationInstance applicationInstance, String status) {
		//TODO: ignoring status parameter for now, add logic to query based on the status
		return getSLAViolations(applicationInstance);
	}
	
	public List<ISLAViolation> getSLAViolationsWithinDateRange(
			ApplicationInstance applicationInstance, String status, Date start,
			Date end) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ISLAViolation> getSLAViolationsWithOffset(
			ApplicationInstance applicationInstance, String status, Date start,
			Date end, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}
}
