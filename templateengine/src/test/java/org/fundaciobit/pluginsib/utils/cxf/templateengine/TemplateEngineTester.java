package org.fundaciobit.pluginsib.utils.cxf.templateengine;


import java.util.HashMap;
import java.util.Map;


import org.fundaciobit.pluginsib.utils.templateengine.TemplateEngine;

/**
 * 
 * @author anadal(u80067)
 *
 */
public class TemplateEngineTester {

  public static class Hola {

    private long caracola;

    public long getCaracola() {
      return caracola;
    }

    public void setCaracola(long caracola) {
      this.caracola = caracola;
    }

  }

  public static void main(String[] args) {

    try {
      Hola hola = new Hola();
      hola.setCaracola(1234);
      Map<String, Object> parameters = new HashMap<String, Object>();

      parameters.put("hola", hola);

      String[] plantilles = new String[] { "${hola.caracola}", "${hola.caracola?c}" };

      for (int i = 0; i < plantilles.length; i++) {
        String result = TemplateEngine.processExpressionLanguage(plantilles[i], parameters);

        System.out.println("Result[" + plantilles[i] + "]: " + result);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
