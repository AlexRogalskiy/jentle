package com.wildbeeslabs.jentle.collections.array.impl;

import java.util.NoSuchElementException;

import static java.lang.String.format;

//                    head
//                    v
// [ ][ ][A][B][C][D][ ][ ][ ]
//        ^
//        tail
//
//       |<- size ->| (4)
// |<------ capacity ------->| (9)
//
public final class CircularBuffer<E> {

    int tail, head, size, capacity;
    Object[] elements;

    CircularBuffer(int capacity) {
        this.capacity = capacity;
        elements = new Object[capacity];
    }

    void add(E elem) {
        if (size == capacity) {
            throw new IllegalStateException(
                format("No room for '%s': capacity=%s", elem, capacity));
        }
        elements[head] = elem;
        head = (head + 1) % capacity;
        size++;
    }

    @SuppressWarnings("unchecked")
    E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Empty");
        }
        E elem = (E) elements[tail];
        elements[tail] = null;
        tail = (tail + 1) % capacity;
        size--;
        return elem;
    }

    @SuppressWarnings("unchecked")
    E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                format("0 <= index <= capacity: index=%s, capacity=%s",
                    index, capacity));
        }
        int idx = (tail + (size - index - 1)) % capacity;
        return (E) elements[idx];
    }

    public void resize(int newCapacity) {
        if (newCapacity < size) {
            throw new IllegalStateException(
                format("newCapacity >= size: newCapacity=%s, size=%s",
                    newCapacity, size));
        }

        Object[] newElements = new Object[newCapacity];

        if (tail < head || size == 0) {
            System.arraycopy(elements, tail, newElements, 0, size);
        } else {
            System.arraycopy(elements, tail, newElements, 0, elements.length - tail);
            System.arraycopy(elements, 0, newElements, elements.length - tail, head);
        }

        elements = newElements;
        tail = 0;
        head = size;
        this.capacity = newCapacity;
    }
}
