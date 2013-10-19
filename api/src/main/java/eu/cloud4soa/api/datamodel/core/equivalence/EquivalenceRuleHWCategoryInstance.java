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
package eu.cloud4soa.api.datamodel.core.equivalence;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vinlau
 */
@XmlRootElement() 
@XmlType(name = "equivalenceRuleHWCategoryInstance")
public class EquivalenceRuleHWCategoryInstance {
    
    private String EquivalenceRuleHWCategoryUriId;
  
    //Source HardwareCategory iD
    private String hasSource;
    private String hasTarget;
    private Float hasConversionRate ;

    public String getEquivalenceRuleHWCategoryUriId() {
        return EquivalenceRuleHWCategoryUriId;
    }

    public void setEquivalenceRuleHWCategoryUriId(String EquivalenceRuleHWCategoryUriId) {
        this.EquivalenceRuleHWCategoryUriId = EquivalenceRuleHWCategoryUriId;
    }
    
    public Float getHasConversionRate() {
        return hasConversionRate;
    }

    public void setHasConversionRate(Float hasConversionRate) {
        this.hasConversionRate = hasConversionRate;
    }

    public String getHasSource() {
        return hasSource;
    }

    public void setHasSource(String hasSource) {
        this.hasSource = hasSource;
    }

    public String getHasTarget() {
        return hasTarget;
    }

    public void setHasTarget(String hasTargetUnit) {
        this.hasTarget = hasTargetUnit;
    }

}
