package com.wildbeeslabs.jentle.collections.stack;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Custom dual stack implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class ConcurrentStack<T> {
    private final AtomicReference<Node<T>> top = new AtomicReference<Node<T>>();

    public void push(final T item) {
        final Node<T> newHead = new Node<>(item);
        Node<T> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!this.top.compareAndSet(oldHead, newHead));
    }

    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        do {
            oldHead = this.top.get();
            if (Objects.isNull(oldHead)) {
                return null;
            }
            newHead = oldHead.next;
        } while (!this.top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }

    public boolean isEmpty() {
        return Objects.isNull(this.top.get());
    }

    public int size() {
        int currentSize = 0;
        Node<T> current = this.top.get();
        while (Objects.nonNull(current)) {
            currentSize++;
            current = current.next;
        }
        return currentSize;
    }

    public T peek() {
        final Node<T> eNode = this.top.get();
        if (Objects.isNull(eNode)) {
            return null;
        }
        return eNode.item;
    }

    private static class Node<E> {
        public final E item;
        public Node<E> next;

        public Node(final E item) {
            this.item = item;
        }
    }
}
