package com.wildbeeslabs.jentle.algorithms.toolset.impl;

import com.wildbeeslabs.jentle.algorithms.toolset.iface.PairIF;
import lombok.*;

import java.util.Objects;
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

    public static class Timer {
        private long start;
        private long lapStart;

        public Timer() {
            this.start = this.lapStart = System.currentTimeMillis();
        }

        public long getLap() {
            long lap = System.currentTimeMillis() - lapStart;
            lapStart = System.currentTimeMillis();
            return lap;
        }

        public float getLapSeconds() {
            return getLap() / 1000.f;
        }

        public long getTotal() {
            return System.currentTimeMillis() - start;
        }

        public float getTotalSeconds() {
            return getTotal() / 1000.f;
        }
    }

    /**
     * Enumerates events which can be monitored.
     */
    @Getter
    @Builder
    @EqualsAndHashCode
    @ToString
    public static class Event {

        public static final Event MIME_BODY_PREMATURE_END = Event.of("Body part ended prematurely. Boundary detected in header or EOF reached.");
        public static final Event HEADERS_PREMATURE_END = Event.of("Unexpected end of headers detected. Higher level boundary detected or EOF reached.");
        public static final Event INVALID_HEADER = Event.of("Invalid header encountered");
        public static final Event OBSOLETE_HEADER = Event.of("Obsolete header encountered");

        private final String code;

        public Event(final String code) {
            this.code = Objects.requireNonNull(code, "Code should not be null!");
        }

        public static Event of(final String code) {
            return new Event(code);
        }
    }
}
