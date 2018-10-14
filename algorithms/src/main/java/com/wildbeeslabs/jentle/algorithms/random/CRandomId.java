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

import com.wildbeeslabs.jentle.algorithms.utils.CDigestUtils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom random id utility implementation.
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CRandomId {

    /**
     * The default message digest algorithm.
     */
    protected static final String DEFAULT_ALGORITHM = "MD5";
    /**
     * A random number generator to use when generating session identifiers.
     */
    private Random random = new SecureRandom();
    /**
     * The MessageDigest implementation to be used when creating session
     * identifiers.
     */
    private MessageDigest digest = null;

    public CRandomId() {
        this(CRandomId.DEFAULT_ALGORITHM);
    }

    /**
     * Seed the random number
     *
     * @param algorithm digest algorithm
     */
    public CRandomId(final String algorithm) {
        long seed = System.currentTimeMillis();
        char[] entropy = toString().toCharArray();
        for (int i = 0; i < entropy.length; i++) {
            long update = ((byte) entropy[i]) << ((i % 8) * 8);
            seed ^= update;
        }
        this.random.setSeed(seed);
        this.digest = CDigestUtils.getDigest(algorithm);
    }

    /**
     * Generate and return a new session identifier.
     *
     * @param length The number of bytes to generate
     * @return A new page id string
     */
    public synchronized String generateId(int length) {
        byte[] buffer = new byte[length];
        final StringBuffer reply = new StringBuffer();
        int resultLenBytes = 0;
        while (resultLenBytes < length) {
            this.random.nextBytes(buffer);
            buffer = this.getDigest().digest(buffer);
            for (int j = 0; j < buffer.length && resultLenBytes < length; j++) {
                byte b1 = (byte) ((buffer[j] & 0xf0) >> 4);
                if (b1 < 10) {
                    reply.append((char) ('0' + b1));
                } else {
                    reply.append((char) ('A' + (b1 - 10)));
                }
                byte b2 = (byte) (buffer[j] & 0x0f);
                if (b2 < 10) {
                    reply.append((char) ('0' + b2));
                } else {
                    reply.append((char) ('A' + (b2 - 10)));
                }
                resultLenBytes++;
            }
        }
        return reply.toString();
    }
}
