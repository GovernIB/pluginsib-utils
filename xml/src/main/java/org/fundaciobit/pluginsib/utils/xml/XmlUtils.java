/*
 * Copyright 2020 gdeignacio.
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
package org.fundaciobit.pluginsib.utils.xml;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author gdeignacio
 */
public class XmlUtils {
    
    @Deprecated
    public static Element namespacedElement(Element element, String namespace){
        
        Document document = element.getOwnerDocument();
        
        if (document==null) return element;
        
        Element namespacedElement = document.createElementNS(namespace, element.getTagName());
        
        //namespacedElement.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, namespacedElement.getTagName(), namespace);
        
        if (element.hasAttributes()){
            for (int i=0; i< element.getAttributes().getLength(); i++){
                Attr attr = (Attr)element.getAttributes().item(i);
                namespacedElement.setAttributeNode(attr);
            }
        }
        
        if (element.hasChildNodes()){
            for (int i=0; i< element.getChildNodes().getLength();i++){
                namespacedElement.appendChild(element.getChildNodes().item(i));
            }
        }
        
        return namespacedElement;
        
    }
    
    public static Element node2Element(Node node){
    
        if (node == null) return null;
        
        Element element = (Element)node;
        System.out.println(element.toString());
        boolean hasChildNodes = node.hasChildNodes();
        if (!hasChildNodes) return (Element)element.cloneNode(true);
        
        for (int i=0; i < element.getChildNodes().getLength(); i++){
            Node childNode = element.getChildNodes().item(i);
            if (childNode.getNodeType()==Node.ELEMENT_NODE){
                Element childElement = node2Element(childNode);
                element.replaceChild(childElement, childNode);
            }
        }
        return element;
 
    }
    
    
     public static Element stringToElement(String xml) throws ParserConfigurationException, SAXException, IOException {
        
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = factory.newDocumentBuilder();
         InputSource inputStream = new InputSource();

         //For parsing XML document using FileReader
         //inputStream.setCharacterStream(new FileReader("student.xml"));

         //If you have an xml string then you can pass the string into StringReader
         inputStream.setCharacterStream(new StringReader(xml));
         Document document = documentBuilder.parse(inputStream);
         
         
         
         return node2Element(document.getDocumentElement());
         
         /*
         NodeList studentNodeList = document.getElementsByTagName("student");
         for (int index = 0; index < studentNodeList.getLength(); index++) {
             Node node = studentNodeList.item(index);
             if (node.getNodeType() == Node.ELEMENT_NODE) {
                 Element element = (Element) node;
                 NodeList nameNodeList = element.getElementsByTagName("name");
                 for (int eIndex = 0; eIndex < nameNodeList.getLength(); eIndex++) {
                     if (nameNodeList.item(eIndex).getNodeType() == Node.ELEMENT_NODE) {
                         Element nameElement = (Element) nameNodeList.item(eIndex);
                         System.out.println("Name = " + nameElement.getFirstChild().getNodeValue().trim());
                     }
                 }
             }
         }
         */
         
         
        
    }
    
    
    
}
