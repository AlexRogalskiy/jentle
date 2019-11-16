package com.wildbeeslabs.jentle.collections.list;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public abstract class AbstractForwardSequentialList<T> extends AbstractSequentialList<T> {

    @Nonnull
    private Iterator<T> iterator(int index) {
        if (index < 0) {
            throw new NoSuchElementException();
        }

        Iterator<T> it = iterator();
        for (int i = 0; i < index; i++) {
            it.next();
        }
        return it;
    }

    @Override
    @Nonnull
    public abstract Iterator<T> iterator();

    @Override
    @Nonnull
    public ListIterator<T> listIterator(final int initialIndex) {

        final Iterator<T> initialIterator;
        try {
            initialIterator = iterator(initialIndex);
        } catch (NoSuchElementException ex) {
            throw new IndexOutOfBoundsException();
        }

        return new AbstractListIterator<T>() {
            private int index = initialIndex - 1;
            @Nullable
            private Iterator<T> forwardIterator = initialIterator;

            @Nonnull
            private Iterator<T> getForwardIterator() {
                if (forwardIterator == null) {
                    try {
                        forwardIterator = iterator(index + 1);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new NoSuchElementException();
                    }
                }
                return forwardIterator;
            }

            @Override
            public boolean hasNext() {
                return getForwardIterator().hasNext();
            }

            @Override
            public boolean hasPrevious() {
                return index >= 0;
            }

            @Override
            public T next() {
                T ret = getForwardIterator().next();
                index++;
                return ret;
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public T previous() {
                forwardIterator = null;
                try {
                    return iterator(index--).next();
                } catch (IndexOutOfBoundsException ex) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public int previousIndex() {
                return index;
            }
        };
    }

    @Override
    @Nonnull
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }
}
