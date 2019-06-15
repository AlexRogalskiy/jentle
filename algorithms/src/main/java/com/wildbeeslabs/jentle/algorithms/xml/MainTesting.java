package com.wildbeeslabs.jentle.algorithms.xml;

import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class MainTesting {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String xmlRecords = "<?xml version=\"1.0\"?>\n" +
            "<COMMAND>\n" +
            "    <DATA>\n" +
            "        <TXNID>1234567891</TXNID>\n" +
            "        <TXNAMT>15.00</TXNAMT>\n" +
            "        <TXNID>1234567892</TXNID>\n" +
            "        <TXNAMT>15.00</TXNAMT>\n" +
            "        <TXNID>1234567893</TXNID>\n" +
            "        <TXNAMT>15.00</TXNAMT>\n" +
            "        <TXNID>1234567894</TXNID>\n" +
            "        <TXNAMT>15.00</TXNAMT>\n" +
            "    </DATA>\n" +
            "</COMMAND>";

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        Document doc = db.parse(is);
        NodeList dataTag = doc.getElementsByTagName("DATA");
        NodeList dataItems = dataTag.item(0).getChildNodes();
        DataItem item = null;
        List<DataItem> items = new LinkedList<>();
        Node node = dataItems.item(1);
        while (node != null) {
            if (node.getNodeName().equalsIgnoreCase("TXNID")) {
                item = new DataItem();
                items.add(item);
            }
            setValue(item, node.getNodeName(), node.getTextContent());
            node = node.getNextSibling().getNextSibling();
        }
        items.add(item);
        items.iterator().forEachRemaining(System.out::println);
    }

    static void setValue(DataItem item, String nodeName, String nodeValue) {
        switch (nodeName) {
            case "TXNID":
                item.setTXNID(nodeValue);
                break;
            case "TXNAMT":
                item.setTXNAMT(nodeValue);
                break;
        }
    }
}

// Use Lombok
@Data
class DataItem {
    private String TXNID;
    private String TXNAMT;
}
