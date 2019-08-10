package com.wildbeeslabs.jentle.collections.list.node;

public class Node {
    private int data;
    public Node next;


    public Node(int data) {
        this.data = data;
        this.next = null;
    }

    public int getData() {
        return this.data;
    }
}
