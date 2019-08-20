//package com.wildbeeslabs.jentle.algorithms.utils;
//
//import lombok.experimental.UtilityClass;
//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;
//import org.w3c.dom.Document;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.IOException;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.io.Writer;
//
///**
// * Pretty-prints uk.co.alt236.apkdetails.xml, supplied as a string.
// * <p/>
// * eg.
// * <code>
// * String formattedXml = new XmlFormatter().format("<tag><nested>hello</nested></tag>");
// * </code>
// */
//@UtilityClass
//public class XmlFormatter {
//
//    public static String format(final String unformattedXml) {
//        try {
//            final Document document = parseXmlFile(unformattedXml);
//            final OutputFormat format = new OutputFormat(document);
//
//            format.setLineWidth(65);
//            format.setIndenting(true);
//            format.setIndent(2);
//            Writer out = new StringWriter();
//            XMLSerializer serializer = new XMLSerializer(out, format);
//            serializer.serialize(document);
//
//            return out.toString();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static Document parseXmlFile(String in) {
//        try {
//            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            final DocumentBuilder db = dbf.newDocumentBuilder();
//            final InputSource is = new InputSource(new StringReader(in));
//            return db.parse(is);
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
