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
package eu.cloud4soa.api.governance;

import java.util.Date;
import java.util.List;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.governance.sla.enforcement.ISLAViolation;


public interface SLAEnforcementModule {
	
	public void startEnforcement(ApplicationInstance applicationInstance);

	public void stopEnforcement(ApplicationInstance applicationInstance);
	
	public List<ISLAViolation> getSLAViolations(ApplicationInstance applicationInstance); 

	public List<ISLAViolation> getSLAViolations(ApplicationInstance applicationInstance, String status); //status is optional (if null return all SLAViolations for user)
	
	public List<ISLAViolation> getSLAViolationsWithinDateRange(ApplicationInstance applicationInstance, String status, Date start, Date end); //status is optional (if null return all SLAViolations for user)
	
	public List<ISLAViolation> getSLAViolationsWithOffset(ApplicationInstance applicationInstance, String status, Date start, Date end, int offset, int size); //status is optional (if null return all SLAViolations for user)

}
