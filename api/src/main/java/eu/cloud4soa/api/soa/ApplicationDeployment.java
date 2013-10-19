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
import eu.cloud4soa.api.datamodel.core.SlaContract;
import eu.cloud4soa.api.datamodel.soa.ApplicationDeploymentParameters;
import eu.cloud4soa.api.datamodel.soa.GitRepoInfo;
import eu.cloud4soa.api.datamodel.soa.StringList;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.io.InputStream;
import java.util.List;

import java.util.Map;
import java.util.Map.Entry;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
//import org.json.JSONObject;

/**
 *
 * @author vincenzo
 * C4S Frontend required methods added by Yosu
 */
public interface ApplicationDeployment {
    
    @Deprecated
    @POST
    @Produces("text/plain")
    @Path("/deployApplication")
    Response deployApplication(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain")  String applicationInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "applicationArchive" , type = "application/octet-stream") InputStream is) throws SOAException;

    @Deprecated
    @POST
    @Produces("text/plain")
    @Path("/deployApplication")
    Response deployApplication(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain")  String applicationInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain")  String paaSInstanceUriId,
            @Multipart(value = "slaTemplateId", type = "text/plain")  String slaTemplateId,
            @Multipart(value = "applicationArchive" , type = "application/octet-stream")  InputStream is
    ) throws SOAException;
    
    
    
//    @POST
//    @Produces("text/plain")
//    @Path("/deployApplication")
    Response deployApplication(
           /* @Multipart(value = "deploymentParameters", type = "application/octet-stream") */ 
                    ApplicationDeploymentParameters parameters ) throws SOAException;
   

    
    @POST
    @Produces("text/plain")
    @Path("/startStopApplication")
    Response startStopApplication(
            @Multipart(value = "applicationInstance", type = "text/plain") String applicationInstanceUriId, 
            @Multipart(value = "startStopCommand", type = "text/plain") String startStopCommand) throws SOAException;
    
    @POST
    //@Consumes("text/plain")
    @Produces("text/plain")
    //@Produces("application/json")
    @Path("/removeApplication")
    Response removeApplication(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Produces("application/json")
    @Path("/retrieveAllDeployedApplicationProfiles")
    List<ApplicationInstance> retrieveAllDeployedApplicationProfiles(String userInstanceUriId) throws SOAException;
    
    //DB management
    @POST
//    @Consumes("multipart/form-data")
    @Produces("text/plain")
    @Path("/createDatabase")
    Response createDatabase(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain")String applicationInstanceUriId, 
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "dbStorageComponentUriId", type = "text/plain") String dbStorageComponentUriId) throws SOAException;
    
    @POST
    @Produces("text/plain")
    @Path("/initializeDatabase")
    Response initializeDatabase(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain")String applicationInstanceUriId, 
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "dbStorageComponentUriId", type = "text/plain") String dbStorageComponentUriId,
            @Multipart(value = "dumpFile" , type = "application/octet-stream") InputStream is) throws SOAException;
    
    @POST
    @Produces("text/plain")
    @Path("/dumpDatabase")
    InputStream dumpDatabase(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain")String applicationInstanceUriId, 
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "dbStorageComponentUriId", type = "text/plain") String dbStorageComponentUriId) throws SOAException;    
    
    // Git keys management - CRUD operations ----------------------------/
    
    @POST
    @Produces("text/plain")
    @Path("/getC4SOAPublicKey") 
    String getC4SOAPublicKey();
    
    @POST
    @Consumes("multipart/related")
    @Produces("text/plain")
    @Path("/registerPublicKeyForUser") 
    Response registerPublicKeyForUser(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "publicKey", type = "text/plain") String publicKey) throws SOAException;    
    
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml", "application/json"})
    @Path("/getPublicKeysForUser") 
    StringList getPublicKeysForUser( String userInstanceUriId ) throws SOAException;
    
    @POST
    @Consumes("multipart/related")
    @Produces("text/plain")
    @Path("/deletePublicKeyFromUser") 
    Response deletePublicKeyFromUser(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "publicKey", type = "text/plain") String publicKey ) throws SOAException;
    
            
    //-------------------------------------------------------------------/
    
    // Deployment using git ---------------------------------------------/
    @POST
    @Consumes("multipart/related")
    @Produces({"application/xml", "application/json"})
    @Path("/initializeGitProxy")
    GitRepoInfo initializeGitProxy(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId ) throws SOAException;

    
    @POST
    @Consumes("multipart/related")
    @Produces({"application/xml", "application/json"})
    @Path("/createGitRepo")
    GitRepoInfo createGitRepo(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId ) throws SOAException;

    @POST
    @Consumes("multipart/related")
    @Produces("text/plain")
    @Path("/registerGitRepository")
    Response registerGitRepository(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId,
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "repositoryName", type = "text/plain") String repositoryName,
            @Multipart(value = "gitUrl", type = "text/plain") String gitUrl ) throws SOAException;
    
    @POST
    @Consumes("multipart/related")
    @Produces("text/plain")
    @Path("/registerGitProxy")
    Response registerGitProxy(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "repositoryName", type = "text/plain") String repositoryName) throws SOAException;
    
    @POST
    @Consumes("multipart/related")
    @Produces("text/plain")
    @Path("/bindProxyToGit")
    Response bindProxyToGit(@Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "proxyId", type = "text/plain") String proxyId, 
            @Multipart(value = "gitRepoId", type = "text/plain") String gitRepoId ) throws SOAException;
    
    @POST
    @Produces("text/plain")
    @Path("/deleteRepo")
    Response deleteRepo(
            @Multipart(value = "c4sUserUriId", type = "text/plain") String c4sUserUriId,
            @Multipart(value = "repoName",    type = "text/plain") String repoName       ) throws SOAException;
    
    @POST
    @Consumes("multipart/related")
    @Produces({"application/xml", "application/json"})
    @Path("/commitGitDeploy")
    Response commitGitDeploy(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId ) throws SOAException;
    
    
    @POST
    @Consumes("multipart/related")
    @Produces({"application/xml", "application/json"})
    @Path("/getGitRepos")
    Map<Long, Entry<String, String>> getGitRepos(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId ) throws SOAException;
    
    @POST
    @Consumes("multipart/related")
    @Produces({"application/xml", "application/json"})
    @Path("/getGitProxies")
    Map<Long, Entry<String, Long>> getGitProxies(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId ) throws SOAException;
    
    
    @POST
    @Produces("text/plain")
    @Path("/relocateRepo")
    Response relocateRepo(
            @Multipart(value = "c4sUserUriId", type = "text/plain") String c4sUserUriId,
            @Multipart(value = "newPaasUriId", type = "text/plain") String newPaasUriId    ) throws SOAException;
    
    @POST
    @Consumes("multipart/related")
    @Produces({"application/xml", "application/json"})
    @Path("/getGitProxyInfos")
    GitRepoInfo getGitRepoInfos(
            @Multipart(value = "userInstanceUriId", type = "text/plain") String userInstanceUriId,
            @Multipart(value = "paaSInstanceUriId", type = "text/plain") String paaSInstanceUriId,
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId ) throws SOAException;

    
    @POST
    @Consumes("multipart/related")
    @Produces({"application/xml", "application/json"})
    @Path("/getSLAContract")
    SlaContract getSLAContract(
            @Multipart(value = "applicationInstanceUriId", type = "text/plain") String applicationInstanceUriId ) throws SOAException;

    
    /*
     * To Be removed!
     //------------------------------------------------------------------/ 
     */
    
    @Deprecated
    Response deployApplication(String applicationInstanceUriId, String paaSInstanceUriId, String publicKey, String secretKey, InputStream is) throws SOAException;
    
    @Deprecated
    Response startStopApplication( String applicationInstanceUriId, String startStopCommand, String publicKey, String secretKey) throws SOAException;
    
    @Deprecated
    Response removeApplication(String applicationInstanceUriId, String publicKey, String secretKey) throws SOAException;
    
    @Deprecated
    Response createDatabase(String applicationInstanceUriId, String paaSInstanceUriId, String dbStorageComponentUriId, String publicKey, String secretKey) throws SOAException;
    
    //------------------------------------------------------------------/
    
   
    
}
