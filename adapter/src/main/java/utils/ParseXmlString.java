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

import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 * @deprecated 
 */
public class ParseXmlString {


  public void parse_print(String xml_string, String element_name) {

    

    try {
        DocumentBuilderFactory dbf =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml_string));

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName(element_name);

        // iterate the employees
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
    catch (Exception e) {
        e.printStackTrace();
    }
    /*
    output :
        Name: John
        Title: Manager
        Name: Sara
        Title: Clerk
    */

  }


  public String parse(String xml_string,String root_element, String element_name) {

      String ret="";
    try {
        DocumentBuilderFactory dbf =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml_string));

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName(root_element);

        // iterate the employees
        //for (int i = 0; i < nodes.getLength(); i++) {
        Element element = (Element) nodes.item(0);

        NodeList name = element.getElementsByTagName(element_name);
        Element line = (Element) name.item(0);
        System.out.println(element_name+" : " + getCharacterDataFromElement(line));
        ret=getCharacterDataFromElement(line);

    }
    catch (Exception e) {
        e.printStackTrace();
    }


      return ret;
  }

  public static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
       CharacterData cd = (CharacterData) child;
       return cd.getData();
    }
    return "?";
  }
}
