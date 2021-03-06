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
package com.wildbeeslabs.jentle.algorithms.set;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.BitSet;
import java.util.Objects;

/**
 * Custom set algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CSet {

    public static byte[] toByteArray(final BitSet bits) {
        Objects.requireNonNull(bits);
        final byte[] bytes = new byte[bits.length() / Byte.SIZE + 1];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / Byte.SIZE - 1] |= 1 << (i % Byte.SIZE);
            }
        }
        return bytes;
    }

    public static BitSet fromByteArray(byte[] bytes) {
        Objects.requireNonNull(bytes);
        final BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * Byte.SIZE; i++) {
            if ((bytes[bytes.length - i / Byte.SIZE - 1] & (1 << (i % Byte.SIZE))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }
}
