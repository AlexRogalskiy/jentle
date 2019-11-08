package com.wildbeeslabs.jentle.collections.model;

import lombok.Data;

/**
 * A node on the double-linked list.
 */
@Data
public final class Node {
    final long key;

    Node prev;
    Node next;

    /**
     * Creates a new sentinel node.
     */
    public Node() {
        this.key = Integer.MIN_VALUE;
        this.prev = this;
        this.next = this;
    }

    /**
     * Creates a new, unlinked node.
     */
    public Node(long key) {
        this.key = key;
    }

    public void moveToTail(Node head) {
        remove();
        appendToTail(head);
    }

    /**
     * Appends the node to the tail of the list.
     */
    public void appendToHead(Node head) {
        Node first = head.next;
        head.next = this;
        first.prev = this;
        prev = head;
        next = first;
    }

    /**
     * Appends the node to the tail of the list.
     */
    public void appendToTail(Node head) {
        Node tail = head.prev;
        head.prev = this;
        tail.next = this;
        next = head;
        prev = tail;
    }

    /**
     * Removes the node from the list.
     */
    public void remove() {
        prev.next = next;
        next.prev = prev;
        next = prev = null;
    }
}
