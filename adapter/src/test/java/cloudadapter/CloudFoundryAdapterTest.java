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
package cloudadapter;

import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.util.Assert;

/**
 *
 * @author Oriol Collell
 */
public class CloudFoundryAdapterTest {
    
    public static String APP_NAME = "cftester-app";
    public static String USER = "me@test.com";
    public static String PASS = "secret";
    
    public CloudFoundryAdapterTest() {
    }
    
   /* @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        System.out.println("* UtilsJUnit4Test: @Before method");
    }
*/
    @After
    public void tearDown() {
        try {
            Adapter.deleteApplication("CloudFoundry", "",USER, PASS, "", APP_NAME, null,null,null,null,null,null);
        } catch (Exception e) {
            //ignore
        }
    }
    
    @Ignore
    @Test
    public void testAuthentication() throws Exception {
        System.out.println("Testing Authentication");
        System.out.println("--------------------------------------");
        System.out.println("Authenticate with correct credentials");
        AuxAdapter.init(USER, PASS);
        System.out.println("Authenticate with bad credentials");
        try {
            AuxAdapter.init("fakeuser", "fakepass");
            fail("No exception!");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("error"));
        }
    }
    
    @Ignore
    @Test
    public void testCreateApplication() throws Exception {
        System.out.println("Testing Create Application");
        System.out.println("--------------------------------------");
        System.out.println("Create app with correct name");
        Adapter.createApplication("CloudFoundry", USER, PASS, "", APP_NAME, null);
        Assert.notNull(AuxAdapter.getApplicationCF(USER, PASS, APP_NAME));
        System.out.println("Create an app with no name");
        try {
            Adapter.createApplication("CloudFoundry", USER, PASS, "", "", null);
            fail("No exception!");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("empty"));
        }
    }
    
    @Ignore
    @Test
    public void testDeleteApplication() throws Exception {
        System.out.println("Testing Delete Application");
        System.out.println("--------------------------------------");
        System.out.println("Delete existing application");
        Adapter.createApplication("CloudFoundry", USER, PASS, "", APP_NAME, null);
        Assert.notNull(AuxAdapter.getApplicationCF(USER, PASS, APP_NAME));
        Adapter.deleteApplication("CloudFoundry", "",USER, PASS, "", APP_NAME, null,null,null,null,null,null);
        try {
            AuxAdapter.getApplicationCF(USER, PASS, APP_NAME);
            fail("No Exception!");
        } catch (Cloud4SoaException e) {
            assertTrue(e.getMessage().contains("cannot be found"));
        }
        System.out.println("Delete non-existent application");
        Adapter.deleteApplication("CloudFoundry", "",USER, PASS, "", "fakename", null,null,null,null,null,null);
        System.out.println("Bad name parameter");
        Adapter.deleteApplication("CloudFoundry", "",USER, PASS, "", "", null,null,null,null,null,null);
    }
    
    @Ignore
    @Test
    public void testDeploy() throws Exception {
        System.out.println("Testing Deployment");
        System.out.println("--------------------------------------");
        System.out.println("Create and deploy an app");
        Adapter.createApplication("CloudFoundry", USER, PASS, "", APP_NAME, null);
        String uri = Adapter.deploy("CloudFoundry", "more/SampleApp1.war",USER, PASS, "", APP_NAME, null,null,null,null,null,null,null);
        Thread.sleep(10000);
        URL app = new URL("http://"+uri);
        URLConnection appc = app.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                appc.getInputStream()));
        String inputLine = in.readLine();
        assertTrue(inputLine.contains("html"));
        in.close();
        
        System.out.println("Deploy non-existent application");
        try {
            Adapter.deploy("CloudFoundry", "more/SampleApp1.war",USER, PASS, "", "fakename", null,null,null,null,null,null,null);
            fail("No Exception!");
        } catch (Cloud4SoaException e) {
            assertTrue(e.getMessage().contains("cannot be found"));
        }
        
        System.out.println("Bad name parameter");
        try {
            Adapter.deploy("CloudFoundry", "more/SampleApp1.war",USER, PASS, "", "", null,null,null,null,null,null,null);
            fail("No Exception!");
        } catch (Exception e) {
        }
        
        System.out.println("Deploy non-existent binary");
        try {
            Adapter.deploy("CloudFoundry", "more/fake.war",USER, PASS, "", "", null,null,null,null,null,null,null);
            fail("No Exception!");
        } catch (Exception e) {
        }
    }
    
    @Ignore
    @Test
    public void testAppStatus() throws Exception {
        System.out.println("Testing App Status");
        System.out.println("--------------------------------------");
        System.out.println("Create and get status");
        Adapter.createApplication("CloudFoundry", USER, PASS, "", APP_NAME, null);
        assertEquals(Adapter.getAppStatus("CloudFoundry",USER, PASS, "", APP_NAME, null,null,null),"created");
        
        System.out.println("deploy and get status");
        Adapter.deploy("CloudFoundry", "more/SampleApp1.war",USER, PASS, "", APP_NAME, null,null,null,null,null,null,null);
        assertEquals(Adapter.getAppStatus("CloudFoundry",USER, PASS, "", APP_NAME, null,null,null),"deployed");

        System.out.println("Get status from non-existent application");
        try {
            Adapter.getAppStatus("CloudFoundry",USER, PASS, "", "fakename", null,null,null);
            fail("No Exception!");
        } catch (Cloud4SoaException e) {
            assertTrue(e.getMessage().contains("cannot be found"));
        }
        
        System.out.println("Bad name parameter");
        try {
            Adapter.getAppStatus("CloudFoundry",USER, PASS, "", "", null,null,null);
            fail("No Exception!");
        } catch (Exception e) {
        }
    }
    
    @Ignore
    @Test
    public void testRunningStatus() throws Exception {
        
        CloudFoundryClient client = AuxAdapter.init(USER, PASS);
        
        System.out.println("Testing Running Status");
        System.out.println("--------------------------------------");
        System.out.println("Create and get status");
        Adapter.createApplication("CloudFoundry", USER, PASS, "", APP_NAME, null);
        assertEquals(Adapter.getRunningStatus("CloudFoundry",USER, PASS, "", APP_NAME, null,null,null),"stopped");
        
        System.out.println("deploy and get status");
        Adapter.deploy("CloudFoundry", "more/SampleApp1.war",USER, PASS, "", APP_NAME, null,null,null,null,null,null,null);
        assertEquals(Adapter.getRunningStatus("CloudFoundry",USER, PASS, "", APP_NAME, null,null,null),"running");
        
        System.out.println("stop and get status");
        client.stopApplication(APP_NAME);
        assertEquals(Adapter.getRunningStatus("CloudFoundry",USER, PASS, "", APP_NAME, null,null,null),"stopped");
        
        System.out.println("Get status from non-existent application");
        try {
            Adapter.getRunningStatus("CloudFoundry",USER, PASS, "", "fakename", null,null,null);
            fail("No Exception!");
        } catch (Cloud4SoaException e) {
            assertTrue(e.getMessage().contains("cannot be found"));
        }
        
        System.out.println("Bad name parameter");
        try {
            Adapter.getRunningStatus("CloudFoundry",USER, PASS, "", "", null,null,null);
            fail("No Exception!");
        } catch (Exception e) {
        }
    }
    
    @Ignore
    @Test
    public void testCheckAppAvailability() throws Exception {
              
        System.out.println("Testing App Availabiltiy");
        System.out.println("--------------------------------------");
        System.out.println("Create and check availability");
        Adapter.createApplication("CloudFoundry", USER, PASS, "", APP_NAME, null);
        assertFalse(Adapter.checkAppAvailability("CloudFoundry",USER, PASS, "", APP_NAME, null,null,null));
        
        System.out.println("check availability of non-existent app");
        assertTrue(Adapter.checkAppAvailability("CloudFoundry",USER, PASS, "", "fakename", null,null,null));

    }
    
    @Ignore
    @Test
    public void testDownloadDB() throws Exception {
        /* Precondition: There is a MySQL service called test-db with a table called persons */      
        System.out.println("Testing DownloadDB");
        System.out.println("--------------------------------------");
        System.out.println("Download database");
        Adapter.downloadDBCF("me@test.com", "secret", "mysql", "test-db", "dbdump.sql");
        assertTrue(CloudFoundryAdapterTest.fileContainsString(new File("dbdump.sql"), "persons"));
        
        System.out.println("Download non-existent database");
        try {
            Adapter.downloadDBCF("me@test.com", "secret", "mysql", "fake-db", "dbdump.sql");
            fail("No Exception!");
        } catch (Cloud4SoaException e) {
            assertTrue(e.getMessage().contains("cannot be found"));
        }
        
        System.out.println("Bad name parameter");
        try {
            Adapter.downloadDBCF("me@test.com", "secret", "mysql", "", "dbdump.sql");
            fail("No Exception!");
        } catch (Cloud4SoaException e) {
            assertTrue(e.getMessage().contains("cannot be empty"));
        }

    }
    
    @Ignore
    @Test
    public void testRestoreDB() throws Exception {
        /* Precondition: There is a MySQL service called test-db */      
        System.out.println("Testing RestoreDB");
        System.out.println("--------------------------------------");
        System.out.println("Restore database");
        Adapter.restoreDBCF("me@test.com", "secret", "mysql", "test-db", "dbdump.sql");
        
        System.out.println("Restore non-existent database");
        try {
            Adapter.restoreDBCF("me@test.com", "secret", "mysql", "fake-db", "dbdump.sql");
            fail("No Exception!");
        } catch (Cloud4SoaException e) {
            assertTrue(e.getMessage().contains("cannot be found"));
        }
        
        System.out.println("Bad name parameter");
        try {
            Adapter.restoreDBCF("me@test.com", "secret", "mysql", "", "dbdump.sql");
            fail("No Exception!");
        } catch (Cloud4SoaException e) {
            assertTrue(e.getMessage().contains("cannot be empty"));
        }

    }
    
    public static boolean fileContainsString(File file, String aString) throws FileNotFoundException {
  
      FileInputStream fis = null;
      BufferedReader in = null;
  
      try{
         fis = new FileInputStream(file);
         in = new BufferedReader(new InputStreamReader(fis));
   
         String currentLine = "";
         while ((currentLine = in.readLine()) != null) {
            if(currentLine.indexOf(aString) > 0)  return true;
         }
   
      }catch(IOException ioe){
         ioe.printStackTrace();
      }finally{
         try{
            if(in != null) in.close();
            if(fis != null) fis.close();
         }catch(IOException ioe){ }
      }
      return false;
   } 
}
