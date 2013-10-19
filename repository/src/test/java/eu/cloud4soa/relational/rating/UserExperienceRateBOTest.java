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

import eu.cloud4soa.api.datamodel.repository.FiveStarsRate;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.relational.businessobjects.UserExperienceRateBO;
import eu.cloud4soa.relational.datamodel.rating.UserExperienceRate;
import eu.cloud4soa.relational.persistence.rating.UserExperienceRateDao;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
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
@Ignore
public class UserExperienceRateBOTest {
    
    // static values derived by the file src/test/resources/rating/importUserExperienceRateTest.sql
    protected static final String APP1TEST_URIID = "app1UriID";
    protected static final String APP2TEST_URIID = "app2UriID";
    protected static final String APP3TEST_URIID = "app3UriID";
    protected static final String APP4TEST_URIID = "app4UriID";
    protected static final int RATE1_VALUE = 4;
    protected static final int RATE2_VALUE = 2;
    public static final String PAAS1_URIID = "paas1UriID";
    public static final String PAAS2_URIID = "paas2UriID";
    public static final int RATES_NUMBER_SET_BY_INIT = 3;
    
    Logger logger = LoggerFactory.getLogger(UserExperienceRateBOTest.class);
    
    @Autowired
    private UserExperienceRateBO userExperienceRateBO;
    
    @Autowired
    protected UserExperienceRateDao userExperienceRateDao;
    
    
    
/*    @Before
    public void checkingTheSetup() {
        logger.debug( "Verifying that the database has been correctly set");
        assertTrue( "Database should contain at least 1 rate",
                userExperienceRateDao.retrieveAll().size() == RATES_NUMBER_SET_BY_INIT );
    }
     * 
     */
    
    
    
    @Test(expected=SOAException.class)
    public void testInsertRateForMissingApp() throws SOAException {
        String appURI;
        FiveStarsRate rate;
        
        logger.debug( "Test the creation of a rate object when the relative application is missing; expected an exception." );
        appURI = "notExistingURI";
        rate = new FiveStarsRate( 3 );
        userExperienceRateBO.storeUserExperienceRate(appURI, rate);
    }
    
    
    
    @Test
    public void testInsertRateForExistingApp() throws Exception {
        String appURI;
        FiveStarsRate rate;
        int expectedNumberOfRates;
        List<UserExperienceRate> userRateInserted;
        UserExperienceRate insertedRate;
       
        logger.debug( "Test the creation of a rate object." );
        appURI = APP1TEST_URIID;
        rate = this.getRandomRateValue();
        userExperienceRateBO.storeUserExperienceRate(appURI, rate);
        
        logger.debug( "UserExperienceRate successfully created; let's verify it." );
        userRateInserted = userExperienceRateDao.retrieveAll();
        expectedNumberOfRates = RATES_NUMBER_SET_BY_INIT + 1;
        assertTrue( "In the database I should have " + userRateInserted + " rates", 
                userRateInserted.size() == expectedNumberOfRates );
        
        insertedRate = this.retrieveSpecificRate(userRateInserted, appURI);
        assertTrue( rate.getRate() == insertedRate.getRate().intValue());
    }
    
    
    
    @Test
    public void testLoadExistingRateByAppUriId() throws Exception {
        FiveStarsRate targetRate;
        
        targetRate = userExperienceRateBO.getUserExperienceRate(APP2TEST_URIID);
        
        assertTrue( targetRate.getRate() ==  RATE1_VALUE);
        
    }
    
    
    
    
    @Test
    public void testUserExperienceRateDelete() throws Exception {
        int ratesInTheDatabase;
        
        logger.debug( "counting the namber of rates in the database");
        ratesInTheDatabase = userExperienceRateDao.retrieveAll().size();
        
        logger.debug("Deleting one UserRate ");
        userExperienceRateBO.deleteUserExperienceRate( APP4TEST_URIID );
        
        logger.debug( "Verifying the rate has been deleted");
        assertTrue( "Database should contain one object less", 
                userExperienceRateDao.retrieveAll().size() == (ratesInTheDatabase - 1) );
        
    }
    
    
    
    @Test
    public void testUserExperienceRateUpdate() throws Exception {
        int newRateValue;
        UserExperienceRate rateToModify;
        UserExperienceRate modifiedRate;
        
        newRateValue = 3;
        
        logger.debug("Getting the Rate and verifying the value");
        rateToModify = userExperienceRateDao.getUserExperienceRateByAppUri( APP3TEST_URIID );
        assertTrue( "The loaded rate doesn't have the expected value", RATE2_VALUE == rateToModify.getRate().intValue() );
        
        logger.debug( "Modifying and updating the rate on the database");
        userExperienceRateBO.updateUserExperienceRate( APP3TEST_URIID, newRateValue );
        
        logger.debug( "Verifying the rate has been modified");
        modifiedRate = userExperienceRateDao.getUserExperienceRateByAppUri( APP3TEST_URIID );
        assertTrue( "The rate hasn't been modified", newRateValue == modifiedRate.getRate().intValue() );
        
    }
    
    
    
    
    @Test
    public void TestRetrieveAllPaasRates() throws Exception {
        List<FiveStarsRate> ratesPerPaas1;
        
        ratesPerPaas1 = userExperienceRateBO.getPaasUserExperienceEvaluation( PAAS1_URIID );
        
        assertTrue( ratesPerPaas1.size() == RATES_NUMBER_SET_BY_INIT );
    }
    
    
    
    protected UserExperienceRate retrieveSpecificRate( List<UserExperienceRate> rateList, String appURI) {
        UserExperienceRate targetRate;
        
        targetRate = null;
        for ( UserExperienceRate listElement: rateList ) {
            if ( listElement.getApplicationInstance().getUriID().equals( appURI ) ) {
                assertTrue( "Found another rate with the same app uriID", targetRate == null );
                targetRate = listElement;
            }
        }
        assertTrue( "UserExperienceRate not found", targetRate != null );
        
        return targetRate;
    }
    
    
    
    protected FiveStarsRate getRandomRateValue() {
        int randomRate;
        
        randomRate = 1 + (int)( Math.random() * 5);
        assertTrue( randomRate >= 1);
        assertTrue( randomRate <= 5);
        return new FiveStarsRate( randomRate );
    }
    
    
}
