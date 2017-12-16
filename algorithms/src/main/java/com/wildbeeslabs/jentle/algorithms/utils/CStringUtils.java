/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * Custom string utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CStringUtils {

    private CStringUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Check whether string consists of unique set of characters
     *
     * @param value - input string
     * @return true - if string is unique, false - otherwise
     */
    public static boolean isUnique(final String value) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return false;
        }
        return value.codePoints().allMatch(new HashSet<>()::add);
    }

    public static boolean permutation(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last) || first.length() != last.length()) {
            return false;
        }
        Map<Character, Long> firstMap = first.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Character, Long> secondMap = last.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return firstMap.equals(secondMap);
    }

    public static boolean isPermutationOfPalindrome(final String value) {
        if (Objects.isNull(value)) {
            return false;
        }
        final AtomicInteger count = new AtomicInteger();
        value.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).forEach((k, v) -> {
            if (v % 2 == 1) {
                if (count.get() > 1) {
                    return;
                }
                count.incrementAndGet();
            }
        });
        return count.get() <= 1;
    }

    public static boolean isSimilarByOneEdit(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last) || Math.abs(first.length() - last.length()) > 1) {
            return false;
        }
        int index1 = 0, index2 = 0;
        String s1 = first.length() < last.length() ? first : last;
        String s2 = first.length() < last.length() ? last : first;
        boolean flag = false;
        while (index2 < s2.length() && index1 < s1.length()) {
            if (s1.codePointAt(index1) != s2.codePointAt(index2)) {
                if (flag) {
                    return false;
                }
                flag = true;
                if (s1.length() == s2.length()) {
                    index1++;
                }
            } else {
                index1++;
            }
            index2++;
        }
        return true;
    }

    public static String compress(final String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        StringBuilder compStr = new StringBuilder(value.length());
        int count = 0, len = value.length();
        for (int i = 0; i < len; i++) {
            count++;
            if (i + 1 >= value.length() || value.codePointAt(i) != value.codePointAt(i + 1)) {
                compStr.append(Character.toChars(value.codePointAt(i)));
                compStr.append(count);
                count = 0;
            }
        }
        return compStr.length() < len ? compStr.toString() : value;
    }

    public static boolean isRotation(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last)) {
            return false;
        }
        int len = first.length();
        if (len == last.length() && len > 0) {
            return (first + first).contains(last);
        }
        return false;
    }

    public static long countOccurences(final String value, final String searchValue) {
        if (Objects.isNull(value) || Objects.isNull(searchValue)) {
            return -1;
        }
        return value.codePoints().filter(ch -> Objects.equals(ch, searchValue)).count();
        //return StringUtils.countMatches(value, searchValue);
    }

    public static String[] splitByRegex(final String value, final String regex) {
        return split(value, Pattern.quote(regex));
    }

    public static String[] split(final String value, final String delimeter) {
        if (Objects.isNull(value) || Objects.isNull(delimeter)) {
            return null;
        }
        return Optional.ofNullable(value).filter(str -> str.length() != 0).map(str -> str.split(delimeter)).orElse(null);//"\\s+|,\\s*|\\.\\s*"
        //StringUtils.split(value, delimeter);
    }

    public static String removeLastCharacter(final String value) {
        return Optional.ofNullable(value).filter(str -> str.length() != 0).map(str -> str.substring(0, str.length() - 1)).orElse(value);
        //return StringUtils.chop(value);
    }

    public static String generateRandom(int length, boolean useLetters, boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String generateAlphabeticRandom(int length) {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String generateAlphaNumericRandom(int length) {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static <T> List<? extends T> getTokens(final String value, final String delimeter, boolean returnDelims) {
        return Collections.list(new StringTokenizer(value, delimeter, returnDelims)).stream().map(token -> (T) token).collect(Collectors.toList());
    }

    public static Stream<String> streamOfStrings(final String value) {
        return value.codePoints().mapToObj(c -> String.valueOf((char) c));
    }

    public static Stream<Character> streamOfChars(final String value) {
        return value.codePoints().mapToObj(c -> (char) c);
    }

    public static String toString(final InputStream inputStream, final String encoding) {
        final StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(inputStream, writer, encoding);
        } catch (IOException ex) {
            return null;
        }
        return writer.toString();
    }

    public static String toString(final InputStream inputStream) {
        return toString(inputStream, StandardCharsets.UTF_8.name());
    }

    public static List<String> generateParenthesis(int count) {
        assert (count > 0);
        char[] str = new char[count * 2];
        final List<String> list = new ArrayList<>();
        addParanthesis(list, count, count, str, 0);
        return list;
    }

    private static void addParanthesis(final List<String> list, int left, int right, char[] str, int count) {
        if (left < 0 || right < left) {
            return;
        }
        if (0 == left && 0 == right) {
            String s = String.copyValueOf(str);
            list.add(s);
        } else {
            if (left > 0) {
                str[count] = '(';
                addParanthesis(list, left - 1, right, str, count + 1);
            }

            if (right > left) {
                str[count] = ')';
                addParanthesis(list, left, right - 1, str, count + 1);
            }
        }
    }

    public static int countEval(final String s, boolean result, final Map<String, Integer> memo) {
        Objects.requireNonNull(s);
        if (0 == s.length()) {
            return 0;
        }
        if (1 == s.length()) {
            return stringToBool(s) == result ? 1 : 0;
        }
        if (memo.containsKey(result + s)) {
            return memo.get(result + s);
        }
        int ways = 0;
        for (int i = 1; i < s.length(); i += 2) {
            char c = s.charAt(i);
            String left = s.substring(0, i);
            String right = s.substring(i + 1, s.length());

            int leftTrue = countEval(left, true, memo);
            int leftFalse = countEval(left, false, memo);
            int rightTrue = countEval(right, true, memo);
            int rightFalse = countEval(right, false, memo);

            int total = (leftTrue + leftFalse) * (rightTrue + rightFalse);

            int totalTrue = 0;
            if ('^' == c) {
                totalTrue = leftTrue * rightFalse + leftFalse * rightTrue;
            } else if ('&' == c) {
                totalTrue = leftTrue * rightTrue;
            } else if ('|' == c) {
                totalTrue = leftTrue * rightTrue + leftFalse * rightTrue + leftTrue * rightFalse;
            }
            int subWays = result ? totalTrue : total - totalTrue;
            ways += subWays;
        }
        memo.put(result + s, ways);
        return ways;
    }

    private static boolean stringToBool(final String value) {
        return "1".equals(value);
    }

    public static boolean doesMatch(final String pattern, final String value) {
        if (pattern.length() == 0) {
            return value.length() == 0;
        }
        char mainChar = pattern.charAt(0);
        char altChar = mainChar == 'a' ? 'b' : 'a';
        int size = value.length();

        int countOfMain = countOf(pattern, mainChar);
        int countOfAlt = pattern.length() - countOfMain;
        int firstAlt = pattern.indexOf(altChar);
        int maxMainSize = size / countOfMain;

        for (int i = 0; i <= maxMainSize; i++) {
            int remainLength = size - i * countOfMain;
            if (0 == countOfAlt || remainLength % countOfAlt == 0) {
                int altIndex = firstAlt * i;
                int altSize = 0 == countOfAlt ? 0 : remainLength / countOfAlt;
                if (matches(pattern, value, i, altSize, altIndex)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean matches(final String pattern, final String value, int mainSize, int altSize, int firstAlt) {
        int stringIndex = mainSize;
        for (int i = 1; i < pattern.length(); i++) {
            int size = pattern.charAt(i) == pattern.charAt(0) ? mainSize : altSize;
            int offset = pattern.charAt(i) == pattern.charAt(0) ? 0 : firstAlt;
            if (!isEqual(value, offset, stringIndex, size)) {
                return false;
            }
            stringIndex += size;
        }
        return true;
    }

    private static boolean isEqual(final String s1, int offset1, int offset2, int size) {
        for (int i = 0; i < size; i++) {
            if (s1.charAt(offset1 + i) != s1.charAt(offset2 + i)) {
                return false;
            }
        }
        return true;
    }

    private static int countOf(final String pattern, char c) {
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
}
