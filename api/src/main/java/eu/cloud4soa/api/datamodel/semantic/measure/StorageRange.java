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

@RDFBean("http://www.cloud4soa.eu/v0.1/measure#StorageRange")
public class StorageRange extends MeasurementRange {

    private StorageUnit maxStorageValue;
    private StorageUnit minStorageValue;
    private StorageUnit storageStep;
    private List<StorageUnit> offeredStorageValues = new ArrayList<StorageUnit>();

    @Override
    @RDFSubject(prefix = "http://www.cloud4soa.eu/v0.1/measure#")
    public String getUriId() {
        return super.getUriId();
    }
    
    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasStorageValue")
    public List<StorageUnit> getOfferedStorageValues() {
        return offeredStorageValues;
    }

    public void setOfferedStorageValues(List<StorageUnit> offeredStorageValues) {
        this.offeredStorageValues = offeredStorageValues;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasMaxStorageValue")
    public StorageUnit getMax() {
        return maxStorageValue;
    }

    public void setMax(StorageUnit maxStorageValue) {
        this.maxStorageValue = maxStorageValue;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasMinStorageValue")
    public StorageUnit getMin() {
        return minStorageValue;
    }

    public void setMin(StorageUnit minStorageValue) {
        this.minStorageValue = minStorageValue;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasStorageStep")
    public StorageUnit getStep() {
        return storageStep;
    }

    public void setStep(StorageUnit storageStep) {
        this.storageStep = storageStep;
    }
}