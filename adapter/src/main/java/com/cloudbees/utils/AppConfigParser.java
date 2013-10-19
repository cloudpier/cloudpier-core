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
 * Copyright 2010-2011, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.utils;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.cloudbees.api.ApplicationConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AppConfigParser {
    public AppConfigParser()
    {
    }
	public void load(ApplicationConfiguration applicationConfiguration, Document doc, String[] environments,
            String[] implicitEnvironments) {
        XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try {
			Element e = doc.getDocumentElement();			
			applyToAppConfig(applicationConfiguration, xpath, e);
			applicationConfiguration.setDefaultEnvironment(e.getAttribute("default"));
			if(environments == null || environments.length == 0)
			{				
				environments = getEnvironmentList(applicationConfiguration.getDefaultEnvironment(), implicitEnvironments);
			}
			else
			{
				environments = getEnvironmentList( join(environments, ","), implicitEnvironments);
			}
			
			//now apply any environment-specific overrides
			NodeList nodes = 
			    (NodeList) xpath.evaluate("environment",
			    		e, XPathConstants.NODESET);			
			Map<String, Node> envNodes = new HashMap<String, Node>();
			for(int i=0; i<nodes.getLength(); i++)
			{
				Node n = nodes.item(i);
				String envName = getAttribute(n, "name", null);
				if(envName == null)
					throw new IllegalArgumentException("missing required attribute (name) on environment element");
				envNodes.put(envName, n);
			}
			
			if(environments != null)
			{
				for(String env : environments)
				{
					Node n = envNodes.get(env);
					if(n != null)
					{
						applyToAppConfig(applicationConfiguration, xpath, n);
						applicationConfiguration.getAppliedEnvironments().add(env);
					}
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private void applyToAppConfig(ApplicationConfiguration applicationConfiguration, XPath xpath, Node e)
			throws XPathExpressionException {
		String appId = getNodeValue(e, xpath, "appid", true);
		if(appId != null)
			applicationConfiguration.setApplicationId(appId);
		
	}
	
	private String getAttribute(Node node, String attr, String defaultValue)
	{
		Node attrNode = node.getAttributes().getNamedItem(attr);
		if(attrNode == null)
			return defaultValue;
		else
			return attrNode.getNodeValue();
	}
	
	private String getNodeValue(Node node, XPath xpath, String expression, boolean trim) throws XPathExpressionException
	{
		Node n = (Node)xpath.evaluate(expression, node, XPathConstants.NODE);
		if(n == null)
			return null;
		else
		{
			String nodeValue = xpath.evaluate("text()", n);
			return nodeValue.trim();
		}
	}
	
	public static String[] getEnvironmentList(String environments, String...prependEnvs)
	{
		if(environments == null && prependEnvs.length == 0)
			return new String[0];
		
		if(environments == null)
			environments = "";
		
		//split the environments string and prepend the run environment 
		String[] envSplit = environments.split(",");
		String[] envList = new String[envSplit.length+prependEnvs.length];
		for(int i=0; i<prependEnvs.length; i++)
			envList[i] = prependEnvs[i];
		
		for(int i=0; i<envSplit.length; i++)
			envList[prependEnvs.length + i] = envSplit[i].trim();
		return envList;
	}

    public String join( String[] array, String delim ) {
        String str = "";
        for ( int i=0; i<array.length; i++ ) {
            if (i!=0) str += delim;
            str += array[i];
        }
        return str;
    }
}
