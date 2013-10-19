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

import java.util.List;

import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.util.exception.soa.SOAException;
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
public interface AnnouncementModule {

    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/getPaaSInstance")
    //    core.PaaSInstance getPaaSInstance(ui.PaaSInstance);
    PaaSInstance getPaaSInstance(String paasInstanceUriId) throws SOAException;
    
    @POST
//    @Produces({"application/xml","application/json"})
    @Path("/storePaaSInstance")
    //    void storePaaSInstance(core.PaaSInstance);
    String storePaaSInstance(
            @Multipart(value = "paaSInstance", type = "application/json") PaaSInstance paaSInstance, 
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId) throws SOAException;

    @POST
    @Produces("text/plain")
    @Path("/storeTurtlePaaSProfile")
    Response storeTurtlePaaSProfile(
            @Multipart(value = "paasProfile", type = "text/plain") String paasProfile, 
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/retrieveAllPaaSInstances")
    List<PaaSInstance> retrieveAllPaaSInstances(String userInstanceUriId) throws SOAException;
    
    @POST
    @Consumes("application/json")
//    @Produces({"application/xml","application/json"})
    @Path("/updatePaaSInstance")
    void updatePaaSInstance(PaaSInstance paaSInstance) throws SOAException;
    
    @POST
    @Consumes("text/plain")
//    @Produces({"application/xml","application/json"})
    @Path("/removePaaSInstance")
    void removePaaSInstance(String paasInstanceUriId) throws SOAException;

    
}
