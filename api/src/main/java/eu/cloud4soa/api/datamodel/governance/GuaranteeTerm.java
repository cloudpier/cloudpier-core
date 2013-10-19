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
package eu.cloud4soa.api.datamodel.governance;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@XmlRootElement()
@XmlType(name = "guaranteeTerm", namespace="eu.cloud4soa.api.datamodel.governance")
public class GuaranteeTerm {

	/*
	 * ServiceLevelObjective 
	 */	
	private String guaranteeTermName;
	private String serviceScopeServiceName;
	private String kpiName;
	private String customServiceLevel;
	
	/*
	 * BusinessValueList - Penalty
	 */
	private String penaltyAssessmentInterval;
	private String penaltyValueUnit;
	private String penaltyValueExpression;
 
	
	/*
	 * BusinessValueList - Reward
	 */
	private String rewardAssessmentInterval;
	private String rewardValueUnit;
	private String rewardValueExpression;
	
    
    public GuaranteeTerm() {
        
    }
	
	public GuaranteeTerm(String guaranteeTermName,
			String serviceScopeServiceName, String kpiName,
			String customServiceLevel, String penaltyAssessmentInterval,
			String penaltyValueUnit, String penaltyValueExpression,
			String rewardAssessmentInterval, String rewardValueUnit,
			String rewardValueExpression) {
		super();
		this.guaranteeTermName = guaranteeTermName;
		this.serviceScopeServiceName = serviceScopeServiceName;
		this.kpiName = kpiName;
		this.customServiceLevel = customServiceLevel;
		this.penaltyAssessmentInterval = penaltyAssessmentInterval;
		this.penaltyValueUnit = penaltyValueUnit;
		this.penaltyValueExpression = penaltyValueExpression;
		this.rewardAssessmentInterval = rewardAssessmentInterval;
		this.rewardValueUnit = rewardValueUnit;
		this.rewardValueExpression = rewardValueExpression;
	}
	
	// Old constructor, not used anymore, will keep for a while for backwards compatibility
	/**
	 * Constructs a GuaranteeTerm from a GuaranteeTerm XML element in a WS-Agreement offer
	 *  
	 * @param element
	 */
	public GuaranteeTerm(Element element){
           this.guaranteeTermName = element.getAttribute("wsag:Name");           
           Element serviceScope = (Element)element.getElementsByTagName("wsag:ServiceScope").item(0);
           this.serviceScopeServiceName = serviceScope.getAttribute("wsag:ServiceName");
           
           /*
            * Parse serviceLevelObjective
            */
           Element serviceLevelObjective = (Element)element.getElementsByTagName("wsag:ServiceLevelObjective").item(0);
           Element kpiTarget = (Element)serviceLevelObjective.getElementsByTagName("wsag:KPITarget").item(0);   
           this.kpiName = getTagValue("wsag:KPIName", kpiTarget);
           this.customServiceLevel = getTagValue("wsag:CustomServiceLevel", kpiTarget);
           
           Element businessValueList = (Element)element.getElementsByTagName("wsag:BusinessValueList").item(0);
           /*
            * Parse penalty
            */         
           Element penalty = (Element)businessValueList.getElementsByTagName("wsag:Penalty").item(0);
           Element assessmentInterval = (Element)penalty.getElementsByTagName("wsag:AssessmentInterval").item(0);       
           this.penaltyAssessmentInterval = getTagValue("wsag:TimeInterval", assessmentInterval);
           this.penaltyValueUnit = getTagValue("wsag:ValueUnit", penalty);
           this.penaltyValueExpression = getTagValue("wsag:ValueExpression", penalty);
           
           /*
            * Parse reward
            */
           Element reward = (Element)businessValueList.getElementsByTagName("wsag:Reward").item(0);
           Element rewardAssessmentInterval = (Element)reward.getElementsByTagName("wsag:AssessmentInterval").item(0);       
           this.rewardAssessmentInterval = getTagValue("wsag:TimeInterval", rewardAssessmentInterval);
           this.rewardValueUnit = getTagValue("wsag:ValueUnit", reward);
           this.rewardValueExpression = getTagValue("wsag:ValueExpression", reward);
	}
	
	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}

	public String getGuaranteeTermName() {
		return guaranteeTermName;
	}

	public String getServiceScopeServiceName() {
		return serviceScopeServiceName;
	}

	public String getKpiName() {
		return kpiName;
	}

	public String getCustomServiceLevel() {
		return customServiceLevel;
	}

	public String getPenaltyAssessmentInterval() {
		return penaltyAssessmentInterval;
	}

	public String getPenaltyValueUnit() {
		return penaltyValueUnit;
	}

	public String getPenaltyValueExpression() {
		return penaltyValueExpression;
	}

	public String getRewardAssessmentInterval() {
		return rewardAssessmentInterval;
	}

	public String getRewardValueUnit() {
		return rewardValueUnit;
	}

	public String getRewardValueExpression() {
		return rewardValueExpression;
	}
	
}
