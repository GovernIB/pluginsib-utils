package org.fundaciobit.plugins.utils.cxf;

import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * @author anadal
 *
 */
public class CXFUtils {
  
  protected static Logger log = Logger.getLogger(CXFUtils.class);
  
  public static final Charset UTF_8 = Charset.forName("UTF-8");

  public static boolean isXMLFormat(byte[] data) {

    if (isBinaryFile(data)) {
      //log.info("\n\n XYZ ZZZ ES BINARY FILE \n\n");
    } else {

      boolean isXML = isXMLLike(new String(data, UTF_8));

      // System.out.println("isXML = " + isXML);
      if (isXML) {
        return true;
      }

    }
    return false;
  }

  public static boolean isBinaryFile(byte[] data) {

    int size = data.length;
    if (size > 1024)
      size = 1024;

    int ascii = 0;
    int other = 0;

    for (int i = 0; i < size; i++) {
      byte b = data[i];
      if (b < 0x09)
        return true;

      if (b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D)
        ascii++;
      else if (b >= 0x20 && b <= 0x7E)
        ascii++;
      else
        other++;
    }

    if (other == 0)
      return false;
    return 100 * other / (ascii + other) > 95;
  }

  protected static boolean isXMLLike(String inXMLStr) {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
      InputSource is = new InputSource(new StringReader(inXMLStr.replace('ñ', 'n')));

      Document doc = db.parse(is);

      doc.getDocumentElement().getTextContent();
      
      return true;
    } catch (Throwable e1) {
      // handle ParserConfigurationException 
      e1.printStackTrace();

      try {

          XMLReader reader = XMLReaderFactory.createXMLReader();
          InputSource is = new InputSource(new StringReader(inXMLStr.replace('ñ', 'n')));
          reader.parse(is);

          return true;

      } catch (Exception e) {

        e.printStackTrace();

      } 
      
      
      
      return false;
    }

  }
  

  public static ClientHandler getClientHandler(AbstractPluginProperties properties, 
      String basePath) throws Exception {
    
    final ClientHandler clientHandler;
    String username = properties.getProperty(basePath + "authorization.username");

    if (username != null && username.trim().length() != 0) {
      String password = properties.getProperty(basePath + "authorization.password");

      clientHandler = new ClientHandlerUsernamePassword(username, password);

    } else {

      String keystoreLocation = properties.getPropertyRequired(basePath + "authorization.ks.path");

      String keystoreType = properties.getPropertyRequired(basePath + "authorization.ks.type");
      String keystorePassword = properties.getPropertyRequired(basePath + "authorization.ks.password");
      String keystoreCertAlias = properties.getPropertyRequired(basePath + "authorization.ks.cert.alias");
      String keystoreCertPassword = properties.getPropertyRequired(basePath + "authorization.ks.cert.password");

      clientHandler = new ClientHandlerCertificate(keystoreLocation, keystoreType,
          keystorePassword, keystoreCertAlias, keystoreCertPassword);
    }
    return clientHandler;
  }

}
