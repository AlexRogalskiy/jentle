package com.wildbeeslabs.jentle.algorithms.toolset;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A value combined with an index, indicating its position in an ordered sequence.
 *
 * @param <T> The type of the indexed value.
 */
@Data
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Indexed<T> {

    /**
     * Combine an index and a value into an indexed value.
     *
     * @param index The index of the value.
     * @param value The value indexed.
     * @param <T>   The type of the value.
     * @return The indexed value.
     */
    public static <T> Indexed<T> index(final long index, final T value) {
        return new Indexed<>(index, value);
    }

    private final long index;
    private final T value;
}
