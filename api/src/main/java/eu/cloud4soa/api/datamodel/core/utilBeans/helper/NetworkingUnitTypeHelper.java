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

import eu.cloud4soa.api.datamodel.semantic.measure.GigaByteperSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.KiloByteperSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.MegaByteperSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.TeraByteperSecond;
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
public class NetworkingUnitTypeHelper {

    private static Map<NetworkingUnitType, Class> mapping = new HashMap<NetworkingUnitType, Class>(){{
        put(NetworkingUnitType.Unknown, NetworkingUnit.class);
        put(NetworkingUnitType.KiloByteperSecond, KiloByteperSecond.class);
        put(NetworkingUnitType.MegaByteperSecond, MegaByteperSecond.class);
        put(NetworkingUnitType.GigaByteperSecond, GigaByteperSecond.class);
        put(NetworkingUnitType.TeraByteperSecond, TeraByteperSecond.class);
    }};

    public static NetworkingUnitType getNetworkingUnitType(Class unitClass){
        Set<NetworkingUnitType> keySet = mapping.keySet();
        for (NetworkingUnitType networkingUnitType : keySet) {
            if(mapping.get(networkingUnitType).equals(unitClass))
                return networkingUnitType;
        }
        return null;
    }
    
    public static Class getClass(NetworkingUnitType unitType){
        return mapping.get(unitType);
    }
    
    public static NetworkingUnit getInstance(NetworkingUnitType unitType){
        try {
            Class<NetworkingUnit> unitClass = mapping.get(unitType);
            return unitClass.getDeclaredConstructor((Class<?>[]) null).newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(NetworkingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NetworkingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NetworkingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(NetworkingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(NetworkingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(NetworkingUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
