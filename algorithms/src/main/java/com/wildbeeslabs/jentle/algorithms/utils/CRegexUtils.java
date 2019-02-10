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

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom pool utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CRegexUtils {

    /**
     * Default regex patterns
     */
    private static final Pattern DEFAULT_PASSWORD_STRENGH_PATTERN = Pattern.compile("^(?=.*[A-Z].*[A-Z])(?=.*[!@#$&amp;*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}$");

    private static final Pattern DEFAULT_HEX_COLOR_PATTERN = Pattern.compile("^\\#([a-fA-F]|[0-9]){3,6}$");

    private static final Pattern DEFAULT_EMAIL_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");//S "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    private static final Pattern DEFAULT_IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern DEFAULT_IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern DEFAUL_IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    private static final String TO_ESCAPE = "\\.[]{}()*+-?^$|";

    public static String escape(final String literal) {
        String escaped = literal;
        for (int i = 0; i < TO_ESCAPE.length(); i++) {
            char c = TO_ESCAPE.charAt(i);
            escaped = escaped.replace(c + "", "\\" + c);
        }
        return escaped;
    }

    public static int execute(final String regex, final String text) {
        return CRegexUtils.execute(regex, text, 0);
    }

    public static int execute(final String regex, final String text, int flags) {
        final Matcher matcher = CRegexUtils.matcher(regex, text, flags);
        int matches = 0;
        while (matcher.find()) {
            matches++;
        }
        return matches;
    }

    public static Matcher matcher(@NonNull final String regex, @NonNull final String text, int flags) {
        assert (flags >= 0);
        final Pattern pattern = Pattern.compile(regex, flags);
        return pattern.matcher(text);
    }

    public static boolean isIPv4Address(final String input) {
        return CRegexUtils.DEFAULT_IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6StdAddress(final String input) {
        return CRegexUtils.DEFAULT_IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        return CRegexUtils.DEFAUL_IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {
        return CRegexUtils.isIPv6StdAddress(input) || CRegexUtils.isIPv6HexCompressedAddress(input);
    }

    public static boolean isValidEmailAddress(final String value, final Boolean allowLocal) {
        return EmailValidator.getInstance(allowLocal).isValid(value);
    }

    public boolean isValidEmailAddress(final String input) {
        return CRegexUtils.DEFAULT_EMAIL_PATTERN.matcher(input).matches();
    }

    public boolean isValidHexColor(final String input) {
        return CRegexUtils.DEFAULT_HEX_COLOR_PATTERN.matcher(input).matches();
    }

    public boolean isValidStrengthPassword(final String input) {
        return CRegexUtils.DEFAULT_PASSWORD_STRENGH_PATTERN.matcher(input).matches();
    }
}
