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
@XmlType(name = "serviceDescriptionTerm", namespace="eu.cloud4soa.api.datamodel.governance")
public class ServiceDescriptionTerm {
	
	private String serviceDescriptionTermName;
	private String serviceDescriptionServiceName;
	
	private String applicationName;
	private String applicationVersion;
	private String applicationDescription;
	
    public ServiceDescriptionTerm() {
        
    }

	public ServiceDescriptionTerm(String serviceDescriptionTermName,
			String serviceDescriptionServiceName, String applicationName,
			String applicationVersion, String applicationDescription) {
		
		this.serviceDescriptionTermName = serviceDescriptionTermName;
		this.serviceDescriptionServiceName = serviceDescriptionServiceName;
		this.applicationName = applicationName;
		this.applicationVersion = applicationVersion;
		this.applicationDescription = applicationDescription;
	}

	// Old constructor, not used anymore, will keep for a while for backwards compatibility
	/**
	 * Constructs a ServiceDescriptionTerm from a ServiceDescriptionTerm XML element in a WS-Agreement offer
	 *  
	 * @param element
	 */	
	public ServiceDescriptionTerm(Element element){
		serviceDescriptionTermName = element.getAttribute("wsag:Name");
        serviceDescriptionServiceName = element.getAttribute("wsag:ServiceName");
	}	
	
	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}

	public String getServiceDescriptionTermName() {
		return serviceDescriptionTermName;
	}

	public String getServiceDescriptionServiceName() {
		return serviceDescriptionServiceName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public String getApplicationDescription() {
		return applicationDescription;
	}
	
}
