package com.wildbeeslabs.jentle.collections.list.impl;

import java.util.AbstractList;
import java.util.Objects;

public class UnmodifiableArrayList<E> extends AbstractList<E> {
    private final E[] array;

    public UnmodifiableArrayList(final E[] array) {
        Objects.requireNonNull(array, "Array should not be null");
        this.array = array;
    }

    @Override
    public E get(int index) {
        if (index >= this.array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
        return this.array[index];
    }

    @Override
    public int size() {
        return this.array.length;
    }
}
