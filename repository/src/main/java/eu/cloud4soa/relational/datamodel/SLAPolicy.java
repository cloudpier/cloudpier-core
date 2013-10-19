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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eu.cloud4soa.api.datamodel.governance.SlaPolicy.SlaPenaltyType;
import eu.cloud4soa.api.datamodel.governance.SlaTemplate.ServiceGuaranteeType;

@Entity
@Table(name="SLAPOLICY")
public class SLAPolicy extends AbstractModel<SLAPolicy> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5500997262674857503L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
	private Long id;
	
	@Column
	private String metric_name;
	
	@Column
	private Date time_interval;
	
	@Column
	private Integer breach;
	
	@Column(columnDefinition = "enum('MIGRATION','SOFT_VIOLATION','WARNING')")
    @Enumerated( javax.persistence.EnumType.STRING )
	private SlaPenaltyType value_expr;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slaContract", nullable = true)
	private SLAContract slaContract;
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public ServiceGuaranteeType getMetric_name() {
		return ServiceGuaranteeType.valueOf(metric_name);
	}

	public void setMetric_name(ServiceGuaranteeType metric_name) {
		this.metric_name = metric_name.toString();
	}

	public Date getTime_interval() {
		return time_interval;
	}

	public void setTime_interval(Date time_interval) {
		this.time_interval = time_interval;
	}

	public Integer getBreach() {
		return breach;
	}

	public void setBreach(Integer breach) {
		this.breach = breach;
	}

	public SlaPenaltyType getValue_expr() {
		return value_expr;
	}

	public void setValue_expr(SlaPenaltyType value_expr) {
		this.value_expr = value_expr;
	}

	public SLAContract getSLAContract() {
		return slaContract;
	}

	public void setSLAContract(SLAContract slaContract) {
		this.slaContract = slaContract;
	}
}
