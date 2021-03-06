/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Custom dom utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CDomUtils {

    private static final ThreadLocal<SAXParser> saxParserHolder = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(CDomUtils.class);

    public static Document readDocument(final String xmlString) {
        return readDocument(new StringReader(xmlString));
    }

    public static Document readDocument(final Reader reader) {
        SAXReader xmlReader = getSaxReader();
        try {
            return xmlReader.read(reader);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to read XML from reader", e);
        }
    }

    private static SAXReader getSaxReader() {
        String useThreadLocalCache = System.getProperty("cuba.saxParserThreadLocalCache");
        if (useThreadLocalCache == null || Boolean.parseBoolean(useThreadLocalCache)) {
            try {
                return new SAXReader(getParser().getXMLReader());
            } catch (SAXException e) {
                throw new RuntimeException("Unable to create SAX reader", e);
            }
        } else {
            return new SAXReader();
        }
    }

    public static SAXParser getParser() {
        SAXParser parser = saxParserHolder.get();
        if (parser == null) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            XMLReader xmlReader;
            try {
                parser = factory.newSAXParser();
                xmlReader = parser.getXMLReader();
            } catch (ParserConfigurationException | SAXException e) {
                throw new RuntimeException("Unable to create SAX parser", e);
            }

            setParserFeature(xmlReader, "http://xml.org/sax/features/namespaces", true);
            setParserFeature(xmlReader, "http://xml.org/sax/features/namespace-prefixes", false);

            // external entites
            setParserFeature(xmlReader, "http://xml.org/sax/properties/external-general-entities", false);
            setParserFeature(xmlReader, "http://xml.org/sax/properties/external-parameter-entities", false);

            // external DTD
            setParserFeature(xmlReader, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // use Locator2 if possible
            setParserFeature(xmlReader, "http://xml.org/sax/features/use-locator2", true);

            saxParserHolder.set(parser);
        }
        return parser;
    }

    private static void setParserFeature(final XMLReader reader, final String featureName, boolean value) {
        try {
            reader.setFeature(featureName, value);
        } catch (SAXNotSupportedException | SAXNotRecognizedException e) {
            log.trace("Error while setting XML reader feature", e);
        }
    }

    public static Document readDocument(final InputStream stream) {
        SAXReader xmlReader = getSaxReader();
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return xmlReader.read(reader);
        } catch (IOException | DocumentException e) {
            throw new RuntimeException("Unable to read XML from stream", e);
        }
    }

    public static Document readDocument(final File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return readDocument(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to read XML from file", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static String writeDocument(final Document doc, boolean prettyPrint) {
        StringWriter writer = new StringWriter();
        writeDocument(doc, prettyPrint, writer);
        return writer.toString();
    }

    public static void writeDocument(final Document doc, boolean prettyPrint, final Writer writer) {
        XMLWriter xmlWriter;
        try {
            if (prettyPrint) {
                OutputFormat format = OutputFormat.createPrettyPrint();
                xmlWriter = new XMLWriter(writer, format);
            } else {
                xmlWriter = new XMLWriter(writer);
            }
            xmlWriter.write(doc);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write XML", e);
        }
    }

    public static void writeDocument(final Document doc, boolean prettyPrint, final OutputStream stream) {
        XMLWriter xmlWriter;
        try {
            if (prettyPrint) {
                OutputFormat format = OutputFormat.createPrettyPrint();
                xmlWriter = new XMLWriter(stream, format);
            } else {
                xmlWriter = new XMLWriter(stream);
            }
            xmlWriter.write(doc);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write XML", e);
        }
    }

    /**
     * @deprecated Use CDomUtils API.
     */
    @Deprecated
    public static List<Element> elements(final Element element) {
        return element.elements();
    }

    /**
     * @deprecated Use CDomUtils API.
     */
    @Deprecated
    public static List<Element> elements(final Element element, String name) {
        return element.elements(name);
    }

    /**
     * @deprecated Use CDomUtils API.
     */
    @Deprecated
    public static List<Attribute> attributes(final Element element) {
        return element.attributes();
    }

    public static void storeMap(final Element parentElement, final Map<String, String> map) {
        if (map == null) {
            return;
        }

        Element mapElem = parentElement.addElement("map");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Element entryElem = mapElem.addElement("entry");
            entryElem.addAttribute("key", entry.getKey());
            Element valueElem = entryElem.addElement("value");
            if (entry.getValue() != null) {
                String value = StringEscapeUtils.escapeXml11(entry.getValue());
                valueElem.setText(value);
            }
        }
    }

    public static void loadMap(final Element mapElement, final Map<String, String> map) {
        Objects.requireNonNull(map, "map is null");

        for (final Element entryElem : mapElement.elements("entry")) {
            String key = entryElem.attributeValue("key");
            if (key == null) {
                throw new IllegalStateException("No 'key' attribute");
            }

            String value = null;
            Element valueElem = entryElem.element("value");
            if (valueElem != null) {
                value = StringEscapeUtils.unescapeXml(valueElem.getText());
            }

            map.put(key, value);
        }
    }

    public static void walkAttributesRecursive(final Element element, final ElementAttributeVisitor visitor) {
        walkAttributes(element, visitor);

        for (final Element childElement : element.elements()) {
            walkAttributesRecursive(childElement, visitor);
        }
    }

    public static void walkAttributes(final Element element, final ElementAttributeVisitor visitor) {
        for (final Attribute attribute : element.attributes()) {
            visitor.onVisit(element, attribute);
        }
    }

    public interface ElementAttributeVisitor {
        void onVisit(final Element element, final Attribute attribute);
    }
}
