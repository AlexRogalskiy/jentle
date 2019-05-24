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

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Objects;

/**
 * Custom charset utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@UtilityClass
public class CCharsetUtils {

    /**
     * carriage return - line feed sequence
     */
    public static final String CRLF = "\r\n";
    /**
     * US-ASCII CR, carriage return (13)
     */
    public static final int CR = '\r';

    /**
     * US-ASCII LF, line feed (10)
     */
    public static final int LF = '\n';

    /**
     * US-ASCII SP, space (32)
     */
    public static final int SP = ' ';

    /**
     * US-ASCII HT, horizontal-tab (9)
     */
    public static final int HT = '\t';

    /**
     * Returns <code>true</code> if the specified character falls into the US
     * ASCII character set (Unicode range 0000 to 007f).
     *
     * @param ch character to test.
     * @return <code>true</code> if the specified character falls into the US
     * ASCII character set, <code>false</code> otherwise.
     */
    public static boolean isASCII(char ch) {
        return (0xFF80 & ch) == 0;
    }

    /**
     * Returns <code>true</code> if the specified string consists entirely of
     * US ASCII characters.
     *
     * @param s string to test.
     * @return <code>true</code> if the specified string consists entirely of
     * US ASCII characters, <code>false</code> otherwise.
     */
    public static boolean isASCII(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("String may not be null");
        }
        final int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!isASCII(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns <code>true</code> if the specified character is a whitespace
     * character (CR, LF, SP or HT).
     *
     * @param ch character to test.
     * @return <code>true</code> if the specified character is a whitespace
     * character, <code>false</code> otherwise.
     */
    public static boolean isWhitespace(final char ch) {
        return ch == SP || ch == HT || ch == CR || ch == LF;
    }

    /**
     * Returns <code>true</code> if the specified string consists entirely of
     * whitespace characters.
     *
     * @param s string to test.
     * @return <code>true</code> if the specified string consists entirely of
     * whitespace characters, <code>false</code> otherwise.
     */
    public static boolean isWhitespace(final String s) {
        Objects.requireNonNull(s, "String should not be null");
        final int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a {@link Charset} instance if character set with the given name
     * is recognized and supported by Java runtime. Returns <code>null</code>
     * otherwise.
     * <p/>
     * This method is a wrapper around {@link Charset#forName(String)} method
     * that catches {@link IllegalCharsetNameException} and
     * {@link UnsupportedCharsetException} and returns <code>null</code>.
     */
    public static Charset lookup(final String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        try {
            return Charset.forName(name);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException ex) {
            return null;
        }
    }
}