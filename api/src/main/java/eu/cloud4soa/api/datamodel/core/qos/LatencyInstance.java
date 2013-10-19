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

import eu.cloud4soa.api.datamodel.semantic.ea.Latency;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.measure.MilliSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.TimeRange;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "latencyInstance")
public class LatencyInstance extends ServiceQualityInstance {

    public LatencyInstance(Latency latency) {
        super(latency);
    }
    
    public LatencyInstance() {
        super(new Latency());
    }
    
    private Latency getLatency(){
        return (Latency)super.getServiceQuality();
    }
    
    private void setLatency(Latency latency){
        super.setServiceQuality(latency);
    }


    public void setHasTimeRangeValue(Float minValue, Float maxValue) {
        TimeRange timeRange = new TimeRange();
        MilliSecond minValueMs = new MilliSecond();
        MilliSecond maxValueMs = new MilliSecond();
        minValueMs.setValue(minValue);
        maxValueMs.setValue(maxValue);
        timeRange.setMin(minValueMs);
        timeRange.setMax(maxValueMs);
        if(getLatency()==null){
            Latency latency = new Latency();
            setLatency(latency);
        }
        getLatency().setHasTimeRangeValue(timeRange);
    }
    
    public Float getMinValueMs(){
        if(getLatency()!=null && getLatency().getHasTimeRangeValue() != null && getLatency().getHasTimeRangeValue().getMin() != null){
            return getLatency().getHasTimeRangeValue().getMin().getValue();
        }
        return null;
    }
    
    public Float getMaxValueMs(){
        if(getLatency()!=null && getLatency().getHasTimeRangeValue() != null && getLatency().getHasTimeRangeValue().getMax() != null){
            return getLatency().getHasTimeRangeValue().getMax().getValue();
        }
        return null;
    }
    
    public void setMinValueMs(Float minValue){
        if(getLatency()!=null && getLatency().getHasTimeRangeValue() != null){
            MilliSecond minValueMs = new MilliSecond();
            minValueMs.setValue(minValue);
            getLatency().getHasTimeRangeValue().setMin(minValueMs);
        }
        else{
            if(getLatency()==null){
                Latency latency = new Latency();
                setLatency(latency);
            }
            if(getLatency().getHasTimeRangeValue() == null){
                TimeRange timeRange = new TimeRange();
                MilliSecond minValueMs = new MilliSecond();
                minValueMs.setValue(minValue);
                timeRange.setMin(minValueMs);
                getLatency().setHasTimeRangeValue(timeRange);
            }
        }
    }
    
    public void setMaxValueMs(Float maxValue){
        if(getLatency()!=null && getLatency().getHasTimeRangeValue() != null){
            MilliSecond maxValueMs = new MilliSecond();
            maxValueMs.setValue(maxValue);
            getLatency().getHasTimeRangeValue().setMax(maxValueMs);
        }
        else{
            if(getLatency()==null){
                Latency latency = new Latency();
                setLatency(latency);
            }
            if(getLatency().getHasTimeRangeValue() == null){
                TimeRange timeRange = new TimeRange();
                MilliSecond maxValueMs = new MilliSecond();
                maxValueMs.setValue(maxValue);
                timeRange.setMax(maxValueMs);
                getLatency().setHasTimeRangeValue(timeRange);
            }
        }
    }
    
    @Override
    public Technology_Service_Quality getServiceQuality() {
        return getLatency();
    }

//    @Override
//    public void setServiceQuality(Technology_Service_Quality serviceQuality) {
//        setServiceQuality((Latency)serviceQuality);
//    }
    
    
}
