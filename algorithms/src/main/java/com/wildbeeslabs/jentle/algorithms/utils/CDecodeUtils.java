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

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Custom decode utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@UtilityClass
public class CDecodeUtils {

    public static String decode(final byte[] bytes) {
        return new String(Base64.decodeBase64(bytes));
    }

    public static String[] decodeMultiple(final byte[]... bytes) {
        final String[] result = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = decode(bytes[i]);
        }
        return result;
    }

    public static byte[] encode(final String string) {
        return (Objects.isNull(string) ? null : Base64.encodeBase64(string.getBytes()));
    }

    public static Map<byte[], byte[]> encodeMap(final Map<String, byte[]> map) {
        return Optional.ofNullable(map).orElseGet(Collections::emptyMap).entrySet().stream().collect(Collectors.toMap(e -> encode(e.getKey()), e -> e.getValue()));
    }

    public static Map<String, byte[]> decodeMap(final Map<byte[], byte[]> map) {
        return Optional.ofNullable(map).orElseGet(Collections::emptyMap).entrySet().stream().collect(Collectors.toMap(e -> decode(e.getKey()), e -> e.getValue()));
    }
}
