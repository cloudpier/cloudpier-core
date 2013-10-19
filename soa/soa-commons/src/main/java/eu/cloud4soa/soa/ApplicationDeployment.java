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

package eu.cloud4soa.soa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.SlaContract;
import eu.cloud4soa.api.datamodel.core.utilBeans.DBStorageComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.StatusType;
import eu.cloud4soa.api.datamodel.governance.DatabaseInfo;
import eu.cloud4soa.api.datamodel.governance.DeployApplicationParameters;
import eu.cloud4soa.api.datamodel.governance.SlaContractValidity;
import eu.cloud4soa.api.datamodel.semantic.inf.DBDeployment;
import eu.cloud4soa.api.datamodel.semantic.inf.DBStorageComponent;
import eu.cloud4soa.api.datamodel.soa.ApplicationDeploymentParameters;
import eu.cloud4soa.api.datamodel.soa.GitRepoInfo;
import eu.cloud4soa.api.datamodel.soa.StringList;
import eu.cloud4soa.api.datamodel.soa.UserPaaSCredentials;
import eu.cloud4soa.api.governance.ExecutionManagementServiceModule;
import eu.cloud4soa.api.governance.SLAModule;
import eu.cloud4soa.api.repository.ApplicationProfilesRepository;
import eu.cloud4soa.api.repository.PaaSOfferingProfilesRepository;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.relational.datamodel.GitProxy;
import eu.cloud4soa.relational.datamodel.GitRepo;
import eu.cloud4soa.relational.datamodel.PubKey;
import eu.cloud4soa.soa.git.GitServices;

/**
 *
 * @author vincenzo
 * C4S Frontend required methods added by Yosu
 */
@Transactional
public class ApplicationDeployment implements eu.cloud4soa.api.soa.ApplicationDeployment{

    final Logger logger = LoggerFactory.getLogger(ApplicationDeployment.class);

    private SLAModule slaModule;
    private ApplicationProfilesRepository applicationProfilesRepository;
    private PaaSOfferingProfilesRepository paaSOfferingProfilesRepository;
    private ExecutionManagementServiceModule executionManagementServiceModule;
    private UserManagementAndSecurityModule userManagementAndSecurityModule;
    
    @Autowired
    private GitServices gitServices;
    
    
    /**
     * @param slaModule the slaModule to set
     */
    public void setSlaModule(SLAModule slaModule) {
        this.slaModule = slaModule;
    }

    /**
     * @param applicationProfilesRepository the applicationProfilesRepository to set
     */
    public void setApplicationProfilesRepository(ApplicationProfilesRepository applicationProfilesRepository) {
        this.applicationProfilesRepository = applicationProfilesRepository;
    }

    /**
     * @param paaSOfferingProfilesRepository the paaSOfferingProfilesRepository to set
     */
    public void setPaaSOfferingProfilesRepository(PaaSOfferingProfilesRepository paaSOfferingProfilesRepository) {
        this.paaSOfferingProfilesRepository = paaSOfferingProfilesRepository;
    }

    /**
     * @param executionManagementServiceModule the executionManagementServiceModule to set
     */
    public void setExecutionManagementServiceModule(ExecutionManagementServiceModule executionManagementServiceModule) {
        this.executionManagementServiceModule = executionManagementServiceModule;
    }

    public void setUserManagementAndSecurityModule(UserManagementAndSecurityModule userManagementAndSecurityModule) {
        this.userManagementAndSecurityModule = userManagementAndSecurityModule;
    }
    
    
    /*
     * To be removed when UI interface will adopt method with slaTemplateId
     * 
     */
    @Deprecated
    @Override
    public Response deployApplication(  String          applicationInstanceUriId, 
                                        String          paaSInstanceUriId,
                                        InputStream     archiveFileStream)  throws SOAException {
        
        String id = null;
        /* try {
            slaModule.startNegotiation(
                applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId),
                paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId)
                        ).getId().toString();
        } catch(Exception e) {
            
        }  */  
        return this.deployApplication(applicationInstanceUriId, paaSInstanceUriId, id, archiveFileStream);
    
    }
    
    
    @Deprecated
    @Override
    public Response deployApplication(  String          applicationInstanceUriId, 
                                        String          paaSInstanceUriId,
                                        String          slaTemplateId,
                                        InputStream     archiveFileStream )  throws SOAException {
        
        ApplicationDeploymentParameters parameters;
        
        parameters = new ApplicationDeploymentParameters();
        parameters.setApplicationInstanceUriId(applicationInstanceUriId);
        parameters.setPaaSInstanceUriId(paaSInstanceUriId);
        parameters.setArchiveFileStream(archiveFileStream);
        parameters.setSlaTemplateId(slaTemplateId);
        parameters.setPenalties( null );
        
        return this.deployApplication( parameters );
    }
    
    @Override
    public Response deployApplication( ApplicationDeploymentParameters parameters )  throws SOAException {
        
        String          applicationInstanceUriId; 
        String          paaSInstanceUriId;
        InputStream     archiveFileStream;
        String          slaTemplateId;
        ApplicationInstance         applicationInstance;
        PaaSInstance                paaSInstance;
        File                        applicationArchiveFile;
        String                      deployedAppURL;
        String                      responseMessage;
        Response.Status             responseStatus;
        DeployApplicationParameters emsDeploymentParameters;

        applicationInstanceUriId    = parameters.getApplicationInstanceUriId();
        paaSInstanceUriId           = parameters.getPaaSInstanceUriId();
        archiveFileStream           = parameters.getArchiveFileStream();
        slaTemplateId               = parameters.getSlaTemplateId();
        logger.debug( "---- DEPLOYING WITH SLA TEMPLATE ID ---"+slaTemplateId);
        logger.debug(   "received applicationInstanceUriId: "+ applicationInstanceUriId +
                        ", paaSInstanceUriId: "              + paaSInstanceUriId            );
        
        
        if(slaTemplateId==null ||slaTemplateId.isEmpty()||slaTemplateId.equalsIgnoreCase("")){
        try {
           slaTemplateId= slaModule.startNegotiation(
                applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId),
                paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId)
                        ).getId().toString();
        logger.info( "---- Negotianiated again got SLA TEMPLATE ID ---"+slaTemplateId);
        } catch(Exception e) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        } 
        }
        
        try {
            applicationInstance     = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug(   "retrieved applicationInstance: "       + applicationInstance   );
        logger.debug(   "retrieved application name: "       + applicationInstance.getTitle()   );
        logger.debug(   "retrieved applicationInstance: "       + applicationInstance.getArchiveFileName()   );
        try {
            paaSInstance            = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug(   "retrived paaSInstance: "+paaSInstance);
        
        List<SoftwareComponentInstance> softwareComponents = applicationInstance.getSoftwareComponents();

        for (SoftwareComponentInstance softwareComponentInstance : softwareComponents) {
            if(softwareComponentInstance instanceof DBStorageComponentInstance) {                
                DBStorageComponentInstance dbStorageComponentInstance = (DBStorageComponentInstance) softwareComponentInstance;

                //check if there is a db not created yet
                if(dbStorageComponentInstance.getDeploymentLocationUriId()==null){
                    logger.debug("found DBStorageComponent having dbStorageComponentUriId: " + ((DBStorageComponentInstance)softwareComponentInstance).getUriId() + " that is not created yet");
                    return Response.status(Response.Status.PRECONDITION_FAILED).entity("The application dbs need to be created first.").type(MediaType.TEXT_PLAIN).build();
                }
            }
        }
        
        applicationArchiveFile  = null;
        
        try {        
            applicationArchiveFile      = this.getApplicationArchiveFile( applicationInstance, archiveFileStream );
            
            // skip file size and hash checks
            if (false) {
                this.checkArchiveSizeAndDigest(applicationArchiveFile, applicationInstance);
            }
            
            this.checkSlaValidity(applicationInstance, paaSInstance);
                       
            UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), paaSInstanceUriId);
            
            emsDeploymentParameters = new DeployApplicationParameters();
            emsDeploymentParameters.setApplicationInstance(applicationInstance);
            emsDeploymentParameters.setPaaSInstance(paaSInstance);
            emsDeploymentParameters.setApplicationArchive(applicationArchiveFile);
            emsDeploymentParameters.setPublicKey( userCredentialsForPaaS.getPublicKey() );
            emsDeploymentParameters.setSecretKey(userCredentialsForPaaS.getSecretKey() );
            emsDeploymentParameters.setAccountName( userCredentialsForPaaS.getAccountName() );
            emsDeploymentParameters.setSlaTemplateID(slaTemplateId);
            emsDeploymentParameters.setPenalties( parameters.getPenalties() );
            // prepare parameters
            logger.debug(   "Going to deploy application " + applicationInstanceUriId + " on PaaS " + paaSInstanceUriId );
//            deployedAppURL      =   executionManagementServiceModule.deployApplication( applicationArchiveFile, applicationInstance, paaSInstance, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), userCredentialsForPaaS.getAccountName(), slaTemplateId);
            logger.debug("emsDeploymentParameters.setSlaTemplateID"+emsDeploymentParameters.getSlaTemplateID());
            deployedAppURL      =   executionManagementServiceModule.deployApplication( emsDeploymentParameters );
            logger.debug(   "Deploy successfull; url of the deployed application: " + deployedAppURL);
            
            this.updateRepository(applicationInstance, paaSInstance, deployedAppURL);
            
            responseStatus      = Response.Status.ACCEPTED;
            responseMessage     =   "Application "                      + applicationInstance.getTitle() + 
                                    " successfully deployed on PaaS "   + paaSInstance.getProviderTitle() +
                                    "; URL: " + deployedAppURL;
            
        } catch (Cloud4SoaException ex) {
            logger.error("Failed to process attachments", ex);
            responseStatus  =   Response.Status.INTERNAL_SERVER_ERROR;
            responseMessage  =   ex.getError();
            throw new SOAException(responseStatus, responseMessage);
        } catch (Exception ex) {
            logger.error("Failed to process attachments", ex);
            responseStatus  =   Response.Status.BAD_REQUEST;
            responseMessage =   "Failed to deploy: " + ex.getClass().getName() + " - " + ex.getMessage() ;
            throw new SOAException(responseStatus, responseMessage);
        }
        applicationArchiveFile.delete();
        return Response.status( responseStatus ).entity( responseMessage ).type(MediaType.TEXT_PLAIN).build();

    }

        

    //ApplicationInstance applicationInstance, StartStopCommand startStopCommand
    @Override
    public Response startStopApplication(String applicationInstanceUriId,
            String startStopCommand) throws SOAException {
        
        logger.debug("received applicationInstanceUriId: "+applicationInstanceUriId);
        logger.debug("received startStopCommand: "+startStopCommand);
        
        logger.debug("call applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId)");
        logger.debug("applicationProfilesRepository instance:"+applicationProfilesRepository);

        ApplicationInstance applicationInstance;
        try {
            applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("executionManagementServiceModule.startStopApplication(applicationInstance, startStopCommandJsonObj)");
        UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), applicationInstance.getPaaSOfferingDeploymentUriId());
        try {           
            executionManagementServiceModule.startStopApplication(applicationInstance, startStopCommand, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), userCredentialsForPaaS.getAccountName());
        } catch (Cloud4SoaException ex) {
            logger.error("Failed to process attachments", ex);
            Status responseStatus  =   Response.Status.INTERNAL_SERVER_ERROR;
            String responseMessage  =   ex.getError();
            throw new SOAException(responseStatus, responseMessage);
        }

        //Update the Status of the ontology...
        if(startStopCommand.equals("stop"))
            applicationInstance.setStatus(StatusType.Stopped);
        else if(startStopCommand.equals("start"))
            applicationInstance.setStatus(StatusType.Deployed);
        try {
            applicationProfilesRepository.updateApplicationInstance(applicationInstance);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        
        return Response.status(Response.Status.ACCEPTED).entity("StartStopCommand processed successfully.").type(MediaType.TEXT_PLAIN).build();
    }

	

    @Override
    public List<ApplicationInstance> retrieveAllDeployedApplicationProfiles(
                    String userInstanceUriId) throws SOAException{
        List<ApplicationInstance> retrievedDeployedApplicationInstances = new ArrayList<ApplicationInstance>();
        logger.debug("received userInstanceUriId: "+userInstanceUriId);

        logger.debug("call applicationProfilesRepository.retrieveAllApplicationProfile(userInstanceUriId)");

        List<ApplicationInstance> applicationInstances;
        try {
            applicationInstances = applicationProfilesRepository.retrieveAllApplicationProfile(userInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        logger.debug("filtering applicationInstances: "+applicationInstances);

        for (ApplicationInstance applicationInstance : applicationInstances) {
            if(applicationInstance.getStatus()!=null){
//                if(applicationInstance.getStatus().equals(StatusType.Deployed) || applicationInstance.getStatus().equals(StatusType.Stopped)){
                    retrievedDeployedApplicationInstances.add(applicationInstance);
                    logger.debug("found deployed applicationInstance: "+applicationInstance);
//                }
            }
        }

        logger.debug("retrievedDeployedApplicationInstances: "+retrievedDeployedApplicationInstances);

        return retrievedDeployedApplicationInstances;
    }

    @Override
    public Response removeApplication(String applicationInstanceUriId) throws SOAException {
        logger.debug("received applicationInstanceUriId: "+applicationInstanceUriId);

        logger.debug("call applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId)");

        ApplicationInstance applicationInstance;
        try {
            applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        logger.debug("executionManagementServiceModule.startStopApplication(applicationInstance, startStopCommandJsonObj)");

        //check if the application is deployed
        if(applicationInstance.getPaaSOfferingDeploymentName()==null){
            Status responseStatus  =   Response.Status.BAD_REQUEST;
            String responseMessage =   "[Application " + applicationInstanceUriId + "] The selected application is not deployed yet." ;
            logger.error(responseMessage);
            throw new SOAException(responseStatus, responseMessage);
//                return Response.status(Response.Status.BAD_REQUEST).entity("The selected application is not deployed yet.").type(MediaType.TEXT_PLAIN).build();
        }
        UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), applicationInstance.getPaaSOfferingDeploymentUriId());
        try {
            executionManagementServiceModule.unDeployApplication(applicationInstance, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), userCredentialsForPaaS.getAccountName());
        } catch (Cloud4SoaException ex) {
            logger.error("Failed to process attachments", ex);
            Status responseStatus  =   Response.Status.INTERNAL_SERVER_ERROR;
            String responseMessage  =   ex.getError();
            throw new SOAException(responseStatus, responseMessage);
        }

        //Update the Status of the ontology...
        applicationInstance.getApplication().setStatus(null);
        applicationInstance.getApplication().setDeployment(null);
        try {
            //            applicationInstance.setPaaSOfferingDeployment(null);
            //            applicationInstance.setDeploymentIP(null);
                    applicationProfilesRepository.updateApplicationInstance(applicationInstance);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return Response.status(Response.Status.ACCEPTED).entity("RemoveApplication processed successfully.").type(MediaType.TEXT_PLAIN).build();
    }

    @Override
    public Response createDatabase(String applicationInstanceUriId, String paaSInstanceUriId, String dbStorageComponentUriId) throws SOAException {
        logger.info("received applicationInstanceUriId: " + applicationInstanceUriId);
        logger.info("received paaSInstanceUriId: " + paaSInstanceUriId);
        logger.info("received dbStorageComponentUriId: " + dbStorageComponentUriId);

        logger.info("call applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId)");

        ApplicationInstance applicationInstance;
        try {
            applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        logger.debug("retrieved applicationInstance: " + applicationInstance);
        PaaSInstance paaSInstance;
        try {
            paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("retrived paaSInstance: " + paaSInstance);

        //check if the application is deployed
        if( !paaSInstanceUriId.equalsIgnoreCase("Heroku") && applicationInstance.getPaaSOfferingDeploymentName()!=null){
            return Response.status(Response.Status.BAD_REQUEST).entity("The selected application is already deployed.").type(MediaType.TEXT_PLAIN).build();
        }

        String dbUrl = null;
        String dbName = null;
        String dbUser = null;
        String dbPassword = null;
        String dbType = null;

        List<SoftwareComponentInstance> softwareComponents = applicationInstance.getSoftwareComponents();

        for (SoftwareComponentInstance softwareComponentInstance : softwareComponents) {
            String uriId = softwareComponentInstance.getUriId();
            if(softwareComponentInstance instanceof DBStorageComponentInstance && uriId.equals(dbStorageComponentUriId) ) {
                logger.debug("found DBStorageComponent having dbStorageComponentUriId: " + dbStorageComponentUriId);
                DBStorageComponentInstance dbStorageComponentInstance = (DBStorageComponentInstance) softwareComponentInstance;

                //check if the db is already created
                if(dbStorageComponentInstance.getDeploymentLocationUriId()!=null){
                    return Response.status(Response.Status.BAD_REQUEST).entity("The selected db is already created.").type(MediaType.TEXT_PLAIN).build();
                }

                dbName = dbStorageComponentInstance.getDbname();
                dbUser = dbStorageComponentInstance.getDbuser();
                dbPassword = dbStorageComponentInstance.getDbpassword();
                dbType = dbStorageComponentInstance.getRelatedhwcategoryInstance().getTitle();

                logger.debug("executionManagementServiceModule.createDatabase");
                
                UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), paaSInstance.getUriId());

                try {
                    DatabaseInfo dbInfo = executionManagementServiceModule.createDatabase(applicationInstance, paaSInstance, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), userCredentialsForPaaS.getAccountName(), dbName, dbUser, dbPassword, dbType);
                    
                    //FIXME Encoding DatabaseInfo into the dbURL to be parsed by GUI
                    dbUrl= "dbUrl=" + dbInfo.getDatabaseUrl() + 
                    	", host=" + dbInfo.getHost() + 
                    	", port=" + dbInfo.getPort() + 
                    	", dbName=" + dbInfo.getDatabaseName() +
                    	", dbUser=" + dbInfo.getUserName();
                    
                } catch (Cloud4SoaException ex) {
                    logger.error("Failed to process attachments", ex);
                    Status responseStatus  =   Response.Status.INTERNAL_SERVER_ERROR;
                    String responseMessage  =   ex.getError();
                    throw new SOAException(responseStatus, responseMessage);
                }
                logger.debug("created DB with url: " + dbUrl);
                DBDeployment dbDeployment = new DBDeployment();
                dbDeployment.setDeploymentLocation(paaSInstance.getPaaSOffering());
                ((DBStorageComponent)dbStorageComponentInstance.getSoftwareComponent()).setDBdeployment(dbDeployment);
                dbStorageComponentInstance.setUrl(dbUrl);
                break;
            }

        }

        //            hardwareComponent.setRelatedhwcategory(storageResource);
        //            hardwareComponentInstance = new HardwareComponentInstance(hardwareComponent);
        //            hardwareComponentInstance.setRelatedhwcategoryInstance(new StorageResourceInstance(storageResource));
                        
        //Update the Status of the ontology...
        try {      
            applicationProfilesRepository.updateApplicationInstance(applicationInstance);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        Response.Status responseStatus = Response.Status.CREATED;
        return Response.status( responseStatus ).entity( dbUrl ).type(MediaType.TEXT_PLAIN).build();
    }
    
    @Override
    public Response initializeDatabase(String applicationInstanceUriId, String paaSInstanceUriId, String dbStorageComponentUriId, InputStream is) throws SOAException{
        logger.debug("received applicationInstanceUriId: " + applicationInstanceUriId);
        logger.debug("received paaSInstanceUriId: " + paaSInstanceUriId);
        logger.debug("received dbStorageComponentUriId: " + dbStorageComponentUriId);

        logger.debug("call applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId)");

        ApplicationInstance applicationInstance;
        try {
            applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        logger.debug("retrieved applicationInstance: " + applicationInstance);
        PaaSInstance paaSInstance;
        try {
            paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("retrived paaSInstance: " + paaSInstance);

        //check if the application is deployed (The application should be stopped or not deployed during the db initialization)
        if(applicationInstance.getPaaSOfferingDeploymentName()!=null){
            if(applicationInstance.getStatus().compareTo(StatusType.Stopped)!=0)
                return Response.status(Response.Status.BAD_REQUEST).entity("The selected application is deployed and running.").type(MediaType.TEXT_PLAIN).build();
        }

        String dbUrl = null;
        String dbName = null;
        String dbUser = null;
        String dbPassword = null;
        String dbType = null;

        List<SoftwareComponentInstance> softwareComponents = applicationInstance.getSoftwareComponents();

        for (SoftwareComponentInstance softwareComponentInstance : softwareComponents) {
            String uriId = softwareComponentInstance.getUriId();
            if(softwareComponentInstance instanceof DBStorageComponentInstance && uriId.equals(dbStorageComponentUriId) ) {
                logger.debug("found DBStorageComponent having dbStorageComponentUriId: " + dbStorageComponentUriId);
                DBStorageComponentInstance dbStorageComponentInstance = (DBStorageComponentInstance) softwareComponentInstance;

                //check if the db is already created
                if(dbStorageComponentInstance.getDeploymentLocationUriId()==null){
                    return Response.status(Response.Status.BAD_REQUEST).entity("The selected db has to be created first.").type(MediaType.TEXT_PLAIN).build();
                }
                
                dbName = dbStorageComponentInstance.getDbname();
                dbUser = dbStorageComponentInstance.getDbuser();
                dbPassword = dbStorageComponentInstance.getDbpassword();
                dbType = dbStorageComponentInstance.getRelatedhwcategoryInstance().getTitle();
                
                UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), paaSInstance.getUriId());
                File dumpFile = getTempFile(dbName+"_"+"dump", ".sql", is);
                try {
                    executionManagementServiceModule.restoreDataBase(applicationInstance, paaSInstance, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), dbName, dbUser, dbPassword, dbType, dumpFile.getAbsolutePath());
                } catch (Cloud4SoaException ex) {
                    String error = "Impossible to initialize database: " + dbName + " cause: " + ex.getMessage();
                    logger.error(error);
                    applicationInstance.setApplicationStatus(new eu.cloud4soa.api.datamodel.semantic.app.Error());
                    throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "Error in initializing the DB: " + dbName);
                }
            }        
        }
        
        Response.Status responseStatus = Response.Status.OK;
        return Response.status( responseStatus ).entity( "database: " + dbName + "initialized succesfully" ).type(MediaType.TEXT_PLAIN).build();
    }
    
    @Override
    public InputStream dumpDatabase(String applicationInstanceUriId, String paaSInstanceUriId, String dbStorageComponentUriId) throws SOAException {
        InputStream inputStream = null;
        logger.debug("received applicationInstanceUriId: " + applicationInstanceUriId);
        logger.debug("received paaSInstanceUriId: " + paaSInstanceUriId);
        logger.debug("received dbStorageComponentUriId: " + dbStorageComponentUriId);

        logger.debug("call applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId)");

        ApplicationInstance applicationInstance;
        try {
            applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        logger.debug("retrieved applicationInstance: " + applicationInstance);
        PaaSInstance paaSInstance;
        try {
            paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("retrived paaSInstance: " + paaSInstance);

        //check if the application is deployed (The application should be stopped or not deployed during the db initialization)
        if(applicationInstance.getPaaSOfferingDeploymentName()!=null){
            if(applicationInstance.getStatus().compareTo(StatusType.Stopped)!=0)
                throw new SOAException(Response.Status.BAD_REQUEST, "The selected application is deployed and running.");
        }

        String dbUrl = null;
        String dbName = null;
        String dbUser = null;
        String dbPassword = null;
        String dbType = null;

        List<SoftwareComponentInstance> softwareComponents = applicationInstance.getSoftwareComponents();

        for (SoftwareComponentInstance softwareComponentInstance : softwareComponents) {
            String uriId = softwareComponentInstance.getUriId();
            if(softwareComponentInstance instanceof DBStorageComponentInstance && uriId.equals(dbStorageComponentUriId) ) {
                logger.debug("found DBStorageComponent having dbStorageComponentUriId: " + dbStorageComponentUriId);
                DBStorageComponentInstance dbStorageComponentInstance = (DBStorageComponentInstance) softwareComponentInstance;

                //check if the db is already created
                if(dbStorageComponentInstance.getDeploymentLocationUriId()==null){
                    throw new SOAException(Response.Status.BAD_REQUEST, "The selected db has to be created first.");
                }
                
                dbName = dbStorageComponentInstance.getDbname();
                dbUser = dbStorageComponentInstance.getDbuser();
                dbPassword = dbStorageComponentInstance.getDbpassword();
                dbType = dbStorageComponentInstance.getRelatedhwcategoryInstance().getTitle();
                
                UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), paaSInstance.getUriId());
                File dumpFile;
                try {
                    dumpFile = File.createTempFile(dbName+"_"+"dump", ".sql");
                } catch (IOException ex) {
                    String error = "Impossible to dump database: " + dbName + " since the temp file cannot be created - cause: " + ex.getMessage();
                    logger.error(error);
                    throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "Error in dumping the DB: " + dbName);
                }
                try {
                    executionManagementServiceModule.downloadDataBase(applicationInstance, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), dbName, dbUser, dbPassword, dbType, dumpFile.getAbsolutePath());
                    inputStream = FileUtils.openInputStream(dumpFile);
                } catch (IOException ex) {
                    String error = "Impossible to dump database: " + dbName + " since the temp file cannot be read - cause: " + ex.getMessage();
                    logger.error(error);
                    throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "Error in dumping the DB: " + dbName);
                } catch (Cloud4SoaException ex) {
                    String error = "Impossible to dump database: " + dbName + " cause: " + ex.getMessage();
                    logger.error(error);
                    throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "Error in dumping the DB: " + dbName);
                }
            }        
        }
        

        return inputStream;
    }
    
    private File getApplicationArchiveFile( ApplicationInstance appInstance, InputStream is ) throws SOAException {
        String              fileName;
        String              fileExtension;
        File                file;
        
        file = null;
        fileName                = appInstance.getArchiveFileName();
        fileExtension           = appInstance.getArchiveExtensionName();

        logger.debug(   "applicationInstance fileName: " + fileName + "." + fileExtension);
        
        if ( fileName==null) {
            fileName="unspecifiedArchiveName";
        }
        if ( fileExtension==null ) {
            fileExtension="war";
        }
        
        file = getTempFile( fileName, fileExtension, is );
        
        return file;
    }

    private ApplicationInstance getApplicationInstance(String applicationInstanceUriId) throws SOAException {
        ApplicationInstance applicationInstance;
        try {
            applicationInstance     = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new SOAException(Response.Status.BAD_REQUEST, "Wrong parameter applicationInstanceUriId: "+ex.getMessage());
        }
        logger.debug(   "retrieved applicationInstance: "       + applicationInstance   );
        return applicationInstance;
    }

    private PaaSInstance getPaaSInstance(String paaSInstanceUriId) throws SOAException {
        PaaSInstance paaSInstance = null;
        try {
            paaSInstance            = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new SOAException(Response.Status.BAD_REQUEST, "Wrong parameter paaSInstanceUriId: "+ex.getMessage());
        }
        logger.debug("retrived paaSInstance: " + paaSInstance);
        return paaSInstance;
    }
   
    private File getTempFile( String fileName, String fileExtension, InputStream is ) throws SOAException {
        File                file;
        OutputStream        out;
        
        file = null;

        try {
            file    = File.createTempFile(fileName, fileExtension);
            out     = new FileOutputStream(file);
            IOUtils.copy(is, out);
            out.close();
            is.close();
            logger.debug("File is created: "    + file.getAbsolutePath());
        
        } catch (FileNotFoundException ex) {
            logger.debug("Failed to process attachments", ex);
            throw new SOAException( Response.Status.BAD_REQUEST, 
                    "File " + fileName + "." + fileExtension + " not found." );
            
        } catch (IOException ex) {
            logger.debug("Failed to process attachments", ex);
            throw new SOAException(Response.Status.BAD_REQUEST, "Failed to process attachments. Reason : " + ex.getMessage());
        }
        
        return file;
    }
    
    private void checkArchiveSizeAndDigest( File fileToCheck, ApplicationInstance appInstance) throws SOAException {
    
        if ( fileToCheck.length() != appInstance.getSizeQuantity() ) {
            fileToCheck.delete();
            throw new SOAException(Response.Status.BAD_REQUEST, 
                    "Failed to process attachments. Reason : different file size" + fileToCheck.length() + " != " + appInstance.getSizeQuantity() );
        }

        if ( this.getFileDigest(fileToCheck).equals( appInstance.getDigest() ) ) {
            fileToCheck.delete();
            throw new SOAException( Response.Status.BAD_REQUEST, 
                    "Failed to process attachments. Reason : different file digest" + this.getFileDigest(fileToCheck) + " != " + appInstance.getDigest() );
        }
    }        
    
    
    
    private String getFileDigest( File file) throws SOAException {
        
        InputStream     fileStream;
        String          digest;
        
        try {
            fileStream  = new FileInputStream( file );
            digest      = DigestUtils.sha256Hex( fileStream );
        } catch (IOException ie) {
            throw new SOAException(Response.Status.BAD_REQUEST, "Failed to process attachments. Reason : " + ie.getMessage());
        }
        
        return digest;
    }
    
    
    
    
    protected void checkSlaValidity( ApplicationInstance appInstance, PaaSInstance paasInstance) throws SOAException {
        SlaContractValidity         slaContractValidity;
        boolean                     isSlaValid;
        
        slaContractValidity         = slaModule.checkContractValidity( appInstance, paasInstance);
        logger.debug("obtained SlaContractValidity: "+slaContractValidity);
        //check slaContractValidity: at the moment, always valid
        isSlaValid                  = true;
        if ( !isSlaValid ) {
            throw new SOAException( Response.Status.BAD_REQUEST, "SLA contract not valid");
        }
    }
    
    
    protected void updateRepository( ApplicationInstance appInstance, PaaSInstance paasInstance, String deployedAppURL) throws SOAException {
        logger.debug("Going to update the repository after the deploy");
        appInstance.setStatus( StatusType.Deployed );
        appInstance.setPaaSOfferingDeployment(paasInstance);
        appInstance.setDeploymentIP(deployedAppURL);
        try {
            applicationProfilesRepository.updateApplicationInstance(appInstance);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    
    private void ifConditionThrowsException(boolean condition, Response.Status responseStatus, String errorMessage) throws SOAException{
        if(condition){
            logger.debug(errorMessage);
            throw new SOAException(responseStatus, errorMessage);
        }
    }
    
    
    @Override
    public GitRepoInfo initializeGitProxy(String userInstanceUriId, String paaSInstanceUriId, String applicationInstanceUriId ) throws SOAException {
        GitRepoInfo gitRepoInfo = null;
        boolean exists = false;
        String message = "Received the request for creating a new app repo on the provider: " 
                + paaSInstanceUriId + " for the application: " + applicationInstanceUriId;
        logger.info( message );
        
        ApplicationInstance applicationInstance = getApplicationInstance(applicationInstanceUriId);
        PaaSInstance paaSInstance = getPaaSInstance(paaSInstanceUriId);
        try{
            gitRepoInfo = getGitRepoInfos(userInstanceUriId, paaSInstanceUriId, applicationInstanceUriId);
            exists = true;
        } catch (SOAException ex){
            //createGitRepo is the connection to ems
            gitRepoInfo = createGitRepo(userInstanceUriId, paaSInstance, applicationInstance);
        }

        String repositoryName = gitRepoInfo.getRepositoryName();
        String gitUrl = gitRepoInfo.getUrl();
        String userId = gitRepoInfo.getUserId();
        String appUrl = gitRepoInfo.getApplicationUrl();
        
        logger.info("Git repository created for application: " + applicationInstanceUriId +" on the provider: " 
                + paaSInstanceUriId +" - application Url: "+appUrl);
        
        if(!exists)
            registerGitRepository(applicationInstanceUriId, userInstanceUriId, paaSInstanceUriId, repositoryName, gitUrl);
        
        String proxyName = "proxy" + userId + applicationInstance.getTitle().replaceAll(" ", "").trim().toLowerCase()+paaSInstanceUriId;
        
        if(!exists)
            registerGitProxy(userInstanceUriId, proxyName);
        
        //Obtain gitId and && proxyId required by the binding function
        
        String gitRepoId = gitServices.getRepoId(repositoryName, gitUrl);
        String proxyId = gitServices.getProxyId(proxyName);
        
        bindProxyToGit(userInstanceUriId, proxyId, gitRepoId);
        
        //Call deployment for CloudControl then store to database and start monitoring
        /*
        String message2 = "Updating the deployme(if CC) and storing into relational db->provider: " 
                + paaSInstance.getUriId() + " for the application: " + applicationInstance.getUriId();
        logger.info( message2 );
        
        UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), paaSInstance.getUriId());
            
        logger.debug(   "Going to deploy through git the application " + applicationInstance.getUriId() + " on PaaS " + paaSInstance.getUriId() );

            try {
            gitRepoInfo = executionManagementServiceModule.deployThroughGitFinalStep(gitRepoInfo, applicationInstance, paaSInstance, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), userCredentialsForPaaS.getAccountName());
            } catch (Cloud4SoaException ex) {
            String error = "Impossible to create a git repository for the application: " + applicationInstance.getTitle() + " on the paas: "+paaSInstance.getTitle()+" cause: " + ex.getMessage();
            logger.error(error);
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }*/
        
        
        
        return gitRepoInfo;        
    }

    @Override
    public GitRepoInfo createGitRepo(String userInstanceUriId, String paaSInstanceUriId, String applicationInstanceUriId ) throws SOAException {

        ApplicationInstance applicationInstance = getApplicationInstance(applicationInstanceUriId);
        PaaSInstance paaSInstance = getPaaSInstance(paaSInstanceUriId);
        
        GitRepoInfo gitRepoInfo = createGitRepo(userInstanceUriId, paaSInstance, applicationInstance);
        
        return gitRepoInfo;        
    }

    public GitRepoInfo createGitRepo(String userInstanceUriId, PaaSInstance paaSInstance, ApplicationInstance applicationInstance ) throws SOAException {

        String message = "Received the request for creating a new app repo on the provider: " 
                + paaSInstance.getUriId() + " for the application: " + applicationInstance.getUriId();
        logger.info( message );
        
        UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), paaSInstance.getUriId());
            
        logger.debug(   "Going to deploy through git the application " + applicationInstance.getUriId() + " on PaaS " + paaSInstance.getUriId() );

        GitRepoInfo gitRepoInfo = null;
        try {
            gitRepoInfo = executionManagementServiceModule.deployThroughGit("", applicationInstance, paaSInstance, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), userCredentialsForPaaS.getAccountName());
        } catch (Cloud4SoaException ex) {
            String error = "Impossible to create a git repository for the application: " + applicationInstance.getTitle() + " on the paas: "+paaSInstance.getTitle()+" cause: " + ex.getMessage();
            logger.error(error);
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return gitRepoInfo;        
    }

    
    public GitRepoInfo UpdateGitRepo(GitRepoInfo gitRepoInfo,String userInstanceUriId, PaaSInstance paaSInstance, ApplicationInstance applicationInstance ) throws SOAException {

        String message = "Received the request for creating a new app repo on the provider: " 
                + paaSInstance.getUriId() + " for the application: " + applicationInstance.getUriId();
        logger.info( message );
        
        UserPaaSCredentials userCredentialsForPaaS = userManagementAndSecurityModule.readUserCredentialsForPaaS(applicationInstance.getOwnerUriId(), paaSInstance.getUriId());
            
        logger.debug(   "Going to deploy through git the application " + applicationInstance.getUriId() + " on PaaS " + paaSInstance.getUriId() );

        GitRepoInfo retGitRepoInfo = null;
        try {
            retGitRepoInfo = executionManagementServiceModule.deployThroughGitFinalStep(gitRepoInfo, applicationInstance, paaSInstance, userCredentialsForPaaS.getPublicKey(), userCredentialsForPaaS.getSecretKey(), userCredentialsForPaaS.getAccountName());
        } catch (Cloud4SoaException ex) {
            String error = "Impossible to create a git repository for the application: " + applicationInstance.getTitle() + " on the paas: "+paaSInstance.getTitle()+" cause: " + ex.getMessage();
            logger.error(error);
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return retGitRepoInfo;        
    }
    
    @Override
    public Response commitGitDeploy(String userInstanceUriId, String paaSInstanceUriId, String applicationInstanceUriId) throws SOAException {
        ApplicationInstance applicationInstance;
        PaaSInstance paaSInstance;
        String responseMessage;
        Response.Status responseStatus;

        logger.debug("commitGitDeploy for applicationInstanceUriId: " + applicationInstanceUriId
                + ", paaSInstanceUriId: " + paaSInstanceUriId);
        try {
            applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("retrieved applicationInstance: " + applicationInstance);
        try {
            paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("retrived paaSInstance: " + paaSInstance);

        //CC FIX: Call UpdateGitRepo
        GitRepoInfo gitRepoInfo = null;

        try {
            gitRepoInfo = getGitRepoInfos(userInstanceUriId, paaSInstanceUriId, applicationInstanceUriId);
            UpdateGitRepo(gitRepoInfo, userInstanceUriId, paaSInstance, applicationInstance);

        } catch (SOAException ex) {
            //createGitRepo is the connection to ems
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        ////END FIX
        
        try {

            logger.debug("Going to deploy application " + applicationInstanceUriId + " on PaaS " + paaSInstanceUriId);

            String deployedAppURL = gitServices.getApplicationUrl(applicationInstanceUriId,paaSInstanceUriId);

            this.updateRepository(applicationInstance, paaSInstance, deployedAppURL);

            responseStatus = Response.Status.ACCEPTED;
            responseMessage = "Application " + applicationInstance.getTitle()
                    + " successfully deployed on PaaS " + paaSInstance.getProviderTitle()
                    + "; URL: " + deployedAppURL;

            return Response.status(responseStatus).entity(responseMessage).type(MediaType.TEXT_PLAIN).build();

        } catch (SOAException ex) {
            responseStatus = Response.Status.INTERNAL_SERVER_ERROR;
            responseMessage = "Failed to commit the git deploy: " + ex.getClass().getName() + " - " + ex.getMessage();
            throw new SOAException(responseStatus, responseMessage);
        }

    }
    
    
    @Override
    public Response deleteRepo(String c4sUserId, String repoId) throws SOAException {
        throw new UnsupportedOperationException("Not supported yet. Received: " +
                " c4sUserId = " + c4sUserId + ", repoId = " + repoId);
    }

    
    
    @Override
    // < repoId, <gitUrl, repoName>>
    public Map<Long, Entry<String, String>> getGitRepos( String userInstanceUriId ) throws SOAException {
        Map<Long, Entry<String, String>> map = new HashMap<Long, Entry<String, String>>();
        List<GitRepo> gitRepositories = gitServices.getGitRepositoriesForUser(userInstanceUriId);
        for (GitRepo gitRepo : gitRepositories) {
            Long id = gitRepo.getId();
            String giturl = gitRepo.getGiturl();
            String gitRepoName = gitRepo.getGitrepo();
            map.put(id, new SimpleEntry<String, String>(giturl, gitRepoName));
        }
        return map;
    }

    @Override
    // < proxyId, <repoName, repoId>>
    public Map<Long, Entry<String, Long>> getGitProxies(String userInstanceUriId) throws SOAException {
        Map<Long, Entry<String, Long>> map = new HashMap<Long, Entry<String, Long>>();
        List<GitProxy> gitProxiesForUser = gitServices.getGitProxiesForUser(userInstanceUriId);
        for (GitProxy gitProxy : gitProxiesForUser) {
            Long proxyId = gitProxy.getId();
            GitRepo bindrepo = gitProxy.getRepo();
            Long binding = null;
            if (bindrepo != null) {
                binding = bindrepo.getId();
            }
            String proxyName = gitProxy.getProxyname();
            map.put(proxyId, new SimpleEntry<String, Long>(proxyName, binding));            
        }
        return map;                   
    }

    
    
    @Override
    public Response registerGitRepository(String applicationInstanceUriId, String userInstanceUriId, String paaSInstanceUriId, String repositoryName, String gitUrl) throws SOAException {
        
        logger.info("registerExistingGitRepo - userInstanceUriId = " + userInstanceUriId + ", paaSInstanceUriId = " + paaSInstanceUriId
                + ", repositoryName = " + repositoryName + ", gitUrl = " + gitUrl);

        String[] result = gitServices.registerGitRepository(applicationInstanceUriId, userInstanceUriId, gitUrl, repositoryName, paaSInstanceUriId);
        
        if ("0".equals(result[0])) {
            String message = "Received the request for register the repo: " + repositoryName + " having the url:" + gitUrl + " at the provider: "
                    + paaSInstanceUriId + " by the user: " + userInstanceUriId;
            return this.getResponse(Status.OK, message);
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, result[1]);
        }
    }
    
    @Override
    public Response registerGitProxy(String userInstanceUriId, String proxyName) throws SOAException {
        
        logger.info("registerGitProxy - userInstanceUriId = " + userInstanceUriId + ", "
                + ", proxyName = " + proxyName);

        String[] result = gitServices.registerGitProxy(userInstanceUriId, proxyName);
        
        if ("0".equals(result[0])) {
            String message = "Received the request for register the proxy entry: " + proxyName
                    + " by the user: " + userInstanceUriId;
            return this.getResponse(Status.OK, message);
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, result[1]);
        }
    }
    
    @Override
    public Response bindProxyToGit(String userInstanceUriId, String proxyId, String gitRepoId ) throws SOAException {
        
        logger.info("registerGitProxy - userInstanceUriId = " + userInstanceUriId + ", "
                + ", proxyid = " + proxyId + ", gitid = " + gitRepoId);

        String[] result = gitServices.bindProxyToGit(userInstanceUriId, proxyId, gitRepoId);
        
        if ("0".equals(result[0])) {
            String message = "Received the request for binding proxyid = " + proxyId + ", gitid = " + gitRepoId;
            return this.getResponse(Status.OK, message);
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, result[1]);
        }
    }

    
    
    @Override
    public Response relocateRepo(String c4sUserId, String newPaasUriId) throws SOAException {
        throw new UnsupportedOperationException("Not supported yet. Received: " +
                " c4sUserId = " + c4sUserId + ", paasUriId = " + newPaasUriId );
    }                

    @Override
    public GitRepoInfo getGitRepoInfos(String userInstanceUriId, String paaSInstanceUriId, String applicationInstanceUriId) throws SOAException{
        GitRepoInfo gitRepoInfo = gitServices.getGitProxyInfos(userInstanceUriId, paaSInstanceUriId, applicationInstanceUriId);
        return gitRepoInfo;
    }

    
    protected Response getResponse( Response.Status status, String message ) {
        return Response.status( status).entity( message).type( MediaType.TEXT_PLAIN).build();
    }

    
    // Git keys management - CRUD operations ----------------------------/

    @Override
    public String getC4SOAPublicKey() {
        String[] c4SOAPublicKey = gitServices.getC4SOAPublicKey();
        return c4SOAPublicKey[1];
    }

    @Override
    public Response registerPublicKeyForUser(String userInstanceUriId, String publicKey) throws SOAException {
        String[] result = gitServices.registerPublicKeyForUser(userInstanceUriId, publicKey);
        if("0".equals(result[0])){
            Response.Status responseStatus = Response.Status.OK;
            return Response.status( responseStatus ).entity( "Public key succesfully registered" ).type(MediaType.TEXT_PLAIN).build();
        }
        else
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, result[1]);

    }

    @Override
    public StringList getPublicKeysForUser(String userInstanceUriId) throws SOAException {
        List<String> ret = new ArrayList<String>();
        List<PubKey> pubkeys = gitServices.getPublicKeysForUser(userInstanceUriId);
        for (int i = 0; i < pubkeys.size(); i++) {
            PubKey pubKey = pubkeys.get(i);
            ret.add(pubKey.getPubkey());
        }
        StringList stringList = new StringList();
        stringList.setList(ret);
        return stringList;
    }

    @Override
    public Response deletePublicKeyFromUser(String userInstanceUriId, String publicKey) throws SOAException {
        String[] result = gitServices.deletePublicKeyFromUser(userInstanceUriId, publicKey);
        if ("0".equals(result[0])) {
            Response.Status responseStatus = Response.Status.OK;
            return Response.status(responseStatus).entity("Public key succesfully deleted").type(MediaType.TEXT_PLAIN).build();
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, result[1]);
        }
    }

    
    
    @Override
    public SlaContract getSLAContract(String applicationInstanceUriId) throws SOAException {
        ApplicationInstance application;
        eu.cloud4soa.api.datamodel.semantic.app.ApplicationDeployment applicationDeployment;
        SlaContract slaContract;
        
        slaContract = null;
        try {
            application = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
            applicationDeployment = application.getApplication().getDeployment();
            if ( applicationDeployment == null ) {
                throw  new SOAException(Status.PRECONDITION_FAILED, "The application doesn't look to be deployed on any paas; the sla contract cannot be retrieved");
            } else if ( applicationDeployment.getSLAcontractID() == null ) {
                throw  new SOAException(Status.PRECONDITION_FAILED, "No slaContract ID associated to the deployed application");
            } else {
                slaModule.getSLAContract( applicationDeployment.getSLAcontractID()  );
            }
        } catch (RepositoryException ex) {
            throw  new SOAException(Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        
        return slaContract;
    }
    
    
    
    
    
    //-------------------------------------------------------------------/
    
     /*
     * To Be removed!
     //------------------------------------------------------------------/ 
     */
    
    @Deprecated
    @Override
    public Response deployApplication(String applicationInstanceUriId, String paaSInstanceUriId, String publicKey, String secretKey, InputStream is) throws SOAException {
        return this.deployApplication(applicationInstanceUriId, paaSInstanceUriId, null, is);
    }

    @Deprecated
    @Override
    public Response startStopApplication(String applicationInstanceUriId, String startStopCommand, String publicKey, String secretKey) throws SOAException {
        return this.startStopApplication(applicationInstanceUriId, startStopCommand);
    }

    @Deprecated
    @Override
    public Response removeApplication(String applicationInstanceUriId, String publicKey, String secretKey) throws SOAException {
        return this.removeApplication(applicationInstanceUriId);
    }

    @Deprecated
    @Override
    public Response createDatabase(String applicationInstanceUriId, String paaSInstanceUriId, String dbStorageComponentUriId, String publicKey, String secretKey) throws SOAException {
        return this.createDatabase(applicationInstanceUriId, paaSInstanceUriId, dbStorageComponentUriId);
    }
    
    //------------------------------------------------------------------/
}
