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

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.relational.datamodel.MonitoringStatistic;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;

/**
 * 
 * @author denisneuling <dn@cloudcontrol.de>
 * 
 */
@Repository
public class MonitoringStatisticRepository extends AbstractHbmDao<MonitoringStatistic> implements IMonitoringStatisticRepository {

	@Autowired
	public MonitoringStatisticRepository(SessionFactory sessionFactory) {
		super(sessionFactory, MonitoringStatistic.class);
	}

	public void store(MonitoringStatistic monitoringStatistic) {
		this.saveOrUpdate(monitoringStatistic);
	}

	public void delete(MonitoringStatistic monitoringStatistic) {
		/*
		 * (non-Javadoc) TODO change naming,
		 * MonitoringStatisticRepository#delete to something else
		 */
		super.delete(monitoringStatistic);
	}

	public List<MonitoringStatistic> retrieveAllMonitoringStatistics() {
		return this.findAll();
	}

	public List<MonitoringStatistic> retrieveAllMonitoringStatistics(IMonitoringJob monitoringJob) {
		return this.findBy("monitoringJobId", monitoringJob.getId());
	}

	public List<MonitoringStatistic> retrieveAllInRange(IMonitoringJob monitoringJob, Date start, Date end) {
		return this.find("monitoringJobId=? AND date>=? AND date<=? ORDER BY date DESC ", monitoringJob.getId(), start, end);
	}

	public List<MonitoringStatistic> retrieveAllInRangeLimited(IMonitoringJob monitoringJob, Date start, Date end, int limit){
		if(monitoringJob==null){
			throw new RuntimeException("MonitoringJob can not be NULL!");
		}

		java.sql.Timestamp sqlStart = new java.sql.Timestamp(start.getTime());
		java.sql.Timestamp sqlEnd = new java.sql.Timestamp(end.getTime());
		
		String queryString = String.format("from MonitoringStatistic WHERE monitoringJobId = :monitoringJobId AND date >= :start AND date <= :end ORDER BY date DESC");
				
		System.out.println(sqlStart);
		System.out.println(sqlEnd);
		
		Query query = getSession()
				.createQuery(queryString)
				.setParameter("monitoringJobId", monitoringJob.getId())
				.setTimestamp("start", sqlStart)
				.setTimestamp("end", sqlEnd)
				.setMaxResults(limit);
		
		System.out.println(queryString.toString());
		
		@SuppressWarnings("unchecked")
		List<MonitoringStatistic> resultSet = ((List<MonitoringStatistic>)query.list());
		
		return resultSet;
	}

}
