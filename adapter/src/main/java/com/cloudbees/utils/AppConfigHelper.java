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

import java.io.IOException;
import java.io.InputStream;

import com.cloudbees.api.ApplicationConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AppConfigHelper {
    public static void load(ApplicationConfiguration applicationConfiguration, InputStream in, String[] environments, String[] implicitEnvironments)
    {
        InputSource input = new InputSource(in);
        load(applicationConfiguration, input, environments, implicitEnvironments);
    }
    
    private static void load(ApplicationConfiguration applicationConfiguration, InputSource input, String[] environments, String[] implicitEnvironments) {
        Document doc = readXML(input);
        Element rootElement = doc.getDocumentElement();
        if(rootElement.getNodeName().equals("stax-application") ||
                rootElement.getNodeName().equals("stax-web-app") || rootElement.getNodeName().equals("cloudbees-web-app"))
        {
            AppConfigParser parser = new AppConfigParser();
            parser.load(applicationConfiguration, doc, environments, implicitEnvironments);
        }
    }

    private static Document readXML(InputSource input) {
        DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
        dBF.setIgnoringComments(true); // Ignore the comments present in the
        // XML File when reading the xml
        DocumentBuilder builder = null;
        try {
            builder = dBF.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = builder.parse(input);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

}
