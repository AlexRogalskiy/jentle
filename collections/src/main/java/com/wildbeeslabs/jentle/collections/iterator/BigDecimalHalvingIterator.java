package com.wildbeeslabs.jentle.collections.iterator;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.math.RoundingMode.HALF_UP;

public class BigDecimalHalvingIterator implements Iterator<BigDecimal> {
    private final BigDecimal max;

    private boolean done;
    private BigDecimal next;

    public BigDecimalHalvingIterator(final BigDecimal start, final BigDecimal max) {
        this.max = max;
        this.next = start;
    }

    @Override
    public boolean hasNext() {
        return !this.done;
    }

    @Override
    public BigDecimal next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.next = this.peek();
        this.done = this.next.equals(this.peek());
        return this.next;
    }

    private BigDecimal peek() {
        return this.next.add(this.max.subtract(this.next).divide(BigDecimal.valueOf(2), HALF_UP));
    }
}
