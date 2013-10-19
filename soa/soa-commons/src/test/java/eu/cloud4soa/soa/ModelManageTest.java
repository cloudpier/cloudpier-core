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


package eu.cloud4soa.soa;

import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eu.cloud4soa.repository.utils.RepositoryManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.equivalence.EquivalenceRuleHWCategoryInstance;
import eu.cloud4soa.api.repository.ApplicationProfilesRepository;

import java.util.UUID;
import javax.ws.rs.core.Response;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ModelManagerCtx.xml"})
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ModelManageTest {

    final Logger logger = LoggerFactory.getLogger(ModelManageTest.class);

    @Autowired
    private ModelManager modelManager;
    
    @Autowired
    private RepositoryManager repositoryManager;

    //	private UserManagementAndSecurityModule userMngt;
    private ApplicationInstance applicationInstance;
    UserInstance userInstance;
    //mock objects
    @Autowired
    private ApplicationProfilesRepository applicationProfilesRepository;
    
    @Before
    public void setUp() {
//        userMngt = new UserManagementAndSecurityModule();
        repositoryManager.beginTxOnSemRepo();
        applicationInstance = createApplicationInstance();
        userInstance = createTestUserInstance();
        //Get userInstance through authentication.
//		userInstance = userMngt.authenticateUser();
    }
    
    
    @After
    public void cleanUp() {
//            applicationProfilesRepository.cleanUp();
        repositoryManager.rollbackTxSemRepo();
    }


    @Test
    public void TestStoreApplicationProfile() {
        try {
            //Application instance create in setUp
            Assert.assertNull("Application already stored in ApplicationRepository", applicationProfilesRepository.getApplicationInstance(applicationInstance.getUriId()));
        } catch (RepositoryException ex) {
            Assert.fail("getApplicationInstance method has thrown an exception!");
        }
        try {
            modelManager.storeApplicationProfile(applicationInstance, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storeApplicationProfile method has thrown an exception!");
        }
        try {
            Assert.assertNotNull("Application is not stored in ApplicationRepository", applicationProfilesRepository.getApplicationInstance(applicationInstance.getUriId()));
        } catch (RepositoryException ex) {
            Assert.fail("getApplicationInstance method has thrown an exception!");
        }
    }

// also before    
    @Ignore
    @Test
    public void TestStoreTurtleApplicationProfile() {
        String stringModelProfile =
                "@prefix essential-metamodel: <http://www.enterprise-architecture.org/essential-metamodel.owl#> ."
                + "@prefix c4s-app-m: <http://www.cloud4soa.eu/v0.1/application-domain#> ."
                + "@prefix c4s-inf-m: <http://www.cloud4soa.eu/v0.1/infrastructural-domain#> ."
                + "@prefix dcterms: <http://purl.org/dc/terms/> ."
                + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."
                + "c4s-app-m:aaa1 a essential-metamodel:Application; "
                + "dcterms:title \"C4Sv1.0\"^^xsd:string ; "
                + "dcterms:alternative \"c4sApp\" ; "
                + "essential-metamodel:description \"new c4s test application\" ; "
                + "c4s-app-m:application_code \"c4s00001\"^^xsd:string ; "
                + "dcterms:hasVersion \"1.0\"^^xsd:string . ";
        //Application instance create in setUp
//            Assert.assertFalse("Application already stored in ApplicationRepository", applicationProfilesRepository.hasApplicationProfile(applicationInstance, userInstance.getUriId()));
        try {
            modelManager.storeTurtleApplicationProfile(stringModelProfile, "http://www.cloud4soa.eu/v0.1/user-model#Developer/0910e8cf-6031-4d33-999e-7c0583c45257");
        } catch (SOAException ex) {
            Assert.fail("storeTurtleApplicationProfile method has thrown an exception!");
        }
//            Assert.assertTrue("Application is not stored in ApplicationRepository", applicationProfilesRepository.hasApplicationProfile(applicationInstance, userInstance.getUriId()));
    }

    
    @Test
    public void TestUpdateApplicationProfile() {
        try {
            modelManager.storeApplicationProfile(applicationInstance, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storeApplicationProfile method has thrown an exception!");
        }
        try {
            Assert.assertTrue("Application has a different version", applicationProfilesRepository.getApplicationInstance(applicationInstance.getUriId()).getVersion().equalsIgnoreCase("1.0"));
        } catch (RepositoryException ex) {
            Assert.fail("getApplicationInstance method has thrown an exception!");
        }
        applicationInstance.setVersion("2.0");
        try {
            modelManager.updateApplicationProfile(applicationInstance);
        } catch (SOAException ex) {
            Assert.fail("updateApplicationProfile method has thrown an exception!");
        }
        try {
            Assert.assertTrue("Application has a different version", applicationProfilesRepository.getApplicationInstance(applicationInstance.getUriId()).getVersion().equalsIgnoreCase("2.0"));
        } catch (RepositoryException ex) {
            Assert.fail("getApplicationInstance method has thrown an exception!");
        }
    }

    
    
    @Test
    public void TestRetrieveAllApplicationProfiles() {
        //Test create 2 application instances
        ApplicationInstance applicationInstance2 = createApplicationInstance2();
        try {
            modelManager.storeApplicationProfile(applicationInstance, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storeApplicationProfile method has thrown an exception!");
        }
        try {
            modelManager.storeApplicationProfile(applicationInstance2, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storeApplicationProfile method has thrown an exception!");
        }
        List<ApplicationInstance> applications = null;
        try {
            applications = modelManager.retrieveAllApplicationProfile(userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("retrieveAllApplicationProfile method has thrown an exception!");
        }
        Assert.assertEquals(
                "retrieved a different number of applicationProfiles,",
                2,
                applications.size());
        Assert.assertTrue("Application is not retrieved", applicationInstance.getUriId().equals(applications.get(0).getUriId()) || applicationInstance.getUriId().equals(applications.get(1).getUriId()));
        Assert.assertTrue("Application2 is not retrieved", applicationInstance2.getUriId().equals(applications.get(0).getUriId()) || applicationInstance2.getUriId().equals(applications.get(1).getUriId()));
    }

    
    
    @Test
    public void TestRemoveApplicationProfile() {
        //Store first
        try {
            modelManager.storeApplicationProfile(applicationInstance, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storeApplicationProfile method has thrown an exception!");
        }
        //try to remove
        try {
            modelManager.removeApplicationProfile(applicationInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("removeApplicationProfile method has thrown an exception!");
        }

        try {
            ApplicationInstance retrievedApplicationProfile = modelManager.retrieveApplicationProfile(applicationInstance.getUriId(), userInstance.getUriId());
            Assert.fail("Removed application is retrieved succesfully!");
        } catch (SOAException ex) {
            Assert.assertEquals("Different exception status", Response.Status.BAD_REQUEST, ex.getResponseStatus());
        }
    }

    
    
    @Test
    public void TestRemoveApplicationProfile2() {
        //Store first
        ApplicationInstance applicationInstance2 = createApplicationInstance2();
        try {
            modelManager.storeApplicationProfile(applicationInstance, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storeApplicationProfile method has thrown an exception!");
        }
        try {
            modelManager.storeApplicationProfile(applicationInstance2, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storeApplicationProfile method has thrown an exception!");
        }
        try {
            //try to remove
            modelManager.removeApplicationProfile(applicationInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("removeApplicationProfile method has thrown an exception!");
        }

        try {
            ApplicationInstance retrievedApplicationProfile = modelManager.retrieveApplicationProfile(applicationInstance.getUriId(), userInstance.getUriId());
            Assert.fail("Removed application is retrieved succesfully!");
        } catch (SOAException ex) {
            Assert.assertEquals("Different exception status", Response.Status.BAD_REQUEST, ex.getResponseStatus());
        }

    }

    @Test
    public void TestStoreEquivalenceRule_justName() {
        String source;
        String target;
        EquivalenceRuleHWCategoryInstance ruleToAdd;
        
        logger.info("%@%@%@@%@%@@%@%@%@%@@%@%@%@%@   TestStoreEquivalenceRule_justName    %@%@%@%%@%@@%@%@%@%@% " + repositoryManager.getManager().isAutocommit() +  " - transaction support " + repositoryManager.checkAutocommit() );
//        repositoryManager.begin();
        source = "Clone";
        target = "ECU";
        ruleToAdd = createEquivalenceRule( source, target, 1f );
        try {
            modelManager.addEquivalenceRule( ruleToAdd );
        } catch (SOAException ex) {
            logger.error("Error in storing equivalence rule: ", ex);
            Assert.fail("Impossible to store the EquivalenceRule: " + ex.getMessage());
        }
        logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^  TestStoreEquivalenceRule_justName finished ^^^^^^^^^^^^^^^^^^");
        logger.info( " <><><><> aborting");
//        repositoryManager.rollback();
    }

    @Transactional
    @Test
    public void TestStoreEquivalenceRule_fullUris() {
        String source;
        String target;
        EquivalenceRuleHWCategoryInstance ruleToAdd;
        
        logger.info("%@%@%@@%@%@@%@%@%@%@@%@%@%@%@   TestStoreEquivalenceRule_fullUris    %@%@%@%%@%@@%@%@%@%@%" + repositoryManager.getManager().isAutocommit() +  " - transaction support " + repositoryManager.checkAutocommit());
        source = "http://www.cloud4soa.eu/v0.1/infrastructural-domain#Clone";
        target = "http://www.cloud4soa.eu/v0.1/infrastructural-domain#ECU";
        ruleToAdd = createEquivalenceRule( source, target, 1f );
        try {
            modelManager.addEquivalenceRule( ruleToAdd );
        } catch (SOAException ex) {
            logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~  TestStoreEquivalenceRule_fullUris ERROR ~~~~~~~~~~~~~~~~~~~~");
            Assert.fail("Impossible to store the EquivalenceRule: " + ex.getMessage());
        }
        logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^  TestStoreEquivalenceRule_fullUris finished ^^^^^^^^^^^^^^^^^^");
        logger.info( " <><><><> aborting");
//        repositoryManager.rollback();
    }
        
        
    @Test
    public void TestStoreEquivalenceRule2() {
        EquivalenceRuleHWCategoryInstance erhc = new EquivalenceRuleHWCategoryInstance();
        erhc.setHasSource("http://www.cloud4soa.eu/v0.1/infrastructural-domain#Clone");
        erhc.setHasTarget("http://www.cloud4soa.eu/v0.1/infrastructural-domain#ECU");
        erhc.setHasConversionRate(1f);
        try {
            modelManager.addEquivalenceRule(erhc);
        } catch (SOAException ex) {
            Assert.fail("Impossible to store the EquivalenceRule: " + ex.getMessage());
        }
        
        erhc = new EquivalenceRuleHWCategoryInstance();
        erhc.setHasSource("http://www.cloud4soa.eu/v0.1/infrastructural-domain#Clone");
        erhc.setHasTarget("http://www.cloud4soa.eu/v0.1/infrastructural-domain#ECU");
        erhc.setHasConversionRate(1f);
        try {
            modelManager.addEquivalenceRule(erhc);
            Assert.fail("The model manager should not permit to store an EquivalenceRule havin an UriId that already exists!");
        } catch (SOAException ex) {
            Assert.assertEquals("The thrown exception is not the expected one!", "An EquivalenceRuleHardwareCategory with the same UriId already exists", ex.getMessage());
        }
        
        erhc = new EquivalenceRuleHWCategoryInstance();
        erhc.setEquivalenceRuleHWCategoryUriId("eqRuleCloneToECU");
        erhc.setHasSource("http://www.cloud4soa.eu/v0.1/infrastructural-domain#Clone");
        erhc.setHasTarget("http://www.cloud4soa.eu/v0.1/infrastructural-domain#ECU");
        erhc.setHasConversionRate(1f);
        try {
            modelManager.addEquivalenceRule(erhc);
            Assert.fail("The model manager should not permit to store an already existent EquivalenceRule!");
        } catch (SOAException ex) {
            Assert.assertEquals("The thrown exception is not the expected one!", "An EquivalenceRuleHardwareCategory with the same Source and Target already exists", ex.getMessage());
        }

    }

    private ApplicationInstance createApplicationInstance() {
        ApplicationInstance ai = new ApplicationInstance();
        ai.setAcronym("C4S");
        ai.setApplicationcode("C4Sv1.0");
        ai.setDigest("C4S_Digest");
        ai.setProgramminglanguage("Java");
        ai.setProgramminglanguageVersion("1.6");
        ai.setSizeQuantity(45234567f);
//		ai.setUriId("http://www.cloud4soa.eu/software/C4S_v1.0/");
        ai.setVersion("1.0");
        return ai;
    }

    private ApplicationInstance createApplicationInstance2() {
        ApplicationInstance ai = new ApplicationInstance();
        ai.setAcronym("C4S2");
        ai.setApplicationcode("C4S2v1.0");
        ai.setDigest("C4S_Digest");
        ai.setProgramminglanguage("Java");
        ai.setProgramminglanguageVersion("1.6");
        ai.setSizeQuantity(45234567f);
//		ai.setUriId("http://www.cloud4soa.eu/software/C4S_v1.0/");
        ai.setVersion("1.0");
        return ai;
    }

    private UserInstance createTestUserInstance() {
        UserInstance userInstance = new UserInstance();
        userInstance.setUriId("yosuId");
        userInstance.setFirstName("Yosu");
        userInstance.setFamilyname("Gorroñogoitia");
        userInstance.setAccountname("yosu");
        userInstance.setGeekcode("yosu");
        userInstance.setSurname("Gorroñogoitia");
        Calendar calendar = Calendar.getInstance(Locale.ITALY);
        calendar.set(1967, 3, 11);
        userInstance.setBirthday(calendar.getTime());
        userInstance.setCloud4SoaAccountUriId("yosu");
        userInstance.setAccountname("yosu");
//		cloud4SoaAccount.setUriId("http:www.cloud4soa.eu/yosu#");
        return userInstance;
    }
    
    
    private EquivalenceRuleHWCategoryInstance createEquivalenceRule( String source, String target, float conversionFactor ) {
        EquivalenceRuleHWCategoryInstance erhc = new EquivalenceRuleHWCategoryInstance();
        
        erhc = new EquivalenceRuleHWCategoryInstance();    
        erhc.setEquivalenceRuleHWCategoryUriId( UUID.randomUUID().toString() );
        erhc.setHasSource( source );
        erhc.setHasTarget( target );
        erhc.setHasConversionRate( conversionFactor);
        
        return erhc;
    }            
}
