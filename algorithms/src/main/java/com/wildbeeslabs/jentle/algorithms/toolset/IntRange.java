package com.wildbeeslabs.jentle.algorithms.toolset;

import java.io.Serializable;
import java.util.stream.IntStream;

import static io.jenetics.internal.util.Hashes.hash;
import static java.lang.String.format;

/**
 * Integer range class.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.2
 * @implNote This class is immutable and thread-safe.
 * @since 3.2
 */
public final class IntRange implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int _min;
    private final int _max;

    private IntRange(final int min, final int max) {
        if (min > max) {
            throw new IllegalArgumentException(format(
                "Min greater than max: %s > %s", min, max
            ));
        }

        _min = min;
        _max = max;
    }

    /**
     * Return the minimum value of the integer range.
     *
     * @return the minimum value of the integer range
     */
    public int getMin() {
        return _min;
    }

    /**
     * Return the maximum value of the integer range.
     *
     * @return the maximum value of the integer range
     */
    public int getMax() {
        return _max;
    }

    /**
     * Return the size of the {@code IntRange}: {@code max - min}.
     *
     * @return the size of the int range
     * @since 3.9
     */
    public int size() {
        return _max - _min;
    }

    /**
     * Returns a sequential ordered {@code IntStream} from {@link #getMin()}
     * (inclusive) to {@link #getMax()} (exclusive) by an incremental step of
     * {@code 1}.
     * <p>
     * An equivalent sequence of increasing values can be produced sequentially
     * using a {@code for} loop as follows:
     * <pre>{@code
     * for (int i = range.getMin(); i < range.getMax(); ++i) {
     *     ...
     * }
     * }</pre>
     *
     * @return a sequential {@link IntStream} for the range of {@code int}
     * elements
     * @since 3.4
     */
    public IntStream stream() {
        return IntStream.range(_min, _max);
    }

    /**
     * Create a new {@code IntRange} object with the given {@code min} and
     * {@code max} values.
     *
     * @param min the lower bound of the integer range
     * @param max the upper bound of the integer range
     * @return a new {@code IntRange} object
     * @throws IllegalArgumentException if {@code min > max}
     */
    public static IntRange of(final int min, final int max) {
        return new IntRange(min, max);
    }

    /**
     * Return a new (half open) range, which contains only the given value:
     * {@code [value, value + 1)}.
     *
     * @param value the value of the created (half open) integer range
     * @return a new (half open) range, which contains only the given value
     * @since 4.0
     */
    public static IntRange of(final int value) {
        return of(value, value + 1);
    }

    @Override
    public int hashCode() {
        return hash(_min, hash(_max, hash(getClass())));
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this ||
            obj instanceof IntRange &&
                _min == ((IntRange) obj)._min &&
                _max == ((IntRange) obj)._max;
    }

    @Override
    public String toString() {
        return "[" + _min + ", " + _max + "]";
    }

}
