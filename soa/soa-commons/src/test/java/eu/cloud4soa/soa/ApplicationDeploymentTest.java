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

import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.Cloud4SoaAccountInstance;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationDeploymentTest {

    final Logger logger = LoggerFactory.getLogger(ApplicationDeploymentTest.class);
    private ApplicationDeployment appDeployment;
    private String applicationInstanceUriId;
    private AnnouncementModule announcementModule;
    private UserInstance userInstance;
    private InputStream applicationWar;
    private PaaSInstance paaSInstance;
    private File warFile = new File("/path/to/war/file");

    @Before
    public void setUp() throws FileNotFoundException {
        appDeployment = new ApplicationDeployment();
        applicationInstanceUriId = createApplicationInstance();
        userInstance = createTestUserInstance();
        applicationWar = getApplicationWar();

        paaSInstance = createTestPaasInstance();

        announcementModule = new AnnouncementModule();
        //Get userInstance through authentication.
//		userInstance = userMngt.authenticateUser();
        try {
            announcementModule.storePaaSInstance(paaSInstance, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storePaaSInstance method has thrown an exception!");
        }
    }

    @Ignore
    @Test
    public void TestDeployApplication() {
        //Application instance create in setUp
        String publicKey = null;
        String secretKey = null;
        if (paaSInstance.getTitle().equals("Beanstalk")) {
            publicKey = "AKIAJRSZ7FBNKBAOUR6A";
            secretKey = "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";
        }
        if (paaSInstance.getTitle().equals("CloudBees")) {
            publicKey = "4184E8A5D19D02D9";
            secretKey = "UZPYSQVJMQLVNNVK6GSZQPRUTAZ+QKNB9QCKDWVNQMK=";
        }
//        try {
//            appDeployment.deployApplication(applicationInstanceUriId, paaSInstance.getUriId(), publicKey, secretKey, applicationWar);
//        } catch (SOAException ex) {
//            logger.error(ex.getMessage());
//        }
    }

    @Ignore
    @Test
    public void TestRetrieveAllDeployedApplicationProfiles() {
        //Test create application instances in another test, so we assume here they are available.
        List<ApplicationInstance> applications = null;
        try {
            applications = appDeployment.retrieveAllDeployedApplicationProfiles(userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storePaaSInstance method has thrown an exception!");
        }
        Assert.assertTrue(!applications.isEmpty());
    }

    @Ignore
    @Test
    public void TestStartApplication() {
        String publicKey = null;
        String secretKey = null;
        if (paaSInstance.getTitle().equals("Beanstalk")) {
            publicKey = "AKIAJRSZ7FBNKBAOUR6A";
            secretKey = "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";
        }
        if (paaSInstance.getTitle().equals("CloudBees")) {
            publicKey = "4184E8A5D19D02D9";
            secretKey = "UZPYSQVJMQLVNNVK6GSZQPRUTAZ+QKNB9QCKDWVNQMK=";
        }
//        try {
//            appDeployment.startStopApplication(applicationInstanceUriId, "start", publicKey, secretKey);
//        } catch (SOAException ex) {
//            logger.error(ex.getMessage());
//        }
    }

    @Ignore
    @Test
    public void TestStopApplication() {
        String publicKey = null;
        String secretKey = null;
        if (paaSInstance.getTitle().equals("Beanstalk")) {
            publicKey = "AKIAJRSZ7FBNKBAOUR6A";
            secretKey = "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";
        }
        if (paaSInstance.getTitle().equals("CloudBees")) {
            publicKey = "4184E8A5D19D02D9";
            secretKey = "UZPYSQVJMQLVNNVK6GSZQPRUTAZ+QKNB9QCKDWVNQMK=";
        }
//        try {
//            appDeployment.startStopApplication(applicationInstanceUriId, "stop", publicKey, secretKey);
//        } catch (SOAException ex) {
//            logger.error(ex.getMessage());
//        }
    }

    @Ignore
    @Test
    public void TestUndeployApplication() {
        String publicKey = null;
        String secretKey = null;
        if (paaSInstance.getTitle().equals("Beanstalk")) {
            publicKey = "AKIAJRSZ7FBNKBAOUR6A";
            secretKey = "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";
        }
        if (paaSInstance.getTitle().equals("CloudBees")) {
            publicKey = "4184E8A5D19D02D9";
            secretKey = "UZPYSQVJMQLVNNVK6GSZQPRUTAZ+QKNB9QCKDWVNQMK=";
        }
//        try {
//            appDeployment.removeApplication(applicationInstanceUriId, publicKey, secretKey);
//        } catch (SOAException ex) {
//            logger.error(ex.getMessage());
//        }
        List<ApplicationInstance> applications = null;
        try {
            applications = appDeployment.retrieveAllDeployedApplicationProfiles(userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("retrieveAllDeployedApplicationProfiles method has thrown an exception!");
        }
        Assert.assertTrue(applications.isEmpty());
    }

    @After
    public void cleanUp() {
        try {
            announcementModule.removePaaSInstance(paaSInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("removePaaSInstance method has thrown an exception!");
        }
    }

    private String createApplicationInstance() {
        return "http:www.cloud4soa.eu/yosu#";
    }
//	private ApplicationInstance createApplicationInstance() {
//		ApplicationInstance ai = new ApplicationInstance();
//		ai.setAcronym("C4S");
//		ai.setApplicationcode("C4Sv1.0");
//		ai.setDigest("C4S_Digest");
//		ai.setOwnerUriId("http://www.cloud4soa.eu/team");
//		ai.setProgramminglanguage("Java");
//		ai.setProgramminglanguageVersion("1.6");
//		ai.setSizeQuantity(45234567f);
//		ai.setUriId("http://www.cloud4soa.eu/software/C4S_v1.0/");
//		ai.setVersion(1.0f);
//		return ai;
//	}

    private UserInstance createTestUserInstance() {
        UserInstance userInstance = new UserInstance();
        userInstance.setFirstName("Yosu");
        userInstance.setFamilyname("Gorroñogoitia");
        userInstance.setAccountname("yosu");
        userInstance.setGeekcode("yosu");
        userInstance.setSurname("Gorroñogoitia");
        Calendar calendar = Calendar.getInstance(Locale.ITALY);
        calendar.set(1967, 3, 11);
        userInstance.setBirthday(calendar.getTime());
        userInstance.setCloud4SoaAccountUriId("http:www.cloud4soa.eu/yosu#");
        Cloud4SoaAccountInstance cloud4SoaAccountInstance = new Cloud4SoaAccountInstance();
        cloud4SoaAccountInstance.setAccountname("yosu");
//		cloud4SoaAccount.setUriId("http:www.cloud4soa.eu/yosu#");
        userInstance.setHoldsaccount(cloud4SoaAccountInstance);
        return userInstance;
    }

    private InputStream getApplicationWar() throws FileNotFoundException {
        return new FileInputStream(warFile);
    }

    private PaaSInstance createTestPaasInstance() {
        PaaSInstance paaSInstance = new PaaSInstance();
        paaSInstance.setTitle("CloudControl");
        paaSInstance.setSupportedProgrammingLanguage("PHP");
        paaSInstance.setProviderTitle("Cloud Control Inc.");
        paaSInstance.setStatus("Active");
        paaSInstance.setURL("http://cloudcontrol.com/");

        return paaSInstance;
    }
}
