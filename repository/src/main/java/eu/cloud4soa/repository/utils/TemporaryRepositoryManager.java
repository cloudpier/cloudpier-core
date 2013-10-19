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

import com.viceversatech.rdfbeans.RDFBeanManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.MalformedQueryException;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.Syntax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vins
 */
public class TemporaryRepositoryManager {
    
    private static final Logger logger = LoggerFactory.getLogger( TemporaryRepositoryManager.class );
    
    protected Model model;
    protected String rdf2GOImplementation = "org.openrdf.rdf2go.RepositoryModelFactory";
    protected String backend = "";
    protected RDFBeanManager manager;
    
    public TemporaryRepositoryManager(String stringModel) throws IOException{
        // setting rdf2go implementation (Jena or Sesame) 
//            RDF2Go.register( this.rdf2GOImplementation ); 

            logger.info("creating the model ");
            model = this.modelBuilder();
            model.open();

            logger.info("Loading data into model");
            this.loadDataIntoModel(model, stringModel);
            
            logger.info("Creating the RDFBeanManager");
            manager = new RDFBeanManager(model);
            manager.setAutocommit(true);
            
            logger.info("Repository successfully created");
            
//        } catch( Exception e) {
//            logger.error(" Cannot setup the repository", e);
//            closeModel();
//        } 
    }
    
    protected Model modelBuilder(){
        //----------------------- RDF2go --------------------------	
        ModelFactory modelFactory = RDF2Go.getModelFactory();
        return modelFactory.createModel( );
    }
    
    protected void readDataFromString(Model model, String stringModel) throws IllegalArgumentException, IOException {
        if(stringModel==null || stringModel.isEmpty() )
            throw new IllegalArgumentException("stringModel==null || stringModel.isEmpty()");
        StringReader stringReader = new StringReader(stringModel);
        Syntax syntax; 
//        Syntax.forName(syntaxName);
        try {
            model.readFrom(stringReader, Syntax.Turtle);
//                    model.readFrom(resourceStream);
            if (stringReader != null) {
                    stringReader.close();
            }
        } catch (ModelRuntimeException e) {
                logger.error("Error in loading data in the model ", e);
        } catch (IOException e) {
                logger.error("Error in loading data in the model ", e);
                throw e;
        }
    }
    
        
    protected void loadDataIntoModel(Model model, String modelString) throws IOException{     
            logger.debug( "Loading data into model from string: " + modelString );
            this.readDataFromString( model, modelString);
    }
    
    protected InputStream getResourceStream( String filename ) throws FileNotFoundException, IllegalArgumentException{
        InputStream resourceStream = null;
        File file = new File( filename );
        String abs = file.getAbsolutePath();
        
        if (!file.exists() && this.model.isEmpty() ) {
            throw new FileNotFoundException("File " + filename + " not found!");
        } else {
            logger.debug("Reading from " + filename);
            
            resourceStream = new FileInputStream(file);

            if (resourceStream == null) {
                throw new IllegalArgumentException( "Could not open file " + filename  );
            }
        }

        return resourceStream;
    }
    
    public QueryResultTable sparqlSelect(String query) throws MalformedQueryException, ModelRuntimeException{ 
        return model.sparqlSelect(query);
    }

    public RDFBeanManager getManager() {
        return manager;
    }


}
