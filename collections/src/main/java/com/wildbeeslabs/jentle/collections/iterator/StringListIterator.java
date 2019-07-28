package com.wildbeeslabs.jentle.collections.iterator;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;

/**
 * A custom iterator for a list of strings with peek functionality.
 *
 * @author Richard Veach
 */
@RequiredArgsConstructor
public class StringListIterator implements Iterator<String> {
    /**
     * The wrapped Iterator.
     */
    private final Iterator<String> iterator;
    /**
     * The next item in the iterator, or {@code null} if to use {@link #iterator}.
     */
    private String next;

    @Override
    public boolean hasNext() {
        final boolean result;
        if (this.next != null) {
            result = true;
        } else {
            result = this.iterator.hasNext();
        }
        return result;
    }

    @Override
    public String next() {
        final String result;
        if (this.next != null) {
            result = this.next;
            next = null;
        } else {
            result = this.iterator.next();
        }
        return result;
    }

    /**
     * Check the next element without reading it from the iterator. Returns {@code null} if the
     * iterator is at the end or has no more elements. A call to this method will be equal to
     * the next return of {@link #next()}.
     *
     * @return The next item.
     */
    public String peek() {
        final String result;
        if (this.next != null) {
            result = this.next;
        } else if (this.iterator.hasNext()) {
            next = this.iterator.next();
            result = this.next;
        } else {
            result = null;
        }
        return result;
    }
}
