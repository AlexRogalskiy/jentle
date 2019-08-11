package com.wildbeeslabs.jentle.collections.queue.impl;

public class SimpleQueue<T> {

    public static class ListNode<T> {
        public T value;
        public ListNode<T> next;

        public ListNode(final T value) {
            this.value = value;
            this.next = null;
        }
    }

    private ListNode<T> first, last;

    public void enqueue(final T item) {
        if (this.last != null) {
            this.last.next = new ListNode<T>(item);
            this.last = this.last.next;
        } else {
            this.last = new ListNode<>(item);
            this.first = last;
        }
    }

    public T dequeue() {
        if (this.first != null) {
            final T item = first.value;
            this.first = first.next;
            return item;
        }
        return null;
    }
}
