package org.fundaciobit.pluginsib.utils.rest;

import java.io.File;

import org.fundaciobit.pluginsib.utils.rest.adaptpom.AdaptApiClientPomToJboss72;

/**
 * 
 */
public class AdaptApiClientPomToJboss72Test {

    public static void main(String[] args) {

        String path = "D:\\dades\\dades\\CarpetesPersonals\\ProgramacioPortaFIB3\\portafib-2.2\\api-interna";

        try {
            AdaptApiClientPomToJboss72.adaptApiClientPomToJboss72(new File(path));
            System.out.println("Finalitzat");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
