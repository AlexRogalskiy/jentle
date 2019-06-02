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
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

@UtilityClass
public class CPreconditionsUtils {

    /**
     * Assert that the supplied {@link Object} is not {@code null}.
     *
     * @param object  the object to check
     * @param message precondition violation message
     * @return the supplied object as a convenience
     * @throws PreconditionViolationException if the supplied object is {@code null}
     * @see #notNull(Object, Supplier)
     */
    public static <T> T notNull(T object, String message) throws IllegalArgumentException {
        condition(object != null, message);
        return object;
    }

    /**
     * Assert that the supplied {@link Object} is not {@code null}.
     *
     * @param object          the object to check
     * @param messageSupplier precondition violation message supplier
     * @return the supplied object as a convenience
     * @throws PreconditionViolationException if the supplied object is {@code null}
     * @see #condition(boolean, Supplier)
     */
    public static <T> T notNull(T object, Supplier<String> messageSupplier) throws IllegalArgumentException {
        condition(object != null, messageSupplier);
        return object;
    }

    /**
     * Assert that the supplied array is neither {@code null} nor <em>empty</em>.
     *
     * <p><strong>WARNING</strong>: this method does NOT check if the supplied
     * array contains any {@code null} elements.
     *
     * @param array   the array to check
     * @param message precondition violation message
     * @return the supplied array as a convenience
     * @throws PreconditionViolationException if the supplied array is
     *                                        {@code null} or <em>empty</em>
     * @see #containsNoNullElements(Object[], String)
     * @see #condition(boolean, String)
     */
    public static <T> T[] notEmpty(T[] array, String message) throws IllegalArgumentException {
        condition(array != null && array.length > 0, message);
        return array;
    }

    /**
     * Assert that the supplied array is neither {@code null} nor <em>empty</em>.
     *
     * <p><strong>WARNING</strong>: this method does NOT check if the supplied
     * array contains any {@code null} elements.
     *
     * @param array           the array to check
     * @param messageSupplier precondition violation message supplier
     * @return the supplied array as a convenience
     * @throws PreconditionViolationException if the supplied array is
     *                                        {@code null} or <em>empty</em>
     * @see #containsNoNullElements(Object[], String)
     * @see #condition(boolean, String)
     */
    public static <T> T[] notEmpty(T[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        condition(array != null && array.length > 0, messageSupplier);
        return array;
    }

    /**
     * Assert that the supplied {@link Collection} is neither {@code null} nor empty.
     *
     * <p><strong>WARNING</strong>: this method does NOT check if the supplied
     * collection contains any {@code null} elements.
     *
     * @param collection the collection to check
     * @param message    precondition violation message
     * @return the supplied collection as a convenience
     * @throws PreconditionViolationException if the supplied collection is {@code null} or empty
     * @see #containsNoNullElements(Collection, String)
     * @see #condition(boolean, String)
     */
    public static <T extends Collection<?>> T notEmpty(T collection, String message)
        throws IllegalArgumentException {

        condition(collection != null && !collection.isEmpty(), message);
        return collection;
    }

    /**
     * Assert that the supplied {@link Collection} is neither {@code null} nor empty.
     *
     * <p><strong>WARNING</strong>: this method does NOT check if the supplied
     * collection contains any {@code null} elements.
     *
     * @param collection      the collection to check
     * @param messageSupplier precondition violation message supplier
     * @return the supplied collection as a convenience
     * @throws PreconditionViolationException if the supplied collection is {@code null} or empty
     * @see #containsNoNullElements(Collection, String)
     * @see #condition(boolean, String)
     */
    public static <T extends Collection<?>> T notEmpty(T collection, Supplier<String> messageSupplier)
        throws IllegalArgumentException {

        condition(collection != null && !collection.isEmpty(), messageSupplier);
        return collection;
    }

    /**
     * Assert that the supplied array contains no {@code null} elements.
     *
     * <p><strong>WARNING</strong>: this method does NOT check if the supplied
     * array is {@code null} or <em>empty</em>.
     *
     * @param array   the array to check
     * @param message precondition violation message
     * @return the supplied array as a convenience
     * @throws PreconditionViolationException if the supplied array contains
     *                                        any {@code null} elements
     * @see #notNull(Object, String)
     */
    public static <T> T[] containsNoNullElements(T[] array, String message) throws IllegalArgumentException {
        if (array != null) {
            Arrays.stream(array).forEach(object -> notNull(object, message));
        }
        return array;
    }

    /**
     * Assert that the supplied array contains no {@code null} elements.
     *
     * <p><strong>WARNING</strong>: this method does NOT check if the supplied
     * array is {@code null} or <em>empty</em>.
     *
     * @param array           the array to check
     * @param messageSupplier precondition violation message supplier
     * @return the supplied array as a convenience
     * @throws PreconditionViolationException if the supplied array contains
     *                                        any {@code null} elements
     * @see #notNull(Object, String)
     */
    public static <T> T[] containsNoNullElements(T[] array, Supplier<String> messageSupplier)
        throws IllegalArgumentException {

        if (array != null) {
            Arrays.stream(array).forEach(object -> notNull(object, messageSupplier));
        }
        return array;
    }

    /**
     * Assert that the supplied collection contains no {@code null} elements.
     *
     * <p><strong>WARNING</strong>: this method does NOT check if the supplied
     * collection is {@code null} or <em>empty</em>.
     *
     * @param collection the collection to check
     * @param message    precondition violation message
     * @return the supplied collection as a convenience
     * @throws PreconditionViolationException if the supplied collection contains
     *                                        any {@code null} elements
     * @see #notNull(Object, String)
     */
    public static <T extends Collection<?>> T containsNoNullElements(T collection, String message)
        throws IllegalArgumentException {

        if (collection != null) {
            collection.forEach(object -> notNull(object, message));
        }
        return collection;
    }

    /**
     * Assert that the supplied collection contains no {@code null} elements.
     *
     * <p><strong>WARNING</strong>: this method does NOT check if the supplied
     * collection is {@code null} or <em>empty</em>.
     *
     * @param collection      the collection to check
     * @param messageSupplier precondition violation message supplier
     * @return the supplied collection as a convenience
     * @throws PreconditionViolationException if the supplied collection contains
     *                                        any {@code null} elements
     * @see #notNull(Object, String)
     */
    public static <T extends Collection<?>> T containsNoNullElements(T collection, Supplier<String> messageSupplier)
        throws IllegalArgumentException {

        if (collection != null) {
            collection.forEach(object -> notNull(object, messageSupplier));
        }
        return collection;
    }

    /**
     * Assert that the supplied {@link String} is not blank.
     *
     * <p>A {@code String} is <em>blank</em> if it is {@code null} or consists
     * only of whitespace characters.
     *
     * @param str     the string to check
     * @param message precondition violation message
     * @return the supplied string as a convenience
     * @throws PreconditionViolationException if the supplied string is blank
     * @see #notBlank(String, Supplier)
     */
    public static String notBlank(String str, String message) throws IllegalArgumentException {
        condition(StringUtils.isNotBlank(str), message);
        return str;
    }

    /**
     * Assert that the supplied {@link String} is not blank.
     *
     * <p>A {@code String} is <em>blank</em> if it is {@code null} or consists
     * only of whitespace characters.
     *
     * @param str             the string to check
     * @param messageSupplier precondition violation message supplier
     * @return the supplied string as a convenience
     * @throws PreconditionViolationException if the supplied string is blank
     * @see StringUtils#isNotBlank(String)
     * @see #condition(boolean, Supplier)
     */
    public static String notBlank(String str, Supplier<String> messageSupplier) throws IllegalArgumentException {
        condition(StringUtils.isNotBlank(str), messageSupplier);
        return str;
    }

    /**
     * Assert that the supplied {@code predicate} is {@code true}.
     *
     * @param predicate the predicate to check
     * @param message   precondition violation message
     * @throws PreconditionViolationException if the predicate is {@code false}
     * @see #condition(boolean, Supplier)
     */
    public static void condition(boolean predicate, String message) throws IllegalArgumentException {
        if (!predicate) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that the supplied {@code predicate} is {@code true}.
     *
     * @param predicate       the predicate to check
     * @param messageSupplier precondition violation message supplier
     * @throws PreconditionViolationException if the predicate is {@code false}
     */
    public static void condition(boolean predicate, Supplier<String> messageSupplier)
        throws IllegalArgumentException {

        if (!predicate) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }
}
