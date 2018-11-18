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

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom locale utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CLocaleUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CLocaleUtils.class);
    /**
     * Default locale source
     */
    public static final String DEFAULT_LOCALE_SOURCE = "resources/words";

    private CLocaleUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static String getWord(final Locale currentLocale, final String key) {
        final ResourceBundle words = ResourceBundle.getBundle(DEFAULT_LOCALE_SOURCE, currentLocale);
        return words.getString(key);
    }
}
