package org.fundaciobit.plugins.utils.cxf.test;

import java.io.InputStream;

import org.fundaciobit.plugins.utils.FileUtils;
import org.fundaciobit.plugins.utils.cxf.CXFUtils;

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
