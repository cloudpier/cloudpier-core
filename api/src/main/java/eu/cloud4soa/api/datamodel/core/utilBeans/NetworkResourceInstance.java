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
package eu.cloud4soa.api.datamodel.core.utilBeans;

import eu.cloud4soa.api.datamodel.core.utilBeans.helper.NetworkingUnitType;
import eu.cloud4soa.api.datamodel.core.utilBeans.helper.NetworkingUnitTypeHelper;
import eu.cloud4soa.api.datamodel.core.utilBeans.helper.TimeUnitType;
import eu.cloud4soa.api.datamodel.core.utilBeans.helper.TimeUnitTypeHelper;
import eu.cloud4soa.api.datamodel.semantic.inf.NetworkResource;
import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingRange;
import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.TimeRange;
import eu.cloud4soa.api.datamodel.semantic.measure.TimeUnit;
import java.util.List;

/**
 *
 * @author vins
 */
public class NetworkResourceInstance extends HardwareComponentInstance{
//    private NetworkResource networkResource;

    public NetworkResourceInstance() {
        this.hardwareComponent=new NetworkResource();
    }

    public NetworkResourceInstance(NetworkResource networkResource) {
        this.hardwareComponent = networkResource;
    }

    public String getUriId() {
        return hardwareComponent.getUriId();
    }
    
    public void setUriId(String uriId){
       hardwareComponent.setUriId(uriId);
    }
    
    private NetworkResource getNetworkResource(){
        return (NetworkResource)hardwareComponent;
    }

    public String getTitle() {
        return getNetworkResource().getTitle();
    }

    public void setTitle(String title) {
        getNetworkResource().setTitle(title);
    }

    public String getDescription() {
        return getNetworkResource().getDescription();
    }

    public void setDescription(String description) {
        getNetworkResource().setDescription(description);
    }

    /*BANDWIDTH*/
    public NetworkingRange getBandwidthRange() {
        if (getNetworkResource().getBandwidth() != null) {
            return getNetworkResource().getBandwidth();
        }
        return null;
    }

    public void setBandwidthRange(NetworkingRange bandwidth) {
        getNetworkResource().setBandwidth(bandwidth);
    }

    /*MAX BANDWIDTH*/
    public Float getMaxBandwidthValue() {
        if (getNetworkResource().getBandwidth() != null) {
            if (getNetworkResource().getBandwidth().getMax() != null) {
                return getNetworkResource().getBandwidth().getMax().getValue();
            }
        }
        return null;
    }

    public void setMaxBandwidthValue(Float bandwidth) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }
        if (getNetworkResource().getBandwidth().getMax() == null) {
            getNetworkResource().getBandwidth().setMax(new NetworkingUnit());
        }

        getNetworkResource().getBandwidth().getMax().setValue(bandwidth);
    }

    public NetworkingUnitType getMaxBandwidthUnit() {
        if (getNetworkResource().getBandwidth() != null && getNetworkResource().getBandwidth().getMax() != null) {
            return NetworkingUnitTypeHelper.getNetworkingUnitType(getNetworkResource().getBandwidth().getMax().getClass());
        }
        return null;
    }

    public void setMaxBandwidthUnit(NetworkingUnitType unitType) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }

        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getBandwidth().setMax(instance);
    }

    public void setMaxBandwidthUnitAndValue(NetworkingUnitType unitType, Float bandwidth) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }

        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getBandwidth().setMax(instance);
        getNetworkResource().getBandwidth().getMax().setValue(bandwidth);
    }

    /*MIN BANDWIDTH*/
    public Float getMinBandwidthValue() {
        if (getNetworkResource().getBandwidth() != null) {
            if (getNetworkResource().getBandwidth().getMin() != null) {
                return getNetworkResource().getBandwidth().getMin().getValue();
            }
        }
        return null;
    }

    public void setMinBandwidthValue(Float bandwidth) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }
        if (getNetworkResource().getBandwidth().getMin() == null) {
            getNetworkResource().getBandwidth().setMin(new NetworkingUnit());
        }

        getNetworkResource().getBandwidth().getMin().setValue(bandwidth);
    }

    public NetworkingUnitType getMinBandwidthUnit() {
        if (getNetworkResource().getBandwidth() != null && getNetworkResource().getBandwidth().getMin() != null) {
            return NetworkingUnitTypeHelper.getNetworkingUnitType(getNetworkResource().getBandwidth().getMin().getClass());
        }
        return null;
    }

    public void setMinBandwidthUnit(NetworkingUnitType unitType) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }

        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getBandwidth().setMin(instance);
    }

    public void setMinBandwidthUnitAndValue(NetworkingUnitType unitType, Float bandwidth) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }

        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getBandwidth().setMin(instance);
        getNetworkResource().getBandwidth().getMin().setValue(bandwidth);
    }

    /*BANDWIDTH STEP*/
    public Float getStepBandwidthValue() {
        if (getNetworkResource().getBandwidth() != null) {
            if (getNetworkResource().getBandwidth().getStep() != null) {
                return getNetworkResource().getBandwidth().getStep().getValue();
            }
        }
        return null;
    }

    public void setStepBandwidthValue(Float bandwidth) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }
        if (getNetworkResource().getBandwidth().getStep() == null) {
            getNetworkResource().getBandwidth().setStep(new NetworkingUnit());
        }

        getNetworkResource().getBandwidth().getStep().setValue(bandwidth);
    }

    public NetworkingUnitType getStepBandwidthUnit() {
        if (getNetworkResource().getBandwidth() != null && getNetworkResource().getBandwidth().getStep() != null) {
            return NetworkingUnitTypeHelper.getNetworkingUnitType(getNetworkResource().getBandwidth().getStep().getClass());
        }
        return null;
    }

    public void setStepBandwidthUnit(NetworkingUnitType unitType) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }

        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getBandwidth().setStep(instance);
    }

    public void setStepBandwidthUnitAndValue(NetworkingUnitType unitType, Float bandwidth) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }

        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getBandwidth().setStep(instance);
        getNetworkResource().getBandwidth().getStep().setValue(bandwidth);
    }

    /*BANDWIDTH VALUE*/
    public List<NetworkingUnit> getBandwidthValues() {
        if (getNetworkResource().getBandwidth() != null) {
            return getNetworkResource().getBandwidth().getOfferedNetworkingValues();
        }
        return null;
    }

    public void setBandwidthValues(List<NetworkingUnit> bandwidthValues) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }
        getNetworkResource().getBandwidth().setOfferedNetworkingValues(bandwidthValues);
    }

    public void addBandwidthUnitAndValue(NetworkingUnitType unitType, Float bandwidthValue) {
        if (getNetworkResource().getBandwidth() == null) {
            getNetworkResource().setBandwidth(new NetworkingRange());
        }
        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        instance.setValue(bandwidthValue);
        getNetworkResource().getBandwidth().getOfferedNetworkingValues().add(instance);
    }

    /*LATENCY*/
    public TimeRange getLatencyRange() {
        if (getNetworkResource().getBandwidth() != null) {
            return getNetworkResource().getLatency();
        }
        return null;
    }

    public void setLatencyRange(TimeRange latency) {
        getNetworkResource().setLatency(latency);
    }

    /*MAX LATENCY*/
    public Float getMaxLatencyValue() {
        if (getNetworkResource().getLatency() != null) {
            if (getNetworkResource().getLatency().getMax() != null) {
                return getNetworkResource().getLatency().getMax().getValue();
            }
        }
        return null;
    }

    public void setMaxLatencyValue(Float latency) {
        if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        if (getNetworkResource().getLatency().getMax() == null) {
            getNetworkResource().getLatency().setMax(new TimeUnit());
        }

        getNetworkResource().getLatency().getMax().setValue(latency);
    }

    public TimeUnitType getMaxLatencyUnit() {
        if (getNetworkResource().getLatency() != null && getNetworkResource().getLatency().getMax() != null) {
            return TimeUnitTypeHelper.getTimeUnitType(getNetworkResource().getLatency().getMax().getClass());
        }
        return null;
    }

    public void setMaxLatencyUnit(TimeUnitType unitType) {
        if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }

        TimeUnit instance = TimeUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getLatency().setMax(instance);
    }

    public void setMaxLatencyUnitAndValue(TimeUnitType unitType, Float latency) {
        if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        
        TimeUnit instance = TimeUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getLatency().setMax(instance);
        getNetworkResource().getLatency().getMax().setValue(latency);
    }

    /*MIN LATENCY*/
    public Float getMinLatencyValue() {
        if (getNetworkResource().getLatency() != null) {
            if (getNetworkResource().getLatency().getMin() != null) {
                return getNetworkResource().getLatency().getMin().getValue();
            }
        }
        return null;
    }

    public void setMinLatencyValue(Float latency) {
        if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        if (getNetworkResource().getLatency().getMin() == null) {
            getNetworkResource().getLatency().setMin(new TimeUnit());
        }

        getNetworkResource().getLatency().getMin().setValue(latency);
    }

    public TimeUnitType getMinLatencyUnit() {
        if (getNetworkResource().getLatency() != null && getNetworkResource().getLatency().getMin() != null) {
            return TimeUnitTypeHelper.getTimeUnitType(getNetworkResource().getLatency().getMin().getClass());
        }
        return null;
    }

    public void setMinLatencyUnit(TimeUnitType unitType) {
         if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        
        TimeUnit instance = TimeUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getLatency().setMin(instance);
    }

    public void setMinLatencyUnitAndValue(TimeUnitType unitType, Float latency) {
         if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        
        TimeUnit instance = TimeUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getLatency().setMin(instance);
        getNetworkResource().getLatency().getMin().setValue(latency);
    }

    /*LATENCY STEP*/
    public Float getStepLatencyValue() {
        if (getNetworkResource().getLatency() != null) {
            if (getNetworkResource().getLatency().getStep() != null) {
                return getNetworkResource().getLatency().getStep().getValue();
            }
        }
        return null;
    }

    public void setStepLatencyValue(Float latency) {
        if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        if (getNetworkResource().getLatency().getStep() == null) {
            getNetworkResource().getLatency().setStep(new TimeUnit());
        }

        getNetworkResource().getLatency().getStep().setValue(latency);
    }

    public TimeUnitType getStepLatencyUnit() {
        if (getNetworkResource().getLatency() != null && getNetworkResource().getLatency().getStep() != null) {
            return TimeUnitTypeHelper.getTimeUnitType(getNetworkResource().getLatency().getStep().getClass());
        }
        return null;
    }

    public void setStepLatencyUnit(TimeUnitType unitType) {
         if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        
        TimeUnit instance = TimeUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getLatency().setStep(instance);
    }

    public void setStepLatencyUnitAndValue(TimeUnitType unitType, Float latency) {
         if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        
        TimeUnit instance = TimeUnitTypeHelper.getInstance(unitType);
        getNetworkResource().getLatency().setStep(instance);
        getNetworkResource().getLatency().getStep().setValue(latency);
    }

    /*LATENCY VALUE*/
    public List<TimeUnit> getLatencyValues() {
        if (getNetworkResource().getLatency() != null) {
            return getNetworkResource().getLatency().getOfferedTimeValues();
        }
        return null;
    }

    public void setLatencyValues(List<TimeUnit> latencyValues) {
        if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        getNetworkResource().getLatency().setOfferedTimeValues(latencyValues);
    }

    public void addLatencyUnitAndValue(TimeUnitType unitType, Float latencyValue) {
        if (getNetworkResource().getLatency() == null) {
            getNetworkResource().setLatency(new TimeRange());
        }
        TimeUnit instance = TimeUnitTypeHelper.getInstance(unitType);
        instance.setValue(latencyValue);
        getNetworkResource().getLatency().getOfferedTimeValues().add(instance);
    }   

}
