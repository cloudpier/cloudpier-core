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
import eu.cloud4soa.api.datamodel.semantic.measure.ComputingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import java.util.ArrayList;
import java.util.List;

@RDFBean("http://www.cloud4soa.eu/v0.1/measure#ComputingRange")
public class ComputingRange extends MeasurementRange {

    private ComputingUnit maxComputingValue;
    private ComputingUnit minComputingValue;
    private ComputingUnit computingStep;
    private List<ComputingUnit> offeredComputingValues = new ArrayList<ComputingUnit>();

    @Override
    @RDFSubject(prefix = "http://www.cloud4soa.eu/v0.1/measure#")
    public String getUriId() {
        return super.getUriId();
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasComputingValue")
    public List<ComputingUnit> getOfferedComputingValues() {
        return offeredComputingValues;
    }

    public void setOfferedComputingValues(List<ComputingUnit> offeredComputingValues) {
        this.offeredComputingValues = offeredComputingValues;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasMaxComputingValue")
    public ComputingUnit getMax() {
        return maxComputingValue;
    }

    public void setMax(ComputingUnit maxComputingValue) {
        this.maxComputingValue = maxComputingValue;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasMinComputingValue")
    public ComputingUnit getMin() {
        return minComputingValue;
    }

    public void setMin(ComputingUnit minComputingValue) {
        this.minComputingValue = minComputingValue;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasComputingStep")
    public ComputingUnit getStep() {
        return computingStep;
    }

    public void setStep(ComputingUnit computingStep) {
        this.computingStep = computingStep;
    }
}