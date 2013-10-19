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


package eu.cloud4soa.relational.persistence;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eu.cloud4soa.relational.datamodel.Account;
import eu.cloud4soa.relational.datamodel.ApplicationInstance;
import eu.cloud4soa.relational.datamodel.GuaranteeTerm;
import eu.cloud4soa.relational.datamodel.ISLAEnforcementJob;
import eu.cloud4soa.relational.datamodel.Paas;
import eu.cloud4soa.relational.datamodel.RecoveryAction;
import eu.cloud4soa.relational.datamodel.SLAEnforcementJob;
import eu.cloud4soa.relational.datamodel.SLATemplate;
import eu.cloud4soa.relational.datamodel.SLAViolation;
import eu.cloud4soa.relational.datamodel.User;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;


@Repository
public class SLAViolationRepository extends AbstractHbmDao<SLAViolation> implements ISLAViolationRepository {
	
	@Autowired
	public SLAViolationRepository(SessionFactory sessionFactory) {
		super(sessionFactory, SLAViolation.class);
	}	

	public void store(SLAViolation slaViolation) {
		this.saveOrUpdate(slaViolation);
	}

	public void delete(SLAViolation slaViolation) {		
		super.delete(slaViolation);
	}

	public List<SLAViolation> retrieveAllSLAViolations() {
		return this.findAll();
	}

	public List<SLAViolation> retrieveAllSLAViolations(ISLAEnforcementJob slaEnforcementJob) {
		return this.findBy("slaEnforcementJobId", slaEnforcementJob.getId());
	}	

	public List<SLAViolation> retrieveAllInRange(ISLAEnforcementJob slaEnforcementJob, Date start, Date end){
		return this.find("slaEnforcementJobId=? AND date>=? AND date<=? ORDER BY date DESC ", slaEnforcementJob.getId(), start, end);
	}	
 
	public List<SLAViolation> retrieveAllInRangeLimited(ISLAEnforcementJob slaEnforcementJob, Date start, Date end, long limit){
		LinkedList<SLAViolation> resultSet = (LinkedList<SLAViolation>) retrieveAllInRange(slaEnforcementJob, start, end);
		// we have to use that because hibernates criteria handling is a bit buggy und suboptimal designed.
		while(resultSet.size()>limit){
			resultSet.pollLast();
		}
		return resultSet;
	}
	
	/**
	 * Returns all sla violations for a given PaaS provider following
	 * the following query:
	 * 
	 * SELECT slaviolation.id
	 * 	FROM slaviolation
	 * 	WHERE slaviolation.idenforcementjob IN (
	 * 		SELECT enforcementjob.id
	 * 		 FROM enforcementjob
	 * 		 JOIN slacontract ON enforcementjob.idslacontract=slacontract.id
	 * 		 WHERE slacontract.idprovider=XXX
	 * 	)
	 * 
	 * @param providerId the id of the PaaS provider
	 * @return a list of SLAViolation objects
	 */
	public List<SLAViolation> retrieveAllForProvider (String providerId) {
		DetachedCriteria slap_criteria = DetachedCriteria.forClass(SLATemplate.class)
				.add(Restrictions.eq("serviceProvider", providerId))
				.setProjection(Projections.property("id"));
		
		DetachedCriteria enf_criteria  = DetachedCriteria.forClass(SLAEnforcementJob.class)
				.add(Property.forName("slaContractId").in(slap_criteria))
				.setProjection(Projections.property("id"));
		
		return (List<SLAViolation>) getSession().createCriteria(SLAViolation.class)
				.add(Subqueries.geAll("slaEnforcementJobId", enf_criteria))
				.list();
	}
	
	/**
	 * Returns all sla violations for a given PaaS offering following
	 * the following query:
	 * 
	 * SELECT slaviolation.id
	 * 	FROM slaviolation
	 * 	WHERE slaviolation.idappinstance IN (
	 * 		SELECT idappinstance.id
	 * 		 FROM idappinstance
	 * 		 JOIN paas ON idappinstance.idpaas=paas.id
	 * 		 WHERE paas.id=XXX
	 * 	)
	 * 
	 * @param offeringId the id of the PaaS offering
	 * @return a list of SLAViolation objects
	 */
	public List<SLAViolation> retrieveAllForOffering (String offeringId) {
		DetachedCriteria pass_criteria = DetachedCriteria.forClass(Paas.class)
				.add(Restrictions.eq("url", offeringId));
		
		DetachedCriteria acc_criteria  = DetachedCriteria.forClass(Account.class)
				.add(Property.forName("paas").in(pass_criteria));
		
		DetachedCriteria app_criteria  = DetachedCriteria.forClass(ApplicationInstance.class)
				.add(Property.forName("account").in(acc_criteria))
				.setProjection(Projections.property("id"));
		
		return (List<SLAViolation>) getSession().createCriteria(SLAViolation.class)
				.add(Subqueries.geAll("applicationInstanceUriId", app_criteria))
				.list();
	}
	
	/**
	 * Returns all SLA violations for a given User between two moments in time.
	 * 
	 * @param userId the user id
	 * @param start  the moment we start to get the SLA violations
	 * @param end	 the moment we finish to get the SLA violations
	 * @return		 a list of SLA violations
	 */
	public List<SLAViolation> retrieveAllForUserAndTime (String userId,
														 Date   start,
														 Date   end) {
		DetachedCriteria user_criteria = DetachedCriteria.forClass(User.class)
				.add(Restrictions.eq("uriID", userId))
				.setProjection(Projections.property("id"));
		
		DetachedCriteria acc_criteria  = DetachedCriteria.forClass(Account.class)
				.add(Property.forName("user.id").in(user_criteria))
				.setProjection(Projections.property("id"));
		
		DetachedCriteria app_criteria  = DetachedCriteria.forClass(ApplicationInstance.class)
				.add(Property.forName("account.id").in(acc_criteria))
				.setProjection(Projections.property("uriID"));
		
		return (List<SLAViolation>) getSession().createCriteria(SLAViolation.class)
				.add(Property.forName("applicationInstanceUriId").in(app_criteria))
				.add(Restrictions.between("dateAndTime", start, end))
				.list();
	}
	
	public List<SLAViolation> retrieveAlForRecoveryAction(RecoveryAction recoveryAction) {
        List<SLAViolation> slaViolationList =	this.find("recoveryAction = ?", recoveryAction);
        return slaViolationList;
	}
}
