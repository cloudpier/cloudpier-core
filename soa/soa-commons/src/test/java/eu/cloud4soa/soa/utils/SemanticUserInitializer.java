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
 * @author vinlau
 */
public class SemanticUserInitializer {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UserManagementAndSecurityModule userManagementAndSecurityModule;
        
    public void initialize() throws SOAException {
        try {
            String developerFileName = "developer.ttl";
            String developerUserTurtleProfile = loadDeveloperTurtleProfile(developerFileName);
            String developerUriId = storeTurtleUserProfile(developerUserTurtleProfile, "fdandria", "ddandria");   
        } catch (IOException ex) {
            logger.error("Error during the creation of the Developer profile", ex);
        }
    }
    
        private String loadDeveloperTurtleProfile(String fileName) throws IOException {
        String developerUserDir = "developerUsers";
        return loadTurtleFileIntoString(developerUserDir, fileName);
    }
    
    private String loadTurtleFileIntoString(String dir, String fileName) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream( dir + "/" + fileName );
        String developerTurtleProfile = IOUtils.toString(is);
        return developerTurtleProfile;
    }

    
    public String storeTurtleUserProfile(String userProfile, String username, String password) throws SOAException{
        Response response = userManagementAndSecurityModule.storeTurtleUserProfile(userProfile, username, password);
        String userInstanceUriId = null;
        if(Response.Status.fromStatusCode(response.getStatus())==Response.Status.CREATED){
                userInstanceUriId = (String)response.getEntity();
                logger.info("Response Status CREATED - userInstanceUriId: " + userInstanceUriId);
        }
        return userInstanceUriId;
    }
}
