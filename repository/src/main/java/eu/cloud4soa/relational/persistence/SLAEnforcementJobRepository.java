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

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.relational.datamodel.SLAEnforcementJob;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;


@Repository
public class SLAEnforcementJobRepository extends AbstractHbmDao<SLAEnforcementJob> implements ISLAEnforcementJobRepository {

	@Autowired
	public SLAEnforcementJobRepository(SessionFactory sessionFactory) {
		super(sessionFactory, SLAEnforcementJob.class);
	}
	
	public void store(SLAEnforcementJob slaEnforcementJob) {
		this.saveOrUpdate(slaEnforcementJob);
	}

	public void update(SLAEnforcementJob slaEnforcementJob) {
		super.update(slaEnforcementJob);
	}

	public void delete(SLAEnforcementJob slaEnforcementJob) {
		super.delete(slaEnforcementJob);
	}

	public List<SLAEnforcementJob> retrieveAll() {
		return this.findAll();
	}
	
	public List<SLAEnforcementJob> retrieveAllUnresolved(Date offset){				
		List<SLAEnforcementJob> unresolved = this.find("enabled = true AND lastExecuted < ?",offset);
		return unresolved;
	}

	public SLAEnforcementJob retrieveByApplicationInstance(ApplicationInstance applicationInstance) {
		return this.findUniqueBy("applicationInstanceUriId", applicationInstance.getUriId());
	}
}
