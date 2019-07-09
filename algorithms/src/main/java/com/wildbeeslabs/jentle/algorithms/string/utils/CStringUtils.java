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
package com.wildbeeslabs.jentle.algorithms.string.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.wildbeeslabs.jentle.algorithms.utils.CDigestUtils;
import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.exception.OverflowStackException;
import com.wildbeeslabs.jentle.collections.queue.iface.IStack;
import com.wildbeeslabs.jentle.collections.stack.CBoundStack;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import pl.allegro.finance.tradukisto.MoneyConverters;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    private static final int MaxCachedBuilderSize = 8 * 1024;
    private static final int MaxIdleBuilders = 8;
    private static final Stack<StringBuilder> builders = new Stack<>();

    /**
     * Default hex digits array
     */
    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();
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
     * Default numeric pattern format
     */
    public static final String DEFAULT_FORMAT_PATTERN = "#.##";
    /**
     * Default locale delimiter
     */
    public static final String DEFAULT_LOCALE_DELIMITER = "_";

    /**
     * Default string length {@link Function}
     */
    public static final Function<String, Integer> DEFAULT_STRING_LENGTH = (s) -> s.length();

    /**
     * Default decimal format instance {@link DecimalFormat}
     */
    public static final ThreadLocal<DecimalFormat> DEFAULT_DECIMAL_FORMATTER = ThreadLocal.withInitial(() -> {
        final DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
        return new DecimalFormat(DEFAULT_FORMAT_PATTERN, decimalSymbols);
    });

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
        return first.indexOf(last) >= 0;
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

    private static String toHexString(final byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String[] split(final String value, final String delimiter) {
        if (Objects.isNull(value) || Objects.isNull(delimiter)) {
            return null;
        }
        return Optional.ofNullable(value).filter(StringUtils::isNotEmpty).map(str -> str.trim().split(delimiter)).orElse(null);//"\\s+|,\\s*|\\.\\s*"
        //StringUtils.split(value, delimiter);
    }

    public static String chop(final String value) {
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

    public static boolean brackets(final String value, int maxDeep) {
        try {
            final IStack<Character> stack = new CBoundStack<>(maxDeep, Character[].class);
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
        }
        final String[] elements = p_text.split(System.lineSeparator());
        final StringBuilder result = new StringBuilder();
        int size = elements.length;
        for (int i = 0; i < size; ++i) {
            result.append(System.lineSeparator()).append(elements[i].trim());
        }
        result.delete(0, 1);
        return result.toString();
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

    /**
     * Returns {@link UUID} instance by input {@link String}
     *
     * @param name - initial input {@link String}
     * @return {@link UUID} instance
     */
    public static UUID fromString(final String name) {
        final String[] components = name.split("-");
        if (components.length != 5) {
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        }
        for (int i = 0; i < 5; i++) {
            components[i] = "0x" + components[i];
        }

        long mostSigBits = Long.decode(components[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]).longValue();

        long leastSigBits = Long.decode(components[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]).longValue();

        return new UUID(mostSigBits, leastSigBits);
    }

    public static String getBinaryToken(final String path, final String secret, final Long expire) throws NoSuchAlgorithmException {
        final byte[] messageDigest = CDigestUtils.md5(getString(path, secret, expire.toString()));
        return Base64.getEncoder().encodeToString(messageDigest);
    }

    /**
     * Returns string formatted decimal number by default formatter instance {@link DecimalFormat}
     *
     * @param num - initial input decimal number
     * @return string formatted decimal number
     */
    public static String format(double num) {
        return DEFAULT_DECIMAL_FORMATTER.get().format(num);
    }

    public static String getString(final String... values) {
        return getStringByDelimiter(org.apache.commons.lang3.StringUtils.EMPTY, values);
    }

    public static String getStringByDelimiter(final String delimiter, final String... values) {
        return org.apache.commons.lang3.StringUtils.join(values, delimiter);
    }

    /**
     * Returns {@link String} by input {@link Locale} instance
     *
     * @param locale - input {@link Locale} instance
     * @return locale string representation
     */
    public static String localeToString(@NonNull final Locale locale) {
        return Joiner.on(DEFAULT_LOCALE_DELIMITER).join(locale.getLanguage(), locale.getCountry());
    }

    /**
     * Returns {@link Locale} by input locale string representation
     *
     * @param locale - input locale string representation
     * @return {@link Locale}
     */
    public static Locale stringToLocale(@NonNull final String locale) {
        return LocaleUtils.toLocale(locale);
    }

    /**
     * Returns {@link List} collection of tokens {@link String} by delimiter {@link String}
     *
     * @param str   - input string to tokenize
     * @param delim - input token delimiter
     * @return {@link List} collection of tokens {@link String}
     */
    public List<String> getTokensWithCollection(@NonNull final String str, @NonNull final String delim) {
        return Collections.list(new StringTokenizer(str, delim)).stream()
            .map(token -> (String) token)
            .collect(Collectors.toList());
    }

    public static <K, V> String convertToStr(@NonNull final Map<K, V> map) {
        return Joiner.on(",").withKeyValueSeparator("=").join(map);
    }

    public static String asString() {
        final StringBuilder sb = new StringBuilder();
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            sb.append(stackTrace[i]).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private Iterable<String> splitBy(final String compValue, final String separator) {
        return Splitter.on(separator).omitEmptyStrings().split(compValue);
    }

    /**
     * Tests if a code point is "whitespace" as defined by what it looks like. Used for Element.text etc.
     *
     * @param c code point to test
     * @return true if code point is whitespace, false otherwise
     */
    public static boolean isActuallyWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r' || c == 160;
        // 160 is &nbsp; (non-breaking space). Not in the spec but expected.
    }

    /**
     * Normalise the whitespace within this string; multiple spaces collapse to a single, and all whitespace characters
     * (e.g. newline, tab) convert to a simple space
     *
     * @param string content to normalise
     * @return normalised string
     */
    public static String normaliseWhitespace(final String string) {
        StringBuilder sb = borrowBuilder();
        appendNormalisedWhitespace(sb, string, false);
        return releaseBuilder(sb);
    }

    /**
     * After normalizing the whitespace within a string, appends it to a string builder.
     *
     * @param accum        builder to append to
     * @param string       string to normalize whitespace within
     * @param stripLeading set to true if you wish to remove any leading whitespace
     */
    public static void appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading) {
        boolean lastWasWhite = false;
        boolean reachedNonWhite = false;

        int len = string.length();
        int c;
        for (int i = 0; i < len; i += Character.charCount(c)) {
            c = string.codePointAt(i);
            if (isActuallyWhitespace(c)) {
                if ((stripLeading && !reachedNonWhite) || lastWasWhite)
                    continue;
                accum.append(' ');
                lastWasWhite = true;
            } else if (!isInvisibleChar(c)) {
                accum.appendCodePoint(c);
                lastWasWhite = false;
                reachedNonWhite = true;
            }
        }
    }

    /**
     * Create a new absolute URL, from a provided existing absolute URL and a relative URL component.
     *
     * @param base   the existing absolute base URL
     * @param relUrl the relative URL to resolve. (If it's already absolute, it will be returned)
     * @return the resolved absolute URL
     * @throws MalformedURLException if an error occurred generating the URL
     */
    public static URL resolve(URL base, String relUrl) throws MalformedURLException {
        // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
        if (relUrl.startsWith("?"))
            relUrl = base.getPath() + relUrl;
        // workaround: //example.com + ./foo = //example.com/./foo, not //example.com/foo
        if (relUrl.indexOf('.') == 0 && base.getFile().indexOf('/') != 0) {
            base = new URL(base.getProtocol(), base.getHost(), base.getPort(), "/" + base.getFile());
        }
        return new URL(base, relUrl);
    }

    public static String resolve(final String baseUrl, final String relUrl) {
        URL base;
        try {
            try {
                base = new URL(baseUrl);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute/rel may be abs on its own, so try that
                URL abs = new URL(relUrl);
                return abs.toExternalForm();
            }
            return resolve(base, relUrl).toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    public static StringBuilder borrowBuilder() {
        synchronized (builders) {
            return builders.empty() ?
                new StringBuilder(MaxCachedBuilderSize) :
                builders.pop();
        }
    }

    public static String releaseBuilder(StringBuilder sb) {
        String string = sb.toString();

        if (sb.length() > MaxCachedBuilderSize)
            sb = new StringBuilder(MaxCachedBuilderSize); // make sure it hasn't grown too big
        else
            sb.delete(0, sb.length()); // make sure it's emptied on release

        synchronized (builders) {
            builders.push(sb);

            while (builders.size() > MaxIdleBuilders) {
                builders.pop();
            }
        }
        return string;
    }

    public static boolean isInvisibleChar(int c) {
        return Character.getType(c) == 16 && (c == 8203 || c == 8204 || c == 8205 || c == 173);
    }

    /**
     * Computes the Levenshtein distance of two strings in a matrix.
     * Based on pseudo-code provided here:
     * https://en.wikipedia.org/wiki/Levenshtein_distance#Computing_Levenshtein_distance
     * which in turn is based on the paper Wagner, Robert A.; Fischer, Michael J. (1974),
     * "The String-to-String Correction Problem", Journal of the ACM 21 (1): 168-173
     *
     * @param wordForm the form
     * @param lemma    the lemma
     * @return the distance
     */
    public static int[][] levenshteinDistance(String wordForm, String lemma) {
        int wordLength = wordForm.length();
        int lemmaLength = lemma.length();
        int cost;
        int[][] distance = new int[wordLength + 1][lemmaLength + 1];

        if (wordLength == 0) {
            return distance;
        }
        if (lemmaLength == 0) {
            return distance;
        }
        //fill in the rows of column 0
        for (int i = 0; i <= wordLength; i++) {
            distance[i][0] = i;
        }
        //fill in the columns of row 0
        for (int j = 0; j <= lemmaLength; j++) {
            distance[0][j] = j;
        }
        //fill in the rest of the matrix calculating the minimum distance
        for (int i = 1; i <= wordLength; i++) {
            int s_i = wordForm.charAt(i - 1);
            for (int j = 1; j <= lemmaLength; j++) {
                if (s_i == lemma.charAt(j - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                //obtain minimum distance from calculating deletion, insertion, substitution
                distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1]
                    + 1, distance[i - 1][j - 1] + cost);
            }
        }
        return distance;
    }

    /**
     * Computes the Shortest Edit Script (SES) to convert a word into its lemma.
     * This is based on Chrupala's PhD thesis (2008).
     *
     * @param wordForm     the token
     * @param lemma        the target lemma
     * @param distance     the levenshtein distance
     * @param permutations the number of permutations
     */
    public static void computeShortestEditScript(String wordForm, String lemma,
                                                 int[][] distance, StringBuffer permutations) {

        int n = distance.length;
        int m = distance[0].length;

        int wordFormLength = n - 1;
        int lemmaLength = m - 1;
        while (true) {

            if (distance[wordFormLength][lemmaLength] == 0) {
                break;
            }
            if ((lemmaLength > 0 && wordFormLength > 0) && (distance[wordFormLength - 1][lemmaLength - 1]
                < distance[wordFormLength][lemmaLength])) {
                permutations.append('R').append(Integer.toString(wordFormLength - 1))
                    .append(wordForm.charAt(wordFormLength - 1)).append(lemma.charAt(lemmaLength - 1));
                lemmaLength--;
                wordFormLength--;
                continue;
            }
            if (lemmaLength > 0 && (distance[wordFormLength][lemmaLength - 1]
                < distance[wordFormLength][lemmaLength])) {
                permutations.append('I').append(Integer.toString(wordFormLength))
                    .append(lemma.charAt(lemmaLength - 1));
                lemmaLength--;
                continue;
            }
            if (wordFormLength > 0 && (distance[wordFormLength - 1][lemmaLength]
                < distance[wordFormLength][lemmaLength])) {
                permutations.append('D').append(Integer.toString(wordFormLength - 1))
                    .append(wordForm.charAt(wordFormLength - 1));
                wordFormLength--;
                continue;
            }
            if ((wordFormLength > 0 && lemmaLength > 0) && (distance[wordFormLength - 1][lemmaLength - 1]
                == distance[wordFormLength][lemmaLength])) {
                wordFormLength--;
                lemmaLength--;
                continue;
            }
            if (wordFormLength > 0 && (distance[wordFormLength - 1][lemmaLength]
                == distance[wordFormLength][lemmaLength])) {
                wordFormLength--;
                continue;
            }
            if (lemmaLength > 0 && (distance[wordFormLength][lemmaLength - 1]
                == distance[wordFormLength][lemmaLength])) {
                lemmaLength--;
            }
        }
    }

    /**
     * Read predicted SES by the lemmatizer model and apply the
     * permutations to obtain the lemma from the wordForm.
     *
     * @param wordForm     the wordForm
     * @param permutations the permutations predicted by the lemmatizer model
     * @return the lemma
     */
    public static String decodeShortestEditScript(String wordForm, String permutations) {

        StringBuffer lemma = new StringBuffer(wordForm).reverse();

        int permIndex = 0;
        while (true) {
            if (permutations.length() <= permIndex) {
                break;
            }
            //read first letter of permutation string
            char nextOperation = permutations.charAt(permIndex);
            //System.err.println("-> NextOP: " + nextOperation);
            //go to the next permutation letter
            permIndex++;
            if (nextOperation == 'R') {
                String charAtPerm = Character.toString(permutations.charAt(permIndex));
                int charIndex = Integer.parseInt(charAtPerm);
                // go to the next character in the permutation buffer
                // which is the replacement character
                permIndex++;
                char replace = permutations.charAt(permIndex);
                //go to the next char in the permutation buffer
                // which is the candidate character
                permIndex++;
                char with = permutations.charAt(permIndex);

                if (lemma.length() <= charIndex) {
                    return wordForm;
                }
                if (lemma.charAt(charIndex) == replace) {
                    lemma.setCharAt(charIndex, with);
                }
                //System.err.println("-> ROP: " + lemma.toString());
                //go to next permutation
                permIndex++;

            } else if (nextOperation == 'I') {
                String charAtPerm = Character.toString(permutations.charAt(permIndex));
                int charIndex = Integer.parseInt(charAtPerm);
                permIndex++;
                //character to be inserted
                char in = permutations.charAt(permIndex);

                if (lemma.length() < charIndex) {
                    return wordForm;
                }
                lemma.insert(charIndex, in);
                //System.err.println("-> IOP " + lemma.toString());
                //go to next permutation
                permIndex++;
            } else if (nextOperation == 'D') {
                String charAtPerm = Character.toString(permutations.charAt(permIndex));
                int charIndex = Integer.parseInt(charAtPerm);
                if (lemma.length() <= charIndex) {
                    return wordForm;
                }
                lemma.deleteCharAt(charIndex);
                permIndex++;
                // go to next permutation
                permIndex++;
            }
        }
        return lemma.reverse().toString();
    }

    /**
     * Get the SES required to go from a word to a lemma.
     *
     * @param wordForm the word
     * @param lemma    the lemma
     * @return the shortest edit script
     */
    public static String getShortestEditScript(String wordForm, String lemma) {
        String reversedWF = new StringBuffer(wordForm.toLowerCase()).reverse().toString();
        String reversedLemma = new StringBuffer(lemma.toLowerCase()).reverse().toString();
        StringBuffer permutations = new StringBuffer();
        String ses;
        if (!reversedWF.equals(reversedLemma)) {
            int[][] levenDistance = opennlp.tools.util.StringUtil.levenshteinDistance(reversedWF, reversedLemma);
            StringUtil.computeShortestEditScript(reversedWF, reversedLemma, levenDistance, permutations);
            ses = permutations.toString();
        } else {
            ses = "O";
        }
        return ses;
    }

    /**
     * Get mininum of three values.
     *
     * @param a number a
     * @param b number b
     * @param c number c
     * @return the minimum
     */
    private static int minimum(int a, int b, int c) {
        int minValue;
        minValue = a;
        if (b < minValue) {
            minValue = b;
        }
        if (c < minValue) {
            minValue = c;
        }
        return minValue;
    }
}
