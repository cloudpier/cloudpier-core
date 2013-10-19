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

package eu.cloud4soa.api.soa;

import eu.cloud4soa.api.datamodel.core.MatchingPlatform;
import eu.cloud4soa.api.datamodel.frontend.PaaSInstance;
import eu.cloud4soa.api.datamodel.frontend.PaaSRating;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.repository.FiveStarsRate;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import eu.cloud4soa.api.util.exception.soa.SOAException;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

/**
 *
 * @author vincenzo
 */
public interface PaaSOfferingRecommendation {
//    void ratePaaSProvider(ui.PaaSInstance, ui.PaaSRating);
  
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/storeUserExperienceRate")
    public void storeUserExperienceRate(
            @Multipart(value = "appURI", type = "text/plain")String appURI, 
            @Multipart(value = "rate", type = "application/json")FiveStarsRate rate)throws SOAException;
        
    @POST
    @Consumes("text/plain")
    @Path("/deleteUserExperienceRate")
    public void deleteUserExperienceRate(String appURI)throws SOAException;
    
         
    @POST
    @Produces({"application/xml","application/json"})
    @Consumes("text/plain")
    @Path("/getUserExperienceRate")
    public FiveStarsRate getUserExperienceRate(String appURI)throws SOAException;
    
    
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/updateUserExperienceRate")
    public void updateUserExperienceRate(
            @Multipart(value = "appURI", type = "text/plain")String appURI, 
            @Multipart(value = "rate", type = "text/plain")String rate)throws SOAException;
      
    @POST
    @Produces({"application/xml","application/json"})
    @Consumes("text/plain")
    @Path("/getPaaSUserExperienceEvaluation")
    public List<FiveStarsRate> getPaaSUserExperienceEvaluation(String paasURI)throws SOAException;
    
     
    @POST
    @Produces({"application/xml","application/json"})
    @Consumes("text/plain")
    @Path("/getAveragePaaSUserExperienceRate")
    public float getAveragePaaSUserExperienceRate(String paasURI)throws SOAException;
    
    @POST
    @Produces({"application/xml","application/json"})
    @Consumes("text/plain")
    @Path("/getPaaSBreachesPerWeek")
    public float getPaaSBreachesPerWeek(String paasURI)throws SOAException,RepositoryException;
    
    @POST
    @Produces({"application/xml","application/json"})
    @Consumes("text/plain")
    @Path("/getPaaSBreachesPerMonth")
    public float getPaaSBreachesPerMonth(String paasURI)throws SOAException,RepositoryException;
    
    @POST
    @Produces({"application/xml","application/json"})
    @Consumes("text/plain")
    @Path("/getPaaSBreachesPerDay")
    public float getPaaSBreachesPerDay(String paasURI)throws SOAException,RepositoryException;
    
    
    /*
    
   TODO          
    public List<FiveStarsRate> getPaasUserExperienceEvaluation( String paasURI) throws SOAException;
    
 */
    
    
    
 //   void ratePaaSProvider(PaaSInstance paaSInstance, PaaSRating paaSRating);
 //   MatchingPlatform getRecommendation(MatchingPlatform matchingPlatform, ApplicationInstance applicationInstance);
}
