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

import eu.cloud4soa.api.datamodel.semantic.ea.CloudResponseTime;
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
@XmlType(name = "cloudResponseTimeInstance")
public class CloudResponseTimeInstance extends ServiceQualityInstance {

    public CloudResponseTimeInstance(CloudResponseTime cloudResponseTime) {
        super(cloudResponseTime);
    }
    
    public CloudResponseTimeInstance() {
        super(new CloudResponseTime());
    }
    
    private CloudResponseTime getCloudResponseTime(){
        return (CloudResponseTime)super.getServiceQuality();
    }
    
    private void setCloudResponseTime(CloudResponseTime cloudResponseTime){
        super.setServiceQuality(cloudResponseTime);
    }


    public void setHasTimeRangeValue(Float minValue, Float maxValue) {
        TimeRange timeRange = new TimeRange();
        MilliSecond minValueMs = new MilliSecond();
        MilliSecond maxValueMs = new MilliSecond();
        minValueMs.setValue(minValue);
        maxValueMs.setValue(maxValue);
        timeRange.setMin(minValueMs);
        timeRange.setMax(maxValueMs);
        if(getCloudResponseTime()==null){
            CloudResponseTime cloudResponseTime = new CloudResponseTime();
            setCloudResponseTime(cloudResponseTime);
        }
        getCloudResponseTime().setHasTimeRangeValue(timeRange);
    }
    
    public Float getMinValueMs(){
        if(getCloudResponseTime()!=null && getCloudResponseTime().getHasTimeRangeValue() != null && getCloudResponseTime().getHasTimeRangeValue().getMin() != null){
            return getCloudResponseTime().getHasTimeRangeValue().getMin().getValue();
        }
        return null;
    }
    
    public Float getMaxValueMs(){
        if(getCloudResponseTime()!=null && getCloudResponseTime().getHasTimeRangeValue() != null && getCloudResponseTime().getHasTimeRangeValue().getMax() != null){
            return getCloudResponseTime().getHasTimeRangeValue().getMax().getValue();
        }
        return null;
    }
    
    public void setMinValueMs(Float minValue){
        if(getCloudResponseTime()!=null && getCloudResponseTime().getHasTimeRangeValue() != null){
            MilliSecond minValueMs = new MilliSecond();
            minValueMs.setValue(minValue);
            getCloudResponseTime().getHasTimeRangeValue().setMin(minValueMs);
        }
        else{
            if(getCloudResponseTime()==null){
                CloudResponseTime cloudResponseTime = new CloudResponseTime();
                setCloudResponseTime(cloudResponseTime);
            }
            if(getCloudResponseTime().getHasTimeRangeValue() == null){
                TimeRange timeRange = new TimeRange();
                MilliSecond minValueMs = new MilliSecond();
                minValueMs.setValue(minValue);
                timeRange.setMin(minValueMs);
                getCloudResponseTime().setHasTimeRangeValue(timeRange);
            }
        }
    }
    
    public void setMaxValueMs(Float maxValue){
        if(getCloudResponseTime()!=null && getCloudResponseTime().getHasTimeRangeValue() != null){
            MilliSecond maxValueMs = new MilliSecond();
            maxValueMs.setValue(maxValue);
            getCloudResponseTime().getHasTimeRangeValue().setMax(maxValueMs);
        }
        else{
            if(getCloudResponseTime()==null){
                CloudResponseTime cloudResponseTime = new CloudResponseTime();
                setCloudResponseTime(cloudResponseTime);
            }
            if(getCloudResponseTime().getHasTimeRangeValue() == null){
                TimeRange timeRange = new TimeRange();
                MilliSecond maxValueMs = new MilliSecond();
                maxValueMs.setValue(maxValue);
                timeRange.setMax(maxValueMs);
                getCloudResponseTime().setHasTimeRangeValue(timeRange);
            }
        }
    }
    
    @Override
    public Technology_Service_Quality getServiceQuality() {
        return getCloudResponseTime();
    }
  
}
