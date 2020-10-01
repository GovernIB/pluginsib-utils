package org.fundaciobit.pluginsib.utils.templateengine;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @author anadal(u80067)
 *
 */
public class TemplateEngine {

  protected static final Logger log = Logger.getLogger(TemplateEngine.class);

  public static String processExpressionLanguage(String plantilla,
      Map<String, Object> parameters) throws IOException {
    return internalProcessExpressionLanguage(plantilla, parameters, null, false);
  }
  
  public static String processExpressionLanguageSquareBrackets(String plantilla,
      Map<String, Object> parameters) throws IOException {
    return internalProcessExpressionLanguage(plantilla, parameters, null, true);
  }

  public static String processExpressionLanguage(String plantilla,
      Map<String, Object> parameters, Locale locale) throws IOException {

    return internalProcessExpressionLanguage(plantilla, parameters, locale, false);
  }

  public static String processExpressionLanguageSquareBrackets(String plantilla,
      Map<String, Object> parameters, Locale locale) throws IOException {

    return internalProcessExpressionLanguage(plantilla, parameters, locale, true);
  }
  
  protected static String internalProcessExpressionLanguage(String plantilla,
      Map<String, Object> parameters, Locale locale, boolean useSquareBrackets) throws IOException {

    if (parameters == null) {
      parameters = new HashMap<String, Object>();
    }

    Configuration configuration;

    configuration = new Configuration(Configuration.VERSION_2_3_28);
    configuration.setDefaultEncoding("UTF-8");
    if (useSquareBrackets){
        configuration.setInterpolationSyntax(Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX);
    }
    
    if (locale != null) {
      configuration.setLocale(locale);
    }
    Template template;
    template = new Template("exampleTemplate", new StringReader(plantilla), configuration);

    try {
      Writer out = new StringWriter();
      template.process(parameters, out);

      String res = out.toString();
      return res;
    } catch (TemplateException te) {
      final String msg = "No s'ha pogut processar l'Expression Language " + plantilla + ":"
          + te.getMessage();
      throw new IOException(msg, te);
    }
  }
  
}
