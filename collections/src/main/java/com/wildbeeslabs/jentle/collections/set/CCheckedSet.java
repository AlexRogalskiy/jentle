/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.collections.set;

import com.wildbeeslabs.jentle.collections.interfaces.ISet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom checked set implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CCheckedSet<T> extends ACSet<T> implements ISet<T> {

    private final Set rawSet = new HashSet();
    private final Class<? extends T> type;
    private final boolean strict;

    public CCheckedSet(final Set rawSet, final Class<? extends T> type, boolean strict) {
        this.setRawSet(rawSet);
        this.type = type;
        this.strict = strict;
    }

    private void setRawSet(final Set rawSet) {
        this.rawSet.clear();
        if (Objects.nonNull(rawSet)) {
            this.rawSet.addAll(rawSet);
        }
    }

    private boolean acceptEntry(final Object o) {
        if (Objects.isNull(o)) {
            return true;
        } else if (this.type.isInstance(o)) {
            return true;
        } else if (this.strict) {
            throw new ClassCastException(String.format("Class: %s cannot be cast to type=%s", o.getClass(), this.type.getName()));
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new CCheckedSetIterator<T>(this.rawSet.iterator()) {
            @Override
            protected boolean accept(final Object o) {
                return acceptEntry(o);
            }
        };
    }

    @Override
    public boolean has(final T item) {
        return this.rawSet.contains(item);
    }

    @Override
    public ISet<T> disjunct(final T item) {
        this.rawSet.remove(item);
        return this;
    }

    protected abstract static class CCheckedSetIterator<T> implements Iterator<T> {

        private static final Object WAITING = new Object();
        private final Iterator it;
        private Object next = WAITING;

        public CCheckedSetIterator(final Iterator it) {
            this.it = it;
        }

        public boolean hasNext() {
            if (!Objects.equals(this.next, CCheckedSetIterator.WAITING)) {
                return true;
            }
            while (this.it.hasNext()) {
                this.next = this.it.next();
                if (this.accept(this.next)) {
                    return true;
                }
            }
            this.next = CCheckedSetIterator.WAITING;
            return false;
        }

        public T next() {
            if (Objects.equals(this.next, CCheckedSetIterator.WAITING) && !this.hasNext()) {
                throw new NoSuchElementException();
            }
            assert (!Objects.equals(this.next, CCheckedSetIterator.WAITING));
            final T x = (T) this.next;
            this.next = CCheckedSetIterator.WAITING;
            return x;
        }

        public void remove() {
            this.it.remove();
        }

        protected abstract boolean accept(final Object o);
    }

    @Override
    public int size() {
        int c = 0;
        final Iterator it = this.rawSet.iterator();
        while (it.hasNext()) {
            if (this.acceptEntry(it.next())) {
                c++;
            }
        }
        return c;
    }

    @Override
    public boolean add(final T o) {
        return this.rawSet.add(this.type.cast(o));
    }

    @Override
    public boolean contains(final Object o) {
        return this.rawSet.contains(this.type.cast(o));
    }
}
