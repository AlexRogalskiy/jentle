package com.wildbeeslabs.jentle.algorithms.utils;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Custom object utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CObjectUtils {

    /**
     * Checks that the specified value is of a specific type.
     *
     * @param value   the value to check
     * @param type    the type to require
     * @param message the message to use in exception if type is not as required
     * @param <T>     the type being required
     * @return the value casted to the required type
     * @throws IllegalArgumentException if {@code value} is not of the required type
     * @throws NullPointerException     if {@code value}, {@code type}, or {@code message} is {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> T requireType(final Object value, final Class<T> type, String message) {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(message, "message must not be null");

        if (!type.isInstance(value)) {
            throw new IllegalArgumentException(message);
        }
        return (T) value;
    }

    public static void freezeProcessing(final int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> wrapWithList(final Supplier<T> supplier) {
        return Lists.newArrayList(supplier.get());
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object object) {
        return (T) object;
    }
}
