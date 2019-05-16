package com.wildbeeslabs.jentle.algorithms.toolset.impl;

import com.wildbeeslabs.jentle.algorithms.toolset.iface.PairIF;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;

/**
 * {@link PairIF} implementation
 *
 * @param <T1> type of pair first element
 * @param <T2> type of pair last element
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Pair<T1, T2> implements PairIF<T1, T2> {

    /**
     * Pair first element {@code T1}
     */
    private final T1 first;
    /**
     * Pair last element {@code T2}
     */
    private final T2 last;

    /**
     * Returns {@link Pair} by input parameters
     *
     * @param <T1>   type of pair first element
     * @param <T2>   type of pair last element
     * @param first  - initial input pair first element {@code T1}
     * @param second - initial input pair last value {@code T2}
     * @return {@link Pair}
     */
    public static <T1, T2> Pair<T1, T2> of(final T1 first, final T2 second) {
        return new Pair<>(first, second);
    }

    /**
     * Returns {@link Pair} if both {@link Optional} instances have values or {@link Optional#empty()} if one or both
     * are missing.
     *
     * @param <T1>  type of pair first element
     * @param <T2>  type of pair last element
     * @param first - initial input pair first value {@link Optional}
     * @param last  - initial input pair last value {@link Optional}
     * @return {@link Optional} of {@link Pair}
     */
    public static <T1, T2> Optional<Pair<T1, T2>> with(final Optional<T1> first, final Optional<T2> last) {
        return first.flatMap(f -> last.map(l -> Pair.of(f, l)));
    }
}
