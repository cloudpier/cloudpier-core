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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "serviceQualityInstance")//, namespace="eu.cloud4soa.api.datamodel.core.qos")
@XmlSeeAlso({LatencyInstance.class, UptimeInstance.class, CPULoadInstance.class,CloudResponseTimeInstance.class,ContainerResponseTimeInstance.class, MemoryLoadInstance.class})
public class ServiceQualityInstance {
    private Technology_Service_Quality serviceQuality;

    public ServiceQualityInstance() {
        this.serviceQuality = new Technology_Service_Quality();
    }
    
    public ServiceQualityInstance(Technology_Service_Quality serviceQuality) {
        this.serviceQuality = serviceQuality;
    }
    
    public Technology_Service_Quality getServiceQuality() {
        return serviceQuality;
    }

    protected void setServiceQuality(Technology_Service_Quality serviceQuality) {
        this.serviceQuality = serviceQuality;
    }
    
}
