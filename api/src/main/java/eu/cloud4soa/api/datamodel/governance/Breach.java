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

import eu.cloud4soa.api.datamodel.governance.SlaTemplate.ServiceGuaranteeType;

@XmlRootElement()
@XmlType(name = "breach", namespace = "eu.cloud4soa.api.datamodel.governance")
public class Breach {

	private Long   				 id;
	private Date   				 timestamp;
	private String 				 user_id;
	private ServiceGuaranteeType metric_name;
	private String   			 value;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SlaTemplate.ServiceGuaranteeType getMetric_name() {
		return metric_name;
	}

	public void setMetric_name(SlaTemplate.ServiceGuaranteeType metric_name) {
		this.metric_name = metric_name;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getValue_expr() {
		return value;
	}

	public void setValue_expr(String value_expr) {
		this.value = value_expr;
	}
}
