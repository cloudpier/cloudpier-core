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

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.repository.FiveStarsRate;
import eu.cloud4soa.api.datamodel.repository.UserExperienceRate;
import eu.cloud4soa.api.repository.ApplicationProfilesRepository;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.api.util.exception.soa.SOAException;
import eu.cloud4soa.relational.datamodel.Breach;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import eu.cloud4soa.relational.persistence.BreachRepository;

/**
 * @author Zeginis Dimitris (CERTH)
 * @author Vincenzo Laudisio (DERI)
 */
@Transactional
public class PaaSOfferingRecommendation implements eu.cloud4soa.api.soa.PaaSOfferingRecommendation {

    final Logger logger = LoggerFactory.getLogger(PaaSOfferingRecommendation.class);
    private UserExperienceRate userExperienceRate;
    private ApplicationProfilesRepository applicationProfilesRepository;
    private ApplicationInstanceRepository applicationInstanceRepository;
    private BreachRepository breachRepository;

    @Required
    public void setBreachRepository(BreachRepository breachRepository) {
        this.breachRepository = breachRepository;
    }

    @Required
    public void setApplicationProfilesRepository(ApplicationProfilesRepository applicationProfilesRepository) {
        this.applicationProfilesRepository = applicationProfilesRepository;
    }

    @Required
    public void setApplicationInstanceRepository(ApplicationInstanceRepository applicationInstanceRepository) {
        this.applicationInstanceRepository = applicationInstanceRepository;
    }

    @Required
    public void setUserExperienceRate(UserExperienceRate userExperienceRate) {
        this.userExperienceRate = userExperienceRate;
    }

    /*    @Override
    public void ratePaaSProvider(PaaSInstance paaSInstance, PaaSRating paaSRating) {
    throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public MatchingPlatform getRecommendation(MatchingPlatform matchingPlatform, ApplicationInstance applicationInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
    }*/
    @Override
    public void storeUserExperienceRate(String appURI, FiveStarsRate rate) throws SOAException {
        logger.debug("Store User experience rate appURI: " + appURI + " rate:" + rate.getRate());
        userExperienceRate.storeUserExperienceRate(appURI, rate);
    }

    @Override
    public void deleteUserExperienceRate(String appURI) throws SOAException {
        logger.debug("Delete User experience rate for appURI: " + appURI);
        userExperienceRate.deleteUserExperienceRate(appURI);
    }

    @Override
    public FiveStarsRate getUserExperienceRate(String appURI) throws SOAException {
        logger.debug("Get User experience rate for appURI: " + appURI);
        return userExperienceRate.getUserExperienceRate(appURI);
    }

    @Override
    public void updateUserExperienceRate(String appURI, String rate) throws SOAException {
        logger.debug("Update User experience rate for appURI: " + appURI);
        int intRate = Integer.parseInt(rate);
        if ((intRate < 1) || (intRate > 5)) {
            throw new IllegalArgumentException();
        } else {
            userExperienceRate.updateUserExperienceRate(appURI, intRate);
        }
    }

    @Override
    public List<FiveStarsRate> getPaaSUserExperienceEvaluation(String paasURI) throws SOAException {
        logger.debug("Get PaaS User Experience Evaluation for paasURI: " + paasURI);
        return userExperienceRate.getPaasUserExperienceEvaluation(paasURI);
    }

    @Override
    public float getAveragePaaSUserExperienceRate(String paasURI) throws SOAException {
        logger.debug("Get average PaaS User Experience Rate for paasURI: " + paasURI);
        List<FiveStarsRate> userRates = userExperienceRate.getPaasUserExperienceEvaluation(paasURI);
        Iterator it = userRates.iterator();
        int i = 0;
        int sum = 0;
        while (it.hasNext()) {
            FiveStarsRate rate = (FiveStarsRate) it.next();
            sum += rate.getRate();
            i++;
        }

        float average = (float) sum / i;
        return average;
    }

    @Override
    public float getPaaSBreachesPerWeek(String paasURI) throws SOAException, RepositoryException {
        logger.debug("Get PaaS Breaches per week for paasURI: " + paasURI);
        long totalRunningTime = getAllAppsRunningTime(paasURI);
        if (totalRunningTime == 0) {
            return 0;
        }
        List<Breach> breaches = breachRepository.retrieveAllForOffering(paasURI);
        float breachperweek = (float) ((604800000.0 * breaches.size()) / totalRunningTime);
        return breachperweek;
    }

    @Override
    public float getPaaSBreachesPerMonth(String paasURI) throws SOAException, RepositoryException {
        logger.debug("Get PaaS Breaches per week for paasURI: " + paasURI);
        long totalRunningTime = getAllAppsRunningTime(paasURI);
        if (totalRunningTime == 0) {
            return 0;
        }
        List<Breach> breaches = breachRepository.retrieveAllForOffering(paasURI);
        float breachperweek = (float) ((2592000000.0 * breaches.size()) / totalRunningTime);
        return breachperweek;
    }

    @Override
    public float getPaaSBreachesPerDay(String paasURI) throws SOAException, RepositoryException {
        logger.debug("Get PaaS Breaches per week for paasURI: " + paasURI);
        long totalRunningTime = getAllAppsRunningTime(paasURI);
        if (totalRunningTime == 0) {
            return 0;
        }
        List<Breach> breaches = breachRepository.retrieveAllForOffering(paasURI);
        float breachperweek = (float) ((86400000.0 * breaches.size()) / totalRunningTime);
        return breachperweek;
    }

    private long getAllAppsRunningTime(String paasURI) throws RepositoryException {
        List<ApplicationInstance> appsInst = applicationProfilesRepository.retrieveAllApplicationProfileDeployedOnPaaS(paasURI);
        Iterator<ApplicationInstance> it = appsInst.iterator();
        long totalRunningTime = 0;
        while (it.hasNext()) {
            ApplicationInstance app = it.next();
            totalRunningTime += applicationInstanceRepository.updateAndGetApplicationTotalRunningTime(app.getUriId());
        }
        return totalRunningTime;

    }
}
