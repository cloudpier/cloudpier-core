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

@RDFBean("http://www.cloud4soa.eu/v0.1/measure#NetworkingRange")
public class NetworkingRange extends MeasurementRange {

    private NetworkingUnit maxNetworkingValue;
    private NetworkingUnit minNetworkingValue;
    private NetworkingUnit networkingStep;
    private List<NetworkingUnit> offeredNetworkingValues = new ArrayList<NetworkingUnit>();

    @Override
    @RDFSubject(prefix = "http://www.cloud4soa.eu/v0.1/measure#")
    public String getUriId() {
        return super.getUriId();
    }
    
    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasNetworkingValue")
    public List<NetworkingUnit> getOfferedNetworkingValues() {
        return offeredNetworkingValues;
    }

    public void setOfferedNetworkingValues(List<NetworkingUnit> offeredNetworkingValues) {
        this.offeredNetworkingValues = offeredNetworkingValues;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasMaxNetworkingValue")
    public NetworkingUnit getMax() {
        return maxNetworkingValue;
    }

    public void setMax(NetworkingUnit maxNetworkingValue) {
        this.maxNetworkingValue = maxNetworkingValue;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasMinNetworkingValue")
    public NetworkingUnit getMin() {
        return minNetworkingValue;
    }

    public void setMin(NetworkingUnit minNetworkingValue) {
        this.minNetworkingValue = minNetworkingValue;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/measure#hasNetworkingStep")
    public NetworkingUnit getStep() {
        return networkingStep;
    }

    public void setStep(NetworkingUnit networkingStep) {
        this.networkingStep = networkingStep;
    }
}