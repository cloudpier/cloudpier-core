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


package eu.cloud4soa.relationalrepo;


import eu.cloud4soa.relational.datamodel.Usertype;
import eu.cloud4soa.relational.persistence.UsertypeRepository;
import java.util.List;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author pgouvas
 */
//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:repository-context.xml"})
@Transactional
public class DatabaseTest 
//        extends AbstractTransactionalJUnit4SpringContextTests 
{
        
        final Logger logger = LoggerFactory.getLogger(DatabaseTest.class);
        
        @Autowired
        UsertypeRepository usertyperepository;
    
/*	@Before
	public void setUp(){ 

        }
         * 
         */
 
	@Test
    @DirtiesContext
	public void testWireing(){
		Assert.assertNotNull(usertyperepository);
	}        
        
        @Test
//        @Transactional
        
        public void testAddUser(){
            try {
                usertyperepository.findById(new Long("1"));
                
                //Initial Add
                Usertype usertype = new Usertype("test");
                usertyperepository.store(usertype);
                usertyperepository.refresh(usertype);
                //getAll
                List<Usertype> usertypes = usertyperepository.retrieveAll();
                Assert.assertTrue(usertypes.size()>0);
                //update
                usertype.setName("test12");
                usertyperepository.update(usertype);
                
                Usertype copy = usertyperepository.findById(usertype.getId());
                Assert.assertEquals(usertype.getId(), copy.getId());                
                
            } catch (Exception e){
                Assert.fail(e.getMessage());
            }
        }//EoM testAddUser
        
        
    @Test
    public void testUserRepositoryContent() {
        assertTrue( "Usertype table should contain at least 2 elements", usertyperepository.findAll().size() >= 2 );
    }
        
}