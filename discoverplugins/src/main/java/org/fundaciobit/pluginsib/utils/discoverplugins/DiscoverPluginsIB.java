package org.fundaciobit.pluginsib.utils.discoverplugins;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.fundaciobit.pluginsib.core.v3.IPluginIB;

import org.jboss.logging.Logger;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

/**
 * Classe d'utilitat per descobrir en temps d'execució, mitjançant l'escaneig
 * del classpath amb <a href="https://github.com/classgraph/classgraph">ClassGraph</a>,
 * classes concretes que implementen una determinada interfície o que estan
 * anotades amb una anotació concreta.
 * <p>
 * L'escaneig del classpath es fa una única vegada (bloc estàtic) i el
 * resultat es reutilitza ({@link #scanResult}) en totes les cerques
 * posteriors per evitar el cost de tornar a escanejar el classpath.
 *
 * @author anadal
 */
public class DiscoverPluginsIB {

    protected static final Logger log = Logger.getLogger(DiscoverPluginsIB.class);

    /** Resultat de l'escaneig del classpath (bytecode, sense carregar classes) fet per ClassGraph. */
    protected static final ScanResult scanResult;

    static {
        long start = System.currentTimeMillis();
        scanResult = new ClassGraph().enableClassInfo().enableAnnotationInfo().scan();
        log.info("ClassGraph Time Scan Classes: " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * Constructor privat: aquesta classe només conté mètodes estàtics i no
     * s'ha d'instanciar.
     */
    private DiscoverPluginsIB() {
    }

    /**
     * Cerca totes les classes concretes (no abstractes ni interfícies) del
     * classpath que implementen la interfície {@link IPluginIB} indicada.
     * <p>
     * Aquest mètode és equivalent a {@link #searchTypesByInterface(Class)}
     * però restringit a interfícies que estenen {@link IPluginIB}.
     *
     * @param <T> tipus de la interfície de plugin cercada
     * @param interficie interfície (que ha d'estendre {@link IPluginIB}) que
     *            han d'implementar les classes cercades
     * @return conjunt de classes concretes que implementen la interfície indicada
     * @throws Exception si hi ha algun problema en carregar alguna de les classes trobades
     */
    public static <T extends IPluginIB> Set<Class<? extends T>> getPluginsByInterface(
            Class<? extends IPluginIB> interficie) throws Exception {

        List<Class<?>> clases;
        clases = scanResult.getClassesImplementing(interficie).loadClasses();

        Set<Class<? extends T>> plugins = new HashSet<Class<? extends T>>();

        for (Class<?> clase : clases) {
            if (clase.isInterface() || Modifier.isAbstract(clase.getModifiers())) {
                log.debug("Ignorada classe " + clase.getName() + ": es abstact o interface.");
            } else {

                if (scanResult.getInterfaces(clase).loadClasses().contains(IPluginIB.class)) {
                    plugins.add((Class<T>) clase);
                } else {
                    log.debug("Ignorada classe " + clase.getName() + ": no implementa interface "
                            + IPluginIB.class.getName());
                }
            }
        }
        return plugins;
    }

    /**
     * Igual que {@link #getPluginsByInterface(Class)} però independent de
     * {@link IPluginIB}: es pot passar qualsevol interfície.
     * <p>
     * Cerca totes les classes concretes (no abstractes ni interfícies) del
     * classpath que implementen directament la interfície indicada.
     *
     * @param <T> tipus de la interfície cercada
     * @param interficie interfície que han d'implementar les classes cercades
     * @return conjunt de classes concretes que implementen la interfície indicada
     * @throws Exception si hi ha algun problema en carregar alguna de les classes trobades
     */
    public static <T> Set<Class<? extends T>> searchTypesByInterface(Class<T> interficie) throws Exception {

        List<Class<?>> clases;
        clases = scanResult.getClassesImplementing(interficie).loadClasses();

        Set<Class<? extends T>> plugins = new HashSet<Class<? extends T>>();

        for (Class<?> clase : clases) {
            if (clase.isInterface() || Modifier.isAbstract(clase.getModifiers())) {
                log.debug("Ignorada classe " + clase.getName() + ": es abstact o interface.");
            } else {

                if (scanResult.getInterfaces(clase).loadClasses().contains(interficie)) {
                    plugins.add((Class<T>) clase);
                } else {
                    log.debug(
                            "Ignorada classe " + clase.getName() + ": no implementa interface " + interficie.getName());
                }
            }
        }
        return plugins;
    }

    /**
     * Cerca totes les classes concretes (no abstractes ni interfícies) del
     * classpath que estan anotades directament amb l'anotació indicada.
     *
     * @param annotation anotació que han de tenir les classes cercades
     * @return conjunt de classes concretes anotades amb l'anotació indicada
     * @throws Exception si hi ha algun problema en carregar alguna de les classes trobades
     */
    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) throws Exception {

        List<Class<?>> clases;
        clases = scanResult.getClassesWithAnnotation(annotation).loadClasses();

        Set<Class<?>> plugins = new HashSet<Class<?>>();

        for (Class<?> clase : clases) {
            if (clase.isInterface() || Modifier.isAbstract(clase.getModifiers())) {
                log.debug("Ignorada classe " + clase.getName() + ": es abstact o interface.");
            } else {
                plugins.add(clase);
            }
        }
        return plugins;
    }

    /**
     * Cerca totes les classes concretes (no abstractes ni interfícies) del
     * classpath que són subtipus (implementen o estenen, de forma directa)
     * el tipus indicat.
     * <p>
     * A diferència de {@link #searchTypesByInterface(Class)}, no comprova
     * que la interfície aparegui a la llista d'interfícies immediates de la
     * classe, sinó que confia directament en el resultat de ClassGraph.
     *
     * @param <T> tipus (classe o interfície) cercat
     * @param interficie classe o interfície de la qual es volen cercar subtipus
     * @return conjunt de classes concretes que són subtipus de {@code interficie}
     * @throws Exception si hi ha algun problema en carregar alguna de les classes trobades
     */
    public static <T extends Object> Set<Class<? extends T>> getSubTypesOfInterface(Class<T> interficie)
            throws Exception {

        List<Class<?>> clases;
        clases = DiscoverPluginsIB.scanResult.getClassesImplementing(interficie).loadClasses();

        Set<Class<? extends T>> plugins = new HashSet<Class<? extends T>>();

        for (Class<?> clase : clases) {
            if (clase.isInterface() || Modifier.isAbstract(clase.getModifiers())) {
                log.debug("Ignorada classe " + clase.getName() + ": es abstact o interface.");
            } else {
                plugins.add((Class<T>) clase);
            }
        }
        return plugins;
    }



}
