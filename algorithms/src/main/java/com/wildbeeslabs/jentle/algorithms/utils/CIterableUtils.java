package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

/**
 * Custom iterable utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
@UtilityClass
public class CIterableUtils {

    public static <T> Iterator<T> arrayIterator(@NonNull final T[] array) {
        return new Iterator<>() {
            private int pos = 0;

            public boolean hasNext() {
                return (array.length > pos);
            }

            public T next() {
                return array[pos++];
            }
        };
    }
}
