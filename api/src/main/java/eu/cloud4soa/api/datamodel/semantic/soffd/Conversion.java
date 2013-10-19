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


package eu.cloud4soa.api.datamodel.semantic.soffd;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.Thing;
import eu.cloud4soa.api.datamodel.semantic.measure.MeasurementUnit;

/**
 *
 * @author vinlau
 */
@RDFBean("http://usoa.deri.ie/ontology/conversion_v.0.1#Conversion")
public class Conversion extends Thing {

    private Thing hasSourceUnit;
    private Thing hasTargetUnit;
    private Float hasConversionRate ;
            
    @Override
    @RDFSubject(prefix="http://usoa.deri.ie/ontology/conversion_v.0.1#")
    public String getUriId() {
            return super.getUriId();
    }
    
    @RDF("http://usoa.deri.ie/ontology/conversion_v.0.1#hasConversionRate")
    public Float getHasConversionRate() {
        return hasConversionRate;
    }

    public void setHasConversionRate(Float hasConversionRate) {
        this.hasConversionRate = hasConversionRate;
    }
    
    @RDF("http://usoa.deri.ie/ontology/conversion_v.0.1#hasSourceUnit")
    public Thing getHasSourceUnit() {
        return hasSourceUnit;
    }

    public void setHasSourceUnit(MeasurementUnit hasSourceUnit) {
        this.hasSourceUnit = hasSourceUnit;
    }

    @RDF("http://usoa.deri.ie/ontology/conversion_v.0.1#hasTargetUnit")
    public Thing getHasTargetUnit() {
        return hasTargetUnit;
    }

    public void setHasTargetUnit(MeasurementUnit hasTargetUnit) {
        this.hasTargetUnit = hasTargetUnit;
    }

    
    
}