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
package eu.cloud4soa.api.datamodel.core.utilBeans.helper;

import eu.cloud4soa.api.datamodel.semantic.measure.GigaByte;
import eu.cloud4soa.api.datamodel.semantic.measure.KiloByte;
import eu.cloud4soa.api.datamodel.semantic.measure.MegaByte;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.TeraByte;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vins
 */
public class StorageUnitTypeHelper {

    private static Map<StorageUnitType, Class> mapping = new HashMap<StorageUnitType, Class>(){{
        put(StorageUnitType.Unknown, StorageUnit.class);
        put(StorageUnitType.KiloByte, KiloByte.class);
        put(StorageUnitType.MegaByte, MegaByte.class);
        put(StorageUnitType.GigaByte, GigaByte.class);
        put(StorageUnitType.TeraByte, TeraByte.class);
    }};

    public static StorageUnitType getStorageUnitType(Class unitClass){
        Set<StorageUnitType> keySet = mapping.keySet();
        for (StorageUnitType storageUnitType : keySet) {
            if(mapping.get(storageUnitType).equals(unitClass))
                return storageUnitType;
        }
        return null;
    }
    
    public static Class getClass(StorageUnitType unitType){
        return mapping.get(unitType);
    }
    
    public static StorageUnit getInstance(StorageUnitType unitType){
        try {
            Class<StorageUnit> unitClass = mapping.get(unitType);
            return unitClass.getDeclaredConstructor((Class<?>[]) null).newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(StorageUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(StorageUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(StorageUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(StorageUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(StorageUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(StorageUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
