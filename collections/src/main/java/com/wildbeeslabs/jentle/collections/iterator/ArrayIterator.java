package com.wildbeeslabs.jentle.collections.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator implementation used to efficiently expose contents of an
 * Array as read-only iterator.
 */
public class ArrayIterator<T> implements Iterator<T>, Iterable<T> {
    /**
     * Default array of {@code T} items
     */
    private final T[] _a;
    /**
     * Default array index
     */
    private int _index;

    public ArrayIterator(final T[] a) {
        this._a = a;
        this._index = 0;
    }

    @Override
    public boolean hasNext() {
        return this._index < this._a.length;
    }

    @Override
    public T next() {
        if (this._index >= this._a.length) {
            throw new NoSuchElementException();
        }
        return this._a[this._index++];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }
}
