package com.wildbeeslabs.jentle.collections.iterator;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Wrapper to convert an iterator to an enumeration
 *
 * @author Stuart Douglas
 */
public class IteratorEnumeration<T> implements Enumeration<T> {

    private final Iterator<T> iterator;

    public IteratorEnumeration(final Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    @Override
    public T nextElement() {
        return iterator.next();
    }
}
