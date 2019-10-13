package com.wildbeeslabs.jentle.collections.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.stream.Stream;

public class StreamIterator<T> implements Iterator<T> {
    private final Spliterator<T> spliterator;
    private boolean nextIsKnown = false;
    private T next = null;

    public StreamIterator(final Stream<T> stream) {
        this.spliterator = stream.spliterator();
    }

    @Override
    public boolean hasNext() {
        if (this.nextIsKnown) {
            return true;
        }
        return this.spliterator.tryAdvance(t -> {
            this.next = t;
            this.nextIsKnown = true;
        });
    }

    @Override
    public T next() {
        if (this.nextIsKnown) {
            return resetAndReturnNext();
        }
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return this.resetAndReturnNext();
    }

    private T resetAndReturnNext() {
        final T result = next;
        this.nextIsKnown = false;
        this.next = null;
        return result;
    }
}
