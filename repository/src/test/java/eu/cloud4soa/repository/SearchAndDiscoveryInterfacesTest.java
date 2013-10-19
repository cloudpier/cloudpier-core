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

import eu.cloud4soa.api.datamodel.repository.QueryResultRow;
import eu.cloud4soa.api.datamodel.repository.QueryResultTable;
import eu.cloud4soa.repository.utils.RepositoryManager;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;

/**
 *
 * @author frarav
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:SearchAndDiscoveryInterfacesCtx.xml"})
public class SearchAndDiscoveryInterfacesTest {
    
    @Autowired
    private SearchAndDiscoveryInterfaces searchAndDiscoveryInterfaces;
    
    
    @Ignore @Test
    public void testSparqlSelect() throws Exception {
        
        String              rdf;
        String              query;
        QueryResultTable    result;
        int                 counter;
        
        counter = 0;
        rdf     = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        query   = "SELECT ?object WHERE { ?object <" + rdf + "type>  <"+ rdf + "Property> }";
        
        System.out.println(query);
        result  = searchAndDiscoveryInterfaces.sparqlSelect(query);
        
        for( QueryResultRow row: result) {
            counter++;
            System.out.println( row.getStringValue( "object") );
        }
        
        
        assertTrue( counter > 0 );
        System.out.println("Number of properties: " + counter );
        
    }
    
    
    
    @Ignore @Test
    public void testSparqlSelectSecond() throws Exception {
        
        
        String              dct;
        String              rdfs;
        String              query;
        String              varname;
        QueryResultTable    result;
        int                 counter;
        
        counter = 0;
        dct     = "http://purl.org/dc/terms/";
        rdfs    = "http://www.w3.org/2000/01/rdf-schema#";
        varname = "aComment";
        query   = "SELECT ?"+ varname +" WHERE { <"+ dct + "MediaType> <" + rdfs + "comment> ?" + varname +"  }";
        
        System.out.println(query);
        result  = searchAndDiscoveryInterfaces.sparqlSelect(query);
        
        System.out.println("Number of properties: " + counter );
        
        for( QueryResultRow row: result) {
            counter++;
            System.out.println( row.getStringValue(varname) );
        }
        
        assertEquals( 1, counter);
    }
}
