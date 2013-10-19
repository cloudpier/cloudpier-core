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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.Cloud4SoaAccountInstance;
import org.junit.Ignore;

public class AnnouncementModuleTest {

    private AnnouncementModule announcementModule;
    private UserManagementAndSecurityModule userMngt;
    PaaSInstance paaSInstance;
    UserInstance userInstance;

    @Before
    public void setUp() {
        announcementModule = new AnnouncementModule();
        userMngt = new UserManagementAndSecurityModule();
        userInstance = createTestUserInstance();
        paaSInstance = createTestPaasInstance();
        //Get userInstance through authentication.
//		userInstance = userMngt.authenticateUser();
    }

    @Ignore
    @Test
    public void TestStorePaaSInstance() {
        try {
            //Application instance create in setUp
            announcementModule.storePaaSInstance(paaSInstance, userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("storePaaSInstance method has thrown an exception!");
        }
    }

    @Ignore
    @Test
    public void TestUpdatePaaSInstance() {
        paaSInstance.setSupportedProgrammingLanguage("Python");
        try {
            announcementModule.updatePaaSInstance(paaSInstance);
        } catch (SOAException ex) {
            Assert.fail("updatePaaSInstance method has thrown an exception!");
        }
    }

    @Ignore
    @Test
    public void TestRetrieveAllApplicationProfiles() {
        //Test create application instances in another test, so we assume here they are available.
        List<PaaSInstance> applications = null;
        try {
            applications = announcementModule.retrieveAllPaaSInstances(userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("retrieveAllPaaSInstances method has thrown an exception!");
        }
        Assert.assertTrue(!applications.isEmpty());
    }

    @Ignore
    @Test
    public void TestRemovePaaSInstance() {
        try {
            announcementModule.removePaaSInstance(paaSInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("removePaaSInstance method has thrown an exception!");
        }
        List<PaaSInstance> applications = null;
        try {
            applications = announcementModule.retrieveAllPaaSInstances(userInstance.getUriId());
        } catch (SOAException ex) {
            Assert.fail("retrieveAllPaaSInstances method has thrown an exception!");
        }
        Assert.assertTrue(applications.isEmpty());
    }

    @After
    public void cleanUp() {
    }

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
//		userInstance.setCloud4SoaAccountUriId("http:www.cloud4soa.eu/yosu#");
        Cloud4SoaAccountInstance cloud4SoaAccountInstance = new Cloud4SoaAccountInstance();
        cloud4SoaAccountInstance.setAccountname("yosu");
//		cloud4SoaAccount.setUriId("http:www.cloud4soa.eu/yosu#");
        userInstance.setHoldsaccount(cloud4SoaAccountInstance);
        return userInstance;
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
