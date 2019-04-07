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

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.exception.OverflowStackException;
import com.wildbeeslabs.jentle.collections.interfaces.IStack;
import com.wildbeeslabs.jentle.collections.stack.CBoundStack;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import pl.allegro.finance.tradukisto.MoneyConverters;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Custom string utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CStringUtils {

    /**
     * Default replace prefix
     */
    private static final String DEFAULT_REPLACE_PREFIX = "$";
    /**
     * Default not replace prefix
     */
    private static final String DEFAULT_NOT_REPLACE_PREFIX = "\\$";
    /**
     * Default regular expression (only alpha-numeric characters)
     */
    public static final String DEFAULT_ALPHANUMERIC_PATTERN = "[^a-zA-Z0-9]";

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

    public static boolean isSubstring(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last)) {
            return false;
        }
        if (first.indexOf(last) >= 0) {
            return true;
        }
        return false;
    }

    public static boolean permutation(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last) || first.length() != last.length()) {
            return false;
        }
        Map<Character, Long> firstMap = first.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Character, Long> secondMap = last.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return firstMap.equals(secondMap);
    }

    public static boolean permutation2(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last) || first.length() != last.length()) {
            return false;
        }
        return sort(first).equals(sort(last));
    }

    public static String sort(final String s) {
        char[] content = s.toCharArray();
        Arrays.sort(content);
        return new String(content);
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

    public static String insertCharAt(final String word, char c, int i) {
        final String start = word.substring(0, i);
        final String end = word.substring(i);
        return start + c + end;
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
        return Optional.ofNullable(value).filter(StringUtils::isNotEmpty).map(str -> str.trim().split(delimeter)).orElse(null);//"\\s+|,\\s*|\\.\\s*"
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

    public static List<String> generateBrackets(int count) {
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

    public static boolean isEqual(final String s1, int offset1, int offset2, int size) {
        for (int i = 0; i < size; i++) {
            if (s1.charAt(offset1 + i) != s1.charAt(offset2 + i)) {
                return false;
            }
        }
        return true;
    }

    public static int countOf(final String pattern, char c) {
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public static boolean brackets(final String value, int maxdeep) {
        try {
            final IStack<Character> stack = new CBoundStack<>(maxdeep, Character[].class);
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                switch (c) {
                    case '(':
                    case '[':
                    case '{':
                        stack.push(c);
                        break;
                    case ')':
                        if (stack.pop() != '(') {
                            return false;
                        }
                        break;
                    case ']':
                        if (stack.pop() != '[') {
                            return false;
                        }
                        break;
                    case '}':
                        if (stack.pop() != '{') {
                            return false;
                        }
                        break;
                }
            }
            return stack.isEmpty();
        } catch (OverflowStackException | EmptyStackException ex) {
            return false;
        }
    }

    public static class StringLengthComparator implements Comparator<String> {

        @Override
        public int compare(final String first, final String second) {
            //return first.length() - second.length();
            if (first.length() < second.length()) {
                return 1;
            }
            if (first.length() > second.length()) {
                return -1;
            }
            return 0;
        }
    }

    public static String reverseEachTokenInString(final String value, final String delimiter) {
        final StringBuilder builder = new StringBuilder();
        for (final String word : value.trim().split(Pattern.quote(delimiter))) {
            final StringBuilder eachWord = new StringBuilder(word).reverse();
            builder.append(eachWord).append(delimiter);
        }
        return builder.toString();
    }

    public static String reverseStringTokensWise(final String value, final String delimiter) {
        final String[] arrString = value.trim().split(Pattern.quote(delimiter));
        final StringBuilder builder = new StringBuilder();
        int length = arrString.length;
        while (--length >= 0) {
            builder.append(arrString[length]).append(delimiter);
        }
        return builder.toString();
    }

    public static String reverse(final String value) {
        if (Objects.isNull(value) || value.length() == 0) {
            return StringUtils.EMPTY;
        }
        int length = value.length();
        char[] chInputArray = value.toCharArray();
        for (int index = 0; index < length / 2; index++) {
            char firstHalf = chInputArray[index];
            char secondHalf = chInputArray[length - index - 1];
            chInputArray[index] = secondHalf;
            chInputArray[length - index - 1] = firstHalf;
        }
        return String.valueOf(chInputArray);
    }

    public static int knuthMorrisPratt(final String value, int startIndex, final String pattern) {
        if (Objects.isNull(pattern) || pattern.isEmpty()) {
            return -1;
        }
        assert (startIndex >= 0);
        int[] pf = new int[pattern.length()];
        for (int k = 0, i = 1; i < pattern.length(); i++) {
            while (k > 0 && pattern.charAt(i) != pattern.charAt(k)) {
                k = pf[k - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(k)) {
                k++;
            }
            pf[i] = k;
        }

        for (int k = 0, i = startIndex; i < value.length(); i++) {
            while (k > 0 && pattern.charAt(k) != value.charAt(i)) {
                k = pf[k - 1];
            }
            if (pattern.charAt(k) == value.charAt(i)) {
                k++;
            }
            if (k == pattern.length()) {
                return (i - k + 1);
            }
        }
        return -1;
    }

    //int distance = shortest(wordlist, word1, word2);
    //boolean confirm = searchConfirm(wordlist, word1, word2, distance);
    public static int shortest(final String[] words, final String word1, final String word2) {
        int min = Integer.MAX_VALUE;
        int lastPosWord1 = -1;
        int lastPosWord2 = -1;
        for (int i = 0; i < words.length; i++) {
            String currentWord = words[i];
            if (currentWord.equals(word1)) {
                lastPosWord1 = i;
                int distance = lastPosWord1 - lastPosWord2;
                if (lastPosWord2 >= 0 && min > distance) {
                    min = distance;
                }
            } else if (currentWord.equals(word2)) {
                lastPosWord2 = i;
                int distance = lastPosWord2 - lastPosWord1;
                if (lastPosWord1 >= 0 && min > distance) {
                    min = distance;
                }
            }
        }
        return min;
    }

    private static String wordAtLocation(final String[] words, int loc) {
        if (loc < 0 || loc >= words.length) {
            return null;
        }
        return words[loc];
    }

    public static boolean searchConfirm(final String[] words, final String word1, final String word2, int distance) {
        boolean found_at_distance = false;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1)) {
                for (int j = 1; j < distance; j++) {
                    final String loc2a = wordAtLocation(words, i - j);
                    final String loc2b = wordAtLocation(words, i + j);
                    if (word2.equals(loc2a) || word2.equals(loc2b)) {
                        return false;
                    }
                }
                final String loc2a = wordAtLocation(words, i - distance);
                final String loc2b = wordAtLocation(words, i + distance);
                if (word2.equals(loc2a) || word2.equals(loc2b)) {
                    found_at_distance = true;
                }
            }
        }
        return found_at_distance;
    }

    /**
     * Converts a string from the Unicode representation into something that can
     * be embedded in a java properties file. All references outside the ASCII
     * range are replaced with \\uXXXX.
     *
     * @param value The string to convert
     * @return the ASCII string
     */
    public static String native2Ascii(final String value) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            char aChar = value.charAt(i);
            if ((aChar < 0x0020) || (aChar > 0x007e)) {
                sb.append('\\');
                sb.append('u');
                sb.append(toHex((aChar >> 12) & 0xF));
                sb.append(toHex((aChar >> 8) & 0xF));
                sb.append(toHex((aChar >> 4) & 0xF));
                sb.append(toHex(aChar & 0xF));
            } else {
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

    private static char toHex(int nibble) {
        final char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        return hexDigit[nibble & 0xF];
    }

    public static byte[] stringToBytesAscii(final String str) {
        final byte[] b = new byte[str.length()];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) str.charAt(i);
        }
        return b;
    }

    public static byte[] stringToBytesUTFCustom(final String str) {
        final byte[] b = new byte[str.length() << 1];
        for (int i = 0; i < str.length(); i++) {
            char strChar = str.charAt(i);
            int bpos = i << 1;
            b[bpos] = (byte) ((strChar & 0xFF00) >> Byte.SIZE);
            b[bpos + 1] = (byte) (strChar & 0x00FF);
        }
        return b;
    }

    public static String bytesToStringUTFCustom(final byte[] bytes) {
        final char[] buffer = new char[bytes.length >> 1];
        for (int i = 0; i < buffer.length; i++) {
            int bpos = i << 1;
            char c = (char) (((bytes[bpos] & 0x00FF) << Byte.SIZE) + (bytes[bpos + 1] & 0x00FF));
            buffer[i] = c;
        }
        return new String(buffer);
    }

    public static String bytesToStringUTFNIO(final byte[] bytes) {
        final CharBuffer cBuffer = ByteBuffer.wrap(bytes).asCharBuffer();
        return cBuffer.toString();
    }

    public static byte[] stringToBytesUTFNIO(final String str) {
        final char[] buffer = str.toCharArray();
        final byte[] b = new byte[buffer.length << 1];
        final CharBuffer cBuffer = ByteBuffer.wrap(b).asCharBuffer();
        for (int i = 0; i < buffer.length; i++) {
            cBuffer.put(buffer[i]);
        }
        return b;
    }

    public static boolean checkForAllLetters(final String input) {
        Objects.requireNonNull(input);
        return input.toLowerCase()
            .replaceAll("[^a-z]", "")
            .replaceAll("(.)(?=.*\\1)", "")
            .length() == 26;
    }

    public static boolean checkForAllLetters2(final String input) {
        Objects.requireNonNull(input);
        long c = input.toLowerCase().chars()
            .filter(ch -> ch >= 'a' && ch <= 'z')
            .distinct()
            .count();
        return c == 26;
    }

    public static String capitalize(final String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    // MoneyConverters.ENGLISH_BANKING_MONEY_VALUE
    public String toLexicalCurrency(final String input, final MoneyConverters converter) {
        Objects.requireNonNull(converter);
        return converter.asWords(new BigDecimal(input));
    }

    public static String convertToTitleCaseWordFull(final String text) {
        return WordUtils.capitalizeFully(text);
    }

    public static String convertToTitleCaseWord(final String text) {
        return WordUtils.capitalize(text);
    }

    public boolean isPalindrome(final String text) {
        String temp = text.replaceAll("\\s+", StringUtils.EMPTY).toLowerCase();
        return IntStream.range(0, temp.length() / 2)
            .noneMatch(i -> temp.charAt(i) != temp.charAt(temp.length() - i - 1));
    }

    public static String removeLastCharRegex(final String text) {
        return (Objects.isNull(text)) ? null : text.replaceAll(".$", "");
    }

    public static String generateRandomString(int length, boolean useLetters, boolean useNumbers) {
        assert (length > 0);
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String generateRandomAplhabeticString(int length) {
        assert (length > 0);
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String generateRandomAplhanumericString(int length) {
        assert (length > 0);
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static final String convertBytesToHexString(byte[] p_data) {
        final StringBuffer buf = new StringBuffer();
        for (int i = 0; i < p_data.length; ++i) {
            int halfbyte = p_data[i] >>> 4 & 15;
            int var4 = 0;
            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char) (48 + halfbyte));
                } else {
                    buf.append((char) (97 + (halfbyte - 10)));
                }
                halfbyte = p_data[i] & 15;
            } while (var4++ < 1);
        }
        return buf.toString();
    }

    public static String formatLongToStringMin2Digest(long p_long) {
        String help = String.valueOf(p_long);
        if (help.length() == 0) {
            help = "00";
        } else if (help.length() == 1) {
            help = "0" + help;
        }
        return help;
    }

    public static final String convertSecondsToString(long p_seconds) {
        long help = p_seconds / 3600L;
        String totalTime = help + ":";
        p_seconds %= 3600L;
        help = p_seconds / 60L;
        if (help < 10L) {
            totalTime = totalTime + "0" + help + ":";
        } else {
            totalTime = totalTime + help + ":";
        }
        help = p_seconds % 60L;
        if (help < 10L) {
            totalTime = totalTime + "0" + help;
        } else {
            totalTime = totalTime + help;
        }
        return totalTime;
    }

    public static final String cleanupWithspacesBevorAndAfterNewlines(final String p_text) {
        if (Objects.isNull(p_text)) {
            return null;
        } else {
            final String[] elements = p_text.split(System.lineSeparator());
            final StringBuilder result = new StringBuilder();
            int size = elements.length;
            for (int i = 0; i < size; ++i) {
                result.append(System.lineSeparator()).append(elements[i].trim());
            }
            result.delete(0, 1);
            return result.toString();
        }
    }

    public static final String replaceInString(final String p_rawString, final String p_replaceString, final String p_ignoreReplaceString, final List<String> p_insertStrings) {
        int i = 1;
        String displayString = p_rawString;
        String toReplace = p_replaceString + i;
        for (int index = p_rawString.indexOf(toReplace); index >= 0 && i <= p_insertStrings.size(); index = displayString.indexOf(toReplace)) {
            int ignoreIndex = displayString.indexOf(p_ignoreReplaceString);
            if (ignoreIndex < 0 || ignoreIndex + 1 != index) {
                displayString = displayString.replace(toReplace, (CharSequence) p_insertStrings.get(i - 1));
            }
            StringBuilder var10000 = new StringBuilder(String.valueOf(p_replaceString));
            ++i;
            toReplace = var10000.append(i).toString();
        }
        return displayString;
    }

    public static final String replaceInString(final String p_rawString, final String p_replaceString, final String p_ignoreReplaceString, final String[] p_insertStrings) {
        int i = 1;
        String displayString = p_rawString;
        String toReplace = p_replaceString + i;
        for (int index = p_rawString.indexOf(toReplace); index >= 0 && i <= p_insertStrings.length; index = displayString.indexOf(toReplace)) {
            int ignoreIndex = displayString.indexOf(p_ignoreReplaceString);
            if (ignoreIndex < 0 || ignoreIndex + 1 != index) {
                displayString = displayString.replace(toReplace, nullStringConvert(p_insertStrings[i - 1]));
            }
            StringBuilder var10000 = new StringBuilder(String.valueOf(p_replaceString));
            ++i;
            toReplace = var10000.append(i).toString();
        }
        return displayString;
    }

    private static String nullStringConvert(final String p_string) {
        return Objects.isNull(p_string) ? "null" : p_string;
    }

    public static final String replaceInString(final String p_rawString, final String... p_insertStrings) {
        return replaceInString(p_rawString, DEFAULT_REPLACE_PREFIX, DEFAULT_NOT_REPLACE_PREFIX, p_insertStrings);
    }

    public static final String replaceInString(final String p_rawString, final List<String> p_insertStrings) {
        return replaceInString(p_rawString, DEFAULT_REPLACE_PREFIX, DEFAULT_NOT_REPLACE_PREFIX, p_insertStrings);
    }

    public static final String arrayToString(Object[] p_array) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < p_array.length; ++i) {
            if (i > 0) {
                sb.append(",").append(p_array[i].toString());
            } else {
                sb.append(p_array[i].toString());
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns string with replaced values by pattern
     *
     * @param initialValue - initial argument value
     * @param pattern      - initial pattern to be replaced by
     * @param replaceValue - initial value replacement by pattern occurrences
     * @return string with replaced values by pattern
     */
    public static String replaceAll(final String initialValue, final String pattern, final String replaceValue) {
        Objects.requireNonNull(initialValue);
        return initialValue.replaceAll(pattern, replaceValue);
    }

    /**
     * Returns string sanitized by default regex pattern {@see DEFAULT_ALPHANUMERIC_PATTERN}
     *
     * @param initialValue - initial argument value
     * @return string stripped by default regex pattern
     */
    public static String sanitize(final String initialValue) {
        return replaceAll(initialValue, DEFAULT_ALPHANUMERIC_PATTERN, org.apache.commons.lang3.StringUtils.EMPTY).trim();
    }
}
