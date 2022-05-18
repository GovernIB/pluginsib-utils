/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.fundaciobit.pluginsib.utils.xml;

import es.caib.scsp.esquemas.SCDCPAJUv3.peticion.datosespecificos.Solicitud;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

/**
 *
 * @author gdeignacio
 */
public class XmlManagerTest {
    
    XmlManager<PeticionDatosEspecificosTest> manager;
    
    PeticionDatosEspecificosTest item;
    
    Element element;
    
    String xml;
    
    public XmlManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        
        try {
            manager = new XmlManager<PeticionDatosEspecificosTest>(PeticionDatosEspecificosTest.class);
            
            item = new PeticionDatosEspecificosTest();
            
            xml = "<datosEspecificos><Solicitud><ProvinciaSolicitud>07</ProvinciaSolicitud><MunicipioSolicitud>026</MunicipioSolicitud><Titular><Documentacion><Tipo>NIF</Tipo><Valor>43085322C</Valor></Documentacion></Titular></Solicitud></datosEspecificos>";
            
            try {
                manager.stringToElement(xml);
            } catch (TransformerException ex) {
                Logger.getLogger(XmlManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(XmlManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(XmlManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XmlManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        } catch (JAXBException ex) {
            Logger.getLogger(XmlManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    @After
    public void tearDown() {
    }

    
    /**
     * Test of getXmlSchemaAnnotation method, of class XmlManager.
     */
    @Test
    public void testGetXmlSchemaAnnotation() {
        
        System.out.println("getXmlSchemaAnnotation");
        
        XmlSchema xmlSchemaAnnotation = manager.getXmlSchemaAnnotation();
  
        System.out.println("SCHEMANAMESPACE: " + xmlSchemaAnnotation.namespace());
        System.out.println("LOCATION: " + xmlSchemaAnnotation.location());
        
    }
    
    
       
    /**
     * Test of getXmlSchemaAnnotation method, of class XmlManager.
     */
    @Test
    public void testGetXmlRootElementAnnotation() {
        
        System.out.println("getXmlRootElementAnnotation");
        
        XmlRootElement xmlRootElementAnnotation = manager.getXmlRootElementAnnotation();
        
        assertNotNull(xmlRootElementAnnotation);
  
        System.out.println("ROOTNAMESPACE: " + xmlRootElementAnnotation.namespace());
        System.out.println("NAME: " + xmlRootElementAnnotation.name());
        
    }
    


    /**
     * Test of generateItem method, of class XmlManager.
     */
    @Test
    public void testGenerateItem_String() throws Exception {
        System.out.println("generateItem");
        
        Object expResult = null;
        Object result = manager.generateItem(element);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        
        
        
        
        NamedNodeMap attrs = element.getAttributes();
        
        while (attrs.getLength() > 0) {
            attrs.removeNamedItem(attrs.item(0).getNodeName());
        }
        
        //peticionDatosEspecificos.setAttribute(XMLConstants.XMLNS_ATTRIBUTE.concat(":ns2"), EMISERV_BACKOFFICE_XMLNS);
       
        
        /*
        XmlManager<SCDCPAJUv3PeticionDatosEspecificos> manager
                = new XmlManager<SCDCPAJUv3PeticionDatosEspecificos>(SCDCPAJUv3PeticionDatosEspecificos.class);
        pde = manager.generateItem(peticionDatosEspecificos, false, true);

        log.info("SCDCPAJUv3Client :: Datos Especificos Peticion: " +  ((pde!=null)?pde.toString():""));
        */
        
        
        
        
    }


    
    
    
    
    
    
    
}
