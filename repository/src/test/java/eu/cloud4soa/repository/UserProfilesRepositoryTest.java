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
package eu.cloud4soa.repository;

import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.Cloud4SoaAccountInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author vins
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = {"classpath:UserProfilesRepositoryCtx.xml"})
public class UserProfilesRepositoryTest {
    final Logger logger = LoggerFactory.getLogger(UserProfilesRepositoryTest.class);
    
    @Autowired
    private UserProfilesRepository userProfilesRepository;
    private UserInstance userInstance;
	
    @Before
    public void setUp(){
         userInstance = createTestUserInstance();
    }
    
    
    
    
    @Test
    @DirtiesContext
    public void TestGetUserInstanceFromAccountName (){
        UserInstance retrievedUserInstance = null;
        try {
            userProfilesRepository.createUserInstance(userInstance);
        } catch (RepositoryException ex) {
            Assert.fail("createUserInstance method has thrown an unexpected exception!");
        }
        try {
            retrievedUserInstance = userProfilesRepository.getUserInstanceFromAccountName(userInstance.getAccountname());
        } catch (RepositoryException ex) {
            Assert.fail("createUserInstance method has thrown an unexpected exception!");
        }
        Assert.assertEquals("Different userInstanceUriId", userInstance.getUriId(), retrievedUserInstance.getUriId());
        
        String nonExistingAccountname = "nonExistingAccountname";
        try {
            retrievedUserInstance = userProfilesRepository.getUserInstanceFromAccountName(nonExistingAccountname);
        } catch (RepositoryException ex) {
            Assert.fail("getUserInstanceFromAccountName method has thrown an unexpected exception!");
        }
        Assert.assertNull("retrived a UriID != null for nonExistingAccountname", retrievedUserInstance);
    }
    
    private UserInstance createTestUserInstance() {
        UserInstance userInstance = new DeveloperInstance();
        userInstance.setFirstName("Yosu");
        userInstance.setFamilyname("Gorroñogoitia");
        userInstance.setAccountname("yosu");
        userInstance.setGeekcode("yosu");
        userInstance.setSurname("Gorroñogoitia");
        String date = 1967 + "/" + 3 + "/" + 11;
        java.util.Date utilDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            utilDate = formatter.parse(date);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        userInstance.setBirthday(utilDate);
        //Assigned by the system
//		userInstance.setCloud4SoaAccountUriId("http:www.cloud4soa.eu/yosu#");
        Cloud4SoaAccountInstance cloud4SoaAccountInstance = new Cloud4SoaAccountInstance();
        cloud4SoaAccountInstance.setAccountname("yosu");
//        Assigned by the system
//		cloud4SoaAccountInstance.setUriId(getUUID());
        userInstance.setHoldsaccount(cloud4SoaAccountInstance);
        return userInstance;
    }
    
}
