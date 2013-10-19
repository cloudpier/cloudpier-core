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

import eu.cloud4soa.api.datamodel.semantic.ea.ContainerResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.measure.MilliSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.TimeRange;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Dimitris
 */
@XmlRootElement()
@XmlType(name = "containerResponseTimeInstance")
public class ContainerResponseTimeInstance extends ServiceQualityInstance {

    public ContainerResponseTimeInstance(ContainerResponseTime containerResponseTime) {
        super(containerResponseTime);
    }
    
    public ContainerResponseTimeInstance() {
        super(new ContainerResponseTime());
    }
    
    private ContainerResponseTime getContainerResponseTime(){
        return (ContainerResponseTime)super.getServiceQuality();
    }
    
    private void setContainerResponseTime(ContainerResponseTime containerResponseTime){
        super.setServiceQuality(containerResponseTime);
    }


    public void setHasTimeRangeValue(Float minValue, Float maxValue) {
        TimeRange timeRange = new TimeRange();
        MilliSecond minValueMs = new MilliSecond();
        MilliSecond maxValueMs = new MilliSecond();
        minValueMs.setValue(minValue);
        maxValueMs.setValue(maxValue);
        timeRange.setMin(minValueMs);
        timeRange.setMax(maxValueMs);
        if(getContainerResponseTime()==null){
            ContainerResponseTime containerResponseTime = new ContainerResponseTime();
            setContainerResponseTime(containerResponseTime);
        }
        getContainerResponseTime().setHasTimeRangeValue(timeRange);
    }
    
    public Float getMinValueMs(){
        if(getContainerResponseTime()!=null && getContainerResponseTime().getHasTimeRangeValue() != null && getContainerResponseTime().getHasTimeRangeValue().getMin() != null){
            return getContainerResponseTime().getHasTimeRangeValue().getMin().getValue();
        }
        return null;
    }
    
    public Float getMaxValueMs(){
        if(getContainerResponseTime()!=null && getContainerResponseTime().getHasTimeRangeValue() != null && getContainerResponseTime().getHasTimeRangeValue().getMax() != null){
            return getContainerResponseTime().getHasTimeRangeValue().getMax().getValue();
        }
        return null;
    }
    
    public void setMinValueMs(Float minValue){
        if(getContainerResponseTime()!=null && getContainerResponseTime().getHasTimeRangeValue() != null){
            MilliSecond minValueMs = new MilliSecond();
            minValueMs.setValue(minValue);
            getContainerResponseTime().getHasTimeRangeValue().setMin(minValueMs);
        }
        else{
            if(getContainerResponseTime()==null){
                ContainerResponseTime containerResponseTime = new ContainerResponseTime();
                setContainerResponseTime(containerResponseTime);
            }
            if(getContainerResponseTime().getHasTimeRangeValue() == null){
                TimeRange timeRange = new TimeRange();
                MilliSecond minValueMs = new MilliSecond();
                minValueMs.setValue(minValue);
                timeRange.setMin(minValueMs);
                getContainerResponseTime().setHasTimeRangeValue(timeRange);
            }
        }
    }
    
    public void setMaxValueMs(Float maxValue){
        if(getContainerResponseTime()!=null && getContainerResponseTime().getHasTimeRangeValue() != null){
            MilliSecond maxValueMs = new MilliSecond();
            maxValueMs.setValue(maxValue);
            getContainerResponseTime().getHasTimeRangeValue().setMax(maxValueMs);
        }
        else{
            if(getContainerResponseTime()==null){
                ContainerResponseTime containerResponseTime = new ContainerResponseTime();
                setContainerResponseTime(containerResponseTime);
            }
            if(getContainerResponseTime().getHasTimeRangeValue() == null){
                TimeRange timeRange = new TimeRange();
                MilliSecond maxValueMs = new MilliSecond();
                maxValueMs.setValue(maxValue);
                timeRange.setMax(maxValueMs);
                getContainerResponseTime().setHasTimeRangeValue(timeRange);
            }
        }
    }
    
    @Override
    public Technology_Service_Quality getServiceQuality() {
        return getContainerResponseTime();
    }
  
}
