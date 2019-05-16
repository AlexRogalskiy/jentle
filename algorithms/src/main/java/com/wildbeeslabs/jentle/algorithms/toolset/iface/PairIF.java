package com.wildbeeslabs.jentle.algorithms.toolset.iface;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Pair interface declaration
 *
 * @param <T1> type of pair first element
 * @param <T2> type of pair last element
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface PairIF<T1, T2> {

    /**
     * Returns pair first value {@code T1}
     *
     * @return pair first value {@code T1}
     */
    T1 getFirst();

    /**
     * Returns pair last value {@code T2}
     *
     * @return pair last value {@code T2}
     */
    T2 getLast();

    /**
     * A collector to create {@link Map} from a {@link Stream} of {@link PairIF}s
     *
     * @param <T1> type of pair first element
     * @param <T2> type of pair last element
     * @return {@link Map} from a {@link Stream} of {@link PairIF}s.
     */
    static <T1, T2> Collector<PairIF<T1, T2>, ?, Map<T1, T2>> toMap() {
        return Collectors.toMap(PairIF::getFirst, PairIF::getLast);
    }

    /**
     * Invokes the given {@link BiConsumer} if all given {@link Optional} are present
     *
     * @param <T1>     type of pair first element
     * @param <T2>     type of pair last element
     * @param key      - initial input key {@link Optional}
     * @param value    - initial input value {@link Optional}
     * @param consumer - initial input {@link BiConsumer} operator
     */
    static <T1, T2> void ifAllPresent(final Optional<T1> key, final Optional<T2> value, final BiConsumer<T1, T2> consumer) {
        Objects.requireNonNull(key, "Optional first value should not be null!");
        Objects.requireNonNull(value, "Optional last value should not be null!");
        Objects.requireNonNull(consumer, "Consumer function operator should not be null!");

        mapIfAllPresent(key, value, (k, v) -> {
            consumer.accept(k, v);
            return null;
        });
    }

    /**
     * Maps the values contained in the given {@link Optional} if both of them are present
     *
     * @param <T1>     type of pair first element
     * @param <T2>     type of pair last element
     * @param <R>      type of function result
     * @param key      - initial input key {@link Optional}
     * @param value    - initial input value {@link Optional}
     * @param function - initial input {@link BiFunction} operator
     * @return {@link Optional} of {@link BiFunction} operator result {@code R}
     */
    static <T1, T2, R> Optional<R> mapIfAllPresent(final Optional<T1> key, final Optional<T2> value, final BiFunction<T1, T2, R> function) {
        Objects.requireNonNull(key, "Optional first value should not be null!");
        Objects.requireNonNull(value, "Optional last value should not be null!");
        Objects.requireNonNull(function, "Binary function operator should not be null!");

        return key.flatMap(k -> value.map(v -> function.apply(k, v)));
    }
}
