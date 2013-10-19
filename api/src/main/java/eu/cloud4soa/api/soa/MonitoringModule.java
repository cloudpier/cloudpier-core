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

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import eu.cloud4soa.api.datamodel.core.qos.ServiceQualityInstance;
import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric.MetricKey;
import eu.cloud4soa.api.governance.monitoring.IMonitoringStatistic;
import eu.cloud4soa.api.util.exception.soa.SOAException;

/**
 * 
 * This interface exposes the methods of the MonitoringModule: it contains all 
 * the methods needed to retrieve data collected by the monitoring.
 * 
 * @author frarav
 */
public interface MonitoringModule {
    
    
    
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/getMonitoringJob")
    public IMonitoringJob getMonitoringJob( String applicationUriId ) throws SOAException;
    
    
    
    @POST
    @Consumes("text/plain")
    @Produces({"application/xml","application/json"})
    @Path("/getMonitoringStatistics")
    public List<IMonitoringStatistic> getMonitoringStatistics(String applicationUriId) throws SOAException;
    
    
    
    @POST
    @Produces({"application/xml","application/json"})
    @Path("/getMonitoringStatistics")
    public List<IMonitoringStatistic> getMonitoringStatisticsWhithinRange(
            @Multipart(value = "applicationUriId", type = "text/plain")         String  applicationUriId,
            @Multipart(value = "applicationUriId", type = "application/json")   Date    start, 
            @Multipart(value = "applicationUriId", type = "application/json")   Date    end
        )throws SOAException;
    
    
    
    @POST
    @Produces({"application/xml","application/json"})
    @Path("/getMonitoringStatistics")
    public List<IMonitoringStatistic> getMonitoringStatisticsWhithinRangeLimited(
            @Multipart(value = "applicationUriId", type = "text/plain")         String  applicationUriId,
            @Multipart(value = "applicationUriId", type = "application/json")   Date    start, 
            @Multipart(value = "applicationUriId", type = "application/json")   Date    end,
            @Multipart(value = "applicationUriId", type = "application/json")   int     maxResults        
        )throws SOAException;
    
    @POST
    @Produces({"application/xml","application/json"})
    @Path("/getMonitoringStatistics")
    public List<IMonitoringMetric> getMonitoringMetricsWhithinRangeLimited(
            @Multipart(value = "applicationUriId", type = "text/plain")         String      applicationUriId,
            @Multipart(value = "applicationUriId", type = "text/plain")         MetricKey   metricKey,
            @Multipart(value = "applicationUriId", type = "application/json")   Date        start, 
            @Multipart(value = "applicationUriId", type = "application/json")   Date        end,
            @Multipart(value = "applicationUriId", type = "application/json")   int         maxResults   
    	) throws SOAException;
    
    @POST
    @Consumes("text/plain")
    @Path("/startMonitoring")
    public void startMonitoring(String applicationUriId) throws SOAException;
    
    
    
    @POST
    @Consumes("text/plain")
    @Path("/stopMonitoring")
    public void stopMonitoring(String applicationUriId) throws SOAException;
    
    
    
    public List<ServiceQualityInstance> getSupportedMetrics( String paasOfferingId ) throws SOAException;
    
    
}
