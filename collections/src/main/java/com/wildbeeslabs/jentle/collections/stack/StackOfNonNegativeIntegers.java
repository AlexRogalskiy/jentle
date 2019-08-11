package com.wildbeeslabs.jentle.collections.stack;

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.exception.FullStackException;
import com.wildbeeslabs.jentle.collections.exception.NegativeElementException;

public class StackOfNonNegativeIntegers {
    public final static int DEFAULT_CAPACITY = 10;

    private int size;

    private final int capacity;

    private final int[] elements;

    public StackOfNonNegativeIntegers() {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        this.elements = new int[this.capacity];
    }

    public StackOfNonNegativeIntegers(int maximumNumbersOfElementsAllowed) {
        if (maximumNumbersOfElementsAllowed <= 0)
            this.capacity = DEFAULT_CAPACITY;
        else
            this.capacity = maximumNumbersOfElementsAllowed;

        this.size = 0;
        this.elements = new int[this.capacity];
    }

    public void push(int newElement) throws FullStackException, NegativeElementException {
        if (this.size == this.capacity)
            throw new FullStackException();

        if (newElement < 0)
            throw new NegativeElementException();

        this.elements[this.size] = newElement;
        this.size += 1;
    }

    public int pop() throws EmptyStackException {
        if (this.size == 0)
            throw new EmptyStackException();

        this.size -= 1;
        return this.elements[this.size];
    }

    public int view() throws EmptyStackException {
        if (this.size == 0)
            throw new EmptyStackException();

        return this.elements[this.size - 1];
    }

    public int getSize() {
        return this.size;
    }

    public int getCapacity() {
        return this.capacity;
    }
}
