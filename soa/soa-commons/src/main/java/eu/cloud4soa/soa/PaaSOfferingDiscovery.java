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

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.MatchingPlatform;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.PaaSProviderDetails;
import eu.cloud4soa.api.datamodel.core.SlaContract;
import eu.cloud4soa.api.governance.SLAModule;
import eu.cloud4soa.api.repository.ApplicationProfilesRepository;
import eu.cloud4soa.api.repository.PaaSOfferingProfilesRepository;
import eu.cloud4soa.api.repository.SearchAndDiscoveryInterfaces;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import eu.cloud4soa.api.datamodel.governance.SlaTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zeginis Dimitris (CERTH)
 * @author Vincenzo Laudisio (DERI)
 */
@Transactional
public class PaaSOfferingDiscovery implements eu.cloud4soa.api.soa.PaaSOfferingDiscovery {

    final Logger logger = LoggerFactory.getLogger(PaaSOfferingDiscovery.class);
    private SearchAndDiscoveryInterfaces searchAndDiscoveryInterfaces;
    eu.cloud4soa.api.datamodel.core.ApplicationInstance coreInstance;
    private SLAModule slamodule;
    MatchingPlatform negotiatedSearchResults;
    private PaaSOfferingProfilesRepository paaSOfferingProfilesRepository;
    private ApplicationProfilesRepository applicationProfilesRepository;

    @Required
    public void setPaaSOfferingProfilesRepository(PaaSOfferingProfilesRepository paaSOfferingProfilesRepository) {
        this.paaSOfferingProfilesRepository = paaSOfferingProfilesRepository;
    }

    /**
     * @param applicationProfilesRepository the applicationProfilesRepository to set
     */
    @Required
    public void setApplicationProfilesRepository(ApplicationProfilesRepository applicationProfilesRepository) {
        this.applicationProfilesRepository = applicationProfilesRepository;
    }

    @Required
    public void setSearchAndDiscoveryInterfaces(SearchAndDiscoveryInterfaces searchAndDiscoveryInterfaces) {
        this.searchAndDiscoveryInterfaces = searchAndDiscoveryInterfaces;
    }

    public void setSlaModule(SLAModule slamodule) {
        this.slamodule = slamodule;
    }

    @Override
    public MatchingPlatform searchForMatchingPlatform(String applicationInstanceUriId) throws SOAException {
        logger.debug("received applicationInstanceUriId: " + applicationInstanceUriId);

        logger.debug("call applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId)");

        ApplicationInstance applicationInstance;
        try {
            applicationInstance = applicationProfilesRepository.getApplicationInstance(applicationInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        logger.debug("retrived applicationInstance: " + applicationInstance);

        Map<PaaSInstance,Float> searchResultList = searchAndDiscoveryInterfaces.searchForMatchingPlatform(applicationInstance);

        Map<PaaSInstance,SlaTemplate> slatemplates = new HashMap<PaaSInstance,SlaTemplate>();

        Iterator<PaaSInstance> iterator = searchResultList.keySet().iterator();

        while (iterator.hasNext()) {
            PaaSInstance paasInstance = iterator.next();
            SlaTemplate slaTemplate = slamodule.startNegotiation(applicationInstance, paasInstance);
            slatemplates.put(paasInstance, slaTemplate);
        }

        //To instantiate        
        MatchingPlatform negotiatedSearchResults = new MatchingPlatform();
        negotiatedSearchResults.setRankedListPaaSInstances(searchResultList);
        //negotiatedSearchResults.setListSlaContract(slatemplates);
        negotiatedSearchResults.setListSlaTemplates(slatemplates);

        return negotiatedSearchResults;
    }

    @Override
    public PaaSInstance searchForPaaS(String paaSInstanceUriId) throws SOAException {
        logger.debug("call paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId)");
        PaaSInstance retrievedpaaSInstance;
        try {
            retrievedpaaSInstance = paaSOfferingProfilesRepository.getPaaSInstance(paaSInstanceUriId);
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("retrieved paaSInstance: "+retrievedpaaSInstance);
        return retrievedpaaSInstance;
    }

    @Override
    public PaaSProviderDetails getPaaSProviderDetails(String paaSInstanceUriId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String query(String sparql) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PaaSInstance> getAllAvailablePaaSInstances() throws SOAException {
        logger.debug("call paaSOfferingProfilesRepository.getAllAvailablePaaSInstances()");
        List<PaaSInstance> allAvailablePaaSInstances;
        try {
            allAvailablePaaSInstances = paaSOfferingProfilesRepository.getAllAvailablePaaSInstances();
        } catch (RepositoryException ex) {
            throw new SOAException(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        logger.debug("retrieved allAvailablePaaSInstances: " + allAvailablePaaSInstances);
        return allAvailablePaaSInstances;
    }
}
