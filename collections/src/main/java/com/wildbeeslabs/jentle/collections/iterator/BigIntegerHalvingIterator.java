package com.wildbeeslabs.jentle.collections.iterator;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BigIntegerHalvingIterator implements Iterator<BigInteger> {
    private final BigInteger max;

    private boolean done;
    private BigInteger next;

    public BigIntegerHalvingIterator(final BigInteger start, final BigInteger max) {
        this.max = max;
        this.next = start;
    }

    @Override
    public boolean hasNext() {
        return !this.done;
    }

    @Override
    public BigInteger next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.next = this.peek();
        this.done = next.equals(this.peek());
        return this.next;
    }

    private BigInteger peek() {
        return this.next.add(this.max.subtract(this.next).divide(BigInteger.valueOf(2)));
    }
}
