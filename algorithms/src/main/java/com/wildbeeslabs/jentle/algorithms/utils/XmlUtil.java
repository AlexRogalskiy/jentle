package com.wildbeeslabs.jentle.algorithms.utils;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;

public class XmlUtil {

    /**
     * Create a new DocumentBuilder which processes XML securely.
     *
     * @return a DocumentBuilder
     */
    public static DocumentBuilder createDocumentBuilder() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Create a new SAXParser which processes XML securely.
     *
     * @return a SAXParser
     */
    public static SAXParser createSaxParser() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            return spf.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException(e);
        }
    }
}
