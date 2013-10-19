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

import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.relational.datamodel.ApplicationInstance;
import eu.cloud4soa.relational.datamodel.GitProxy;
import eu.cloud4soa.relational.datamodel.GitRepo;
import eu.cloud4soa.relational.datamodel.Paas;
import org.springframework.test.annotation.DirtiesContext;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.mockito.MockitoAnnotations;
import eu.cloud4soa.relational.datamodel.PubKey;
import java.util.ArrayList;
import java.util.List;
import eu.cloud4soa.relational.datamodel.User;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import eu.cloud4soa.relational.persistence.GitProxyRepository;
import eu.cloud4soa.relational.persistence.GitRepoRepository;
import eu.cloud4soa.relational.persistence.PaasRepository;
import eu.cloud4soa.relational.persistence.PubKeyRepository;
import eu.cloud4soa.relational.persistence.UserRepository;
import java.io.File;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 //Let's import Mockito statically so that the code looks clearer
 import static org.mockito.Mockito.*;

/**
 *
 * @author vinlau
 */
//@RunWith(MockitoJUnitRunner.class)
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = {"classpath:GitServiceCtx.xml"})
public class GitProxyTest {
    
    final Logger logger = LoggerFactory.getLogger(GitProxyTest.class);
    
    private static String originalAuthFile = null;
    private static String originalGitFile = null;
    
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
    @Mock
    ApplicationInstanceRepository appdao;
    
    File authTempFile;
    File gitTempFile;
    
    String userInstanceUriId = "1122334455";

    @Before
    public void before() throws Exception {

        MockitoAnnotations.initMocks(this);
        
        if(originalAuthFile==null)
            originalAuthFile = gitservices.AUTHORIZED_KEYS_FILE;
        if(originalGitFile==null)
            originalGitFile = gitservices.PROXY_GIT_FILE;
        
        this.authTempFile = File.createTempFile("authorizedKeys", ".txt");
        FileUtils.copyFile(new File(originalAuthFile), this.authTempFile);        
        gitservices.AUTHORIZED_KEYS_FILE = this.authTempFile.getAbsolutePath();
                
        this.gitTempFile = File.createTempFile("git", ".txt");                
        FileUtils.copyFile(new File(originalGitFile), this.gitTempFile);
        gitservices.PROXY_GIT_FILE = this.gitTempFile.getAbsolutePath();
            
    }

    @After
    public void after() throws Exception {
        this.authTempFile.delete();
        this.gitTempFile.delete();
    }
    
    @Test
    public void testPubKey(){
        String pubKey = "ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAwqkTgZC8aZHbmgD6w+OA32fCZzCcJTi1YGb/0yDW+ugGvD6Es/KSs1SgCHcNL4DlkK4IzbIcpS6oP3+AlJPRP5ggeR3jj8KlF5Nz9CF5umkSu3jD0yikB5N8bVISabTUCZpyogzBLNKVZwqIc1JNBg0LWB49tUNmtpOEiGhDglmFaJDktWj1J8mrZvhPCEnH8r+n/0LYvdpsdHgnlgGHJBAbreBLGUo/XNIDj1Qw37h1IDCrfH52GEinqv1S9ZhSvKsH3DOvOvn6fiDwzKHrF8e/jX6qjjTOLys9lz/5eDMrH1IlRkqLGES6LM4gQZ0SiN0+FdGU5BMb4VKIOSI4DQ== cloud@localhost";
        String[] array = gitservices.getC4SOAPublicKey();
        logger.info(array[0].toString());
        Assert.assertEquals("0", array[0]);
        Assert.assertEquals(pubKey, array[1]);       
    }
    
    @Test
    public void testRegisterPublicKeyForUser(){
                
        User user = new User();
        user.setFullname("Test");
        user.setUriID(userInstanceUriId);
        user.setUsername("testUsername");
        List<Object> arrayList = new ArrayList<Object>();
        arrayList.add(user);
        when(userdao.findBy("uriID", userInstanceUriId)).thenReturn(arrayList);
        
        String rsa_pub_key = "aabbcc";
        when(pubkeydao.findByUserAndPubkey(user, rsa_pub_key)).thenReturn(new ArrayList<PubKey>());

        String[] array = gitservices.registerPublicKeyForUser(userInstanceUriId, rsa_pub_key);
        Assert.assertEquals("0", array[0]);
        Assert.assertEquals("OK", array[1]); 
                
        ArrayList<PubKey> arrayList1 = new ArrayList<PubKey>();
        arrayList1.add(new PubKey());
        when(pubkeydao.findByUserAndPubkey(user, rsa_pub_key)).thenReturn(arrayList1);
        
        array = gitservices.registerPublicKeyForUser(userInstanceUriId, rsa_pub_key);
        Assert.assertEquals("1", array[0]);

        try {
            String originalContent = FileUtils.readFileToString(new File(originalAuthFile));
            String contentModified = FileUtils.readFileToString(authTempFile);            
            int compareResult = originalContent.compareTo(contentModified);
            Assert.assertNotSame("File contents are equals!", 0, compareResult);
        } catch (IOException ex) {
            logger.error("Error in reading the file: "+ex.getMessage());
            Assert.fail("Error in reading the file: "+ex.getMessage());
        }
    }
    
    @Test
    public void testDeletePublicKeyForUser(){
        
        User user = new User();
        user.setId(new Long(0));
        user.setFullname("Test");
        user.setUriID(userInstanceUriId);  
        user.setUsername("testUsername");
        List<Object> arrayList = new ArrayList<Object>();
        arrayList.add(user);
        when(userdao.findBy("uriID", userInstanceUriId)).thenReturn(arrayList);
        
        String rsa_pub_key = "aabbcc";
        when(pubkeydao.findByUserAndPubkey(user, rsa_pub_key)).thenReturn(new ArrayList<PubKey>());

        String[] array = gitservices.registerPublicKeyForUser(userInstanceUriId, rsa_pub_key);
        Assert.assertEquals("0", array[0]);
        Assert.assertEquals("OK", array[1]); 
                
        ArrayList<PubKey> arrayList1 = new ArrayList<PubKey>();
        PubKey pubKey = new PubKey();
        pubKey.setId(new Long(0));
        arrayList1.add(pubKey);
        when(pubkeydao.findByPubkey(rsa_pub_key)).thenReturn(arrayList1);     
        
        when(userdao.findBy("uriID", userInstanceUriId)).thenReturn(arrayList);
        when(pubkeydao.findByUserAndPubkey(user, rsa_pub_key )).thenReturn(arrayList1);
        array = gitservices.deletePublicKeyFromUser(userInstanceUriId, rsa_pub_key);
        Assert.assertEquals("0", array[0]);
        Assert.assertEquals("OK", array[1]); 
        try {
            String originalContent = FileUtils.readFileToString(new File(originalAuthFile));
            String contentModified = FileUtils.readFileToString(authTempFile);            
            int compareResult = originalContent.compareTo(contentModified);
            logger.info("Compare:"+compareResult);
            Assert.assertNotSame("File contents are different!", 0, compareResult);
        } catch (IOException ex) {
            logger.error("Error in reading the file: "+ex.getMessage());
            Assert.fail("Error in reading the file: "+ex.getMessage());
        }
    }

    @Test
    public void getPublicKeysForUser() {
        User user = new User();
        user.setId(new Long(0));
        user.setFullname("Test");
        user.setUriID(userInstanceUriId);
        user.setUsername("testUsername");
        List<Object> userList = new ArrayList<Object>();
        userList.add(user);
        when(userdao.findBy("uriID", userInstanceUriId)).thenReturn(userList);
        
        PubKey pubkey1 = new PubKey();
        pubkey1.setId(new Long(0));
        pubkey1.setPubkey("pubkey1");
        pubkey1.setUser(user);
        
        PubKey pubkey2 = new PubKey();
        pubkey2.setId(new Long(0));
        pubkey2.setPubkey("pubkey2");
        pubkey2.setUser(user);        
        
        List<PubKey> pubkeysList = new ArrayList<PubKey>();
        pubkeysList.add(pubkey1);
        pubkeysList.add(pubkey2);
        
        when(pubkeydao.findByUser(user)).thenReturn(pubkeysList);
        

        try {
            List<PubKey> publicKeysForUser = gitservices.getPublicKeysForUser(userInstanceUriId);
            logger.info(publicKeysForUser.toString());
            Assert.assertNotNull(publicKeysForUser);
            Assert.assertEquals(2, publicKeysForUser.size());
        } catch (SOAException ex) {
            Assert.fail("SOA Error in getPublicKeysForUser: " + ex.getMessage());     
        } catch(Exception ex) {
            logger.error("Error in getPublicKeysForUser: " + ex.getMessage());
            Assert.fail("Error in getPublicKeysForUser: " + ex.getMessage());            
        }        
    }

    
    @Test
    public void testRegisterGitRepository() {
        //Arguments String userInstanceUriId, String giturl, String reponame, String paasid
        String GITURL      = "git@heroku.com";
        String GITREPONAME = "c4sapp1.git";
        String PAASID = "0";
        String APPID = "0";
        String APPURIID = "appUriId";
        //Mocks
        //Mock userdao.findBy
        User user = new User();
        user.setId(new Long(0));
        user.setFullname("Test");
        user.setUriID(userInstanceUriId);
        user.setUsername("testUsername");
        List<Object> userList = new ArrayList<Object>();
        userList.add(user);
        when(userdao.findBy("uriID", userInstanceUriId)).thenReturn(userList);
        
        //Mock paasdao.findBy
        Paas paas = new Paas("Heroku", "heroku.com");
        paas.setId(new Long(PAASID));
        List<Object> paasList = new ArrayList<Object>();
        paasList.add(paas);
        when(paasdao.findBy("id", new Long(PAASID))).thenReturn(paasList);
        
        ApplicationInstance app = new ApplicationInstance();
        app.setId(new Long(APPID));
        app.setUriID(APPID);
        List<ApplicationInstance> appList = new ArrayList<ApplicationInstance>();
        appList.add(app);
        when(appdao.findByUriId(APPURIID)).thenReturn(appList);
        
        //Mock repodao.findByGitrepo(reponame);
        when( repodao.findByGitrepo(GITREPONAME) ).thenReturn(new ArrayList<GitRepo>() );
        //Mock repodao.save(gitrepo);
        GitRepo repo = new GitRepo();
        repo.setId(new Long(0));        
        repo.setGitrepo(GITREPONAME);
        repo.setGiturl(GITURL);
        repo.setUser(user);
        repo.setPaas(paas);
        when(repodao.save(repo)).thenReturn(repo);
        
        //Actual invocation
        String[] ret = gitservices.registerGitRepository("", userInstanceUriId, GITURL, GITREPONAME, PAASID);
        try {
            Assert.assertEquals("0", ret[0]);
        } catch (Exception ex) {
            logger.error("Error in testRegisterGitRepository: " + ex.getMessage());
            Assert.fail("Error in testRegisterGitRepository: " + ex.getMessage());
        }        
        
    }    
    
    @Test
    public void testRegisterGitProxy() {
        //Arguments String userInstanceUriId, String proxyname
        String PROXYNAME = "proxy1.git";
        //Mocks
        //Mock userdao.findBy("uriID", userInstanceUriId);
        User user = new User();
        user.setId(new Long(0));
        user.setFullname("Test");
        user.setUriID(userInstanceUriId);
        user.setUsername("testUsername");
        List<Object> userList = new ArrayList<Object>();
        userList.add(user);
        when(userdao.findBy("uriID", userInstanceUriId)).thenReturn(userList);        
        
        //Mock proxydao.findByProxyname(proxyname)
        when(proxydao.findByProxyname(PROXYNAME)).thenReturn(new ArrayList<GitProxy>());
        //Mock proxydao.save(gitproxy);
        GitProxy proxy = new GitProxy();
        proxy.setId(new Long(0));
        proxy.setProxyname(PROXYNAME);
        proxy.setUser(user);
        //Invocation
        String[] ret = gitservices.registerGitProxy(userInstanceUriId, PROXYNAME);
        try {
            Assert.assertEquals("0", ret[0]);
        } catch (Exception ex) {
            logger.error("Error in testRegisterGitProxy: " + ex.getMessage());
            Assert.fail("Error in testRegisterGitProxy: " + ex.getMessage());
        }        
    }

    @Test
    public void testBindProxytoGit() {
        //Arguments  String userInstanceUriId, String proxyid, String gitid
        String REPOID = "0";
        String GITURL = "git@heroku.com";
        String GITREPONAME = "c4sapp1.git";       
        String PROXYID = "0";
        String PROXYNAME = "proxy1.git";
        //Mocks
        //Mock userdao.findBy("uriID", userInstanceUriId)
        User user = new User();
        user.setId(new Long(0));
        user.setFullname("Test");
        user.setUriID(userInstanceUriId);
        user.setUsername("testUsername");
        List<Object> userList = new ArrayList<Object>();
        userList.add(user);
        when(userdao.findBy("uriID", userInstanceUriId)).thenReturn(userList);
        
        //Mock repodao.findByUserAndGitrepoid(user, new Long(gitid))
        GitRepo repo = new GitRepo();
        repo.setId(new Long(0));
        repo.setUser(user);  
        repo.setGiturl(GITURL);
        repo.setGitrepo(GITREPONAME);
        List<GitRepo> repos = new ArrayList<GitRepo>();
        repos.add(repo);
        when( repodao.findByUserAndGitrepoid(user, new Long(REPOID)) ).thenReturn(repos);
        
        //Mock proxydao.findByUserAndGitproxyid(user,new Long(proxyid))
        GitProxy proxy = new GitProxy();
        proxy.setId(new Long(0));
        proxy.setUser(user);
        proxy.setProxyname(PROXYNAME);
        List<GitProxy> proxies = new ArrayList<GitProxy>();
        proxies.add(proxy);
        when( proxydao.findByUserAndGitproxyid(user, new Long(PROXYID)) ).thenReturn(proxies);
        
        //Mock proxydao.save(proxy);
        proxy.setRepo(repo);
        when( proxydao.save(proxy) ).thenReturn(proxy);
                
        //Invocation        
        try {
            String[] ret = gitservices.bindProxyToGit(userInstanceUriId, PROXYID, REPOID);            
            Assert.assertEquals("0", ret[0]);
            String contents = FileUtils.readFileToString(gitTempFile);
            logger.info("contents");
            logger.info(contents);
            String musthave= //"#proxy0\n"
                    //+ "REPO=\"c4sapp1.git\"                                                                                         \n"
                     "if [ \"$repo\" == \"'proxy1.git'\" ]; then ";
            Assert.assertEquals( contents.indexOf(musthave)!=-1 , true);
        } catch (Exception ex) {
            logger.error("Error in testBindProxytoGit: " + ex.getMessage());
            Assert.fail("Error in testBindProxytoGit: " + ex.getMessage());
        }        
        
    }    
    
    
}
