package com.wildbeeslabs.jentle.algorithms.toolset.iface;

@FunctionalInterface
public interface TriConsumer<T, U, R> {

    void consume(final T t, final U u, final R r);
}
