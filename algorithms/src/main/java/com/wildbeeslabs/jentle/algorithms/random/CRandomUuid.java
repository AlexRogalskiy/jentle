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

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

/**
 * Custom random UUID utility implementation.
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
public class CRandomUuid {

    /**
     * Default singleton instance
     */
    private static final CRandomUuid DEFAULT_SINGLETON_INSTANCE = new CRandomUuid();
    /**
     * Default ArrayUtils of hex values
     */
    private static final char[] DEFAULT_HEX_VALUES = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * Default random generator
     */
    private static final SecureRandom DEFAULT_RANDOM_INSTANCE = new SecureRandom();

    /**
     * Private constructor by singleton pattern implementation
     */
    private CRandomUuid() {
    }

    /**
     * Creates unique UUID identifier.
     *
     * @return a unique UUID identifier.
     */
    public String createUUID() {
        final byte[] bytes = new byte[16];
        CRandomUuid.DEFAULT_RANDOM_INSTANCE.nextBytes(bytes);
        final StringBuffer uuid = new StringBuffer(41);
        uuid.append("uuid:");
        for (int n = 0; n < Character.SIZE; n++) {
            if (n == 4 || n == 6 || n == 8 || n == 10) {
                uuid.append('-');
            }
            int hex = bytes[n] & 255;
            uuid.append(CRandomUuid.DEFAULT_HEX_VALUES[hex >> 4]);
            uuid.append(CRandomUuid.DEFAULT_HEX_VALUES[hex & Character.SIZE - 1]);
        }
        return uuid.toString();
    }

    /**
     * Returns singleton random UUID factory instance.
     *
     * @return random UUID factory instance
     */
    public static CRandomUuid getInstance() {
        return CRandomUuid.DEFAULT_SINGLETON_INSTANCE;
    }
}
