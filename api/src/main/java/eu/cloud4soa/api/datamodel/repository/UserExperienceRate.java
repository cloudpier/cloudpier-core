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
package eu.cloud4soa.api.datamodel.repository;

import eu.cloud4soa.api.util.exception.soa.SOAException;
import java.util.List;

/**
 *
 * @author frarav
 */
public interface UserExperienceRate {
    
    
    public void storeUserExperienceRate( String appURI, FiveStarsRate rate) throws SOAException;
    
    public void deleteUserExperienceRate( String appURI)  throws SOAException;
    
    public void updateUserExperienceRate( String appURI, int rate) throws SOAException;
    
    public FiveStarsRate getUserExperienceRate( String appURI ) throws SOAException;
    
    public List<FiveStarsRate> getPaasUserExperienceEvaluation( String paasURI) throws SOAException;
    
    
}
