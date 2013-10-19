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

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.HardwareComponentInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.SoftwareComponentInstance;
import eu.cloud4soa.api.datamodel.semantic.ea.CloudResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.ContainerResponseTime;
import eu.cloud4soa.api.datamodel.semantic.ea.Latency;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.ea.Uptime;
import eu.cloud4soa.api.datamodel.semantic.inf.Compute;
import eu.cloud4soa.api.datamodel.semantic.inf.DBStorageComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.HttpRequestsHandler;
import eu.cloud4soa.api.datamodel.semantic.inf.NetworkResource;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.StorageResource;
import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.TimeUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Zeginis
 */
public class PaaSRankingCalculator {

    private ComputationalConverter computationalConverter;

    @Required
    public void setComputationalConverter(ComputationalConverter computationalConverter) {
        this.computationalConverter = computationalConverter;
    }

    public float calculateRanking(PaaSInstance paasInstance, ApplicationInstance applicationInstance) {

        float numOfpreference = 0;
        float satisfiedPreference = 0;

        //Compute scaling factor 
        if (applicationInstance.getApplication().getRequireComputeScalingFactor() != null
                && (applicationInstance.getApplication().getRequireComputeScalingFactor().getIsRequired() == null
                    ||(applicationInstance.getApplication().getRequireComputeScalingFactor().getIsRequired() != null
                       && !applicationInstance.getApplication().getRequireComputeScalingFactor().getIsRequired()))
                && applicationInstance.getApplication().getRequireComputeScalingFactor().getMin() != null) {

            numOfpreference++;
            Float minAppComputeScalingFactor = applicationInstance.getApplication().getRequireComputeScalingFactor().getMin();

            if (paasInstance.getPaaSOffering().getOfferComputeScalingFactor() != null
                    && paasInstance.getPaaSOffering().getOfferComputeScalingFactor().getMax() != null) {

                Float maxPaaSComputeScalingFactor = paasInstance.getPaaSOffering().getOfferComputeScalingFactor().getMax();
                if (minAppComputeScalingFactor <= maxPaaSComputeScalingFactor) {
                    satisfiedPreference++;
                }
            }
        }

        //Web scaling factor
        if (applicationInstance.getApplication().getRequireWebScalingFactor() != null
                && (applicationInstance.getApplication().getRequireWebScalingFactor().getIsRequired() == null||
                    (applicationInstance.getApplication().getRequireWebScalingFactor().getIsRequired() != null
                      && !applicationInstance.getApplication().getRequireWebScalingFactor().getIsRequired()))
                && applicationInstance.getApplication().getRequireWebScalingFactor().getMin() != null) {

            numOfpreference++;
            Float minWebScalingFactor = applicationInstance.getApplication().getRequireWebScalingFactor().getMin();

            if (paasInstance.getPaaSOffering().getOfferWebScalingFactor() != null
                    && paasInstance.getPaaSOffering().getOfferWebScalingFactor().getMax() != null) {

                Float maxPaaSWebScalingFactor = paasInstance.getPaaSOffering().getOfferWebScalingFactor().getMax();
                if (minWebScalingFactor <= maxPaaSWebScalingFactor) {
                    satisfiedPreference++;
                }
            }
        }

        //SOFTWARE COMPONENTS
        List<SoftwareComponentInstance> applicationSWcomponents = applicationInstance.getSoftwareComponents();
        List<SoftwareComponentInstance> paasSWcomponents = paasInstance.getSoftwareComponents();

        for (int i = 0; i < applicationSWcomponents.size(); i++) {
            SoftwareComponentInstance swAppInstance = applicationSWcomponents.get(i);
            SoftwareComponent swAppComponent = swAppInstance.getSoftwareComponent();




            if ((swAppComponent.getIsRequired() == null || (swAppComponent.getIsRequired() != null && !swAppComponent.getIsRequired()))
                    && swAppComponent.getRelatedswcategory() != null && swAppComponent.getRelatedswcategory().getUriId() != null) {

                numOfpreference++;
                String appSWcomponentUriId = swAppComponent.getRelatedswcategory().getUriId();

                for (int j = 0; j < paasSWcomponents.size(); j++) {
                    SoftwareComponentInstance swPaasInstance = paasSWcomponents.get(j);
                    SoftwareComponent swPaasComponent = swPaasInstance.getSoftwareComponent();

                    if (swPaasComponent.getRelatedswcategory() != null && swPaasComponent.getRelatedswcategory().getUriId() != null) {
                        String paasSWcomponentUriId = swPaasComponent.getRelatedswcategory().getUriId();
                        if (paasSWcomponentUriId.equals(appSWcomponentUriId)) {
                            satisfiedPreference++;
                            break; //SW component with same category found
                        }
                    }
                }
            }

            if (swAppComponent instanceof DBStorageComponent) {
                DBStorageComponent appDBStorageComponent = (DBStorageComponent) swAppComponent;

                boolean checkDBcache = false;
                boolean checkDBcapacity = false;

                //DB CACHE
                if (appDBStorageComponent.getDBconfiguration() != null
                        && appDBStorageComponent.getDBconfiguration().getHasDBcache() != null
                        && (appDBStorageComponent.getDBconfiguration().getHasDBcache().getIsRequired() == null||
                            (appDBStorageComponent.getDBconfiguration().getHasDBcache().getIsRequired() != null
                             && !appDBStorageComponent.getDBconfiguration().getHasDBcache().getIsRequired()))
                        && appDBStorageComponent.getDBconfiguration().getHasDBcache().getMin() != null
                        && appDBStorageComponent.getDBconfiguration().getHasDBcache().getMin().getValue() != null) {

                    checkDBcache = true;
                    numOfpreference++;
                }

                //DB CAPACITY
                if (appDBStorageComponent.getDBconfiguration() != null
                        && appDBStorageComponent.getDBconfiguration().getHasDBcapacity() != null
                        && (appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getIsRequired() == null||
                            (appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getIsRequired() != null
                             && !appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getIsRequired()))
                        && appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMin() != null
                        && appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMin().getValue() != null) {

                    checkDBcapacity = true;
                    numOfpreference++;
                }


                int maxSWdbRanking = 0;
                for (int j = 0; j < paasSWcomponents.size(); j++) {
                    SoftwareComponentInstance swPaasInstance = paasSWcomponents.get(j);
                    SoftwareComponent swPaasComponent = swPaasInstance.getSoftwareComponent();
                    if (swPaasComponent.getRelatedswcategory().getUriId().equals(appDBStorageComponent.getRelatedswcategory().getUriId())) {
                        int swDBRanking = 0;
                        if (swPaasComponent instanceof DBStorageComponent) {
                            DBStorageComponent paasDBStorageComponent = (DBStorageComponent) swPaasComponent;

                            //DB CACHE
                            if (checkDBcache
                                    && paasDBStorageComponent.getDBconfiguration() != null
                                    && paasDBStorageComponent.getDBconfiguration().getHasDBcache() != null
                                    && paasDBStorageComponent.getDBconfiguration().getHasDBcache().getMax() != null
                                    && paasDBStorageComponent.getDBconfiguration().getHasDBcache().getMax().getValue() != null) {

                                StorageUnit minAppDBcache = appDBStorageComponent.getDBconfiguration().getHasDBcache().getMin();
                                StorageUnit maxPaasDBcache = paasDBStorageComponent.getDBconfiguration().getHasDBcache().getMax();


                                if (MeasurementUnitConverter.convertStorage2Gigabyte(minAppDBcache)
                                        <= MeasurementUnitConverter.convertStorage2Gigabyte(maxPaasDBcache)) {
                                    swDBRanking++;
                                }
                            }

                            //DB CAPACITY
                            if (checkDBcapacity
                                    && paasDBStorageComponent.getDBconfiguration() != null
                                    && paasDBStorageComponent.getDBconfiguration().getHasDBcapacity() != null
                                    && paasDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMax() != null
                                    && paasDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMax().getValue() != null) {

                                StorageUnit minAppDBcapacity = appDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMin();
                                StorageUnit maxPaasDBcapacity = paasDBStorageComponent.getDBconfiguration().getHasDBcapacity().getMax();


                                if (MeasurementUnitConverter.convertStorage2Gigabyte(minAppDBcapacity)
                                        <= MeasurementUnitConverter.convertStorage2Gigabyte(maxPaasDBcapacity)) {
                                    swDBRanking++;
                                }
                            }
                        }

                        maxSWdbRanking = Math.max(maxSWdbRanking, swDBRanking);
                    }

                }
                satisfiedPreference += maxSWdbRanking;
            }
        }

        //Hardware components
        List< HardwareComponentInstance> applicationHWcomponents = applicationInstance.getHardwareComponents();
        List<HardwareComponentInstance> paasHWcomponents = paasInstance.getHardwareComponents();
        for (int i = 0; i < applicationHWcomponents.size(); i++) {
            HardwareComponentInstance hwAppInstance = applicationHWcomponents.get(i);
            HardwareComponent hdAppComponent = hwAppInstance.getHardwareComponent();

            //NETWORK RESOURCE
            if (hdAppComponent instanceof NetworkResource) {
                NetworkResource appNetworkResource = (NetworkResource) hdAppComponent;

                boolean checkBandwidth = false;
                boolean checkLatency = false;
                //BANDWIDTH
                if (appNetworkResource.getBandwidth() != null
                        && (appNetworkResource.getBandwidth().getIsRequired() == null||
                            (appNetworkResource.getBandwidth().getIsRequired() != null
                             && !appNetworkResource.getBandwidth().getIsRequired()))
                        && appNetworkResource.getBandwidth().getMin() != null
                        && appNetworkResource.getBandwidth().getMin().getValue() != null) {

                    checkBandwidth = true;
                    numOfpreference++;
                }

                //LATENCY
                if (appNetworkResource.getLatency() != null
                        && (appNetworkResource.getLatency().getIsRequired() == null||
                            (appNetworkResource.getLatency().getIsRequired() != null
                             && !appNetworkResource.getLatency().getIsRequired()))
                        && appNetworkResource.getLatency().getMin() != null
                        && appNetworkResource.getLatency().getMin().getValue() != null) {

                    checkLatency = true;
                    numOfpreference++;
                }

                int maxNetworkResourceRanking = 0;
                for (int j = 0; j < paasHWcomponents.size(); j++) {
                    HardwareComponentInstance hwPaaSInstance = paasHWcomponents.get(j);
                    HardwareComponent hdPaaSComponent = hwPaaSInstance.getHardwareComponent();
                    int networkResourceRanking = 0;
                    if (hdPaaSComponent instanceof NetworkResource) {
                        NetworkResource paasNetworkResource = (NetworkResource) hdPaaSComponent;

                        //BANDWIDTH
                        if (checkBandwidth
                                && paasNetworkResource.getBandwidth() != null
                                && paasNetworkResource.getBandwidth().getMax() != null
                                && paasNetworkResource.getBandwidth().getMax().getValue() != null) {

                            NetworkingUnit minAppBandwidth = appNetworkResource.getBandwidth().getMin();
                            NetworkingUnit maxPaasBandwidth = paasNetworkResource.getBandwidth().getMax();

                            if (MeasurementUnitConverter.convertBandwidth2GigabytePerSecond(minAppBandwidth)
                                    <= MeasurementUnitConverter.convertBandwidth2GigabytePerSecond(maxPaasBandwidth)) {
                                networkResourceRanking++;
                            }
                        }
                        //LATENCY
                        if (checkLatency
                                && paasNetworkResource.getLatency() != null
                                && paasNetworkResource.getLatency().getMin() != null
                                && paasNetworkResource.getLatency().getMin().getValue() != null) {

                            TimeUnit maxAppLatency = appNetworkResource.getLatency().getMax();
                            TimeUnit minPaasLatency = paasNetworkResource.getLatency().getMin();

                            if (MeasurementUnitConverter.convertTime2MilliSecond(minPaasLatency)
                                    <= MeasurementUnitConverter.convertTime2MilliSecond(maxAppLatency)) {
                                networkResourceRanking++;
                            }

                        }
                    }

                    maxNetworkResourceRanking = Math.max(maxNetworkResourceRanking, networkResourceRanking);
                }
                satisfiedPreference += maxNetworkResourceRanking;

                // HTTP REQUESTS HANDLER
            } else if (hdAppComponent instanceof HttpRequestsHandler) {
                HttpRequestsHandler appBox = (HttpRequestsHandler) hdAppComponent;

                boolean checkHTTPrequests = false;
                boolean checkComputationalPowerFactor = false;

                //HTTP REQUESTS
                if (appBox.getHTTPRequests() != null
                        && (appBox.getHTTPRequests().getIsRequired() == null||
                            (appBox.getHTTPRequests().getIsRequired() != null
                             && !appBox.getHTTPRequests().getIsRequired()))
                        && appBox.getHTTPRequests().getMin() != null) {

                    checkHTTPrequests = true;
                    numOfpreference++;
                }

                //COMPUTATIONAL POWER FACTOR
                if (appBox.getComputationalPowerFactor() != null
                        && (appBox.getComputationalPowerFactor().getIsRequired() == null||
                            (appBox.getComputationalPowerFactor().getIsRequired() != null
                             && !appBox.getComputationalPowerFactor().getIsRequired()))
                        && appBox.getComputationalPowerFactor().getMin() != null) {

                    checkComputationalPowerFactor = true;
                    numOfpreference++;
                }

                int maxHTTPhandlerRanking = 0;
                for (int j = 0; j < paasHWcomponents.size(); j++) {
                    HardwareComponentInstance hwPaaSInstance = paasHWcomponents.get(j);
                    HardwareComponent hdPaaSComponent = hwPaaSInstance.getHardwareComponent();
                    int HTTPhandlerRanking = 0;
                    if (hdPaaSComponent instanceof HttpRequestsHandler) {
                        HttpRequestsHandler paasBox = (HttpRequestsHandler) hdPaaSComponent;

                        //HTTP REQUESTS
                        if (checkHTTPrequests
                                && paasBox.getHTTPRequests() != null
                                && paasBox.getHTTPRequests().getMax() != null) {

                            float minAppHTTPrequests = appBox.getHTTPRequests().getMin();
                            float maxPaasHTTPrequests = paasBox.getHTTPRequests().getMax();

                            if (minAppHTTPrequests <= maxPaasHTTPrequests) {
                                HTTPhandlerRanking++;
                            }
                        }

                        //COMPUTATIONAL POWER FACTOR
                        if (checkComputationalPowerFactor
                                && paasBox.getComputationalPowerFactor() != null
                                && paasBox.getComputationalPowerFactor().getMax() != null) {

                            float minAppComputationalPowerFactor = appBox.getComputationalPowerFactor().getMin();
                            float maxPaasComputationalPowerFactor = paasBox.getComputationalPowerFactor().getMax();

                            if (computationalConverter.compareComputationalPowerFactor(minAppComputationalPowerFactor, maxPaasComputationalPowerFactor,
                                    appBox.getRelatedhwcategory().getUriId(), paasBox.getRelatedhwcategory().getUriId())) {
                                HTTPhandlerRanking++;
                            }
                        }
                    }

                    maxHTTPhandlerRanking = Math.max(maxHTTPhandlerRanking, HTTPhandlerRanking);
                }
                satisfiedPreference += maxHTTPhandlerRanking;

                //COMPUTE COMPONENT
            } else if (hdAppComponent instanceof Compute) {
                Compute appCompute = (Compute) hdAppComponent;

                boolean checkCache = false;
                boolean checkCores = false;
                boolean checkMemory = false;
                boolean checkComputationalPowerFactor = false;

                //CACHE
                if (appCompute.getCache() != null
                        && (appCompute.getCache().getIsRequired() == null||
                            (appCompute.getCache().getIsRequired() != null
                             && !appCompute.getCache().getIsRequired()))
                        && appCompute.getCache().getMin() != null
                        && appCompute.getCache().getMin().getValue() != null) {

                    checkCache = true;
                    numOfpreference++;
                }

                //CORES
                if (appCompute.getHasCores() != null
                        && (appCompute.getHasCores().getIsRequired() == null||
                            (appCompute.getHasCores().getIsRequired() != null
                             && !appCompute.getHasCores().getIsRequired()))
                        && appCompute.getHasCores().getMin() != null) {

                    checkCores = true;
                    numOfpreference++;
                }

                //MEMORY
                if (appCompute.getMemory() != null
                        && (appCompute.getMemory().getIsRequired() == null||
                            (appCompute.getMemory().getIsRequired() != null
                             && !appCompute.getMemory().getIsRequired()))
                        && appCompute.getMemory().getMin() != null
                        && appCompute.getMemory().getMin().getValue() != null) {

                    checkMemory = true;
                    numOfpreference++;
                }

                // COMPUTATIONAL POWER FACTOR
                if (appCompute.getComputationalPowerFactor() != null
                        && (appCompute.getComputationalPowerFactor().getIsRequired() == null||
                             (appCompute.getComputationalPowerFactor().getIsRequired() != null
                              && !appCompute.getComputationalPowerFactor().getIsRequired()))
                        && appCompute.getComputationalPowerFactor().getMin() != null) {

                    checkComputationalPowerFactor = true;
                    numOfpreference++;
                }

                int maxComputeRanking = 0;
                for (int j = 0; j < paasHWcomponents.size(); j++) {
                    HardwareComponentInstance hwPaaSInstance = paasHWcomponents.get(j);
                    HardwareComponent hdPaaSComponent = hwPaaSInstance.getHardwareComponent();
                    int computeRanking = 0;
                    if (hdPaaSComponent instanceof Compute) {
                        Compute paasCompute = (Compute) hdPaaSComponent;

                        //CACHE
                        if (checkCache
                                && paasCompute.getCache() != null
                                && paasCompute.getCache().getMax() != null
                                && paasCompute.getCache().getMax().getValue() != null) {

                            StorageUnit minAppCache = appCompute.getCache().getMin();
                            StorageUnit maxPaasCache = paasCompute.getCache().getMax();

                            if (MeasurementUnitConverter.convertStorage2Gigabyte(minAppCache)
                                    <= MeasurementUnitConverter.convertStorage2Gigabyte(maxPaasCache)) {
                                computeRanking++;
                            }
                        }

                        //CORES
                        if (checkCores
                                && paasCompute.getHasCores() != null
                                && paasCompute.getHasCores().getMax() != null) {

                            float minAppCores = appCompute.getHasCores().getMin();
                            float maxPaasCores = paasCompute.getHasCores().getMax();

                            if (minAppCores <= maxPaasCores) {
                                computeRanking++;
                            }
                        }

                        //MEMORY
                        if (checkMemory
                                && paasCompute.getMemory() != null
                                && paasCompute.getMemory().getMax() != null
                                && paasCompute.getMemory().getMax().getValue() != null) {

                            StorageUnit minAppMemory = appCompute.getMemory().getMin();
                            StorageUnit maxPaasMemory = paasCompute.getMemory().getMax();

                            if (MeasurementUnitConverter.convertStorage2Gigabyte(minAppMemory)
                                    <= MeasurementUnitConverter.convertStorage2Gigabyte(maxPaasMemory)) {
                                computeRanking++;
                            }
                        }

                        //COMPUTATIONAL POWER FACTOR
                        if (checkComputationalPowerFactor
                                && paasCompute.getComputationalPowerFactor() != null
                                && paasCompute.getComputationalPowerFactor().getMax() != null) {

                            float minAppComputationalPowerFactor = appCompute.getComputationalPowerFactor().getMin();
                            float maxPaasComputationalPowerFactor = paasCompute.getComputationalPowerFactor().getMax();

                            // ComputationalConverter conv = new ComputationalConverter();
                            if (computationalConverter.compareComputationalPowerFactor(minAppComputationalPowerFactor, maxPaasComputationalPowerFactor,
                                    appCompute.getRelatedhwcategory().getUriId(), paasCompute.getRelatedhwcategory().getUriId())) {
                                computeRanking++;
                            }
                        }

                    }

                    maxComputeRanking = Math.max(maxComputeRanking, computeRanking);
                }
                satisfiedPreference += maxComputeRanking;

            } else if (hdAppComponent instanceof StorageResource) {
                StorageResource appStorageResource = (StorageResource) hdAppComponent;

                boolean checkCapacity = false;
                boolean checkBandwidth = false;

                //CAPACITY
                if (appStorageResource.getCapacity() != null
                        && (appStorageResource.getCapacity().getIsRequired() == null||
                             (appStorageResource.getCapacity().getIsRequired() != null
                              && !appStorageResource.getCapacity().getIsRequired()))
                        && appStorageResource.getCapacity().getMin() != null
                        && appStorageResource.getCapacity().getMin().getValue() != null) {

                    checkCapacity = true;
                    numOfpreference++;
                }

                //BANDWIDTH
                if (appStorageResource.getBandwidth() != null
                        && (appStorageResource.getBandwidth().getIsRequired() == null||
                             (appStorageResource.getBandwidth().getIsRequired() != null
                              && !appStorageResource.getBandwidth().getIsRequired()))
                        && appStorageResource.getBandwidth().getMin() != null
                        && appStorageResource.getBandwidth().getMin().getValue() != null) {

                    checkBandwidth = true;
                    numOfpreference++;
                }

                int maxStorageRanking = 0;
                for (int j = 0; j < paasHWcomponents.size(); j++) {
                    HardwareComponentInstance hwPaaSInstance = paasHWcomponents.get(j);
                    HardwareComponent hdPaaSComponent = hwPaaSInstance.getHardwareComponent();
                    int storageRanking = 0;
                    if (hdPaaSComponent instanceof StorageResource) {
                        StorageResource paasStorageResource = (StorageResource) hdPaaSComponent;

                        //CAPACITY
                        if (checkCapacity
                                && paasStorageResource.getCapacity() != null
                                && paasStorageResource.getCapacity().getMax() != null
                                && paasStorageResource.getCapacity().getMax().getValue() != null) {

                            StorageUnit minAppCapacity = appStorageResource.getCapacity().getMin();
                            StorageUnit maxPaasCapacity = paasStorageResource.getCapacity().getMax();

                            if (MeasurementUnitConverter.convertStorage2Gigabyte(minAppCapacity)
                                    <= MeasurementUnitConverter.convertStorage2Gigabyte(maxPaasCapacity)) {
                                storageRanking++;
                            }
                        }

                        //BANDWIDTH
                        if (checkBandwidth
                                && paasStorageResource.getBandwidth() != null
                                && paasStorageResource.getBandwidth().getMax() != null
                                && paasStorageResource.getBandwidth().getMax().getValue() != null) {

                            NetworkingUnit minAppBandwidth = appStorageResource.getBandwidth().getMin();
                            NetworkingUnit maxPaasBandwidth = paasStorageResource.getBandwidth().getMax();

                            if (MeasurementUnitConverter.convertBandwidth2GigabytePerSecond(minAppBandwidth)
                                    <= MeasurementUnitConverter.convertBandwidth2GigabytePerSecond(maxPaasBandwidth)) {
                                storageRanking++;
                            }
                        }
                    }

                    maxStorageRanking = Math.max(maxStorageRanking, storageRanking);
                }
                satisfiedPreference += maxStorageRanking;
            }
        }

        //QoS 
        List<Technology_Service_Quality> applicationSWqualities = applicationInstance.getApplication().getRequiresServiceQuality();
        List<Technology_Service_Quality> paasSWqualities = paasInstance.getPaaSOffering().getProvidesServiceQuality();
        for (int i = 0; i < applicationSWqualities.size(); i++) {
            Technology_Service_Quality appQuality = applicationSWqualities.get(i);

            if (appQuality instanceof Latency) {
                Latency appLatency = (Latency) appQuality;

                if (appLatency.getHasTimeRangeValue() != null
                        && (appLatency.getHasTimeRangeValue().getIsRequired() == null||
                            (appLatency.getHasTimeRangeValue().getIsRequired() != null
                             && !appLatency.getHasTimeRangeValue().getIsRequired()))
                        && appLatency.getHasTimeRangeValue().getMax() != null
                        && appLatency.getHasTimeRangeValue().getMax().getValue() != null) {

                    numOfpreference++;

                    for (int j = 0; j < paasSWqualities.size(); j++) {
                        Technology_Service_Quality paasQuality = paasSWqualities.get(j);

                        if (paasQuality instanceof Latency) {
                            Latency paasLatency = (Latency) paasQuality;

                            if (paasLatency.getHasTimeRangeValue() != null
                                    && paasLatency.getHasTimeRangeValue().getMax() != null
                                    && paasLatency.getHasTimeRangeValue().getMax().getValue() != null) {

                                TimeUnit maxAppLatency = appLatency.getHasTimeRangeValue().getMax();
                                TimeUnit maxPaasLatency = paasLatency.getHasTimeRangeValue().getMax();

                                if (MeasurementUnitConverter.convertTime2MilliSecond(maxPaasLatency)
                                        <= MeasurementUnitConverter.convertTime2MilliSecond(maxAppLatency)) {
                                    satisfiedPreference++;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (appQuality instanceof Uptime) {
                Uptime appUptimeQuality = (Uptime) appQuality;

                if (appUptimeQuality.getHasPercentage() != null) {

                    numOfpreference++;

                    for (int j = 0; j < paasSWqualities.size(); j++) {
                        Technology_Service_Quality paasQuality = paasSWqualities.get(j);

                        if (paasQuality instanceof Uptime) {
                            Uptime paasUptime = (Uptime) paasQuality;

                            //CAPACITY
                            if (paasUptime.getHasPercentage() != null) {

                                Float maxAppUptime = appUptimeQuality.getHasPercentage();
                                Float maxPaasUptime = paasUptime.getHasPercentage();

                                if (maxAppUptime <= maxPaasUptime) {
                                    satisfiedPreference++;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (appQuality instanceof CloudResponseTime) {
                CloudResponseTime appCloudResponseTime = (CloudResponseTime) appQuality;

                if (appCloudResponseTime.getHasTimeRangeValue() != null
                        && (appCloudResponseTime.getHasTimeRangeValue().getIsRequired() == null||
                            (appCloudResponseTime.getHasTimeRangeValue().getIsRequired() != null
                             && !appCloudResponseTime.getHasTimeRangeValue().getIsRequired()))
                        && appCloudResponseTime.getHasTimeRangeValue().getMax() != null
                        && appCloudResponseTime.getHasTimeRangeValue().getMax().getValue() != null) {

                    numOfpreference++;

                    for (int j = 0; j < paasSWqualities.size(); j++) {
                        Technology_Service_Quality paasQuality = paasSWqualities.get(j);

                        if (paasQuality instanceof CloudResponseTime) {
                            CloudResponseTime paasCloudResponseTime = (CloudResponseTime) paasQuality;

                            if (paasCloudResponseTime.getHasTimeRangeValue() != null
                                    && paasCloudResponseTime.getHasTimeRangeValue().getMax() != null
                                    && paasCloudResponseTime.getHasTimeRangeValue().getMax().getValue() != null) {

                                TimeUnit maxAppCloudResponseTime = appCloudResponseTime.getHasTimeRangeValue().getMax();
                                TimeUnit maxPaasCloudResponseTime = paasCloudResponseTime.getHasTimeRangeValue().getMax();

                                if (MeasurementUnitConverter.convertTime2MilliSecond(maxPaasCloudResponseTime)
                                        <= MeasurementUnitConverter.convertTime2MilliSecond(maxAppCloudResponseTime)) {
                                    satisfiedPreference++;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (appQuality instanceof ContainerResponseTime) {
                ContainerResponseTime appContainerResponseTime = (ContainerResponseTime) appQuality;

                if (appContainerResponseTime.getHasTimeRangeValue() != null
                        && (appContainerResponseTime.getHasTimeRangeValue().getIsRequired() == null||
                            (appContainerResponseTime.getHasTimeRangeValue().getIsRequired() != null
                             && !appContainerResponseTime.getHasTimeRangeValue().getIsRequired()))
                        && appContainerResponseTime.getHasTimeRangeValue().getMax() != null
                        && appContainerResponseTime.getHasTimeRangeValue().getMax().getValue() != null) {

                    numOfpreference++;

                    for (int j = 0; j < paasSWqualities.size(); j++) {
                        Technology_Service_Quality paasQuality = paasSWqualities.get(j);

                        if (paasQuality instanceof ContainerResponseTime) {
                            ContainerResponseTime paasContainerResponseTime = (ContainerResponseTime) paasQuality;

                            if (paasContainerResponseTime.getHasTimeRangeValue() != null
                                    && paasContainerResponseTime.getHasTimeRangeValue().getMax() != null
                                    && paasContainerResponseTime.getHasTimeRangeValue().getMax().getValue() != null) {

                                TimeUnit maxAppContainerResponseTime = appContainerResponseTime.getHasTimeRangeValue().getMax();
                                TimeUnit maxPaasContainerResponseTime = paasContainerResponseTime.getHasTimeRangeValue().getMax();

                                if (MeasurementUnitConverter.convertTime2MilliSecond(maxPaasContainerResponseTime)
                                        <= MeasurementUnitConverter.convertTime2MilliSecond(maxAppContainerResponseTime)) {
                                    satisfiedPreference++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (numOfpreference == 0) {
            return 1;
        }

        return (satisfiedPreference / numOfpreference);
    }
}
