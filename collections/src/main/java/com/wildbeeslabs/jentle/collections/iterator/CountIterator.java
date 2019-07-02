package com.wildbeeslabs.jentle.collections.iterator;

import java.util.Iterator;

/**
 * Count {@link Iterator}
 */
public class CountIterator implements Iterator {
    private int cursor;
    private int countTo;

    public CountIterator(int countTo) {
        this.countTo = countTo;
    }

    public boolean hasNext() {
        return this.cursor < countTo;
    }

    public Object next() {
        return this.cursor++;
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove operation is not supported");
    }
}
