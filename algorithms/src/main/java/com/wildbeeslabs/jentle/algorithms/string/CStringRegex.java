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
package com.wildbeeslabs.jentle.algorithms.string;

import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Custom converter utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class CStringRegex {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CStringRegex.class);

    private CStringRegex() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Builds a regular expression to search for the terms
     *
     * @param terms a list of search terms.
     * @param sb place to build the regular expression.
     * @throws IllegalArgumentException if the length of terms is zero.
     */
    private static void buildFindAnyPattern(final String[] terms, final StringBuffer sb) {
        if (terms.length == 0) {
            throw new IllegalArgumentException("There must be at least one term to find.");
        }
        sb.append("(?:");
        for (int i = 0; i < terms.length; i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append("(?:");
            sb.append(terms[i]);
            sb.append(")");
        }
        sb.append(")");
    }

    /**
     * Compile a pattern that can will match a string if the string contains any
     * of the given terms.
     *
     * Usage:<br>
     * <code>boolean b = getContainsAnyPattern(terms).matcher(s).matches();</code>
     *
     * If multiple strings are matched against the same set of terms, it is more
     * efficient to reuse the pattern returned by this function.
     *
     * @param terms Array of search strings.
     * @return Compiled pattern that can be used to match a string to see if it
     * contains any of the terms.
     */
    public static Pattern getContainsAnyPattern(String[] terms) {
        StringBuffer sb = new StringBuffer();
        sb.append("(?s).*");
        buildFindAnyPattern(terms, sb);
        sb.append(".*");
        return Pattern.compile(sb.toString());
    }

    /**
     * Compile a pattern that can will match a string if the string equals any
     * of the given terms.
     *
     * Usage:<br>
     * <code>boolean b = getEqualsAnyPattern(terms).matcher(s).matches();</code>
     *
     * If multiple strings are matched against the same set of terms, it is more
     * efficient to reuse the pattern returned by this function.
     *
     * @param terms Array of search strings.
     * @return Compiled pattern that can be used to match a string to see if it
     * equals any of the terms.
     */
    public static Pattern getEqualsAnyPattern(String[] terms) {
        StringBuffer sb = new StringBuffer();
        sb.append("(?s)\\A");
        buildFindAnyPattern(terms, sb);
        sb.append("\\z");
        return Pattern.compile(sb.toString());
    }

    /**
     * Compile a pattern that can will match a string if the string starts with
     * any of the given terms.
     *
     * Usage:<br>
     * <code>boolean b = getStartsWithAnyPattern(terms).matcher(s).matches();</code>
     *
     * If multiple strings are matched against the same set of terms, it is more
     * efficient to reuse the pattern returned by this function.
     *
     * @param terms Array of search strings.
     * @return Compiled pattern that can be used to match a string to see if it
     * starts with any of the terms.
     */
    public static Pattern getStartsWithAnyPattern(String[] terms) {
        StringBuffer sb = new StringBuffer();
        sb.append("(?s)\\A");
        buildFindAnyPattern(terms, sb);
        sb.append(".*");
        return Pattern.compile(sb.toString());
    }

    /**
     * Compile a pattern that can will match a string if the string ends with
     * any of the given terms.
     *
     * Usage:<br>
     * <code>boolean b = getEndsWithAnyPattern(terms).matcher(s).matches();</code>
     *
     * If multiple strings are matched against the same set of terms, it is more
     * efficient to reuse the pattern returned by this function.
     *
     * @param terms Array of search strings.
     * @return Compiled pattern that can be used to match a string to see if it
     * ends with any of the terms.
     */
    public static Pattern getEndsWithAnyPattern(String[] terms) {
        StringBuffer sb = new StringBuffer();
        sb.append("(?s).*");
        buildFindAnyPattern(terms, sb);
        sb.append("\\z");
        return Pattern.compile(sb.toString());
    }

    /**
     * Compile a pattern that can will match a string if the string contains any
     * of the given terms.
     *
     * Case is ignored when matching using Unicode case rules.
     *
     * Usage:<br>
     * <code>boolean b = getContainsAnyPattern(terms).matcher(s).matches();</code>
     *
     * If multiple strings are matched against the same set of terms, it is more
     * efficient to reuse the pattern returned by this function.
     *
     * @param terms Array of search strings.
     * @return Compiled pattern that can be used to match a string to see if it
     * contains any of the terms.
     */
    public static Pattern getContainsAnyIgnoreCasePattern(String[] terms) {
        StringBuffer sb = new StringBuffer();
        sb.append("(?i)(?u)(?s).*");
        buildFindAnyPattern(terms, sb);
        sb.append(".*");
        return Pattern.compile(sb.toString());
    }

    /**
     * Compile a pattern that can will match a string if the string equals any
     * of the given terms.
     *
     * Case is ignored when matching using Unicode case rules.
     *
     * Usage:<br>
     * <code>boolean b = getEqualsAnyPattern(terms).matcher(s).matches();</code>
     *
     * If multiple strings are matched against the same set of terms, it is more
     * efficient to reuse the pattern returned by this function.
     *
     * @param terms Array of search strings.
     * @return Compiled pattern that can be used to match a string to see if it
     * equals any of the terms.
     */
    public static Pattern getEqualsAnyIgnoreCasePattern(String[] terms) {
        StringBuffer sb = new StringBuffer();
        sb.append("(?i)(?u)(?s)\\A");
        buildFindAnyPattern(terms, sb);
        sb.append("\\z");
        return Pattern.compile(sb.toString());
    }

    /**
     * Compile a pattern that can will match a string if the string starts with
     * any of the given terms.
     *
     * Case is ignored when matching using Unicode case rules.
     *
     * Usage:<br>
     * <code>boolean b = getStartsWithAnyPattern(terms).matcher(s).matches();</code>
     *
     * If multiple strings are matched against the same set of terms, it is more
     * efficient to reuse the pattern returned by this function.
     *
     * @param terms Array of search strings.
     * @return Compiled pattern that can be used to match a string to see if it
     * starts with any of the terms.
     */
    public static Pattern getStartsWithAnyIgnoreCasePattern(String[] terms) {
        StringBuffer sb = new StringBuffer();
        sb.append("(?i)(?u)(?s)\\A");
        buildFindAnyPattern(terms, sb);
        sb.append(".*");
        return Pattern.compile(sb.toString());
    }

    /**
     * Compile a pattern that can will match a string if the string ends with
     * any of the given terms.
     *
     * Case is ignored when matching using Unicode case rules.
     *
     * Usage:<br>
     * <code>boolean b = getEndsWithAnyPattern(terms).matcher(s).matches();</code>
     *
     * If multiple strings are matched against the same set of terms, it is more
     * efficient to reuse the pattern returned by this function.
     *
     * @param terms Array of search strings.
     * @return Compiled pattern that can be used to match a string to see if it
     * ends with any of the terms.
     */
    public static Pattern getEndsWithAnyIgnoreCasePattern(String[] terms) {
        StringBuffer sb = new StringBuffer();
        sb.append("(?i)(?u)(?s).*");
        buildFindAnyPattern(terms, sb);
        sb.append("\\z");
        return Pattern.compile(sb.toString());
    }

    /**
     * Tests to see if the given string contains any of the given terms.
     *
     * This implementation is more efficient than the brute force approach of
     * testing the string against each of the terms. It instead compiles a
     * single regular expression that can test all the terms at once, and uses
     * that expression against the string.
     *
     * This is a convenience method. If multiple strings are tested against the
     * same set of terms, it is more efficient not to compile the regular
     * expression multiple times.
     *
     * @see #getContainsAnyPattern(String[])
     *
     * @param s String that may contain any of the given terms.
     * @param terms list of substrings that may be contained in the given
     * string.
     * @return true iff one of the terms is a substring of the given string.
     */
    public static boolean containsAny(String s, String[] terms) {
        return getContainsAnyPattern(terms).matcher(s).matches();
    }

    /**
     * Tests to see if the given string equals any of the given terms.
     *
     * This implementation is more efficient than the brute force approach of
     * testing the string against each of the terms. It instead compiles a
     * single regular expression that can test all the terms at once, and uses
     * that expression against the string.
     *
     * This is a convenience method. If multiple strings are tested against the
     * same set of terms, it is more efficient not to compile the regular
     * expression multiple times.
     *
     * @see #getEqualsAnyPattern(String[])
     *
     * @param s String that may equal any of the given terms.
     * @param terms list of strings that may equal the given string.
     * @return true iff one of the terms is equal to the given string.
     */
    public static boolean equalsAny(String s, String[] terms) {
        return getEqualsAnyPattern(terms).matcher(s).matches();
    }

    /**
     * Tests to see if the given string starts with any of the given terms.
     *
     * This implementation is more efficient than the brute force approach of
     * testing the string against each of the terms. It instead compiles a
     * single regular expression that can test all the terms at once, and uses
     * that expression against the string.
     *
     * This is a convenience method. If multiple strings are tested against the
     * same set of terms, it is more efficient not to compile the regular
     * expression multiple times.
     *
     * @see #getStartsWithAnyPattern(String[])
     *
     * @param s String that may start with any of the given terms.
     * @param terms list of strings that may start with the given string.
     * @return true iff the given string starts with one of the given terms.
     */
    public static boolean startsWithAny(String s, String[] terms) {
        return getStartsWithAnyPattern(terms).matcher(s).matches();
    }

    /**
     * Tests to see if the given string ends with any of the given terms.
     *
     * This implementation is more efficient than the brute force approach of
     * testing the string against each of the terms. It instead compiles a
     * single regular expression that can test all the terms at once, and uses
     * that expression against the string.
     *
     * This is a convenience method. If multiple strings are tested against the
     * same set of terms, it is more efficient not to compile the regular
     * expression multiple times.
     *
     * @see #getEndsWithAnyPattern(String[])
     *
     * @param s String that may end with any of the given terms.
     * @param terms list of strings that may end with the given string.
     * @return true iff the given string ends with one of the given terms.
     */
    public static boolean endsWithAny(String s, String[] terms) {
        return getEndsWithAnyPattern(terms).matcher(s).matches();
    }

    /**
     * Tests to see if the given string contains any of the given terms.
     *
     * Case is ignored when matching using Unicode case rules.
     *
     * This implementation is more efficient than the brute force approach of
     * testing the string against each of the terms. It instead compiles a
     * single regular expression that can test all the terms at once, and uses
     * that expression against the string.
     *
     * This is a convenience method. If multiple strings are tested against the
     * same set of terms, it is more efficient not to compile the regular
     * expression multiple times.
     *
     * @see #getContainsAnyIgnoreCasePattern(String[])
     *
     * @param s String that may contain any of the given terms.
     * @param terms list of substrings that may be contained in the given
     * string.
     * @return true iff one of the terms is a substring of the given string.
     */
    public static boolean containsAnyIgnoreCase(String s, String[] terms) {
        return getContainsAnyIgnoreCasePattern(terms).matcher(s).matches();
    }

    /**
     * Tests to see if the given string equals any of the given terms.
     *
     * Case is ignored when matching using Unicode case rules.
     *
     * This implementation is more efficient than the brute force approach of
     * testing the string against each of the terms. It instead compiles a
     * single regular expression that can test all the terms at once, and uses
     * that expression against the string.
     *
     * This is a convenience method. If multiple strings are tested against the
     * same set of terms, it is more efficient not to compile the regular
     * expression multiple times.
     *
     * @see #getEqualsAnyIgnoreCasePattern(String[])
     *
     * @param s String that may equal any of the given terms.
     * @param terms list of strings that may equal the given string.
     * @return true iff one of the terms is equal to the given string.
     */
    public static boolean equalsAnyIgnoreCase(String s, String[] terms) {
        return getEqualsAnyIgnoreCasePattern(terms).matcher(s).matches();
    }

    /**
     * Tests to see if the given string starts with any of the given terms.
     *
     * Case is ignored when matching using Unicode case rules.
     *
     * This implementation is more efficient than the brute force approach of
     * testing the string against each of the terms. It instead compiles a
     * single regular expression that can test all the terms at once, and uses
     * that expression against the string.
     *
     * This is a convenience method. If multiple strings are tested against the
     * same set of terms, it is more efficient not to compile the regular
     * expression multiple times.
     *
     * @see #getStartsWithAnyIgnoreCasePattern(String[])
     *
     * @param s String that may start with any of the given terms.
     * @param terms list of strings that may start with the given string.
     * @return true iff the given string starts with one of the given terms.
     */
    public static boolean startsWithAnyIgnoreCase(String s, String[] terms) {
        return getStartsWithAnyIgnoreCasePattern(terms).matcher(s).matches();
    }

    /**
     * Tests to see if the given string ends with any of the given terms.
     *
     * Case is ignored when matching using Unicode case rules.
     *
     * This implementation is more efficient than the brute force approach of
     * testing the string against each of the terms. It instead compiles a
     * single regular expression that can test all the terms at once, and uses
     * that expression against the string.
     *
     * This is a convenience method. If multiple strings are tested against the
     * same set of terms, it is more efficient not to compile the regular
     * expression multiple times.
     *
     * @see #getEndsWithAnyIgnoreCasePattern(String[])
     *
     * @param s String that may end with any of the given terms.
     * @param terms list of strings that may end with the given string.
     * @return true iff the given string ends with one of the given terms.
     */
    public static boolean endsWithAnyIgnoreCase(String s, String[] terms) {
        return getEndsWithAnyIgnoreCasePattern(terms).matcher(s).matches();
    }

//    /**
//     * Escapes characters that have special meaning to regular expressions
//     *
//     * @param s String to be escaped
//     * @return escaped String
//     * @throws NullPointerException if s is null.
//     */
//    public static String escapeRegularExpressionLiteral(String s) {
//        int length = s.length();
//        int newLength = length;
//        for (int i = 0; i < length; i++) {
//            char c = s.charAt(i);
//            if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
//                newLength += 1;
//            }
//        }
//        if (length == newLength) {
//            // nothing to escape in the string
//            return s;
//        }
//        final StringBuffer sb = new StringBuffer(newLength);
//        for (int i = 0; i < length; i++) {
//            char c = s.charAt(i);
//            if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
//                sb.append('\\');
//            }
//            sb.append(c);
//        }
//        return sb.toString();
//    }
}
