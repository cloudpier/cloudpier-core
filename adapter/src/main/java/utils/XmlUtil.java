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


package utils;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {
  private static DocumentBuilderFactory documentBuilderFactory;

  /**
   * Not Thread Safe
   * @return javax.xml.parsers.DocumentBuilderFactory
   */
  public static synchronized DocumentBuilderFactory getDocumentBuilderFactory() {
    if( XmlUtil.documentBuilderFactory == null ) {
      // configure the document builder factory
      documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setValidating( true );
      documentBuilderFactory.setNamespaceAware( true );
    }
    return XmlUtil.documentBuilderFactory;
  }

  /**
   * @param is an InputSource
   * @return org.w3c.dom.Document
   * @throws javax.xml.parsers.ParserConfigurationException
   * @throws org.xml.sax.SAXException
   * @throws java.io.IOException
   */
  public static Document getDocument( InputSource is )
      throws ParserConfigurationException, SAXException, IOException {
    // get a builder to create a DOM document
    DocumentBuilder builder =
        XmlUtil.getDocumentBuilderFactory().newDocumentBuilder();

    // parse the damned document
    return builder.parse( is );
  }

}