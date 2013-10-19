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

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.StatusType;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;
import eu.cloud4soa.api.datamodel.semantic.user.PaaSUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author vins
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = {"classpath:ApplicationProfilesRepositoryCtx.xml"})
public class ApplicationProfilesRepositoryTest {
    @Autowired
    private ApplicationProfilesRepository applicationProfilesRepository;
    private PaaSInstance paaSInstance;
    private ApplicationInstance applicationInstance;
    private String paaSUserUriId;
    private int numberTests = 10;
    
//    @Required
//    public void setPaaSOfferingProfilesRepository(PaaSOfferingProfilesRepository paaSOfferingProfilesRepository) {
//        this.paasOfferingProfilesRepository = paaSOfferingProfilesRepository;
//    }
	
    @Before
    public void setUp(){
         paaSInstance = createTestPaaSInstance();
         applicationInstance = createTestApplicationInstance();
    }

//    @Test
//    public void TestStoreApplicationInstance (){
//        
//        applicationProfilesRepository.storeApplicationInstance(applicationInstance);
//        ApplicationInstance newApplicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstance.getUriId());
//        newApplicationInstance.setStatus( StatusType.Deployed );
//        newApplicationInstance.setPaaSOfferingDeployment(paaSInstance);
//        newApplicationInstance.setDeploymentIP("applicationUrl");
//        applicationProfilesRepository.updateApplicationInstance(newApplicationInstance);
//        ApplicationInstance retrievedApplicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstance.getUriId());
//        Assert.assertEquals("Provider title is different!", paaSInstance.getProviderTitle(), retrievedApplicationInstance.getPaaSOfferingDeploymentName());
//    }
    
    @Test
    public void TestConcurrentStoreApplicationInstance () throws ExecutionException{
        Collection<CallableNode> children = new ArrayList<CallableNode>();
        for (int i = 0; i < numberTests; i++) {
//            PaaSInstance localPaaSInstance = createTestPaaSInstance();
            ApplicationInstance localApplicationInstance = createTestApplicationInstance();
            //Creazione Thread...
            CallableNode callableNode = new CallableNode(paaSInstance, localApplicationInstance);
            children.add(callableNode);
        }

        ExecutorService executor = Executors.newFixedThreadPool(numberTests);  
        try {
            List<Future<Boolean>> invokeAll = executor.invokeAll(children);
            for (Future<Boolean> future : invokeAll) {
                while (!future.isDone());
                Boolean get = future.get();
            }
            System.out.print("All "+numberTests+ " deploy requests are completed!");
            executor.shutdownNow();
        } catch (InterruptedException ex) {
            System.out.println("exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            System.out.println("exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private PaaSInstance createTestPaaSInstance() {
        PaaSOffering paaSOffering = new PaaSOffering();
        PaaSProvider paaSProvider = new PaaSProvider();
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        PaaSUser paaSUser = new PaaSUser();
        paaSUserUriId = randomUUIDString;
        paaSUser.setUriId(randomUUIDString);
        paaSProvider.setUser(paaSUser);
        uuid = UUID.randomUUID();
        randomUUIDString = uuid.toString();
        paaSProvider.setUriId(randomUUIDString);
        paaSOffering.setPaaSProvider(paaSProvider);
        paaSOffering.setTitle(randomUUIDString);
        paaSProvider.setTitle(randomUUIDString);
        paaSProvider.setUriId(randomUUIDString);
        paaSOffering.setUriId(randomUUIDString);
        return new PaaSInstance(paaSOffering);
    }
    
    private ApplicationInstance createTestApplicationInstance(){
        ApplicationInstance applicationInstance = new ApplicationInstance();
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        applicationInstance.setAcronym(randomUUIDString);
        applicationInstance.setApplicationcode(randomUUIDString);
        applicationInstance.setProgramminglanguage("Java");
        applicationInstance.setProgramminglanguageVersion("1.6");
        applicationInstance.setVersion("1.0");
        applicationInstance.setArchiveFileName("SimpleWar");
        applicationInstance.setArchiveExtensionName(".war");
        return applicationInstance;
    }
    
    private class CallableNode implements Callable<Boolean> {
        private final PaaSInstance localPaaSInstance;
        private final ApplicationInstance localApplicationInstance;

        private CallableNode(PaaSInstance localPaaSInstance, ApplicationInstance localApplicationInstance) {
            this.localPaaSInstance = localPaaSInstance;
            this.localApplicationInstance = localApplicationInstance;
        }
        
        @Override
        public Boolean call() throws Exception {
            applicationProfilesRepository.storeApplicationInstance(localApplicationInstance);
            ApplicationInstance newApplicationInstance = applicationProfilesRepository.getApplicationInstance(localApplicationInstance.getUriId());
            newApplicationInstance.setStatus( StatusType.Deployed );
            newApplicationInstance.setPaaSOfferingDeployment(localPaaSInstance);
            newApplicationInstance.setDeploymentIP("applicationUrl");
            applicationProfilesRepository.updateApplicationInstance(newApplicationInstance);
            ApplicationInstance retrievedApplicationInstance = applicationProfilesRepository.getApplicationInstance(localApplicationInstance.getUriId());
//            Assert.assertNotNull("getPaaSOfferingDeploymentName is NULL!", retrievedApplicationInstance.getPaaSOfferingDeploymentName());
            Assert.assertNotNull("Deployment is NULL!", retrievedApplicationInstance.getApplication().getDeployment());
            Assert.assertNotNull("DeployingLocation is NULL!", retrievedApplicationInstance.getApplication().getDeployment().getDeployingLocation());
//            Assert.assertNotNull("PaaSProvider() is NULL!", retrievedApplicationInstance.getApplication().getDeployment().getDeployingLocation().getPaaSProvider());
            Assert.assertNotNull(".getTitle() is NULL!", retrievedApplicationInstance.getApplication().getDeployment().getDeployingLocation().getTitle());
            Assert.assertEquals("", localPaaSInstance.getProviderTitle(), retrievedApplicationInstance.getPaaSOfferingDeploymentName());
//            Assert.assertEquals("", localPaaSInstance.getProviderTitle(), retrievedApplicationInstance.getPaaSOfferingDeploymentName());
            return true;
        }
        
    }
}
