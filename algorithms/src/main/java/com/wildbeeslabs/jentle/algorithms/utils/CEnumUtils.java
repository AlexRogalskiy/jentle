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

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Objects;

/**
 * Custom enum utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@UtilityClass
public class CEnumUtils {

    /**
     * Returns an enum value for the given id.
     *
     * @param e    enum class
     * @param name id
     * @return enum value or null if the passed id is null
     * @throws IllegalArgumentException if there are no enum values with the given id
     */
    public static <T extends Enum<T>> T fromName(final Class<T> e, final String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        return Arrays.stream(e.getEnumConstants())
            .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Can't parse {%s} from name {%s}", e.getSimpleName(), name)));
    }

    /**
     * Returns an enum value for the given id.
     *
     * @param e            enum class
     * @param id           id
     * @param defaultValue the value to return if null is passed as id
     * @return enum value
     * @throws IllegalArgumentException if there are no enum values with the given id
     */
    public static <T extends Enum<T>> T fromName(final Class<T> e, final String name, final T defaultValue) {
        if (Objects.isNull(name)) {
            return defaultValue;
        }
        return Arrays.stream(e.getEnumConstants())
            .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Can't parse {%s} from name {%s}", e.getSimpleName(), name)));
    }

    /**
     * Returns an enum value for the given id, or the default value if null is passed or there are no enum values with
     * the given id.
     *
     * @param e            enum class
     * @param id           id
     * @param defaultValue the value to return if null is passed as id or if there are no enum values with the given id
     * @return enum value
     */
    public static <T extends Enum<T>> T fromNameOrDefault(final Class<T> e, final String name, final T defaultValue) {
        if (Objects.isNull(name)) {
            return defaultValue;
        }
        return Arrays.stream(e.getEnumConstants())
            .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(defaultValue);
    }
}
