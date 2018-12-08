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
package com.wildbeeslabs.jentle.algorithms.enums;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper enumeration class to process LITTLE/BIG-endian values
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public enum Endianness {

    LITTLE {
                @Override
                public short readShort(final InputStream stream) throws IOException {
                    final int low = readByte(stream) & 0xff;
                    final int high = readByte(stream);
                    return (short) ((high << 8) | low);
                }
            },
    BIG {
                @Override
                public short readShort(final InputStream stream) throws IOException {
                    final int high = readByte(stream);
                    final int low = readByte(stream) & 0xff;
                    return (short) ((high << 8) | low);
                }
            };

    private static int readByte(final InputStream stream) throws IOException {
        return stream.read();
    }

    public abstract short readShort(final InputStream stream) throws IOException;
}
