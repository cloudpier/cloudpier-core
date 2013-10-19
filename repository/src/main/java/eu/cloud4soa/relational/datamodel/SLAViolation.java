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


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import eu.cloud4soa.api.governance.sla.enforcement.ISLAViolation;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="SLAVIOLATION")
public class SLAViolation extends AbstractModel<SLAViolation> implements ISLAViolation, Serializable {
		
	private static final long serialVersionUID = 8432423906513702299L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private long slaEnforcementJobId;
	
	@Column
	private String applicationInstanceUriId;
	
	@Column
    //@Temporal(javax.persistence.TemporalType.DATE)
	private Date dateAndTime;
	
	@Column
	private String metricName;
	
	@Column
	private float expectedValue;
	
	@Column
	private float actualValue;
	
	//TODO: not sure if arrays can be stored as a column in Hibernate. Putting a single value for now
	//TODO: for now hard-coding three recovery actions for each SLAViolation 
	@Column
	private int recoveryaction0 = ISLAViolation.RECOVERYACTION_DISCOUNT;

	@Column
	private int recoveryaction1 = ISLAViolation.RECOVERYACTION_MIGRATE;
	
	@Column
	private int recoveryaction2 = ISLAViolation.RECOVERYACTION_UNDEPLOY;

	@Column
	private long slaPolicyId;
	
	@Column
	private int status = ISLAViolation.STATUS_OPEN;
	
	@OneToOne(targetEntity=RecoveryAction.class)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name="recoveryAction",referencedColumnName="id")
	private RecoveryAction recoveryAction;
	
	@OneToMany(mappedBy="slaViolation")
	@Cascade(CascadeType.SAVE_UPDATE)
	private List <Breach> breaches;
	
	public SLAViolation() {
		super();
	}
	
	public SLAViolation(String applicationInstanceUriId,
			             String metricName,
			             float expectedValue,
			             float actualValue,
			             List <Breach> breaches) {
		this.dateAndTime              = new Date();
		this.applicationInstanceUriId = applicationInstanceUriId;
		this.metricName               = metricName;
		this.expectedValue            = expectedValue;
		this.actualValue              = actualValue;
		this.breaches                 = breaches;
	}
	
	public SLAViolation(String         applicationInstanceUriId,
						String         metricName,
						/*float          expectedValue,
						float          actualValue,*/
						List<Breach>   breaches) {
		this.dateAndTime = new Date();
		this.applicationInstanceUriId = applicationInstanceUriId;
		this.metricName = metricName;
		/*this.expectedValue = expectedValue;
		this.actualValue = actualValue;*/
		this.breaches = breaches;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	public long getSLAEnforcementJobId() {
		return slaEnforcementJobId;
	}

	public void setSLAEnforcementJobId(long slaEnforcementJobId) {
		this.slaEnforcementJobId = slaEnforcementJobId;
	}
	
	@Override
	public String getMetricName() {
		return metricName;
	}

	@Override
	public float getExpectedValue() {
		return expectedValue;
	}

	@Override
	public float getActualValue() {
		return actualValue;
	}

	@Override
	public String toString() {
		return new String("SLA Violation [id=" + id 
				+ ", applicationInstanceUriId=" + applicationInstanceUriId 
				+ ", status=" + status 
				+  ", recoveryactions= {" + recoveryaction0 + "," + recoveryaction1 + "," + recoveryaction2 
				+ "}, timestamp=" + dateAndTime
				+", SLA Violation for " + metricName + ": Expected " + expectedValue + " percent compliance, found " + actualValue + " percent compliance]");
	}

	@Override
	public String getApplicationInstanceUriId() {
		return applicationInstanceUriId;
	}

	@Override
	public Date getDateAndTime() {
		return dateAndTime;
	}
	
	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	@Override
	public int[] getRecoveryActions() {
		return new int [] {recoveryaction0, recoveryaction1, recoveryaction2};
	}

	@Override
	public int getStatus() {
		return status;
	}
	
	public RecoveryAction getRecoveryAction() {
		return recoveryAction;
	}
	
	public void setRecoveryAction(RecoveryAction recoveryAction) {
		this.recoveryAction = recoveryAction;
	}

	public long getSlaPolicyId() {
		return slaPolicyId;
	}

	public void setSlaPolicyId(long slaPolicyId) {
		this.slaPolicyId = slaPolicyId;
	}
}
