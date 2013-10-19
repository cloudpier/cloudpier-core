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
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.frontend.PaaSRating;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import java.util.List;

/**
 *
 * @author vincenzo
 */
public interface PaaSOfferingProfilesRepository {
//    List<core.PaaSInstance> getPaaSInstances(core.ApplicationInstance);
    List<PaaSInstance> getPaaSInstances(ApplicationInstance applicationInstance) throws RepositoryException;
//    core.PaaSInstance getPaaSInstance(ui.PaaSInstance);
    PaaSInstance getPaaSInstance(String paaSInstanceUriId) throws RepositoryException;
//    void updatePaaSInstance(core.PaaSInstance, core.PaaSRating);
    void updatePaaSInstance(PaaSInstance paaSInstance, PaaSRating paaSRating) throws RepositoryException;
//    void storePaaSInstance(core.PaaSInstance);
    String storePaaSInstance(PaaSInstance paaSInstance) throws RepositoryException;
    
    //Requested by the Frontend for the DEMO
    List<PaaSInstance> getAllAvailablePaaSInstances() throws RepositoryException;
    void removePaaSInstance(String paasInstanceUriId) throws RepositoryException;
    List<PaaSInstance> retrieveAllPaaSInstances(String userInstanceUriId) throws RepositoryException;
}
