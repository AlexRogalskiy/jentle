package com.wildbeeslabs.jentle.collections.stack;

public class SimpleStack<T> {

    public static class ListNode<T> {
        public T value;
        public ListNode<T> next;

        public ListNode(final T value) {
            this.value = value;
            this.next = null;
        }
    }

    private ListNode<T> top;

    public void push(final T item) {
        final ListNode<T> node = new ListNode<>(item);
        if (this.top != null) {
            node.next = this.top;
            this.top = node;
        }
    }

    public T pop() {
        if (top != null) {
            final T item = this.top.value;
            this.top = this.top.next;
            return item;
        }
        return null;
    }

    public T peek() {
        return this.top.value;
    }
}
