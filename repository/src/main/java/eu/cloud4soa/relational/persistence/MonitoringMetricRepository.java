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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud4soa.relational.persistence;

import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric.MetricKey;
import eu.cloud4soa.relational.datamodel.MonitoringJob;
import eu.cloud4soa.relational.datamodel.MonitoringMetric;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vervas
 */
@Repository
public class MonitoringMetricRepository extends AbstractHbmDao<MonitoringMetric> implements IMonitoringMetricRepository {
    
    @Autowired
    public MonitoringMetricRepository(SessionFactory sessionFactory) {
            super(sessionFactory, MonitoringMetric.class);
    }


    public void store(MonitoringMetric monitoringMetric) {
            this.saveOrUpdate(monitoringMetric);
    }

    public void delete(MonitoringMetric monitoringMetric) {
            super.delete(monitoringMetric);
    }
        
    @Override
    public List<MonitoringMetric> retrieveAllInRangeforMetric(MonitoringJob monitoringJob, MetricKey metricKey, Date start, Date end, int limit){
        if(monitoringJob==null){
                throw new RuntimeException("MonitoringJob can not be NULL!");
        }

        java.sql.Timestamp sqlStart = new java.sql.Timestamp(start.getTime());
        java.sql.Timestamp sqlEnd = new java.sql.Timestamp(end.getTime());

        String queryString = String.format("from MonitoringMetric WHERE monitoringJob = :monitoringJob AND metricKey = :metricKey AND date >= :start AND date <= :end ORDER BY date DESC");

        System.out.println(sqlStart);
        System.out.println(sqlEnd);

        Query query = getSession()
                        .createQuery(queryString)
                        .setParameter("monitoringJob", monitoringJob)
                        .setParameter("metricKey", metricKey)
                        .setTimestamp("start", sqlStart)
                        .setTimestamp("end", sqlEnd)
                        .setMaxResults(limit);

        System.out.println(queryString.toString());

        @SuppressWarnings("unchecked")
        List<MonitoringMetric> resultSet = ((List<MonitoringMetric>)query.list());

        return resultSet;
    }

}
