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
import com.viceversatech.rdfbeans.exceptions.RDFBeanException;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.equivalence.EquivalenceRuleHWCategoryInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DBStorageComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareComponentInstance;
import eu.cloud4soa.api.datamodel.repository.QueryResultTable;
import eu.cloud4soa.api.datamodel.semantic.app.Application;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationDeployment;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareCategory;
import eu.cloud4soa.api.datamodel.semantic.soffd.EquivalenceRuleHardwareCategory;
import eu.cloud4soa.api.datamodel.semantic.user.Developer;
import eu.cloud4soa.api.repository.ApplicationProfilesRepository;
import eu.cloud4soa.api.repository.SearchAndDiscoveryInterfaces;
import eu.cloud4soa.api.repository.UserProfilesRepository;
import eu.cloud4soa.api.util.exception.SparqlQueryException;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.repository.utils.RepositoryManager;
import eu.cloud4soa.repository.utils.TemporaryRepositoryManager;
import eu.cloud4soa.soa.exceptions.ResourceException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author vincenzo
 * C4S Frontend required methods added by Yosu
 */
@Transactional
public class ModelManager implements eu.cloud4soa.api.soa.ModelManager{
    final Logger logger = LoggerFactory.getLogger(ModelManager.class);

    private ApplicationProfilesRepository applicationProfilesRepository;
    private UserProfilesRepository userProfilesRepository;
    private SearchAndDiscoveryInterfaces searchAndDiscoveryInterfaces;
//    private RepositoryManager repositoryManager = RepositoryManager.getInstance();
    private RepositoryManager repositoryManager = null;

    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    public void setApplicationProfilesRepository(ApplicationProfilesRepository applicationProfilesRepository) {
        this.applicationProfilesRepository = applicationProfilesRepository;
    }

    public void setUserProfilesRepository(UserProfilesRepository userProfilesRepository) {
        this.userProfilesRepository = userProfilesRepository;
    }

    public void setSearchAndDiscoveryInterfaces(SearchAndDiscoveryInterfaces searchAndDiscoveryInterfaces) {
        this.searchAndDiscoveryInterfaces = searchAndDiscoveryInterfaces;
    }
    
    
    @Override
    public String storeApplicationProfile(ApplicationInstance applicationInstance, String userInstanceUriId) throws SOAException{
        logger.debug("received applicationInstance: "+applicationInstance);
        logger.debug("applicationInstance.getTitle(): "+applicationInstance.getTitle());
        logger.debug("applicationInstance.getApplicationcode(): "+applicationInstance.getApplicationcode());
        logger.debug("applicationInstance.getArchiveExtensionName(): "+applicationInstance.getArchiveExtensionName());
        logger.debug("applicationInstance.getArchiveFileName(): "+applicationInstance.getArchiveFileName());
        logger.debug("applicationInstance.getDigest(): "+applicationInstance.getDigest());
        logger.debug("applicationInstance.getProgramminglanguage(): "+applicationInstance.getProgramminglanguage());
        logger.debug("applicationInstance.getProgramminglanguageVersion(): "+applicationInstance.getProgramminglanguageVersion());
        logger.debug("applicationInstance.getOwnerUriId(): "+applicationInstance.getOwnerUriId());
        
        //user application list should be updated...        
//        DeveloperInstance userInstance = (DeveloperInstance) userProfilesRepository.getUserInstance(userInstanceUriId);
//        userInstance.addApplication(applicationInstance);
//        userProfilesRepository.storeUserInstance(userInstance);
        applicationInstance.setOwnerUriId(userInstanceUriId);
        
        logger.debug("call applicationProfilesRepository.storeApplicationInstance(applicationInstance)");
        //store applicationInstance into repository
        List<SoftwareComponentInstance> softwareComponents = applicationInstance.getSoftwareComponents();
        for (SoftwareComponentInstance softwareComponentInstance : softwareComponents) {
            if(softwareComponentInstance instanceof DBStorageComponentInstance)
                logger.debug("getDbname: "+((DBStorageComponentInstance)softwareComponentInstance).getDbname());
        }
        try {
            applicationProfilesRepository.storeApplicationInstance(applicationInstance);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("applicationInstanceUriId: "+applicationInstance.getUriId());
        
        
        
        return applicationInstance.getUriId();
        
//        return Response.status(Response.Status.CREATED).entity(userInstance.getUriId()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @Override
    public Response storeTurtleApplicationProfile(String applicationProfile, String userInstanceUriId) throws SOAException {
        logger.debug("received applicationProfile: "+applicationProfile);
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
  
        TemporaryRepositoryManager trm;
        try {
            trm = new TemporaryRepositoryManager(applicationProfile);
        } catch (IOException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Model temporaryModel = trm.getManager().getModel();
        ClosableIterator<Statement> newStatements = temporaryModel.iterator();
        Model c4sModel = null;
        try{  
            String applicationUriString = Application.class.getAnnotation(RDFBean.class).value();
            String applicationUriId = null;
            //here add the validations queries
            URI applicationUri = new URIImpl( applicationUriString, true);
            
            String query = 
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX c4s-inf-m: <http://www.cloud4soa.eu/v0.1/infrastructural-domain#>"
                + "PREFIX dcterms: <http://purl.org/dc/terms/>"
                + "SELECT ?object WHERE { ?object rdf:type "+applicationUri.toSPARQL()+" .}";
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
                applicationUriId = queryRow.getValue("object").asURI().toString();
            }
            else{
                String error = "There is no application resource inside the sent statements";
                logger.debug(error);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.TEXT_PLAIN).build();
            }
            if(it.hasNext()){
                String error = "The sent profile has more than one application.";
                logger.debug(error);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.TEXT_PLAIN).build();
            }
            String applicationInstanceUri = null;
            try {
                applicationInstanceUri = Application.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
            } catch (NoSuchMethodException ex) {
                logger.error(ex.getMessage());
            } catch (SecurityException ex) {
                logger.error(ex.getMessage());
            }
            String applicationUriIdWithoutPrefix = applicationUriId;
            if(applicationUriId.contains(applicationInstanceUri.toString()))
                applicationUriIdWithoutPrefix = applicationUriId.replace(applicationInstanceUri.toString(), "");
            
            c4sModel = repositoryManager.getModel();
        
            logger.debug("c4sModel.addAll(newStatements)");
            c4sModel.setAutocommit(false);
            c4sModel.addAll(newStatements);
            c4sModel.commit();
            
            ApplicationInstance applicationInstance;
            try {
                applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationUriIdWithoutPrefix);
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
            DeveloperInstance userInstance;                
            try {
                userInstance = (DeveloperInstance) userProfilesRepository.getUserInstance(userInstanceUriId);
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
            applicationInstance.getApplication().setOwner((Developer)userInstance.getUser());
            try {
                applicationProfilesRepository.updateApplicationInstance(applicationInstance);
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
            logger.debug("applicationInstanceUriId: "+applicationInstance.getUriId());
            return Response.status(Response.Status.CREATED).entity(applicationInstance.getUriId()).type(MediaType.TEXT_PLAIN).build();
            
        } catch (SecurityException ex) {
            logger.error(ex.getMessage());
        } catch (ModelRuntimeException ex) {
            c4sModel.removeAll(newStatements);
            c4sModel.commit();
            logger.error(ex.getMessage());
        } 
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    @Override
    public void updateApplicationProfile(ApplicationInstance applicationInstance) throws SOAException {
        logger.debug("received applicationInstance: "+applicationInstance);
        logger.debug("applicationInstance getUriId(): "+applicationInstance.getUriId());
        logger.debug("applicationInstance.getTitle(): "+applicationInstance.getTitle());
        logger.debug("applicationInstance.getApplicationcode(): "+applicationInstance.getApplicationcode());
        logger.debug("applicationInstance.getArchiveExtensionName(): "+applicationInstance.getArchiveExtensionName());
        logger.debug("applicationInstance.getArchiveFileName(): "+applicationInstance.getArchiveFileName());
        logger.debug("applicationInstance.getDigest(): "+applicationInstance.getDigest());
        logger.debug("applicationInstance.getProgramminglanguage(): "+applicationInstance.getProgramminglanguage());
        logger.debug("applicationInstance.getProgramminglanguageVersion(): "+applicationInstance.getProgramminglanguageVersion());
        logger.debug("applicationInstance.getOwnerUriId(): "+applicationInstance.getOwnerUriId());
        
        try {
            logger.debug("call applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId)");
            //Application Instance (serialized with JAXB) does not contain any information regarding Deployment!
            //Those informations are stored with the old instance (in the repository)
            //Now we want to keep the changes and keep old informations that are not changed by the update!
            ApplicationInstance retrievedApplicationInstance;
            try {
                retrievedApplicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstance.getUriId());
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
            ApplicationDeployment deployment = retrievedApplicationInstance.getApplication().getDeployment();
            applicationInstance.getApplication().setDeployment(deployment);
            applicationInstance.getApplication().setStatus(retrievedApplicationInstance.getApplication().getStatus());
            applicationInstance.getApplication().setOwner(retrievedApplicationInstance.getApplication().getOwner());
            try {
                applicationProfilesRepository.updateApplicationInstance(applicationInstance);
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        } catch (IllegalArgumentException e) {
            throw new ResourceException("The requested resource does not exists.");
        }
        
//        return Response.status(Response.Status.ACCEPTED).entity("Account Updated: "+userInstance.getUriId()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
    
    //Added by Yosu
    @Override
    public void removeApplicationProfile(String applicationInstanceUriId) throws SOAException {
        logger.debug("applicationInstanceUriId: "+applicationInstanceUriId);
        
        //user's application list should be updated... 
        //owner to be retrieved first        
        try {
            applicationProfilesRepository.removeApplicationProfile(applicationInstanceUriId);            
        } catch (IllegalArgumentException e) {
            throw new ResourceException("The requested resource does not exists.");
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<ApplicationInstance> retrieveAllApplicationProfile(String userInstanceUriId) throws SOAException {
        logger.debug("userInstanceUriId: "+userInstanceUriId);
        
        try {
            return applicationProfilesRepository.retrieveAllApplicationProfile(userInstanceUriId);
        } catch (IllegalArgumentException e) {
            throw new ResourceException("The requested resource does not exists.");
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    
    @Override
    public QueryResultTable sparqlSelect(String query) throws SparqlQueryException { 
        logger.debug("Query: "+query);
        QueryResultTable queryResultTable = searchAndDiscoveryInterfaces.sparqlSelect(query);
        logger.debug(queryResultTable.toString());
        return queryResultTable;
    }

//    private void extractUptime(Model temporaryModel) {
//         String uptimeUriString = Uptime.class.getAnnotation(RDFBean.class).value();
//            String uptimeUriId = null;
//            //here add the validations queries
//            URI applicationUri = new URIImpl( uptimeUriString, true);
//            hasPercentageString  = Uptime.class.getMethod("getHasPercentage", new Class[0]).getAnnotation(RDF.class).value();
//            hasPercentageUri     = new URIImpl(hasPercentageString, true);
//                
//            String query = 
//                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
//                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
//                + "PREFIX qos-m: <http://www.cloud4soa.eu/v0.1/qos-model#> "
//                + "PREFIX dcterms: <http://purl.org/dc/terms/>"
//                + "SELECT ?object ?percentage WHERE { ?object rdf:type "+applicationUri.toSPARQL()+" . OPTIONAL { ?percentage dcterms:title ?title }}";
//            logger.debug(query);
//            org.ontoware.rdf2go.model.QueryResultTable resultTable = temporaryModel.sparqlSelect(query);
//            
//            if(resultTable == null){
//                logger.debug("An error happens when querying the model");
//                return;
//            }
//            Iterator<QueryRow> it = resultTable.iterator();
//            if(it.hasNext()){
//                QueryRow queryRow = it.next();
//                uptimeUriId = queryRow.getValue("object").asURI().toString();
//                uptimeUriId = queryRow.getValue("percentage").asDatatypeLiteral().getValue().toString();
//            }
//            else{
//                logger.debug("There is no application resource inside the sent statements");
//                return null;
//            }
//            if(it.hasNext())
//                throw new ResourceException("The sent profile has more than one application.");
//    }

    @Override
    public ApplicationInstance retrieveApplicationProfile(String applicationInstanceUriId, String userInstanceUriId) throws SOAException {
        logger.debug("applicationInstanceUriId: "+applicationInstanceUriId);
        logger.debug("userInstanceUriId: "+userInstanceUriId);
        
        try {
            ApplicationInstance applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
            if(applicationInstance.getOwnerUriId() == null ? userInstanceUriId != null : !applicationInstance.getOwnerUriId().equals(userInstanceUriId))
                throw new ResourceException("One of the request inputs is not valid."); 
            return applicationInstance;
        } catch (IllegalArgumentException e) {
            throw new SOAException(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new SOAException(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    
    @Transactional
    @Override
    public Response addEquivalenceRule(EquivalenceRuleHWCategoryInstance equivalenceRule) throws SOAException {
        logger.debug("equivalenceRuleUriId: "+equivalenceRule.getEquivalenceRuleHWCategoryUriId());
        logger.debug("equivalenceRule hasSource: "+equivalenceRule.getHasSource());
        logger.debug("equivalenceRule hasTarget: "+equivalenceRule.getHasTarget());
        logger.debug("equivalenceRule hasConversionRate: "+equivalenceRule.getHasConversionRate());
        String equivalenceRuleHWCategoryUriId = equivalenceRule.getEquivalenceRuleHWCategoryUriId();
        String hasSource = equivalenceRule.getHasSource();
        String hasTarget = equivalenceRule.getHasTarget();
        
        checkConditionOrThrowsException(hasSource==null || hasSource.isEmpty(), Response.Status.BAD_REQUEST, "HasSource is null or empty");
        checkConditionOrThrowsException(hasTarget==null || hasTarget.isEmpty(), Response.Status.BAD_REQUEST, "HasTarget is null or empty");
        
        Model model = repositoryManager.getModel();
        
        String query;
        boolean askResult;
                
        URI hasSourceUriString = null;
        
        try {
            if(hasSource.startsWith("http://"))
                hasSourceUriString = new URIImpl( hasSource, true);
            else {
                String hasSourceUri = HardwareCategory.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
//                String hwCategoryUriString = HardwareCategory.class.getAnnotation(RDFBean.class).value();
                hasSourceUriString = new URIImpl(hasSourceUri + hasSource , true);
            }
        } catch (IllegalArgumentException e) {
            
            String error = "Malformed hasSource UriId: " + hasSource + " - "+e.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error); 
        } catch (NoSuchMethodException ex) {
            String error = ex.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        } catch (SecurityException ex) {
            String error = ex.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        }
            
        query = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
            + "PREFIX other: <http://www.cloud4soa.eu/v0.1/other#>"
            + "PREFIX c4s-inf-m: <http://www.cloud4soa.eu/v0.1/infrastructural-domain#>"
            + "ASK { { "+hasSourceUriString.toSPARQL()+" rdf:type c4s-inf-m:Computational_Category } UNION {   "+hasSourceUriString.toSPARQL()+" rdf:type c4s-inf-m:Communicational_Category } UNION {   "+hasSourceUriString.toSPARQL()+" rdf:type c4s-inf-m:Storage_Category } }";      
        logger.debug(query);
        askResult = model.sparqlAsk(query);
        checkConditionOrThrowsException(!askResult, Response.Status.BAD_REQUEST, "Does not exist any HardwareCategory with UriId: "+equivalenceRule.getHasSource());
        
               
        URI hasTargetUriString = null;
        
        try {
            if(hasSource.startsWith("http://"))
                hasTargetUriString = new URIImpl( hasTarget, true);
            else {
                String hasTargetUri = HardwareCategory.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
//                String hwCategoryUriString = HardwareCategory.class.getAnnotation(RDFBean.class).value();
                hasTargetUriString = new URIImpl(hasTargetUri + hasTarget , true);
            }
        } catch (IllegalArgumentException e) {
            String error = "Malformed hasTarget UriId: " + hasTarget + " - "+e.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        } catch (NoSuchMethodException ex) {
            String error = ex.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        } catch (SecurityException ex) {
            String error = ex.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        }   
        
        query = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
            + "PREFIX other: <http://www.cloud4soa.eu/v0.1/other#>"
            + "PREFIX c4s-inf-m: <http://www.cloud4soa.eu/v0.1/infrastructural-domain#>"
            + "ASK { { "+hasTargetUriString.toSPARQL()+" rdf:type c4s-inf-m:Computational_Category } UNION {   "+hasTargetUriString.toSPARQL()+" rdf:type c4s-inf-m:Communicational_Category } UNION {   "+hasTargetUriString.toSPARQL()+" rdf:type c4s-inf-m:Storage_Category } }";      
        logger.debug(query);
        askResult = model.sparqlAsk(query);
        checkConditionOrThrowsException(!askResult, Response.Status.BAD_REQUEST, "Does not exist any HardwareCategory with UriId: "+equivalenceRule.getHasTarget());
 
        //Checking that the Source and Target categories are of the same type
        query = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
            + "PREFIX other: <http://www.cloud4soa.eu/v0.1/other#>"
            + "PREFIX c4s-inf-m: <http://www.cloud4soa.eu/v0.1/infrastructural-domain#>"
            + "ASK "
            + "{"
            + " { "+hasSourceUriString.toSPARQL()+" rdf:type c4s-inf-m:Computational_Category . "
           +        hasTargetUriString.toSPARQL()+" rdf:type c4s-inf-m:Computational_Category"
            + " }"
            + " UNION"
            + " { "+hasSourceUriString.toSPARQL()+" rdf:type c4s-inf-m:Communicational_Category . "
            +       hasTargetUriString.toSPARQL()+" rdf:type c4s-inf-m:Communicational_Category"
            + " }"
            + " UNION"
            + " { "+hasSourceUriString.toSPARQL()+" rdf:type c4s-inf-m:Storage_Category . "
            +       hasTargetUriString.toSPARQL()+" rdf:type c4s-inf-m:Storage_Category"
            +   "} "
            + "}";      
        logger.debug(query);
        askResult = model.sparqlAsk(query);
        checkConditionOrThrowsException(!askResult, Response.Status.BAD_REQUEST, "The Source and Target categories of the defined EquivalenceRuleHardwareCategory are not of the same type");
        
        
        String eqUriIdWithoutPrefix = null;
        String hasSourceUriIdWithoutPrefix = hasSourceUriString.toString().split("#")[1];
        String hasTargetUriIdWithoutPrefix = hasTargetUriString.toString().split("#")[1];
        Float hasConversionRate = equivalenceRule.getHasConversionRate();
        
        URI equivalenceRuleHWCategoryUri = null;
        if(equivalenceRuleHWCategoryUriId == null || equivalenceRuleHWCategoryUriId.isEmpty()){
            equivalenceRuleHWCategoryUriId = hasSourceUriIdWithoutPrefix + "To" + hasTargetUriIdWithoutPrefix;
        }

        try {
            if(equivalenceRuleHWCategoryUriId.startsWith("http://"))
                equivalenceRuleHWCategoryUri = new URIImpl( equivalenceRuleHWCategoryUriId, true);
            else {
                String equivalenceRuleUriString = EquivalenceRuleHardwareCategory.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
                equivalenceRuleHWCategoryUri = new URIImpl(equivalenceRuleUriString + equivalenceRuleHWCategoryUriId , true);
            }
        } catch (IllegalArgumentException e) {
            String error = "Malformed equivalenceRuleHWCategory UriId: " + equivalenceRuleHWCategoryUriId + " - " + e.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        } catch (NoSuchMethodException ex) {
            String error = "Malformed equivalenceRuleHWCategory UriId: " + equivalenceRuleHWCategoryUriId + " - " + ex.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        } catch (SecurityException ex) {
            String error = "Malformed equivalenceRuleHWCategory UriId: " + equivalenceRuleHWCategoryUriId + " - " + ex.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        }
        

        query = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
            + "PREFIX other: <http://www.cloud4soa.eu/v0.1/other#>"
            + "ASK { " + equivalenceRuleHWCategoryUri.toSPARQL() + " rdf:type other:EquivalenceRuleHardwareCategory }";

        logger.debug(query);
        askResult = model.sparqlAsk(query);
        checkConditionOrThrowsException(askResult, Response.Status.BAD_REQUEST, "An EquivalenceRuleHardwareCategory with the same UriId already exists");
        eqUriIdWithoutPrefix = equivalenceRuleHWCategoryUri.toString().split("#")[1];

        //Check if an equivalence rule with the same Source and Target categories already exists
        query = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
            + "PREFIX other: <http://www.cloud4soa.eu/v0.1/other#>"
            + "ASK "
            + "{"
            + " ?object rdf:type other:EquivalenceRuleHardwareCategory . "
                + "{"
                    + "{"
                    +   " ?object other:hasSource " + hasSourceUriString.toSPARQL() + " ."
                    +   " ?object other:hasTarget " + hasTargetUriString.toSPARQL() + " . "
                    + "}"
                    + " UNION "
                    + "{"
                    +   " ?object other:hasSource " + hasTargetUriString.toSPARQL() + " ."
                    +   " ?object other:hasTarget " + hasSourceUriString.toSPARQL() + " . "
                    + "} "
                + "}"
            + "}";


        logger.debug(query);
        askResult = model.sparqlAsk(query);
        checkConditionOrThrowsException(askResult, Response.Status.BAD_REQUEST, "An EquivalenceRuleHardwareCategory with the same Source and Target already exists");
        eqUriIdWithoutPrefix = equivalenceRuleHWCategoryUri.toString().split("#")[1];
        
        
        //        String hasSourceUri = EquivalenceRuleHardwareCategory.class.getMethod("getHasSource", new Class[0]).getAnnotation(RDFSubject.class).prefix();
        //        model.addStatement(konrad, hasSourceUri, hasSourceUriString);
            
        HardwareCategory sourceHwCategory = null;
        HardwareCategory targetHwCategory = null;
        try {
            sourceHwCategory = repositoryManager.getManager().get(hasSourceUriIdWithoutPrefix, HardwareCategory.class);
            targetHwCategory = repositoryManager.getManager().get(hasTargetUriIdWithoutPrefix, HardwareCategory.class);
//            sourceHwCategory = repositoryManager.getManager().get(hasSourceUriIdWithoutPrefix, ComputationalCategory.class);
//            targetHwCategory = repositoryManager.getManager().get(hasTargetUriIdWithoutPrefix, ComputationalCategory.class);
        } catch (RDFBeanException ex) {
            String error = "Error while querying the repository : "+ ex.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, error);
        }
        EquivalenceRuleHardwareCategory equivalenceRuleHardwareCategory = new EquivalenceRuleHardwareCategory();
        equivalenceRuleHardwareCategory.setUriId(eqUriIdWithoutPrefix);
        equivalenceRuleHardwareCategory.setHasSource(sourceHwCategory);
        equivalenceRuleHardwareCategory.setHasTarget(targetHwCategory);
        equivalenceRuleHardwareCategory.setHasConversionRate(hasConversionRate);
        try {
            repositoryManager.getManager().add(equivalenceRuleHardwareCategory);
        } catch (RDFBeanException ex) {
            String error = "Error while storing the equivalenceRule: "+ ex.getMessage();
            logger.debug(error);
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, error);
        }
        
        return Response.status(Response.Status.ACCEPTED).entity("EquivalenceRule Stored: "+eqUriIdWithoutPrefix).build();
    }

    private void checkConditionOrThrowsException(boolean condition, Response.Status responseStatus, String errorMessage) throws SOAException{
        if(condition){
            logger.debug(errorMessage);
            throw new SOAException(responseStatus, errorMessage);
        }
    }
}
