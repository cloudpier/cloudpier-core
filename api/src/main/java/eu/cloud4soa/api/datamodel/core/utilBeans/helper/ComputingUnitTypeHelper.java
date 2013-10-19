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

import eu.cloud4soa.api.datamodel.semantic.measure.ComputingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.Flops;
import eu.cloud4soa.api.datamodel.semantic.measure.GigaHertz;
import eu.cloud4soa.api.datamodel.semantic.measure.KiloHertz;
import eu.cloud4soa.api.datamodel.semantic.measure.MegaHertz;
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
public class ComputingUnitTypeHelper {

    private static Map<ComputingUnitType, Class> mapping = new HashMap<ComputingUnitType, Class>(){{
        put(ComputingUnitType.Unknown, ComputingUnit.class);
        put(ComputingUnitType.KiloHertz, KiloHertz.class);
        put(ComputingUnitType.MegaHertz, MegaHertz.class);
        put(ComputingUnitType.GigaHertz, GigaHertz.class);
        put(ComputingUnitType.Flops, Flops.class);
    }};

    public static ComputingUnitType getComputingUnitType(Class unitClass){
        Set<ComputingUnitType> keySet = mapping.keySet();
        for (ComputingUnitType computingUnitType : keySet) {
            if(mapping.get(computingUnitType).equals(unitClass))
                return computingUnitType;
        }
        return null;
    }
    
    public static Class getClass(ComputingUnitType unitType){
        return mapping.get(unitType);
    }
    
    public static ComputingUnit getInstance(ComputingUnitType unitType){
        try {
            Class<ComputingUnit> unitClass = mapping.get(unitType);
            return unitClass.getDeclaredConstructor((Class<?>[]) null).newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(ComputingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ComputingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ComputingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ComputingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ComputingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ComputingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
   
}
