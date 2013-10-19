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
package eu.cloud4soa.governance.monitoring;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import eu.cloud4soa.api.repository.ApplicationProfilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import eu.cloud4soa.adapter.rest.auth.Credentials;
import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric.MetricKey;
import eu.cloud4soa.api.governance.monitoring.IMonitoringStatistic;
import eu.cloud4soa.governance.monitoring.factory.MonitoringModelFactory;
import eu.cloud4soa.governance.monitoring.worker.MonitoringWorker;
import eu.cloud4soa.relational.datamodel.MonitoringJob;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import eu.cloud4soa.relational.persistence.MonitoringJobRepository;
import eu.cloud4soa.relational.persistence.MonitoringMetricRepository;
import eu.cloud4soa.relational.persistence.MonitoringStatisticRepository;
import org.apache.commons.lang.NotImplementedException;

/**
 * Properties which have to been set before running the application
 *
 * eu.cloud4soa.governance.monitoring.spawnlookup.cron=*\/5 * * * * * means that
 * threads will be spawned every 5 seconds if monitoring of that application is
 * enabled and the monitor is not polling yet
 *
 * eu.cloud4soa.governance.monitoring.poll.interval.seconds=10 means that every
 * 10 seconds metrics/statistics will be polled from the PaaS provider
 *
 *
 * @author vincenzo
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Component
public class MonitoringModule implements eu.cloud4soa.api.governance.MonitoringModule {

    final Logger logger = LoggerFactory.getLogger(MonitoringModule.class);
    /**
     * FIXME retrieve the credentials from the applicationInstance but there is
     * no repository what i could use to persist those data
     */
    private Credentials adapterCredentials = new CustomerCredentials(
            "86168c87685da592b857cdf1b5379b73b967b31c59516c93d824f1e734163e062441af21f4445c9b84599d10befd258d", 
            "1d6459840547b1e0ef83f463b5eaf2bf0248d1ae68cbb3e84234af08db3337136ecfd741a0a6a74b1f81c75be44dc921");
    @Autowired
    private MonitoringStatisticRepository monitoringStatisticRepository;
    @Autowired
    private MonitoringMetricRepository monitoringMetricRepository;
    @Autowired
    private MonitoringJobRepository monitoringJobRepository;
    @Qualifier("applicationProfilesRepository")
    @Autowired
    private ApplicationProfilesRepository applicationProfilesRepository;
    @Autowired
    ApplicationInstanceRepository applicationInstanceRepository;
    @Autowired
    @SuppressWarnings("unused")
    private MonitoringWorker monitoringWorker;

    public void startMonitoringJob(ApplicationInstance applicationInstance) {

        logger.info("Starting monitoring for applicationInstance " + applicationInstance.getUriId());

        MonitoringJob retrievedJob = (MonitoringJob) getMonitoringJob(applicationInstance.getUriId());
        if (retrievedJob == null) {
            retrievedJob = MonitoringModelFactory.toMonitoringJob(applicationInstance);
            // updateAdapterCredentials(applicationInstance, retrievedJob);
            addMonitoringJob(retrievedJob);
            logger.info("MonitoringJob created.");
        } else {
            logger.info("MonitoringJob already found.");
        }
        retrievedJob.setEnabled(true);
        retrievedJob.setCheckUrl(applicationInstance.getAdapterUrl());

        //application has not been stored to db yet
        List<eu.cloud4soa.relational.datamodel.ApplicationInstance> applicationlist = applicationInstanceRepository.findBy("uriID", applicationInstance.getUriId());
        if (applicationlist.size() > 0) {
            eu.cloud4soa.relational.datamodel.ApplicationInstance appInstance = applicationlist.get(0);
            retrievedJob.setApplicationInstance(appInstance);
        }

        monitoringJobRepository.flush();
    }

    public void stopMonitoring(String applicationUriId) {
        logger.info("Stopping monitoring for applicationInstance " + applicationUriId);

        MonitoringJob retrievedJob = (MonitoringJob) getMonitoringJob(applicationUriId);
        if (retrievedJob != null) {
            retrievedJob.setEnabled(false);
            monitoringJobRepository.flush();
        }
    }

    public void UpdateMonitoringApplicationInstance(String applicationUriId) {
        logger.info("Update  ApplicationInstance Id for applicationInstance " + applicationUriId);
        //application has not been stored to db yet
        
        MonitoringJob retrievedJob = (MonitoringJob) getMonitoringJob(applicationUriId);
        List<eu.cloud4soa.relational.datamodel.ApplicationInstance> applicationlist = applicationInstanceRepository.findByUriIdNoCheck(applicationUriId);
        if (applicationlist.size() > 0 && retrievedJob != null) {
            eu.cloud4soa.relational.datamodel.ApplicationInstance appInstance = applicationlist.get(0);
            retrievedJob.setApplicationInstance(appInstance);
            monitoringJobRepository.flush();
        }
  
    }

    public IMonitoringJob getMonitoringJob(String applicationUriId) {
        MonitoringJob retrievedJob = monitoringJobRepository.retrieveByApplicationUriId(applicationUriId);
        if (retrievedJob != null) {
            // updateAdapterCredentials(applicationInstance, retrievedJob);
        }
        return retrievedJob;
    }

    @SuppressWarnings("unchecked")
    public List<IMonitoringStatistic> getMonitoringStatistics(String applicationUriId) {
        MonitoringJob monitoringJob = (MonitoringJob) getMonitoringJob(applicationUriId);
        List<IMonitoringStatistic> statistics = new LinkedList<IMonitoringStatistic>();
        if (monitoringJob != null) {
            statistics = (List<IMonitoringStatistic>) (List<?>) monitoringStatisticRepository.retrieveAllMonitoringStatistics(monitoringJob);
        }
        return statistics;
    }

    @SuppressWarnings("unchecked")
    public List<IMonitoringStatistic> getMonitoringStatisticsWhithinRange(String applicationUriId, Date start, Date end) {
        MonitoringJob monitoringJob = (MonitoringJob) getMonitoringJob(applicationUriId);
        List<IMonitoringStatistic> statistics = new LinkedList<IMonitoringStatistic>();
        if (monitoringJob != null) {
            statistics = (List<IMonitoringStatistic>) (List<?>) monitoringStatisticRepository.retrieveAllInRange(monitoringJob, start, end);
        }
        return statistics;
    }

    @Deprecated
    @Override
    public List<IMonitoringStatistic> getMonitoringStatisticsWhithinRangeLimited(String applicationUriId, Date start, Date end, int maxResults) {
        MonitoringJob monitoringJob = (MonitoringJob) getMonitoringJob(applicationUriId);
        List<IMonitoringStatistic> statistics = new LinkedList<IMonitoringStatistic>();
        if (monitoringJob != null) {
            statistics = (List<IMonitoringStatistic>) (List<?>) monitoringStatisticRepository.retrieveAllInRangeLimited(monitoringJob, start, end, maxResults);
        }
        return statistics;
    }

    @Override
    public List<IMonitoringMetric> getMonitoringMetricsWhithinRangeLimited(String applicationUriId, MetricKey metricKey, Date start, Date end, int maxResults) {
        MonitoringJob monitoringJob = (MonitoringJob) getMonitoringJob(applicationUriId);
        List<IMonitoringMetric> metrics = new LinkedList<IMonitoringMetric>();
        if (monitoringJob != null) {
            metrics = (List<IMonitoringMetric>) (List<?>) monitoringMetricRepository.retrieveAllInRangeforMetric(monitoringJob, metricKey, start, end, maxResults);
        }
        return metrics;
    }

    private void addMonitoringJob(MonitoringJob monitoringJob) {
        monitoringJobRepository.store(monitoringJob);
    }

//	private Credentials inquireCredentials(ApplicationInstance applicationInstance) {
//		/**
//		 * TODO && FIXME
//		 */
//		return adapterCredentials;
//	}
    /*
     * private void updateAdapterCredentials(ApplicationInstance
     * applicationInstance, MonitoringJob monitoringJob) { if (monitoringJob !=
     * null && applicationInstance != null) { Credentials credentials =
     * inquireCredentials(applicationInstance); if (monitoringJob.getApiKey() ==
     * null || !monitoringJob.getApiKey().equals(credentials.getApiKey())) {
     * monitoringJob.setApiKey(credentials.getApiKey()); } if
     * (monitoringJob.getSecretKey() == null ||
     * !monitoringJob.getSecretKey().equals(credentials.getApiKey())) {
     * monitoringJob.setSecretKey(credentials.getSecretKey()); }
     * monitoringJobRepository.saveOrUpdate(monitoringJob); } }
     */
    @Override
    public void startMonitoring(String applicationUriId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
