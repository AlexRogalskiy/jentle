package com.wildbeeslabs.jentle.collections.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Array {@link Iterator} implementation
 */
public class ArrayIterator<T> implements Iterator<T[]> {

    private final T[][] m_objects;
    private int m_count;

    public ArrayIterator(final T[][] objects) {
        this.m_objects = objects;
        this.m_count = 0;
    }

    @Override
    public boolean hasNext() {
        return this.m_count < this.m_objects.length;
    }

    @Override
    public T[] next() {
        if (this.m_count >= this.m_objects.length) {
            throw new NoSuchElementException();
        }
        return this.m_objects[this.m_count++];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove operation is not supported on this iterator");
    }
}
