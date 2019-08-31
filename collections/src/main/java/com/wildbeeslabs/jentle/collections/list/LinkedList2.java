package com.wildbeeslabs.jentle.collections.list;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;

/**
 * {@link Iterable} linked list {@code T} implementation
 */
@NoArgsConstructor
public class LinkedList2<E extends Comparable> implements Iterable<E> {
    private ListNode<E> firstNode;
    private ListNode<E> lastNode;

    @Data
    public class ListNode<E extends Comparable> {
        private ListNode<E> prev;
        private ListNode<E> next;
        private E value;

        public ListNode(final E value) {
            this.value = value;
        }
    }

    private class LinkListIterator<F extends Comparable> implements Iterator<F> {
        private ListNode<F> currentNode;

        public LinkListIterator(final LinkedList2<F> linkedList) {
            this.currentNode = (ListNode<F>) linkedList.firstNode;
        }

        @Override
        public boolean hasNext() {
            return this.currentNode != null;
        }

        @Override
        public F next() {
            final ListNode<F> returnNode = this.currentNode;
            this.currentNode = returnNode.next;
            return returnNode.value;
        }

        @Override
        public void remove() {
            final ListNode<F> newCurrent = this.currentNode.prev;
            newCurrent.next = this.currentNode.next;
            this.currentNode.next.prev = newCurrent;
            this.currentNode = newCurrent;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkListIterator<>(this);
    }


    public void add(final E value) {
        final ListNode<E> listNode = new ListNode<E>(value);
        this.addNodeAfter(this.lastNode, listNode);
    }

    public E get(final int index) {
        return getNodeAt(index, this.firstNode).value;
    }

    private ListNode<E> getNodeAt(int index) {
        ListNode<E> node = this.firstNode;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    private ListNode<E> getNodeAt(final int index, final ListNode<E> node) {
        if (index == 0) {
            return node;
        }
        return getNodeAt(index - 1, node.next);
    }


    private void addNodeAfter(final ListNode<E> prevNode, final ListNode<E> newNode) {
        if (this.firstNode == null) {
            this.firstNode = newNode;
            this.lastNode = newNode;
        } else {
            newNode.next = prevNode.next;
            prevNode.next = newNode;
            newNode.prev = prevNode;
            if (newNode.next == null) {
                this.lastNode = newNode;
            }
        }
    }
}
