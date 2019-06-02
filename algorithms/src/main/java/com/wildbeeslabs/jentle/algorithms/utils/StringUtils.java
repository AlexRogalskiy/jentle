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

import static org.apache.commons.lang3.StringUtils.EMPTY;

@UtilityClass
public class StringUtils {

    /**
     * Determine if the supplied {@link String} is <em>blank</em> (i.e.,
     * {@code null} or consisting only of whitespace characters).
     *
     * @param str the string to check; may be {@code null}
     * @return {@code true} if the string is blank
     * @see #isNotBlank(String)
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().isEmpty());
    }

    /**
     * Determine if the supplied {@link String} is not {@linkplain #isBlank
     * blank}.
     *
     * @param str the string to check; may be {@code null}
     * @return {@code true} if the string is not blank
     * @see #isBlank(String)
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Determine if the supplied {@link String} contains any whitespace characters.
     *
     * @param str the string to check; may be {@code null}
     * @return {@code true} if the string contains whitespace
     * @see #containsIsoControlCharacter(String)
     * @see Character#isWhitespace(int)
     */
    public static boolean containsWhitespace(String str) {
        return str != null && str.codePoints().anyMatch(Character::isWhitespace);
    }

    /**
     * Determine if the supplied {@link String} does not contain any whitespace
     * characters.
     *
     * @param str the string to check; may be {@code null}
     * @return {@code true} if the string does not contain whitespace
     * @see #containsWhitespace(String)
     * @see #containsIsoControlCharacter(String)
     * @see Character#isWhitespace(int)
     */
    public static boolean doesNotContainWhitespace(String str) {
        return !containsWhitespace(str);
    }

    /**
     * Determine if the supplied {@link String} contains any ISO control characters.
     *
     * @param str the string to check; may be {@code null}
     * @return {@code true} if the string contains an ISO control character
     * @see #containsWhitespace(String)
     * @see Character#isISOControl(int)
     */
    public static boolean containsIsoControlCharacter(String str) {
        return str != null && str.codePoints().anyMatch(Character::isISOControl);
    }

    /**
     * Determine if the supplied {@link String} does not contain any ISO control
     * characters.
     *
     * @param str the string to check; may be {@code null}
     * @return {@code true} if the string does not contain an ISO control character
     * @see #containsIsoControlCharacter(String)
     * @see #containsWhitespace(String)
     * @see Character#isISOControl(int)
     */
    public static boolean doesNotContainIsoControlCharacter(String str) {
        return !containsIsoControlCharacter(str);
    }

    /**
     * Convert the supplied {@code Object} to a {@code String} using the
     * following algorithm.
     *
     * <ul>
     * <li>If the supplied object is {@code null}, this method returns {@code "null"}.</li>
     * <li>If the supplied object is a primitive array, the appropriate
     * {@code Arrays#toString(...)} variant will be used to convert it to a String.</li>
     * <li>If the supplied object is an object array, {@code Arrays#deepToString(Object[])}
     * will be used to convert it to a String.</li>
     * <li>Otherwise, the result of invoking {@code toString()} on the object
     * will be returned.</li>
     * <li>If any of the above results in an exception, the String returned by
     * this method will be generated using the supplied object's class name and
     * hash code as follows:
     * {@code obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj))}</li>
     * </ul>
     *
     * @param obj the object to convert to a String; may be {@code null}
     * @return a String representation of the supplied object; never {@code null}
     * @see Arrays#deepToString(Object[])
     * @see ClassUtils#nullSafeToString(Class...)
     */
    public static String nullSafeToString(Object obj) {
        if (Objects.isNull(obj)) {
            return EMPTY;
        }

        try {
            if (obj.getClass().isArray()) {
                if (obj.getClass().getComponentType().isPrimitive()) {
                    if (obj instanceof boolean[]) {
                        return Arrays.toString((boolean[]) obj);
                    }
                    if (obj instanceof char[]) {
                        return Arrays.toString((char[]) obj);
                    }
                    if (obj instanceof short[]) {
                        return Arrays.toString((short[]) obj);
                    }
                    if (obj instanceof byte[]) {
                        return Arrays.toString((byte[]) obj);
                    }
                    if (obj instanceof int[]) {
                        return Arrays.toString((int[]) obj);
                    }
                    if (obj instanceof long[]) {
                        return Arrays.toString((long[]) obj);
                    }
                    if (obj instanceof float[]) {
                        return Arrays.toString((float[]) obj);
                    }
                    if (obj instanceof double[]) {
                        return Arrays.toString((double[]) obj);
                    }
                }
                return Arrays.deepToString((Object[]) obj);
            }

            // else
            return obj.toString();
            //return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
        } catch (Throwable throwable) {
            throw throwable;
        }
    }
}
