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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.DatatypeLiteral;
import org.ontoware.rdf2go.util.TypeConverter;
import org.ontoware.rdf2go.vocabulary.XSD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vlaudizio
 */
public class ComputationalConverter {

    private static final Logger logger = LoggerFactory.getLogger(ComputationalConverter.class);
    private RepositoryManager repositoryManager;

    @Autowired
    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }
    private static final String infrastructurePrefix = "c4s-inf-m";
    private List<String> alreadyExplored;

    public String convertComputationalComponent(Float minComputationalPowerFactor, String componentCategoryRelativeURI, Integer hwCounter) {
        String queryString;
        
        logger.info(" _________ converti the category _________");
        init();
        String infrastructureNamespace = repositoryManager.getModel().getNamespace(infrastructurePrefix);
        String componentCategoryURI = infrastructureNamespace + componentCategoryRelativeURI;
        queryString = recursiveMethod(componentCategoryURI, minComputationalPowerFactor, hwCounter);
        logger.info(" _________ conversion done _________");
        
        return queryString;
    }

    private void init() {
        alreadyExplored = new ArrayList<String>();
    }

    private String recursiveMethod(String componentCategoryURI, Float minComputationalPowerFactor, Integer hwCounter) {
        Map<String, Float> map;
        String recursiveQueryPiece;
        Float convertedMinComputationalPowerFactor;
        
        convertedMinComputationalPowerFactor = minComputationalPowerFactor;
        alreadyExplored.add(componentCategoryURI);
        map = newMethod(componentCategoryURI);
        String queryString = "";
        if (!map.isEmpty()) {
            for (String convertedComponentCategoryURI : map.keySet()) {
                if (!alreadyExplored.contains(convertedComponentCategoryURI)) {
                    convertedMinComputationalPowerFactor = minComputationalPowerFactor * map.get(convertedComponentCategoryURI);
                    String localString = createSparqlCriteria(convertedComponentCategoryURI, convertedMinComputationalPowerFactor, hwCounter);
                    queryString += localString;
                    logger.info(" ****** componentCategoryURI " + componentCategoryURI + 
                            "; componentCategoryURI.substring(1,3) " + componentCategoryURI.substring(1,3));
                    logger.info(" ****** componentCategoryURI.contains(ECU) " + componentCategoryURI.contains( "ECU" ));
                    if (!componentCategoryURI.substring(1,3).equals("ECU")) {
                        recursiveQueryPiece = recursiveMethod(convertedComponentCategoryURI, convertedMinComputationalPowerFactor, hwCounter);
                        queryString += recursiveQueryPiece;
                    } else {
                        recursiveQueryPiece = findingReverseRelations(componentCategoryURI, minComputationalPowerFactor, hwCounter);
                        queryString += recursiveQueryPiece;
                    }             
                }              
            }
                 
        }
        
        if ( componentCategoryURI.contains( "ECU" ) ) {
            recursiveQueryPiece = findingReverseRelations(componentCategoryURI, minComputationalPowerFactor, hwCounter);
            queryString += recursiveQueryPiece;
        }
        
        return queryString;
    }
    
    
    protected String createSPARQLCriterias4EqCategories( Map<String, Float> categories, Float minComputationalPowerFactor, Integer hwCounter ) {
        String queryString;
        
        queryString = "";
        for (String convertedComponentCategoryURI : categories.keySet()) {
            if (!alreadyExplored.contains(convertedComponentCategoryURI)) {
                Float convertedMinComputationalPowerFactor = minComputationalPowerFactor * categories.get(convertedComponentCategoryURI);
                String localString = createSparqlCriteria(convertedComponentCategoryURI, convertedMinComputationalPowerFactor, hwCounter);
                queryString += localString;                 
            }
        }
        
        return queryString;
    }
    
    
    protected String createSparqlCriteria( String componentURI, Float minComputationalPowerFactor, Integer hwCounter) {
        String sparqlCriteria;
        
        sparqlCriteria = 
            "UNION "
            + "{"
                + "?hw" + Integer.toString(hwCounter) + 
                    " ea:realisation_of_technology_capability " +  
                    repositoryManager.getModel().createURI(componentURI).toSPARQL() + " ."
                + "?hw" + Integer.toString(hwCounter) + 
                    " inf-m:computationalPowerFactor ?box_cpf_range" + 
                    Integer.toString(hwCounter) + " ."
                + "?box_cpf_range" + Integer.toString(hwCounter) + 
                    " measure:hasMaxNumericValue ?box_cpf_val" + 
                    Integer.toString(hwCounter) + " ."
                + "FILTER ("
                    + " ?box_cpf_val" + Integer.toString(hwCounter) + 
                    ">=" + 
                    minComputationalPowerFactor + ")"
            + "}";
        
        return sparqlCriteria;
    }

    
    
    private Map<String, Float> newMethod(String componentCategoryURI) {
        Map<String, Float> map = new HashMap<String, Float>();
        //Obtain equivalence rule details if any
        String query =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX c4s-inf-m: <http://www.cloud4soa.eu/v0.1/infrastructural-domain#>"
                + "PREFIX conv: <http://usoa.deri.ie/ontology/conversion_v.0.1#>"
                + "PREFIX common-m: <http://www.cloud4soa.eu/v0.1/other#>"
                + "SELECT ?targetCategory ?conversionRate "
                + "WHERE { "
                + "?eqRule a common-m:EquivalenceRuleHardwareCategory."
                + "?eqRule common-m:hasSource " + repositoryManager.getModel().createURI(componentCategoryURI).toSPARQL() + "."
                + "?eqRule common-m:hasTarget ?targetCategory."
                + "?eqRule conv:hasConversionRate ?conversionRate."
                + "}";
        logger.debug(query);
        QueryResultTable resultTable = repositoryManager.getModel().sparqlSelect(query);

        if (resultTable == null) {
            String error = "An error happens when querying the model";
            logger.error(error);
            throw new RuntimeException(error);
        }
        ClosableIterator<QueryRow> it = resultTable.iterator();
        QueryRow queryRow;
        if (it.hasNext()) {
            while (it.hasNext()) {
                queryRow = it.next();
                String targetCategoryUriId = queryRow.getValue("targetCategory").asURI().toString();
                DatatypeLiteral conversionRateLiteral = queryRow.getValue("conversionRate").asDatatypeLiteral();
                //Convert the conversion rate value if possible
                float conversionRateFloat;
                if (conversionRateLiteral.getDatatype().equals(XSD._float)) {
                    conversionRateFloat = TypeConverter.toFloat(conversionRateLiteral);
                    //adding the new equivalent rule to the hashmap
                    map.put(targetCategoryUriId, conversionRateFloat);
                } else {
                    String error = "Found an EquivalenceRuleHardwareCategory which conversionRate is not a Float!";
                    logger.error(error);
                    continue;
                }

            }
        } else {
            String info = "There is no EquivalenceRuleHardwareCategory for the following Harware Category: " + componentCategoryURI;
            logger.debug(info);
        }
        return map;
    }

    
    
    public boolean compareComputationalPowerFactor(Float minAppComputationalPowerFactor,
            Float maxPaasComputationalPowerFactor,
            String appComponentCategoryRelativeURI, String paasComponentCategoryRelativeURI) {

        boolean found = false;
        if (appComponentCategoryRelativeURI.equals(paasComponentCategoryRelativeURI)) {
            found = true;
        } else {
            String infrastructureNamespace = repositoryManager.getModel().getNamespace(infrastructurePrefix);
            String appComponentCategoryURI = infrastructureNamespace + appComponentCategoryRelativeURI;
            String paasComponentCategoryURI = infrastructureNamespace + paasComponentCategoryRelativeURI;

            float conversionRate = getConversionRate(appComponentCategoryURI, paasComponentCategoryURI);

            if (conversionRate != -1) {
                found = true;
                 minAppComputationalPowerFactor = minAppComputationalPowerFactor * conversionRate;
            } else {
                //check both direction rules
                conversionRate = getConversionRate(paasComponentCategoryURI, appComponentCategoryURI);
                if (conversionRate != -1) {
                    found = true;
                    maxPaasComputationalPowerFactor = maxPaasComputationalPowerFactor * conversionRate;
                }
            }
        }
        boolean result = false;
        //if not found then there is not way to compare the computational power factors
        if (found && minAppComputationalPowerFactor <= maxPaasComputationalPowerFactor) {
            result = true;
        }
        return result;
    }

    private float getConversionRate(String sourceURI, String targetURI) {
        float conversionRate = -1;
        String query =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX c4s-inf-m: <http://www.cloud4soa.eu/v0.1/infrastructural-domain#>"
                + "PREFIX conv: <http://usoa.deri.ie/ontology/conversion_v.0.1#>"
                + "PREFIX common-m: <http://www.cloud4soa.eu/v0.1/other#>"
                + "SELECT ?conversionRate "
                + "WHERE { "
                + "?eqRule a common-m:EquivalenceRuleHardwareCategory."
                + "?eqRule common-m:hasSource " + repositoryManager.getModel().createURI(sourceURI).toSPARQL() + "."
                + "?eqRule common-m:hasTarget " + repositoryManager.getModel().createURI(targetURI).toSPARQL() + "."
                + "?eqRule conv:hasConversionRate ?conversionRate."
                + "}";

        logger.debug(query);
        QueryResultTable resultTable = repositoryManager.getModel().sparqlSelect(query);
        if (resultTable == null) {
            String error = "An error happens when querying the model";
            logger.error(error);
            throw new RuntimeException(error);
        }
        ClosableIterator<QueryRow> it = resultTable.iterator();
        QueryRow queryRow;
        while (it.hasNext()) {
            queryRow = it.next();
            DatatypeLiteral conversionRateLiteral = queryRow.getValue("conversionRate").asDatatypeLiteral();

            if (conversionRateLiteral.getDatatype().equals(XSD._float)) {
                conversionRate = TypeConverter.toFloat(conversionRateLiteral);
            }
        }
        return conversionRate;

    }
    
    
    public Map<String, Float> reverseEquivalence(String componentCategoryURI ) {
        Map<String, Float> invertedEquivalences;
        String query =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX c4s-inf-m: <http://www.cloud4soa.eu/v0.1/infrastructural-domain#>"
                + "PREFIX conv: <http://usoa.deri.ie/ontology/conversion_v.0.1#>"
                + "PREFIX common-m: <http://www.cloud4soa.eu/v0.1/other#>"
                + "SELECT ?sourceCategory ?conversionRate "
                + "WHERE { "
                + "?eqRule a common-m:EquivalenceRuleHardwareCategory."
                + "?eqRule common-m:hasTarget " + repositoryManager.getModel().createURI(componentCategoryURI).toSPARQL() + "."
                + "?eqRule common-m:hasSource ?sourceCategory."
                + "?eqRule conv:hasConversionRate ?conversionRate."
                + "}";
        QueryRow queryRow;
        float conversionRateFloat;
        float invertedConversionRate;
        QueryResultTable resultTable;
        
        invertedEquivalences = new HashMap<String, Float>();
        
        resultTable = repositoryManager.getModel().sparqlSelect(query);
        if (resultTable == null) {
            String error = "An error happens when querying the model";
            logger.error(error);
            throw new RuntimeException(error);
        }
        
        ClosableIterator<QueryRow> it = resultTable.iterator();
        
        if (!it.hasNext() ) {
            String info = "There is no EquivalenceRuleHardwareCategory for the following Harware Category: " + componentCategoryURI;
            logger.debug(info);
        }    
        while (it.hasNext()) {
            queryRow = it.next();
            String sourceCategoryUriId = queryRow.getValue("sourceCategory").asURI().toString();
            DatatypeLiteral conversionRateLiteral = queryRow.getValue("conversionRate").asDatatypeLiteral();
            //Convert the conversion rate value if possible
            
            try {
                conversionRateFloat = TypeConverter.toFloat(conversionRateLiteral);
                //inverted rate
                invertedConversionRate = 1 / conversionRateFloat;
                //adding the new equivalent rule to the hashmap
                invertedEquivalences.put(sourceCategoryUriId, invertedConversionRate);
            } catch(NumberFormatException nfe) {
                String error = "Found an EquivalenceRuleHardwareCategory which conversionRate is not a Float!";
                logger.error(error, nfe);
            }

        }
        
        return invertedEquivalences;
    }
    
    
    private String findingReverseRelations(String componentCategoryURI, Float minComputationalPowerFactor, Integer hwCounter) {
        String referenceCategoryURI;
        Map<String, Float> eqCategories;
        String queryingCriterias;
        
        queryingCriterias = "";
        referenceCategoryURI = componentCategoryURI;
        eqCategories = reverseEquivalence(referenceCategoryURI);
        alreadyExplored.add(componentCategoryURI);
        for (String convertedComponentCategoryURI : eqCategories.keySet()) {
            if (!alreadyExplored.contains(convertedComponentCategoryURI)) {
                Float convertedMinComputationalPowerFactor = minComputationalPowerFactor * (1 / eqCategories.get(convertedComponentCategoryURI));
                String localString = createSparqlCriteria(convertedComponentCategoryURI, convertedMinComputationalPowerFactor, hwCounter);
                queryingCriterias += localString;                 
            }              
        }
        
        return queryingCriterias;
    }
}
