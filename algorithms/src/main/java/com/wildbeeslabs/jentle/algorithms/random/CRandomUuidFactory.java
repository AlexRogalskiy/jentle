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
package com.wildbeeslabs.jentle.algorithms.random;

import java.security.SecureRandom;

/**
 *
 * Custom random UUID factory implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CRandomUuidFactory {

    /**
     * Default singleton instance
     */
    private static final CRandomUuidFactory _SINGLETON = new CRandomUuidFactory();
    /**
     * Default array of hex values
     */
    private static final char[] _HEX_VALUES = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Default random generator
     */
    private static final SecureRandom _RNG = new SecureRandom();

    /**
     * Private constructor by singleton pattern implementation
     */
    private CRandomUuidFactory() {
    }

    /**
     * Creates unique UUID identifier
     *
     * @return a unique UUID identifier.
     */
    public String createUUID() {
        final byte[] bytes = new byte[16];
        _RNG.nextBytes(bytes);
        final StringBuffer uuid = new StringBuffer(41);
        uuid.append("uuid:");
        for (int n = 0; n < Character.SIZE; n++) {
            if (n == 4 || n == 6 || n == 8 || n == 10) {
                uuid.append('-');
            }
            int hex = bytes[n] & 255;
            uuid.append(_HEX_VALUES[hex >> 4]);
            uuid.append(_HEX_VALUES[hex & Character.SIZE - 1]);
        }
        return uuid.toString();
    }

    /**
     * Returns singleton random UUID factory instance
     *
     * @return random UUID factory instance
     */
    public static CRandomUuidFactory getInstance() {
        return CRandomUuidFactory._SINGLETON;
    }
}
