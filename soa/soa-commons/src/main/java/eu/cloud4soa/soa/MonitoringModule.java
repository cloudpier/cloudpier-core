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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.qos.ServiceQualityInstance;
import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric.MetricKey;
import eu.cloud4soa.api.governance.monitoring.IMonitoringStatistic;
import eu.cloud4soa.api.util.exception.soa.SOAException;

/**
 * This class implements all the methods of the interface eu.cloud4soa.api.soa.MonitoringModule.
 * 
 * 
 * @author frarav
 */
@Transactional
public class MonitoringModule implements eu.cloud4soa.api.soa.MonitoringModule {
    
    private     final Logger            logger         = LoggerFactory.getLogger( MonitoringModule.class );
    protected   final SimpleDateFormat  dateFormatter  = new SimpleDateFormat("dd-MM-yyyy");
    
    protected eu.cloud4soa.api.governance.MonitoringModule      governanceMonitoringModule;
    protected eu.cloud4soa.api.soa.AnnouncementModule           announcementModule;

    
    @Required
    public void setGovernanceMonitoringModule(
            eu.cloud4soa.governance.monitoring.MonitoringModule governanceMonitoringModule) {
        
        this.governanceMonitoringModule = governanceMonitoringModule;
    
    }
    
    
    @Required
    public void setAnnouncementModule(
            eu.cloud4soa.api.soa.AnnouncementModule announcementModule) {
        
        this.announcementModule = announcementModule;
    
    }

    
    @Override
    public IMonitoringJob getMonitoringJob(String applicationUriId) throws SOAException {
        
       IMonitoringJob       resultingMonitoringJob;
        
       logger.debug( "Requested getMonitoringJob( " + applicationUriId +" )");
       logger.debug("ApplicationUriId = "         + applicationUriId);
        
       resultingMonitoringJob = this.governanceMonitoringModule.getMonitoringJob(applicationUriId);
       
       logger.debug( "ApplicationRetrieved = "      + applicationUriId +
                     " retrieved iMonitoringJob = " + resultingMonitoringJob      );
        
       return resultingMonitoringJob;
    
    }

    

    
    
    @Override
    public List<IMonitoringStatistic> getMonitoringStatistics(String applicationUriId) throws SOAException {
       
       List<IMonitoringStatistic>  resultingMonitoringStatistic;
        
       logger.debug( "Requested getMonitoringStatistics( " + applicationUriId +" )");

       logger.debug("ApplicationUriId = "         + applicationUriId);
        
       resultingMonitoringStatistic = this.governanceMonitoringModule.getMonitoringStatistics(applicationUriId);
        
       logger.debug( "ApplicationRetrieved = "              + applicationUriId +
                     " retrieved iMonitoringStatistics = "  + resultingMonitoringStatistic      );
        
        return resultingMonitoringStatistic;
        
    }

    
    
    @Override
    public List<IMonitoringStatistic> getMonitoringStatisticsWhithinRange(String applicationUriId, Date start, Date end) throws SOAException {
        
        List<IMonitoringStatistic>  resultingMonitoringStatistic;
        
        logger.debug(   "Requested getMonitoringStatistics( " + applicationUriId  +
                        ", " + dateFormatter.format(start) + ", " + dateFormatter.format(end) + " )" );

        logger.debug("ApplicationUriId = "         + applicationUriId );

        resultingMonitoringStatistic = this.governanceMonitoringModule.getMonitoringStatisticsWhithinRange( applicationUriId, start, end );
        
        logger.debug( "Retrieved iMonitoringJob for application " + applicationUriId +
                " = " + resultingMonitoringStatistic);
        
        return resultingMonitoringStatistic;
    }

    
    
    @Override
    public List<IMonitoringStatistic> getMonitoringStatisticsWhithinRangeLimited(String applicationUriId, Date start, Date end, int maxResults) throws SOAException {
        
        ApplicationInstance  applicationInstance;
        List<IMonitoringStatistic>  resultingMonitoringStatistic;
        
        
        logger.debug(   "Requested getMonitoringStatistics( " + applicationUriId  +
                        ", " + dateFormatter.format(start) + ", " + dateFormatter.format(end) +
                        ", " + maxResults + " )" );

       logger.debug("ApplicationUriId = "         + applicationUriId);
       
        resultingMonitoringStatistic = this.governanceMonitoringModule.getMonitoringStatisticsWhithinRangeLimited( 
                applicationUriId, start, end, maxResults );
        
        logger.debug(   "Retrieved iMonitoringJob for application " + applicationUriId +
                        " = " + resultingMonitoringStatistic);
        
        return resultingMonitoringStatistic;
    }
    
    @Override
    public List<IMonitoringMetric> getMonitoringMetricsWhithinRangeLimited(String applicationUriId, MetricKey metricKey, Date start, Date end, int maxResults) throws SOAException {
        
        ApplicationInstance  applicationInstance;
        List<IMonitoringMetric>  resultingMonitoringMetric;
        
        
        logger.debug(   "Requested getMonitoringStatisticsWhithinRangeLimited( " + applicationUriId + 
                        ", " + metricKey.name() + ", " + dateFormatter.format(start) + ", " + dateFormatter.format(end) +
                        ", " + maxResults + " )" );

        logger.debug("ApplicationUriId = "         + applicationUriId);
        logger.debug("metricKey = "         + metricKey.name());
       
        resultingMonitoringMetric = this.governanceMonitoringModule.getMonitoringMetricsWhithinRangeLimited( 
                applicationUriId, metricKey, start, end, maxResults );
        
        logger.debug(   "Retrieved iMonitoringJob for application " + applicationUriId +
                        " = " + resultingMonitoringMetric);
        
        return resultingMonitoringMetric;
    }


    
    
    @Override
    public void startMonitoring(String applicationUriId) throws SOAException {
        
       logger.debug( "Requested startMonitoring( " + applicationUriId + " )");

       logger.debug("ApplicationUriId = "         + applicationUriId);
       
       this.governanceMonitoringModule.startMonitoring(applicationUriId);
        
       logger.debug( "Monitoring started on application " + applicationUriId );
        
    }

    
    
    @Override
    public void stopMonitoring(String applicationUriId) throws SOAException {
         
        logger.debug( "Requested stopMonitoring( " + applicationUriId + " )");

        logger.debug("ApplicationUriId = "         + applicationUriId);

        this.governanceMonitoringModule.stopMonitoring(applicationUriId);

        logger.debug( "Monitoring stopped on application " + applicationUriId );
        
    }
    
    
    
    @Override
	public List<ServiceQualityInstance> getSupportedMetrics( String paasOfferingId ) throws SOAException {
        
        List<ServiceQualityInstance>    supportedMetrics;
        PaaSInstance                    paaSInstance;
        
        supportedMetrics    = new ArrayList<ServiceQualityInstance>();
        paaSInstance        = this.announcementModule.getPaaSInstance( paasOfferingId );
        if ( paaSInstance == null) {
            throw new SOAException( Status.NOT_FOUND, "Not found PaaS instance corresponding to the id " + paasOfferingId);
        } else {
            supportedMetrics    = paaSInstance.getSupportedMetrics();
        }
        
        
        return supportedMetrics;
    }
    
    
    
}
