/*
 * Copyright 2016 gdeignacio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fundaciobit.pluginsib.utils.commons;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Strings {

    /**
     * Función que elimina acentos y caracteres especiales de
     * una cadena de texto.
     * @param input
     * @return cadena de texto limpia de acentos y caracteres especiales.
     */
    public static String quitarAcentos(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }

    /**
     * Función que elimina los puntos de una
     * una cadena de texto.
     * @param input
     * @return cadena de texto limpia de puntos
     */
    public static String quitarPuntos(String input) {
        String output = input.replace( '.', ' ' );
        return output;
    }

    public static String quitarAcentosPuntos(String input) {
        String output = quitarAcentos(input);
        String output2 = quitarPuntos(output);
        return output2;
    }

    /**
     * Función que elimina acentos y caracteres especiales de
     * una cadena de texto.
     * @param input
     * @return cadena de texto limpia de acentos y caracteres especiales.
     */
    public static String quitarAcentos2(String input) {
        // Descomposición canónica
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Nos quedamos únicamente con los caracteres ASCII
        Pattern pattern = Pattern.compile("\\P{ASCII}+");
        return pattern.matcher(normalized).replaceAll("");
    }//remove2


/*    public String[] nombre(String fullName) {
        StringTokenizer tokens = new StringTokenizer(fullName);
        String[] arrayNombre = new String[3];

        while(tokens.hasMoreTokens()){
            System.out.println(tokens.nextToken());
        }

        return ;
    }*/

    public static String parseUrl(String url) {
        String nombreContexto = "firma";
        if(url.contains(nombreContexto)){
            int position = url.indexOf(nombreContexto);
            return url.substring(0,position+nombreContexto.length());
        } else {
            return url;
        }
    }

    public static String eliminarUltimoCaracter(String source) {
        if(source.length()>0) {
            return source.substring(0,source.length()-1);
        } else {
            return "";
        }
    }


}
