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
package eu.cloud4soa.relational.businessobjects;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import eu.cloud4soa.api.datamodel.repository.FiveStarsRate;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.relational.datamodel.ApplicationInstance;
import eu.cloud4soa.relational.datamodel.rating.UserExperienceRate;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import eu.cloud4soa.relational.persistence.rating.UserExperienceRateDao;

/**
 *
 * @author frarav
 */
public class UserExperienceRateBO implements eu.cloud4soa.api.datamodel.repository.UserExperienceRate{

    @Autowired
    protected UserExperienceRateDao userExperienceRateDao;
    
    @Autowired
    protected ApplicationInstanceRepository applicationInstanceRepository;
    
    
    
    @Override
    public void deleteUserExperienceRate( String appURI)  throws SOAException {
        UserExperienceRate targetRate;
        
        targetRate = userExperienceRateDao.getUserExperienceRateByAppUri( appURI );
        
        userExperienceRateDao.delete( targetRate );
    }

    
    
    @Override
    public FiveStarsRate getUserExperienceRate(String appURI ) throws SOAException {
    	FiveStarsRate result = null;
        UserExperienceRate targetRate = userExperienceRateDao.getUserExperienceRateByAppUri( appURI );
        if (targetRate != null)
        	result = new FiveStarsRate( targetRate.getRate().intValue() );
        return result;
    }

    
    
    @Override
    public void storeUserExperienceRate( String appURI, FiveStarsRate rate) throws SOAException {
        UserExperienceRate userExperienceRate;
        ApplicationInstance application;
        
        application = this.getApplicationInstance( appURI );
        userExperienceRate = new UserExperienceRate( application, rate.getRate() );
        userExperienceRateDao.store( userExperienceRate );
        
    }

    
    
    @Override
    public void updateUserExperienceRate( String appURI, int rate) throws SOAException {
        UserExperienceRate targetRate;
        
        targetRate = userExperienceRateDao.getUserExperienceRateByAppUri( appURI );
         
        targetRate.setRate( Short.valueOf( Integer.valueOf( rate).shortValue() ) );
        
        userExperienceRateDao.update( targetRate );
    }

    
    
    @Override
    public List<FiveStarsRate> getPaasUserExperienceEvaluation(String paasURI) throws SOAException {
        List<FiveStarsRate> ratesValuesList;
        List<UserExperienceRate> userExperienceRatesList;
        
        userExperienceRatesList = userExperienceRateDao.retrieveByPaasUri( paasURI );
        ratesValuesList = getRatesValues( userExperienceRatesList );
        
        return ratesValuesList;
    }
    
    
    
    protected ApplicationInstance getApplicationInstance( String uriId) throws SOAException {
        List<ApplicationInstance> applicationList;
        ApplicationInstance application;
        
        
        applicationList = applicationInstanceRepository.findByUriId( uriId );
        if ( applicationList == null || applicationList.isEmpty() ) {
            throw new SOAException(Status.NOT_FOUND, " Application with URI " + uriId + " not found.");
        }
        application = applicationList.get(0);
        
        return application;
    }
    
    
    
    protected List<FiveStarsRate> getRatesValues(List<UserExperienceRate> userExperienceRatesList) {
        List<FiveStarsRate> rateValuesList;
        
        rateValuesList = new ArrayList<FiveStarsRate>();
        for( UserExperienceRate listElement: userExperienceRatesList) {
            rateValuesList.add( listElement.getRateAsFiveStarsRate() );
        }
        
        return rateValuesList;
    }
}
