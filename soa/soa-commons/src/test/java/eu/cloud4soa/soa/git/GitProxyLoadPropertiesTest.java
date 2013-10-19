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
package eu.cloud4soa.soa.git;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import eu.cloud4soa.relational.persistence.GitProxyRepository;
import eu.cloud4soa.relational.persistence.GitRepoRepository;
import eu.cloud4soa.relational.persistence.PaasRepository;
import eu.cloud4soa.relational.persistence.PubKeyRepository;
import eu.cloud4soa.relational.persistence.UserRepository;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;

 //Let's import Mockito statically so that the code looks clearer

/**
 *
 * @author vinlau
 */
//@RunWith(MockitoJUnitRunner.class)
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = {"classpath:GitServiceCtx.xml"})
public class GitProxyLoadPropertiesTest {
    
    final Logger logger = LoggerFactory.getLogger(GitProxyLoadPropertiesTest.class);
    
    @Autowired
    @InjectMocks
    private GitServices gitservices;;

    @Mock
    PubKeyRepository   pubkeydao;
    @Mock
    UserRepository     userdao;
    @Mock
    GitRepoRepository  repodao;
    @Mock
    GitProxyRepository proxydao;
    @Mock
    PaasRepository     paasdao;

    @Test
    public void testProperties(){
        Assert.assertEquals("cloud", gitservices.SERVER_ACCOUNT_NAME);
        Assert.assertEquals("127.0.0.1", gitservices.SERVER_IP_ADDRESS);
        Assert.assertEquals("src/test/resources/git/authorized_keys", gitservices.AUTHORIZED_KEYS_FILE);
        Assert.assertEquals("src/test/resources/git/id_rsa.pub", gitservices.C4SOA_SERVER_PUBLIC_KEY);
        Assert.assertEquals("src/test/resources/git/proxy-git", gitservices.PROXY_GIT_FILE);
    }
    
}
