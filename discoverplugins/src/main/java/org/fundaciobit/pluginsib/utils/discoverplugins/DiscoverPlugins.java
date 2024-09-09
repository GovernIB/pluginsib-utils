package org.fundaciobit.pluginsib.utils.discoverplugins;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.fundaciobit.pluginsib.core.v3.IPluginIB;
import org.jboss.logging.Logger;

/**
 * 
 * @author anadal
 * @Deprecated Use DiscoverPluginsIB
 */
@Deprecated
public class DiscoverPlugins {

    protected static final Logger log = Logger.getLogger(DiscoverPlugins.class);

    public static <T extends Object> Set<Class<? extends T>> getSubTypesOfInterface(Class<T> interficie)
            throws Exception {

        return DiscoverPluginsIB.getSubTypesOfInterface(interficie);
    }

    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation,
            ClassLoader... classLoaders) throws Exception {

        return DiscoverPluginsIB.getTypesAnnotatedWith(annotation);
    }

    public static <T extends IPluginIB> Set<Class<? extends T>> getPluginsByInterface(Class<T> interficie)
            throws Exception {

        return DiscoverPluginsIB.getPluginsByInterface(interficie);
    }

}