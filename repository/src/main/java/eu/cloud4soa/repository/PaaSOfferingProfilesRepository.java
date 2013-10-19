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

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import com.viceversatech.rdfbeans.exceptions.RDFBeanException;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.frontend.PaaSRating;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
import eu.cloud4soa.api.datamodel.semantic.paas.Channel;
import eu.cloud4soa.api.datamodel.semantic.paas.Exception;
import eu.cloud4soa.api.datamodel.semantic.paas.Operation;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;
import eu.cloud4soa.api.datamodel.semantic.paas.Parameter;
import eu.cloud4soa.api.datamodel.semantic.user.PaaSUser;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.repository.utils.RepositoryManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vincenzo
 */
public class PaaSOfferingProfilesRepository implements eu.cloud4soa.api.repository.PaaSOfferingProfilesRepository{

    final Logger logger = LoggerFactory.getLogger(PaaSOfferingProfilesRepository.class);
    //    private RepositoryManager repositoryManager = RepositoryManager.getInstance();
    private RepositoryManager repositoryManager = null;

    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }
    
    @Override
    public List<PaaSInstance> getPaaSInstances(ApplicationInstance applicationInstance) {
        throw new UnsupportedOperationException("Not supported yet.");
//        List<PaaSInstance> retrievedUserInstances = new ArrayList<PaaSInstance>();
//        if(applicationInstanceUriId!=null && !applicationInstanceUriId.isEmpty()){
//            try {
//                
//                Set<PaaSOffering> results = new HashSet<PaaSOffering>();  
//                
//                URI predicate = model.createURI("http://xmlns.com/foaf/0.1/workplaceHomepage");
//                URI object = model.createURI("http://google.com");
//                ClosableIterator<Statement> ci = model.findStatements(Variable.ANY, predicate, object);
//                while (ci.hasNext()) {
//                  Resource r = ci.next().getSubject();
//                  results.add((Person)manager.get(r));
//                }
//                ci.close(); 
//
//                Developer developer = repositoryManager.getManager().get(userInstanceUriId, Developer.class);
//                if(developer != null)
//                    retrievedUserInstance = new DeveloperInstance(developer);
//
//                logger.debug("retrieved userInstance: "+retrievedUserInstance);
//            } catch (RDFBeanException ex) {
//                logger.error("RDFBeanException: "+ex);
//            }
//        }
//        else{
//            //Query by-example exploiting information obtained from the Model...
//            logger.error("Query by-example Not supported yet.");
//        }
//        return retrievedUserInstance;
    }

    @Override
    public PaaSInstance getPaaSInstance(String paaSInstanceUriId) throws RepositoryException, IllegalArgumentException{
//        throw new UnsupportedOperationException("Not supported yet.");
        PaaSInstance retrievedPaaSInstance = null;
        if(paaSInstanceUriId!=null && !paaSInstanceUriId.isEmpty()){
            try {
                PaaSOffering paaSOffering = repositoryManager.getManager().get(paaSInstanceUriId, PaaSOffering.class);
                if(paaSOffering != null)
                    retrievedPaaSInstance = new PaaSInstance(paaSOffering);
                else
                    throw new IllegalArgumentException("The requested resource does not exists.");
                logger.debug("retrieved paaSInstance: "+retrievedPaaSInstance);
            } catch (RDFBeanException ex) {
                logger.error("RDFBeanException: "+ex);
                throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
            }
        }
        else{
            //Query by-example exploiting information obtained from the Model...
            logger.error("Query by-example Not supported yet.");
            throw new RepositoryException("Query by-example Not supported yet.");
        }
        return retrievedPaaSInstance;
    }

    @Override
    public void updatePaaSInstance(PaaSInstance paaSInstance, PaaSRating paaSRating) throws RepositoryException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("updatePaaSInstance(PaaSInstance paaSInstance, PaaSRating paaSRating)");
        Resource existingResource = null;
        PaaSOffering paaSOffering = (PaaSOffering)((PaaSInstance)paaSInstance).getPaaSOffering();
        try {
            //check if the specific resource exists
            existingResource = repositoryManager.getManager().getResource(paaSOffering.getUriId(), PaaSOffering.class);
            if(existingResource!=null){
                setUriIds(paaSOffering);
                repositoryManager.getManager().update(paaSOffering);
//                repositoryManager.getManager().delete(paaSOffering.getUriId(), PaaSOffering.class);
//                setUriIds(paaSOffering);
//                repositoryManager.getManager().add(paaSOffering);
            }
            else throw new IllegalArgumentException("The specific resource does not exists");
        } catch (RDFBeanException ex) {
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
        }
    }

    @Override
    public String storePaaSInstance(PaaSInstance paaSInstance) throws RepositoryException{
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("storePaaSInstance(PaaSInstance paaSInstance)");
        if(paaSInstance.getUriId()==null){
            try {
                //Obtain the main RDFBean Class from the wrapper
                PaaSOffering paaSOffering = (PaaSOffering)((PaaSInstance)paaSInstance).getPaaSOffering();
                paaSOffering.setUriId(getUUID());
                setUriIds(paaSOffering);
                repositoryManager.getManager().add(paaSOffering);
                return paaSOffering.getUriId();
            } catch (RDFBeanException ex) {
                logger.error("RDFBeanException: "+ex);
                throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
            }
            
        }
        return null;
    }

    @Override
    public List<PaaSInstance> getAllAvailablePaaSInstances() throws RepositoryException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("getAllAvailablePaaSInstances()");
        List<PaaSInstance> retrievedPaaSInstances = new ArrayList<PaaSInstance>();
        try {
            ClosableIterator<PaaSOffering> retrievedPaaSOfferings = repositoryManager.getManager().getAll(PaaSOffering.class);
            while (retrievedPaaSOfferings.hasNext()) {
                PaaSOffering paaSOffering = retrievedPaaSOfferings.next();
                logger.debug("retrieved paaSOffering: "+paaSOffering);
                PaaSInstance paaSInstance = new PaaSInstance(paaSOffering);
                logger.debug("created paaSInstance: "+paaSInstance);
                retrievedPaaSInstances.add(paaSInstance);
            }
        } catch (RDFBeanException ex) {
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
        }
        logger.debug("retrievedPaaSInstances: "+retrievedPaaSInstances);
        return retrievedPaaSInstances;
    }

    @Override
    public void removePaaSInstance(String paasInstanceUriId) throws RepositoryException {
        //        throw new UnsupportedOperationException("Not supported yet.");
        try {
            PaaSOffering retrievedPaaSOffering = getPaaSOffering(paasInstanceUriId);
            if(retrievedPaaSOffering!=null)
                repositoryManager.getManager().delete(paasInstanceUriId, PaaSOffering.class);
        } catch (RDFBeanException ex) {
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
        }
            
    }
    
    @Override
    public List<PaaSInstance> retrieveAllPaaSInstances(String userInstanceUriId) throws RepositoryException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("retrieveAllPaaSInstances(String userInstanceUriId)");
        List<PaaSInstance> retrievedPaaSInstances = new ArrayList<PaaSInstance>();
        try {
            List<PaaSOffering> retrievedPaaSOffering = new ArrayList<PaaSOffering>();
            String paaSOfferingString = PaaSOffering.class.getAnnotation(RDFBean.class).value();
            URI paaSOfferingUri= new URIImpl(paaSOfferingString, true);
            String providedByPaaSProviderString = PaaSOffering.class.getMethod("getPaaSProvider", new Class[0]).getAnnotation(RDF.class).value();
            URI providedByPaaSProviderUri= new URIImpl(providedByPaaSProviderString, true);
            String hasUserString = PaaSProvider.class.getMethod("getUser", new Class[0]).getAnnotation(RDF.class).value();
            URI hasUserUri= new URIImpl(hasUserString, true);
            String prefix = PaaSUser.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
            URI paaSUserUriId = new URIImpl(prefix+userInstanceUriId, true);
            URI type = new URIImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", true);
            
            ClosableIterator<QueryRow> ci = repositoryManager.getManager().getModel().sparqlSelect(
                "SELECT ?r WHERE { "
                    + "?r "+type.toSPARQL()+" "+paaSOfferingUri.toSPARQL()+" . "
                    + "?r "+providedByPaaSProviderUri.toSPARQL()+" ?y . "
                    + "?y "+hasUserUri.toSPARQL()+" "+paaSUserUriId.toSPARQL()+" . }"
            ).iterator();
            while (ci.hasNext()) {
                Resource r = ci.next().getValue("r").asResource();
                retrievedPaaSOffering.add((PaaSOffering)repositoryManager.getManager().get(r));
            }
            for (PaaSOffering paaSOffering : retrievedPaaSOffering) {
                logger.debug("retrieved paaSOffering: "+paaSOffering);
                PaaSInstance paaSInstance = new PaaSInstance(paaSOffering);
                logger.debug("created paaSInstance: "+paaSInstance);
                retrievedPaaSInstances.add(paaSInstance);
            }
        } catch (NoSuchMethodException ex) {
           // java.util.logging.Logger.getLogger(PaaSOfferingProfilesRepository.class.getName()).log(Level.SEVERE, null, ex);
            logger.warn( "Exception while executing method retrieveAllPaaSInstances.", ex);
            logger.error("NoSuchMethodException: "+ex);
            throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
        } catch (SecurityException ex) {            
            logger.warn( "Exception while executing method retrieveAllPaaSInstances.", ex);
            logger.error("SecurityException: "+ex);
            throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
        } catch (RDFBeanException ex) {                        
            logger.warn( "Exception while executing method retrieveAllPaaSInstances.", ex);
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
        }
        logger.debug("retrievedPaaSInstances: "+retrievedPaaSInstances);
        return retrievedPaaSInstances;
    }
        
    /* private methods */
    private PaaSOffering getPaaSOffering(String paaSOfferingUriId) throws RepositoryException{
        PaaSOffering retrievedPaaSOffering = null;
        if(paaSOfferingUriId!=null && !paaSOfferingUriId.isEmpty()){
            try {
                retrievedPaaSOffering = repositoryManager.getManager().get(paaSOfferingUriId, PaaSOffering.class);
            } catch (RDFBeanException ex) {
                logger.error("RDFBeanException: "+ex);
                throw new RepositoryException("PaaSOfferingProfilesRepository", ex);
            }
        }
        return retrievedPaaSOffering;
    }
    
    private void setUriIds(PaaSOffering paaSOffering){
        List<HardwareComponent> offeredResource = paaSOffering.getOfferedHardwareComponents();
        for (HardwareComponent hardwareComponent : offeredResource) {
            if(hardwareComponent.getUriId()==null || hardwareComponent.getUriId().isEmpty())
                hardwareComponent.setUriId(getUUID());
            if(hardwareComponent.getRelatedhwcategory()!=null && hardwareComponent.getRelatedhwcategory().getUriId()==null || hardwareComponent.getRelatedhwcategory().getUriId().isEmpty())
                hardwareComponent.getRelatedhwcategory().setUriId(getUUID());
        }
        List<SoftwareComponent> offeredSoftwareComponent = paaSOffering.getOfferedSoftware();
        for (SoftwareComponent softwareComponent : offeredSoftwareComponent) {
            if(softwareComponent.getUriId()==null || softwareComponent.getUriId().isEmpty())
                softwareComponent.setUriId(getUUID());
            if(softwareComponent.getRelatedswcategory()!=null && softwareComponent.getRelatedswcategory().getUriId()==null || softwareComponent.getRelatedswcategory().getUriId().isEmpty())
                softwareComponent.getRelatedswcategory().setUriId(getUUID());
        }
        List<Channel> communicationChannels = paaSOffering.getCommunicationChannels();
            for (Channel channel : communicationChannels) {
                if(channel.getUriId()==null || channel.getUriId().isEmpty())
                    channel.setUriId(getUUID());
                List<Operation> supportedOperations = channel.getSupportedOperations();
                for (Operation operation : supportedOperations) {
                    if(operation.getUriId()==null || operation.getUriId().isEmpty())
                        operation.setUriId(getUUID());
                    if(operation.getOperationType()!=null && operation.getOperationType().getUriId()==null || operation.getOperationType().getUriId().isEmpty())
                        operation.getOperationType().setUriId(getUUID());                    
                    List<Parameter> requiredParameters = operation.getRequiredParameters();
                    for (Parameter parameter : requiredParameters) {
                        if(parameter.getUriId()==null || parameter.getUriId().isEmpty())
                            parameter.setUriId(getUUID());
                    }
                    List<Exception> thrownException = operation.getThrownException();
                    for (Exception exception : thrownException) {
                        if(exception.getUriId()==null || exception.getUriId().isEmpty())
                            exception.setUriId(getUUID());
                    }
                }
            }
            
        if(paaSOffering.getSupportedLanguage()!=null && (paaSOffering.getSupportedLanguage().getUriId()==null || paaSOffering.getSupportedLanguage().getUriId().isEmpty()))
            paaSOffering.getSupportedLanguage().setUriId(getUUID());
    }
    
    private String getUUID(){
        // Creating a random UUID (Universally unique identifier)
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
