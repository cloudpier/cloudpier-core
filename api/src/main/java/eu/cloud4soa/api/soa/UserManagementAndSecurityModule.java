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

import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.soa.UserPaaSCredentials;
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
 *
 */
public interface UserManagementAndSecurityModule {

    @POST
//    @Consumes("application/json")
    @Produces("text/plain")
    @Path("/createNewAccount")
    //    void createNewAccount(ui.UserInstance);
    //    void createNewAccount(UserInstance userInstance);
    Response createNewAccount(
            @Multipart(value = "userInstance", type = "application/json") UserInstance userInstance,
            @Multipart(value = "username", type = "text/plain")String username, 
            @Multipart(value = "password", type = "text/plain")String password) throws SOAException;
    
    @POST
//    @Consumes("application/json")
    @Produces("text/plain")
//    @Produces({"application/xml","application/json"})
    @Path("/createNewUserAccount")
    Response storeTurtleUserProfile(
            @Multipart(value = "userProfile", type = "text/plain")String userProfile,
            @Multipart(value = "username", type = "text/plain")String username, 
            @Multipart(value = "password", type = "text/plain")String password) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/getUserInstance")
//    core.UserSemanticModel, core.UserInstance getUserInstance(ui.UserInstance);
    UserInstance getUserInstance(String userInstanceUriId) throws SOAException;
    
    @POST
    @Consumes({"application/xml","application/json"})
    @Produces("text/plain")
    @Path("/updateUserInstance")
    Response updateUserInstance(UserInstance userInstance) throws SOAException;

    @POST
    @Produces({"application/xml","application/json"})
    @Path("/authenticateUser")
    public UserInstance authenticateUser (
            @Multipart(value = "username", type = "text/plain")String username, 
            @Multipart(value = "password", type = "text/plain")String password) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("/isAccountNameInUse")
    public boolean isAccountNameInUse(String accountName) throws SOAException ;
    
    @POST
//    @Consumes("multipart/related")
    @Produces("text/plain")
    @Path("/storeUserCredentialsForPaaS")
    public Response storeUserCredentialsForPaaS (
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "publicKey", type = "text/plain") String publicKey, 
            @Multipart(value = "secretKey", type = "text/plain") String secretKey,
            @Multipart(value = "accountName", type = "text/plain", required = false) String accountName) throws SOAException;
    
    @POST
    @Produces("text/plain")
    @Path("/removeUserCredentialsForPaaS")
    public Response removeUserCredentialsForPaaS (
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId) throws SOAException;
    
    @POST
    @Produces("text/plain")
    @Path("/updateUserCredentialsForPaaS")
    public Response updateUserCredentialsForPaaS (
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "publicKey", type = "text/plain") String publicKey, 
            @Multipart(value = "secretKey", type = "text/plain") String secretKey,
            @Multipart(value = "accountName", type = "text/plain", required = false) String accountName) throws SOAException;
    
    @POST
    @Produces({"application/xml","application/json"})
    @Path("/readUserCredentialsForPaaS")
    public UserPaaSCredentials readUserCredentialsForPaaS (
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId) throws SOAException;
    
    @POST
    @Produces({"application/xml","application/json"})
    @Path("/readAllUserCredentialsForPaaS")
    public List<UserPaaSCredentials> readAllUserCredentialsForPaaS (String userInstanceUriId) throws SOAException;
}
