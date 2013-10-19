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

import eu.cloud4soa.api.datamodel.core.MatchingPlatform;
import eu.cloud4soa.api.datamodel.core.utilBeans.HardwareComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.OperationInstance;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.PaaSProviderDetails;
import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.equivalence.EquivalenceRuleHWCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.CLIInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ChannelType;
import eu.cloud4soa.api.datamodel.core.utilBeans.ComputationalCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ComputeInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ExceptionInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.HardwareCategoryType;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSProviderInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSUserInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.ParameterInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareCategoryInstance;
import eu.cloud4soa.api.repository.PaaSOfferingProfilesRepository;
import eu.cloud4soa.repository.utils.RepositoryManager;
import eu.cloud4soa.soa.utils.SemanticInitializer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;

//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:PaaSOfferingDiscoveryCtx.xml"})
public class PaaSOfferingDiscoveryTest {

    final Logger logger = LoggerFactory.getLogger(getClass());
    private PaaSInstance paaSInstance;
    private PaaSUserInstance userInstance;
    private String userInstanceId;
    
    @Autowired
    private UserManagementAndSecurityModule userManagementAndSecurityModule;
    @Autowired
    private AnnouncementModule announcementModule;
    @Autowired
    private PaaSOfferingDiscovery paaSOfferingDiscovery;
    @Autowired 
    private ModelManager modelManager;
    @Autowired
    private SemanticInitializer semanticInitializer;  
    @Autowired
    private PaaSOfferingProfilesRepository paaSOfferingProfilesRepository;
    @Autowired
    private RepositoryManager repositoryManager;

/*    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }
     * 
     */
    
    


    
    
    @Before
    public void setUp() throws FileNotFoundException {
//        userInstance = createPaaSUserInstance();
//        String username = "cloudcontrol";
//        String password = "cloudcontrolPassword";
//        //Get userInstance through authentication.
//        Response resp;
//        try {
//            resp = userManagementAndSecurityModule.createNewAccount(userInstance, username, password);                
//            userInstanceId = (String)resp.getEntity();
//        } catch (SOAException ex) {
//            Assert.fail("createNewAccount has thrown an exception: "+ ex.getMessage());
//        }
        
        repositoryManager.beginTxOnSemRepo();
    }

    
    @Ignore 
    @Test
//    @DirtiesContext
    public void TestSearchForMatchingPlatform_Compute_WithEqRules() {
        String appUriId = "AppComputePowerFactor.AppComputePowerFactor";
        try {
            semanticInitializer.initialize();
            MatchingPlatform searchForMatchingPlatform = paaSOfferingDiscovery.searchForMatchingPlatform(appUriId);
            List<PaaSInstance> listPaaSInstance = searchForMatchingPlatform.getListPaaSInstance();
            Assert.assertTrue(!listPaaSInstance.isEmpty());
            int numberOfMatchingPlatforms = listPaaSInstance.size();
            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 2, numberOfMatchingPlatforms);
            //Adding an equivalence rule ECU = 1 Clone
            EquivalenceRuleHWCategoryInstance erhc = new EquivalenceRuleHWCategoryInstance();
            erhc.setHasSource("ECU");
            erhc.setHasTarget("Clone");
            erhc.setHasConversionRate(0.5f);
            modelManager.addEquivalenceRule(erhc);
            //Checking the new marching platforms list
            searchForMatchingPlatform = paaSOfferingDiscovery.searchForMatchingPlatform(appUriId);
            listPaaSInstance = searchForMatchingPlatform.getListPaaSInstance();
            Assert.assertTrue(!listPaaSInstance.isEmpty());
            numberOfMatchingPlatforms = listPaaSInstance.size();
            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 3, numberOfMatchingPlatforms);
            //Adding an equivalence rule Clone = 1 AppCell
            erhc = new EquivalenceRuleHWCategoryInstance();
            erhc.setHasSource("Clone");
            erhc.setHasTarget("AppCell");
            erhc.setHasConversionRate(1f);
            modelManager.addEquivalenceRule(erhc);
            //Checking the new marching platforms list
            searchForMatchingPlatform = paaSOfferingDiscovery.searchForMatchingPlatform(appUriId);
            listPaaSInstance = searchForMatchingPlatform.getListPaaSInstance();
            Assert.assertTrue(!listPaaSInstance.isEmpty());
            numberOfMatchingPlatforms = listPaaSInstance.size();
            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 4, numberOfMatchingPlatforms);
//            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 3, numberOfMatchingPlatforms);
        } catch (SOAException ex) {
            logger.error(ex.getMessage());
            Assert.fail("Repository initialization failed: " + ex.getMessage());
        }
    }

    @Ignore
    @Test
//    @DirtiesContext
    public void TestSearchForMatchingPlatform_HttpRequestsHandler_WithEqRules() {
        String appUriId = "AppBox.AppBox";
        try {
            semanticInitializer.initialize();
            MatchingPlatform searchForMatchingPlatform = paaSOfferingDiscovery.searchForMatchingPlatform(appUriId);
            List<PaaSInstance> listPaaSInstance = searchForMatchingPlatform.getListPaaSInstance();
            Assert.assertTrue(!listPaaSInstance.isEmpty());
            int numberOfMatchingPlatforms = listPaaSInstance.size();
            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 1, numberOfMatchingPlatforms);
            //Adding an equivalence rule ECU = 1 Clone
            EquivalenceRuleHWCategoryInstance erhc = new EquivalenceRuleHWCategoryInstance();
            erhc.setHasSource("ECU");
            erhc.setHasTarget("Clone");
            erhc.setHasConversionRate(0.5f);
            modelManager.addEquivalenceRule(erhc);
            //Checking the new marching platforms list
            searchForMatchingPlatform = paaSOfferingDiscovery.searchForMatchingPlatform(appUriId);
            listPaaSInstance = searchForMatchingPlatform.getListPaaSInstance();
            Assert.assertTrue(!listPaaSInstance.isEmpty());
            numberOfMatchingPlatforms = listPaaSInstance.size();
            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 1, numberOfMatchingPlatforms);
            //Adding an equivalence rule Clone = 1 AppCell
            erhc = new EquivalenceRuleHWCategoryInstance();
            erhc.setHasSource("Clone");
            erhc.setHasTarget("AppCell");
            erhc.setHasConversionRate(1f);
            modelManager.addEquivalenceRule(erhc);
            //Checking the new marching platforms list
            searchForMatchingPlatform = paaSOfferingDiscovery.searchForMatchingPlatform(appUriId);
            listPaaSInstance = searchForMatchingPlatform.getListPaaSInstance();
            Assert.assertTrue(!listPaaSInstance.isEmpty());
            numberOfMatchingPlatforms = listPaaSInstance.size();
            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 1, numberOfMatchingPlatforms);
//            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 3, numberOfMatchingPlatforms);
        } catch (SOAException ex) {
            logger.error(ex.getMessage());
            Assert.fail("Repository initialization failed: " + ex.getMessage());
        }
    }

  //  @Ignore
    @Test
//    @DirtiesContext
    public void TestSearchForMatchingPlatform_Ranking() {
        String appUriId = "AppPreferencesRanking.AppPreferencesRanking";
        try {
            semanticInitializer.initialize();
            MatchingPlatform searchForMatchingPlatform = paaSOfferingDiscovery.searchForMatchingPlatform(appUriId);
            Map<PaaSInstance, Float> rankedListPaaSInstance = searchForMatchingPlatform.getRankedListPaaSInstances();
            Assert.assertTrue(!rankedListPaaSInstance.isEmpty());
            int numberOfMatchingPlatforms = rankedListPaaSInstance.size();
            Assert.assertEquals("The number Of Matching Platforms is not the expected one!", 8, numberOfMatchingPlatforms);
           
            for (PaaSInstance pInst : rankedListPaaSInstance.keySet()) {
                String paasURIid = pInst.getUriId();
                float paasRanking = rankedListPaaSInstance.get(pInst);
                if (paasURIid.equals("cloudBees_runAtCloud")) {
                    Assert.assertEquals("The ranking Of Matchingng Platform Cloudbees is not the expected one!", 0.5, paasRanking, 0.0);
                } else if (paasURIid.equals("Heroku")) {
                    Assert.assertEquals("The ranking Of Matchingng Platform Heroku is not the expected one!", 0.375, paasRanking, 0.0);
                } else if (paasURIid.equals("amazon_beanstalk")) {
                   Assert.assertEquals("The ranking Of Matchingng Platform amazon_beanstalk is not the expected one!", 0.75, paasRanking, 0.0);
                } else if (paasURIid.equals("MyTestPaaS")) {
                    Assert.assertEquals("The ranking Of Matchingng Platform MyTestPaaS is not the expected one!", 1.0, paasRanking, 0.0);
                } else if (paasURIid.equals("googleAppEngine")) {
                    Assert.assertEquals("The ranking Of Matchingng Platform googleAppEngine is not the expected one!", 0.375, paasRanking, 0.0);
                } else if (paasURIid.equals("CumuLogic")) {
                    Assert.assertEquals("The ranking Of Matchingng Platform CumuLogic is not the expected one!", 0.0, paasRanking, 0.0);
                }
            }

        } catch (SOAException ex) {
            logger.error(ex.getMessage());
            Assert.fail("Repository initialization failed: " + ex.getMessage());
        }
    }

    @Ignore
    @Test
    public void TestGetPaaSProviderDetails() {
        PaaSProviderDetails details = paaSOfferingDiscovery.getPaaSProviderDetails(paaSInstance.getUriId());
        Assert.assertNotNull(details);
    }

    @Ignore
    @Test
    public void TestQuery() {
        //Query requires to be amended.
        String sparql = "select ?p where ?p rdf.type c4s.PaaSInstance";
        String result = paaSOfferingDiscovery.query(sparql);
        //TODO Get first returned PaaSInstance uri;
//		paaSInstanceURI = null;
        Assert.assertTrue(result != null && !result.isEmpty());
    }

    @Ignore
    @Test
    public void TestGetPaaSInstanceByURI() {
        PaaSInstance instance = null;
        try {
            instance = announcementModule.getPaaSInstance(paaSInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("getPaaSInstance method has thrown an exception!");
        }
        Assert.assertNotNull(instance);
    }

    @Test
//    @DirtiesContext
    public void TestCreatePaasInstance() {
        userInstance = createPaaSUserInstance();
        String username = "cloudcontrol";
        String password = "cloudcontrolPassword";
        //Get userInstance through authentication.
        Response resp;
        try {
            resp = userManagementAndSecurityModule.createNewAccount(userInstance, username, password);
            userInstanceId = (String) resp.getEntity();
        } catch (SOAException ex) {
            Assert.fail("createNewAccount has thrown an exception: " + ex.getMessage());
        }
        // Create PaaS Instance
        createPaaSInstance();
        try {
            // Store it
            announcementModule.storePaaSInstance(paaSInstance, userInstanceId);
        } catch (SOAException ex) {
            Assert.fail("storePaaSInstance method has thrown an exception!");
        }

        // Read all existing PaaS instances
        List<PaaSInstance> paasInstances = null;
        try {
            paasInstances = paaSOfferingDiscovery.getAllAvailablePaaSInstances();
        } catch (SOAException ex) {
            Assert.fail("getAllAvailablePaaSInstances method has thrown an exception!");
        }

        Assert.assertTrue(paasInstances != null && !paasInstances.isEmpty());
    }

    public void createPaaSInstance() {
//        List<PaaSInstance> providedPaaSInstances = new ArrayList<PaaSInstance>();
        paaSInstance = new PaaSInstance();

        paaSInstance.setTitle("CloudControl PHP");

        //Offering
        paaSInstance.setURL("http://cloudcontrol.com/home/");
        paaSInstance.setStatus("Available");
        CLIInstance cLIInstance = (CLIInstance) paaSInstance.createAndAddChannel(ChannelType.CLI);
        cLIInstance.setTitle("CLI");
        cLIInstance.setDescription("The command line client is a powerful interface to our API enabling developers to control all features of the cloudControl platform");
        cLIInstance.setVersion("1.0");
        cLIInstance.setURL("http://cloudcontrol.com/CLI");
        OperationInstance operationInstance = cLIInstance.createAndAddOperation("create", "create new application", "cctrlapp <app_name> create php", "");
        ParameterInstance parameterInstance = new ParameterInstance("app_name", "Application Name", false, "hellocc");
        operationInstance.addRequiredParameter(parameterInstance);
        ExceptionInstance exceptionInstance = new ExceptionInstance("APPLICATION_NOT_FOUND", "Application not found", "Uknown");
        operationInstance.addThrownException(exceptionInstance);
        HardwareComponentInstance hardwareComponent = paaSInstance.createAndAddHardwareComponent(HardwareCategoryType.HttpRequestHandlerCategory);
//      fill the hardwareComponent parameters
        ComputationalCategoryInstance boxInstance = (ComputationalCategoryInstance) hardwareComponent.getRelatedhwcategoryInstance();

        SoftwareCategoryInstance softwareCategoryInstance = new SoftwareCategoryInstance("RDBMS", "relational database");
        paaSInstance.createAndAddSoftwareComponent("MySQL", "MySQL is one of the most used relational database systems for web applications",
                "5.5.16", "GPL", softwareCategoryInstance);
        paaSInstance.setSupportedProgrammingLanguage("PHP");

        PaaSProviderInstance paaSProviderInstance = new PaaSProviderInstance("CloudControl", "http://cloudcontrol.com");

//        paaSUserInstance.setPaaSProviderInstance(paaSProviderInstance);
//        providedPaaSInstances.add(paaSInstance);         
    }

    private ApplicationInstance createApplication() {
        ApplicationInstance applicationInstance = new ApplicationInstance();
        applicationInstance.setAcronym("C4SFE");
        applicationInstance.setApplicationcode("WAR");
        applicationInstance.setDigest("b928fb9e52853eca082b9313ef6d2678");
        applicationInstance.setVersion("0.2");
        applicationInstance.setProgramminglanguage("Java");
        applicationInstance.setProgramminglanguageVersion("1.6");
        applicationInstance.setArchiveFileName("frontend-dashboard-0.0.1-SNAPSHOT.war");
        applicationInstance.setArchiveExtensionName("war");
        applicationInstance.setSizeQuantity(4.2f);
        //Required Hardware Components
        HardwareComponentInstance hardwareComponent = applicationInstance.createAndAddHardwareComponent(HardwareCategoryType.ComputationalCategory);
        ComputeInstance computeInstance = (ComputeInstance) hardwareComponent;
        computeInstance.setTitle("Dedicated Server");
        computeInstance.setArchitecture("32 bit");
        computeInstance.setMinMemoryValue(512f);
        computeInstance.setMinHasCores(1f);
//        computeInstance.setMinSpeedValue(2000f);
//        hardwareComponent.setRelatedhwcategoryInstance(computeInstance);
        //Required Software Components
        SoftwareCategoryInstance softwareCategoryInstance = new SoftwareCategoryInstance();
        softwareCategoryInstance.setTitle("Application server");
        applicationInstance.createAndAddSoftwareComponent("Tomcat", "Application Server", "7.0", "GPL", softwareCategoryInstance);

        return applicationInstance;
    }

    private DeveloperInstance createDeveloper(String firstName, String familyName, String geekCode) {
        DeveloperInstance developer = new DeveloperInstance();

        developer.setFirstName(firstName);
        developer.setFamilyname(familyName);
        developer.setGeekcode(geekCode);
        developer.setSurname(familyName);
        Calendar calendar = Calendar.getInstance(Locale.ITALY);
        calendar.set(1967, 3, 11);
        developer.setBirthday(calendar.getTime());

        //TODO: Complete

        return developer;
    }

    @After
    public void cleanUp() {
//        paaSOfferingProfilesRepository.cleanUp();
        repositoryManager.rollbackTxSemRepo();
    }

    private ApplicationInstance createApplicationInstance() {
        ApplicationInstance ai = new ApplicationInstance();
        ai.setAcronym("C4S");
        ai.setApplicationcode("C4Sv1.0");
        ai.setDigest("C4S_Digest");
        ai.setOwnerUriId("http://www.cloud4soa.eu/team");
        ai.setProgramminglanguage("Java");
        ai.setProgramminglanguageVersion("1.6");
        ai.setSizeQuantity(45234567f);
        ai.setUriId("http://www.cloud4soa.eu/software/C4S_v1.0/");
        ai.setVersion("1.0");
        return ai;
    }

    private String createTestUserInstance() {
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
//        userInstance.setCloud4SoaAccountUriId("yosu");
//        userInstance.setAccountname("yosu");
//		cloud4SoaAccount.setUriId("http:www.cloud4soa.eu/yosu#");
        String username = "yosu";
        String password = "yosuPassword";
        Response resp = null;
        try {
            resp = userManagementAndSecurityModule.createNewAccount(userInstance, username, password);
        } catch (SOAException ex) {
            Assert.fail("createNewAccount has thrown an exception: " + ex.getMessage());
        }
        String userId = (String) resp.getEntity();
        return userId;

    }

    private PaaSUserInstance createPaaSUserInstance() {
        PaaSUserInstance userInstance = new PaaSUserInstance();
        userInstance.setFirstName("Yosu");
        userInstance.setFamilyname("Gorroñogoitia");
        userInstance.setGeekcode("yosu");
        userInstance.setSurname("Gorroñogoitia");
        Calendar calendar = Calendar.getInstance(Locale.ITALY);
        calendar.set(1967, 3, 11);
        userInstance.setBirthday(calendar.getTime());
        //Assigned by the system
//		userInstance.setCloud4SoaAccountUriId("http:www.cloud4soa.eu/yosu#");
//        Cloud4SoaAccountInstance cloud4SoaAccountInstance = new Cloud4SoaAccountInstance();
//        cloud4SoaAccountInstance.setAccountname("cloudcontrol");
        //Assigned by the system
//		cloud4SoaAccount.setUriId("http:www.cloud4soa.eu/yosu#");
//        userInstance.setHoldsaccount(cloud4SoaAccountInstance);

        PaaSProviderInstance paaSProviderInstance = new PaaSProviderInstance("CloudControl", "http://cloudcontrol.com");

        userInstance.setPaaSProviderInstance(paaSProviderInstance);

        return userInstance;
    }
}
