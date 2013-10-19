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
package eu.cloud4soa.governance.sla.enforcement.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










import eu.cloud4soa.api.datamodel.governance.SlaTemplate;
import eu.cloud4soa.api.datamodel.governance.SlaTemplate.ServiceGuaranteeType;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric;
import eu.cloud4soa.api.governance.monitoring.IMonitoringMetric.MetricKey;
import eu.cloud4soa.governance.monitoring.MonitoringModule;
import eu.cloud4soa.governance.sla.decisor.SLADecisor;
import eu.cloud4soa.relational.datamodel.ApplicationInstance;
import eu.cloud4soa.relational.datamodel.Breach;
import eu.cloud4soa.relational.datamodel.GuaranteeTerm;
import eu.cloud4soa.relational.datamodel.ISLAEnforcementJob;
import eu.cloud4soa.relational.datamodel.SLAContract;
import eu.cloud4soa.relational.datamodel.SLAPolicy;
import eu.cloud4soa.relational.datamodel.SLAViolation;
import eu.cloud4soa.relational.persistence.ApplicationInstanceRepository;
import eu.cloud4soa.relational.persistence.BreachRepository;
import eu.cloud4soa.relational.persistence.GuaranteeTermRepository;
import eu.cloud4soa.relational.persistence.RecoveryActionRepository;
import eu.cloud4soa.relational.persistence.SLAContractRepository;
import eu.cloud4soa.relational.persistence.SLAPolicyRepository;
import eu.cloud4soa.relational.persistence.SLAViolationRepository;


public class SLAEnforcementTask extends TimerTask implements Runnable{
    
    /*
     * This is a low value, but if metrics are taken each 30s, it will take a long to have 
     * 30 samples, for example.
     */
	private static final int MIN_UPTIME_SAMPLES = 10;

    final Logger logger = LoggerFactory.getLogger(getClass());

	private BreachRepository	           breach_repository;
	private SLAViolationRepository        slaViolationRepository;
	private RecoveryActionRepository      recoveryActionRepository;	
	private ApplicationInstanceRepository applicationInstanceRepository;
	private SLAContractRepository         slaContractRepository;
	private GuaranteeTermRepository       guaranteeTermRepository;
	private SLAPolicyRepository           slaPolicyRepository;
	private ISLAEnforcementJob 	       slaEnforcementJob;
	private MonitoringModule 	           monitoringModule;
	private SLAContract			       slaContract;
	private Date                          lastExecuted;
	
	private Date called;
	
	private HashMap<ServiceGuaranteeType, ArrayList <Long>> violations;
		
	/*TODO Temporal solution until agreement of what type to use*/
	/* The good */
	private ApplicationInstance persistedApplicationInstance;
	/* The bad and the ugly */
	private eu.cloud4soa.api.datamodel.core.ApplicationInstance apiApplicationInstance;
	
	/*
	 * applicationInstance  = this.applicationProfilesRepository.getApplicationInstance(applicationUriId);
	 * */
	
	
	public SLAEnforcementTask(SLAViolationRepository   slaViolationRepository,
							  RecoveryActionRepository recoveryActionRepository,
							  ApplicationInstanceRepository applicationInstanceRepository,
							  SLAContractRepository    slaContractRepository,
							  GuaranteeTermRepository  guaranteeTermRepository,
							  ISLAEnforcementJob       slaEnforcementJob,
							  MonitoringModule         monitoringModule,
							  Date                     lastExecuted,
							  BreachRepository         breachRepository,
							  SLAPolicyRepository      slaPolicyRepository){
		this.recoveryActionRepository      = recoveryActionRepository;
		this.slaEnforcementJob             = slaEnforcementJob;
		this.slaViolationRepository        = slaViolationRepository;
		this.applicationInstanceRepository = applicationInstanceRepository;
		this.slaContractRepository         = slaContractRepository;
		this.guaranteeTermRepository       = guaranteeTermRepository;
		this.monitoringModule              = monitoringModule;
		this.breach_repository             = breachRepository;
		this.violations				  =
				new HashMap<ServiceGuaranteeType, ArrayList<Long>>();
		
		/*TODO There could be more than one application instance per uri id?*/
		this.persistedApplicationInstance = applicationInstanceRepository.findByUriIdNoCheck(slaEnforcementJob.getApplicationInstanceUriId()).get(0);
		this.lastExecuted = lastExecuted;
		
		/*TODO Temporal solution until agreement of what type to use*/
		apiApplicationInstance = toApiApplicationInstance(persistedApplicationInstance);
	}

	@Override
	public void run() {
		called = new Date();
		slaContract = slaContractRepository.retrieveAll(Long.parseLong(slaEnforcementJob.getSlaContractId())).get(0);
		
		checkViolationsInPeriod(persistedApplicationInstance, slaContract, called);
	}
	
	private SLAViolation createandStoreSLAViolation(String metricName,
													 SLAPolicy slaPolicy,
													 float    expectedValue,
													 float  actualValue,
													 List <Breach> breaches) {
		SLAViolation violation = new SLAViolation(persistedApplicationInstance.getUriID(),
												  metricName,
												  expectedValue,
												  actualValue,
												  breaches);
		violation.setDateAndTime(called);
		violation.setSLAEnforcementJobId(slaEnforcementJob.getId());
		
		if (slaPolicy != null) {
			violation.setSlaPolicyId(slaPolicy.getId());
		}
		
		slaViolationRepository.store(violation);
		//TODO: switch print out with logger after development    
		//logger.debug("SLAEnforcementTask found: " + violation);
		System.out.println("SLAEnforcementTask found: " + violation);
		return violation;
	}
	
	private void logFindings (String metricName,int expectedValue, float actualValue)  {
		//TODO: switch print out with logger after development
		//logger.debug("SLAEnforcementTask stats for" + metricName + ": Expected " + expectedValue + " percent compliance, found " + actualValue + " percent compliance");
		System.out.println("SLAEnforcementTask stats for" + metricName +
						   ": Expected " + expectedValue +
						   " percent compliance, found " + actualValue +
						   " percent compliance");
	}
	
	private void checkViolationsInPeriod (ApplicationInstance applicationInstance,
			  							    SLAContract 		contract,
			  							    Date				end) {
		Date checkMetricsBeginning, checkBreachesBeginning;
		String applicationUriID = applicationInstance.getUriID();
		List<SLAPolicy> policies = contract.getSlaPolicies();
		List<Breach> breaches;
		List<IMonitoringMetric> metrics;
		Date now = new Date();
		int nBreaches;
		Breach newBreach;
		SLADecisor decisor = new SLADecisor
									(recoveryActionRepository,
									 slaViolationRepository,
									 contract,
									 applicationInstance,
									 slaPolicyRepository);
		SLAViolation violation;

		logger.debug("checkViolationsInPeriod in ");
		for (GuaranteeTerm guaranteeTerm : contract.getGuaranteeTerms()) {
            if (guaranteeTerm == null || 
                    guaranteeTerm.getCustomServiceLevel() == null) {
                continue;
            }
            double threshold = Double.parseDouble(guaranteeTerm.getCustomServiceLevel());
            String kpiName = guaranteeTerm.getKpiName();
            
            if (policies.isEmpty()) {
                logger.debug("No policies - About to read metrics");
				metrics =
					monitoringModule.getMonitoringMetricsWhithinRangeLimited
						(applicationUriID, IMonitoringMetric.MetricKey.valueOf(kpiName),
								lastExecuted, now, 1000);
				
                Iterator<IMonitoringMetric> it = getBreachesInMetrics(kpiName, metrics, threshold);
                while (it.hasNext()) {
                    IMonitoringMetric metric = it.next();
                    if (metric == null) {
                        continue;
                    }
				    double metricValue = metric.getMetricValue();
				        
				    /*
				     * W/o policies, consider each breach a violation 
				     */
					createandStoreSLAViolation(kpiName,
							null, Float.parseFloat(guaranteeTerm.getCustomServiceLevel()),
							(float)metricValue, null);
				}
			} else {
				for (SLAPolicy policy : contract.getSlaPolicies()) {
				    
					ServiceGuaranteeType metric_name = policy.getMetric_name();
                    if (kpiName.equals(metric_name.toString())){
						long policyInterval = policy.getTime_interval().getTime();
						checkBreachesBeginning = new Date(now.getTime() - policyInterval);
						logger.debug("With policies - About to read metrics");
						
						/*
						 * TODO: checkBreachesBeginning shoud be max(cBB, last(violation)).
						 * Ioc, we could be generating the same violations again and again until
						 * the breaches leave the time window.
						 */
						breaches =
							breach_repository.retrieveAllInRangeLimited
								(applicationUriID, policy.getId(),
									kpiName,
									checkBreachesBeginning, end);
						
                        nBreaches = breaches.size();
                        /*
                         * checkMetricsBeginning is max(now - interval, last(breach)).
                         * Ioc, we could be generating the same breaches again and again until
                         * the metrics leave the time window.
                         */                        
                        if (nBreaches > 0) {
                            checkMetricsBeginning = breaches.get(0).getTimestamp();
                        }
                        else {
                            checkMetricsBeginning = new Date(now.getTime() - policyInterval);
                        }
                        metrics =
                            monitoringModule.getMonitoringMetricsWhithinRangeLimited
                            (applicationUriID, IMonitoringMetric.MetricKey.valueOf(kpiName),
                                    checkMetricsBeginning, now, 1000);
                        

						Iterator<IMonitoringMetric> it = getBreachesInMetrics(kpiName, metrics, threshold);
						while (it.hasNext()) {
						    IMonitoringMetric metric = it.next();
						    if (metric == null) {
						        continue;
						    }
						    
							double metricValue = metric.getMetricValue();
								
							newBreach = storeBreach(metric.getDate(),
											policy.getId(),
											applicationUriID,
											null,
											metric_name.toString(),
											String.valueOf(metricValue));
							
							breaches.add(newBreach);
							
							if (++nBreaches > policy.getBreach()) {
								violation = createandStoreSLAViolation
									(kpiName, policy,
										Float.parseFloat(guaranteeTerm.getCustomServiceLevel()),
										(float)metricValue,
										breaches);
								
								/*
								 * TODO: slaPolicyRepository is null because is not constructed
								 * in spring yet. Uncomment this when solved.
								 */
//									decisor.createRecoveryAction(contract, violation);
								
								/*
								 * We only store one violation per task execution.
								 */
								break;
							}
						}
					}
				}
			}
		}
        logger.debug("checkViolationsInPeriod out");
	}
	
	private Breach storeBreach (Date      date,
								 Long slaPolicy,
							     String    applicationInstanceUriId,
							     String    user_id,
							     String    type,
							     String    value) {
		Breach breach = new Breach();
		
		breach.setTimestamp(date);
		breach.setSlaPolicyId(slaPolicy);
		breach.setApplicationInstanceUriId(applicationInstanceUriId);
		breach.setUser_id(user_id);
		breach.setType(type);
		breach.setValue(value);
		
		breach_repository.store(breach);
		
		return breach;
	}
	
	/*TODO Temporal solution until agreement of what type to use*/
	private eu.cloud4soa.api.datamodel.core.ApplicationInstance toApiApplicationInstance (ApplicationInstance appInstance) {
		eu.cloud4soa.api.datamodel.core.ApplicationInstance ret =
				new eu.cloud4soa.api.datamodel.core.ApplicationInstance();
		
		ret.setAdapterUrl(appInstance.getAdapterurl());
		ret.setApplicationDeploymentUriId(appInstance.getAppurl());		
		ret.setUriId(appInstance.getUriID());
		ret.setVersion(appInstance.getVersion());
		ret.setOwnerUriId(appInstance.getAccount().getUser().getUriID());
		
		return ret;
	}



	/**
     * Iterator over the uptime breaches in a list of metrics.
     * 
     * A list of metrics causes an uptime breach if percentage of values=200 if less than 
     * <code>minUptime</code>. There MUST be more or equal than <code>minSamples</code> samples.
     * 
     * To ease implementation, this iterator can return <code>null</code>,
     * so nulls have to be skipped in client call.
	 */
	private class UptimeCheckerIterator implements Iterator<IMonitoringMetric> {
	    
        private List<IMonitoringMetric> metrics;
        private ListIterator<IMonitoringMetric> it;
        private double minUptime;
        private int minSamples;

        public UptimeCheckerIterator(List<IMonitoringMetric> metrics, 
                double minUptime, int minSamples) {
            this.metrics = metrics;
            this.it = metrics.listIterator();
            this.minUptime = minUptime;
            this.minSamples = minSamples;
        }
        

        @Override
        public boolean hasNext() {

            return it.hasNext();
        }

        @Override
        public IMonitoringMetric next() {
            int isUp = 0;
            int size = 0;
            
            if (!it.hasNext()) {
                throw new NoSuchElementException();
            }
            IMonitoringMetric metric = null;
            while (it.hasNext()) {
                metric = it.next();
                if (metric.getMetricValue() == 200) {
                    isUp++;
                }
                size++;
            }
            
            final double uptime = isUp * 100.0 / size;
            if (size >= minSamples && uptime < minUptime) {
                
                /*
                 * Return onthefly IMonitoringMetric. Get date of last metric as metric date
                 * (metrics are date desc ordered)
                 */
                metric = metrics.get(0);
                final MetricKey metricKey = metric.getMetricKey();
                final Date metricDate = metric.getDate();

                IMonitoringMetric result = new IMonitoringMetric() {
                    
                    @Override
                    public MetricKey getMetricKey() {
                        return metricKey;
                    }

                    @Override
                    public double getMetricValue() {
                        return uptime; 
                    }
                    
                    @Override
                    public Date getDate() {
                        return metricDate;
                    }
                };
                return result;
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("CheckerIterator does not support remove");
        }
	}

	/**
	 * Iterator over the time breaches in a list of metrics.
	 * 
	 * A metric is a breach if its value is less than <code>maxTime</code>
	 * 
	 * To ease implementation, this iterator return <code>null</code> on any non-breach metric,
	 * so nulls have to be skipped in client call.
	 */
    private class TimeCheckerIterator implements Iterator<IMonitoringMetric> {
        private List<IMonitoringMetric> metrics;
        private ListIterator<IMonitoringMetric> it;
        private double maxTime;

        public TimeCheckerIterator(List<IMonitoringMetric> metrics, double maxTime) {
            this.metrics = metrics;
            this.it = metrics.listIterator();
            this.maxTime = maxTime;
        }


        @Override
        public boolean hasNext() {
            
            return it.hasNext();
        }

        @Override
        public IMonitoringMetric next() {

            IMonitoringMetric metric = it.next();
            if (metric.getMetricValue() > maxTime) {
                return metric;
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("CheckerIterator does not support remove");
        }
    }
    
    /**
     * Simple factory method to get checker given a metric name.
     */
    private Iterator<IMonitoringMetric> getBreachesInMetrics(String metricName, 
            List<IMonitoringMetric> metrics, double threshold) {
        
        /*
         * TODO: cpu_load and memory_load are returned as TimeCheckerIterator, and that's wrong.
         * CapacityCheckerIterator should be implemented if these parameters are needed.
         */
        if (IMonitoringMetric.MetricKey.statusCode.toString().equals(metricName)) {
            
            return new UptimeCheckerIterator(metrics, threshold, MIN_UPTIME_SAMPLES);
        }
        return new TimeCheckerIterator(metrics, threshold);
    }
}

