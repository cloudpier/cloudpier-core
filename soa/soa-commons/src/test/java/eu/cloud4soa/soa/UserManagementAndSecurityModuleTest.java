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

import eu.cloud4soa.repository.utils.RepositoryManager;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.soa.UserPaaSCredentials;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSProviderInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSUserInstance;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.relational.persistence.UsertypeRepository;
import eu.cloud4soa.soa.exceptions.ResourceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:UserManagementCtx.xml"})
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserManagementAndSecurityModuleTest {

    final Logger logger = LoggerFactory.getLogger(UserManagementAndSecurityModuleTest.class);
    @Autowired
    private UserManagementAndSecurityModule userManagementAndSecurityModule;
    @Autowired
    private AnnouncementModule announcementModule;
    @Autowired
    private UsertypeRepository userTypeRepository;
    @Autowired
    private RepositoryManager repositoryManager;
    private UserInstance userInstance;
    private String userId;

    @Before
    public void setUp() {
        repositoryManager.beginTxOnSemRepo();
        userInstance = createTestUserInstance();
        String username = "yosu";
        String password = "yosuPassword";
        //Get userInstance through authentication.
        Response resp;
        try {
            resp = userManagementAndSecurityModule.createNewAccount(userInstance, username, password);
            userId = (String) resp.getEntity();
        } catch (SOAException ex) {
            Assert.fail("createNewAccount has thrown an exception: " + ex.getMessage());
        }
        
    }
    
    
    @Test
    public void testUserRepositoryContent() {
        assertTrue( "Usertype table should contain at least 2 elements", userTypeRepository.findAll().size() >= 2 );
    }

    @Ignore
    @Test
    public void TestAuthenticateUser() {
        try {
            //AuthenticateUser input to be determined in SOA interface
            userManagementAndSecurityModule.authenticateUser("username", "password");
        } catch (SOAException ex) {
            Assert.fail("authenticateUser method has thrown an exception!");
        }
    }

    @Test
    public void TestGetUserInstance() {
        UserInstance retrivedUserInstance = null;
        try {
            retrivedUserInstance = userManagementAndSecurityModule.getUserInstance(userId);
        } catch (SOAException ex) {
            Assert.fail("getUserInstance method has thrown an exception!");
        }
        Assert.assertEquals("retrieve UriId: ", userInstance.getUriId(), retrivedUserInstance.getUriId());
        Assert.assertEquals("retrieve Accountname", userInstance.getAccountname(), retrivedUserInstance.getAccountname());
        Assert.assertEquals("retrieve FirstName", userInstance.getFirstName(), retrivedUserInstance.getFirstName());
        Assert.assertEquals("retrieve Surname", userInstance.getSurname(), retrivedUserInstance.getSurname());
        Assert.assertEquals("retrieve Geekcode", userInstance.getGeekcode(), retrivedUserInstance.getGeekcode());
        Assert.assertEquals("retrieve Cloud4SoaAccountUriId", userInstance.getCloud4SoaAccountUriId(), retrivedUserInstance.getCloud4SoaAccountUriId());
    }

    @Test
    public void TestGetUserInstance1() {
        try {
            UserInstance retrivedUserInstance = userManagementAndSecurityModule.getUserInstance(userId);
        } catch (ResourceException e) {
            Assert.fail("userManagementAndSecurityModule in not able to retrieve an existing user: " + userId);
        } catch (SOAException ex) {
            Assert.fail("getUserInstance method has thrown an exception!");
        }
        try {
            UserInstance retrivedUserInstance = userManagementAndSecurityModule.getUserInstance("yosu1");
            Assert.fail("userManagementAndSecurityModule is able to retrieve a non existing user: " + userId);
        } catch (ResourceException e) {
            Response resp = e.getResponse();
            Status.fromStatusCode(resp.getStatus()).equals(Status.BAD_REQUEST);
            Assert.assertTrue("Different exception code", Status.fromStatusCode(resp.getStatus()).equals(Status.BAD_REQUEST));
        } catch (SOAException ex) {
            Assert.fail("getUserInstance method has thrown an exception!");
        }
    }

    @Test
    public void TestIsAccountNameInUse() {
        UserInstance retrivedUserInstance = null;
        try {
            retrivedUserInstance = userManagementAndSecurityModule.getUserInstance(userId);
        } catch (ResourceException e) {
            Assert.fail("userManagementAndSecurityModule in not able to retrieve an existing user: " + userId);
        } catch (SOAException ex) {
            Assert.fail("getUserInstance method has thrown an exception!");
        }
        try {
            boolean accountNameInUse = userManagementAndSecurityModule.isAccountNameInUse(retrivedUserInstance.getAccountname());
            if (accountNameInUse == false) {
                Assert.fail("userManagementAndSecurityModule has not found the existing account name: " + retrivedUserInstance.getAccountname());
            }
            String nonExistingAccountName = "whatever";
            accountNameInUse = userManagementAndSecurityModule.isAccountNameInUse(nonExistingAccountName);
            if (accountNameInUse == true) {
                Assert.fail("userManagementAndSecurityModule has not found the existing account name: " + nonExistingAccountName);
            }
        } catch (ResourceException e) {
            Response resp = e.getResponse();
            Status.fromStatusCode(resp.getStatus()).equals(Status.BAD_REQUEST);
            Assert.assertTrue("Different exception code", Status.fromStatusCode(resp.getStatus()).equals(Status.BAD_REQUEST));
        } catch (SOAException ex) {
            Assert.fail("userManagementAndSecurityModule is not able to check if the account name already exists: " + ex.getMessage());
        }
    }
    
    @Test
    @Transactional
    public void TestStoreCredentials() {
        //CLOUDBEES keys
        String api_key = "4184E8A5D19D02D9";
        String api_secret = "UZPYSQVJMQLVNNVK6GSZQPRUTAZ+QKNB9QCKDWVNQMK=";
        String accountName = "testaccount";
        String paaSInstanceUriId = null;
        //Application instance create in setUp
        PaaSInstance paaSInstance = createTestPaasInstance();
        UserInstance paaSUserInstance = createTestPaaSUserInstance();
        String paaSUserId = storePaaSUser(paaSUserInstance);
        try {
            paaSInstanceUriId = announcementModule.storePaaSInstance(paaSInstance, paaSUserId);
        } catch (SOAException ex) {
            Assert.fail("storePaaSInstance method has thrown an exception!");
        }
        
        List<UserPaaSCredentials> listUserCredentialsForPaaS = null;

        try {
            listUserCredentialsForPaaS = userManagementAndSecurityModule.readAllUserCredentialsForPaaS(userId);
            Assert.assertTrue("Credentials list is not empty!", listUserCredentialsForPaaS.isEmpty());
            
            //Testing STORE Credentials
            userManagementAndSecurityModule.storeUserCredentialsForPaaS(userId, paaSInstanceUriId, api_key, api_secret, accountName);
            
            //Testing READ ALL Credentials
            listUserCredentialsForPaaS = userManagementAndSecurityModule.readAllUserCredentialsForPaaS(userId);
            Assert.assertTrue("Credentials list size is not 1!", listUserCredentialsForPaaS.size()==1);
            UserPaaSCredentials userPaaSCredentials = listUserCredentialsForPaaS.get(0);
            String credentialsUserInstanceUriId = userPaaSCredentials.getUserInstanceUriId();
            String credentialsPaaSInstanceUriId = userPaaSCredentials.getPaaSInstanceUriId();
            String credentialsPublicKey = userPaaSCredentials.getPublicKey();
            String credentialsSecretKey = userPaaSCredentials.getSecretKey();
            String credentialsAccountName = userPaaSCredentials.getAccountName();
            Assert.assertEquals("retrieved credential userInstanceUriId is not the expected one", userId, credentialsUserInstanceUriId);
//            Assert.assertEquals("retrieved credential paaSInstanceUriId is not the expected one", paaSInstanceUriId, credentialsPaaSInstanceUriId);
            Assert.assertEquals("retrieved credential publicKey is not the expected one", api_key, credentialsPublicKey);
            Assert.assertEquals("retrieved credential secretKey is not the expected one", api_secret, credentialsSecretKey);
            Assert.assertEquals("retrieved credential accountName is not the expected one", accountName, credentialsAccountName);
            
            //Testing READ ONE Credentials
            UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(userId, paaSInstanceUriId);
            Assert.assertNotNull("Credentials object is null!", userCredentialsForPaaS);
            credentialsUserInstanceUriId = userCredentialsForPaaS.getUserInstanceUriId();
            credentialsPaaSInstanceUriId = userCredentialsForPaaS.getPaaSInstanceUriId();
            credentialsPublicKey = userCredentialsForPaaS.getPublicKey();
            credentialsSecretKey = userCredentialsForPaaS.getSecretKey();
            credentialsAccountName = userCredentialsForPaaS.getAccountName();
            Assert.assertEquals("retrieved credential userInstanceUriId is not the expected one", userId, credentialsUserInstanceUriId);
//            Assert.assertEquals("retrieved credential paaSInstanceUriId is not the expected one", paaSInstanceUriId, credentialsPaaSInstanceUriId);
            Assert.assertEquals("retrieved credential publicKey is not the expected one", api_key, credentialsPublicKey);
            Assert.assertEquals("retrieved credential secretKey is not the expected one", api_secret, credentialsSecretKey);
            Assert.assertEquals("retrieved credential accountName is not the expected one", accountName, credentialsAccountName);

/*            
            //Testing UPDATE Credentials
            api_key = "changedPublicKey";
            api_secret = "changedSecretKey";
            accountName = "accountNameChanged";
            userManagementAndSecurityModule.updateUserCredentialsForPaaS(userId, paaSInstanceUriId, api_key, api_secret, accountName);
            userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(userId, paaSInstanceUriId);
            Assert.assertNotNull("Credentials object is null!", userCredentialsForPaaS);
            credentialsUserInstanceUriId = userCredentialsForPaaS.getUserInstanceUriId();
            credentialsPaaSInstanceUriId = userCredentialsForPaaS.getPaaSInstanceUriId();
            credentialsPublicKey = userCredentialsForPaaS.getPublicKey();
            credentialsSecretKey = userCredentialsForPaaS.getSecretKey();
            credentialsAccountName = userCredentialsForPaaS.getAccountName();
            Assert.assertEquals("retrieved credential userInstanceUriId is not the expected one", userId, credentialsUserInstanceUriId);
//            Assert.assertEquals("retrieved credential paaSInstanceUriId is not the expected one", paaSInstanceUriId, credentialsPaaSInstanceUriId);
            Assert.assertEquals("retrieved credential publicKey is not the expected one", api_key, credentialsPublicKey);
            Assert.assertEquals("retrieved credential secretKey is not the expected one", api_secret, credentialsSecretKey);
            Assert.assertEquals("retrieved credential accountName is not the expected one", accountName, credentialsAccountName);
*/
            
            //Testing REMOVE Credentials
            userManagementAndSecurityModule.removeUserCredentialsForPaaS(userId, paaSInstanceUriId);
            listUserCredentialsForPaaS = userManagementAndSecurityModule.readAllUserCredentialsForPaaS(userId);
            Assert.assertTrue("Credentials list is not empty!", listUserCredentialsForPaaS.isEmpty());
            
        } catch (ResourceException e) {
            Assert.fail("userManagementAndSecurityModule in not able to retrieve an existing user: " + userId);
        } catch (SOAException ex) {
            Assert.fail("Exception: "+ex.getMessage());
        }
    }

    @Ignore
    @Test
    public void TestStoreUserInstance() {
        //Commented since current SOA interface use frontend UserInstance
//		userMngt.storeUserInstance(userInstance); 
    }

    @After
    public void cleanUp() {
        repositoryManager.rollbackTxSemRepo();
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
    
    private UserInstance createTestPaaSUserInstance() {
        PaaSUserInstance userInstance = new PaaSUserInstance();
        userInstance.setFirstName("CloudControl");
        userInstance.setFamilyname("CloudControl");
        userInstance.setAccountname("CloudControl");
        userInstance.setGeekcode("CloudControl");
        userInstance.setSurname("CloudControl");
        Calendar calendar = Calendar.getInstance(Locale.ITALY);
        calendar.set(1967, 3, 11);
        userInstance.setBirthday(calendar.getTime());
        PaaSProviderInstance paaSProviderInstance = new PaaSProviderInstance("CloudControl", "http://cloudcontrol.com");
        userInstance.setPaaSProviderInstance(paaSProviderInstance);
        return userInstance;
    }
    
    private String storePaaSUser(UserInstance userInstance){
        String username = "cc";
        String password = "cc";
        //Get userInstance through authentication.
        Response resp;
        String userId = null;
        try {
            resp = userManagementAndSecurityModule.createNewAccount(userInstance, username, password);
            userId = (String) resp.getEntity();
        } catch (SOAException ex) {
            Assert.fail("createNewAccount has thrown an exception: " + ex.getMessage());
        }
        return userId;
    }
}
