package com.wildbeeslabs.jentle.collections.iterator;

import lombok.RequiredArgsConstructor;

import java.util.Enumeration;
import java.util.Iterator;

@RequiredArgsConstructor
public class IteratorWrapper implements Iterator {
    private final Enumeration myEnumeration;

    @Override
    public boolean hasNext() {
        return this.myEnumeration.hasMoreElements();
    }

    @Override
    public Object next() {
        return this.myEnumeration.nextElement();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
