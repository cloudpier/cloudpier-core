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

package eu.cloud4soa.api.datamodel.governance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author vincenzo
 */
public class SlaContract extends SlaTemplate {

	private static final List<ServiceGuaranteeType> kpi_types;
    static {
    	ArrayList<ServiceGuaranteeType> aList = new ArrayList<ServiceGuaranteeType>();
    	aList.add(ServiceGuaranteeType.CloudResponseTime);
    	aList.add(ServiceGuaranteeType.ContainerResponseTime);
    	aList.add(ServiceGuaranteeType.CPU_Load);
    	aList.add(ServiceGuaranteeType.Memory_Load);
    	aList.add(ServiceGuaranteeType.UNRECOGNIZED);
    	kpi_types = Collections.unmodifiableList(aList);
    }
    
	private ArrayList <SlaPolicy> slaPolicies;	
	
    public SlaContract () {}
    
    public void setContractId (Long id) {
    	this.setId(id);
    }
    
    public void addSlaPolicy (SlaPolicy slaPolicy) {
    	slaPolicies.add(slaPolicy);
    }
}
