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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import eu.cloud4soa.api.datamodel.governance.SlaPolicy.SlaPenaltyType;

@Entity
@Table(name="RECOVERYACTION")
public class RecoveryAction extends AbstractModel<RecoveryAction> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8108959745410768955L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "application_instance", nullable = true)
	private ApplicationInstance application_instance;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sla_contract", nullable = true)
	private SLAContract sla_contract;
	
	@Column(name="date_and_time")
	private Date date_and_time;
	
	@OneToOne(targetEntity=SLAViolation.class)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name="sla_violation",referencedColumnName="id")
	private SLAViolation sla_violation;
	
	/*@Column(length = 64 , columnDefinition = "enum(MIGRATION,SOFT_VIOLATION,WARNING)")
    @Enumerated( javax.persistence.EnumType.STRING )
	private SlaPenaltyType action;*/
	
	@Column
	private String action;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public ApplicationInstance getApplication_instance() {
		return application_instance;
	}

	public void setApplication_instance(ApplicationInstance application_instance) {
		this.application_instance = application_instance;
	}

	public SLAContract getSla_contract() {
		return sla_contract;
	}

	public void setSla_contract(SLAContract sla_contract) {
		this.sla_contract = sla_contract;
	}

	public Date getDate_and_time() {
		return date_and_time;
	}

	public void setDate_and_time(Date date_and_time) {
		this.date_and_time = date_and_time;
	}

	public SLAViolation getSla_violation() {
		return sla_violation;
	}

	public void setSla_violation(SLAViolation sla_violations) {
		this.sla_violation = sla_violations;
	}

	public SlaPenaltyType getAction() {
		return SlaPenaltyType.valueOf(action);
	}

	public void setAction(SlaPenaltyType action) {
		this.action = action.toString();
	}
}
