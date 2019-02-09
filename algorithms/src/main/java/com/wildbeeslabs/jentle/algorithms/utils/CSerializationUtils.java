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
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Custom serialization utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CSerializationUtils {

    /**
     * Gets bytes representation of object instance
     *
     * @param value - object instance to serialize to a byte array
     * @return byte array representing the serialized object instance
     */
    public static byte[] serialize(final Object value) {
        try (final ObjectOutputStream o = new ObjectOutputStream(new ByteArrayOutputStream())) {
            o.writeObject(value);
            o.flush();
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot serialize input value=%s, message=%s", value, ex.getMessage()));
        }
        return null;
    }

    /**
     * Gets object instance representation of bytes stream
     *
     * @param array - the byte array that holds the serialized object instance
     * @return the de-serialized object instance
     */
    public static Object deserialize(final byte[] array) {
        try (final ObjectInputStream o = new ObjectInputStream(new ByteArrayInputStream(array))) {
            return o.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            log.error(String.format("ERROR: cannot deserialize input stream=%s, message=%s", array, ex.getMessage()));
        }
        return null;
    }

    /**
     * Clones object instance
     *
     * @param value - object instance to be cloned
     * @param <T>
     * @return copy of object instance
     */
    public static <T> T cloneObject(final T value) {
        return (T) CSerializationUtils.deserialize(serialize(value));
    }
}
