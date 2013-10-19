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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GuaranteeTerm extends AbstractModel<GuaranteeTerm> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7902277475235275905L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	/*
	 * ServiceLevelObjective 
	 */	
	@Column
	private String guaranteeTermName;
	@Column
	private String serviceScopeServiceName;
	@Column
	private String kpiName;
	@Column
	private String customServiceLevel;
	
	/*
	 * BusinessValueList - Penalty
	 */
	@Column
	private String penaltyAssessmentInterval;
	@Column
	private String penaltyValueUnit;
	@Column
	private String penaltyValueExpression;
 
	
	/*
	 * BusinessValueList - Reward
	 */
	@Column
	private String rewardAssessmentInterval;
	@Column
	private String rewardValueUnit;
	@Column
	private String rewardValueExpression;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slaTemplate", nullable = true)
	private SLATemplate slaTemplate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slaContract", nullable = true)
	private SLAContract slaContract;
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getGuaranteeTermName() {
		return guaranteeTermName;
	}
	
	public void setGuaranteeTermName(String guaranteeTermName) {
		this.guaranteeTermName = guaranteeTermName;
	}
	
	public String getServiceScopeServiceName() {
		return serviceScopeServiceName;
	}
	
	public void setServiceScopeServiceName(String serviceScopeServiceName) {
		this.serviceScopeServiceName = serviceScopeServiceName;
	}
	
	public String getKpiName() {
		return kpiName;
	}
	
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	
	public String getCustomServiceLevel() {
		return customServiceLevel;
	}
	
	public void setCustomServiceLevel(String customServiceLevel) {
		this.customServiceLevel = customServiceLevel;
	}
	
	public String getPenaltyAssessmentInterval() {
		return penaltyAssessmentInterval;
	}
	
	public void setPenaltyAssessmentInterval(String penaltyAssessmentInterval) {
		this.penaltyAssessmentInterval = penaltyAssessmentInterval;
	}
	
	public String getPenaltyValueUnit() {
		return penaltyValueUnit;
	}
	
	public void setPenaltyValueUnit(String penaltyValueUnit) {
		this.penaltyValueUnit = penaltyValueUnit;
	}
	
	public String getPenaltyValueExpression() {
		return penaltyValueExpression;
	}
	
	public void setPenaltyValueExpression(String penaltyValueExpression) {
		this.penaltyValueExpression = penaltyValueExpression;
	}
	
	public String getRewardAssessmentInterval() {
		return rewardAssessmentInterval;
	}
	
	public void setRewardAssessmentInterval(String rewardAssessmentInterval) {
		this.rewardAssessmentInterval = rewardAssessmentInterval;
	}
	
	public String getRewardValueUnit() {
		return rewardValueUnit;
	}
	
	public void setRewardValueUnit(String rewardValueUnit) {
		this.rewardValueUnit = rewardValueUnit;
	}
	
	public String getRewardValueExpression() {
		return rewardValueExpression;
	}
	
	public void setRewardValueExpression(String rewardValueExpression) {
		this.rewardValueExpression = rewardValueExpression;
	}

	public SLATemplate getSLATemplate() {
		return slaTemplate;
	}

	public void setSLATemplate(SLATemplate slaTemplate) {
		this.slaTemplate = slaTemplate;
	}

	public SLAContract getSLAContract() {
		return slaContract;
	}

	public void setSLAContract(SLAContract slaContract) {
		this.slaContract = slaContract;
	}	
}
