package org.fundaciobit.pluginsib.utils.signature;

/**
 * 
 * @author anadal
 * 5 nov 2024 15:26:19
 */
public interface SignatureConstants {

    /**
     * =======  SIGNATURE TYPES ==============
     */

    public static final String SIGNTYPE_CMS = "CMS";
    public static final String SIGNTYPE_CAdES = "CAdES";
    public static final String SIGNTYPE_XAdES = "XAdES";
    public static final String SIGNTYPE_ODF = "ODF";
    public static final String SIGNTYPE_PDF = "PDF"; // ?????
    public static final String SIGNTYPE_PAdES = "PAdES";
    public static final String SIGNTYPE_OOXML = "OOXML";
    public static final String SIGNTYPE_XML_DSIG = "XML_DSIG";

    public static final String SIGNTYPE_PKCS7 = "PKCS7";

    public static final String SIGNTYPE_XML_TST = "XML_TST"; // OASIS  XML-encoded time-stamps.

    /**
     * ============  SIGNATURE FORMATS ============
     */
    // Veure https://ec.europa.eu/digital-building-blocks/DSS/webapp-demo/doc/dss-documentation.html#Packaging
    // veure https://ec.europa.eu/digital-building-blocks/DSS/webapp-demo/doc/dss-documentation.html#SignatureProfileGuide

    /** El fitxer de dades resultant inclou la firma: PDF, ODT, ... */
    public static final int SIGN_MODE_ATTACHED_ENVELOPED = 0;

    /** El fitxer resultant serà la firma que incloura les dades originals */
    public static final int SIGN_MODE_ATTACHED_ENVELOPING = 3;

    /** El fitxer de firma no inclourà les dades: per separat trobarem un fitxer de firma i el fitxer original */
    public static final int SIGN_MODE_DETACHED = 1;

    /** Firma especial XAdES en que la firma i les dades estan al mateix nivell dins de l'XML: ni la firma inclou les dades ni les dades inclouen la firma */
    public static final int SIGN_MODE_INTERNALLY_DETACHED = 4;

    /** Firma especial XAdES també anomenada XAdES-Manifest
     * 
     * https://www.linkedin.com/pulse/art%C3%ADculo-t%C3%A9cnico-firmas-electr%C3%B3nicas-xades-de-con-tom%C3%A1s-garc%C3%ADa-mer%C3%A1s/
     * https://administracionelectronica.gob.es/ctt/resources/Soluciones/323/Descargas/Sistema%20de%20referenciacion%20de%20documentos%20en%20las%20AAPP-v11.docx?idIniciativa=323&idElemento=16433
     * https://www.w3.org/TR/2000/WD-xmldsig-core-20000510/#sec-o-Manifest
     */
    public static final int SIGN_MODE_EXTERNALLY_DETACHED = 5;

    /**
     * ============  SIGNATURE PROFILES ============
     */

    /**
     * AdES-BES:Basic Electronic Signature.Es el formato más básico del estándar y
     * constituye la base para la extensión de la firma hacia otros formatos más
     * complejos.
     */
    public static final String SIGNPROFILE_BES = "AdES-BES";

    //
    /**
     * AdES-EPES:(BES + Politica de firma) Explicit Policy Electronic Signature.Es
     * una variante del formato AdES–BES que contiene información acerca de la
     * política de firma utilizada.
     */
    public static final String SIGNPROFILE_EPES = "AdES-EPES";

    /** AdES-T:Timestamp.Permite la adición de un sello de tiempo sobre la firma. */
    public static final String SIGNPROFILE_T = "AdES-T";

    /**
     * AdES-C:Complete. Añade referencias sobre los certificados utilizados y las
     * listas de revocación asociadas a los mismos.
     */
    public static final String SIGNPROFILE_C = "AdES-C";

    /**
     * AdES-X:Extended. Añade sellos de tiempo sobre las referencias introducidas
     * por los formatos AdES–C. Este formato,a su vez,puede dividirse en 2, en
     * función del contenido sellado por los sellos de tiempo: X1 y X2
     */
    public static final String SIGNPROFILE_X = "AdES-X";

    /**
     * AdES-X1: El sello de tiempo se calcula sobre la estructura
     * AdES-C(formatopordefecto).
     */
    public static final String SIGNPROFILE_X1 = "AdES-X1";

    /**
     * AdES-X2:El sello de tiempo se calcula sobre las referencias de los
     * certificados y evidencias de revocación.
     */
    public static final String SIGNPROFILE_X2 = "AdES-X2";

    /**
     * AdES-XL: ExtendedLong-Term. Añade los propios certificados (cadenas de
     * certificación) y listas de revocación.A su vez,al igual que con el formato
     * AdES-X, puede subdividirse en 2 formatos: XL1 y XL2
     */
    public static final String SIGNPROFILE_XL = "AdES-XL";

    /**
     * AdES-XL1:Si se ha tomado como base para su generación una firma con formato
     * AdES-X1(formatopordefecto).
     */
    public static final String SIGNPROFILE_XL1 = "AdES-XL1";

    /**
     * AdES-XL2: Si se ha tomado como base para sugeneración una firma con formato
     * AdES-X2.
     */
    public static final String SIGNPROFILE_XL2 = "AdES-XL2";

    /**
     * AdES-A:Archival.Permite la posibilidad de añadir periódicamente sellos de
     * tiempo sobre la firma para alargar lavigencia de la misma.
     *
     */
    public static final String SIGNPROFILE_A = "AdES-A";

    /**
     * PAdES-LTV: Long term validation (Segell temps): NOMES PER PADES. Además de
     * tener al menos una firma con el formato de los anteriores contenida en el
     * documento PDF, debe poseer al menos un diccionario de sello de tiempo que
     * selle todos los datos contenidos en el documento PDF. Adicionalmente, puede
     * incluir un diccionario DSS con todos los elementos que permiten la
     * validación de las firmas contenidas en el documentoPDF.
     */
    public static final String SIGNPROFILE_PADES_LTV = "PAdES-LTV";

    /**
     * PAdES-Basic: Evolución de la firma PDF recogida en la ISO32000-1 obligando
     * a que la firma electrónica sea CMS.
     */
    public static final String SIGNPROFILE_PADES_BASIC = "PAdES-Basic";

    /**
     * ============  SIGNATURE ALGORITHMS ============
     */

    public static final String SIGN_ALGORITHM_SHA1 = "SHA-1";
    public static final String SIGN_ALGORITHM_SHA256 = "SHA-256";
    public static final String SIGN_ALGORITHM_SHA384 = "SHA-384";
    public static final String SIGN_ALGORITHM_SHA512 = "SHA-512";
}
