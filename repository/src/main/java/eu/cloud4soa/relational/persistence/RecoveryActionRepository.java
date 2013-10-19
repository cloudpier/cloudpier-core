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


package eu.cloud4soa.relational.persistence;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eu.cloud4soa.relational.datamodel.ApplicationInstance;
import eu.cloud4soa.relational.datamodel.RecoveryAction;
import eu.cloud4soa.relational.datamodel.SLAContract;
import eu.cloud4soa.relational.datamodel.SLAViolation;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;


@Repository
public class RecoveryActionRepository extends AbstractHbmDao<RecoveryAction> {
	
	@Autowired
	public RecoveryActionRepository(SessionFactory session_factory) {
		super(session_factory, RecoveryAction.class);
	}	

	public void store(RecoveryAction decision) {
		this.saveOrUpdate(decision);
	}

	public void delete(RecoveryAction decision) {
		super.delete(decision);
	}

	public List<RecoveryAction> retrieveAllDecisions() {
		List<RecoveryAction> recoveryActions = this.findAll();
		return recoveryActions;
	}

	public List<RecoveryAction> retrieveAllDecisions(ApplicationInstance application_instance) {
		List<RecoveryAction> recoveryActions = this.findBy("application_instance",
															application_instance);
		return recoveryActions;
	}
	
	public List<RecoveryAction> retrieveAllDecisions(SLAContract sla_contract) {
		List<RecoveryAction> recoveryActions = this.findBy("sla_contract",
															sla_contract);
		return recoveryActions;
	}
}
