package org.fundaciobit.pluginsib.utils.discoverplugins;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fundaciobit.pluginsib.core.v3.IPluginIB;
import org.jboss.logging.Logger;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

/**
 * 
 * @author anadal
 * 
 * @param <T>
 * @param interficie
 * @return
 * @throws Exception
 */
public class DiscoverPluginsIB {

    protected static final Logger log = Logger.getLogger(DiscoverPluginsIB.class);

    protected static final ScanResult scanResult;

    static {
        long start = System.currentTimeMillis();
        scanResult = new ClassGraph().enableClassInfo().enableAnnotationInfo().scan();
        log.info("ClassGraph Time Scan Classes: " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 
     */
    private DiscoverPluginsIB() {
    }

    /**
     * 
     * @param <T>
     * @param interficie
     * @return
     * @throws Exception
     */
    public static <T extends IPluginIB> Set<Class<? extends T>> getPluginsByInterface(Class<T> interficie)
            throws Exception {

        List<Class<?>> clases;
        clases = scanResult.getClassesImplementing(interficie).loadClasses();

        Set<Class<? extends T>> plugins = new HashSet<Class<? extends T>>();

        for (Class<?> clase : clases) {
            if (clase.isInterface() || Modifier.isAbstract(clase.getModifiers())) {
                log.info("Ignorada classe " + clase.getName() + ": es abstact o interface.");
            } else {

                if (scanResult.getInterfaces(clase).loadClasses().contains(IPluginIB.class)) {
                    plugins.add((Class<T>) clase);
                } else {
                    log.info("Ignorada classe " + clase.getName() + ": no implementa interface "
                            + IPluginIB.class.getName());
                }
            }
        }
        return plugins;
    }
    

    /**
     * 
     * @param annotation
     * @return
     * @throws Exception
     */
    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) throws Exception {

        List<Class<?>> clases;
        clases = scanResult.getClassesWithAnnotation(annotation).loadClasses();

        Set<Class<?>> plugins = new HashSet<Class<?>>();

        for (Class<?> clase : clases) {
            if (clase.isInterface() || Modifier.isAbstract(clase.getModifiers())) {
                log.info("Ignorada classe " + clase.getName() + ": es abstact o interface.");
            } else {
                plugins.add(clase);
            }
        }
        return plugins;
    }
    
    /**
     * 
     * @param <T>
     * @param interficie
     * @return
     * @throws Exception
     */
    public static <T extends Object> Set<Class<? extends T>> getSubTypesOfInterface(Class<T> interficie)
            throws Exception {

        List<Class<?>> clases;
        clases = DiscoverPluginsIB.scanResult.getClassesImplementing(interficie).loadClasses();

        Set<Class<? extends T>> plugins = new HashSet<Class<? extends T>>();

        for (Class<?> clase : clases) {
            if (clase.isInterface() || Modifier.isAbstract(clase.getModifiers())) {
                log.info("Ignorada classe " + clase.getName() + ": es abstact o interface.");
            } else {
                plugins.add((Class<T>) clase);
            }
        }
        return plugins;
    }

}
