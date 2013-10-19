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
package eu.cloud4soa.relational.persistence.rating;

import eu.cloud4soa.relational.datamodel.rating.UserExperienceRate;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author frarav
 */
@Repository
public class UserExperienceRateDao extends AbstractHbmDao<UserExperienceRate> {
    
    
    @Autowired
	public UserExperienceRateDao(SessionFactory sessionFactory) {
		super(sessionFactory, UserExperienceRate.class);
	}
    
    
    
    public void store(UserExperienceRate account) {
		this.saveOrUpdate(account);
	}

    
    
    
	public void update(UserExperienceRate account) {
		super.update(account);
	}

    
    
    
	public void delete(UserExperienceRate account) {
		super.delete(account);
	}

    
    
    
	public List<UserExperienceRate> retrieveAll() {
		return this.findAll();
	}	
    
    
    
    
/*    public List<UserExperienceRate> retrieveAll(Long userId) {
            List<UserExperienceRate> accountList = this.find("user.id = ?", userId);
        return accountList;
	}
     * 
     */
    
    
    
    public UserExperienceRate getUserExperienceRate(Long applicationInstanceId) {
        UserExperienceRate targetRate;
        List<UserExperienceRate> ratesList;
        
        ratesList = this.find(" appId = ? ", applicationInstanceId);
        
        if ( ratesList == null || ratesList.isEmpty() ) {
             targetRate = null;
        } else if ( ratesList.size() > 1) {
            throw new IndexOutOfBoundsException("Found more then one rate for the same application id");
        } else {
            targetRate = ratesList.get(0);
        }
        
        return targetRate;
    }
    
    
    
    public UserExperienceRate getUserExperienceRateByAppUri(String appUriId) {
        UserExperienceRate targetRate;
        List<UserExperienceRate> ratesList;
        
        ratesList = this.find(" applicationInstance.uriID = ? ", appUriId);
        
        if ( ratesList == null || ratesList.isEmpty() ) {
             targetRate = null;
        } else if ( ratesList.size() > 1) {
            throw new IndexOutOfBoundsException("Found more then one rate for the same application id");
        } else {
            targetRate = ratesList.get(0);
        }
        
        return targetRate;
    }
    
    
    public List<UserExperienceRate> retrieveByPaasUri( String paasUriID) {
        List<UserExperienceRate> ratesList;
        
        ratesList = this.find(" applicationInstance.account.paas.uriID = ? ", paasUriID);
        
        return ratesList;
	}   
}
