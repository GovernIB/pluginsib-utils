package org.fundaciobit.pluginsib.utils.discoverplugins.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fundaciobit.pluginsib.utils.discoverplugins.DiscoverPluginsIB;
import org.junit.Assert;

/**
 * 
 * @author anadal
 * 
 */

public class Test {

    @org.junit.Test
    public void testInterface() throws Exception {

        Set<Class<? extends IExportDataPlugin>> conjunt;

        conjunt = DiscoverPluginsIB.getPluginsByInterface(IExportDataPlugin.class);

        Assert.assertFalse(
                "Error llista buida: si que existeixen classes que implementen " + IExportDataPlugin.class.getName(),
                conjunt.isEmpty());

        List<Class<? extends IExportDataPlugin>> classesEsperades = new ArrayList<Class<? extends IExportDataPlugin>>();
        classesEsperades.add(ExcelExportData.class);
        classesEsperades.add(WordExportData.class);
        classesEsperades.add(OdtExportData.class);

        for (Class<? extends IExportDataPlugin> c : classesEsperades) {
            Assert.assertTrue("El resultat de la cerca hauria de contenir la classe " + c.getName(),
                    conjunt.contains(c));
        }

    }

    @org.junit.Test
    public void testAnnotations() throws Exception {

        Set<Class<?>> conjunt;

        conjunt = DiscoverPluginsIB.getTypesAnnotatedWith(MicrosoftPluginAnnotation.class);

        List<Class<?>> classesEsperades = new ArrayList<Class<?>>();
        classesEsperades.add(ExcelExportData.class);
        classesEsperades.add(WordExportData.class);

        Assert.assertTrue(
                "Existeixen " + classesEsperades.size() + " classes amb l'anotació "
                        + MicrosoftPluginAnnotation.class.getName() + "(retornades " + conjunt.size() + ")",
                conjunt.size() == classesEsperades.size());

        for (Class<?> c : classesEsperades) {
            Assert.assertTrue("El resultat de la cerca hauria de contenir la classe " + c.getName(),
                    conjunt.contains(c));
        }

    }

    public void testAnnotation() throws Exception {

    }

    public static void main(String[] args) {

        try {

            Test test = new Test();
            test.testInterface();
            test.testAnnotations();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
