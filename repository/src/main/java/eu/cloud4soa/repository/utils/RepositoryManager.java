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
package eu.cloud4soa.repository.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.impl.jena26.ModelImplJena26;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.openrdf.rdf2go.RepositoryModel;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.sail.SailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.sdb.util.StoreUtils;
import com.viceversatech.rdfbeans.RDFBeanManager;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vins
 */
public class RepositoryManager {
	
    protected   final static String	    DEFAULT_REPO_FILE_NAME	= "repositoryFile.rdf";
    protected   final static String	    DATA_FILE_NAME          = "dataFile.ttl";
    protected   final static String	    ONTOLOGY_FILE_NAME      = "c4sOntology.ttl";
    protected   final static String     ONTOLOGY_PATH           = "ontology";
    
    protected   final static String	    FILE_BACKEND_OPT        = "FILE";
    protected   final static String     DATABASE_BACKEND_OPT    = "DATABASE";
    protected   final static String	    EXT_SERVER_BACKEND_OPT  = "EXT_SERVER";
    
    protected   final static String     HOME_DIR_PROPERTY       = "c4s_home";    
    protected   final static String     PROPERTY_FILE_NAME      = "repository.properties";
    protected   final static String     REPO_NAME_PROPERTY      = "fileBackend.fileName";
    protected   final static String     RDF2GO_IMPLEMENTATION   = "implementation";
    protected   final static String     BACKEND_PROPERTY        = "backend";
    protected   final static String     DB_CONF_PROPERTY        = "dbBackend";
    protected   final static String     SESAME_CONF_PROPERTY    = "extServerBackend";
    protected   final static String     SESAME_SERVER_URL_PROP  = "serverUrl";
    
    protected   enum      SUPPORTED_DB { 
        mysql, 
        hsqldb; 
        
        public static boolean contains(String s) 
        {
                for(SUPPORTED_DB elem :values())
                    if (elem.name().equals(s)) 
                    return true;
                return false;
          }

    
    }
 
    //Properties loaded by using Spring PropertyPlaceholderConfigurer
    @Value("ORD{rdf2go.jena.implementationClass}") 
                                          protected String JENA_MODEL_FACTORY = "";
    @Value("ORD{rdf2go.implementationClass}") 
                                          protected String rdf2GOImplementation = "";
    @Value("ORD{backend.file.name}")      protected String repoFileName = "";
    @Value("ORD{backend.type}" )          protected String backend      = "";
    @Value("ORD{backend.ext.serverUrl}")  protected String sesameServerUrl = "";
 /*   @Value("ORD{backend.db.type}")  */  protected String dbType       = "";
    
    @Value("ORD{dataSource.host}")        protected String dbHost       = "";
    @Value("ORD{dataSource.port}")        protected String dbPort       = "";
    @Value("ORD{dataSource.database}")    protected String dbName       = "";
    @Value("ORD{dataSource.user}")        protected String dbUser       = "";
    @Value("ORD{dataSource.password}")    protected String dbPass       = "";
    @Value("ORD{dataSource.driverClass}") protected String dbDriver     = "";
    @Value("ORD{dataSource.protocol}")    protected String dbProtocol   = "";
    @Value("ORD{dataSource.properties}")  protected String dbProperties = "";
    
    private static final Logger logger = LoggerFactory.getLogger( RepositoryManager.class );
  
    protected   RDFBeanManager                  manager;
    protected   SesameMysqlRepository           mysqlRepository;
    protected   HTTPRepository                  sesameServerRepository;    
    protected   Model                           rdf2goModel;    
    protected   SDBConnection                   sdbConn;
    protected   Store                           store;
    protected   com.hp.hpl.jena.rdf.model.Model jenaModel;
    @Autowired
    protected DataSource                         dataSource;

    
	
     /**
     * @return the manager
     */
    public RDFBeanManager getManager() {
        return manager;
    }

    public Model getModel() {
        return rdf2goModel;
    }
    
 
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    
    public void initRepository() throws Exception {
        logger.info("***********************************");
            
        this.setDbType();
         RDF2Go.register( this.rdf2GOImplementation ); 

        logger.info("creating the model ");
        rdf2goModel   = this.modelBuilder( this.backend );
        rdf2goModel.open();
 
        if ( rdf2goModel.isEmpty() ) {
            logger.info("Loading data into model");
            this.loadDataIntoModel(rdf2goModel, backend);
        }     

        logger.info("Creating the RDFBeanManager");
        manager = new RDFBeanManager(rdf2goModel);
        manager.setAutocommit(true);
        logger.info("Repository successfully created");
           
    }
    
    
    protected void loadDataIntoModel(Model model, String backendOption) throws Exception {
        
        if( this.backend.equals( FILE_BACKEND_OPT ) ) {
            logger.debug( "Loading data into model from " + this.repoFileName );
            
            this.readDataFromFile( model, this.repoFileName);
        } else if ( model.isEmpty() ) {
            logger.debug("Loading data into model from default ontology " + this.ONTOLOGY_FILE_NAME );
            this.readDataFromFile( model, ONTOLOGY_FILE_NAME);
        }
    }
		
    
    
    protected void readDataFromFile(Model model, String filename) throws Exception {
            InputStream		resourceStream;
            File			file;
            Syntax			syntax;

            resourceStream	= this.getResourceStream(filename);

            try {
                    model.readFrom(resourceStream, Syntax.Turtle);
//                    model.readFrom(resourceStream);
                    if (resourceStream != null) {
                            resourceStream.close();
                    }
            } catch (ModelRuntimeException e) {
                    logger.error("Error in loading data in the model ", e);
                    throw e;
            } catch (FileNotFoundException e) {
                    logger.error("Error in loading data in the model ", e);
                    throw e;
            } catch (IOException e) {
                    logger.error("Error in loading data in the model ", e);
                    throw e;
            }
    }
        
	
    protected InputStream getResourceStream( String filename ) {
        InputStream		resourceStream = null;
        File			file;

        file			= new File( filename );
        String abs = file.getAbsolutePath();
        if (!file.exists() && this.rdf2goModel.isEmpty() ) {
            logger.debug(" File " + filename + " not found; reading from " + RepositoryManager.ONTOLOGY_FILE_NAME);
            resourceStream	= RepositoryManager.class.getClassLoader().getResourceAsStream( RepositoryManager.ONTOLOGY_PATH+"/"+RepositoryManager.ONTOLOGY_FILE_NAME);
        } else {
            logger.debug("Reading from " + filename);
            try {
                resourceStream = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                logger.error( "Problems reading file " + filename, ex);
            }
            if (resourceStream == null) {
                throw new IllegalArgumentException( "Could not open file " + filename  );
            }
        }

        return resourceStream;
    } 

    
    
    private void ensureFileExistsOrCopyFromDefault(File configFolder, String name, String defaultSourceName) throws IOException {
        File target = new File(configFolder,name);
        if(!target.exists()){
            copyFromResourceToFile( defaultSourceName ,target);
        }
    
    }
	
	
    private void copyFromResourceToFile(String resource, File target) throws IOException {
        try{
            InputStream in = RepositoryManager.class.getClassLoader().getResourceAsStream(resource);
            if(in == null){
                logger.warn(" %%%missing "+resource+" from classpath");
            }else{
                // try to write the application logging file so admin can change it.
                try{
                    target.getParentFile().mkdirs();
                    FileOutputStream out = new FileOutputStream(target);
                    try{
                        byte[] buff = new byte[1024];
                        int read = 0;
                        while((read = in.read(buff))>0){
                            out.write(buff,0,read);
                        }
                    }finally{
                        out.close();
                    }
                }finally{
                    in.close();
                }
            }
        }catch(IOException e){
            logger.warn("couldn't write file "+target, e);
            throw e;
        }
    }
    
    
    protected File ensureDirectory( String directoryName ) throws IOException {
        File    configFolder;
    
        configFolder = new File( directoryName );
        if( !(configFolder.exists() && configFolder.isDirectory()) ){
            logger.warn("Missing configuration folder "+configFolder);
            if(configFolder.mkdirs()){
                logger.warn("Creating default configuration at "+configFolder);
            }else{
                logger.error("Cannot create directory " + directoryName);
                throw new IOException("Cannot create directory " + directoryName);
            }
         }
        return configFolder;
    }    
    
    
    
    public void closeModel() {
        FileOutputStream	outputStream;
        Model				usedModel;

        logger.debug(" ****************    Closing the model    ***************");
        usedModel	= manager.getModel();
        
        if (this.backend.equals(FILE_BACKEND_OPT)) {
            try {
                outputStream	= new FileOutputStream( this.repoFileName );

                usedModel.writeTo(outputStream, Syntax.Turtle);
                
                outputStream.close();
            } catch (FileNotFoundException fnfe) {
                logger.error( "Problems handling writing into file " + repoFileName, fnfe);
            } catch ( IOException ioe) {
                logger.error( "Problem writing model into file " + repoFileName, ioe );
            }    
        } else if ( this.backend.equals( DATABASE_BACKEND_OPT) ) {
            this.sdbConn.close();
        } 
        
        usedModel.close();
/*        
        try { 
            this.mysqlRepository.shutDown();
        } catch (SailException se) {
            logger.error( "Problems shutting down repository", se);
        }
 * 
 */
        

    }
    

    
    protected Model getDatabaseRepository() throws Exception {
        Model           model;
        Repository      repo;
        
        
        logger.info("configuring repository on " + this.dbType + " database: " + this.getDBConnectString() );
        if ( this.hasJenaImplementation() ) {
            logger.info("Loading db properties file");

            // Building the Store object loading it from a ttl configuration file
//            String  configurationPath = "sdbconfig.ttl";
//            logger.error("CONFIGURATION FILE: " + configurationPath);
//            store = this.getStoreFromTTLConfigurationFile( configurationPath);
                        
            // Building the Store from the PlaceHolder properties
            StoreDesc     storeDesc = new StoreDesc( LayoutType.LayoutTripleNodesHash.getName(), this.dbType );
//            this.sdbConn            = new SDBConnection( this.getDBConnectString(), this.dbUser, this.dbPass);

//            Connection  con = DataSourceUtils.doGetConnection( dataSource );
//            logger.debug( "Is the JDBC connection transactional? " + DataSourceUtils.isConnectionTransactional(con, dataSource));
//            this.sdbConn  = new SDBConnection( con );
            
            this.sdbConn  = new SDBConnection(dataSource);
            // enable query logging (log level must be set to INFO in logback.xml in order to actually get the query logged)
            this.sdbConn.setLogSQLStatements(true);
           
             
         
            this.store                   = SDBFactory.connectStore( sdbConn, storeDesc);
            
             // in case of in memory database, first I remove previous db
  //          if ( dbType.equals("hsqldb") && dbProtocol.substring(0, 15).equals("jdbc:hsqldb:mem")  ) {
  //              this.store.getTableFormatter().format();
  //          }
            
            if ( !StoreUtils.isFormatted( this.store) ) {
                this.store.getTableFormatter().create();
            }

            jenaModel   = SDBFactory.connectDefaultModel(  this.store );
            
            model       = new ModelImplJena26( jenaModel );
            
            
        } else {
            mysqlRepository = new SesameMysqlRepository();
            repo            = new SailRepository( mysqlRepository ); 
            logger.info("initializing repository on mysql database ");
            try {
                repo.initialize();
                logger.info("Repository initialized");
            } catch (RepositoryException e) {
                logger.error( "Cannot initialize repository", e);
                throw e;
            }
            
            model = new RepositoryModel( repo );
        }        
        
        return model;
    }
    
    
    protected Store getStoreFromTTLConfigurationFile(String fileUrl) throws Exception {
        Store store = null;
        String  configurationPath = fileUrl;
        
        logger.error("CONFIGURATION FILE: " + configurationPath);
        try {

            File file = null;
            OutputStream out = null;

            file = File.createTempFile("sdbconfigTemp", ".ttl");
            out=new FileOutputStream(file);
            IOUtils.copy(RepositoryManager.class.getClassLoader().getResourceAsStream(configurationPath), out);
            out.flush();
            out.close();
            store       = SDBFactory.connectStore(file.getAbsolutePath());
            file.deleteOnExit();
 
        } catch (Exception exception) {
            logger.error("CONFIGURATION ERROR while configure Store from the ttl file: " + configurationPath, exception);
            throw exception;
        }
            
         return store;   
    }      
    
    
    
    protected Repository getExternalRepository() throws RepositoryException {
        Repository repository;
        
        logger.debug("|||| Creating the httpRepository" + this.sesameServerUrl );
        this.sesameServerRepository = new HTTPRepository( this.sesameServerUrl);
        logger.debug("|||| Created the httpRepository: " + this.sesameServerUrl  );
        try {
            sesameServerRepository.initialize();
        } catch (RepositoryException re) {
            logger.error( "Error initializing the repository",re);   
            throw re;
        }    
   
       logger.debug("Repository initialized");
        
  //      model = new RepositoryModel ( this.sesameServerRepository );
        
        return this.sesameServerRepository;
    }
    
    
    
    protected Model getFileRepository() throws IOException {

        //----------------------- RDF2go --------------------------	
        ModelFactory modelFactory = RDF2Go.getModelFactory();
        return modelFactory.createModel( );
    }
    
    
    protected Model modelBuilder( String backedType) throws Exception {
        Repository  resultingRepository;
        Model       resultingModel;
        
        
        if ( backedType.equals( FILE_BACKEND_OPT ) ) {
            resultingModel = this.getFileRepository();
        } else {
            if ( backedType.equals( DATABASE_BACKEND_OPT) ) {  
                
                resultingModel = this.getDatabaseRepository();
            } else {
                resultingRepository = this.getExternalRepository();
                resultingModel = new RepositoryModel( resultingRepository );
            }    
             
        }
        
        return resultingModel;
    }
    
    
    
    protected boolean hasJenaImplementation() {
        return ( this.rdf2GOImplementation.equals( JENA_MODEL_FACTORY ) );
    }
    

//    protected Properties getDProperties() {
//        Properties  jenaProperties;
//        
//        jenaProperties  = new Properties();
//        jenaProperties.setProperty( ModelFactoryImpl.BACKEND,           this.backend);
//        jenaProperties.setProperty( ModelFactoryImpl.DB_CONNECT_STRING, this.getDBConnectString() );
//        jenaProperties.setProperty( ModelFactoryImpl.SQL_DRIVERNAME,    this.getSqlDriver());
//        jenaProperties.setProperty( ModelFactoryImpl.DB_USER,           this.getDBUser());
//        jenaProperties.setProperty( ModelFactoryImpl.DB_PASSWD,         this.getDBPass());
//        
//        return jenaProperties;
//    }
    
    
    protected String getDBConnectString() {
        StringBuffer dbConnectString;
        String serverName;
        String portNumber;
        
        dbConnectString = new StringBuffer("");
        dbConnectString.append( this.dbProtocol);
        if ( dbHost != null && !dbHost.equals("")) {       
            if (    dbConnectString.length() > 2 &&
                    !dbConnectString.substring( dbConnectString.length() - 2, dbConnectString.length() ).equals("//")
                    ) {
                logger.info( "^^^^^^ " + dbConnectString.substring( dbConnectString.length() - 2, dbConnectString.length() ));
                dbConnectString.append("//"); 
            }
            dbConnectString.append(this.dbHost);
            if ( this.dbPort != null && !this.dbPort.equals("") ) {
                dbConnectString.append( ":").append( this.dbPort );
            }
            if ( this.dbName != null && !dbName.equals("") ) {
                dbConnectString.append("/").append( this.dbName );
            }    
        }
        if (dbProperties != null ) {
            dbConnectString.append(dbProperties);
        }
        
/*        if ( this.dbType.equals("hsqldb") ) {
            dbConnectString.append(";sql.enforce_size=false");
        }
 * 
 */

        
        logger.debug( "DB connection string: " + dbConnectString.toString());
        return dbConnectString.toString();
    }
    
    
    public void closeStore() {
        this.store.close();
    }
    
    
    
    public boolean checkAutocommit() {
        
        return this.jenaModel.supportsTransactions();
       
    }
    
    
    public void rollbackTxSemRepo() {        
        this.jenaModel.abort();
       
    }
    
    
    public void commitTxSemRepo() {
        this.jenaModel.commit();
        this.manager.setAutocommit(true);
       
    }
    
    public void beginTxOnSemRepo() {
        this.manager.setAutocommit(false);
        this.jenaModel.begin();
    }
    
    
    
    protected void setDbType() throws Exception {
        String dbType;
        
        dbType = this.dbProtocol.substring( this.dbProtocol.indexOf(":") + 1  );
        dbType = dbType.substring(0, dbType.indexOf(":") );
        
        logger.debug( "database type: " + dbType);
        
        if ( SUPPORTED_DB.contains( dbType ) ) {
            this.dbType = dbType;
        } else {
            throw new Exception("Unknown database type");
        }
    }
          
}