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

package eu.cloud4soa.governance.datamodel.sla;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.cloud4soa.api.datamodel.governance.GuaranteeTerm;
import eu.cloud4soa.api.datamodel.governance.ServiceDescriptionTerm;

/**
 *
 * @author vincenzo
 */
public class SlaContract extends eu.cloud4soa.api.datamodel.governance.SlaContract{

	//TODO : Creating all fields as Strings for now 
	
	/** 
	 * SLA Context info
	 */
	private String agreementInitiator;
	private String agreementResponder;
	private String serviceProvider;
	private String expirationTime;
	private String templateId;
	private String templateName;
	
	/**
	 * ServiceDescriptionTerms
	 */
	private List<ServiceDescriptionTerm> serviceDescriptionTerms = new ArrayList<ServiceDescriptionTerm>();
	
	/**
	 * GuaranteeTerms
	 */	
	private List<GuaranteeTerm> guaranteeTerms = new ArrayList<GuaranteeTerm>();
	
	/**
	 * Constructs an SLAContract instance from a given XML filename
	 * @param filename
	 */
	public SlaContract(String filename) {
	    	    
	    try {
	    	InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
	    	
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
			Document doc = db.parse(is);
 			doc.getDocumentElement().normalize();

 			String rootElement = doc.getDocumentElement().getNodeName();		
			Node rootNode = doc.getElementsByTagName(rootElement).item(0);
			Node contextNode = ((Element)rootNode).getElementsByTagName("wsag:Context").item(0);

			if (contextNode.getNodeType() == Node.ELEMENT_NODE) {
			      Element eElement = (Element) contextNode;
			      this.agreementInitiator = getTagValue("wsag:AgreementInitiator", eElement);
			      this.agreementResponder = getTagValue("wsag:AgreementResponder", eElement);
			      this.serviceProvider = getTagValue("wsag:ServiceProvider", eElement);
			      this.expirationTime = getTagValue("wsag:ExpirationTime", eElement);
			      this.templateId = getTagValue("wsag:TemplateId", eElement);
			      this.templateName = getTagValue("wsag:TemplateName", eElement);			      			      
			}

			/*
			 * Parse ServiceDescriptionTerms
			 */
			NodeList serviceDescriptionTermsNodesList = ((Element)rootNode).getElementsByTagName("wsag:ServiceDescriptionTerm");
		    // iterate the serviceDescriptionTerms
	        for (int i = 0; i < serviceDescriptionTermsNodesList.getLength(); i++) {
	           Element element = (Element) serviceDescriptionTermsNodesList.item(i);
	           ServiceDescriptionTerm term = new ServiceDescriptionTerm(element);    
	           serviceDescriptionTerms.add(term);
	        }
			
	        /*
			 * Parse ServiceDescriptionTerms
			 */
			NodeList guaranteeTermsNodesList = ((Element)rootNode).getElementsByTagName("wsag:GuaranteeTerm");
		    // iterate the guaranteeTerms
	        for (int i = 0; i < guaranteeTermsNodesList.getLength(); i++) {
	           Element element = (Element) guaranteeTermsNodesList.item(i);
	           GuaranteeTerm term = new GuaranteeTerm(element);    
	           guaranteeTerms.add(term);
	        }
	        is.close();
	        
	        System.out.println("Parsed SLA Offer between " + agreementInitiator + " and " + agreementResponder);
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }		
	}
	
	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}

	public String getAgreementInitiator() {
		return agreementInitiator;
	}

	public String getAgreementResponder() {
		return agreementResponder;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public List<ServiceDescriptionTerm> getServiceDescriptionTerms() {
		return serviceDescriptionTerms;
	}

	public List<GuaranteeTerm> getGuaranteeTerms() {
		return guaranteeTerms;
	}

}
