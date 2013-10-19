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

import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;
import eu.cloud4soa.api.datamodel.semantic.user.PaaSUser;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@ContextConfiguration(locations = {"classpath:PaaSOfferingProfilesRepositoryCtx.xml"})
public class PaaSOfferingProfilesRepositoryTest {
    @Autowired
    private PaaSOfferingProfilesRepository paaSOfferingProfilesRepository;
    private PaaSInstance paaSInstance;
    private String paaSUserUriId;
    
//    @Required
//    public void setPaaSOfferingProfilesRepository(PaaSOfferingProfilesRepository paaSOfferingProfilesRepository) {
//        this.paasOfferingProfilesRepository = paaSOfferingProfilesRepository;
//    }
	
    @Before
    public void setUp(){
         paaSInstance = createTestPaaSInstance();
    }

    @Test
    public void TestStorePaaSInstance (){
        try {
            paaSOfferingProfilesRepository.storePaaSInstance(paaSInstance);
        } catch (RepositoryException ex) {
            Assert.fail("storePaaSInstance method has thrown an exception!");
        }
        String paaSInstanceUriId = paaSInstance.getUriId();
        List<PaaSInstance> retrieveAllPaaSInstances = null;
        try {
            retrieveAllPaaSInstances = paaSOfferingProfilesRepository.retrieveAllPaaSInstances(paaSUserUriId);
        } catch (RepositoryException ex) {
            Assert.fail("retrieveAllPaaSInstances method has thrown an exception!");
        }        
        Assert.assertEquals("Bad dimension", 1, retrieveAllPaaSInstances.size());
        PaaSInstance retrievedPaaSInstance = retrieveAllPaaSInstances.get(0);
        Assert.assertEquals("Different UriId", paaSInstanceUriId, retrievedPaaSInstance.getUriId());
        System.out.println(retrievedPaaSInstance);
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
        return new PaaSInstance(paaSOffering);
    }
}
