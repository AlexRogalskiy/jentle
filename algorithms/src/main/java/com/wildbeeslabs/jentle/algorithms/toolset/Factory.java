package com.wildbeeslabs.jentle.algorithms.toolset;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@UtilityClass
public class Factory {

    public static <T> T createInstance(final String typeName) {
        try {
            @SuppressWarnings("unchecked") final Class<T> type = (Class<T>) Class.forName(typeName);
            return createInstance(type, 0);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createInstance(@NonNull final Class<T> type, final int sizeIfArray) {
        if (type.isArray()) {
            return type.cast(Array.newInstance(type.getComponentType(), sizeIfArray));
        }
        try {
            return type.getConstructor().newInstance();
        } catch (InvocationTargetException e) {
            log.error("ERROR: cannot invoke target instance: {}, message: {}", type, e.getMessage());
        } catch (NoSuchMethodException e) {
            log.error("ERROR: no such method exception while creating instance: {}, message: {}", type, e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("ERROR: illegal access exception while creating instance: {}, message: {}", type, e.getMessage());
        } catch (InstantiationException e) {
            log.error("ERROR: cannot initialize target instance: {}, message: {}", type, e.getMessage());
        }
        return null;
    }
}
