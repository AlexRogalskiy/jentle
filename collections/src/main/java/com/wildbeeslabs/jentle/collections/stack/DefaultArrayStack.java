package com.wildbeeslabs.jentle.collections.stack;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.wildbeeslabs.jentle.collections.stack.AbstractStack.equal;

/**
 * A Stack implementation based on an array.
 *
 * @author Emina Torlak
 */
public class DefaultArrayStack<T> extends Stack<T> {
    /* stack elements:  the last element in the array is at the top of the stack. */
    private T[] elems;
    private int size;

    /**
     * Constructs an empty stack with the inital capacity of 10.
     *
     * @ensures no this.elems'
     */
    public DefaultArrayStack() {
        this(10);
    }

    @SuppressWarnings("unchecked")
    public DefaultArrayStack(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException(initialCapacity + "<0");
        elems = (T[]) new Object[initialCapacity];
        size = 0;
    }

    /**
     * Increases the capacity of this DefaultArrayStack, if necessary, to ensure that
     * it can hold at least the number of elements specified by the minimum
     * capacity argument.
     */
    @SuppressWarnings("unchecked")
    public void ensureCapacity(int minCapacity) {
        final int oldCapacity = elems.length;
        if (minCapacity > oldCapacity) {
            final T[] oldElems = elems;
            elems = (T[]) new Object[StrictMath.max(minCapacity, (oldCapacity * 3) / 2)];
            System.arraycopy(oldElems, 0, elems, 0, size);
        }
    }

    /**
     * Trims the capacity of this DefaultArrayStack to be the
     * stack's current size.  An application can use this operation to minimize
     * the storage of an DefaultArrayStack instance.
     */
    @SuppressWarnings("unchecked")
    public void trimToSize() {
        final int oldCapacity = elems.length;
        if (size < oldCapacity) {
            final Object oldElems[] = elems;
            elems = (T[]) new Object[size];
            System.arraycopy(oldElems, 0, elems, 0, size);
        }
    }

    public int size() {
        return size;
    }

    @Override
    public T push(T item) {
        ensureCapacity(size + 1);
        elems[size++] = item;
        return item;
    }

    @Override
    public T pop() {
        if (empty()) throw new EmptyStackException();
        final T top = elems[--size];
        elems[size] = null;
        return top;
    }

    public T peek() {
        if (empty()) throw new EmptyStackException();
        return elems[size - 1];
    }

    public int search(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (equal(o, elems[i]))
                return i;
        }
        return -1;
    }

    public boolean empty() {
        return size == 0;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int cursor = size - 1, lastReturned = -1;

            @Override
            public boolean hasNext() {
                return cursor >= 0;
            }

            @Override
            public T next() {
                if (cursor < 0)
                    throw new NoSuchElementException();
                lastReturned = cursor;
                return elems[cursor--];
            }

            @Override
            public void remove() {
                if (lastReturned < 0)
                    throw new UnsupportedOperationException();
                size--;
                System.arraycopy(elems, lastReturned + 1, elems, lastReturned, size - lastReturned);
                elems[size] = null;
                lastReturned = -1;
            }
        };
    }
}
