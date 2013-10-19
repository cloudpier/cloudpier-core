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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name="BREACH")
public class Breach extends AbstractModel<Breach> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6559818879482747078L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
	private Long id;
	
	@Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@Column
	private String applicationInstanceUriId;
	
	@Column
	private String user_id;
	
	@Column
	private String metric_name;
	
	@Column
	private String value;
	
	@Column
	private Long slaPolicyId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slaViolation", nullable = true)
	private SLAViolation slaViolation;
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getApplicationInstanceUriId() {
		return applicationInstanceUriId;
	}

	public void setApplicationInstanceUriId(String applicationInstanceUriId) {
		this.applicationInstanceUriId = applicationInstanceUriId;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMetric_name() {
		return metric_name;
	}

	public void setMetric_name(String metric_name) {
		this.metric_name = metric_name;
	}

	public String getType() {
		return metric_name;
	}

	public void setType(String type) {
		this.metric_name = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Long getSlaPolicyId() {
		return slaPolicyId;
	}

	public void setSlaPolicyId(Long slaPolicyId) {
		this.slaPolicyId = slaPolicyId;
	}

	public SLAViolation getSlaViolation() {
		return slaViolation;
	}

	public void setSlaViolation(SLAViolation slaViolation) {
		this.slaViolation = slaViolation;
	}
}
