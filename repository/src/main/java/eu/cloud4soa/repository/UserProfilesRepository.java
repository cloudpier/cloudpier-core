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
import com.viceversatech.rdfbeans.exceptions.RDFBeanException;
import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSUserInstance;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.foaf.OnlineAccount;
import eu.cloud4soa.api.datamodel.semantic.user.Cloud4SoaAccount;
import eu.cloud4soa.api.datamodel.semantic.user.Developer;
import eu.cloud4soa.api.datamodel.semantic.user.PaaSUser;
import eu.cloud4soa.api.datamodel.semantic.user.User;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.repository.utils.RepositoryManager;
import java.util.UUID;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.PlainLiteral;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vincenzo
 */
public class UserProfilesRepository implements eu.cloud4soa.api.repository.UserProfilesRepository{
    final Logger logger = LoggerFactory.getLogger(UserProfilesRepository.class);
    //    private RepositoryManager repositoryManager = RepositoryManager.getInstance();
    private RepositoryManager repositoryManager = null;

    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }
	
	private int prog = 1;
    
    @Override
    public void createUserInstance(UserInstance userInstance) throws RepositoryException {
//        throw new UnsupportedOperationException("Not supported yet.");
        if(userInstance.getUriId()==null){
            if(userInstance instanceof DeveloperInstance){
                try {
                    userInstance.setUriId(getUUID());
                    //Obtain the main RDFBean Class from the wrapper
                    Developer developer = (Developer)((DeveloperInstance)userInstance).getUser();
                    setUriIds(developer);
                    repositoryManager.getManager().add(developer);
                } catch (RDFBeanException ex) {
                    logger.error("RDFBeanException: "+ex);
                    throw new RepositoryException("UserProfilesRepository", ex);
                }
            }
            else if(userInstance instanceof PaaSUserInstance){
                try {
                    userInstance.setUriId(getUUID());
                    //Obtain the main RDFBean Class from the wrapper
                    PaaSUser paaSUser = (PaaSUser)((PaaSUserInstance)userInstance).getUser();
                    setUriIds(paaSUser);
                    repositoryManager.getManager().add(paaSUser);
                } catch (RDFBeanException ex) {
                    logger.error("RDFBeanException: "+ex);
                    throw new RepositoryException("UserProfilesRepository", ex);
                }
            }
        }

//        try {
//            //Obtain the main RDFBean Class from the wrapper
//            User user = userInstance.getUser();
//            repositoryManager.getManager().add(user);
//        } catch (RDFBeanException ex) {
//            logger.error("RDFBeanException: "+ex);
//        }
    }

    @Override
    public UserInstance getUserInstance(String userInstanceUriId) throws RepositoryException {
//        throw new UnsupportedOperationException("Not supported yet.");
        UserInstance retrievedUserInstance = null;
        if(userInstanceUriId!=null && !userInstanceUriId.isEmpty()){
            try {
								
                Developer developer = repositoryManager.getManager().get(userInstanceUriId, Developer.class);
                if(developer != null)
                    retrievedUserInstance = new DeveloperInstance(developer);
                else{
                    PaaSUser paaSUser = repositoryManager.getManager().get(userInstanceUriId, PaaSUser.class);
                    if(paaSUser != null)
                        retrievedUserInstance = new PaaSUserInstance(paaSUser);
                }

                logger.debug("retrieved userInstance: "+retrievedUserInstance);
            } catch (RDFBeanException ex) {
                logger.error("RDFBeanException: "+ex);
                throw new RepositoryException("UserProfilesRepository", ex);
            }
        }
        else{
            //Query by-example exploiting information obtained from the Model...
            logger.error("Query by-example Not supported yet.");
            throw new RepositoryException("Query by-example Not supported yet.");
        }
        return retrievedUserInstance;
    }

    @Override
    public void storeUserInstance(UserInstance userInstance) throws IllegalArgumentException, RepositoryException{
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("storeUserInstance(UserInstance userInstance)");
        //store userInstance
        try {
            Resource existingResource = null;
            if(userInstance instanceof DeveloperInstance){
                Developer developer = (Developer)((DeveloperInstance)userInstance).getUser();
                //check if the specific resource exists
                existingResource = repositoryManager.getManager().getResource(developer.getUriId(), Developer.class);
                if(existingResource!=null)
                    repositoryManager.getManager().update(developer);
                else throw new IllegalArgumentException("The specific resource does not exists");
            }
            else if(userInstance instanceof PaaSUserInstance){
                PaaSUser paaSUser = (PaaSUser)((PaaSUserInstance)userInstance).getUser();
                //check if the specific resource exists
                existingResource = repositoryManager.getManager().getResource(paaSUser.getUriId(), PaaSUser.class);
                if(existingResource!=null)
                    repositoryManager.getManager().update(paaSUser);
                else throw new IllegalArgumentException("The specific resource does not exists");
            }
        
//            //Obtain the main RDFBean Class from the wrapper
//            User user = userInstance.getUser();
//            //check if the specific resource exists
//            Resource existingResource = repositoryManager.getManager().getResource(user.getUriId(), User.class);
//            if(existingResource!=null)
//                repositoryManager.getManager().update(user);
//            else throw new IllegalArgumentException("The specific resource does not exists");
        } catch (RDFBeanException ex) {
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("UserProfilesRepository", ex);
        }
        logger.debug("UserInstance stored");
    }
	
    
    @Override
    public synchronized void updateUserInstance(UserInstance userInstance) throws RepositoryException {
//        throw new UnsupportedOperationException("Not supported yet.");
        logger.debug("updateApplicationInstance(ApplicationInstance applicationInstance)");
        //update applicationInstance
        try {
            //Obtain the main RDFBean Class from the wrapper
            User user = userInstance.getUser();
            setUriIds(user);
            repositoryManager.getManager().update(user);
        } catch (RDFBeanException ex) {
            logger.error("RDFBeanException: "+ex);
            throw new RepositoryException("UserProfilesRepository", ex);
        }
        logger.debug("UserInstance updated");
    }
	
    @Override
    public UserInstance getUserInstanceFromAccountName(String accountName) throws RepositoryException {
        UserInstance retrievedUserInstance;
        URI userInstanceUri;
        URI holdsaccountUri;
        URI accountNameUri;
        URI type;
        String userInstanceString;
        String holdsaccountString;
        String accountNameString;
        PlainLiteral userNameLiteral;
        ClosableIterator<QueryRow> ci;
        Resource r;
        User retrievedUser;
	retrievedUserInstance = null;
        
        if(accountName!=null && !accountName.isEmpty()){
            try {
                //Developer case
                userInstanceString  = Developer.class.getAnnotation(RDFBean.class).value();
                userInstanceUri     = new URIImpl(userInstanceString, true);
                holdsaccountString  = User.class.getMethod("getHoldsaccount", new Class[0]).getAnnotation(RDF.class).value();
                holdsaccountUri     = new URIImpl(holdsaccountString, true);
                accountNameString   = Cloud4SoaAccount.class.getMethod("getAccountname", new Class[0]).getAnnotation(RDF.class).value();
                accountNameUri      = new URIImpl( accountNameString, true);
                type                = new URIImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", true);
                userNameLiteral     = repositoryManager.getManager().getModel().createPlainLiteral( accountName );

                ci = repositoryManager.getManager().getModel().sparqlSelect(
                    "SELECT ?r WHERE { "
                    + "?r "+type.toSPARQL()+" "+userInstanceUri.toSPARQL()+" . "
                    + "?r "+holdsaccountUri.toSPARQL()+" ?y . "
                    + "?y "+accountNameUri.toSPARQL()+" "+userNameLiteral.toSPARQL()+" . }"

                    ).iterator();
                if (ci.hasNext()) {
                        r = ci.next().getValue("r").asResource();
                        Developer developer = repositoryManager.getManager().get(r, Developer.class);
                        if(developer != null){
                            logger.debug("retrieved Developer: "+ developer );
                            retrievedUserInstance = new DeveloperInstance(developer);
                        }
                }
                else{
                    //PaaSUser case
                    userInstanceString  = PaaSUser.class.getAnnotation(RDFBean.class).value();
                    userInstanceUri     = new URIImpl(userInstanceString, true);
                    ci = repositoryManager.getManager().getModel().sparqlSelect(
                    "SELECT ?r WHERE { "
                    + "?r "+type.toSPARQL()+" "+userInstanceUri.toSPARQL()+" . "
                    + "?r "+holdsaccountUri.toSPARQL()+" ?y . "
                    + "?y "+accountNameUri.toSPARQL()+" "+userNameLiteral.toSPARQL()+" . }"

                    ).iterator();
                    if (ci.hasNext()) {
                        r = ci.next().getValue("r").asResource();                            
                        PaaSUser paaSUser = repositoryManager.getManager().get(r, PaaSUser.class);
                        if(paaSUser != null){
                            logger.debug("retrieved PaaSUser: "+ paaSUser );
                            retrievedUserInstance = new PaaSUserInstance(paaSUser);
                        }                     
                    }
                }
                logger.debug("created userInstance: "+ retrievedUserInstance );
            } catch (NoSuchMethodException ex) {
                    logger.error("NoSuchMethodException: "+ex);
                    throw new RepositoryException("UserProfilesRepository", ex);
            } catch (SecurityException ex) {
                    logger.error("SecurityException: "+ex);
                    throw new RepositoryException("UserProfilesRepository", ex);
            } catch (RDFBeanException ex) {
                    logger.error("RDFBeanException: "+ ex);
                    throw new RepositoryException("UserProfilesRepository", ex);
            }
			
        }
        else{
           logger.error("Illegal parameters: account = " + accountName );
           throw new RepositoryException("Illegal parameters: account = " + accountName);
        }
        return retrievedUserInstance;
    }
		
    private void setUriIds(PaaSUser paaSUser){
        PaaSProvider paaSProvider = paaSUser.getPaaSProvider(); 
        if(paaSProvider.getUriId()==null || paaSProvider.getUriId().isEmpty())
            paaSProvider.setUriId(getUUID());
        OnlineAccount holdsaccount = paaSUser.getHoldsaccount();
        if(holdsaccount.getUriId()==null || holdsaccount.getUriId().isEmpty())
            holdsaccount.setUriId(getUUID());
    }
    
    private void setUriIds(Developer developer){
        OnlineAccount holdsaccount = developer.getHoldsaccount();
        if(holdsaccount.getUriId()==null || holdsaccount.getUriId().isEmpty())
            holdsaccount.setUriId(getUUID());
    }
    
    private void setUriIds(User user){
        OnlineAccount holdsaccount = user.getHoldsaccount();
        if(holdsaccount.getUriId()==null || holdsaccount.getUriId().isEmpty())
            holdsaccount.setUriId(getUUID());
    }
    
    private String getUUID(){
        // Creating a random UUID (Universally unique identifier)
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
	
	
	public void close() {
//		repositoryManager.closeModel();
	}
    
}
