package com.wildbeeslabs.jentle.collections.list.impl;

import com.wildbeeslabs.jentle.collections.list.node.Node;

public class LinkedList {
    private Node head;
    private Node tail;
    public int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.head == this.tail;
    }

    public int getSize() {
        return size;
    }

    public void insertAtStart(int val) {
        final Node newNode = new Node(val);
        this.size++;
        if (head == null) {
            this.head = newNode;
            this.tail = this.head;
        } else {
            newNode.next = this.head;
            this.head = newNode;
        }
    }

    public void insertAtLast(int val) {
        final Node newNode = new Node(val);
        this.size++;
        if (this.head == null) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.tail.next = newNode;
            this.tail = newNode;
        }
    }

    public void insertAtPos(final int val, final int pos) {
        final Node newNode = new Node(val);
        Node temp = this.head;
        int position = pos - 1;
        for (int i = 0; i < size; i++) {
            if (i == position) {
                Node temp1 = temp.next;
                temp.next = newNode;
                newNode.next = temp1;
                break;
            }
            temp = temp.next;
        }
        size++;
    }

    public void delete(int pos) {
        if (pos == 1) {
            this.head = this.head.next;
            this.size--;
            return;
        }
        if (pos == size) {
            Node prev = head;
            Node temp = head;
            while (temp.next != null) {
                prev = temp;
                temp = temp.next;
            }
            tail = prev;
            prev.next = null;
            size--;
            return;
        }
        Node ptr = head;
        pos = pos - 1;
        for (int i = 1; i < this.size - 1; i++) {
            if (i == pos) {
                Node temp = ptr.next;
                temp = temp.next;
                ptr.next = temp;
                break;
            }
            ptr = ptr.next;
        }
        this.size--;
    }
}
