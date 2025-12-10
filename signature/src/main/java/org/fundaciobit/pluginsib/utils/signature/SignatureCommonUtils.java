package org.fundaciobit.pluginsib.utils.signature;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.cms.CMSSignedData;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author anadal
 * 5 nov 2024 15:31:21
 */
public class SignatureCommonUtils implements SignatureConstants {

    private static final Logger log = Logger.getLogger(SignatureCommonUtils.class);

    /**
     * 
     * @param signMode
     * @return
     */
    public static String signModeToString(int signMode) {

        // XAdES internally detached
        if (signMode == SignatureConstants.SIGN_MODE_INTERNALLY_DETACHED) { // = "implicit/internally_detached";
            /** Firma especial XAdES en que la firma i les dades estan al mateix nivell dins de l'XML: ni la firma inclou les dades ni les dades inclouen la firma */
            return "SIGN_MODE_INTERNALLY_DETACHED(4)";
        }

        /** La firma està continguda dins del document: PADES, ODT, OOXML */
        if (signMode == SignatureConstants.SIGN_MODE_ATTACHED_ENVELOPED) { // = "implicit_enveloped/attached";
            /** El fitxer de dades resultant inclou la firma: PDF, ODT, ... */
            return "SIGN_MODE_ATTACHED_ENVELOPED(0)";
        }

        /** La firma conté al document: Xades ATTACHED */
        if (signMode == SignatureConstants.SIGN_MODE_ATTACHED_ENVELOPING) { // = "implicit_enveloping/attached";
            /** El fitxer resultant serà la firma que incloura les dades originals */
            return "SIGN_MODE_ATTACHED_ENVELOPING(3)";
        }

        /**
         * El document està forà de la firma: xades detached i cades detached
         */
        if (signMode == SignatureConstants.SIGN_MODE_DETACHED) { // = "explicit/detached";

            /** El fitxer de firma no inclourà les dades: per separat trobarem un fitxer de firma i el fitxer original */
            return "SIGN_MODE_DETACHED(" + SignatureConstants.SIGN_MODE_DETACHED + ")"; // 1
        }

        /**
         * Cas específic de Xades externally detached
         */
        if (signMode == SignatureConstants.SIGN_MODE_EXTERNALLY_DETACHED) {//  = "explicit/externally_detached";
            return "SIGN_MODE_EXTERNALLY_DETACHED(5)";
        }

        return "UNKONWN_SIGNMODE(" + signMode + ")";

    };

    /**
     * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-INTEGR@
     */
    public static int getSignMode(String signType, final byte[] signData) throws Exception {
        int signFormat;
        if (SIGNTYPE_CMS.equals(signType)) { // "CMS";
            // TODO Això no se si es correcte !!!!!!!
            try {
                signFormat = getCAdESMode(signData);
            } catch (Throwable th) {
                String msg = "Error intentant obtenir el format d'una firma CMS emprant el mètode getCAdESFormat(): "
                        + th.getMessage();
                log.error(msg, th);
                throw new Exception(msg, th);
            }
        } else if (SIGNTYPE_CAdES.equals(signType)) { // "CAdES";
            signFormat = getCAdESMode(signData);
        } else if (SIGNTYPE_XAdES.equals(signType)) { // "XAdES";
            final boolean isInputParams = false;
            signFormat = getXAdESMode(signData, isInputParams);
        } else if (SIGNTYPE_ODF.equals(signType)) { // "ODF";
            signFormat = SIGN_MODE_ATTACHED_ENVELOPED;
        } else if (SIGNTYPE_PDF.equals(signType)) { // "PDF"; // ?????
            signFormat = SIGN_MODE_ATTACHED_ENVELOPED;
        } else if (SIGNTYPE_PAdES.equals(signType)) { // "PAdES";
            signFormat = SIGN_MODE_ATTACHED_ENVELOPED;
        } else if (SIGNTYPE_OOXML.equals(signType)) { // "OOXML";
            signFormat = SIGN_MODE_ATTACHED_ENVELOPED;
        } else if (SIGNTYPE_XML_DSIG.equals(signType)) { // "XML_DSIG";
            // TODO Això no se si es correcte !!!!!!!
            try {
                final boolean isInputParams = false;
                signFormat = getXAdESMode(signData, isInputParams);
            } catch (Throwable th) {
                String msg = "Error intentant obtenir el format d'una firma XML_DSIG emprant el"
                        + " mètode getXAdESFormat(): " + th.getMessage();
                log.error(msg, th);

                throw new Exception(msg, th);
            }
        } else {
            String msg = "Error intentant trobar el format de una firma amb tipus desconegut: " + signType;
            log.error(msg, new Exception());
            throw new Exception(msg);
        }
        return signFormat;
    }

    
    // ====================================================================================
    // ====================================================================================
    // ===================================== C A D E S ====================================
    // ====================================================================================
    // ====================================================================================
    
    
    
    /**
     * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-INTEGR@
     */
    public static int getCAdESMode(byte[] signature) throws Exception {

        CMSSignedData cmsSignedData = new CMSSignedData(signature);
        ContentInfo contentInfo = cmsSignedData.toASN1Structure();
        SignedData signedData = SignedData.getInstance(contentInfo.getContent());

        boolean isImplicit = false;
        if (signedData.getEncapContentInfo() != null) {
            isImplicit = signedData.getEncapContentInfo().getContent() != null;
        }
        
        if (isImplicit) {
            //  "CAdES attached/implicit signature";
            return SIGN_MODE_ATTACHED_ENVELOPING;
        } else {
            //  "CAdES detached/explicit signature"
            return SIGN_MODE_DETACHED;
        }
    }

    
    
    
    // ====================================================================================
    // ====================================================================================
    // ===================================== X A D E S ====================================
    // ====================================================================================
    // ====================================================================================


    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY;

    static {
        DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
        DOCUMENT_BUILDER_FACTORY.setNamespaceAware(true);

    }

    

    /**
     * AQUEST MÈTODE ESTA DUPLICAT AL PLUGIN-INTEGR@
     */
    public static  int getXAdESMode(byte[] signature, boolean inputXML) throws Exception {

        Document eSignature = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(new ByteArrayInputStream(signature));
        Element element = eSignature.getDocumentElement();

        String rootName = element.getNodeName();
        if (rootName.equalsIgnoreCase("ds:Signature") || rootName.equals("ROOT_COSIGNATURES")) {
            //  "XAdES Enveloping"
            return SIGN_MODE_ATTACHED_ENVELOPING;
        }
        NodeList signatureNodeLs = eSignature.getElementsByTagName("ds:Manifest");
        if (signatureNodeLs.getLength() > 0) {
            //  "XAdES Externally Detached
            return SIGN_MODE_EXTERNALLY_DETACHED;
        }
        NodeList signsList = eSignature.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
        if (signsList.getLength() == 0) {
            String msg = "No s'ha trobat cap node de firma dins de l'XML proporcionat: tag 'Signature' "
                    + "amb ds 'http://www.w3.org/2000/09/xmldsig#'. No es pot determinar el mode de signatura.";
            log.error(msg);
            log.error("XML sense node de firma:\n" + new String(signature));
            
            throw new Exception("XS003 " + msg); // TODO
        }
        Node signatureNode = signsList.item(0);

        XMLSignature xmlSignature;
        try {
            xmlSignature = new XMLSignatureElement((Element) signatureNode).getXMLSignature();
        } catch (MarshalException e) {
            throw new Exception("XS005", e); // TODO
        }

        List<?> references = xmlSignature.getSignedInfo().getReferences();
        for (Object reference : references) {
            if (!"".equals(((Reference) reference).getURI()))
                continue;
            //  "XAdES Enveloped"
            return SIGN_MODE_ATTACHED_ENVELOPED;
        }

        if (!inputXML) {
            /* https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/AOXAdESSigner.java */
            try {
                final Element signatureElement = /*XAdESUtil.*/getFirstSignatureElement(element);
                final List<Element> dataReferenceList = /*XAdESUtil.*/getSignatureDataReferenceList(signatureElement);
                if (isSignatureElementInternallyDetached(element, dataReferenceList)) {
                    //  "XAdES Internally Detached"
                    return SIGN_MODE_INTERNALLY_DETACHED;
                }
            } catch (Throwable th) {
                System.err.println(th.getMessage());
                th.printStackTrace();
            }
        }

        //  "XAdES Detached"
        return SIGN_MODE_DETACHED;
    }

    /**
     * https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESUtil.java
     * Obtiene la primera firma encontrada en el elemento XML.
     * @param element Elemento XML.
     * @return Primera firma encontrada o nulo si no se encuentra ninguna.
     */
    private static Element getFirstSignatureElement(final Element element) {

        if (element == null) {
            return null;
        }

        // Localizamos el primer nodo de firma
        Element signatureElement = null;
        if (/*XMLConstants.*/TAG_SIGNATURE.equals(element.getLocalName())) {
            signatureElement = element;
        } else {
            final NodeList signatures = element.getElementsByTagNameNS(/*XMLConstants.*/DSIGNNS,
                    /*XMLConstants.*/TAG_SIGNATURE);
            if (signatures.getLength() > 0) {
                signatureElement = (Element) signatures.item(0);
            }
        }
        return signatureElement;
    }

    /**https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-core-xml/src/main/java/es/gob/afirma/signers/xml/XMLConstants.java
    *  Nombre del nodo de firma ("Signature"). */
    public static final String TAG_SIGNATURE = "Signature"; //$NON-NLS-1$

    /**https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-core-xml/src/main/java/es/gob/afirma/signers/xml/XMLConstants.java 
     * URI que define el NameSpace de firma XMLdSig (Compatible XAdES). */
    public static final String DSIGNNS = "http://www.w3.org/2000/09/xmldsig#"; //$NON-NLS-1$

    /**https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-core-xml/src/main/java/es/gob/afirma/signers/xml/XMLConstants.java 
     * Nombre del nodo "SignedInfo" con la informaci&oacute;n de firma. */
    public static final String TAG_SIGNEDINFO = "SignedInfo"; //$NON-NLS-1$

    /**https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-core-xml/src/main/java/es/gob/afirma/signers/xml/XMLConstants.java  
     * Nombre del nodo de las referencias de firma. */
    public static final String TAG_REFERENCE = "Reference"; //$NON-NLS-1$

    /**
     * https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESUtil.java
     * Obtiene un listado con las referencias a datos de la firma proporcionada.
     * Se considera referencia a datos toda aquella que no sea referencia al
     * SignedProperties ni a un objeto KeyInfo de la firma.
     * @param signatureElement Elemento XML "Signature" de firma.
     * @return Listado con las referencias a datos encontradas.
     */
    private static List<Element> getSignatureDataReferenceList(final Element signatureElement) {

        // Obtemos el nodo SignedInfo
        final Element signedInfoElement = getSignedInfo(signatureElement);
        if (signedInfoElement == null) {
            return null;
        }

        // Obtenemos las referencias declaradas en la firma
        final NodeList references = signedInfoElement.getElementsByTagNameNS(/*XMLConstants.*/DSIGNNS,
                /*XMLConstants.*/TAG_REFERENCE);

        // Omitimos del listado la referencia a los atributos firmados
        final List<Element> dataReferences = new ArrayList<>();
        for (int i = 0; i < references.getLength(); i++) {
            final Element reference = (Element) references.item(i);
            final String type = reference.getAttribute("Type"); //$NON-NLS-1$
            if (type != null && !type.isEmpty()) {
                if (!/*XAdESUtil.*/isSignedPropertiesType(type)) {
                    dataReferences.add(reference);
                }
            }
            // Si no se establecio el tipo de referencia, lo comprobamos a partir de la URI
            else {
                final String uri = reference.getAttribute("URI"); //$NON-NLS-1$

                // Si es una referencia interna, comprobamos que no sea el KeyInfo o el SignedProperties
                if (uri != null && uri.startsWith("#")) { //$NON-NLS-1$
                    final String elementId = uri.substring(1);
                    final Node referencedNode = /*XAdESUtil.*/findElementById(elementId, signatureElement, false);

                    // Si el nodo referenciado no esta dentro del nodo de firma, es que es una referencia
                    // externa a datos
                    if (referencedNode == null) {
                        dataReferences.add(reference);
                    }
                    // Si no, comprobamos que no sea una referencia al KeyInfo o al SignedProperties
                    else {
                        final String nodeName = referencedNode.getLocalName();
                        if (!nodeName.equals("KeyInfo") && !nodeName.equals("SignedProperties")) { //$NON-NLS-1$ //$NON-NLS-2$
                            dataReferences.add(reference);
                        }
                    }
                }
                // Cualquier referencia no interna hay que firmarla
                else {
                    dataReferences.add(reference);
                }
            }
        }

        return dataReferences;
    }

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESConstants.java
     * URI que define el espacio d nombre de XAdES sin indicar la versi&oacute;n. */
    public static final String NAMESPACE_XADES_NO_VERSION = "http://uri.etsi.org/01903#"; //$NON-NLS-1$

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESConstants.java
     * URI que define el espacio de nombres de XAdES v1.1.1. */
    public static final String NAMESPACE_XADES_1_1_1 = "http://uri.etsi.org/01903/v1.1.1#"; //$NON-NLS-1$

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESConstants.java
     * URI que define el espacio de nombres de XAdES v1.2.2. */
    public static final String NAMESPACE_XADES_1_2_2 = "http://uri.etsi.org/01903/v1.2.2#"; //$NON-NLS-1$

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESConstants.java
     * URI que define el espacio de nombres de XAdES v1.3.2. */
    public static final String NAMESPACE_XADES_1_3_2 = "http://uri.etsi.org/01903/v1.3.2#"; //$NON-NLS-1$

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESConstants.java
     * URI que define el espacio de nombres de XAdES v1.4.1. */
    public static final String NAMESPACE_XADES_1_4_1 = "http://uri.etsi.org/01903/v1.4.1#"; //$NON-NLS-1$

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESConstants.java */
    public static final String TAG_SIGNED_PROPERTIES = "SignedProperties"; //$NON-NLS-1$

    /** URI con referencia a SignedProperties que define el espacio de nombres de XAdES sin indicar versi&oacute;n. */
    static final String NAMESPACE_XADES_NO_VERSION_SIGNED_PROPERTIES = NAMESPACE_XADES_NO_VERSION
            + TAG_SIGNED_PROPERTIES;

    /** URI con referencia a SignedProperties que define el espacio de nombres de XAdES v1.2.2. */
    static final String NAMESPACE_XADES_1_2_2_SIGNED_PROPERTIES = NAMESPACE_XADES_1_2_2 + TAG_SIGNED_PROPERTIES;

    /** URI que define el espacio de nombres de XAdES v1.2.2. */
    static final String NAMESPACE_XADES_1_3_2_SIGNED_PROPERTIES = NAMESPACE_XADES_1_3_2 + TAG_SIGNED_PROPERTIES;

    /** URI que define el espacio de nombres de XAdES v1.4.1. */
    static final String NAMESPACE_XADES_1_4_1_SIGNED_PROPERTIES = NAMESPACE_XADES_1_4_1 + TAG_SIGNED_PROPERTIES;

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESUtil.java */
    private static final String[] SIGNED_PROPERTIES_TYPES = new String[] {
            /*XAdESConstants.*/NAMESPACE_XADES_NO_VERSION_SIGNED_PROPERTIES,
            /*XAdESConstants.*/NAMESPACE_XADES_1_2_2_SIGNED_PROPERTIES,
            /*XAdESConstants.*/NAMESPACE_XADES_1_3_2_SIGNED_PROPERTIES,
            /*XAdESConstants.*/NAMESPACE_XADES_1_4_1_SIGNED_PROPERTIES };

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESUtil.java
     * Indica si un tipo se corresponde con el que se debe declarar en la referencia a las
     * propiedades firmadas de una firma.
     * @param type Tipo declarado.
     * @return {@code true} si es un tipo SignedProperties, {@code false} en caso contrario.
     */
    private static boolean isSignedPropertiesType(final String type) {
        for (final String signedPropertiesType : SIGNED_PROPERTIES_TYPES) {
            if (signedPropertiesType.equals(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESUtil.java
     * Obtiene el nodo SignedInfo de un elemento de firma individual.
     * @param signature Elemento de firma.
     * @return Elemento SignedInfo o {@code null} si no se encuentra.
     */
    private static Element getSignedInfo(final Element signature) {

        Element signedInfoElement = null;
        final NodeList childs = signature.getChildNodes();
        for (int i = 0; i < childs.getLength() && signedInfoElement == null; i++) {
            if (childs.item(i).getNodeType() == Node.ELEMENT_NODE
                    && /*XMLConstants.*/DSIGNNS.equals(childs.item(i).getNamespaceURI())
                    && /*XMLConstants.*/TAG_SIGNEDINFO.equals(childs.item(i).getLocalName())) {
                signedInfoElement = (Element) childs.item(i);
            }
        }
        return signedInfoElement;
    }

    /**
     * https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESUtil.java
     * Indica si la firma a la que pertenecen las referencias usadas es internally detached.
     * @param docElement Elemento ra&iacute;z del XML.
     * @param references Referencias a datos encontrados en la firma.
     * @return {@code true} si la firma es internally detached, {@code false} en caso contrario.
     */
    private static boolean isSignatureElementInternallyDetached(final Element docElement,
            final List<Element> references) {

        if (docElement == null || references == null) {
            return false;
        }

        // La consideraremos internally detached si alguno de los datos referenciados esta contenido dentro
        // del XML padre, sin entrar en ninguna firma.
        // NOTA: Una cofirma de una firma enveloping podria considerarse
        // internally detached, ya que tendria una referencia a los datos contenidos en la otra firma, que es
        // externa a ella misma. Se omite la busqueda en todas las firmas para omitir, ademas de otros posibles,
        // este caso de uso
        for (int i = 0; i < references.size(); i++) {
            final String uri = references.get(i).getAttribute("URI"); //$NON-NLS-1$
            if (uri != null && uri.startsWith("#")) { //$NON-NLS-1$
                final Node referencedNode = /*XAdESUtil.*/findElementById(uri.substring(1), docElement, true);
                // Si los datos referenciados estan contenidos dentro del XML padre y fuera de las firmas,
                // se trata de una firma internally detached
                if (referencedNode != null) {
                    return true;
                }
            }
        }
        // Se supone que los datos estarian dentro de alguna de las firmas o fuera del XML
        return false;
    }

    /** https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESConstants.java */
    /** Atributo identificador de elementos. */
    private static final String ID_IDENTIFIER = "Id"; //$NON-NLS-1$

    /**
     * https://github.com/ctt-gob-es/clienteafirma/blob/master/afirma-crypto-xades/src/main/java/es/gob/afirma/signers/xades/XAdESUtil.java
     * Busca un nodo con el atributo 'Id' indicado.
     * @param nodeId Identificador del nodo que queremos encontrar.
     * @param currentElement Elemento en el que queremos buscar.
     * @param omitSignatures Si es {@code true}, se omite la b&uacute;squeda dentro de cualquier
     * nodo de nombre "Signature", aunque podr&iacute;a referenciarse al propio nodo, {@code false}
     * en caso contrario.
     * @return Nodo con el identificador indicado o {@code null} si no
     * se encuentra el nodo.
     */
    private static Element findElementById(final String nodeId, final Element currentElement,
            final boolean omitSignatures) {

        // Si es este el nodo, lo devolvemos
        if (nodeId.equals(currentElement.getAttribute(ID_IDENTIFIER))) {
            return currentElement;
        }

        // Se podria referenciar a un nodo llamado "Signature", pero omitiriamos
        // la busqueda dentro de cualquier nodo con dicho nombre si asi se indica
        if (omitSignatures && currentElement.getLocalName().equals("Signature")) { //$NON-NLS-1$
            return null;
        }

        // Si no, lo buscamos en cada uno de los hijos, deteniendonos
        // en cuanto se encuentre
        Node item;
        final NodeList childList = currentElement.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            item = childList.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                final Element el = findElementById(nodeId, (Element) item, omitSignatures);
                if (el != null) {
                    return el;
                }
            }
        }
        // si no lo encontramos en ninguno de los nodos hijo, devolvemos nulo
        return null;
    }

    
}
