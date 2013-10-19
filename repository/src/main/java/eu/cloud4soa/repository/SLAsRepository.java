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

import eu.cloud4soa.api.datamodel.governance.ApplicationInstance;
import eu.cloud4soa.api.datamodel.governance.PaaSInstance;
import eu.cloud4soa.api.datamodel.governance.SlaContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vincenzo
 */
public class SLAsRepository implements eu.cloud4soa.api.repository.SLAsRepository{
    final Logger logger = LoggerFactory.getLogger(SLAsRepository.class);

    @Override
    public void storeSlaContract(SlaContract slaContract) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SlaContract getSlaContract(ApplicationInstance applicationInstance, PaaSInstance paaSInstance) {
        logger.error("UnsupportedOperationException("+"Not supported yet."+")");
        //        throw new UnsupportedOperationException("Not supported yet.");

        logger.debug("getSlaContract(ApplicationInstance applicationInstance, PaaSInstance paaSInstance)");

        SlaContract slaContract = new SlaContract(){};

        logger.debug("retrieved SlaContract: "+ slaContract);

        logger.debug("return SlaContract: "+slaContract);

        return slaContract;
    }

    @Override
    public void updateSlaContract(SlaContract slaContract) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
