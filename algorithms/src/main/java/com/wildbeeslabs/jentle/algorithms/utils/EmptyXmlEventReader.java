package com.wildbeeslabs.jentle.algorithms.utils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.NoSuchElementException;

/**
 * Simple {@link XMLEventReader} with no elements.
 *
 * @author Richard Veach
 */
public final class EmptyXmlEventReader implements XMLEventReader {
    @Override
    public Object next() {
        throw new NoSuchElementException();
    }

    @Override
    public XMLEvent nextEvent() throws XMLStreamException {
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public XMLEvent peek() throws XMLStreamException {
        return null;
    }

    @Override
    public String getElementText() throws XMLStreamException {
        return null;
    }

    @Override
    public XMLEvent nextTag() throws XMLStreamException {
        return null;
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void close() throws XMLStreamException {
    }
}
