package com.wildbeeslabs.jentle.algorithms.toolset.iface;

import lombok.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Range interface declaration
 *
 * @param <T> type of range bound element
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface RangeIF<T> {

    /**
     * Returns range upper bound {@code T}
     *
     * @return range upper bound {@code T}
     */
    @NonNull
    T getUpperBound();

    /**
     * Returns range lower bound {@code T}
     *
     * @return range lower bound {@code T}
     */
    @NonNull
    T getLowerBound();

    /**
     * Returns binary flag based on lower/upper bounds comparison
     *
     * @return true - if lower/upper bounds are equal, false - otherwise
     */
    default boolean isConstant() {
        return Objects.equals(this.getLowerBound(), this.getUpperBound());
    }

    /**
     * A collector to create {@link Map} from a {@link Stream} of {@link PairIF}s
     *
     * @param <T> type of range bound element
     * @return {@link Map} from a {@link Stream} of {@link PairIF}s.
     */
    static <T> Collector<RangeIF<T>, ?, Map<T, T>> toMap() {
        return Collectors.toMap(RangeIF::getLowerBound, RangeIF::getUpperBound);
    }

    /**
     * Invokes the given {@link BiConsumer} if all given {@link Optional} are present
     *
     * @param <T>        type of range bound element
     * @param lowerBound - initial input range lower bound {@link Optional}
     * @param upperBound - initial input range upper bound {@link Optional}
     * @param consumer   - initial input {@link BiConsumer} operator
     */
    static <T> void ifAllPresent(final Optional<T> lowerBound, final Optional<T> upperBound, final BiConsumer<T, T> consumer) {
        Objects.requireNonNull(lowerBound, "Optional lower bound should not be null!");
        Objects.requireNonNull(upperBound, "Optional upper bound should not be null!");
        Objects.requireNonNull(consumer, "Binary consumer operator should not be null!");

        mapIfAllPresent(lowerBound, upperBound, (l, u) -> {
            consumer.accept(l, u);
            return null;
        });
    }

    /**
     * Maps the values contained in the given {@link Optional} if both of them are present
     *
     * @param <T>        type of range bound element
     * @param <R>        type of function result
     * @param lowerBound - initial input range lower bound {@link Optional}
     * @param upperBound - initial input range upper bound {@link Optional}
     * @param function   - initial input {@link BiFunction} operator
     * @return {@link Optional} of {@link BiFunction} operator result {@code R}
     */
    static <T, R> Optional<R> mapIfAllPresent(final Optional<T> lowerBound, final Optional<T> upperBound, final BiFunction<T, T, R> function) {
        Objects.requireNonNull(lowerBound, "Optional lower bound should not be null!");
        Objects.requireNonNull(upperBound, "Optional upper bound should not be null!");
        Objects.requireNonNull(function, "Binary function operator should not be null!");

        return lowerBound.flatMap(l -> upperBound.map(u -> function.apply(l, u)));
    }
}
