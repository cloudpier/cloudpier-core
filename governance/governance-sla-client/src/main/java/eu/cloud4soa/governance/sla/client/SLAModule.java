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

package eu.cloud4soa.governance.sla.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.governance.Breach;
import eu.cloud4soa.api.datamodel.governance.GuaranteeTerm;
import eu.cloud4soa.api.datamodel.governance.SlaPolicy;
import eu.cloud4soa.api.datamodel.governance.SLAViolation;
import eu.cloud4soa.api.datamodel.governance.ServiceDescriptionTerm;
import eu.cloud4soa.api.datamodel.governance.SlaContract;
import eu.cloud4soa.api.datamodel.governance.SlaContractValidity;
import eu.cloud4soa.api.datamodel.governance.SlaTemplate;
import eu.cloud4soa.api.datamodel.governance.SlaTemplate.ServiceGuaranteeType;
import eu.cloud4soa.api.repository.SLAsRepository;
import eu.cloud4soa.relational.datamodel.SLAContract;
import eu.cloud4soa.relational.persistence.BreachRepository;
import eu.cloud4soa.relational.persistence.SLAContractRepository;
import eu.cloud4soa.relational.persistence.SLATemplateRepository;
import eu.cloud4soa.relational.persistence.SLAViolationRepository;

/**
 *
 * @author vincenzo
 */
@Transactional
public class SLAModule implements eu.cloud4soa.api.governance.SLAModule{
    final Logger logger = LoggerFactory.getLogger(SLAModule.class);
    
    private SLAsRepository sLAsRepository;
    
    @Autowired
    private SLATemplateRepository sla_template_repository;
    
    @Autowired
    private SLAContractRepository sla_contract_repository;
    
    @Autowired
    private SLAViolationRepository sla_violation_repository;

    @Autowired
    private BreachRepository breach_repository;
    
    /**
     * @param slasRepository the slasRepository to set
     */
    public void setSLAsRepository(SLAsRepository sLAsRepository) {
        this.sLAsRepository = sLAsRepository;
    }

    @Override
    public SlaTemplate startNegotiation(ApplicationInstance applicationInstance,
    									PaaSInstance		paaSInstance) {
    	SlaTemplate ret = new SlaTemplate(applicationInstance, paaSInstance);
    	
    	ret.setId(storeSLATemplate(ret));
    	    	
    	return ret;
    }
    

    @Override
    public SlaContractValidity checkContractValidity(ApplicationInstance applicationInstance, PaaSInstance paaSInstance) {
        logger.error("UnsupportedOperationException("+"Not supported yet."+")");
        //        throw new UnsupportedOperationException("Not supported yet.");

        logger.debug("getApplicationSemanticModel()");

        //build ApplicationInstance from ApplicationSemanticModel + applicationInstance
        eu.cloud4soa.api.datamodel.governance.ApplicationInstance governanceApplicationInstance = new eu.cloud4soa.api.datamodel.governance.ApplicationInstance() {};
        //build PaaSInstance from PaaSSemanticModel + paaSInstance
        eu.cloud4soa.api.datamodel.governance.PaaSInstance governancePaaSInstance = new eu.cloud4soa.api.datamodel.governance.PaaSInstance() {};

        logger.debug("call SLAsRepository.getSlaContract(governanceApplicationInstance, governancePaaSInstance)");
        if(sLAsRepository==null)
            System.out.println ("slasRepository==null");
        SlaContract slaContract = sLAsRepository.getSlaContract(governanceApplicationInstance, governancePaaSInstance);

        logger.debug("obtained SlaContract: "+slaContract);

        //create validity informations
        SlaContractValidity slaContractValidity = new eu.cloud4soa.governance.datamodel.sla.SlaContractValidity();

        logger.debug("created SlaContractValidity: "+ slaContractValidity);

        logger.debug("return SlaContractValidity "+slaContractValidity);

        return slaContractValidity;
    }

    /*method exposed only for the DEMO*/
    //changing the signature for now, will complete the implementation later
    @Override
	public SlaTemplate getSLATemplate(String 			  templateId,
    								  ApplicationInstance applicationInstance,
    								  PaaSInstance 		  paaSInstance) {
    	return new SlaTemplate(applicationInstance, paaSInstance);    	
    }
    
    @Override
	public SlaContract getSLAContract(String sla_contract_id) {
    	SlaContract ret_contract = null;
    	SLAContract contract;
    	List <SLAContract> contract_list =
    			sla_contract_repository.retrieveAll(Long.valueOf(sla_contract_id));
    	
    	ArrayList <GuaranteeTerm> 		   guarantee_term_list;
    	ArrayList <ServiceDescriptionTerm> service_description_term_list;
    	ArrayList <SlaPolicy> 			   penalty_list;
    	
    	if (contract_list.size() == 1) {
    		ret_contract = new SlaContract();
    		contract = contract_list.get(0);
    		
    		guarantee_term_list 		  = new ArrayList<GuaranteeTerm>();
    		service_description_term_list = new ArrayList<ServiceDescriptionTerm>();
    		penalty_list				  = new ArrayList<SlaPolicy>();
    		
    		for (eu.cloud4soa.relational.datamodel.GuaranteeTerm term :
    										contract.getGuaranteeTerms()) {
    		    if (term == null) {
    		        continue;
    		    }
    			guarantee_term_list.add(new GuaranteeTerm(term.getGuaranteeTermName(),
    													  term.getServiceScopeServiceName(),
    													  term.getKpiName(),
    													  term.getCustomServiceLevel(),
    													  term.getPenaltyAssessmentInterval(),
    													  term.getPenaltyValueUnit(),
    													  term.getPenaltyValueExpression(),
    													  term.getRewardAssessmentInterval(),
    													  term.getRewardValueUnit(),
    													  term.getRewardValueExpression()));
    		}
    		
    		for (eu.cloud4soa.relational.datamodel.ServiceDescriptionTerm term :
    								contract.getServiceDescriptionTerms()) {
    		    if (term == null) {
    		        continue;
    		    }
    			service_description_term_list.add(new ServiceDescriptionTerm(
    					term.getServiceDescriptionTermName(),
    					term.getServiceDescriptionServiceName(),
    					term.getApplicationName(),
    					term.getApplicationVersion(),
    					term.getApplicationDescription()));
    		}
    		
    		for (eu.cloud4soa.relational.datamodel.SLAPolicy penalty :
    											contract.getSlaPolicies()) {
    			penalty_list.add(new SlaPolicy(
    					penalty.getId(),
    					penalty.getMetric_name().toString(),
    					penalty.getTime_interval(),
    					penalty.getBreach(),
    					penalty.getValue_expr()));
    		}
    		
    		ret_contract.setAgreementInitiator(contract.getAgreementInitiator());
    		ret_contract.setAgreementResponder(contract.getAgreementResponder());
    		ret_contract.setExpirationTime(contract.getExpirationTime().toString()); /*FORMAT!!!!!*/
    		ret_contract.setGuaranteeTerms(guarantee_term_list);
    		ret_contract.setServiceDescriptionTerms(service_description_term_list);
    		ret_contract.setServiceProvider(contract.getServiceProvider());
    		ret_contract.setContractId(contract.getId());
    		ret_contract.setTemplateName(contract.getTemplateName());
    	}
    	
    	return ret_contract;
    }
    
    private Long storeSLATemplate (SlaTemplate template) {
    	eu.cloud4soa.relational.datamodel.SLATemplate persisted_template =
    			toPersistedSLATemplate(template);
    	
    	sla_template_repository.store(persisted_template);
    	
    	return persisted_template.getId();
    }
    
    private eu.cloud4soa.relational.datamodel.SLATemplate toPersistedSLATemplate (SlaTemplate template) {
    	SimpleDateFormat format = new SimpleDateFormat(template.getDateFormat());

    	/*
    	 *	We create the classes to do the conversion
    	 *  between the SOA POJO to the Hibernate one
    	 */
    	eu.cloud4soa.relational.datamodel.GuaranteeTerm          persisted_g_term;
    	eu.cloud4soa.relational.datamodel.ServiceDescriptionTerm persisted_sd_term ;
    	
    	List <eu.cloud4soa.relational.datamodel.GuaranteeTerm> persisted_g_terms =
    			new ArrayList <eu.cloud4soa.relational.datamodel.GuaranteeTerm>();
    	List <eu.cloud4soa.relational.datamodel.ServiceDescriptionTerm> persisted_sd_terms =
    			new ArrayList <eu.cloud4soa.relational.datamodel.ServiceDescriptionTerm>();
    	
    	eu.cloud4soa.relational.datamodel.SLATemplate   persisted_template =
    			new eu.cloud4soa.relational.datamodel.SLATemplate();
    	
    	/*
    	 *	We do the conversion between POJOS
    	 */
    	for (GuaranteeTerm term : template.getGuaranteeTerms()) {
    		persisted_g_term = new eu.cloud4soa.relational.datamodel.GuaranteeTerm();
    		
    		persisted_g_term.setCustomServiceLevel(term.getCustomServiceLevel());
    		persisted_g_term.setGuaranteeTermName(term.getGuaranteeTermName());
    		persisted_g_term.setKpiName(term.getKpiName());
    		persisted_g_term.setPenaltyAssessmentInterval(term.getPenaltyAssessmentInterval());
    		persisted_g_term.setPenaltyValueExpression(term.getPenaltyValueExpression());
    		persisted_g_term.setPenaltyValueUnit(term.getPenaltyValueUnit());
    		persisted_g_term.setRewardAssessmentInterval(term.getRewardAssessmentInterval());
    		persisted_g_term.setRewardValueExpression(term.getRewardValueExpression());
    		persisted_g_term.setRewardValueUnit(term.getRewardValueUnit());
    		persisted_g_term.setServiceScopeServiceName(term.getServiceScopeServiceName());
    		
    		persisted_g_terms.add(persisted_g_term);
    	}
    	
    	for (ServiceDescriptionTerm term : template.getServiceDescriptionTerms()) {
    		persisted_sd_term = new eu.cloud4soa.relational.datamodel.ServiceDescriptionTerm();
    		
    		persisted_sd_term.setApplicationDescription(term.getApplicationDescription());
    		persisted_sd_term.setApplicationName(term.getApplicationName());
    		persisted_sd_term.setApplicationVersion(term.getApplicationVersion());
    		persisted_sd_term.setServiceDescriptionServiceName(term.getServiceDescriptionServiceName());
    		persisted_sd_term.setServiceDescriptionTermName(term.getServiceDescriptionTermName());
    		
    		persisted_sd_terms.add(persisted_sd_term);
    	}
    	
    	persisted_template.setAgreementInitiator(template.getAgreementInitiator());
    	persisted_template.setAgreementResponder(template.getAgreementResponder());
    	persisted_template.setGuaranteeTerms(persisted_g_terms);
    	persisted_template.setServiceDescriptionTerms(persisted_sd_terms);
    	persisted_template.setTemplateName(template.getTemplateName());
    	
    	try {
			persisted_template.setExpirationTime(new Date(format.parse(template.getExpirationTime()).getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return persisted_template;
    }
    
    @Override
    public List<SLAViolation> getSlaViolations (String user_id,
    											Date   start,
    											Date   end) {
    	List <eu.cloud4soa.relational.datamodel.SLAViolation> violations;
    	List <SLAViolation> ret = new ArrayList<SLAViolation>();
    	SLAViolation violation;
    	
    	violations = sla_violation_repository.retrieveAllForUserAndTime(user_id,
    																	start,
    																	end);
    	
    	for (eu.cloud4soa.relational.datamodel.SLAViolation viol : violations) {
    		violation = new SLAViolation();
    		violation.setId(viol.getId());
    		violation.setSla_enforcement_job_id(viol.getSLAEnforcementJobId());
    		violation.setApplication_instance_uri_id(viol.getApplicationInstanceUriId());
    		violation.setDate_and_time(viol.getDateAndTime());
    		violation.setMetric_name(viol.getMetricName());
    		violation.setExpected_value(viol.getExpectedValue());
    		violation.setActual_value(viol.getActualValue());
    		
    		ret.add(violation);
    	}
    	
    	return ret;
    }
    
    @Override
    public List<Breach> getBreaches (String user_id,
									 Date   start,
									 Date   end) {
		List <eu.cloud4soa.relational.datamodel.Breach> breaches;
		List <Breach> ret = new ArrayList<Breach>();
		Breach breach;
		
		breaches = breach_repository.retrieveAllInRange(user_id, start, end);
		
		for (eu.cloud4soa.relational.datamodel.Breach b : breaches) {
			breach = new Breach();
			breach.setId(b.getId());
			breach.setTimestamp(b.getTimestamp());
			breach.setMetric_name(ServiceGuaranteeType.valueOf(b.getMetric_name()));
			breach.setValue_expr(b.getValue());
		}
		
		return ret;
	}
}
