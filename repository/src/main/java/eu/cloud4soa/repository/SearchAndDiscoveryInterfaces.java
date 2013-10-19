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

import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.qos.ServiceQualityInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.HardwareComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareComponentInstance;
import eu.cloud4soa.api.util.exception.SparqlQueryException;
import eu.cloud4soa.api.datamodel.repository.QueryResultRow;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.repository.utils.RepositoryManager;
import eu.cloud4soa.api.datamodel.repository.QueryResultTable;
import eu.cloud4soa.api.datamodel.semantic.ea.CloudResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.ContainerResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.Latency;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.ea.Uptime;
import eu.cloud4soa.api.datamodel.semantic.inf.HttpRequestsHandler;
import eu.cloud4soa.api.datamodel.semantic.inf.Compute;
import eu.cloud4soa.api.datamodel.semantic.inf.DBStorageComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.NetworkResource;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareCategory;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.StorageResource;

import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.TimeUnit;
import eu.cloud4soa.repository.utils.MeasurementUnitConverter;

import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;

import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.repository.utils.ComputationalConverter;
import eu.cloud4soa.repository.utils.PaaSRankingCalculator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.Literal;
import org.ontoware.rdf2go.model.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Zeginis Dimitris (CERTH)
 * @author Vincenzo Laudisio (DERI)
 */
public class SearchAndDiscoveryInterfaces implements eu.cloud4soa.api.repository.SearchAndDiscoveryInterfaces {

    final Logger logger = LoggerFactory.getLogger(SearchAndDiscoveryInterfaces.class);
    private PaaSOfferingProfilesRepository paaSOfferingProfilesRepository;
    private RepositoryManager repositoryManager = null;
    private ComputationalConverter computationalConverter;
    private PaaSRankingCalculator paaSRankingCalculator;

    @Required
    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    @Required
    public void setPaaSOfferingProfilesRepository(PaaSOfferingProfilesRepository paaSOfferingProfilesRepository) {
        this.paaSOfferingProfilesRepository = paaSOfferingProfilesRepository;
    }

    @Required
    public void setComputationalConverter(ComputationalConverter computationalConverter) {
        this.computationalConverter = computationalConverter;
    }

    @Required
    public void setPaaSRankingCalculator(PaaSRankingCalculator paaSRankingCalculator) {
        this.paaSRankingCalculator = paaSRankingCalculator;
    }

    @Override
    public Map<PaaSInstance, Float> searchForMatchingPlatform(ApplicationInstance applicationInstance) throws SOAException {
        
        String queryString = buildSPARQLquery(applicationInstance);
        Map<PaaSInstance, Float> matchingPaaSInstances = new HashMap<PaaSInstance, Float>();
        QueryResultTable result;
        try {
            result = sparqlSelect(queryString);
            List<QueryResultRow> resultRowList = result.getResultingRows();
            List<String> resultvariables = result.getVariables();
            Iterator<QueryResultRow> it = resultRowList.iterator();
            while (it.hasNext()) {
                QueryResultRow resultRow = it.next();
                String instanceURL = resultRow.getStringValue("po");
                String paasUriIdWithoutPrefix = instanceURL.toString();
                String paasInstanceUri = null;
                try {
                    paasInstanceUri = PaaSOffering.class.getMethod("getUriId", new Class[0]).getAnnotation(RDFSubject.class).prefix();
                } catch (NoSuchMethodException ex) {
                    logger.error(ex.getMessage());
                    throw new SOAException(Status.INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (SecurityException ex) {
                    logger.error(ex.getMessage());
                    throw new SOAException(Status.INTERNAL_SERVER_ERROR, ex.getMessage());
                }
                if (instanceURL.toString().contains(paasInstanceUri.toString())) {
                    paasUriIdWithoutPrefix = instanceURL.toString().replace(paasInstanceUri.toString(), "");
                }
                PaaSInstance pInst;
                try {
                    pInst = paaSOfferingProfilesRepository.getPaaSInstance(paasUriIdWithoutPrefix);
                } catch (RepositoryException ex) {
                    throw new SOAException(Status.INTERNAL_SERVER_ERROR, "Repository Exception");
                }
                float rank = paaSRankingCalculator.calculateRanking(pInst, applicationInstance);
                logger.debug("Checking " + pInst.toString() + ", " + applicationInstance.toString() + " ranking: " + rank);
                matchingPaaSInstances.put(pInst, rank);

            }
        } catch (SparqlQueryException ex) {
            logger.error(ex.getMessage());
            throw new SOAException(Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return matchingPaaSInstances;
    }

    //builds a SPARQL query based on the Application Instance
    private String buildSPARQLquery(ApplicationInstance applicationInstance) {

        String queryString = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX paas-m:<http://www.cloud4soa.eu/v0.1/paas-model#> "
                + "PREFIX dcterms:<http://purl.org/dc/terms/>"
                + "PREFIX inf-m:<http://www.cloud4soa.eu/v0.1/infrastructural-domain#>"
                + "PREFIX ea:<http://www.enterprise-architecture.org/essential-metamodel.owl#>"
                + "PREFIX measure:<http://www.cloud4soa.eu/v0.1/measure#>"
                + "PREFIX common-m:<http://www.cloud4soa.eu/v0.1/other#>"
                + "PREFIX qos-m:<http://www.cloud4soa.eu/v0.1/qos-model#>"
                + " Select DISTINCT ?po where {?po rdf:type paas-m:PaaSOffering .";

        //programming language 
        if (applicationInstance.getProgramminglanguage() != null) {
            String languageURI = applicationInstance.getApplication().getUseProgrammingLanguage().getUriId();//Programminglanguage().;
            queryString += "{{?po paas-m:supportLanguage common-m:" + languageURI + " .}"
                    + "UNION{?po paas-m:supportLanguage ?progr_lang ."
                    + "?progr_lang common-m:hasBackwardCompatibilityWith common-m:" + languageURI + ".}}";
        }

        //Compute scaling factor 
        if (applicationInstance.getApplication().getRequireComputeScalingFactor() != null) {
            //If the property IsRequired is not filled --> default value:true
            if (applicationInstance.getApplication().getRequireComputeScalingFactor().getIsRequired() != null
                    && applicationInstance.getApplication().getRequireComputeScalingFactor().getIsRequired()) {
                if (applicationInstance.getApplication().getRequireComputeScalingFactor().getMin() != null) {
                    Float minComputeScalingFactor = applicationInstance.getApplication().getRequireComputeScalingFactor().getMin();
                    queryString += "?po paas-m:offerComputeScalingFactor ?csf_range ."
                            + "?csf_range measure:hasMaxNumericValue ?csf_max_val ."
                            + "FILTER (?csf_max_val >=" + minComputeScalingFactor + ")";
                }
            }
        }

        //Web scaling factor
        if (applicationInstance.getApplication().getRequireWebScalingFactor() != null) {
            //If the property IsRequired is not filled --> default value:true
            if (applicationInstance.getApplication().getRequireWebScalingFactor().getIsRequired() != null
                    && applicationInstance.getApplication().getRequireWebScalingFactor().getIsRequired()) {
                if (applicationInstance.getApplication().getRequireWebScalingFactor().getMin() != null) {
                    Float minWebScalingFactor = applicationInstance.getApplication().getRequireWebScalingFactor().getMin();
                    queryString += "?po paas-m:offerWebScalingFactor ?wsf_range ."
                            + "?wsf_range measure:hasMaxNumericValue ?wsf_max_val ."
                            + "FILTER (?wsf_max_val >=" + minWebScalingFactor + ")";
                }
            }
        }

        //software components 
        List<SoftwareComponentInstance> applicationSWcomponents = applicationInstance.getSoftwareComponents();
        for (int i = 0; i < applicationSWcomponents.size(); i++) {
            SoftwareComponentInstance swAppInstance = applicationSWcomponents.get(i);
            //Check if the S/W component is required
            //If the property IsRequired is not filled --> default value:true
            if (swAppInstance.getSoftwareComponent().getIsRequired() != null && swAppInstance.getSoftwareComponent().getIsRequired()) {

                queryString += "?po paas-m:offerSoftware ?sw" + Integer.toString(i) + " .";

                //check if the SW category is null
                if (swAppInstance.getSoftwareCategoryInstance() != null && swAppInstance.getSoftwareCategoryInstance().getSoftwareCategory() != null) {
                    SoftwareCategory swAppCategory = swAppInstance.getSoftwareCategoryInstance().getSoftwareCategory();
                    String categoryURI = swAppCategory.getUriId();
                    queryString += "?sw" + Integer.toString(i) + " inf-m:related_sw_category inf-m:" + categoryURI + " .";
                }

                //DBStorage Components 
                SoftwareComponent swAppComponent = swAppInstance.getSoftwareComponent();
                if (swAppComponent instanceof DBStorageComponent) {
                    DBStorageComponent appDBStorageComponent = (DBStorageComponent) swAppComponent;

                    boolean dbconfiguration = false;


                    //DB CACHE  
                    if (appDBStorageComponent.getDBconfiguration() != null && appDBStorageComponent.getDBconfiguration().getHasDBcache() != null) {
                        //If the property IsRequired is not filled --> default value:true
                        if (appDBStorageComponent.getDBconfiguration().getHasDBcache().getIsRequired() != null
                                && appDBStorageComponent.getDBconfiguration().getHasDBcache().getIsRequired()) {
                            if (appDBStorageComponent.getDBconfiguration().getHasDBcache().getMin() != null
                                    && appDBStorageComponent.getDBconfiguration().getHasDBcache().getMin().getValue() != null) {
                                StorageUnit minDBCache = appDBStorageComponent.getDBconfiguration().getHasDBcache().getMin();
                                dbconfiguration = true;
                                queryString += "?sw" + Integer.toString(i) + " inf-m:hasDBconfiguration ?dbconf" + Integer.toString(i) + " .";
                                queryString += "?dbconf" + Integer.toString(i) + " inf-m:hasDBcache ?dbcache_range" + Integer.toString(i) + " ."
                                        + "?dbcache_range" + Integer.toString(i) + " measure:hasMaxStorageValue ?dbcache_unit" + Integer.toString(i) + " ."
                                        + "{{?dbcache_unit" + Integer.toString(i) + " rdf:type measure:TeraByte ."
                                        + "?dbcache_unit" + Integer.toString(i) + " measure:has_value ?dbcache_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Terabyte(minDBCache) + ")}"
                                        + "UNION {?dbcache_unit" + Integer.toString(i) + " rdf:type measure:GigaByte ."
                                        + "?dbcache_unit" + Integer.toString(i) + " measure:has_value ?dbcache_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Gigabyte(minDBCache) + ")}"
                                        + "UNION {?dbcache_unit" + Integer.toString(i) + " rdf:type measure:MegaByte ."
                                        + "?dbcache_unit" + Integer.toString(i) + " measure:has_value ?dbcache_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Megabyte(minDBCache) + ")}"
                                        + "UNION {?dbcache_unit" + Integer.toString(i) + " rdf:type measure:KiloByte ."
                                        + "?dbcache_unit" + Integer.toString(i) + " measure:has_value ?dbcache_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Kilobyte(minDBCache) + ")}"
                                        //Default Megabyte
                                        + "UNION {?dbcache_unit" + Integer.toString(i) + " rdf:type measure:Storage_Unit ."
                                        + "?dbcache_unit" + Integer.toString(i) + " measure:has_value ?dbcache_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Megabyte(minDBCache) + ")}}.";
                            }
                        }
                    }

                    //DB CAPACITY 
                    if (appDBStorageComponent.getDBconfiguration() != null && appDBStorageComponent.getDBconfiguration().getHasDBcapacity() != null) {
                        //If the property IsRequired is not filled --> default value:true
                        if (appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getIsRequired() != null
                                && appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getIsRequired()) {
                            if (appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMin() != null
                                    && appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMin().getValue() != null) {
                                StorageUnit minDBCapacity = appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMin();
                                if (!dbconfiguration) {
                                    queryString += "?sw" + Integer.toString(i) + " inf-m:hasDBconfiguration ?dbconf" + Integer.toString(i) + " .";
                                }
                                queryString += "?dbconf" + Integer.toString(i) + " inf-m:hasDBcapacity ?dbcapacity_range" + Integer.toString(i) + " ."
                                        + "?dbcapacity_range" + Integer.toString(i) + " measure:hasMaxStorageValue ?dbcapacity_unit" + Integer.toString(i) + " ."
                                        + "{{?dbcapacity_unit" + Integer.toString(i) + " rdf:type measure:TeraByte ."
                                        + "?dbcapacity_unit" + Integer.toString(i) + " measure:has_value ?dbcapacity_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcapacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Terabyte(minDBCapacity) + ")}"
                                        + "UNION {?dbcapacity_unit" + Integer.toString(i) + " rdf:type measure:GigaByte ."
                                        + "?dbcapacity_unit" + Integer.toString(i) + " measure:has_value ?dbcapacity_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcapacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Gigabyte(minDBCapacity) + ")}"
                                        + "UNION {?dbcapacity_unit" + Integer.toString(i) + " rdf:type measure:MegaByte ."
                                        + "?dbcapacity_unit" + Integer.toString(i) + " measure:has_value ?dbcapacity_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcapacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Megabyte(minDBCapacity) + ")}"
                                        + "UNION {?dbcapacity_unit" + Integer.toString(i) + " rdf:type measure:KiloByte ."
                                        + "?dbcapacity_unit" + Integer.toString(i) + " measure:has_value ?dbcapacity_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcapacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Kilobyte(minDBCapacity) + ")}"
                                        //Default Megabyte
                                        + "UNION {?dbcapacity_unit" + Integer.toString(i) + " rdf:type measure:Storage_Unit ."
                                        + "?dbcapacity_unit" + Integer.toString(i) + " measure:has_value ?dbcapacity_val" + Integer.toString(i) + " ."
                                        + "FILTER (?dbcapacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Megabyte(minDBCapacity) + ")}}.";
                            }
                        }
                    }
                }
            }
        }


        //HARDWARE COMPONENTS
        List<HardwareComponentInstance> applicationHWcomponents = applicationInstance.getHardwareComponents();
        for (int i = 0; i < applicationHWcomponents.size(); i++) {
            HardwareComponentInstance hwAppInstance = applicationHWcomponents.get(i);
            HardwareComponent hdAppComponent = hwAppInstance.getHardwareComponent();

            String componentCategoryURI = null;
            //check if the HW category is null
            if (hdAppComponent.getRelatedhwcategory() != null && hdAppComponent.getRelatedhwcategory().getUriId() != null) {
                componentCategoryURI = hdAppComponent.getRelatedhwcategory().getUriId();
            }

            boolean hdcomponent = false;
            // queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
            //         + "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";

            //NETWORK COMPONENT
            if (hdAppComponent instanceof NetworkResource) {
                NetworkResource appNetworkResource = (NetworkResource) hdAppComponent;
                //    queryString += "?hw" + Integer.toString(i) + " rdf:type inf-m:NetworkResource .";

                //BANDWIDTH
                if (appNetworkResource.getBandwidth() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appNetworkResource.getBandwidth().getIsRequired() != null && appNetworkResource.getBandwidth().getIsRequired()) {
                        if (appNetworkResource.getBandwidth().getMin() != null && appNetworkResource.getBandwidth().getMin().getValue() != null) {
                            NetworkingUnit minBandwidth = appNetworkResource.getBandwidth().getMin();
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:NetworkResource .";
                                if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";
                                }
                                hdcomponent = true;
                            }
                            queryString += "?hw" + Integer.toString(i) + " inf-m:bandwidth ?bandwidth_range" + Integer.toString(i) + " ."
                                    + "?bandwidth_range" + Integer.toString(i) + " measure:hasMaxNetworkingValue ?bandwidth_unit" + Integer.toString(i) + " ."
                                    + "{{?bandwidth_unit" + Integer.toString(i) + " rdf:type measure:TeraByte_Per_Second ."
                                    + "?bandwidth_unit" + Integer.toString(i) + " measure:has_value ?bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2TerabytePerSecond(minBandwidth) + ")}"
                                    + "UNION {?bandwidth_unit" + Integer.toString(i) + " rdf:type measure:GigaByte_Per_Second ."
                                    + "?bandwidth_unit" + Integer.toString(i) + " measure:has_value ?bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2GigabytePerSecond(minBandwidth) + ")}"
                                    + "UNION {?bandwidth_unit" + Integer.toString(i) + " rdf:type measure:MegaByte_Per_Second ."
                                    + "?bandwidth_unit" + Integer.toString(i) + " measure:has_value ?bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2MegabytePerSecond(minBandwidth) + ")}"
                                    + "UNION {?bandwidth_unit" + Integer.toString(i) + " rdf:type measure:KiloByte_Per_Second ."
                                    + "?bandwidth_unit" + Integer.toString(i) + " measure:has_value ?bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2KilobytePerSecond(minBandwidth) + ")}"
                                    //Default Gigabyte per second
                                    + "UNION {?bandwidth_unit" + Integer.toString(i) + " rdf:type measure:Networking_Unit ."
                                    + "?bandwidth_unit" + Integer.toString(i) + " measure:has_value ?bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2GigabytePerSecond(minBandwidth) + ")}}.";
                        }
                    }
                }
                //LATENCY
                if (appNetworkResource.getLatency() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appNetworkResource.getLatency().getIsRequired() != null && appNetworkResource.getLatency().getIsRequired()) {
                        if (appNetworkResource.getLatency().getMax() != null && appNetworkResource.getLatency().getMax().getValue() != null) {
                            TimeUnit maxLatency = appNetworkResource.getLatency().getMax();
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:NetworkResource .";
                                if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";;
                                }
                                hdcomponent = true;
                            }
                            queryString += "?hw" + Integer.toString(i) + " inf-m:latency ?latency_range" + Integer.toString(i) + " ."
                                    + "?latency_range" + Integer.toString(i) + " measure:hasMaxTimeValue ?latency_unit" + Integer.toString(i) + " ."
                                    + "{{?latency_unit" + Integer.toString(i) + " rdf:type measure:Hour ."
                                    + "?latency_unit" + Integer.toString(i) + " measure:has_value ?latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Hours(maxLatency) + ")}"
                                    + "UNION {?latency_unit" + Integer.toString(i) + " rdf:type measure:Minute ."
                                    + "?latency_unit" + Integer.toString(i) + " measure:has_value ?latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Minutes(maxLatency) + ")}"
                                    + "UNION {?latency_unit" + Integer.toString(i) + " rdf:type measure:Second ."
                                    + "?latency_unit" + Integer.toString(i) + " measure:has_value ?latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Second(maxLatency) + ")}"
                                    + "UNION {?latency_unit" + Integer.toString(i) + " rdf:type measure:MilliSecond ."
                                    + "?latency_unit" + Integer.toString(i) + " measure:has_value ?latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2MilliSecond(maxLatency) + ")}"
                                    //Default MilliSecond
                                    + "UNION {?latency_unit" + Integer.toString(i) + " rdf:type measure:Time_Unit ."
                                    + "?latency_unit" + Integer.toString(i) + " measure:has_value ?latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2MilliSecond(maxLatency) + ")}}.";
                        }
                    }
                }

                //BOX COMPONENT
            } else if (hdAppComponent instanceof HttpRequestsHandler) {
                HttpRequestsHandler appBox = (HttpRequestsHandler) hdAppComponent;
                //HTTP REQUESTS
                if (appBox.getHTTPRequests() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appBox.getHTTPRequests().getIsRequired() != null && appBox.getHTTPRequests().getIsRequired()) {
                        if (appBox.getHTTPRequests().getMin() != null) {
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:HttpRequestsHandler .";
                                 if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";;
                                }
                                hdcomponent = true;
                            }
                            Float minHTTPRequests = appBox.getHTTPRequests().getMin();
                            queryString += "?hw" + Integer.toString(i) + " inf-m:hasHTTPRequests ?request_range" + Integer.toString(i) + " ."
                                    + "?request_range" + Integer.toString(i) + " measure:hasMaxNumericValue ?request_val" + Integer.toString(i) + " ."
                                    + "FILTER (?request_val" + Integer.toString(i) + ">=" + minHTTPRequests + ")";
                        }
                    }
                }

                //COMPUTATIONAL POWER FACTOR
                if (appBox.getComputationalPowerFactor() != null) {
                    //If the property IsRequired is not filled --> default value:true  
                    if (appBox.getComputationalPowerFactor().getIsRequired() != null && appBox.getComputationalPowerFactor().getIsRequired()) {
                        if (appBox.getComputationalPowerFactor().getMin() != null) {
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:HttpRequestsHandler .";
                                 if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";;
                                }
                                hdcomponent = true;
                            }
                            Float minComputationalPowerFactor = appBox.getComputationalPowerFactor().getMin();

                            if (componentCategoryURI != null) {
                                queryString += "{";
                                queryString += "{"
                                        + "?hw" + Integer.toString(i) + " inf-m:computationalPowerFactor ?box_cpf_range" + Integer.toString(i) + " ."
                                        + "?box_cpf_range" + Integer.toString(i) + " measure:hasMaxNumericValue ?box_cpf_val" + Integer.toString(i) + " ."
                                        + "FILTER (?box_cpf_val" + Integer.toString(i) + ">=" + minComputationalPowerFactor + ")"
                                        + "}";
                                String convertedComputationalComponent = computationalConverter.convertComputationalComponent(minComputationalPowerFactor, componentCategoryURI, i);
                                queryString += convertedComputationalComponent;
                                queryString += "}";
                            }
                        }
                    }
                }

                //COMPUTE COMPONENT
            } else if (hdAppComponent instanceof Compute) {
                Compute appCompute = (Compute) hdAppComponent;
                //  queryString += "?hw" + Integer.toString(i) + " rdf:type inf-m:Compute .";

                //ARCHITECTURE
                if (appCompute.getArchitecture() != null) {
                    String architecture = appCompute.getArchitecture();
                    if (!hdcomponent) {
                        queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                + "?hw" + Integer.toString(i) + " rdf:type inf-m:Compute .";
                         if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";;
                                }
                        hdcomponent = true;
                    }
                    queryString += "?hw" + Integer.toString(i) + " inf-m:hasArchitecture \"" + architecture + "\" .";
                }

                //CACHE
                if (appCompute.getCache() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appCompute.getCache().getIsRequired() != null && appCompute.getCache().getIsRequired()) {
                        if (appCompute.getCache().getMin() != null && appCompute.getCache().getMin().getValue() != null) {
                            StorageUnit minCache = appCompute.getCache().getMin();
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:Compute .";
                                 if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";;
                                }
                                hdcomponent = true;
                            }
                            queryString += "?hw" + Integer.toString(i) + " inf-m:hasCache ?cache_range" + Integer.toString(i) + " ."
                                    + "?cache_range" + Integer.toString(i) + " measure:hasMaxStorageValue ?cache_unit" + Integer.toString(i) + " ."
                                    + "{{?cache_unit" + Integer.toString(i) + " rdf:type measure:TeraByte ."
                                    + "?cache_unit" + Integer.toString(i) + " measure:has_value ?cache_val" + Integer.toString(i) + " ."
                                    + "FILTER (?cache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Terabyte(minCache) + ")}"
                                    + "UNION {?cache_unit" + Integer.toString(i) + " rdf:type measure:GigaByte ."
                                    + "?cache_unit" + Integer.toString(i) + " measure:has_value ?cache_val" + Integer.toString(i) + " ."
                                    + "FILTER (?cache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Gigabyte(minCache) + ")}"
                                    + "UNION {?cache_unit" + Integer.toString(i) + " rdf:type measure:MegaByte ."
                                    + "?cache_unit" + Integer.toString(i) + " measure:has_value ?cache_val" + Integer.toString(i) + " ."
                                    + "FILTER (?cache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Megabyte(minCache) + ")}"
                                    + "UNION {?cache_unit" + Integer.toString(i) + " rdf:type measure:KiloByte ."
                                    + "?cache_unit" + Integer.toString(i) + " measure:has_value ?cache_val" + Integer.toString(i) + " ."
                                    + "FILTER (?cache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Kilobyte(minCache) + ")}"
                                    //Default Megabyte
                                    + "UNION {?cache_unit" + Integer.toString(i) + " rdf:type measure:Storage_Unit ."
                                    + "?cache_unit" + Integer.toString(i) + " measure:has_value ?cache_val" + Integer.toString(i) + " ."
                                    + "FILTER (?cache_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Megabyte(minCache) + ")}}.";
                        }
                    }
                }

                //NUMBER OF CORES
                if (appCompute.getHasCores() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appCompute.getHasCores().getIsRequired() != null && appCompute.getHasCores().getIsRequired()) {
                        if (appCompute.getHasCores().getMin() != null) {
                            Float minCores = appCompute.getHasCores().getMin();
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:Compute .";
                                 if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";;
                                }
                                hdcomponent = true;
                            }
                            queryString += "?hw" + Integer.toString(i) + " inf-m:hasCores ?cores_range" + Integer.toString(i) + " ."
                                    + "?cores_range" + Integer.toString(i) + " measure:hasMaxNumericValue ?cores_val" + Integer.toString(i) + " ."
                                    + "FILTER (?cores_val" + Integer.toString(i) + ">=" + minCores + ")";
                        }
                    }
                }

                //MEMORY
                if (appCompute.getMemory() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appCompute.getMemory().getIsRequired() != null && appCompute.getMemory().getIsRequired()) {
                        if (appCompute.getMemory().getMin() != null && appCompute.getMemory().getMin().getValue() != null) {
                            StorageUnit minMemory = appCompute.getMemory().getMin();
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:Compute .";
                                 if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";;
                                }
                                hdcomponent = true;
                            }
                            queryString += "?hw" + Integer.toString(i) + " inf-m:hasMemory ?memory_range" + Integer.toString(i) + " ."
                                    + "?memory_range" + Integer.toString(i) + " measure:hasMaxStorageValue ?memory_unit" + Integer.toString(i) + " ."
                                    + "{{?memory_unit" + Integer.toString(i) + " rdf:type measure:TeraByte ."
                                    + "?memory_unit" + Integer.toString(i) + " measure:has_value ?memory_val" + Integer.toString(i) + " ."
                                    + "FILTER (?memory_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Terabyte(minMemory) + ")}"
                                    + "UNION {?memory_unit" + Integer.toString(i) + " rdf:type measure:GigaByte ."
                                    + "?memory_unit" + Integer.toString(i) + " measure:has_value ?memory_val" + Integer.toString(i) + " ."
                                    + "FILTER (?memory_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Gigabyte(minMemory) + ")}"
                                    + "UNION {?memory_unit" + Integer.toString(i) + " rdf:type measure:MegaByte ."
                                    + "?memory_unit" + Integer.toString(i) + " measure:has_value ?memory_val" + Integer.toString(i) + " ."
                                    + "FILTER (?memory_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Megabyte(minMemory) + ")}"
                                    + "UNION {?memory_unit" + Integer.toString(i) + " rdf:type measure:KiloByte ."
                                    + "?memory_unit" + Integer.toString(i) + " measure:has_value ?memory_val" + Integer.toString(i) + " ."
                                    + "FILTER (?memory_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Kilobyte(minMemory) + ")}"
                                    //Default Gigabyte
                                    + "UNION {?memory_unit" + Integer.toString(i) + " rdf:type measure:Storage_Unit ."
                                    + "?memory_unit" + Integer.toString(i) + " measure:has_value ?memory_val" + Integer.toString(i) + " ."
                                    + "FILTER (?memory_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Gigabyte(minMemory) + ")}}.";
                        }
                    }
                }

                //COMPUTATIONAL POWER FACTOR
                if (appCompute.getComputationalPowerFactor() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appCompute.getComputationalPowerFactor().getIsRequired() != null && appCompute.getComputationalPowerFactor().getIsRequired()) {
                        if (appCompute.getComputationalPowerFactor().getMin() != null) {
                            Float minComputationalPowerFactor = appCompute.getComputationalPowerFactor().getMin();

                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:Compute .";
                                 if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";;
                                }
                                hdcomponent = true;
                            }
                            if (componentCategoryURI != null) {
                                queryString += "{";
                                queryString += "{"
                                        + "?hw" + Integer.toString(i) + " inf-m:computationalPowerFactor ?box_cpf_range" + Integer.toString(i) + " ."
                                        + "?box_cpf_range" + Integer.toString(i) + " measure:hasMaxNumericValue ?box_cpf_val" + Integer.toString(i) + " ."
                                        + "FILTER (?box_cpf_val" + Integer.toString(i) + ">=" + minComputationalPowerFactor + ")"
                                        + "}";
                                String convertedComputationalComponent = computationalConverter.convertComputationalComponent(minComputationalPowerFactor, componentCategoryURI, i);
                                queryString += convertedComputationalComponent;
                                queryString += "}";
                            }
                        }
                    }
                }

                //STORAGE RESOURCE
            } else if (hdAppComponent instanceof StorageResource) {
                StorageResource appStorageResource = (StorageResource) hdAppComponent;
                //queryString += "?hw" + Integer.toString(i) + " rdf:type inf-m:StorageResource .";


                //STORAGE CAPACITY
                if (appStorageResource.getCapacity() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appStorageResource.getCapacity().getIsRequired() != null && appStorageResource.getCapacity().getIsRequired()) {
                        if (appStorageResource.getCapacity().getMin() != null && appStorageResource.getCapacity().getMin().getValue() != null) {
                            StorageUnit minCapacity = appStorageResource.getCapacity().getMin();
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:StorageResource .";
                                if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";
                                }
                                hdcomponent = true;
                            }
                            queryString += "?hw" + Integer.toString(i) + " inf-m:capacity ?capacity_range" + Integer.toString(i) + " ."
                                    + "?capacity_range" + Integer.toString(i) + " measure:hasMaxStorageValue ?capacity_unit" + Integer.toString(i) + " ."
                                    + "{{?capacity_unit" + Integer.toString(i) + " rdf:type measure:TeraByte ."
                                    + "?capacity_unit" + Integer.toString(i) + " measure:has_value ?capacity_val" + Integer.toString(i) + " ."
                                    + "FILTER (?capacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Terabyte(minCapacity) + ")}"
                                    + "UNION {?capacity_unit" + Integer.toString(i) + " rdf:type measure:GigaByte ."
                                    + "?capacity_unit" + Integer.toString(i) + " measure:has_value ?capacity_val" + Integer.toString(i) + " ."
                                    + "FILTER (?capacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Gigabyte(minCapacity) + ")}"
                                    + "UNION {?capacity_unit" + Integer.toString(i) + " rdf:type measure:MegaByte ."
                                    + "?capacity_unit" + Integer.toString(i) + " measure:has_value ?capacity_val" + Integer.toString(i) + " ."
                                    + "FILTER (?capacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Megabyte(minCapacity) + ")}"
                                    + "UNION {?capacity_unit" + Integer.toString(i) + " rdf:type measure:KiloByte ."
                                    + "?capacity_unit" + Integer.toString(i) + " measure:has_value ?capacity_val" + Integer.toString(i) + " ."
                                    + "FILTER (?capacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Kilobyte(minCapacity) + ")}"
                                    //Default Gigabyte
                                    + "UNION {?capacity_unit" + Integer.toString(i) + " rdf:type measure:Storage_Unit ."
                                    + "?capacity_unit" + Integer.toString(i) + " measure:has_value ?capacity_val" + Integer.toString(i) + " ."
                                    + "FILTER (?capacity_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertStorage2Gigabyte(minCapacity) + ")}}.";
                        }
                    }
                }

                //BANDWIDTH
                if (appStorageResource.getBandwidth() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appStorageResource.getBandwidth().getIsRequired() != null && appStorageResource.getBandwidth().getIsRequired()) {
                        if (appStorageResource.getBandwidth().getMin() != null
                                && appStorageResource.getBandwidth().getMin().getValue() != null) {
                            NetworkingUnit minBandwidth = appStorageResource.getBandwidth().getMin();
                            if (!hdcomponent) {
                                queryString += "?po paas-m:offerHardwareComponent ?hw" + Integer.toString(i) + " ."
                                        + "?hw" + Integer.toString(i) + " rdf:type inf-m:StorageResource .";
                                if (componentCategoryURI != null) {
                                    queryString += "?hw" + Integer.toString(i) + " ea:realisation_of_technology_capability inf-m:" + componentCategoryURI + " .";
                                }
                                hdcomponent = true;
                            }
                            queryString += "?hw" + Integer.toString(i) + " inf-m:bandwidth ?storage_bandwidth_range" + Integer.toString(i) + " ."
                                    + "?storage_bandwidth_range" + Integer.toString(i) + " measure:hasMaxNetworkingValue ?storage_bandwidth_unit" + Integer.toString(i) + " ."
                                    + "{{?storage_bandwidth_unit" + Integer.toString(i) + " rdf:type measure:TeraByte_Per_Second ."
                                    + "?storage_bandwidth_unit" + Integer.toString(i) + " measure:has_value ?storage_bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?storage_bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2TerabytePerSecond(minBandwidth) + ")}"
                                    + "UNION {?storage_bandwidth_unit" + Integer.toString(i) + " rdf:type measure:GigaByte_Per_Second ."
                                    + "?storage_bandwidth_unit" + Integer.toString(i) + " measure:has_value ?storage_bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?storage_bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2GigabytePerSecond(minBandwidth) + ")}"
                                    + "UNION {?storage_bandwidth_unit" + Integer.toString(i) + " rdf:type measure:MegaByte_Per_Second ."
                                    + "?storage_bandwidth_unit" + Integer.toString(i) + " measure:has_value ?storage_bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?storage_bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2MegabytePerSecond(minBandwidth) + ")}"
                                    + "UNION {?storage_bandwidth_unit" + Integer.toString(i) + " rdf:type measure:KiloByte_Per_Second ."
                                    + "?storage_bandwidth_unit" + Integer.toString(i) + " measure:has_value ?storage_bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?storage_bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2KilobytePerSecond(minBandwidth) + ")}"
                                    //Default Gigabyte per second
                                    + "UNION {?storage_bandwidth_unit" + Integer.toString(i) + " rdf:type measure:Networking_Unit ."
                                    + "?storage_bandwidth_unit" + Integer.toString(i) + " measure:has_value ?storage_bandwidth_val" + Integer.toString(i) + " ."
                                    + "FILTER (?storage_bandwidth_val" + Integer.toString(i) + ">=" + MeasurementUnitConverter.convertBandwidth2GigabytePerSecond(minBandwidth) + ")}}.";
                        }
                    }
                }
            }
        }
        //QoS 
        List<ServiceQualityInstance> applicationSWqualities = applicationInstance.getServiceQualities();
        for (int i = 0; i < applicationSWqualities.size(); i++) {
            ServiceQualityInstance swAppQuality = applicationSWqualities.get(i);
            Technology_Service_Quality quality = swAppQuality.getServiceQuality();

            boolean qos = false;
            //     queryString += "?po paas-m:providesServiceQuality ?qos" + Integer.toString(i) + " .";

            //LATENCY QOS
            if (quality instanceof Latency) {
                Latency appLatencyQuality = (Latency) quality;
                //       queryString += "?qos" + Integer.toString(i) + " rdf:type qos-m:Latency .";

                //LATENCY TIME RANGE
                if (appLatencyQuality.getHasTimeRangeValue() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appLatencyQuality.getHasTimeRangeValue().getIsRequired() != null && appLatencyQuality.getHasTimeRangeValue().getIsRequired()) {
                        if (appLatencyQuality.getHasTimeRangeValue().getMax() != null
                                && appLatencyQuality.getHasTimeRangeValue().getMax().getValue() != null) {
                            TimeUnit maxAppLatency = appLatencyQuality.getHasTimeRangeValue().getMax();
                            if (!qos) {
                                queryString += "?po paas-m:providesServiceQuality ?qos" + Integer.toString(i) + " .";
                                queryString += "?qos" + Integer.toString(i) + " rdf:type qos-m:Latency .";
                                qos = true;
                            }
                            queryString += "?qos" + Integer.toString(i) + " qos-m:hasTimeRangeValue ?qos_latency_range" + Integer.toString(i) + " ."
                                    + "?qos_latency_range" + Integer.toString(i) + " measure:hasMaxTimeValue ?qos_latency_unit" + Integer.toString(i) + " ."
                                    + "{{?qos_latency_unit" + Integer.toString(i) + " rdf:type measure:Hour ."
                                    + "?qos_latency_unit" + Integer.toString(i) + " measure:has_value ?qos_latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Hours(maxAppLatency) + ")}"
                                    + "UNION {?qos_latency_unit" + Integer.toString(i) + " rdf:type measure:Minute ."
                                    + "?qos_latency_unit" + Integer.toString(i) + " measure:has_value ?qos_latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Minutes(maxAppLatency) + ")}"
                                    + "UNION {?qos_latency_unit" + Integer.toString(i) + " rdf:type measure:Second ."
                                    + "?qos_latency_unit" + Integer.toString(i) + " measure:has_value ?qos_latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Second(maxAppLatency) + ")}"
                                    + "UNION {?qos_latency_unit" + Integer.toString(i) + " rdf:type measure:MilliSecond ."
                                    + "?qos_latency_unit" + Integer.toString(i) + " measure:has_value ?qos_latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2MilliSecond(maxAppLatency) + ")}"
                                    //Default MilliSecond
                                    + "UNION {?lqos_atency_unit" + Integer.toString(i) + " rdf:type measure:Time_Unit ."
                                    + "?qos_latency_unit" + Integer.toString(i) + " measure:has_value ?qos_latency_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_latency_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2MilliSecond(maxAppLatency) + ")}}.";
                        }
                    }
                }
            }

            //UPTIME QOS
            if (quality instanceof Uptime) {
                Uptime appUptimeQuality = (Uptime) quality;
                //queryString += "?qos" + Integer.toString(i) + " rdf:type qos-m:Uptime .";

                //UPTIME TIME RANGE
                if (appUptimeQuality.getHasPercentage() != null) {
                    if (!qos) {
                        queryString += "?po paas-m:providesServiceQuality ?qos" + Integer.toString(i) + " .";
                        queryString += "?qos" + Integer.toString(i) + " rdf:type qos-m:Uptime .";
                        qos = true;
                    }
                    Float minAppUptime = appUptimeQuality.getHasPercentage();
                    queryString += "?qos" + Integer.toString(i) + " qos-m:hasPercentage ?qos_Uptime" + Integer.toString(i) + " ."
                            + "FILTER (?qos_Uptime" + Integer.toString(i) + ">=" + minAppUptime + ")";

                }
            }
                        
            //CloudResponseTime QOS
            if (quality instanceof CloudResponseTime) {
                CloudResponseTime appCloudResponseTime = (CloudResponseTime) quality;
                
                //CloudResponse Time RANGE
                if (appCloudResponseTime.getHasTimeRangeValue() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appCloudResponseTime.getHasTimeRangeValue().getIsRequired() != null && appCloudResponseTime.getHasTimeRangeValue().getIsRequired()) {
                        if (appCloudResponseTime.getHasTimeRangeValue().getMax() != null
                                && appCloudResponseTime.getHasTimeRangeValue().getMax().getValue() != null) {
                            TimeUnit maxAppCloudResponse = appCloudResponseTime.getHasTimeRangeValue().getMax();
                            if (!qos) {
                                queryString += "?po paas-m:providesServiceQuality ?qos" + Integer.toString(i) + " .";
                                queryString += "?qos" + Integer.toString(i) + " rdf:type qos-m:CloudResponseTime .";
                                qos = true;
                            }
                            queryString += "?qos" + Integer.toString(i) + " qos-m:hasTimeRangeValue ?qos_cloudResponse_range" + Integer.toString(i) + " ."
                                    + "?qos_cloudResponse_range" + Integer.toString(i) + " measure:hasMaxTimeValue ?qos_cloudResponse_unit" + Integer.toString(i) + " ."
                                    + "{{?qos_cloudResponse_unit" + Integer.toString(i) + " rdf:type measure:Hour ."
                                    + "?qos_cloudResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_cloudResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_cloudResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Hours(maxAppCloudResponse) + ")}"
                                    + "UNION {?qos_cloudResponse_unit" + Integer.toString(i) + " rdf:type measure:Minute ."
                                    + "?qos_cloudResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_cloudResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_cloudResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Minutes(maxAppCloudResponse) + ")}"
                                    + "UNION {?qos_cloudResponse_unit" + Integer.toString(i) + " rdf:type measure:Second ."
                                    + "?qos_cloudResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_cloudResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_cloudResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Second(maxAppCloudResponse) + ")}"
                                    + "UNION {?qos_cloudResponse_unit" + Integer.toString(i) + " rdf:type measure:MilliSecond ."
                                    + "?qos_cloudResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_cloudResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_cloudResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2MilliSecond(maxAppCloudResponse) + ")}"
                                    //Default MilliSecond
                                    + "UNION {?qos_cloudResponse_unit" + Integer.toString(i) + " rdf:type measure:Time_Unit ."
                                    + "?qos_cloudResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_cloudResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_cloudResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2MilliSecond(maxAppCloudResponse) + ")}}.";
                        }
                    }
                }
            }
            
                              
            //ContainerResponseTime QOS
            if (quality instanceof ContainerResponseTime) {
                ContainerResponseTime appContainerResponseTime = (ContainerResponseTime) quality;
                
                //ContainerResponse Time RANGE
                if (appContainerResponseTime.getHasTimeRangeValue() != null) {
                    //If the property IsRequired is not filled --> default value:true
                    if (appContainerResponseTime.getHasTimeRangeValue().getIsRequired() != null && appContainerResponseTime.getHasTimeRangeValue().getIsRequired()) {
                        if (appContainerResponseTime.getHasTimeRangeValue().getMax() != null
                                && appContainerResponseTime.getHasTimeRangeValue().getMax().getValue() != null) {
                            TimeUnit maxAppContainerResponseTime = appContainerResponseTime.getHasTimeRangeValue().getMax();
                            if (!qos) {
                                queryString += "?po paas-m:providesServiceQuality ?qos" + Integer.toString(i) + " .";
                                queryString += "?qos" + Integer.toString(i) + " rdf:type qos-m:ContainerResponseTime .";
                                qos = true;
                            }
                            queryString += "?qos" + Integer.toString(i) + " qos-m:hasTimeRangeValue ?qos_containerResponse_range" + Integer.toString(i) + " ."
                                    + "?qos_containerResponse_range" + Integer.toString(i) + " measure:hasMaxTimeValue ?qos_containerResponse_unit" + Integer.toString(i) + " ."
                                    + "{{?qos_containerResponse_unit" + Integer.toString(i) + " rdf:type measure:Hour ."
                                    + "?qos_containerResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_containerResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_containerResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Hours(maxAppContainerResponseTime) + ")}"
                                    + "UNION {?qos_containerResponse_unit" + Integer.toString(i) + " rdf:type measure:Minute ."
                                    + "?qos_containerResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_containerResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_containerResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Minutes(maxAppContainerResponseTime) + ")}"
                                    + "UNION {?qos_containerResponse_unit" + Integer.toString(i) + " rdf:type measure:Second ."
                                    + "?qos_containerResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_containerResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_containerResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2Second(maxAppContainerResponseTime) + ")}"
                                    + "UNION {?qos_containerResponse_unit" + Integer.toString(i) + " rdf:type measure:MilliSecond ."
                                    + "?qos_containerResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_containerResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_containerResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2MilliSecond(maxAppContainerResponseTime) + ")}"
                                    //Default MilliSecond
                                    + "UNION {?qos_containerResponse_unit" + Integer.toString(i) + " rdf:type measure:Time_Unit ."
                                    + "?qos_containerResponse_unit" + Integer.toString(i) + " measure:has_value ?qos_containerResponse_val" + Integer.toString(i) + " ."
                                    + "FILTER (?qos_containerResponse_val" + Integer.toString(i) + "<=" + MeasurementUnitConverter.convertTime2MilliSecond(maxAppContainerResponseTime) + ")}}.";
                        }
                    }
                }
            }
        }

        queryString += "}";
        logger.debug(queryString);
        return queryString;
    }

    @Override
    public PaaSInstance searchForPass(eu.cloud4soa.api.datamodel.frontend.PaaSInstance paaSInstance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized QueryResultTable sparqlSelect(String query) throws SparqlQueryException {

        Model model = repositoryManager.getModel();
        QueryResultTable result;
        org.ontoware.rdf2go.model.QueryResultTable rdf2goSparqlResult;

        try {
            List<QueryResultRow> queryResultRowList = new ArrayList<QueryResultRow>();
            rdf2goSparqlResult = model.sparqlSelect(query);
            List<String> variables = rdf2goSparqlResult.getVariables();
            for (Iterator<QueryRow> it = rdf2goSparqlResult.iterator(); it.hasNext();) {
                QueryRow queryRow = it.next();
                Map<String, String> row = new HashMap<String, String>();
                for (String varname : variables) {
                    String valueString;
                    try {
                        Node valueNode = queryRow.getValue(varname);
                        //to be moved client side!
                        if (valueNode instanceof Literal) {
                            valueString = valueNode.asLiteral().getValue();
                        } else {
                            valueString = valueNode.toString();
                        }
                    } catch (Exception ex) {
                        valueString = "";
                    }
                    row.put(varname, valueString);
                }
                queryResultRowList.add(new QueryResultRow(row));
            }
            result = new QueryResultTable(queryResultRowList, variables);
            return result;

        } catch (Exception ex) {
            throw new SparqlQueryException(" error in execution model.sparqlSelect: " + ex.getClass().getName() + " - " + ex.getMessage(), ex);
        }

    }
}
