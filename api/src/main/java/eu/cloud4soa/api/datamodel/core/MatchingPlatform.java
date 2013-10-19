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
package eu.cloud4soa.api.datamodel.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import eu.cloud4soa.api.datamodel.governance.SlaTemplate;

/**
 * @author Zeginis Dimitris (CERTH)
 * @author Vincenzo Laudisio (DERI)
 */
@XmlRootElement()
@XmlType(name = "matchingPlatform")//, namespace = "eu.cloud4soa.api.datamodel.core")
public class MatchingPlatform {
   
    private Map<PaaSInstance, SlaTemplate> listSlaTemplates;
    private Map<PaaSInstance, Float> rankedListPaaSInstances;

    public Map<PaaSInstance, Float> getRankedListPaaSInstances() {
        return rankedListPaaSInstances;
    }

    public void setRankedListPaaSInstances(Map<PaaSInstance, Float> rankedListPaaSInces) {
        this.rankedListPaaSInstances = rankedListPaaSInces;
    }

    
    public Map<PaaSInstance, SlaTemplate> getListSlaTemplates() {
        return listSlaTemplates;
    }
    
    
    public void setListSlaTemplates(Map<PaaSInstance, SlaTemplate> listSlaTemplates) {
        this.listSlaTemplates = listSlaTemplates;
    }
    
    
   
    /**
     * @return the listPaaSInstance
     */
    @XmlTransient
    public List<PaaSInstance> getListPaaSInstance() {
        if (rankedListPaaSInstances == null) {
            return null;
        }
        List<PaaSInstance> paasList = new ArrayList<PaaSInstance>(rankedListPaaSInstances.keySet());
        return paasList;
    }
    
    
    public void setListPaaSInstance(List<PaaSInstance> listPaaSInstance) {
        if (rankedListPaaSInstances == null) {
            rankedListPaaSInstances = new HashMap<PaaSInstance, Float>();
        }
        this.rankedListPaaSInstances.clear();
        for (PaaSInstance pInst : listPaaSInstance) {
            this.rankedListPaaSInstances.put(pInst, Float.NaN);
        }
    }

    /**
     * @return the listSlaContract
     */
//    @XmlTransient
    public Map<PaaSInstance, SlaTemplate> getListSlaContract() {
        return listSlaTemplates;
    }

    public void setListSlaContract(Map<PaaSInstance, SlaTemplate> listSlaTemplates) {
        this.listSlaTemplates = listSlaTemplates;
    }
}
