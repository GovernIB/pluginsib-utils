package org.fundaciobit.pluginsib.utils.rest;

import java.io.File;

import org.fundaciobit.pluginsib.utils.rest.pomadaptertojboss72.AdaptApiClientPomToJboss72;

/**
 * 
 */
public class AdaptApiClientPomToJboss72Test {

    public static void main(String[] args) {

        // String path = "D:\\dades\\dades\\CarpetesPersonals\\ProgramacioPortaFIB3\\portafib-2.2\\api-interna";
         String path = "D:\\dades\\dades\\CarpetesPersonals\\ProgramacioPortaFIB3\\genapp-2.0\\demo\\generat\\demogenapp-api-interna";

        try {
            AdaptApiClientPomToJboss72.adaptApiClientPomToJboss72(new File(path));
            System.out.println("Finalitzat");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
