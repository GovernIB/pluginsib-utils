package org.fundaciobit.pluginsib.utils.discoverplugins.test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;

import org.fundaciobit.pluginsib.utils.discoverplugins.DiscoverPluginsIB;
import org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemicrosoft.ExcelExportData;
import org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemicrosoft.IExportDataPlugin;
import org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemicrosoft.MicrosoftPluginAnnotation;
import org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemicrosoft.OdtExportData;
import org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemicrosoft.WordExportData;
import org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemyplugin.IMyPlugin;
import org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemyplugin.MyPlugin;
import org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemyplugin.MyPluginImpl;
import org.junit.Assert;

/**
 * 
 * @author anadal (u80067)
 * 29 sept 2026 9:03:07
 */
public class TestDiscoverPluginsIB {

    static {
        // Força que org.jboss.logging utilitzi el proveïdor de java.util.logging (JDK)
        // i carrega una configuració explícita perquè els logs es mostrin amb el
        // format estàndard de JBoss/JUL, per exemple:
        // sept. 29, 2026 8:23:22 A. M. org.fundaciobit...DiscoverPluginsIB <clinit>
        // INFO: ClassGraph Time Scan Classes: 223ms
        System.setProperty("org.jboss.logging.provider", "jdk");
        configureJulLogging();
    }

    /**
     * Carrega la configuració de {@code java.util.logging} (fitxer
     * {@code logging.properties} present al classpath) perquè els missatges
     * de {@link #log} es mostrin amb el format estàndard de JUL/JBoss.
     * Si no es troba el fitxer, es deixa la configuració per defecte de la JVM.
     */

    private static void configureJulLogging() {
        try (InputStream is = DiscoverPluginsIB.class.getResourceAsStream("/logging.properties")) {
            if (is != null) {
                LogManager.getLogManager().readConfiguration(is);
            }
        } catch (IOException | SecurityException e) {
            System.err.println("No s'ha pogut carregar logging.properties: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testInterfaceMicrosoft() throws Exception {

        List<Class<? extends IExportDataPlugin>> classesEsperades = new ArrayList<Class<? extends IExportDataPlugin>>();
        classesEsperades.add(ExcelExportData.class);
        classesEsperades.add(WordExportData.class);
        classesEsperades.add(OdtExportData.class);

        testSearchByInterfaceCommon(classesEsperades, IExportDataPlugin.class);

    }

    @org.junit.Test
    public void testInterfaceMyPlugin() throws Exception {

        List<Class<? extends IMyPlugin>> classesEsperades = new ArrayList<Class<? extends IMyPlugin>>();
        classesEsperades.add(MyPluginImpl.class);

        testSearchByInterfaceCommon(classesEsperades, IMyPlugin.class);

    }

    public <T> void testSearchByInterfaceCommon(List<Class<? extends T>> classesEsperades, Class<T> classe)
            throws Exception {

        // Utilitzam searchTypesByInterface, ja que és independent de IPluginIB
        Set<Class<? extends T>> conjunt;
        conjunt = DiscoverPluginsIB.searchTypesByInterface(classe);

        System.out.println("Classes que implementen " + classe.getSimpleName() + "(#" + conjunt.size() + "):");

        for (Class<?> c : conjunt) {
            System.out.println(" - " + c.getName());
        }

        Assert.assertTrue(
                "S'esperaven " + classesEsperades.size() + " classes que implementassin " + classe.getSimpleName()
                        + ", pero s'han trobat " + conjunt.size() + " classes.",
                conjunt.size() == classesEsperades.size());

        for (Class<?> c : classesEsperades) {
            Assert.assertTrue("El resultat de la cerca hauria de contenir la classe " + c.getName(),
                    conjunt.contains(c));
        }
    }

    @org.junit.Test
    public void testAnnotationsMicrosoft() throws Exception {

        List<Class<?>> classesEsperades = new ArrayList<Class<?>>();
        classesEsperades.add(ExcelExportData.class);
        classesEsperades.add(WordExportData.class);

        testAnnotationsCommon(MicrosoftPluginAnnotation.class, classesEsperades);

    }

    @org.junit.Test
    public void testAnnotationsPluginsIB() throws Exception {

        List<Class<?>> classesEsperades = new ArrayList<Class<?>>();
        classesEsperades.add(MyPluginImpl.class);

        testAnnotationsCommon(MyPlugin.class, classesEsperades);

    }

    private Set<Class<?>> testAnnotationsCommon(Class<? extends Annotation> annotation, List<Class<?>> classesEsperades)
            throws Exception {

        Set<Class<?>> conjunt;

        conjunt = DiscoverPluginsIB.getTypesAnnotatedWith(annotation);

        System.out.println("Classes amb l'anotació " + annotation.getSimpleName() + ":");
        for (Class<?> c : conjunt) {
            System.out.println(" - " + c.getName());
        }

        Assert.assertTrue(
                "Existeixen " + classesEsperades.size() + " classes amb l'anotació " + annotation.getSimpleName()
                        + " pero s'han trobat " + conjunt.size() + " classes.",
                conjunt.size() == classesEsperades.size());

        for (Class<?> c : classesEsperades) {
            Assert.assertTrue("El resultat de la cerca hauria de contenir la classe " + c.getName(),
                    conjunt.contains(c));
        }
        
        return conjunt;

    }



    public static void main(String[] args) {

        try {

            System.out.println("Inici de Test de DiscoverPluginsIB");

            TestDiscoverPluginsIB test = new TestDiscoverPluginsIB();
            test.testAnnotationsMicrosoft();
            test.testAnnotationsPluginsIB();

            test.testInterfaceMyPlugin();
            test.testInterfaceMicrosoft();

            System.out.println("Test de DiscoverPluginsIB finalitzat correctament");

        } catch (Exception e) {

            System.err.println("ERROR en tests de DiscoverPluginsIB");

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
