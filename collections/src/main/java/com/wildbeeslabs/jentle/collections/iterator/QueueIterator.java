package com.wildbeeslabs.jentle.collections.iterator;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Queue;

@RequiredArgsConstructor
public final class QueueIterator<T> implements Iterator<T> {

    private final Queue<T> queue;

    @Override
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public T next() {
        return this.queue.poll();
    }

    @Override
    public void remove() {
        this.queue.remove();
    }
}
