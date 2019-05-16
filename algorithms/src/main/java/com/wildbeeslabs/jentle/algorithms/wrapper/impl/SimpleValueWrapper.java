package com.wildbeeslabs.jentle.algorithms.wrapper.impl;

import com.wildbeeslabs.jentle.algorithms.wrapper.iface.ValueWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Simple {@link ValueWrapper} implementation
 *
 * @param <T> type of wrapper value
 */
@Data
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class SimpleValueWrapper<T> implements ValueWrapper<T> {

    private final T value;

    /**
     * Return wrapped value {@code T}
     *
     * @return wrapped value {@code T}
     */
    @Override
    public T get() {
        return this.value;
    }
}
