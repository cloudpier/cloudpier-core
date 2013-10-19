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


package eu.cloud4soa.relational.datamodel;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.IndexColumn;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name="SLACONTRACT")
public class SLAContract extends AbstractModel<SLAContract> implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2525975683561229893L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
	private Long id;
	
	@Column
	private String agreementInitiator;
	
	@Column
	private String agreementResponder;
	
	@Column
	private String serviceProvider;
	
	@Column
	private Date expirationTime;
	
	@Column
	private String templateName;
	
	@OneToMany(mappedBy="slaContract", fetch = FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
    private List<ServiceDescriptionTerm> serviceDescriptionTerms;

	@OneToMany(mappedBy="slaContract", fetch = FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
	@IndexColumn(name="id")
    private List<GuaranteeTerm> guaranteeTerms;
	
	@OneToMany(mappedBy="slaContract", fetch = FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
    private List<SLAPolicy> slaPolicies;
	
	@OneToMany(mappedBy="sla_contract", fetch = FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
    private List<RecoveryAction> recoveryActions;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;		
	}

	public String getAgreementInitiator() {
		return agreementInitiator;
	}

	public void setAgreementInitiator(String agreementInitiator) {
		this.agreementInitiator = agreementInitiator;
	}

	public String getAgreementResponder() {
		return agreementResponder;
	}

	public void setAgreementResponder(String agreementResponder) {
		this.agreementResponder = agreementResponder;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<ServiceDescriptionTerm> getServiceDescriptionTerms() {
		return serviceDescriptionTerms;
	}

	public void setServiceDescriptionTerms(
			List<ServiceDescriptionTerm> serviceDescriptionTerms) {
		for (ServiceDescriptionTerm serviceDescriptionTerm : serviceDescriptionTerms) {
			serviceDescriptionTerm.setSLAContract(this);
		}
		
		this.serviceDescriptionTerms = serviceDescriptionTerms;
	}

	public List<GuaranteeTerm> getGuaranteeTerms() {
		return guaranteeTerms;
	}

	public void setGuaranteeTerms(List<GuaranteeTerm> guaranteeTerms) {
		for (GuaranteeTerm guaranteeTerm : guaranteeTerms) {
			guaranteeTerm.setSLAContract(this);
		}
		
		this.guaranteeTerms = guaranteeTerms;
	}
	
	public List<SLAPolicy> getSlaPolicies() {
		return slaPolicies;
	}

	public void setSlaPolicies(List<SLAPolicy> slaPolicies) {
		for (SLAPolicy slaPolicy : slaPolicies) {
			slaPolicy.setSLAContract(this);
		}
		
		this.slaPolicies = slaPolicies;
	}

	public List<RecoveryAction> getRecoveryActions() {
		return recoveryActions;
	}

	public void setRecoveryActions(List<RecoveryAction> recoveryActions) {
		for (RecoveryAction recoveryAction : recoveryActions) {
			recoveryAction.setSla_contract(this);
		}
		
		this.recoveryActions = recoveryActions;
	}
	
	public void addRecoveryAction(RecoveryAction recoveryAction) {
		this.recoveryActions.add(recoveryAction);
	}
}
