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
package eu.cloud4soa.governance.sla.decisor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.Calendar;

import eu.cloud4soa.relational.datamodel.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.SlaContract;
import eu.cloud4soa.api.datamodel.governance.SlaPolicy;
import eu.cloud4soa.api.datamodel.governance.SlaPolicy.SlaPenaltyType;
import eu.cloud4soa.api.datamodel.governance.SlaTemplate.ServiceGuaranteeType;
import eu.cloud4soa.relational.datamodel.Breach;
import eu.cloud4soa.relational.datamodel.SLAPolicy;
import eu.cloud4soa.relational.datamodel.RecoveryAction;
import eu.cloud4soa.relational.datamodel.SLAContract;
import eu.cloud4soa.relational.datamodel.SLAViolation;
import eu.cloud4soa.relational.persistence.RecoveryActionRepository;
import eu.cloud4soa.relational.persistence.SLAPolicyRepository;
import eu.cloud4soa.relational.persistence.SLAViolationRepository;

public class SLADecisor {
	
	final Logger logger = LoggerFactory.getLogger(SLADecisor.class);
	
	private SLAViolationRepository   sLAViolationRepository;
	private SLAPolicyRepository      slaPolicyRepository;
	private RecoveryActionRepository recoveryActionRepository;	
	private SLAContract	          slaContract;
	private ApplicationInstance      applicationInstance;
	
	public SLADecisor (RecoveryActionRepository recoveryActionRepository,
			           SLAViolationRepository   sLAViolationRepository,
			           SLAContract              slaContract,
			           ApplicationInstance      applicationInstance,
			           SLAPolicyRepository      slaPolicyRepository) {
		this.recoveryActionRepository = recoveryActionRepository;
		this.sLAViolationRepository   = sLAViolationRepository;
		this.slaContract              = slaContract;
		this.applicationInstance      = applicationInstance;
		this.slaPolicyRepository      = slaPolicyRepository;
	}
	
	public void createRecoveryAction(SLAContract  contract,
									    SLAViolation violation) {
		SLAPolicy slaPolicy = slaPolicyRepository.findById(violation.getSlaPolicyId());
		
		RecoveryAction recoveryAction = new RecoveryAction();
		recoveryAction.setApplication_instance(applicationInstance);
		recoveryAction.setDate_and_time(Calendar.getInstance().getTime());
		recoveryAction.setSla_contract(slaContract);
		recoveryAction.setSla_violation(violation);
		
		if (slaPolicy != null) {
			recoveryAction.setAction(slaPolicy.getValue_expr());
		} else {
			recoveryAction.setAction(SlaPenaltyType.WARNING);
		}
		
		recoveryActionRepository.save(recoveryAction);
		
		violation.setRecoveryAction(recoveryAction);
		sLAViolationRepository.save(violation);
	}
}
