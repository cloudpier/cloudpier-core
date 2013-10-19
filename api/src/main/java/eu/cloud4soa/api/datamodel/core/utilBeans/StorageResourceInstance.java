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
import eu.cloud4soa.api.datamodel.core.utilBeans.helper.StorageUnitType;
import eu.cloud4soa.api.datamodel.core.utilBeans.helper.StorageUnitTypeHelper;
import eu.cloud4soa.api.datamodel.semantic.inf.StorageResource;
import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingRange;
import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageRange;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vins
 */
public class StorageResourceInstance extends HardwareComponentInstance{
//    private StorageResource storageResource;

    public StorageResourceInstance() {
        this.hardwareComponent = new StorageResource();
    }

    public StorageResourceInstance(StorageResource storageResource) {
        this.hardwareComponent = storageResource;
    }

    private StorageResource getStorageResource() {
        return (StorageResource)this.hardwareComponent;
    }    
    
    public String getUriId(){
        return hardwareComponent.getUriId();
    }
    
    public void setUriId(String uriId){
       hardwareComponent.setUriId(uriId);
    }
    
    public String getTitle(){
        return getStorageResource().getTitle();
    }
    
    public void setTitle(String title){
       getStorageResource().setTitle(title);
    }
    
    public String getDescription(){
        return getStorageResource().getDescription();
    }
    
    public void setDescription(String description){
       getStorageResource().setDescription(description);
    }
    
  
     /*BANDWIDTH*/
    public NetworkingRange getBandwidthRange() {
        if (getStorageResource().getBandwidth() != null) {
            return getStorageResource().getBandwidth();
        }
        return null;
    }

    public void setBandwidthRange(NetworkingRange bandwidth) {
        getStorageResource().setBandwidth(bandwidth);
    }

    /*MAX BANDWIDTH*/
    public Float getMaxBandwidthValue() {
        if (getStorageResource().getBandwidth() != null) {
            if (getStorageResource().getBandwidth().getMax() != null) {
                return getStorageResource().getBandwidth().getMax().getValue();
            }
        }
        return null;
    }

    public void setMaxBandwidthValue(Float bandwidth) {
        if (getStorageResource().getBandwidth() == null) {
            getStorageResource().setBandwidth(new NetworkingRange());
        }
        if (getStorageResource().getBandwidth().getMax() == null) {
            getStorageResource().getBandwidth().setMax(new NetworkingUnit());
        }

        getStorageResource().getBandwidth().getMax().setValue(bandwidth);
    }

    public NetworkingUnitType getMaxBandwidthUnit() {
        if (getStorageResource().getBandwidth() != null &&getStorageResource().getBandwidth().getMax()!=null) {
            return NetworkingUnitTypeHelper.getNetworkingUnitType(getStorageResource().getBandwidth().getMax().getClass());
        }
        return null;
    }

    public void setMaxBandwidthUnit(NetworkingUnitType unitType) {
        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getStorageResource().getBandwidth().setMax(instance);
    }

    public void setMaxBandwidthUnitAndValue(NetworkingUnitType unitType, Float bandwidth) {
        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getStorageResource().getBandwidth().setMax(instance);
        getStorageResource().getBandwidth().getMax().setValue(bandwidth);
    }

    /*MIN BANDWIDTH*/
  
    public Float getMinBandwidthValue() {
        if (getStorageResource().getBandwidth() != null) {
            if (getStorageResource().getBandwidth().getMin() != null) {
                return getStorageResource().getBandwidth().getMin().getValue();
            }
        }
        return null;
    }

    public void setMinBandwidthValue(Float bandwidth) {
        if (getStorageResource().getBandwidth() == null) {
            getStorageResource().setBandwidth(new NetworkingRange());
        }
        if (getStorageResource().getBandwidth().getMin() == null) {
            getStorageResource().getBandwidth().setMin(new NetworkingUnit());
        }

        getStorageResource().getBandwidth().getMin().setValue(bandwidth);
    }

    public NetworkingUnitType getMinBandwidthUnit() {
        if (getStorageResource().getBandwidth() != null && getStorageResource().getBandwidth().getMin()!=null) {
            return NetworkingUnitTypeHelper.getNetworkingUnitType(getStorageResource().getBandwidth().getMin().getClass());
        }
        return null;
    }

    public void setMinBandwidthUnit(NetworkingUnitType unitType) {
        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getStorageResource().getBandwidth().setMin(instance);
    }

    public void setMinBandwidthUnitAndValue(NetworkingUnitType unitType, Float bandwidth) {
        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getStorageResource().getBandwidth().setMin(instance);
        getStorageResource().getBandwidth().getMin().setValue(bandwidth);
    }

    /*BANDWIDTH STEP*/
   
       public Float getStepBandwidthValue() {
        if (getStorageResource().getBandwidth() != null) {
            if (getStorageResource().getBandwidth().getStep() != null) {
                return getStorageResource().getBandwidth().getStep().getValue();
            }
        }
        return null;
    }

    public void setStepBandwidthValue(Float bandwidth) {
        if (getStorageResource().getBandwidth() == null) {
            getStorageResource().setBandwidth(new NetworkingRange());
        }
        if (getStorageResource().getBandwidth().getStep() == null) {
            getStorageResource().getBandwidth().setStep(new NetworkingUnit());
        }

        getStorageResource().getBandwidth().getStep().setValue(bandwidth);
    }

    public NetworkingUnitType getStepBandwidthUnit() {
        if (getStorageResource().getBandwidth() != null && getStorageResource().getBandwidth().getStep()!=null) {
            return NetworkingUnitTypeHelper.getNetworkingUnitType(getStorageResource().getBandwidth().getStep().getClass());
        }
        return null;
    }

    public void setStepBandwidthUnit(NetworkingUnitType unitType) {
        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getStorageResource().getBandwidth().setStep(instance);
    }

    public void setStepBandwidthUnitAndValue(NetworkingUnitType unitType, Float bandwidth) {
        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        getStorageResource().getBandwidth().setStep(instance);
        getStorageResource().getBandwidth().getStep().setValue(bandwidth);
    }
    
    /*BANDWIDTH VALUE*/
    
    public List<NetworkingUnit> getBandwidthValues() {
        if (getStorageResource().getBandwidth()!= null) {
            return getStorageResource().getBandwidth().getOfferedNetworkingValues();
        }
        return null;
    }

    public void setBandwidthValues(List<NetworkingUnit> bandwidthValues) {
        if (getStorageResource().getBandwidth() == null) {
            getStorageResource().setBandwidth(new NetworkingRange());
        }
        getStorageResource().getBandwidth().setOfferedNetworkingValues(bandwidthValues);
    }

    public void addBandwidthUnitAndValue(NetworkingUnitType unitType, Float bandwidthValue ) {
        if (getStorageResource().getBandwidth() == null) {
            getStorageResource().setBandwidth(new NetworkingRange());
        }
        NetworkingUnit instance = NetworkingUnitTypeHelper.getInstance(unitType);
        instance.setValue(bandwidthValue);
        getStorageResource().getBandwidth().getOfferedNetworkingValues().add(instance);
    }   
       
    /*CAPACITY*/
        
     public StorageRange getCapacityRange() {
        if (getStorageResource().getCapacity() != null) {
            return getStorageResource().getCapacity();
        }
        return null;
    }

    public void setCapacityRange(StorageRange capacity) {
        getStorageResource().setCapacity(capacity);
    }

    /*MAX CAPACITY*/
    public Float getMaxCapacityValue() {
        if (getStorageResource().getCapacity() != null) {
            if (getStorageResource().getCapacity().getMax() != null) {
                return getStorageResource().getCapacity().getMax().getValue();
            }
        }
        return null;
    }

    public void setMaxCapacityValue(Float capacity) {
        if (getStorageResource().getCapacity() == null) {
            getStorageResource().setCapacity(new StorageRange());
        }
        if (getStorageResource().getCapacity().getMax() == null) {
            getStorageResource().getCapacity().setMax(new StorageUnit());
        }

        getStorageResource().getCapacity().getMax().setValue(capacity);
    }

    public StorageUnitType getMaxCapacityUnit() {
        if (getStorageResource().getCapacity() != null && getStorageResource().getCapacity().getMax() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getStorageResource().getCapacity().getMax().getClass());
        }
        return null;
    }

    public void setMaxCapacityUnit(StorageUnitType unitType) {
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getStorageResource().getCapacity().setMax(instance);
    }

    public void setMaxCapacityUnitAndValue(StorageUnitType unitType, Float capacity) {
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getStorageResource().getCapacity().setMax(instance);
        getStorageResource().getCapacity().getMax().setValue(capacity);
    }

    /*MIN CAPACITY*/
    public Float getMinCapacityValue() {
        if (getStorageResource().getCapacity() != null) {
            if (getStorageResource().getCapacity().getMin() != null) {
                return getStorageResource().getCapacity().getMin().getValue();
            }
        }
        return null;
    }

    public void setMinCapacityValue(Float capacity) {
        if (getStorageResource().getCapacity() == null) {
            getStorageResource().setCapacity(new StorageRange());
        }
        if (getStorageResource().getCapacity().getMin() == null) {
            getStorageResource().getCapacity().setMin(new StorageUnit());
        }

        getStorageResource().getCapacity().getMin().setValue(capacity);
    }

    public StorageUnitType getMinCapacityUnit() {
        if (getStorageResource().getCapacity() != null && getStorageResource().getCapacity().getMin() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getStorageResource().getCapacity().getMin().getClass());
        }
        return null;
    }

    public void setMinCapacityUnit(StorageUnitType unitType) {
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getStorageResource().getCapacity().setMin(instance);
    }

    public void setMinCapacityUnitAndValue(StorageUnitType unitType, Float capacity) {
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getStorageResource().getCapacity().setMin(instance);
        getStorageResource().getCapacity().getMin().setValue(capacity);
    }

    /*CAPACITY STEP*/
    public Float getStepCapacityValue() {
        if (getStorageResource().getCapacity() != null) {
            if (getStorageResource().getCapacity().getStep() != null) {
                return getStorageResource().getCapacity().getStep().getValue();
            }
        }
        return null;
    }

    public void setStepCapacityValue(Float capacity) {
        if (getStorageResource().getCapacity() == null) {
            getStorageResource().setCapacity(new StorageRange());
        }
        if (getStorageResource().getCapacity().getStep() == null) {
            getStorageResource().getCapacity().setStep(new StorageUnit());
        }

        getStorageResource().getCapacity().getStep().setValue(capacity);
    }

    public StorageUnitType getStepCapacityUnit() {
        if (getStorageResource().getCapacity() != null && getStorageResource().getCapacity().getStep() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getStorageResource().getCapacity().getStep().getClass());
        }
        return null;
    }

    public void setStepCapacityUnit(StorageUnitType unitType) {
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getStorageResource().getCapacity().setStep(instance);
    }

    public void setStepCapacityUnitAndValue(StorageUnitType unitType, Float capacity) {
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getStorageResource().getCapacity().setStep(instance);
        getStorageResource().getCapacity().getStep().setValue(capacity);
    }
    
    /*CAPACITY VALUE*/
    
    public List<StorageUnit> getCapacityValues() {
        if (getStorageResource().getCapacity()!= null) {
            if(getStorageResource().getCapacity().getOfferedStorageValues() == null)
                getStorageResource().getCapacity().setOfferedStorageValues(new ArrayList<StorageUnit>());
            return getStorageResource().getCapacity().getOfferedStorageValues();
        }
        return null;
    }

    public void setCapacityValues(List<StorageUnit> capacityValues) {
        if (getStorageResource().getCapacity() == null) {
            getStorageResource().setCapacity(new StorageRange());
        }
        getStorageResource().getCapacity().setOfferedStorageValues(capacityValues);
       
    }

    public void addCapacityUnitAndValue(StorageUnitType unitType, Float capacityValue ) {
        if (getStorageResource().getCapacity() == null) {
            getStorageResource().setCapacity(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        instance.setValue(capacityValue);
        getStorageResource().getCapacity().getOfferedStorageValues().add(instance);
    }   

}
