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
package eu.cloud4soa.api.datamodel.governance;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlType(name = "slaviolation", namespace = "eu.cloud4soa.api.datamodel.governance")
public class SLAViolation {

	private Long   id;
	private long   sla_enforcement_job_id;
	private String application_instance_uri_id;
	private Date   date_and_time;
	private String metric_name;
	private float  expected_value;
	private float  actual_value;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public long getSla_enforcement_job_id() {
		return sla_enforcement_job_id;
	}
	
	public void setSla_enforcement_job_id(long sla_enforcement_job_id) {
		this.sla_enforcement_job_id = sla_enforcement_job_id;
	}
	
	public String getApplication_instance_uri_id() {
		return application_instance_uri_id;
	}
	
	public void setApplication_instance_uri_id(String application_instance_uri_id) {
		this.application_instance_uri_id = application_instance_uri_id;
	}
	
	public Date getDate_and_time() {
		return date_and_time;
	}
	
	public void setDate_and_time(Date date_and_time) {
		this.date_and_time = date_and_time;
	}
	
	public String getMetric_name() {
		return metric_name;
	}
	
	public void setMetric_name(String metric_name) {
		this.metric_name = metric_name;
	}
	
	public float getExpected_value() {
		return expected_value;
	}
	
	public void setExpected_value(float expected_value) {
		this.expected_value = expected_value;
	}
	
	public float getActual_value() {
		return actual_value;
	}
	
	public void setActual_value(float actual_value) {
		this.actual_value = actual_value;
	}
}
