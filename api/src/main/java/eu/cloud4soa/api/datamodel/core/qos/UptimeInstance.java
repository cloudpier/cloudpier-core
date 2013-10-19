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

import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.ea.Uptime;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "uptimeInstance")
public class UptimeInstance extends ServiceQualityInstance {

    public UptimeInstance(Uptime uptime) {
        super(uptime);
    }

    public UptimeInstance() {
        super(new Uptime());
    }
    
    private Uptime getUptime(){
        return (Uptime)super.getServiceQuality();
    }
    
    private void setUptime(Uptime uptime){
        super.setServiceQuality(uptime);
    }
    
    
    public Float getHasPercentage(){
        return getUptime().getHasPercentage();
    }
    
    public void setHasPercentage(Float percenageValue){
        getUptime().setHasPercentage(percenageValue);
    }

    @Override
    public Technology_Service_Quality getServiceQuality() {
        return getUptime();
    }

//    @Override
//    public void setServiceQuality(Technology_Service_Quality serviceQuality) {
//        setUptime((Uptime)serviceQuality);
//    }
    
    

    
    
}
