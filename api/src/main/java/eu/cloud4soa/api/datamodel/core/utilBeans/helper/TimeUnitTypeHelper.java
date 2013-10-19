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

import eu.cloud4soa.api.datamodel.semantic.measure.Hour;
import eu.cloud4soa.api.datamodel.semantic.measure.MilliSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.Minute;
import eu.cloud4soa.api.datamodel.semantic.measure.Second;
import eu.cloud4soa.api.datamodel.semantic.measure.TimeUnit;
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
public class TimeUnitTypeHelper {

    private static Map<TimeUnitType, Class> mapping = new HashMap<TimeUnitType, Class>(){{
        put(TimeUnitType.Unknown, TimeUnit.class);
        put(TimeUnitType.Millisecond, MilliSecond.class);
        put(TimeUnitType.Second, Second.class);
        put(TimeUnitType.Minute, Minute.class);
        put(TimeUnitType.Hour, Hour.class);
    }};

    public static TimeUnitType getTimeUnitType(Class unitClass){
        Set<TimeUnitType> keySet = mapping.keySet();
        for (TimeUnitType timeUnitType : keySet) {
            if(mapping.get(timeUnitType).equals(unitClass))
                return timeUnitType;
        }
        return null;
    }
    
    public static Class getClass(TimeUnitType unitType){
        return mapping.get(unitType);
    }
    
    public static TimeUnit getInstance(TimeUnitType unitType){
        try {
            Class<TimeUnit> unitClass = mapping.get(unitType);
            return unitClass.getDeclaredConstructor((Class<?>[]) null).newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(TimeUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TimeUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TimeUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TimeUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TimeUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TimeUnitTypeHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
