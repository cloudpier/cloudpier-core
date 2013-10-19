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
@XmlType(name = "slaPolicy", namespace = "eu.cloud4soa.api.datamodel.governance")
public class SlaPolicy {

	static public enum SlaPenaltyType { MIGRATION, SOFT_VIOLATION, WARNING; 
	
	    static public SlaPenaltyType from(String label) {
	        
	        return valueOf(label.toUpperCase().replace(" ", "_"));
	    }
	}
	
	private Long 				 id;
	private ServiceGuaranteeType metric_name;
	private Date 				 time_interval;
	private Integer 			 breach;
	private SlaPenaltyType			 value_expr;
	
	public SlaPolicy(){}
	
	public SlaPolicy (Long        id,
					  String      metric_name,
					  Date        time_interval,
					  Integer     breach,
					  SlaPenaltyType value_expr) {
		this.id 		   = id;
		this.metric_name   = ServiceGuaranteeType.valueOf(metric_name);
		this.time_interval = time_interval;
		this.breach 	   = breach;
		this.value_expr    = value_expr;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ServiceGuaranteeType getMetric_name() {
		return metric_name;
	}

	public void setMetric_name(ServiceGuaranteeType metric_name) {
		this.metric_name = metric_name;
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
}
