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

package eu.cloud4soa.repository;

import com.viceversatech.rdfbeans.RDFBeanManager;
import com.viceversatech.rdfbeans.exceptions.RDFBeanException;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.datamodel.semantic.app.Application;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.repository.utils.RepositoryManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author vincenzo
 */
public class ApplicationProfilesRepository implements  eu.cloud4soa.api.repository.ApplicationProfilesRepository{
    final Logger logger = LoggerFactory.getLogger(ApplicationProfilesRepository.class);
//    private RepositoryManager repositoryManager = RepositoryManager.getInstance();
    private RepositoryManager repositoryManager = null;
    private UserProfilesRepository userProfilesRepository;
    
    @Required
    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    @Required
    public void setUserProfilesRepository(UserProfilesRepository userProfilesRepository) {
        this.userProfilesRepository = userProfilesRepository;
    }
           
    @Override
    public synchronized ApplicationInstance getApplicationInstance(String applicationInstanceUriId) throws RepositoryException, IllegalArgumentException{
        logger.debug("getApplicationInstance(String applicationInstanceUriId)");
//        throw new UnsupportedOperationException("Not supported yet.");
        ApplicationInstance retrievedApplicationInstance = null;
        boolean exception = false;
        if(applicationInstanceUriId!=null && !applicationInstanceUriId.isEmpty()){
//            do{
                try {
                    //Obtain the main RDFBean Class from the ontology
                    Application application = repositoryManager.getManager().get(applicationInstanceUriId, Application.class);
                    //Wrap the RDFBean cluster object
                    if(application==null)
                        throw new IllegalArgumentException("The requested resource does not exists.");
                    retrievedApplicationInstance = new ApplicationInstance(application);
                    logger.debug("retrieved applicationInstance: "+retrievedApplicationInstance);
//                    if(exception){
//                        logger.error("Now Ok!");
//                        logger.error("DeploymentName: "+retrievedApplicationInstance.getPaaSOfferingDeploymentName());
//                        exception = false;
//                    }
                } catch (RDFBeanException ex) {
                    logger.error("RDFBeanException: "+ex);
                    throw new RepositoryException("ApplicationProfilesRepository", ex);
                }
//            } while (exception);
        }
        else{
            //Query by-example exploiting information obtained from the Model...
        }
        return retrievedApplicationInstance;
    }

    @Override
    public synchronized void storeApplicationInstance(ApplicationInstance applicationInstance) throws RepositoryException{
//        logger.error("UnsupportedOperationException("+"Not supported yet."+")");
        //        throw new UnsupportedOperationException("Not supported yet.");

        logger.debug("storeApplicationInstance(ApplicationInstance applicationInstance)");
        //store applicationInstance
//        if(applicationInstance.getUriId()==null){
            try {
                //Obtain the main RDFBean Class from the wrapper
                Application application = applicationInstance.getApplication();
                if(applicationInstance.getUriId()==null)
                    application.setUriId(getUUID());
                setUriIds(application);
                repositoryManager.getManager().add(application);
            } catch (RDFBeanException ex) {
                logger.error("RDFBeanException: "+ex);
                throw new RepositoryException("ApplicationProfilesRepository", ex);
            }
            logger.debug("ApplicationInstance stored");
//        }
    }

    @Override
    public synchronized void updateApplicationInstance(ApplicationInstance applicationInstance) throws RepositoryException{
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("updateApplicationInstance(ApplicationInstance applicationInstance)");
        //update applicationInstance
        try {
            //Obtain the main RDFBean Class from the wrapper
            Application application = applicationInstance.getApplication();
            setUriIds(application);
            repositoryManager.getManager().update(application);
        } catch (RDFBeanException ex) {
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("ApplicationProfilesRepository", ex);
        }
        logger.debug("ApplicationInstance updated");
    }

    @Override
    public List<ApplicationInstance> retrieveAllApplicationProfile(String userInstanceUserId) throws RepositoryException{
//        throw new UnsupportedOperationException("Not supported yet.");
//        List<ApplicationInstance> applicationInstanceList = new ArrayList<ApplicationInstance>();
        
        DeveloperInstance userInstance = (DeveloperInstance) userProfilesRepository.getUserInstance(userInstanceUserId);
        //        List<ApplicationInstance> retrievedApplicationInstance = userInstance.getApplications();
        ClosableIterator<Application> all = null;
        try {
            all = repositoryManager.getManager().getAll(Application.class);
        } catch (RDFBeanException ex) {
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("ApplicationProfilesRepository", ex);
        }
        List<ApplicationInstance> retrievedApplicationInstance = new ArrayList<ApplicationInstance>();
        while (all.hasNext()) {
            Application application = all.next();
            if (application.getOwner() != null && application.getOwner().getUriId()!=null) {
                logger.debug("application.getOwner().getUriId() ? userInstanceUserId:"+ application.getOwner().getUriId() + " " +userInstanceUserId);
                if(application.getOwner().getUriId().equals(userInstanceUserId))
                    retrievedApplicationInstance.add(new ApplicationInstance(application));
            }
        }
        
//        if(applicationInstanceUriId!=null && !applicationInstanceUriId.isEmpty()){
//            try {
//                //Obtain the main RDFBean Class from the ontology
//                Application application = repositoryManager.getManager().get(applicationInstanceUriId, Application.class);
//                //Wrap the RDFBean cluster object
//                retrievedApplicationInstance = new ApplicationInstance(application);
//                logger.debug("retrieved applicationInstance: "+retrievedApplicationInstance);
//            } catch (RDFBeanException ex) {
//                logger.error("RDFBeanException: "+ex);
//            }
//        }
//        else{
//            //Query by-example exploiting information obtained from the Model...
//        }
        return retrievedApplicationInstance;
    }
    
    
    @Override
    public List<ApplicationInstance> retrieveAllApplicationProfileDeployedOnPaaS(String paasInstanceUserId) throws RepositoryException{
        
        List<ApplicationInstance>   applicationInstanceList;
        String                      appsDeployedOnAPaaSQuery;
        ClosableIterator<QueryRow>  uriIdsIterator;
        RDFBeanManager              manager;
        
        applicationInstanceList     = new ArrayList<ApplicationInstance>();
        appsDeployedOnAPaaSQuery    = 
                  "PREFIX essential-metamodel:<http://www.enterprise-architecture.org/essential-metamodel.owl#> "
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX c4s-app-m:<http://www.cloud4soa.eu/v0.1/application-domain#> "
                + "PREFIX paas-m:  <http://www.cloud4soa.eu/v0.1/paas-model#> "
                + "SELECT DISTINCT ?app "
                + "WHERE { "
                + "    ?app rdf:type essential-metamodel:Application . "
                + "    ?app c4s-app-m:hasDeployment ?ad . "
                + "    ?ad c4s-app-m:deploying_location paas-m:" + paasInstanceUserId + "."
                + "}";        
        try {
            manager           = repositoryManager.getManager();
            uriIdsIterator  = manager.getModel().sparqlSelect( appsDeployedOnAPaaSQuery ).iterator();

            while (uriIdsIterator.hasNext()) {
                Resource resource;
                resource = uriIdsIterator.next().getValue("app").asResource();
                
                applicationInstanceList.add( new ApplicationInstance((Application)manager.get( resource )) );
            }
        } catch(RDFBeanException rdfbe) {
            throw new RepositoryException("ApplicationProfilesRepository", rdfbe);
        } catch(Exception e) {
            throw new RepositoryException("ApplicationProfilesRepository", e);
        }
        
        return applicationInstanceList;
    }

    
    
    @Override
    public void removeApplicationProfile(String applicationInstanceUriId) throws RepositoryException{
        logger.debug("removeApplicationProfile(String applicationInstanceUriId)");
        //remove applicationInstance
        try {
            repositoryManager.getManager().delete(applicationInstanceUriId, Application.class);
        } catch (RDFBeanException ex) {
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("ApplicationProfilesRepository", ex);
        }
        logger.debug("ApplicationInstance removed");
    }
    
    private void setUriIds(Application application){
        List<HardwareComponent> requiresResource = application.getRequiresResource();
        for (HardwareComponent hardwareComponent : requiresResource) {
            if(hardwareComponent.getUriId()==null || hardwareComponent.getUriId().isEmpty())
                hardwareComponent.setUriId(getUUID());
        }
        List<SoftwareComponent> requiresSoftwareComponent = application.getRequiresSoftwareComponent();
        for (SoftwareComponent softwareComponent : requiresSoftwareComponent) {
            if(softwareComponent.getUriId()==null || softwareComponent.getUriId().isEmpty())
                softwareComponent.setUriId(getUUID());
        }
        
        if(application.getDeployment()!=null && (application.getDeployment().getUriId()==null || application.getDeployment().getUriId().isEmpty()))
            application.getDeployment().setUriId(getUUID());
    }
    
    private String getUUID(){
        // Creating a random UUID (Universally unique identifier)
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
