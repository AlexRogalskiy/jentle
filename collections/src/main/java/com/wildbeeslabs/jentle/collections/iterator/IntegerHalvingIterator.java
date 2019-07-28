package com.wildbeeslabs.jentle.collections.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntegerHalvingIterator implements Iterator<Integer> {
    private boolean done;
    private int next;

    public IntegerHalvingIterator(int start) {
        this.next = start;
    }

    @Override
    public boolean hasNext() {
        return !this.done;
    }

    @Override
    public Integer next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        int result = this.next;
        this.next = this.peek();
        this.done = this.next == 0;
        return result;
    }

    private int peek() {
        return this.next / 2;
    }
}
