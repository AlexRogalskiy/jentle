/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/**
 * Custom factory utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
@UtilityClass
public class CFactoryUtils {

    public static <T> T createInstance(final String typeName) {
        try {
            @SuppressWarnings("unchecked") final Class<T> type = (Class<T>) Class.forName(typeName);
            return createInstance(type, 0);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(final Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
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
