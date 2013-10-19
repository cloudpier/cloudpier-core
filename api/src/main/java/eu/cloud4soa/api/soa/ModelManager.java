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

package eu.cloud4soa.api.soa;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.ApplicationSemanticModel;
import eu.cloud4soa.api.datamodel.core.PaaSSemanticModel;
import eu.cloud4soa.api.datamodel.core.UserSemanticModel;
import eu.cloud4soa.api.datamodel.core.equivalence.EquivalenceRuleHWCategoryInstance;
import eu.cloud4soa.api.datamodel.repository.QueryResultTable;
import eu.cloud4soa.api.util.exception.SparqlQueryException;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

/**
 *
 * @author vincenzo
 * C4S Frontend required methods added by Yosu
 */
public interface ModelManager {

    @POST
    @Path("/storeApplicationProfile")
    @Produces("text/plain")
//    void storeApplicationProfile(ui.ApplicationInstance);
    String storeApplicationProfile(
            @Multipart(value = "applicationInstance", type = "application/json")ApplicationInstance applicationInstance,
            @Multipart(value = "userInstanceUriId", type = "text/plain")String userInstanceUriId) throws SOAException;
    
    @POST
    @Consumes("application/json")
    @Path("/updateApplicationProfile")
//    void updateApplicationProfile(ui.ApplicationInstance);
    void updateApplicationProfile(ApplicationInstance applicationInstance) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Path("/removeApplicationProfile")
    void removeApplicationProfile(String applicationInstanceUriId) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/retrieveAllApplicationProfile")
//    core.ApplicationSemanticModel, List<core.ApplicationInstance> retrieveAllApplicationProfile(ui.UserInstance );
    List<ApplicationInstance> retrieveAllApplicationProfile(String userInstanceUriId) throws SOAException;
    
    @POST
    @Produces({"application/xml","application/json"})
    @Path("/retrieveApplicationProfile")
    ApplicationInstance retrieveApplicationProfile (
            @Multipart(value = "applicationInstanceUriId", type = "text/plain")String applicationInstanceUriId,
            @Multipart(value = "userInstanceUriId", type = "text/plain")String userInstanceUriId) throws SOAException;
        
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/sparqlSelect")
    QueryResultTable sparqlSelect(String query) throws SparqlQueryException;
    
    @POST
    @Path("/storeTurtleApplicationProfile")
    @Produces("text/plain")
    public Response storeTurtleApplicationProfile(
            @Multipart(value = "applicationProfile", type = "text/plain")String applicationProfile,
            @Multipart(value = "userInstanceUriId", type = "text/plain")String userInstanceUriId) throws SOAException;
    
    @POST
    @Consumes({"application/xml","application/json"})
    @Produces("text/plain")
    @Path("/addEquivalenceRule")
    public Response addEquivalenceRule(EquivalenceRuleHWCategoryInstance equivalenceRule) throws SOAException;
}
