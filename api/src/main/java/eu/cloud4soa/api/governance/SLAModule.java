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
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.governance.Breach;
import eu.cloud4soa.api.datamodel.governance.SLAViolation;
import eu.cloud4soa.api.datamodel.governance.SlaContract;
import eu.cloud4soa.api.datamodel.governance.SlaContractValidity;
import eu.cloud4soa.api.datamodel.governance.SlaTemplate;

/**
 *
 * @author vincenzo
 */
public interface SLAModule {

    SlaTemplate startNegotiation(ApplicationInstance applicationInstance, PaaSInstance paaSInstance);

    SlaContractValidity checkContractValidity(ApplicationInstance applicationInstance, PaaSInstance paaSInstance);
    
   /*method exposed only for the DEMO*/
    SlaTemplate getSLATemplate(String templateId, ApplicationInstance applicationInstance, PaaSInstance paaSInstance);
    
    public SlaContract getSLAContract(String templateId);
    public List<SLAViolation> getSlaViolations (String user_id, Date start, Date end);
    public List<Breach> getBreaches (String user_id, Date start, Date end);
}
