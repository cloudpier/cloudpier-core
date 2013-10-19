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

import eu.cloud4soa.api.soa.ModelManager;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vinlau
 */
public class SemanticAppInitializer {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    final String developerUriId = "FrancescoDandria";
    
    @Autowired
    private ModelManager modelManager;

    public void initialize() throws SOAException {
        
        try {
            String developerUserDir = "applicationProfiles";
            URL resource = scanPackage(developerUserDir);
            String protocol = resource.getProtocol();
            if (protocol.equals("file")) {
                File file = new File(resource.getFile());
                if(file.isDirectory()){
                    String[] list = file.list();
                    for (String fileName : list) {
                        String applicationTurtleProfile = loadTurtleFileIntoString(developerUserDir, fileName);
                        logger.info("Loaded application profile: "+fileName);
                        storeTurtleApplicationProfile(applicationTurtleProfile, developerUriId);
                    }
                }
            }
        } catch (IOException ex) {
            logger.error("Error during the creation of the Application profiles", ex);
        }
    }
    
    private URL scanPackage(String dir) throws IOException {
        URL resource = this.getClass().getClassLoader().getResource(dir);
        return resource;
    }
    
    private String loadTurtleFileIntoString(String dir, String fileName) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream( dir + "/" + fileName );
        String applicationTurtleProfile = IOUtils.toString(is);
        return applicationTurtleProfile;
    }

    
    public String storeTurtleApplicationProfile(String applicationProfile, String userInstanceUriId) throws SOAException{
        Response response = modelManager.storeTurtleApplicationProfile(applicationProfile, userInstanceUriId);
        String applicationInstanceUriId = null;
        if(Response.Status.fromStatusCode(response.getStatus())==Response.Status.CREATED){
            applicationInstanceUriId = (String)response.getEntity();
            logger.debug("applicationInstanceUriId: "+applicationInstanceUriId);
        }
        return applicationInstanceUriId;
    }
}

