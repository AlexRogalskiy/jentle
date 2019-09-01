package com.wildbeeslabs.jentle.algorithms.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author lan
 */
public class Xml {

    public static Stream<Element> elements(Element parent) {
        NodeList nodes = parent.getChildNodes();
        return IntStream.range(0, nodes.getLength()).mapToObj(nodes::item).filter(Element.class::isInstance)
            .map(Element.class::cast);
    }

    public static Element getChild(Element element, String tagName) {
        Node node = element.getFirstChild();
        while (node != null) {
            if (node instanceof Element && ((Element) node).getTagName().equals(tagName)) {
                return (Element) node;
            }
            node = node.getNextSibling();
        }
        return null;
    }

    public static String getText(Element element, String tagName) {
        Element child = getChild(element, tagName);
        return child == null ? "" : child.getTextContent();
    }

    public static String getAttribute(Element element, String attribute) {
        if (element.hasAttribute(attribute))
            return element.getAttribute(attribute);
        return null;
    }

    public static int getIntAttribute(Element element, String attribute, int defaultValue) {
        String str = getAttribute(element, attribute);
        if (str == null)
            return defaultValue;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
