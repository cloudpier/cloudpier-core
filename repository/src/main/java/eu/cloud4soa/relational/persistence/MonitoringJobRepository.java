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
import eu.cloud4soa.relational.datamodel.MonitoringJob;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;


/**
 * 
 * @author denisneuling <dn@cloudcontrol.de>
 *
 */
@Repository
public class MonitoringJobRepository extends AbstractHbmDao<MonitoringJob> implements IMonitoringJobRepository {

	@Autowired
	public MonitoringJobRepository(SessionFactory sessionFactory) {
		super(sessionFactory, MonitoringJob.class);
	}
	
	public void store(MonitoringJob monitoringJob) {
		this.saveOrUpdate(monitoringJob);
	}

	public void update(MonitoringJob monitoringJob) {
		super.update(monitoringJob);
	}

	public void delete(MonitoringJob monitoringJob) {
		super.delete(monitoringJob);
	}

	public List<MonitoringJob> retrieveAll() {
		return this.findAll();
	}
	
	/**
	 * would be better to write a nice query
	 */
	public List<MonitoringJob> retrieveAllUnresolved(Date offset){				
		List<MonitoringJob> unresolved = this.find("enabled = 1 AND lastExecuted < ?",offset);
		return unresolved;
	}

	public MonitoringJob retrieveByApplicationInstance(ApplicationInstance applicationInstance) {
        return retrieveByApplicationUriId(applicationInstance.getUriId());
	}

    public MonitoringJob retrieveByApplicationUriId(String applicationUriId) {
        return this.findUniqueBy("applicationInstanceUriId", applicationUriId);
    }
}
