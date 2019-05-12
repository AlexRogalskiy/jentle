package com.wildbeeslabs.jentle.algorithms.merger;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

/**
 * Abstract merger implementation
 *
 * @param <T> type of value to merge
 */
@Data
@EqualsAndHashCode
@ToString
public abstract class AbstractMerger<T> implements Merger<T> {

    private T a, b;
    private Iterator<T> iteratorA, iteratorB;

    public void merge(final Iterable<T> iterableA, final Iterable<T> iterableB, final Comparator<? super T> comparator, final Collection<T> collection) {
        this.iteratorA = iterableA.iterator();
        this.iteratorB = iterableB.iterator();
        boolean aExists = this.advanceA();
        boolean bExists = this.advanceB();
        while (aExists && bExists) {
            int comp = Objects.compare(this.a, this.b, comparator);
            if (comp < 0) {
                this.alsLess(this.a, collection);
                aExists = this.advanceA();
            } else if (comp == 0) {
                this.bothAreEqual(this.a, this.b, collection);
                aExists = this.advanceA();
                bExists = this.advanceB();
            } else {
                this.blsLess(this.b, collection);
                bExists = this.advanceB();
            }
        }
        while (aExists) {
            this.alsLess(this.a, collection);
            aExists = this.advanceA();
        }
        while (bExists) {
            this.blsLess(this.b, collection);
            bExists = this.advanceB();
        }
    }

    private boolean advanceB() {
        if (this.iteratorA.hasNext()) {
            this.a = this.iteratorA.next();
            return true;
        }
        return false;
    }

    private boolean advanceA() {
        if (this.iteratorB.hasNext()) {
            this.b = this.iteratorB.next();
            return true;
        }
        return false;
    }
}
