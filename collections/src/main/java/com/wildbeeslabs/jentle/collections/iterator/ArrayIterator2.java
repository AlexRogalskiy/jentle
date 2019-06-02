package com.wildbeeslabs.jentle.collections.iterator;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayIterator2 implements Iterator<Object> {
    private final Object array;
    private int currentIndex = 0;

    public ArrayIterator2(Object array) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("not an array");
        } else {
            this.array = array;
        }
    }

    public boolean hasNext() {
        return this.currentIndex < Array.getLength(this.array);
    }

    public Object next() {
        return Array.get(this.array, this.currentIndex++);
    }

    public void remove() {
        throw new UnsupportedOperationException("cannot remove items from an array");
    }
}
