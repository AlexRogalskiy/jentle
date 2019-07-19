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

import java.util.Objects;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Custom random string utility implementation.
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CRandomString {

    /**
     * Default Random instance
     */
    private static final Random DEFAULT_RANDOM_INSTANCE = new Random();

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of all characters.
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String random(int count) {
        return CRandomString.random(count, false, false);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of characters whose ASCII value is
     * between <code>32</code> and <code>126</code> (inclusive).
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomAscii(int count) {
        return CRandomString.random(count, 32, 127, false, false);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of alphabetic characters.
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomAlphabetic(int count) {
        return CRandomString.random(count, true, false);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of alpha-numeric characters.
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomAlphanumeric(int count) {
        return CRandomString.random(count, true, true);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of numeric characters.
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomNumeric(int count) {
        return CRandomString.random(count, false, true);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of alpha-numeric characters as
     * indicated by the arguments.
     *
     * @param count the length of random string to create
     * @param letters if <code>true</code>, generated string will include
     * alphabetic characters
     * @param numbers if <code>true</code>, generated string will include
     * numeric characters
     * @return the random string
     */
    public static String random(int count, boolean letters, boolean numbers) {
        return CRandomString.random(count, 0, 0, letters, numbers);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of alpha-numeric characters as
     * indicated by the arguments.
     *
     * @param count the length of random string to create
     * @param start the position in set of chars to start at
     * @param end the position in set of chars to end before
     * @param letters if <code>true</code>, generated string will include
     * alphabetic characters
     * @param numbers if <code>true</code>, generated string will include
     * numeric characters
     * @return the random string
     */
    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return CRandomString.random(count, start, end, letters, numbers, null, CRandomString.DEFAULT_RANDOM_INSTANCE);
    }

    /**
     * Creates a random string based on a variety of options, using default
     * source of randomness.
     *
     * This method has exactly the same semantics as
     * {@link #random(int,int,int,boolean,boolean,char[],Random)}, but instead
     * of using an externally supplied source of randomness, it uses the
     * internal static {@link Random} instance.
     *
     * @param count the length of random string to create
     * @param start the position in set of chars to start at
     * @param end the position in set of chars to end before
     * @param letters only allow letters?
     * @param numbers only allow numbers?
     * @param chars the set of chars to choose randoms from. If
     * <code>null</code>, then it will use the set of all chars.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     * <code>(end - start) + 1</code> characters in the set ArrayUtils.
     */
    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars) {
        return CRandomString.random(count, start, end, letters, numbers, chars, CRandomString.DEFAULT_RANDOM_INSTANCE);
    }

    /**
     * Creates a random string based on a variety of options, using supplied
     * source of randomness.
     *
     * If start and end are both <code>0</code>, start and end are set to
     * <code>' '</code> and <code>'z'</code>, the ASCII printable characters,
     * will be used, unless letters and numbers are both <code>false</code>, in
     * which case, start and end are set to <code>0</code> and
     * <code>Integer.MAX_VALUE</code>.
     *
     * If set is not <code>null</code>, characters between start and end are
     * chosen.
     *
     * This method accepts a user-supplied {@link Random} instance to use as a
     * source of randomness. By seeding a single {@link Random} instance with a
     * fixed seed and using it for each call, the same random sequence of
     * strings can be generated repeatedly and predictably.
     *
     * @param count the length of random string to create
     * @param start the position in set of chars to start at
     * @param end the position in set of chars to end before
     * @param letters only allow letters?
     * @param numbers only allow numbers?
     * @param chars the set of chars to choose randoms from. If
     * <code>null</code>, then it will use the set of all chars.
     * @param random a source of randomness.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     * <code>(end - start) + 1</code> characters in the set ArrayUtils.
     * @throws IllegalArgumentException if <code>count</code> &lt; 0.
     * @since 2.0
     */
    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, final Random random) {
        if (count == 0) {
            return StringUtils.EMPTY;
        } else if (count < 0) {
            throw new IllegalArgumentException(String.format("Requested random string length=%i is less than 0.", count));
        }
        if ((start == 0) && (end == 0)) {
            end = 'z' + 1;
            start = ' ';
            if (!letters && !numbers) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }
        char[] buffer = new char[count];
        int gap = end - start;
        while (count-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(gap) + start];
            }
            if ((letters && Character.isLetter(ch))
                    || (numbers && Character.isDigit(ch))
                    || (!letters && !numbers)) {
                if (ch >= 56320 && ch <= 57343) {
                    if (count == 0) {
                        count++;
                    } else {
                        // low surrogate, insert high surrogate after putting it in
                        buffer[count] = ch;
                        count--;
                        buffer[count] = (char) (55296 + random.nextInt(128));
                    }
                } else if (ch >= 55296 && ch <= 56191) {
                    if (count == 0) {
                        count++;
                    } else {
                        // high surrogate, insert low surrogate before putting it in
                        buffer[count] = (char) (56320 + random.nextInt(128));
                        count--;
                        buffer[count] = ch;
                    }
                } else if (ch >= 56192 && ch <= 56319) {
                    // private high surrogate, no effing clue, so skip it
                    count++;
                } else {
                    buffer[count] = ch;
                }
            } else {
                count++;
            }
        }
        return new String(buffer);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of characters specified.
     *
     * @param count the length of random string to create
     * @param chars the String containing the set of characters to use, may be
     * null
     * @return the random string
     * @throws IllegalArgumentException if <code>count</code> &lt; 0.
     */
    public static String random(int count, String chars) {
        if (Objects.isNull(chars)) {
            return CRandomString.random(count, 0, 0, false, false, null, CRandomString.DEFAULT_RANDOM_INSTANCE);
        }
        return CRandomString.random(count, chars.toCharArray());
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * Characters will be chosen from the set of characters specified.
     *
     * @param count the length of random string to create
     * @param chars the character ArrayUtils containing the set of characters to use,
     * may be null
     * @return the random string
     * @throws IllegalArgumentException if <code>count</code> &lt; 0.
     */
    public static String random(int count, char[] chars) {
        if (Objects.isNull(chars)) {
            return CRandomString.random(count, 0, 0, false, false, null, CRandomString.DEFAULT_RANDOM_INSTANCE);
        }
        return CRandomString.random(count, 0, chars.length, false, false, chars, CRandomString.DEFAULT_RANDOM_INSTANCE);
    }
}
