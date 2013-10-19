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
package eu.cloud4soa.governance.ems;

import eu.cloud4soa.api.governance.MonitoringModule;
import eu.cloud4soa.relational.datamodel.Account;
import eu.cloud4soa.relational.datamodel.Paas;
import eu.cloud4soa.relational.datamodel.User;
import eu.cloud4soa.relational.persistence.AccountRepository;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import eu.cloud4soa.relational.persistence.PaasRepository;
import eu.cloud4soa.relational.persistence.UserRepository;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author pgouvas
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ems-test-governance.xml"})
public class EmsTest {
    
    //@Autowired
    //private MonitoringModule monitoringModule;
    
    @Autowired
    private UserRepository userrepository;
    
    @Autowired
    private PaasRepository paasrepository;
    
    @Autowired
    private AccountRepository accountrepository;    
    
    @Autowired
    private ApplicationInstanceRepository appinstancerepository;    
    
    @Test
    public void testWireing(){
        Assert.assertNotNull(userrepository);
        Assert.assertNotNull(paasrepository);
        Assert.assertNotNull(accountrepository);
        Assert.assertNotNull(appinstancerepository);        
    }      
    @Ignore
    //@Test
    public void testDBInteraction(){
        String provname = "CloudBees";
        String useruriid = "07a0827a-8bf8-4183-913d-e16c0dafef19";
        String appname = "applicationname";
        String appurl =     "http://1";
        String adapterurl = "http://2";
        String uriid = "uriid";
        //----------------DataBase interaction ---------------------------------------------------        
        long paasid=0;
        List<Paas> paaslist = paasrepository.findBy( "name" , provname );       
        if (paaslist!=null){
            paasid = ((Paas)paaslist.get(0)).getId();
        } else {
            //DB entry - it shouldnt be here
        }
        
        //User
        long userid=0;
        List<User> userlist = userrepository.findBy( "uriID" , useruriid );   
        if (userlist!=null){
            userid = ((User)userlist.get(0)).getId();
        } else {
            //DB entry - it shouldnt be here
        }
        
        //create Accounts for the pair userid - paasid
        if (userid!=0 && paasid!=0){
            //GetDAOS - reduntant
            User accountuser = userrepository.findById(userid);
            Paas accountpaas = paasrepository.findById(paasid);
            
            //Beanstalk
            Account account1 = new Account();
            account1.setPublickey("AKIAJRSZ7FBNKBAOUR6A"); //i.e. API key
            account1.setPrivatekey("7MPB3TqHf5Ds5UAX+nYORlY7/50kB01/vQbvJyyx");
            account1.setAccountname("");
            account1.setUser(accountuser);
            account1.setPaas(accountpaas);
            //Cloudbees
            Account account2 = new Account();     
            account2.setPublickey("4184E8A5D19D02D9"); //i.e. API key
            account2.setPrivatekey("UZPYSQVJMQLVNNVK6GSZQPRUTAZ+QKNB9QCKDWVNQMK=	");
            account2.setAccountname("testaccountname");   
            account2.setUser(accountuser);
            account2.setPaas(accountpaas);            
            accountrepository.store(account1);
            accountrepository.store(account2);  
            
            //create Aplication Instance
            eu.cloud4soa.relational.datamodel.ApplicationInstance appinstance = new eu.cloud4soa.relational.datamodel.ApplicationInstance();
            if (provname.equalsIgnoreCase("Beanstalk")) appinstance.setAccount(account1);
            if (provname.equalsIgnoreCase("CloudBees")) appinstance.setAccount(account2);
            appinstance.setName(appname);
            appinstance.setAppurl(appurl);
            appinstance.setAdapterurl(adapterurl);
            appinstance.setUriID(uriid);
            appinstancerepository.store(appinstance);
            
        }
        //----------------------------------------     
    }
    
    public EmsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

 
    
    
}
