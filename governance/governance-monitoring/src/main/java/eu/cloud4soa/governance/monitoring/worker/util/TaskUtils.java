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
package eu.cloud4soa.governance.monitoring.worker.util;

import eu.cloud4soa.adapter.rest.AdapterClient;
import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.governance.monitoring.task.MonitoringTask;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import eu.cloud4soa.relational.persistence.MonitoringStatisticRepository;
import eu.cloud4soa.relational.persistence.MonitoringMetricRepository;

public class TaskUtils {

	public static MonitoringTask createMonitoringTask(MonitoringStatisticRepository monitoringStatisticRepository, MonitoringMetricRepository monitoringMetricRepository, ApplicationInstanceRepository applicationInstanceRepository, AdapterClient adapterClient, IMonitoringJob monitoringJob){
		MonitoringTask monitoringTask = new MonitoringTask(monitoringStatisticRepository, monitoringMetricRepository, applicationInstanceRepository, adapterClient, monitoringJob);
		
		return monitoringTask;
	}
}
