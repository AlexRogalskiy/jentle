package com.wildbeeslabs.jentle.collections.stack;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A Stack implementation based on a singly linked list.
 *
 * @author Emina Torlak
 */
public final class LinkedStack<T> extends AbstractStack<T> {
    private StackEntry<T> head;
    private int size;

    /**
     * Constructs an empty stack.
     *
     * @ensures no this.elems'
     */
    public LinkedStack() {
        head = null;
        size = 0;
    }

    /**
     * @see kodkod.util.collections.Stack#size()
     */
    public int size() {
        return size;
    }

    /**
     * Pushes an item onto the top of this stack and returns it.
     *
     * @return item
     * @ensures this.size' = this.size + 1 && this.elems'[0] = item &&
     * all i: [0..this.size) | this.elems'[i+1] = this.elems[i]
     */
    public T push(T item) {
        head = new StackEntry<T>(item, head);
        size++;
        return item;
    }

    /**
     * Removes the object at the top of this stack and returns that object as the value of this function.
     *
     * @return this.elems[0]
     * @throws EmptyStackException no this.elems
     * @ensures this.size' = this.size - 1 &&
     * all i: [1..this.size) | this.elems'[i-1] = this.elems[i]
     */
    public T pop() {
        if (head == null) throw new EmptyStackException();
        final T pop = head.data;
        head = head.next;
        size--;
        return pop;
    }

    /**
     * Looks at the object at the top of this stack without removing it from the stack.
     *
     * @return this.elems[0]
     * @throws EmptyStackException no this.elems
     */
    public T peek() {
        if (head == null) throw new EmptyStackException();
        return head.data;
    }

    /**
     * Returns the 1-based position where an object is on this stack.
     * If the object o occurs as an item in this stack, this method
     * returns the distance from the top of the stack of the occurrence
     * nearest the top of the stack; the topmost item on the stack is
     * considered to be at distance 1. The equals method is used to
     * compare o to the items in this stack.
     *
     * @return o in this.elems[int] => min(this.elems.o) + 1, -1
     */
    public int search(Object o) {
        StackEntry<T> e = head;
        int position = 1;
        while (e != null) {
            if (equal(o, e.data))
                return position;
            e = e.next;
            position++;
        }
        return -1;
    }

    /**
     * Returns true if the stack is empty; otherwise returns false.
     *
     * @return no this.elems
     */
    public boolean empty() {
        return head == null;
    }

    /**
     * Iterates over the items in this LinkedStack, starting
     * at the top of the stack and working its way down.
     *
     * @return iterator over the elements in this stack.
     */
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private StackEntry<T> cursor = head, prev = null, pprev = null;

            public boolean hasNext() {
                return cursor != null;
            }

            public T next() {
                if (cursor == null) throw new NoSuchElementException();
                pprev = prev;
                prev = cursor;
                cursor = cursor.next;
                return prev.data;
            }

            public void remove() {
                if (prev == pprev) {
                    throw new UnsupportedOperationException();
                } else if (prev == head) {
                    head = cursor;
                } else {
                    pprev.next = cursor;
                    prev.next = null;
                }
                prev = pprev;
                size--;
            }

        };
    }

    /**
     * Represents a stack entry.
     *
     * @specfield data: T
     * @specfield next: StackEntry<T>
     */
    private static final class StackEntry<T> {
        T data;
        StackEntry<T> next;

        StackEntry(T data, StackEntry<T> next) {
            this.data = data;
            this.next = next;
        }
    }


}
