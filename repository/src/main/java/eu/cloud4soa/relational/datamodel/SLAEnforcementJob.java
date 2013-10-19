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
import javax.persistence.ManyToOne;

@Entity
public class SLAEnforcementJob extends AbstractModel<SLAEnforcementJob> implements ISLAEnforcementJob, Serializable {
	
	private static final long serialVersionUID = -3030195788393394753L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String uriId;
	
	@Column(unique=true)
	private String applicationInstanceUriId;
	
	@ManyToOne(targetEntity = SLAViolation.class)
	private List<SLAViolation> slaViolations;
	
	@Column
	private String slaContractId;
	
	//TODO: Do we need all these apiKey, secretKey logic?
/*	@Column
	private String apiKey;
	
	@Column
	private String secretKey;
*/

	@Column
	private Date lastExecuted;
	
	@Column
	private boolean enabled;
	
	public SLAEnforcementJob(){
		enabled = false;
		lastExecuted = new Date();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getUriId() {
		return uriId;
	}

	public void setUriId(String uriId) {
		this.uriId = uriId;
	}

	public String getApplicationInstanceUriId() {
		return applicationInstanceUriId;
	}

	public void setApplicationInstanceUri(String applicationInstanceUriId) {
		this.applicationInstanceUriId = applicationInstanceUriId;
	}

	public Date getLastExecuted() {
		return lastExecuted;
	}

	public void setLastExecuted(Date lastExecuted) {
		this.lastExecuted = lastExecuted;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<SLAViolation> getSlaViolations() {
		return slaViolations;
	}

	public void setSlaViolations(List<SLAViolation> slaViolations) {
		this.slaViolations = slaViolations;
	}



	@Override
	public String toString() {
		return "SLAEnforcementJob [id=" + id + ", uriId=" + uriId + ", applicationInstanceUriId=" + applicationInstanceUriId + ",SLAViolations="
				+ slaViolations + ", lastExecuted=" + lastExecuted + ", enabled=" + enabled + " ]";
	}

/*	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}*/
	
	public String getSlaContractId() {
		return slaContractId;
	}

	public void setSlaContractId(String slaContractId) {
		this.slaContractId = slaContractId;
	}
}
