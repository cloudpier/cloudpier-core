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

package eu.cloud4soa.api.repository;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import java.util.List;

/**
 *
 * @author vincenzo
 */
public interface ApplicationProfilesRepository {
//    core.ApplicationInstance getApplicationInstance(ui.ApplicationSemanticModel);
    ApplicationInstance getApplicationInstance(String applicationInstanceUriId) throws RepositoryException;
//    void storeApplicationInstance(core.ApplicationInstance);
    void storeApplicationInstance(ApplicationInstance applicationInstance) throws RepositoryException;
//    void updateApplicationInstance(core.ApplicationInstance);
    void updateApplicationInstance(ApplicationInstance applicationInstance) throws RepositoryException;
//    List<core.ApplicationInstance> retrieveAllApplicationProfile(ui.UserInstance);
    List<ApplicationInstance> retrieveAllApplicationProfile(String userInstanceUriId) throws RepositoryException;
    
    public List<ApplicationInstance> retrieveAllApplicationProfileDeployedOnPaaS(String paasInstanceUserId) throws RepositoryException;
    
//Added by CRUD requirements    
    void removeApplicationProfile(String applicationInstanceUriId) throws RepositoryException;
    
    
}
