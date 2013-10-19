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
package eu.cloud4soa.governance.monitoring.task;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.cloud4soa.adapter.rest.AdapterClient;
import eu.cloud4soa.adapter.rest.auth.Credentials;
import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.adapter.rest.common.HttpStatus;
import eu.cloud4soa.adapter.rest.conversion.RequestFactory;
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.request.ExtendedMonitorRequest;
import eu.cloud4soa.adapter.rest.request.MonitorDetailRequest;
import eu.cloud4soa.adapter.rest.response.ExtendedMonitorResponse;
import eu.cloud4soa.adapter.rest.response.MonitorDetailResponse;
import eu.cloud4soa.adapter.rest.response.model.Metric;
import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric.MetricKey;
import eu.cloud4soa.relational.datamodel.MonitoringJob;
import eu.cloud4soa.relational.datamodel.MonitoringMetric;
import eu.cloud4soa.relational.datamodel.MonitoringStatistic;
import eu.cloud4soa.relational.persistence.MonitoringMetricRepository;
import eu.cloud4soa.relational.persistence.MonitoringStatisticRepository;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author denisneuling <dn@cloudcontrol.de>
 *
 */
public class MonitoringTask extends TimerTask implements IMonitoringTask, Runnable {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private AdapterClient adapterClient;
    private MonitoringStatisticRepository monitoringStatisticRepository;
    private MonitoringMetricRepository monitoringMetricRepository;
    private ApplicationInstanceRepository applicationInstanceRepository;
    private IMonitoringJob monitoringJob;
    private Date called;

    public MonitoringTask(MonitoringStatisticRepository monitoringStatisticRepository, MonitoringMetricRepository monitoringMetricRepository, ApplicationInstanceRepository applicationInstanceRepository, AdapterClient adapterClient, IMonitoringJob monitoringJob) {
        this.monitoringJob = monitoringJob;
        this.adapterClient = adapterClient;
        this.monitoringStatisticRepository = monitoringStatisticRepository;
        this.monitoringMetricRepository = monitoringMetricRepository;
        this.applicationInstanceRepository = applicationInstanceRepository;
    }

    @Override
    public void setMonitoringJob(IMonitoringJob monitoringJob) {
        this.monitoringJob = monitoringJob;
    }

    public void setAdapterClient(AdapterClient adapterClient) {
        this.adapterClient = adapterClient;
    }

    @Override
    public void run() {
        logger.debug("mondebug MonitoringTask - run");
        called = new Date();

        MonitorDetailRequest request = RequestFactory.createMonitorDetailRequest(monitoringJob);
        String appUrl = "";

        try {
            List<eu.cloud4soa.relational.datamodel.ApplicationInstance> applicationlist = applicationInstanceRepository.findBy("uriID", monitoringJob.getApplicationInstanceUriId());
            if (applicationlist.size() > 0) {
                eu.cloud4soa.relational.datamodel.ApplicationInstance appInstance = applicationlist.get(0);
                appUrl = appInstance.getAppurl();
            }
        } catch (Exception e) {
            logger.debug("Failed to grab appUrl");
        }

        URI uri = null;
        try {
            uri = new URI(appUrl);
            logger.debug("-------------appUrl: " + uri.getAuthority());
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(MonitoringTask.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.debug("pgouvas: MonitoringTask before creation");
        MonitoringStatistic monitoringStatistic = new MonitoringStatistic();
        Credentials credentials = new CustomerCredentials("apikey", "secretkey");
        try {
            MonitorDetailResponse response = adapterClient.send(request, credentials);
            monitoringStatistic = toMonitoringStatistic(response);
        } catch (IOException e) {
            monitoringStatistic.setMonitoringJobId(monitoringJob.getId());
            monitoringStatistic.setResponseTime(-1);
            monitoringStatistic.setResponseCode(HttpStatus.Conflict.ordinal());
            monitoringStatistic.setMessage("error");
        }
        monitoringStatistic.setDate(called);
        monitoringStatisticRepository.store(monitoringStatistic);

        try {
            ExtendedMonitorRequest extendedMonitorRequest = RequestFactory.createExtendedMonitorRequest(monitoringJob, uri.getAuthority());
            ExtendedMonitorResponse extendedMonitorResponse = adapterClient.send(extendedMonitorRequest, credentials);
            toMonitoringMetric(extendedMonitorResponse);
            monitoringMetricRepository.flush();
            logger.debug("------------Collected Monitoring Metrics. Flushing...");
        } catch (AdapterClientException ex) {
            java.util.logging.Logger.getLogger(MonitoringTask.class.getName()).log(Level.WARNING, "Failed to collect extended metrics. Possible lack of support from the selected provider.");
        } catch (UnknownHostException ex) {
            java.util.logging.Logger.getLogger(MonitoringTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        monitoringJob.setLastExecuted(called);
    }

    private MonitoringStatistic toMonitoringStatistic(MonitorDetailResponse response) {
        MonitoringStatistic monitoringStatistic = new MonitoringStatistic();
        monitoringStatistic.setMonitoringJobId(monitoringJob.getId());
        final long responseTime = response.getResponseTime();
        monitoringStatistic.setResponseTime(responseTime);
        monitoringStatistic.setMessage(response.getMessage());
        final int statusCode = response.getStatusCode().ordinal();
        monitoringStatistic.setResponseCode(statusCode);

        MonitoringMetric monitoringMetricResponseTime = new MonitoringMetric();
        monitoringMetricResponseTime.setMetricKey(MetricKey.responseTime);
        monitoringMetricResponseTime.setMetricValue(responseTime);
        monitoringMetricResponseTime.setDate(called);
        monitoringMetricResponseTime.setMonitoringJob((MonitoringJob) monitoringJob);
        monitoringMetricRepository.store(monitoringMetricResponseTime);

        MonitoringMetric monitoringMetricStatusCode = new MonitoringMetric();
        monitoringMetricStatusCode.setMetricKey(MetricKey.statusCode);
        monitoringMetricStatusCode.setMetricValue(statusCode);
        monitoringMetricStatusCode.setDate(called);
        monitoringMetricStatusCode.setMonitoringJob((MonitoringJob) monitoringJob);
        monitoringMetricRepository.store(monitoringMetricStatusCode);

        return monitoringStatistic;
    }

    private void toMonitoringMetric(ExtendedMonitorResponse extendedMonitorResponse) {

        Metric[] metrics = extendedMonitorResponse.getMetrics();
        logger.debug("------------Collected Monitoring Metrics. Storing...");
        logger.debug("------------Metrics list size: " + metrics.length);

        for (Metric metric : metrics) {
            try {
                logger.debug("------------Storing Monitoring Metric " + metric.getMetricName() + ":" + metric.getValue());
                MonitoringMetric monitoringMetric = new MonitoringMetric();
                monitoringMetric.setMetricKey(MetricKey.valueOf(metric.getMetricName()));
                monitoringMetric.setMetricValue(Double.parseDouble(metric.getValue()));
                monitoringMetric.setDate(called);
                monitoringMetric.setMonitoringJob((MonitoringJob) monitoringJob);
                monitoringMetricRepository.store(monitoringMetric);
                logger.debug("------------Monitoring Metric " + metric.getMetricName() + " stored");
            } catch (Exception e) {
                java.util.logging.Logger.getLogger(MonitoringTask.class.getName()).log(Level.WARNING, "Failed to collect metric", metric.getMetricName());
            }
        }
        logger.debug("------------Collected Monitoring Metrics. Stored");
    }
}