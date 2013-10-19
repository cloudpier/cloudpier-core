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

import eu.cloud4soa.api.datamodel.core.utilBeans.helper.StorageUnitType;
import eu.cloud4soa.api.datamodel.core.utilBeans.helper.StorageUnitTypeHelper;
import eu.cloud4soa.api.datamodel.semantic.inf.Compute;
import eu.cloud4soa.api.datamodel.semantic.measure.NumericRange;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageRange;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vins
 */
public class ComputeInstance extends HardwareComponentInstance {
//    private Compute compute;

    private Logger logger = LoggerFactory.getLogger(ComputeInstance.class.getName());

    public ComputeInstance() {
        this.hardwareComponent = new Compute();
    }

    public ComputeInstance(Compute compute) {
        this.hardwareComponent = compute;
    }

    private Compute getCompute() {
        return (Compute) this.hardwareComponent;
    }

    public String getUriId() {
        return hardwareComponent.getUriId();
    }

    public void setUriId(String uriId) {
        hardwareComponent.setUriId(uriId);
    }

    public String getTitle() {
        return getCompute().getTitle();
    }

    public void setTitle(String title) {
        getCompute().setTitle(title);
    }

    public String getDescription() {
        return getCompute().getDescription();
    }

    public void setDescription(String description) {
        getCompute().setDescription(description);
    }

    public String getArchitecture() {
        return getCompute().getArchitecture();
    }

    public void setArchitecture(String architecture) {
        getCompute().setArchitecture(architecture);
    }

    /*CORES*/
    public NumericRange getHasCores() {
        return getCompute().getHasCores();
    }

    public void setHasCores(NumericRange cores) {
        getCompute().setHasCores(cores);
    }

    public Float getMaxHasCores() {
        if (getCompute().getHasCores() != null) {
            return getCompute().getHasCores().getMax();
        }
        return null;
    }

    public void setMaxHasCores(Float cores) {
        if (getCompute().getHasCores() == null) {
            getCompute().setHasCores(new NumericRange());
        }
        getCompute().getHasCores().setMax(cores);
    }

    public Float getMinHasCores() {
        logger.debug("HardwareCategory = " + this.getCompute());
        if (getCompute().getHasCores() != null) {
            return getCompute().getHasCores().getMin();
        }
        return null;
    }

    public void setMinHasCores(Float cores) {
        if (getCompute().getHasCores() == null) {
            logger.debug(" || getCompute().getHasCore() is null");
            getCompute().setHasCores(new NumericRange());
        }
        logger.debug(" || setting getCompute().getHasCores() " + getCompute().getHasCores());
        getCompute().getHasCores().setMin(cores);
    }

    public Float getStepHasCores() {
        if (getCompute().getHasCores() != null) {
            return getCompute().getHasCores().getStep();
        }
        return null;
    }

    public void setStepHasCores(Float cores) {
        if (getCompute().getHasCores() == null) {
            getCompute().setHasCores(new NumericRange());
        }
        getCompute().getHasCores().setStep(cores);
    }

    public List<Float> getCoreValues() {
        if (getCompute().getHasCores() != null) {
            return getCompute().getHasCores().getOfferedNumericValues();
        }
        return null;
    }

    public void setCoreValues(List<Float> coreValues) {
        if (getCompute().getHasCores() == null) {
            getCompute().setHasCores(new NumericRange());
        }
        getCompute().getHasCores().setOfferedNumericValues(coreValues);
    }

    public void addCoreValue(Float coreValue) {
        if (getCompute().getHasCores() == null) {
            getCompute().setHasCores(new NumericRange());
        }
        getCompute().getHasCores().getOfferedNumericValues().add(coreValue);
    }

    /*MEMORY*/
    public StorageRange getMemoryRange() {
        if (getCompute().getMemory() != null) {
            return getCompute().getMemory();
        }
        return null;
    }

    public void setMemoryRange(StorageRange memory) {
        getCompute().setMemory(memory);
    }

    /*MAX MEMORY*/
    public Float getMaxMemoryValue() {
        if (getCompute().getMemory() != null) {
            if (getCompute().getMemory().getMax() != null) {
                return getCompute().getMemory().getMax().getValue();
            }
        }
        return null;
    }

    public void setMaxMemoryValue(Float memory) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        if (getCompute().getMemory().getMax() == null) {
            getCompute().getMemory().setMax(new StorageUnit());
        }

        getCompute().getMemory().getMax().setValue(memory);
    }

    public StorageUnitType getMaxMemoryUnit() {
        if (getCompute().getMemory() != null && getCompute().getMemory().getMax() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getCompute().getMemory().getMax().getClass());
        }
        return null;
    }

    public void setMaxMemoryUnit(StorageUnitType unitType) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getMemory().setMax(instance);
    }

    public void setMaxMemoryUnitAndValue(StorageUnitType unitType, Float memory) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getMemory().setMax(instance);
        getCompute().getMemory().getMax().setValue(memory);
    }

    /*MIN MEMORY*/
    public Float getMinMemoryValue() {
        if (getCompute().getMemory() != null) {
            if (getCompute().getMemory().getMin() != null) {
                return getCompute().getMemory().getMin().getValue();
            }
        }
        return null;
    }

    public void setMinMemoryValue(Float memory) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        if (getCompute().getMemory().getMin() == null) {
            getCompute().getMemory().setMin(new StorageUnit());
        }

        getCompute().getMemory().getMin().setValue(memory);
    }

    public StorageUnitType getMinMemoryUnit() {
        if (getCompute().getMemory() != null && getCompute().getMemory().getMin() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getCompute().getMemory().getMin().getClass());
        }
        return null;
    }

    public void setMinMemoryUnit(StorageUnitType unitType) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getMemory().setMin(instance);
    }

    public void setMinMemoryUnitAndValue(StorageUnitType unitType, Float memory) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getMemory().setMin(instance);
        getCompute().getMemory().getMin().setValue(memory);
    }

    /*MEMORY STEP*/
    public Float getStepMemoryValue() {
        if (getCompute().getMemory() != null) {
            if (getCompute().getMemory().getStep() != null) {
                return getCompute().getMemory().getStep().getValue();
            }
        }
        return null;
    }

    public void setStepMemoryValue(Float memory) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        if (getCompute().getMemory().getStep() == null) {
            getCompute().getMemory().setStep(new StorageUnit());
        }

        getCompute().getMemory().getStep().setValue(memory);
    }

    public StorageUnitType getStepMemoryUnit() {
        if (getCompute().getMemory() != null && getCompute().getMemory().getStep() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getCompute().getMemory().getStep().getClass());
        }
        return null;
    }

    public void setStepMemoryUnit(StorageUnitType unitType) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getMemory().setStep(instance);
    }

    public void setStepMemoryUnitAndValue(StorageUnitType unitType, Float memory) {
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getMemory().setStep(instance);
        getCompute().getMemory().getStep().setValue(memory);
    }

    /*MEMORY VALUE*/
    public List<StorageUnit> getStorageValues() {
        if (getCompute().getMemory() != null) {
            return getCompute().getMemory().getOfferedStorageValues();
        }
        return null;
    }

    public void setStorageValues(List<StorageUnit> storageValues) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        getCompute().getMemory().setOfferedStorageValues(storageValues);
    }

    public void addStorageUnitAndValue(StorageUnitType unitType, Float storageValue) {
        if (getCompute().getMemory() == null) {
            getCompute().setMemory(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        instance.setValue(storageValue);
        getCompute().getMemory().getOfferedStorageValues().add(instance);
    }


    /*CACHE*/
    public StorageRange getCacheRange() {
        if (getCompute().getCache() != null) {
            return getCompute().getCache();
        }
        return null;
    }

    public void setCacheRange(StorageRange cache) {
        getCompute().setCache(cache);
    }

    /*MAX CACHE*/
    public Float getMaxCacheValue() {
        if (getCompute().getCache() != null) {
            if (getCompute().getCache().getMax() != null) {
                return getCompute().getCache().getMax().getValue();
            }
        }
        return null;
    }

    public void setMaxCacheValue(Float cache) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        if (getCompute().getCache().getMax() == null) {
            getCompute().getCache().setMax(new StorageUnit());
        }

        getCompute().getCache().getMax().setValue(cache);
    }

    public StorageUnitType getMaxCacheUnit() {
        if (getCompute().getCache() != null && getCompute().getCache().getMax() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getCompute().getCache().getMax().getClass());
        }
        return null;
    }

    public void setMaxCahceUnit(StorageUnitType unitType) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getCache().setMax(instance);
    }

    public void setMaxCacheUnitAndValue(StorageUnitType unitType, Float cache) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getCache().setMax(instance);
        getCompute().getCache().getMax().setValue(cache);
    }

    /*MIN CACHE*/
    public Float getMinCacheValue() {
        if (getCompute().getCache() != null) {
            if (getCompute().getCache().getMin() != null) {
                return getCompute().getCache().getMin().getValue();
            }
        }
        return null;
    }

    public void setMinCacheValue(Float cache) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        if (getCompute().getCache().getMin() == null) {
            getCompute().getCache().setMin(new StorageUnit());
        }

        getCompute().getCache().getMin().setValue(cache);
    }

    public StorageUnitType getMinCacheUnit() {
        if (getCompute().getCache() != null && getCompute().getCache().getMin() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getCompute().getCache().getMin().getClass());
        }
        return null;
    }

    public void setMinCacheUnit(StorageUnitType unitType) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getCache().setMin(instance);
    }

    public void setMinCacheUnitAndValue(StorageUnitType unitType, Float cache) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getCache().setMin(instance);
        getCompute().getCache().getMin().setValue(cache);
    }

    /*CACHE STEP*/
    public Float getStepCacheValue() {
        if (getCompute().getCache() != null) {
            if (getCompute().getCache().getStep() != null) {
                return getCompute().getCache().getStep().getValue();
            }
        }
        return null;
    }

    public void setStepCacheValue(Float cache) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        if (getCompute().getCache().getStep() == null) {
            getCompute().getCache().setStep(new StorageUnit());
        }

        getCompute().getCache().getStep().setValue(cache);
    }

    public StorageUnitType getStepCacheUnit() {
        if (getCompute().getCache() != null && getCompute().getCache().getStep() != null) {
            return StorageUnitTypeHelper.getStorageUnitType(getCompute().getCache().getStep().getClass());
        }
        return null;
    }

    public void setStepCacheUnit(StorageUnitType unitType) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getCache().setStep(instance);
    }

    public void setStepCacheUnitAndValue(StorageUnitType unitType, Float cache) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        getCompute().getCache().setStep(instance);
        getCompute().getCache().getStep().setValue(cache);
    }

    /*MEMORY VALUE*/
    public List<StorageUnit> getCacheValues() {
        if (getCompute().getCache() != null) {
            return getCompute().getCache().getOfferedStorageValues();
        }
        return null;
    }

    public void setCacheValues(List<StorageUnit> storageValues) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        getCompute().getCache().setOfferedStorageValues(storageValues);
    }

    public void addCacheUnitAndValue(StorageUnitType unitType, Float storageValue) {
        if (getCompute().getCache() == null) {
            getCompute().setCache(new StorageRange());
        }
        StorageUnit instance = StorageUnitTypeHelper.getInstance(unitType);
        instance.setValue(storageValue);
        getCompute().getCache().getOfferedStorageValues().add(instance);
    }

}
