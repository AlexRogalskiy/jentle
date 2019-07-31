package com.wildbeeslabs.jentle.collections.list.iface;

public interface LinkedList<E> {

    void insert(final E value);

    void insertAt(final E value, int position) throws IllegalArgumentException;

    E removeAt(int position) throws IllegalArgumentException;

    E getAt(int position) throws IllegalArgumentException;

    E getFirst();

    E getLast();

    int getSize();
}
