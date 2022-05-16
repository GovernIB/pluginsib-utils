/*
 * Copyright 2016 gdeignacio.
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



import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author gdeignacio
 * @param <T>
 */
public class XmlManager<T> {

    private static final Logger LOG = Logger.getLogger(XmlManager.class.getName());

    private final Class<T> clazz;

    private final JAXBContext jaxbContext;

    public XmlManager(Class<T> clazz) throws JAXBException {

        this.clazz = clazz;
        this.jaxbContext = JAXBContext.newInstance(clazz);
       
    }

    public JAXBContext getContext() {
        return this.jaxbContext;
    }
    
    public JAXBElement<T> getJAXBElement(T item){
        return getJAXBElement(item, getXmlSchemaAnnotation().namespace(), getXmlRootElementAnnotation().name());
    }
    
    public JAXBElement<T> getJAXBElement(T item, String namespaceURI, String localPart){
        
        QName qname = new QName(namespaceURI, localPart);
        
        JAXBElement<T> jaxbElement = new JAXBElement<T>(qname, clazz, item); 
        return jaxbElement;
    }
    
    
    public XmlSchema getXmlSchemaAnnotation(){
        
        XmlSchema xmlSchemaAnnotation;
        
        xmlSchemaAnnotation = clazz.getAnnotation(XmlSchema.class);
        
        if (xmlSchemaAnnotation!=null) return xmlSchemaAnnotation;
        
        xmlSchemaAnnotation = clazz.getPackage().getAnnotation(XmlSchema.class);
        
        if (xmlSchemaAnnotation!=null) return xmlSchemaAnnotation;
        
        if (clazz.getSuperclass()==null) return null;
        
        xmlSchemaAnnotation = clazz.getSuperclass().getAnnotation(XmlSchema.class);
        
        if (xmlSchemaAnnotation!=null) return xmlSchemaAnnotation;
        
        xmlSchemaAnnotation = clazz.getSuperclass().getPackage().getAnnotation(XmlSchema.class);
        
        if (xmlSchemaAnnotation!=null) return xmlSchemaAnnotation;
        
        return null;
    }
    
    public XmlRootElement getXmlRootElementAnnotation(){
        
        XmlRootElement xmlRootElementAnnotation;
        
        xmlRootElementAnnotation = clazz.getAnnotation(XmlRootElement.class);
        
        if (xmlRootElementAnnotation!=null) return xmlRootElementAnnotation;
        
        return null;
    }
    

    private ByteArrayOutputStream marshalToByteArrayOutputStream(T item) throws JAXBException {

        return XmlManager.this.marshalToByteArrayOutputStream(item, Boolean.TRUE, Boolean.TRUE);

    }

    private ByteArrayOutputStream marshalToByteArrayOutputStream(T item, boolean jaxbFormattedOutput, boolean jaxbFragment) throws JAXBException {

        if (item==null) return null;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, jaxbFormattedOutput);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, jaxbFragment);
        jaxbMarshaller.marshal(item, baos);
        return baos;
    }

    /*
    @Deprecated
    private Element marshalToElement(T item) throws JAXBException, ParserConfigurationException{
        return XmlManager.this.marshalToElement(item, Boolean.TRUE);
    }
    */
    
    /*
    @Deprecated
    private Element marshalToElement(T item, boolean formattedOutput) throws JAXBException, ParserConfigurationException {

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formattedOutput);
        
        jaxbMarshaller.marshal(item, new DOMResult(document));
       
        Element element = document.getDocumentElement();
       
        return element;
    }
    */
    
    private T unmarshal(InputStream is) throws JAXBException {

        if (is==null) return null;
        
       Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
       return jaxbUnmarshaller.unmarshal(new StreamSource(is), clazz).getValue();
       
    }
    
    
    private T unmarshal(String xml) throws JAXBException {

       if (xml==null) return null;
        
       Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
       return jaxbUnmarshaller.unmarshal(new StreamSource(new StringReader(xml)), clazz).getValue();
       
    }
    
    public Element generateElement(T item) throws JAXBException, ParserConfigurationException{
        return generateElement(item, Boolean.FALSE);
    }
    
    
    public Element generateElement(T item, boolean withXmlns) throws JAXBException, ParserConfigurationException{
        
        if (item==null) return null;
        
        String xml = generateXmlString(item);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Element element = null;
        
        try {
            element = stringToElement(xml);
            if (!withXmlns) element.removeAttribute(XMLConstants.XMLNS_ATTRIBUTE);
            return element;

        } catch (TransformerException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        return element;
        
      
    }
    
    public DataHandler generateXml(T item) throws JAXBException {

        if (item==null) return null;
        
        byte[] b = marshalToByteArrayOutputStream(item).toByteArray();
        String mimetype = "application/xml";
        DataSource ds = new ByteArrayDataSource(b, mimetype);
        return new DataHandler(ds);

    }

    public DataHandler generateXml(List<T> items) throws JAXBException {

        if (items==null) return null;
        
        byte[] b = generateXmlString(items).getBytes();
        String mimetype = "text/plain";
        DataSource ds = new ByteArrayDataSource(b, mimetype);
        return new DataHandler(ds);

    }

    public DataHandler generateFlatXml(List<T> items) throws JAXBException {

        if (items==null) return null;
        
        byte[] b = generateFlatXmlString(items).getBytes();
        String mimetype = "text/plain";
        DataSource ds = new ByteArrayDataSource(b, mimetype);
        return new DataHandler(ds);

    }

    public T generateItem(DataHandler document) throws JAXBException, IOException {
        
        if (document==null) return null;
        
        return unmarshal(document.getInputStream());
    }
    
    public T generateItem(InputStream is) throws JAXBException, IOException {
        return unmarshal(is);
    }
    
    public T generateItem(String xml) throws JAXBException, IOException {
        return unmarshal(xml);
    }
    
    public T generateItem(Element element) throws JAXBException, IOException{
        return generateItem(element, false, true);
    }
    
    public T generateItem(Element element, boolean noCheckXmlns, boolean addXmlns) throws JAXBException, IOException{
        
        if (element==null) return null;
        
        if (noCheckXmlns) return serializeElementAndGenerateItem(element);
        
        XmlSchema xmlSchemaAnnotation = getXmlSchemaAnnotation();
        
        String attribute = element.getAttribute(XMLConstants.XMLNS_ATTRIBUTE);
        
        LOG.log(Level.INFO, "SCDCPAJUv3Client :: Esquema :: XMLSchemaAnnotation: {0}", xmlSchemaAnnotation);
        LOG.log(Level.INFO, "SCDCPAJUv3Client :: Element :: xmlns: {0}", attribute);
        
        try {
            LOG.log(Level.INFO, "SCDCPAJUv3Client :: Element :: Antes: {0}", elementToString(element));
        } catch (TransformerException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        element.removeAttribute(XMLConstants.XMLNS_ATTRIBUTE);

        try {
            LOG.log(Level.INFO, "SCDCPAJUv3Client :: Element :: NoAtribut: {0}", elementToString(element));
        } catch (TransformerException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (addXmlns && xmlSchemaAnnotation!=null){
            element.setAttribute(XMLConstants.XMLNS_ATTRIBUTE, xmlSchemaAnnotation.namespace());
        }
        
        try {
            LOG.log(Level.INFO, "SCDCPAJUv3Client :: Element :: Attribute default: {0}", elementToString(element));
        } catch (TransformerException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return serializeElementAndGenerateItem(element);
    }
    
    
    public String serializeElement(Element element){
        Document document = element.getOwnerDocument();
        DOMImplementationLS domImplLS = (DOMImplementationLS) document
                    .getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        serializer.getDomConfig().setParameter("xml-declaration", false);
        String xml = serializer.writeToString(element);
        return xml;
    }
    
    private T serializeElementAndGenerateItem(Element element) throws JAXBException, IOException{
        
        if (element==null) return null;
        
        String xml = "";
        try {
            xml = elementToString(element);
        } catch (TransformerException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return generateItem(xml);
    }

    public byte[] generateXmlByteArray(T item) throws JAXBException {

        if (item==null) return new byte[]{};
        
        return marshalToByteArrayOutputStream(item).toByteArray();

    }

    public String generateFlatXmlString(T item) throws JAXBException {
        
        if (item==null) return null;

        return marshalToByteArrayOutputStream(item, Boolean.FALSE, Boolean.TRUE).toString();

    }

    public String generateFlatXmlString(List<T> items) throws JAXBException {

        if (items==null) return null;
        
        StringBuilder mensajes = new StringBuilder();
        for (T item : items) {
            mensajes.append(generateFlatXmlString(item));
            mensajes.append(IOUtils.LINE_SEPARATOR);
        }
        return mensajes.toString();

    }

    public String generateXmlString(T item) throws JAXBException {
      
        return generateXmlString(item, Boolean.TRUE, Boolean.TRUE);

    }
    
    public String generateXmlString(T item, boolean jaxbFormattedOutput, boolean jaxbFragment) throws JAXBException {
        
        if (item==null) return null;

        return marshalToByteArrayOutputStream(item, jaxbFormattedOutput, jaxbFragment).toString();

    }
    

    public String generateXmlString(List<T> items) throws JAXBException {

        if (items==null) return null;
        
        StringBuilder mensajes = new StringBuilder();
        for (T item : items) {
            mensajes.append(generateXmlString(item));
        }
        return mensajes.toString();

    }
    
    
    public boolean validateItem(T item, DataHandler xsd) throws JAXBException{
        
        return XmlValidation.validateXMLSchema(xsd, generateXml(item));
    
    }
    
    
    private Element stringToElement(String xml) throws TransformerException, ParserConfigurationException, SAXException, IOException{
        return stringToElement(xml, Boolean.FALSE);
    }
    
    private Element stringToElement(String xml, boolean namespaceAware) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(namespaceAware);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        StringReader reader = new StringReader(xml);
        InputSource inputSource = new InputSource(reader);
        Document doc = dBuilder.parse(inputSource);
        return doc.getDocumentElement();
    }

    
    private String elementToString(Element element) throws TransformerException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(
                new DOMSource(element),
                new StreamResult(buffer));
        return buffer.toString();
    }

}
