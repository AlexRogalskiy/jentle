package com.wildbeeslabs.jentle.algorithms.utils;

import java.lang.reflect.Array;

public class Factory {

    public static <T> T createInstance(final String typeName) {

        try {
            @SuppressWarnings("unchecked") final Class<T> type = (Class<T>) Class.forName(typeName);
            return createInstance(type, 0);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createInstance(Class<T> type, int sizeIfArray) {
        try {
            if (type.isArray()) {
                return type.cast(Array.newInstance(type.getComponentType(), sizeIfArray));
            }
            return type.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
