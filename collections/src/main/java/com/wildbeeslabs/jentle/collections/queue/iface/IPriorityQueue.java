package com.wildbeeslabs.jentle.collections.queue.iface;

import com.wildbeeslabs.jentle.collections.iface.collection.ICollection;

public interface IPriorityQueue<K, T> extends ICollection<T> {

    int size();

    boolean isEmpty();

    void insertItem(final T key, final T value);

    T minElement();

    K minKey();

    T removeMin();
}
