package org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemicrosoft;

/**
 * @author anadal(u80067)
 */
@MicrosoftPluginAnnotation
public class WordExportData implements IExportDataPlugin {

    @Override
    public String getName() {
        return "Word";
    }

}
