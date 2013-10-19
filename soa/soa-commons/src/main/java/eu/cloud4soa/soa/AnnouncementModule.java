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

import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import java.util.List;

import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSUserInstance;
import eu.cloud4soa.api.datamodel.semantic.app.Application;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;
import eu.cloud4soa.api.datamodel.semantic.user.Developer;
import eu.cloud4soa.api.repository.PaaSOfferingProfilesRepository;
import eu.cloud4soa.api.repository.UserProfilesRepository;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.relational.datamodel.Paas;
import eu.cloud4soa.relational.persistence.PaasRepository;
import eu.cloud4soa.repository.utils.RepositoryManager;
import eu.cloud4soa.repository.utils.TemporaryRepositoryManager;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vincenzo
 * C4S Frontend required methods added by Yosu
 */
@Transactional
public class AnnouncementModule implements eu.cloud4soa.api.soa.AnnouncementModule {
    
    final Logger logger = LoggerFactory.getLogger(AnnouncementModule.class);
	
    private PaaSOfferingProfilesRepository paaSOfferingProfilesRepository;
    private UserProfilesRepository userProfilesRepository;
    //    private RepositoryManager repositoryManager = RepositoryManager.getInstance();
    private RepositoryManager repositoryManager = null;
    
    @Required
    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }
    
    @Autowired
    private PaasRepository paasrepository;
    
    @Required
    public void setPaaSOfferingProfilesRepository(PaaSOfferingProfilesRepository paaSOfferingProfilesRepository) {
        this.paaSOfferingProfilesRepository = paaSOfferingProfilesRepository;
    }
    
     /**
     * @param userProfilesRepository the userProfilesRepository to set
     */
    @Required
    public void setUserProfilesRepository(UserProfilesRepository userProfilesRepository) {
        this.userProfilesRepository = userProfilesRepository;
    }
    
    @Override
    public PaaSInstance getPaaSInstance(String paaSInstanceUriId) throws SOAException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("call paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId)");
        PaaSInstance retrievedpaaSInstance;
        try {
            retrievedpaaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("retrieved paaSInstance: "+retrievedpaaSInstance);
        return retrievedpaaSInstance;
    }

    @Override
    public String storePaaSInstance(PaaSInstance paaSInstance, String userInstanceUriId) throws SOAException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("received paaSInstance: "+paaSInstance);
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
        
        logger.debug("userProfilesRepository.getUserInstance(userInstanceUriId)");
        PaaSUserInstance paaSUserInstance;
        try {
            paaSUserInstance = (PaaSUserInstance)userProfilesRepository.getUserInstance(userInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(paaSUserInstance!=null){
            logger.debug("retrieved paaSUserInstance: "+paaSUserInstance);
            logger.debug("paaSProviderInstance associated: "+paaSUserInstance.getPaaSProviderInstance());
            
            logger.debug(""+paaSUserInstance.getPaaSProviderInstance().getPaaSProvider());
            
            logger.debug("set paaSProviderInstance to paaSInsatece");
            paaSInstance.setPaaSProviderInstance(paaSUserInstance.getPaaSProviderInstance());            
            logger.debug("paaSOfferingProfilesRepository.storePaaSInstance(paaSInstance)");
            String uriId;
            try {
                uriId = paaSOfferingProfilesRepository.storePaaSInstance(paaSInstance);
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }

            //Relational entries
            Paas paas = new Paas();
            paas.setName(paaSInstance.getProviderTitle());
            paas.setUrl(paaSInstance.getPaaSProviderInstance().getHomePage());           
            paas.setUriID(uriId);
            paasrepository.store(paas);            
            
            logger.debug("paaSInstance"+ paaSInstance +" stored with id: "+uriId);
            return uriId;
        }
        return null;
    }
    
    
    @Override
    public Response storeTurtlePaaSProfile(String paasProfile, String userInstanceUriId) throws SOAException {
        logger.debug("received paasProfile: "+paasProfile);
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
        
        //Check if the user is a PaaSUser
        PaaSUserInstance userInstance;                
        try {
            userInstance = (PaaSUserInstance) userProfilesRepository.getUserInstance(userInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(userInstance==null)
            return Response.status(Response.Status.BAD_REQUEST).entity("user is not a PaaSUser!").type(MediaType.TEXT_PLAIN).build();
        
        TemporaryRepositoryManager trm;
        try {
            trm = new TemporaryRepositoryManager(paasProfile);
        } catch (IOException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Model temporaryModel = trm.getManager().getModel();
        ClosableIterator<Statement> newStatements = temporaryModel.iterator();
        Model c4sModel = null;
        try{  
            String paasOfferingUriString = PaaSOffering.class.getAnnotation(RDFBean.class).value();
            String paasOfferingUriId = null;
            //here add the validations queries
            URI paasOfferingUri = new URIImpl( paasOfferingUriString, true);
            
            String query = 
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX paas-m: <http://www.cloud4soa.eu/v0.1/paas-model#>"
                + "PREFIX dcterms: <http://purl.org/dc/terms/>"
                + "SELECT ?object WHERE { ?object rdf:type "+paasOfferingUri.toSPARQL()+" .}";
            logger.debug(query);
            org.ontoware.rdf2go.model.QueryResultTable resultTable = temporaryModel.sparqlSelect(query);
            
            if(resultTable == null){
                String error = "An error happens when querying the model";
                logger.debug(error);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.TEXT_PLAIN).build();
            }
            Iterator<QueryRow> it = resultTable.iterator();
            if(it.hasNext()){
                QueryRow queryRow = it.next();
                paasOfferingUriId = queryRow.getValue("object").asURI().toString();
            }
            else{
                String error = "There is no paas offering resource inside the sent statements";
                logger.debug(error);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.TEXT_PLAIN).build();
            }
            if(it.hasNext()){
                String error = "The sent profile has more than one paas offering.";
                logger.debug(error);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.TEXT_PLAIN).build();
            }
            String paasOfferingInstanceUri = null;
            try {
                paasOfferingInstanceUri = PaaSOffering.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
            } catch (NoSuchMethodException ex) {
                logger.error(ex.getMessage());
            } catch (SecurityException ex) {
                logger.error(ex.getMessage());
            }
            String applicationUriIdWithoutPrefix = paasOfferingUriId;
            if(paasOfferingUriId.contains(paasOfferingInstanceUri.toString()))
                applicationUriIdWithoutPrefix = paasOfferingUriId.replace(paasOfferingInstanceUri.toString(), "");
            
            c4sModel = repositoryManager.getModel();
        
            logger.debug("c4sModel.addAll(newStatements)");
            c4sModel.setAutocommit(false);
            c4sModel.addAll(newStatements);
            c4sModel.commit();
            PaaSInstance paaSInstance;
            try {
                paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(applicationUriIdWithoutPrefix);
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
            paaSInstance.getPaaSOffering().setPaaSProvider(userInstance.getPaaSProviderInstance().getPaaSProvider());
            try {
                //Add paasOffering to the PaaSInstance!
    //            userInstance.addPaaSOffering...
                paaSOfferingProfilesRepository.updatePaaSInstance(paaSInstance, null);
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
            logger.debug("paaSInstanceUriId: "+paaSInstance.getUriId());
            
            //Relational entries
            Paas paas = new Paas();
            paas.setName(paaSInstance.getProviderTitle());
            paas.setUrl(paaSInstance.getURL());           
            paas.setUriID(paaSInstance.getUriId());
            paasrepository.store(paas);             
            
            return Response.status(Response.Status.CREATED).entity(paaSInstance.getUriId()).type(MediaType.TEXT_PLAIN).build();
            
        } catch (SecurityException ex) {
            logger.error(ex.getMessage());
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        } catch (ModelRuntimeException ex) {
            c4sModel.removeAll(newStatements);
            c4sModel.commit();
            logger.error(ex.getMessage());
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        } 
//        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Override
    public List<PaaSInstance> retrieveAllPaaSInstances(String userInstanceUriId) throws SOAException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
        
        logger.debug("paaSOfferingProfilesRepository.getAllAvailablePaaSInstances()");
        List<PaaSInstance> retrievedPaaSInstances; 
        try {
            retrievedPaaSInstances = paaSOfferingProfilesRepository.retrieveAllPaaSInstances(userInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        
        logger.debug("retrieved List<PaaSInstance>: "+retrievedPaaSInstances);
        return retrievedPaaSInstances;
    }

    @Override
    public void updatePaaSInstance(PaaSInstance paaSInstance) throws SOAException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("received paaSInstance: "+paaSInstance);
        
        logger.debug("paaSOfferingProfilesRepository.updatePaaSInstance(paaSInstance, null);");
        try {
            paaSOfferingProfilesRepository.updatePaaSInstance(paaSInstance, null);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        logger.debug("paaSInstance: "+ paaSInstance +" updated");
    }

    @Override
    public void removePaaSInstance(String paasInstanceUriId) throws SOAException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("received paasInstanceUriId: "+paasInstanceUriId);
        
        logger.debug("paaSOfferingProfilesRepository.updatePaaSInstance(paaSInstance, null);");
        try {
            paaSOfferingProfilesRepository.removePaaSInstance(paasInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        logger.debug("paaSInstance: "+ paasInstanceUriId +" removed");
    }


}
