package com.wildbeeslabs.jentle.algorithms.function;

/**
 * A Processor represents a processing stage—which is both a {@link Subscriber}
 * and a {@link Publisher} and obeys the contracts of both.
 *
 * @param <T> the type of element signaled to the {@link Subscriber}
 * @param <R> the type of element signaled by the {@link Publisher}
 */
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
}
