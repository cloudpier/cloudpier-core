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
package eu.cloud4soa.api.datamodel.repository;

import java.lang.IllegalArgumentException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author frarav
 */
@XmlRootElement()
@XmlType(name = "fiveStarsRate", namespace = "eu.cloud4soa.api.datamodel.repository")
public class FiveStarsRate {
    
    protected int rate;
    
    public  FiveStarsRate( int rate ) {
        this.validateRate(rate);
    }

    
    
    public int getRate() {
        return rate;
    }

    
    
    public void setRate(int rate) {
        this.validateRate(rate);
    }
    
    
    
    public Short toShort() {
        return Short.valueOf( Integer.valueOf( rate ).shortValue() );
    }
    
    
    
    protected void validateRate( int rate ) {
        if ( (rate < 1 ) || ( rate > 5 ) ) {
            throw new IllegalArgumentException();
        } else {
            this.rate = rate;
        }
    }
    
}
