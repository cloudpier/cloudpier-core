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
package eu.cloud4soa.governance.monitoring.worker;

import eu.cloud4soa.relational.persistence.IMonitoringStatisticRepository;
import eu.cloud4soa.relational.persistence.MonitoringJobRepository;
import eu.cloud4soa.relational.persistence.MonitoringStatisticRepository;


/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public interface IMonitoringWorker {

	/**
	 * (self-Explanatory) 
	 * Getter
	 * 
	 * @return monitoringStatisticRepository
	 */
	abstract MonitoringStatisticRepository getMonitoringStatisticRepository();

	/**
	 * (self-Explanatory) 
	 * Setter
	 * 
	 * @param monitoringStatisticRepository
	 */
	abstract void setMonitoringStatisticRepository(IMonitoringStatisticRepository monitoringStatisticRepository);

	/**
	 * (self-Explanatory) 
	 * Getter
	 * 
	 * @return monitoringJobRepository
	 */
	abstract MonitoringJobRepository getMonitoringJobRepository();

	/**
	 * (self-Explanatory) 
	 * Setter
	 * 
	 * @param monitoringJobRepository
	 */
	abstract void setMonitoringJobRepository(MonitoringJobRepository monitoringJobRepository);

	/**
	 * Annotated with <strong>&#64;Scheduled</strong>.<br>
	 * Spawns sub threads, which do the statistic poll.<br>
	 * 
	 * See {@linkplain http://static.springsource.org/spring/docs/3.0.5.RELEASE/reference/scheduling.html} 
	 * for more informations about that functionality.
	 */
	abstract void spawnMonitors();
}