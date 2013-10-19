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


package eu.cloud4soa.api.datamodel.semantic.measure;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import java.util.ArrayList;
import java.util.List;

@RDFBean("http://www.cloud4soa.eu/v0.1/measure#NumericRange")
public class NumericRange extends MeasurementRange {

    private Float maxNumericValue;
    private Float minNumericValue;
    private Float numericStep;
    private List<Float> offeredNumericValues = new ArrayList<Float>();
    

    @Override
    @RDFSubject(prefix = "http://www.cloud4soa.eu/v0.1/measure#")
    public String getUriId() {
        return super.getUriId();
    }
    
    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasNumericValue")
    public List<Float> getOfferedNumericValues() {
        return offeredNumericValues;
    }

    public void setOfferedNumericValues(List<Float> offeredNumericValues) {
        this.offeredNumericValues = offeredNumericValues;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasMaxNumericValue")
    public Float getMax() {
        return maxNumericValue;
    }

    public void setMax(Float maxNumericValue) {
        this.maxNumericValue = maxNumericValue;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasMinNumericValue")
    public Float getMin() {
        return minNumericValue;
    }

    public void setMin(Float minNumericValue) {
        this.minNumericValue = minNumericValue;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasNumericStep")
    public Float getStep() {
        return numericStep;
    }

    public void setStep(Float numericStep) {
        this.numericStep = numericStep;
    }
}