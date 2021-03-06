package com.wildbeeslabs.jentle.algorithms.math.utils;

public class Node {
    private String val;
    private Node parent;

    public Node(String val) {
        this.val = val;
        this.parent = null;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getVal() {
        return val;
    }

    public Node getParent() {
        return parent;
    }
}
