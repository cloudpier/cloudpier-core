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
package eu.cloud4soa.relational.datamodel.rating;

import eu.cloud4soa.api.datamodel.repository.FiveStarsRate;
import eu.cloud4soa.relational.datamodel.AbstractModel;
import eu.cloud4soa.relational.datamodel.ApplicationInstance;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import static javax.persistence.GenerationType.IDENTITY;

/**
 *
 * @author frarav
 */
@Entity
@Table(name = "UserExperienceRate")
public class UserExperienceRate extends AbstractModel<UserExperienceRate> implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appId", nullable = false)
    protected ApplicationInstance applicationInstance;
    
    @Column(name = "rate", nullable = false)
    protected Short rate;

    
    
    protected UserExperienceRate() {
        
    }
    
    
    public UserExperienceRate( ApplicationInstance application, int rate) {
        
        if ( rate  < 1 || rate > 5) {
            throw new IllegalArgumentException( "Rate parameter should be and integer greater then 0 and less then 6" );
        }
        this.applicationInstance = application;
        this.rate = new Short((new Integer( rate )).shortValue());
    }

    
        public ApplicationInstance getApplicationInstance() {
        return applicationInstance;
    }

    
    
    public void setApplicationInstance(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    
    
    @Override
    public Long getId() {
        return id;
    }

    
    
    @Override
    public void setId(Long id) {
        this.id = id;
    }


    
    public Short getRate() {
        return rate;
    }

    
    
    public void setRate(Short rate) {
        this.rate = rate;
    }
    
    
    
    public FiveStarsRate getRateAsFiveStarsRate() {
        FiveStarsRate convertedRate;
        
        convertedRate = new FiveStarsRate( rate.intValue() );
        
        return convertedRate;
    } 
}
