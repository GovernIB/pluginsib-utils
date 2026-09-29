package org.fundaciobit.pluginsib.utils.discoverplugins.test.samplemicrosoft;

/**
 * @author anadal(u80067)
 */
@MicrosoftPluginAnnotation
public class ExcelExportData implements IExportDataPlugin {

    @Override
    public String getName() {

        return "Excel";
    }

}
