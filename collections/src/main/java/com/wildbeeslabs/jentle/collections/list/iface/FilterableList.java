package com.wildbeeslabs.jentle.collections.list.iface;

import lombok.NoArgsConstructor;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public interface FilterableList<T, S extends FilterableList<T, S>> extends List<T> {
    S filter(final Predicate<? super T> var1);

    T getOnly();

    S subList(int var1, int var2);

    @NoArgsConstructor
    abstract class AbstractBase<T, S extends FilterableList<T, S>> extends AbstractList<T> implements FilterableList<T, S> {

        @Override
        public S filter(final Predicate<? super T> elementMatcher) {
            final List<T> filteredElements = new ArrayList(this.size());
            final Iterator var3 = this.iterator();

            while (var3.hasNext()) {
                final T value = (T) var3.next();
                if (elementMatcher.test(value)) {
                    filteredElements.add(value);
                }
            }
            return (filteredElements.size() == this.size()) ? (S) this : this.wrap(filteredElements);
        }

        @Override
        public T getOnly() {
            if (this.size() != 1) {
                throw new IllegalStateException("size = " + this.size());
            }
            return this.get(0);
        }

        @Override
        public S subList(int fromIndex, int toIndex) {
            return this.wrap(super.subList(fromIndex, toIndex));
        }

        protected abstract S wrap(List<T> var1);
    }

    @NoArgsConstructor
    class Empty<T, S extends FilterableList<T, S>> extends AbstractList<T> implements FilterableList<T, S> {

        @Override
        public T get(int index) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public T getOnly() {
            throw new IllegalStateException("size = 0");
        }

        @Override
        public S filter(final Predicate<? super T> elementMatcher) {
            return (S) this;
        }

        @Override
        public S subList(int fromIndex, int toIndex) {
            if (fromIndex == toIndex && toIndex == 0) {
                return (S) this;
            } else if (fromIndex > toIndex) {
                throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
            } else {
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            }
        }
    }
}
