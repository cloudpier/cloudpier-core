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
public class ServiceDescriptionTerm extends AbstractModel<ServiceDescriptionTerm> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3314819373377117836L;

	@Id
	@GeneratedValue
	@Column(name="service_description_term_id")
	private Long id;

	@Column
	private String serviceDescriptionTermName;
	@Column
	private String serviceDescriptionServiceName;

	@Column
	private String applicationName;
	@Column
	private String applicationVersion;
	@Column
	private String applicationDescription;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slaTemplate", nullable = true)
	private SLATemplate slaTemplate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slaContract", nullable = true)
	private SLAContract slaContract;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceDescriptionTermName() {
		return serviceDescriptionTermName;
	}

	public void setServiceDescriptionTermName(String serviceDescriptionTermName) {
		this.serviceDescriptionTermName = serviceDescriptionTermName;
	}

	public String getServiceDescriptionServiceName() {
		return serviceDescriptionServiceName;
	}

	public void setServiceDescriptionServiceName(
			String serviceDescriptionServiceName) {
		this.serviceDescriptionServiceName = serviceDescriptionServiceName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public String getApplicationDescription() {
		return applicationDescription;
	}

	public void setApplicationDescription(String applicationDescription) {
		this.applicationDescription = applicationDescription;
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
