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

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class FindElementFromResponse {
  public static void main(String xml_string) throws Exception{
   // String xmlRecords = "<data><employee><name>A</name>"
  //      + "<title>Manager</title></employee></data>";

    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(xml_string));

    Document doc = db.parse(is);
    NodeList nodes = doc.getElementsByTagName("employee");

    for (int i = 0; i < nodes.getLength(); i++) {
      Element element = (Element) nodes.item(i);

      NodeList name = element.getElementsByTagName("name");
      Element line = (Element) name.item(0);
      System.out.println("Name: " + getCharacterDataFromElement(line));

      NodeList title = element.getElementsByTagName("title");
      line = (Element) title.item(0);
      System.out.println("Title: " + getCharacterDataFromElement(line));
    }

  }
  public static String parseResponse1stlevel(String xml_string,String parent, String elementname)  throws Exception{
  String ret = "";

      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(xml_string));

    Document doc = db.parse(is);
    NodeList nodes = doc.getElementsByTagName(parent);

    for (int i = 0; i < nodes.getLength(); i++) {
      Element element = (Element) nodes.item(i);

      NodeList name = element.getElementsByTagName(elementname);
      Element line = (Element) name.item(0);
      System.out.println(elementname+ ": "+ getCharacterDataFromElement(line));
      ret= getCharacterDataFromElement(line);

    }
  return ret;
   }



  public static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
      CharacterData cd = (CharacterData) child;
      return cd.getData();
    }
    return "";
  }
}
