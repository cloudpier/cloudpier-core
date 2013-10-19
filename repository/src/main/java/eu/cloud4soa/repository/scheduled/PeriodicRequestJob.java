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
package eu.cloud4soa.repository.scheduled;

import eu.cloud4soa.api.repository.SearchAndDiscoveryInterfaces;
import eu.cloud4soa.api.util.exception.SparqlQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author frarav
 */
public class PeriodicRequestJob extends QuartzJobBean {
    
    private static Logger logger = LoggerFactory.getLogger( PeriodicRequestJob.class );
    protected static final String QUERY = 
            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>           " +
            "PREFIX user-m:<http://www.cloud4soa.eu/v0.1/user-model#>           " +
            "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>                     " +
            "PREFIX essential-metamodel:<http://www.enterprise-architecture.org/essential-metamodel.owl#> "+
            "PREFIX c4s-app-m:<http://www.cloud4soa.eu/v0.1/application-domain#> " +
            " SELECT ?s ?o                                                      " +
            " WHERE {                                                           " +
            "   ?s rdf:type essential-metamodel:Application .                   " +
            "   ?s c4s-app-m:hasOwner ?o                                        " +
            " }                                                                 ";
    
    protected SearchAndDiscoveryInterfaces discoveryModule;

    

    public void setDiscoveryModule(SearchAndDiscoveryInterfaces discoveryModule) {
        this.discoveryModule = discoveryModule;
    }
   
    
    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jec) throws org.quartz.JobExecutionException {
        try {
            logger.info( " ** Executing the periodic sparql query **");
            discoveryModule.sparqlSelect( QUERY );
            logger.info( " ** periodic sparql query successfully executed **");
        } catch (SparqlQueryException sqe) {
            logger.error( "Error in executing periodic sparql query", sqe);
        }    
    }
    
    
    
}
