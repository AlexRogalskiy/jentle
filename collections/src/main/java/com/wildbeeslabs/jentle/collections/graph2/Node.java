package com.wildbeeslabs.jentle.collections.graph2;

import java.util.List;

public interface Node<T> {
    String getName();

    String getPath();

    String getParentPath();

    int getChildCount();

    boolean isLeaf();

    void addChild(Node<T> child);

    public T getPayload();

    Node<T> findDescendant(String nodePath);

    long getDescendantCount();

    List<Node<T>> getChildren();
}
