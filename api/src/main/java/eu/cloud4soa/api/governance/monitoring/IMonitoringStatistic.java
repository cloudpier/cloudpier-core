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
package eu.cloud4soa.api.governance.monitoring;

import java.util.Date;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public interface IMonitoringStatistic {
	
	/**
	 * (self-Explanatory) 
	 * The execution date of the poll. 
	 * 
	 * @return date
	 */
	Date getDate();
	
	/**
	 * (self-Explanatory) 
	 * The id of the concerning monitopringJob.
	 * 
	 * @return monitoringJobId the id of the concering monitoringJob.
	 */
	long getMonitoringJobId();
	
	/**
	 * (self-Explanatory) 
	 * The HTTP responseCode of the poll.
	 *  
	 * @return responseCode the HTTP response code
	 */
	int getResponseCode();
	
	/**
	 * (self-Explanatory) 
	 * The time in milliseconds the adapter needed to respond.
	 * 
	 * @return timemillis the milliseconds.
	 */
	long getResponseTime();
	
	/**
	 * A message.
	 * 
	 * @return message a message adapter, (e.g. database is down or something else) 
	 */
	String getMessage();
}
