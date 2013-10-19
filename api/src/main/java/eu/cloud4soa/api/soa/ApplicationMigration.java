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

import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

/**
 *
 * @author vincenzo
 */
public interface ApplicationMigration {
//    void migrateApplication(ui.ApplicationInstance, ui.PaaSInstance
    @POST
    @Produces("text/plain")
    @Path("/migrateApplication")
    Response migrateApplication(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId,
            @Multipart(value = "newPaaSInstanceUriId", type = "text/plain") String newPaaSInstanceUriId,
            @Multipart(value = "applicationArchive" , type = "application/octet-stream") InputStream is) throws SOAException ;
    
    @POST
    @Produces("text/plain")
    @Path("/migrateDatabases")
    Response migrateDatabases(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId,
            @Multipart(value = "newPaaSInstanceUriId", type = "text/plain") String newPaaSInstanceUriId) throws SOAException ;
    
    @POST
    @Produces("text/plain")
    @Path("/commitMigration")
    Response commitMigration(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId) throws SOAException ;
    
    @POST
    @Produces("text/plain")
    @Path("/rollbackMigration")
    Response rollbackMigration(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId) throws SOAException ;
    
    
    
    @POST
    @Consumes("multipart/related")
    @Produces({"application/xml", "application/json"})
    @Path("/migrateApplicationCommitGitPush")
    Response migrateApplicationCommitGitPush(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId
            ) throws SOAException;
    
    
    
    @POST
    @Produces("text/plain")
    @Path("/migrateApplicationPrepareGitPush")
    public Response migrateApplicationPrepareGitPush(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId
            ) throws SOAException;
    
}