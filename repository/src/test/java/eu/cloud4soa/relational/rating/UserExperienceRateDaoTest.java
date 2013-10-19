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
package eu.cloud4soa.relational.rating;

import java.util.List;
import eu.cloud4soa.relational.datamodel.rating.UserExperienceRate;
import eu.cloud4soa.relational.persistence.rating.UserExperienceRateDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;

/**
 *
 * @author frarav
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/UserExperienceRatingBOCtx.xml"})
public class UserExperienceRateDaoTest {
    
    // static values set by the file src/test/resources/rating/importUserExperienceRateTest.sql
    protected static final Long APP2TEST_ID = Long.valueOf( 2l );
    protected static final String APP2TEST_URI_ID = "app2UriID";
    protected static final int RATE_VALUE = 4;
    
    protected static Logger logger = LoggerFactory.getLogger( UserExperienceRateDaoTest.class);
            
    @Autowired
    protected UserExperienceRateDao userExperienceRateDao;
    
    
    @Test
    public void testRetrieveThroughAppId() {
        UserExperienceRate targetRate;
        
        logger.debug( "Loading a rate for a specific applciation" );
        targetRate = userExperienceRateDao.getUserExperienceRate( APP2TEST_ID );
        
        assertTrue("The rate to be loaded should not be null", targetRate != null );
        assertTrue("The rate value is not the expected one", targetRate.getRate() == RATE_VALUE );
        
    }
    
    
    
    @Test
    public void testRetrieveThroughAppUriId() {
        UserExperienceRate targetRate;
        
        logger.debug( "Loading a rate for a specific applciation" );
        targetRate = userExperienceRateDao.getUserExperienceRateByAppUri( APP2TEST_URI_ID );
        
        assertTrue("The rate to be loaded should not be null", targetRate != null );
        assertTrue("The rate value is not the expected one", targetRate.getRate() == RATE_VALUE );
        
    }
    
    
    
    @Test
    public void testRetrieveByPaasUriID() {
        List<UserExperienceRate> ratesList;
        
        ratesList = userExperienceRateDao.retrieveByPaasUri( UserExperienceRateBOTest.PAAS1_URIID ); 
        assertTrue( "The retrieved number of rates is different from the one set", ratesList.size() == UserExperienceRateBOTest.RATES_NUMBER_SET_BY_INIT );
        
        ratesList = userExperienceRateDao.retrieveByPaasUri( UserExperienceRateBOTest.PAAS2_URIID ); 
        assertTrue( "Should not retrieve any rates", ratesList.size() == 0 );
    }
}
