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
import eu.cloud4soa.api.datamodel.core.MatchingPlatform;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.PaaSProviderDetails;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author vincenzo
 * C4S Frontend required methods added by Yosu
 */
public interface PaaSOfferingDiscovery {
//    List<core.PaaSInstance>, List<core.SlaContract> searchForMatchingPlatform(ui.ApplicationInstance);

    @POST
//    @Consumes("application/json")
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/searchForMatchingPlatform")
    public MatchingPlatform searchForMatchingPlatform(String applicationInstanceUriId) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/searchForPaaS")
//    core.PaaSInstance searchForPass(ui.PaaSInstance);
    PaaSInstance searchForPaaS(String paaSInstanceUriId) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/getPaaSProviderDetails")
//    core.PaaSProviderDetails getPaaSProviderDetails(core.PaaSInstance);
    PaaSProviderDetails getPaaSProviderDetails(String paaSInstanceUriId) throws SOAException;
    
    //Yosu: query type and return type could be modified accordingly to aligned them with best practices when using
    //RDF2Go or Jena API supporting SPARQL quering.
    //This method can be moved to another service class
    String query (String sparql) throws SOAException;
    
    //This method is requested by the frontend layer (for the DEMO)
    @POST
    @Produces({"application/xml","application/json"})
    @Path("/getAllAvailablePaaSInstances")
    List<PaaSInstance> getAllAvailablePaaSInstances() throws SOAException;    
}
