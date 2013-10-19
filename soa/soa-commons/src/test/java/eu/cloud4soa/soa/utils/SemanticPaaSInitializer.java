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
package eu.cloud4soa.soa.utils;

import eu.cloud4soa.api.soa.AnnouncementModule;
import eu.cloud4soa.api.soa.UserManagementAndSecurityModule;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.core.Response;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vins
 */
public class SemanticPaaSInitializer {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UserManagementAndSecurityModule userManagementAndSecurityModule;
    
    @Autowired
    private AnnouncementModule announcementModule;
    
    public void initialize() throws SOAException {
        
        try {
            String paasFileName = "AppEngine.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "appengine", "appengine");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the AppEngine profile", ex);
        }

        try {
            String paasFileName = "AppHarbor.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "appharbor", "appharbor");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the AppHarbor profile", ex);
        }   

        try {
            String paasFileName = "Azure.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "azure", "azure");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the Azure profile", ex);
        }
        
        try {
            String paasFileName = "Beanstalk.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "beanstalk", "beanstalk");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the Beanstalk profile", ex);
        }
        
        try {
            String paasFileName = "CloudBees.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "cloudbees", "cloudbees");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the CloudBees profile", ex);
        }
        
        try {
            String paasFileName = "CloudControl.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "cloudcontrol", "cloudcontrol");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the CloudControl profile", ex);
        }  
        
        try {
            String paasFileName = "CloudFoundry.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "cloudfoundry", "cloudcontrol");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the CloudFoundry profile", ex);
        }          

        try {
            String paasFileName = "Cumulogic.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "cumulogic", "cumulogic");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the Cumulogic profile", ex);
        }       
        
        try {
            String paasFileName = "EngineYard.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "engineyard", "engineyard");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the EngineYard profile", ex);
        }   

        try {
            String paasFileName = "Heroku.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "heroku", "heroku");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the Heroku profile", ex);
        }   
        
        try {
            String paasFileName = "MuleiON.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "muleion", "muleion");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the MuleiON profile", ex);
        }        
        
        try {
            String paasFileName = "OpenShift.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "openshift", "openshift");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the OpenShift profile", ex);
        }  
        
        try {
            String paasFileName = "MyPaaS.ttl";
            String paasUserTurtleProfile = loadPaaSUserTurtleProfile(paasFileName);
            String paaSUserUriId = storePaaSUserTurtleProfile(paasUserTurtleProfile, "mypaas", "mypaas");
            String paasOfferingTurtleProfile = loadPaaSOfferingTurtleProfile(paasFileName);
            String storePaaSUserInstance = storePaaSOfferingTurtleProfile(paaSUserUriId, paasOfferingTurtleProfile);
        } catch (IOException ex) {
            logger.error("Error during the creation of the OpenShift profile", ex);
        }  
        
    }

    private String loadPaaSUserTurtleProfile(String fileName) throws IOException {
        String paasUserDir = "paasUsers";
        String paasUserTurtleProfile = loadTurtleFileIntoString(paasUserDir, fileName);
        return paasUserTurtleProfile;
    }

    private String storePaaSUserTurtleProfile(String paasUserTurtleProfile, String username, String password) throws SOAException {
        Response response = userManagementAndSecurityModule.storeTurtleUserProfile(paasUserTurtleProfile, username, password);
        String userInstanceUriId = null;
        if(Response.Status.fromStatusCode(response.getStatus())==Response.Status.CREATED){
                userInstanceUriId = ((String)response.getEntity());
                logger.info("Response Status CREATED - userInstanceUriId: " + userInstanceUriId);
        }
        return userInstanceUriId;
    }

    private String loadPaaSOfferingTurtleProfile(String fileName) throws IOException {
        String paasUserDir = "paasProfiles";
        return loadTurtleFileIntoString(paasUserDir, fileName);
    }
    
    private String loadTurtleFileIntoString(String dir, String fileName) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream( dir + "/" + fileName );
        String paasOfferingTurtleProfile = IOUtils.toString(is);
        return paasOfferingTurtleProfile;
    }

    private String storePaaSOfferingTurtleProfile(String paaSUserUriId, String paasUserTurtleProfile) throws SOAException {
        Response response = announcementModule.storeTurtlePaaSProfile(paasUserTurtleProfile, paaSUserUriId);
        String paasInstanceUriId = null;
        if(Response.Status.fromStatusCode(response.getStatus())==Response.Status.CREATED){
                paasInstanceUriId = (String)response.getEntity();
                logger.info("paasInstanceUriId: "+paasInstanceUriId);
        }
        return paasInstanceUriId;
    }
}

