package com.wildbeeslabs.jentle.collections.list.impl;

import java.util.AbstractList;
import java.util.Objects;

public class CompositeUnmodifiableArrayList<E> extends AbstractList<E> {
    private final E[] arrayFirst;
    private final E[] arrayLast;

    public CompositeUnmodifiableArrayList(final E[] arrayFirst, final E[] arrayLast) {
        Objects.requireNonNull(arrayFirst, "First array should not be null");
        Objects.requireNonNull(arrayLast, "Last array should not be null");

        this.arrayFirst = arrayFirst;
        this.arrayLast = arrayLast;
    }

    @Override
    public E get(int index) {
        E element;
        if (index < this.arrayFirst.length) {
            element = this.arrayFirst[index];
        } else if (index - this.arrayFirst.length < this.arrayLast.length) {
            element = this.arrayLast[index - this.arrayFirst.length];
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
        return element;
    }

    @Override
    public int size() {
        return (this.arrayFirst.length + this.arrayLast.length);
    }
}
