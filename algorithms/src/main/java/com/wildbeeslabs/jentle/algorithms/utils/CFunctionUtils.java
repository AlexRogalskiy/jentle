package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

/**
 * Custom function utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@UtilityClass
public class CFunctionUtils {

    public interface UnaryFunction<T> {
        T apply(final T arg);
    }

    public interface Function<T> {
        T apply(final T arg1, final T arg2);
    }

    private static UnaryFunction<Object> IDENTITY_FUNCTION = arg -> arg;

    private static final Function<Number> MAX = (n1, n2) -> Double.compare(n1.doubleValue(), n2.doubleValue()) > 0 ? n1 : n2;
}
