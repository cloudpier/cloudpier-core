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

package eu.cloud4soa.api.governance;

import java.util.Date;
import java.util.List;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric.MetricKey;
import eu.cloud4soa.api.governance.monitoring.IMonitoringStatistic;

/**
 * @author vincenzo
 * @author denisneuling (dn@cloudcontrol.de)
 * 
 * @see Cloud4Soa Service Lifecycle Governance framework table 16 Monitoring
 *      functionality table
 * 
 *      <strong>Note:</strong> I had to change this class because the method
 *      interfaces were not compatible with the project structure...<br>
 *      I got some build errors while referencing objects from the api package,
 *      which were implemented at the monitoring package, so that each build
 *      failed and I had have the API let depend onto the monitoring module (POM).
 */
public interface MonitoringModule {

	/**
	 * Starts monitoring of the given ApplicationInstance.
	 * 
	 * @param applicationUriId
	 *            the target application instance
	 */
	public void startMonitoring(String applicationUriId);
	/*
	 * (non-JavaDoc) 
	 * Note: eu.cloud4soa.api.datamodel.governance.ApplicationInstance was replaced
	 */
        
	/**
	 * Starts monitoring of the given ApplicationInstance.
	 * 
	 * @param applicationUriId
	 *            the target application instance
	 */
	public void startMonitoringJob(ApplicationInstance applicationInstance);
	/*
	 * (non-JavaDoc) 
	 * Note: eu.cloud4soa.api.datamodel.governance.ApplicationInstance was replaced
	 */
        
	/**
	 * Stops monitoring of the associated application instance.
	 * 
	 * @param applicationUriId
	 *            the target application instance.
	 */
	public void stopMonitoring(String applicationUriId);
        
	/**
	 * Stops monitoring of the associated application instance.
	 * 
	 * @param applicationUriId
	 *            the target application instance.
	 */
	public void UpdateMonitoringApplicationInstance(String applicationUriId);

	/**
	 * Get the MonitoringJob of an application instance.
	 * 
	 * @param applicationUriId
	 *            the application to monitor
	 * @return monitoringJob the job of the concerning application instance or
	 *         null if monitori9ng is not enabled
	 */
	public IMonitoringJob getMonitoringJob(String applicationUriId);

	/**
	 * Retrieve a list of all executed adapter polls.
	 * 
	 * @param applicationUriId
	 *            the application to monitor
	 * @return monitoringStatistic the list of statistics with the given
	 *         character or empty list if the monitoring of the given
	 *         application instance is not enabled
	 */
	public List<IMonitoringStatistic> getMonitoringStatistics(String applicationUriId);

	/**
	 * Retrieve a list of requested statistics.
	 * 
	 * @param applicationUriId
	 *            the application to monitor
	 * @param start
	 *            specifies the start date of the items of the result set
	 * @param end
	 *            specifies the end date of the objects of the result set
	 * @return monitoringStatistic the list of statistics with the given
	 *         character or empty list if the monitoring of the given
	 *         application instance is not enabled
	 */
	public List<IMonitoringStatistic> getMonitoringStatisticsWhithinRange(String applicationUriId, Date start, Date end);

	/**
	 * Retrieve a list of requested statistics.
	 * 
	 * @param applicationUriId
	 *            the application to monitor
	 * @param start
	 *            specifies the start date of the items of the result set
	 * @param end
	 *            specifies the end date of the objects of the result set
	 * @param maxResults
	 *            specifies the maximum size of the result set
	 * @return monitoringStatistic the list of statistics with the given
	 *         character or empty list if the monitoring of the given
	 *         application instance is not enabled
	 */
	public List<IMonitoringStatistic> getMonitoringStatisticsWhithinRangeLimited(String applicationUriId, Date start, Date end, int maxResults);

	
	/**
	 * Retrieve a list of requested statistics.
	 * 
	 * @param applicationUriId
	 *            the application to monitor
	 * @param metricKey
	 *            the key of the metric type requested
	 * @param start
	 *            specifies the start date of the items of the result set
	 * @param end
	 *            specifies the end date of the objects of the result set
	 * @param maxResults
	 *            specifies the maximum size of the result set
	 * @return monitoringStatistic the list of statistics with the given
	 *         character or empty list if the monitoring of the given
	 *         application instance is not enabled
	 */
	public List<IMonitoringMetric> getMonitoringMetricsWhithinRangeLimited(String applicationUriId, MetricKey metricKey, Date start, Date end, int maxResults);
	
	// /**
	// * Adds a singele monitoring job to the list.
	// *
	// * @param monitoringJob
	// */
	// public void addMonitoringJob(IMonitoringJob monitoringJob);
	//
	// /**
	// * get the monitoring statistics of the concerning job.
	// *
	// * @param monitoringJob the job to handle with
	// * @return monitoringStatistic a list of all statistics mit the given
	// character
	// */
	// public List<IMonitoringStatistic> getMonitoringStatistics(IMonitoringJob
	// monitoringJob);
	//
	// /**
	// * Removes the monitoring job from the list.
	// *
	// * @param monitoringJob the job to handle with
	// */
	// public void removeMonitoringJob(IMonitoringJob monitoringJob);
	//
	// /**
	// *
	// * @return monitoringStatistic a list of all statistics mit the given
	// character
	// */
	// public List<IMonitoringJob> listMonitoringJobs();
}
