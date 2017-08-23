package com.wildbeeslabs.jentle.collections.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import static org.apache.commons.lang3.ArrayUtils.toArray;

/**
 *
 * Collection utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CUtils {

    public static Object[] mergeObjects(final Object o1, final Object o2) {
        if (!o1.getClass().isArray() && !o2.getClass().isArray()) {
            return new Object[]{o1, o2};
        }
        Object[] a1 = toArray(o1);
        Object[] a2 = toArray(o2);
        return ArrayUtils.addAll(a1, a2);
    }

    /**
     * Convert List by function converter
     *
     * @param <T>
     * @param <U>
     * @param from
     * @param func
     * @return converted list
     *
     * @see
     *
     * List<String> stringList = Arrays.asList("1","2","3"); List<Integer>
     * integerList = convertList(stringList, s -> Integer.parseInt(s));
     */
    public static <T, U> List<U> convertList(final List<T> from, final Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    /**
     * Convert Array by function converter
     *
     * @param <T>
     * @param <U>
     * @param from
     * @param func
     * @param generator
     * @return converted array
     *
     * @see
     *
     * String[] stringArr = {"1","2","3"}; Double[] doubleArr =
     * convertArray(stringArr, Double::parseDouble, Double[]::new);
     */
    public static <T, U> U[] convertArray(final T[] from, final Function<T, U> func, final IntFunction<U[]> generator) {
        return Arrays.stream(from).map(func).toArray(generator);
    }
}
