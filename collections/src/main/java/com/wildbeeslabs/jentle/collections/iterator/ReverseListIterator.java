package com.wildbeeslabs.jentle.collections.iterator;

import java.util.Iterator;
import java.util.List;

/**
 * An iterator for a list which returns values in the opposite order as the typical list iterator.
 */
public class ReverseListIterator<T> implements Iterator<T> {

    private int index;
    private List<T> list;

    public ReverseListIterator(List<T> list) {
        index = list.size() - 1;
        this.list = list;
    }

    public T next() {
        return list.get(index--);
    }

    public boolean hasNext() {
        return index >= 0;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
