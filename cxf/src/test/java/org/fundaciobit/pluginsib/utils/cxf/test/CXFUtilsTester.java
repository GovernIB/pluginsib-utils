package org.fundaciobit.pluginsib.utils.cxf.test;

import java.io.InputStream;

import org.fundaciobit.pluginsib.core.utils.FileUtils;
import org.fundaciobit.pluginsib.utils.cxf.CXFUtils;

/**
 * 
 * @author anadal
 *
 */
public class CXFUtilsTester {

  public static void main(String[] args) {

    try {

      InputStream is = FileUtils.readResource(CXFUtilsTester.class, "ORVE_firma0.xsig");

      byte[] data = FileUtils.toByteArray(is);

      System.out.println(CXFUtils.isXMLFormat(data));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
