package com.wildbeeslabs.jentle.collections.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CartesianIterator<T> implements Iterator<List<T>> {
    private final List<Buffer<T>> buffers;
    private final boolean allStartedAvailable;

    private int count;

    public CartesianIterator(final List<Iterator<T>> sources) {
        this.buffers = sources.stream()
            .map(s -> new Buffer<>(s, new ArrayList<>()))
            .collect(toList());
        this.allStartedAvailable = sources.stream().allMatch(Iterator::hasNext);
    }

    @Override
    public boolean hasNext() {
        return this.allStartedAvailable && this.buffers.stream().anyMatch(Buffer::available);
    }

    @Override
    public List<T> next() {
        List<T> result = new ArrayList<>();
        int n = this.count;
        for (final Buffer<T> each : this.buffers) {
            int divisor = each.divisor();
            result.add(each.get(n));
            n /= divisor;
        }
        ++this.count;
        return result;
    }

    private static class Buffer<T> {
        private final Iterator<T> source;
        private final List<T> consumed;
        private int index;

        public Buffer(final Iterator<T> source, final List<T> consumed) {
            this.source = source;
            this.consumed = consumed;
        }

        boolean available() {
            return this.source.hasNext() || this.index < this.consumed.size() - 1;
        }

        int divisor() {
            return this.source.hasNext() ? this.consumed.size() + 1 : this.consumed.size();
        }

        T get(int n) {
            this.index = n % divisor();
            if (index == this.consumed.size())
                this.consumed.add(this.source.next());
            return this.consumed.get(this.index);
        }
    }
}
