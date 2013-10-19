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
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.Cloud4SoaAccountInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSUserInstance;
import eu.cloud4soa.api.datamodel.semantic.user.Developer;
import eu.cloud4soa.api.datamodel.semantic.user.PaaSUser;
import eu.cloud4soa.api.datamodel.soa.UserPaaSCredentials;
import eu.cloud4soa.api.repository.PaaSOfferingProfilesRepository;
import eu.cloud4soa.api.repository.UserProfilesRepository;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.relational.datamodel.Account;
import eu.cloud4soa.relational.datamodel.Paas;
import eu.cloud4soa.relational.datamodel.User;
import eu.cloud4soa.relational.datamodel.Usertype;
import eu.cloud4soa.relational.persistence.AccountRepository;
import eu.cloud4soa.relational.persistence.PaasRepository;
import eu.cloud4soa.relational.persistence.UserRepository;
import eu.cloud4soa.relational.persistence.UsertypeRepository;
import eu.cloud4soa.repository.utils.RepositoryManager;
import eu.cloud4soa.repository.utils.TemporaryRepositoryManager;
import eu.cloud4soa.soa.exceptions.ResourceException;
import eu.cloud4soa.soa.git.utils.Util;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author vincenzo
 * C4S Frontend required methods added by Yosu
 */
@Transactional
public class UserManagementAndSecurityModule implements eu.cloud4soa.api.soa.UserManagementAndSecurityModule{
    final Logger logger = LoggerFactory.getLogger(UserManagementAndSecurityModule.class);
    
    private UserProfilesRepository userProfilesRepository;
    //    private RepositoryManager repositoryManager = RepositoryManager.getInstance();
    private RepositoryManager repositoryManager = null;
    private PaaSOfferingProfilesRepository paaSOfferingProfilesRepository;
        
    //Relational DB
    @Autowired
    private UserRepository userrepository;
    @Autowired
    private UsertypeRepository usertyperepository;    
    @Autowired
    private PaasRepository paasrepository;
    @Autowired
    private AccountRepository accountrepository;
    
    @Required
    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    @Required
    public void setUserProfilesRepository(UserProfilesRepository userProfilesRepository) {
        this.userProfilesRepository = userProfilesRepository;
    }
    
    @Required
    public void setPaaSOfferingProfilesRepository(PaaSOfferingProfilesRepository paaSOfferingProfilesRepository) {
        this.paaSOfferingProfilesRepository = paaSOfferingProfilesRepository;
    }
    
    @Override
    public Response createNewAccount(UserInstance userInstance ,String username, String password) throws SOAException{
        logger.debug("received userInstance: "+userInstance);
        if(userInstance instanceof DeveloperInstance) logger.debug("DeveloperInstance");
        if(userInstance instanceof PaaSUserInstance)  logger.debug("PaaSUserInstance");
        logger.debug("userInstance.getAccountname(): "+userInstance.getAccountname());
        logger.debug("userInstance.getCloud4SoaAccountUriId(): "+userInstance.getCloud4SoaAccountUriId());
        logger.debug("userInstance.getFamilyname(): "+userInstance.getFamilyname());
        logger.debug("userInstance.getFirstName(): "+userInstance.getFirstName());
        logger.debug("userInstance.getGeekcode(): "+userInstance.getGeekcode());
        logger.debug("userInstance.getSurname(): "+userInstance.getSurname());
        logger.debug("userInstance.getUriId(): "+userInstance.getUriId());
        logger.debug("userInstance username: "+username);
        logger.debug("userInstance password: "+password);
        logger.debug("userInstance.getUriId(): "+userInstance.getUriId());
//        logger.debug("userInstance.getCloud4SoaAccountUriId(): "+userInstance.getCloud4SoaAccountUriId());
//        logger.debug("userInstance.getAccountname(): "+userInstance.getAccountname());
        
        logger.debug("check if the username already exists: " + username);
        boolean accountNameInUse = isAccountNameInUse(username);
        if(accountNameInUse){
            String error = "The username already exists: "+username;
            logger.debug(error);
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(error).build();
        }
        
        logger.debug("call userProfilesRepository.createUserInstance(userInstance)");
        //store userInstance into the semantic repository
        Cloud4SoaAccountInstance cloud4SoaAccountInstance = new Cloud4SoaAccountInstance();
        cloud4SoaAccountInstance.setAccountname(username);
        userInstance.setHoldsaccount(cloud4SoaAccountInstance);
        try {
            userProfilesRepository.createUserInstance(userInstance);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        
        //add the Relational Entry
        User user = new User();
        user.setUsername( userInstance.getAccountname() );
        user.setPassword( password    );
        user.setFullname( userInstance.getFamilyname() + " "+userInstance.getFirstName() );
        user.setUriID( userInstance.getUriId() );
        int usertypeid = (userInstance instanceof DeveloperInstance)?1:2;
        Usertype usertype = usertyperepository.findById(new Long(""+usertypeid));
        user.setUsertype( usertype );
        userrepository.store( user );
        
        Util.GenerateSSHKeyPair(user.getId()+"");
        
        return Response.status(Response.Status.CREATED).entity(userInstance.getUriId()).build();
    }

        
    @Override
    public Response storeTurtleUserProfile(String userProfile, String username, String password) throws SOAException{
        logger.debug("received userProfile: "+userProfile);
        logger.debug("received username: "+username);
        logger.debug("received password: "+password);
  
        logger.debug("check if the username already exists: " + username);
        boolean accountNameInUse = isAccountNameInUse(username);
        if(accountNameInUse){
            String error = "The username already exists: "+username;
            logger.debug(error);
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(error).build();
        }
        
        TemporaryRepositoryManager trm;
        try {
            trm = new TemporaryRepositoryManager(userProfile);
        } catch (IOException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Model temporaryModel = trm.getManager().getModel();
        ClosableIterator<Statement> newStatements = temporaryModel.iterator();
        Model c4sModel = null;
        
        try{  
            URI userUriId = null;
            URI typeUriId = null;
            String developerUriString = Developer.class.getAnnotation(RDFBean.class).value();
            String paasUserUriString = PaaSUser.class.getAnnotation(RDFBean.class).value();
            //here add the validations queries
            URI developerUri = new URIImpl( developerUriString, true);
            URI paasUserUri = new URIImpl( paasUserUriString, true);
            
            String query = 
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX user-m: <http://www.cloud4soa.eu/v0.1/user-model#>"
                + "SELECT ?object ?type WHERE { { ?object rdf:type ?type . FILTER ( ?type = "+developerUri.toSPARQL()+") } "
                + "UNION "
                + "{ ?object rdf:type ?type . FILTER ( ?type = "+paasUserUri.toSPARQL()+")  } }";
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
                userUriId = queryRow.getValue("object").asURI();
                typeUriId = queryRow.getValue("type").asURI();
            }
            else{
                String error = "There is no user (Developer / PaaSUser) resource inside the sent statements";
                logger.debug(error);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.TEXT_PLAIN).build();
            }
            if(it.hasNext()){
                String error = "The sent profile has more than one user.";
                logger.debug(error);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.TEXT_PLAIN).build();
            }
            String userUriIdWithoutPrefix = userUriId.toString();
            UserInstance userInstance = null;
            int userType = 0;
            String developerInstanceUri = null;
            try {
                developerInstanceUri = Developer.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
            } catch (NoSuchMethodException ex) {
                logger.error(ex.getMessage());
            } catch (SecurityException ex) {
                logger.error(ex.getMessage());
            }
            if(typeUriId.equals(developerUri)){
                if(userUriId.toString().contains(developerInstanceUri.toString()))
                    userUriIdWithoutPrefix = userUriId.toString().replace(developerInstanceUri.toString(), "");
                userType=1; //Developer
            }
            else { //if(typeUriId == paasUserUri)
                String paasUserInstanceUri = null;
                try {
                    paasUserInstanceUri = PaaSUser.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
                } catch (NoSuchMethodException ex) {
                    logger.error(ex.getMessage());
                } catch (SecurityException ex) {
                    logger.error(ex.getMessage());
                }
                if(userUriId.toString().contains(paasUserInstanceUri.toString()))
                    userUriIdWithoutPrefix = userUriId.toString().replace(paasUserInstanceUri.toString(), "");
                userType=2; //PaaSUser
            }
            
                        
            /*
             * Username and password must be saved into the JSK (java security keystore)
             * (also check if the username already exists)
             */
            
            c4sModel = repositoryManager.getModel();
        
            logger.debug("c4sModel.addAll(newStatements)");
            c4sModel.setAutocommit(false);
            c4sModel.addAll(newStatements);
            c4sModel.commit();
            
            if(userType == 1){try {
                    //Developer
                userInstance = (DeveloperInstance) userProfilesRepository.getUserInstance(userUriIdWithoutPrefix);
                } catch (RepositoryException ex) {
                    throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
                }
                logger.debug("created a DeveloperInstance: "+userInstance.getUriId());
            } else if(userType == 2){
                try {
                    //PaaSUser
                    userInstance = (PaaSUserInstance) userProfilesRepository.getUserInstance(userUriIdWithoutPrefix);
                } catch (RepositoryException ex) {
                    throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
                }
                logger.debug("created a PaaSUserInstance: "+userInstance.getUriId());
            }
            Cloud4SoaAccountInstance cloud4SoaAccountInstance = new Cloud4SoaAccountInstance();
            cloud4SoaAccountInstance.setAccountname(username);
            userInstance.setHoldsaccount(cloud4SoaAccountInstance);
            try {
                userProfilesRepository.updateUserInstance(userInstance);
            } catch (IllegalArgumentException ex) {
                throw new ResourceException(ex.getMessage());
            } catch (RepositoryException ex) {
                throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
            logger.debug("User account stored for instance: "+userInstance.getUriId());
            
            //add the Relational Entry
            User user = new User();
            user.setUsername( userInstance.getAccountname() );
            user.setPassword( password    );
            user.setFullname( userInstance.getFamilyname() + " "+userInstance.getFirstName() );
            user.setUriID( userInstance.getUriId() );
            int usertypeid = (userInstance instanceof DeveloperInstance)?1:2;
            Usertype usertype = usertyperepository.findById(new Long(""+usertypeid));
            List<Usertype> usertypes = usertyperepository.findAll();
            int size = usertypes.size();
            logger.debug("USERTYPES: "+size);
            logger.debug("usertype 1: id="+usertypes.get(0).getId()+" name="+usertypes.get(0).getName());
            logger.debug("usertype 2: id="+usertypes.get(1).getId()+" name="+usertypes.get(1).getName());
            user.setUsertype( usertype );
            userrepository.store( user );            
            
            return Response.status(Response.Status.CREATED).entity(userInstance.getUriId()).type(MediaType.TEXT_PLAIN).build();
            
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
    public UserInstance getUserInstance(String userInstanceUriId) throws SOAException {
        logger.debug("received userInstanceUriId: "+ userInstanceUriId);
        eu.cloud4soa.api.datamodel.core.UserInstance userInstanceRetrieved;
        try {
            userInstanceRetrieved = userProfilesRepository.getUserInstance(userInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(userInstanceRetrieved==null){
            throw new ResourceException("The requested resource " + userInstanceUriId + " does not exists.");
        }
        return userInstanceRetrieved;
    }
    
    
    @Override
    public Response updateUserInstance(UserInstance userInstance) throws SOAException{
        logger.debug("received userInstance: "+userInstance);
        logger.debug("userInstance.getAccountname(): "+userInstance.getAccountname());
        logger.debug("userInstance.getCloud4SoaAccountUriId(): "+userInstance.getCloud4SoaAccountUriId());
        logger.debug("userInstance.getFamilyname(): "+userInstance.getFamilyname());
        logger.debug("userInstance.getFirstName(): "+userInstance.getFirstName());
        logger.debug("userInstance.getGeekcode(): "+userInstance.getGeekcode());
        logger.debug("userInstance.getSurname(): "+userInstance.getSurname());
        logger.debug("userInstance.getUriId(): "+userInstance.getUriId());
        logger.debug("userInstance.getCloud4SoaAccountUriId(): "+userInstance.getCloud4SoaAccountUriId());
        
        try {
            UserInstance existingUserInstance = userProfilesRepository.getUserInstance(userInstance.getUriId());
            if(existingUserInstance!=null)
                userProfilesRepository.updateUserInstance(userInstance);
        } catch (IllegalArgumentException e) {
            String error = "The resource does not exists";
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        }catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        
        return Response.status(Response.Status.ACCEPTED).entity("Account Updated: "+userInstance.getUriId()).build();
    }
    
    @Override
    //TODO Input pending of actual logging implementation 
    /* public UserInstance authenticateUser (String username, String password) throws SOAException { */
    public UserInstance authenticateUser (String username, String password) throws SOAException{
        UserInstance	userInstance;
        try {
            userInstance	= userProfilesRepository.getUserInstanceFromAccountName( username );
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        /*
         * To be checked!
         */
        if(userInstance == null){
            String error = "No user instance found having the username: "+username;
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        }
        
        //DBCheck
        List<User> users = userrepository.find("username = ? AND password = ?", username, password);
        logger.info("users-size: " + users.size());
        
        if(users.size()>1){
            String error = "More than one user found having the username: "+username;
            logger.debug(error);
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, error);
        }
        
        if(users.isEmpty()){
            String error = "Login failed: wrong username or password";
            logger.debug(error);
            throw new SOAException(Response.Status.BAD_REQUEST, error);
        }
 
         
        
        return userInstance;
    }
        
    
    @Override
    public boolean isAccountNameInUse(String accountName) throws SOAException {
        boolean accountNameInUse;
        String accountNameSparql = repositoryManager.getModel().createPlainLiteral(accountName).toSPARQL();
        String query = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX user-m: <http://www.cloud4soa.eu/v0.1/user-model#> "
            + "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>"
            + "SELECT ?object ?type WHERE { { ?object rdf:type ?type . ?object <http://xmlns.com/foaf/0.1/holdsAccount> ?c4sAccount . ?c4sAccount <http://xmlns.com/foaf/0.1/accountName> " + accountNameSparql + " . FILTER ( ?type = user-m:Developer ) } "
            + "UNION "
            + "{ ?object rdf:type ?type . ?object <http://xmlns.com/foaf/0.1/holdsAccount> ?c4sAccount . ?c4sAccount <http://xmlns.com/foaf/0.1/accountName> " + accountNameSparql + " . FILTER ( ?type = user-m:PaaSUser ) } }";

        org.ontoware.rdf2go.model.QueryResultTable resultTable = repositoryManager.getModel().sparqlSelect(query);
        
        
        if(resultTable == null){
            String error = "An error happens when querying the model";
            logger.debug(error);
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, error);
        }
        
        Iterator<QueryRow> it = resultTable.iterator();
        if(it.hasNext()){
            accountNameInUse = true;
            return accountNameInUse;
        }
        
        accountNameInUse = false;
        return accountNameInUse;
    }

    @Override
    public Response storeUserCredentialsForPaaS(String userInstanceUriId, String paaSInstanceUriId, String publicKey, String secretKey, String accountName) throws SOAException {
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
        logger.debug("received paaSInstaneUriId: "+paaSInstanceUriId);
        logger.debug("received publicKey: "+publicKey);
        logger.debug("received secretKey: "+secretKey);
        
        if(paaSInstanceUriId.equalsIgnoreCase("Herokujava")||paaSInstanceUriId.equalsIgnoreCase("HerokuPython")||paaSInstanceUriId.equalsIgnoreCase("HerokuNodejs")||paaSInstanceUriId.equalsIgnoreCase("HerokuRuby")){
          secretKey=publicKey;  
        }
        
        
        if(accountName!=null && !accountName.isEmpty())
            logger.debug("received accountName: "+accountName);
        else
            logger.debug("no accountName received");
        
        UserInstance userInstance;
        try {
            userInstance = userProfilesRepository.getUserInstance( userInstanceUriId );
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(userInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing user having the following uriId: "+userInstanceUriId);
        PaaSInstance paaSInstance;
        try {
            paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(paaSInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing paas offering having the following uriId: "+paaSInstance);
        
        //----------------DataBase interaction ---------------------------------------------------
        //User
        User accountuser = null;
        List<User> userlist = userrepository.findBy("uriID", userInstanceUriId);
        if (userlist != null && !userlist.isEmpty()) {
            accountuser = ((User) userlist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing user entry in the relational DB having with uriId: "+userInstanceUriId);
        }
        
        
        
        Paas accountpaas = null;
        //List<Paas> paaslist = paasrepository.findBy("name", paaSInstance.getProviderTitle());
        List<Paas> paaslist = paasrepository.findBy("uriID", paaSInstanceUriId);
        if (paaslist != null && !paaslist.isEmpty()) {
            accountpaas = ((Paas) paaslist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing paas entry in the relational DB with name: "+paaSInstance.getProviderTitle());
        }

        //create Accounts for the pair userid - paasid
        Account account = new Account();
        account.setPublickey(publicKey.trim()); //i.e. API key
        account.setPrivatekey(secretKey.trim());
        account.setAccountname(accountName);
        account.setUser(accountuser);
        account.setPaas(accountpaas);
        
        accountrepository.store(account);
            
        return Response.status(Response.Status.ACCEPTED).entity("Credential stored for user: " + userInstance.getUriId() + " and paas: "+ paaSInstance.getUriId() ).build();
    }

    @Override
    public Response removeUserCredentialsForPaaS(String userInstanceUriId, String paaSInstanceUriId) throws SOAException {
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
        logger.debug("received paaSInstaneUriId: "+paaSInstanceUriId);
        
        UserInstance userInstance;
        try {
            userInstance = userProfilesRepository.getUserInstance( userInstanceUriId );
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(userInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing user having the following uriId: "+userInstanceUriId);
        PaaSInstance paaSInstance;
        try {
            paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(paaSInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing paas offering having the following uriId: "+paaSInstance);
        
        //----------------DataBase interaction ---------------------------------------------------
        //User
        User accountuser = null;
        List<User> userlist = userrepository.findBy("uriID", userInstanceUriId);
        if (userlist != null && !userlist.isEmpty()) {
            accountuser = ((User) userlist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing user entry in the relational DB having with uriId: "+userInstanceUriId);
        }
        
        Paas accountpaas = null;
        //List<Paas> paaslist = paasrepository.findBy("name", paaSInstance.getProviderTitle());
        List<Paas> paaslist = paasrepository.findBy("uriID", paaSInstanceUriId);
        if (paaslist != null && !paaslist.isEmpty()) {
            accountpaas = ((Paas) paaslist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing paas entry in the relational DB with name: "+paaSInstance.getProviderTitle());
        }
        
        Account account;
        //get Account for the pair userid - paasid
        List<Account> accounts = accountrepository.retrieve(accountuser.getId(), accountpaas.getId());
        if (accounts != null && !accounts.isEmpty()) {
            account = ((Account) accounts.get(0));
        } else {
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing pair user - paas account: " + accountuser.getId() + " - " + accountpaas.getId());
        }
        
        //delete Account for the pair userid - paasid
        accountrepository.delete(account);

        return Response.status(Response.Status.ACCEPTED).entity("Credential removed for user: " + userInstance.getUriId() + " and paas: "+ paaSInstance.getUriId() ).build();
    }

    @Override
    public Response updateUserCredentialsForPaaS(String userInstanceUriId, String paaSInstanceUriId, String publicKey, String secretKey, String accountName) throws SOAException {
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
        logger.debug("received paaSInstaneUriId: "+paaSInstanceUriId);
        logger.debug("received publicKey: "+publicKey);
        logger.debug("received secretKey: "+secretKey);
        if(accountName!=null && !accountName.isEmpty())
            logger.debug("received accountName: "+accountName);
        else
            logger.debug("no accountName received");
        
        UserInstance userInstance;
        try {
            userInstance = userProfilesRepository.getUserInstance( userInstanceUriId );
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(userInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing user having the following uriId: "+userInstanceUriId);
        PaaSInstance paaSInstance;
        try {
            paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(paaSInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing paas offering having the following uriId: "+paaSInstance);
        
        //----------------DataBase interaction ---------------------------------------------------
        //User
        User accountuser = null;
        List<User> userlist = userrepository.findBy("uriID", userInstanceUriId);
        if (userlist != null && !userlist.isEmpty()) {
            accountuser = ((User) userlist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing user entry in the relational DB having with uriId: "+userInstanceUriId);
        }
        
        Paas accountpaas = null;
        //List<Paas> paaslist = paasrepository.findBy("name", paaSInstance.getProviderTitle());
        List<Paas> paaslist = paasrepository.findBy("uriID", paaSInstanceUriId);
        if (paaslist != null && !paaslist.isEmpty()) {
            accountpaas = ((Paas) paaslist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing paas entry in the relational DB with name: "+paaSInstance.getProviderTitle());
        }
        
        Account account;
        //get Account for the pair userid - paasid
        List<Account> accounts = accountrepository.retrieve(accountuser.getId(), accountpaas.getId());
        if (accounts != null && !accounts.isEmpty()) {
            account = ((Account) accounts.get(0));
        } else {
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing pair user - paas account: " + accountuser.getId() + " - " + accountpaas.getId());
        }
        
        //Updating values
        account.setPrivatekey(secretKey.trim());
        account.setPublickey(publicKey.trim());
        account.setAccountname(accountName);
        
        accountrepository.update(account);

        return Response.status(Response.Status.ACCEPTED).entity("Credential updated for user: " + userInstance.getUriId() + " and paas: "+ paaSInstance.getUriId() ).build();
    }

    @Override
    public UserPaaSCredentials readUserCredentialsForPaaS(String userInstanceUriId, String paaSInstanceUriId) throws SOAException {
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
        logger.debug("received paaSInstaneUriId: "+paaSInstanceUriId);
        
        UserInstance userInstance;
        try {
            userInstance = userProfilesRepository.getUserInstance( userInstanceUriId );
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(userInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing user having the following uriId: "+userInstanceUriId);
        PaaSInstance paaSInstance;
        try {
            paaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(paaSInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing paas offering having the following uriId: "+paaSInstance);
        
        //----------------DataBase interaction ---------------------------------------------------
        //User
        User accountuser = null;
        List<User> userlist = userrepository.findBy("uriID", userInstanceUriId);
        if (userlist != null && !userlist.isEmpty()) {
            accountuser = ((User) userlist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing user entry in the relational DB having with uriId: "+userInstanceUriId);
        }
        
        Paas accountpaas = null;
        //List<Paas> paaslist = paasrepository.findBy("name", paaSInstance.getProviderTitle());
        List<Paas> paaslist = paasrepository.findBy("uriID", paaSInstanceUriId);
        if (paaslist != null && !paaslist.isEmpty()) {
            accountpaas = ((Paas) paaslist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing paas entry in the relational DB with name: "+paaSInstance.getProviderTitle());
        }
        
        Account account;
        //get Account for the pair userid - paasid
        List<Account> accounts = accountrepository.retrieve(accountuser.getId(), accountpaas.getId());
        if (accounts != null && !accounts.isEmpty()) {
            account = ((Account) accounts.get(0));
        } else {
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing key pair for PaaS: " + paaSInstance.getProviderTitle());
        }
        
        //Getting values from DB account
        String publickey = account.getPublickey();
        String privatekey = account.getPrivatekey();
        String accountname = account.getAccountname();
        
        //Creating SOA serializable bean representing the credentials
        UserPaaSCredentials userPaaSCredentials = new UserPaaSCredentials(userInstanceUriId, paaSInstanceUriId, publickey, privatekey, accountname);

        return userPaaSCredentials;
    }

    @Override
    public List<UserPaaSCredentials> readAllUserCredentialsForPaaS(String userInstanceUriId) throws SOAException {
        logger.debug("received userInstanceUriId: "+userInstanceUriId);
        
        UserInstance userInstance;
        try {
            userInstance = userProfilesRepository.getUserInstance( userInstanceUriId );
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if(userInstance == null)
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing user having the following uriId: "+userInstanceUriId);
        
        //----------------DataBase interaction ---------------------------------------------------
        //User
        User accountuser = null;
        List<User> userlist = userrepository.findBy("uriID", userInstanceUriId);
        if (userlist != null && !userlist.isEmpty()) {
            accountuser = ((User) userlist.get(0));
        } else {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, "No existing user entry in the relational DB having with uriId: "+userInstanceUriId);
        }

        List<UserPaaSCredentials> userPaaSCredentialsList = new ArrayList<UserPaaSCredentials>();
        
        //get all the Account for the userid 
//        List<Account> accounts = accountrepository.findBy("user", accountuser);
        List<Account> accounts = accountrepository.retrieveAll(accountuser.getId());
        if (accounts != null) {
            
            for (Account account : accounts) {
                //Getting values from DB account
                String paaSInstanceUriId = account.getPaas().getUriID();
                String publickey = account.getPublickey();
                String privatekey = account.getPrivatekey();
                String accountname = account.getAccountname();

                //Creating SOA serializable bean representing the credentials
                UserPaaSCredentials userPaaSCredentials = new UserPaaSCredentials(userInstanceUriId, paaSInstanceUriId, publickey, privatekey, accountname);
                
                userPaaSCredentialsList.add(userPaaSCredentials);
            }
            
        } else {
            throw new SOAException(Response.Status.BAD_REQUEST, "No existing paas credentials for user" + accountuser.getId());
        }

        return userPaaSCredentialsList; 
    }
    
       	
    public void close() {
        
    }

}
