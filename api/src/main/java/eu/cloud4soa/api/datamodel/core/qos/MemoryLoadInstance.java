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
package eu.cloud4soa.api.datamodel.core.qos;

import eu.cloud4soa.api.datamodel.semantic.ea.MemoryLoad;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.measure.MegaByte;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageRange;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Dimitris
 */
@XmlRootElement()
@XmlType(name = "memoryLoadInstance")
public class MemoryLoadInstance extends ServiceQualityInstance {

    public MemoryLoadInstance(MemoryLoad memoryLoad) {
        super(memoryLoad);
    }
    
    public MemoryLoadInstance() {
        super(new MemoryLoad());
    }
    
    private MemoryLoad getMemoryLoad(){
        return (MemoryLoad)super.getServiceQuality();
    }
    
    private void setMemoryLoad(MemoryLoad memoryLoad){
        super.setServiceQuality(memoryLoad);
    }


    public void setHasStorageRangeValue(Float minValue, Float maxValue) {
        StorageRange storageRange = new StorageRange();
        MegaByte minValueMB = new MegaByte();
        MegaByte maxValueMB = new MegaByte();
        minValueMB.setValue(minValue);
        maxValueMB.setValue(maxValue);
        storageRange.setMin(minValueMB);
        storageRange.setMax(maxValueMB);
        if(getMemoryLoad()==null){
            MemoryLoad memoryLoad = new MemoryLoad();
            setMemoryLoad(memoryLoad);
        }
        getMemoryLoad().setHasStorageRangeValue(storageRange);
    }
    
    public Float getMinValueMB(){
        if(getMemoryLoad()!=null && getMemoryLoad().getHasStorageRangeValue() != null && getMemoryLoad().getHasStorageRangeValue().getMin() != null){
            return getMemoryLoad().getHasStorageRangeValue().getMin().getValue();
        }
        return null;
    }
    
    public Float getMaxValueMB(){
        if(getMemoryLoad()!=null && getMemoryLoad().getHasStorageRangeValue() != null && getMemoryLoad().getHasStorageRangeValue().getMax() != null){
            return getMemoryLoad().getHasStorageRangeValue().getMax().getValue();
        }
        return null;
    }
    
    public void setMinValueMB(Float minValue){
        if(getMemoryLoad()!=null && getMemoryLoad().getHasStorageRangeValue() != null){
            MegaByte minValueMB = new MegaByte();
            minValueMB.setValue(minValue);
            getMemoryLoad().getHasStorageRangeValue().setMin(minValueMB);
        }
        else{
            if(getMemoryLoad()==null){
                MemoryLoad memoryLoad = new MemoryLoad();
                setMemoryLoad(memoryLoad);
            }
            if(getMemoryLoad().getHasStorageRangeValue() == null){
                StorageRange storageRange = new StorageRange();
                MegaByte minValueMB = new MegaByte();
                minValueMB.setValue(minValue);
                storageRange.setMin(minValueMB);
                getMemoryLoad().setHasStorageRangeValue(storageRange);
            }
        }
    }
    
    public void setMaxValueMB(Float maxValue){
        if(getMemoryLoad()!=null && getMemoryLoad().getHasStorageRangeValue() != null){
            MegaByte maxValueMB = new MegaByte();
            maxValueMB.setValue(maxValue);
            getMemoryLoad().getHasStorageRangeValue().setMax(maxValueMB);
        }
        else{
            if(getMemoryLoad()==null){
                MemoryLoad memoryLoad = new MemoryLoad();
                setMemoryLoad(memoryLoad);
            }
            if(getMemoryLoad().getHasStorageRangeValue() == null){
                StorageRange storageRange = new StorageRange();
                MegaByte maxValueMB = new MegaByte();
                maxValueMB.setValue(maxValue);
                storageRange.setMax(maxValueMB);
                getMemoryLoad().setHasStorageRangeValue(storageRange);
            }
        }
    }
    
    @Override
    public Technology_Service_Quality getServiceQuality() {
        return getMemoryLoad();
    }
  
}
