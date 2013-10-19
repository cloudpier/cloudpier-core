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

import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.relational.datamodel.MonitoringStatistic;
import java.util.Date;
import java.util.List;


/**
 * 
 * @author denisneuling (dn@cloudcontrol.de)
 */
public interface IMonitoringStatisticRepository {
	
	/**
	 * (self-explanatory) 
	 * Persists the business entity.
	 * 
	 * @param monitoringStatistic the statistic object to store
	 */
	public void store(MonitoringStatistic monitoringStatistic);

	/**
	 * (self-explanatory) 
	 * Removes the persisted business entity.
	 * 
	 * @param monitoringStatistic the statistic object to remove
	 */
	public void delete(MonitoringStatistic monitoringStatistic);
	
	/**
	 * <strong>Note:</strong> This method <strong>retrieves all</strong> business entities which were persisted.<br>
	 * It is very recommended to use {@link #retrieveAllMonitoringStatistics(IMonitoringJob)} to retrieve objects by a referenced job.
	 *  
	 * @return monitoringStatistic the list of all statistics with the given character.
	 */
	public List<MonitoringStatistic> retrieveAllMonitoringStatistics();
	
	/**
	 * Retrieves all persisted monitoring statistic object from the persistent layer with the given character.
	 * 
	 * @param monitoringJob the concerning {@link IMonitoringJob}
	 * @return monitoringStatistic the list of all statistics with the given character.
	 */
	public List<MonitoringStatistic> retrieveAllMonitoringStatistics(IMonitoringJob monitoringJob);
	
	/**
	 * Retrieves all persisted monitoring statistic object from the persistent layer with the given character.
	 * 
	 * @param monitoringJob the concerning {@link IMonitoringJob}
	 * @param start the start date of the time range 
	 * @param end the end date of the time range
	 * @return monitoringStatistic the list of all statistics with the given character.
	 */
	public List<MonitoringStatistic> retrieveAllInRange(IMonitoringJob monitoringJob, Date start, Date end);
	
	/**
	 * Retrieves all persisted monitoring statistic object fronm the persistent layer with the given character.
	 * 
	 * @param monitoringJob the concerning {@link IMonitoringJob}
	 * @param start the start date of the time range 
	 * @param end the end date of the time range
	 * @param limit the maximum size of the result set
	 * @return monitoringStatistic the list of all statistics with the given character.
	 */
	public List<MonitoringStatistic> retrieveAllInRangeLimited(IMonitoringJob monitoringJob, Date start, Date end, int limit);
}

