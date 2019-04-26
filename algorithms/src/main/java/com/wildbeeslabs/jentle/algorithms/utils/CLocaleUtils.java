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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Custom locale utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CLocaleUtils {

    /**
     * Default locale source
     */
    public static final String DEFAULT_LOCALE_SOURCE = "resources/words";

    private static final Map<String, Locale> LANG_LOCALES;
    private static final Map<String, java.util.Locale> LANG_JAVA_LOCALES;
    private static final Map<Locale, java.util.Locale> LOCALES;

    static {
        String[] isoLanguages = java.util.Locale.getISOLanguages();

        Map<String, Locale> langLocales = new HashMap<String, Locale>();
        Map<String, java.util.Locale> langJavaLocales = new HashMap<String, java.util.Locale>();
        Map<Locale, java.util.Locale> locales = new HashMap<Locale, java.util.Locale>();

        for (String isoLanguage : isoLanguages) {
            Locale locale = valueOf(isoLanguage);
            if (locale != null) {
                langLocales.put(isoLanguage, locale);
                java.util.Locale javaLocale = new java.util.Locale(isoLanguage);
                langJavaLocales.put(isoLanguage, javaLocale);
                locales.put(locale, javaLocale);
            }
        }
        LANG_LOCALES = Collections.unmodifiableMap(langLocales);
        LANG_JAVA_LOCALES = Collections.unmodifiableMap(langJavaLocales);
        LOCALES = Collections.unmodifiableMap(locales);
    }

    public static List<Long> parseAvailableLocaleIds(final String localeIdsValue) {
        if (StringUtils.isNotBlank(localeIdsValue)) {
            final String[] localeValues = localeIdsValue.split("[,]");
            if (localeValues.length > 0) {
                final List<Long> availableLocaleIds = new ArrayList<Long>(localeValues.length);
                for (final String localeValue : localeValues) {
                    availableLocaleIds.add(Long.valueOf(localeValue));
                }
                return availableLocaleIds;
            }
        }
        return null;
    }

    public static Locale parseLocale(final String language) {
        return StringUtils.isNotBlank(language) ? LANG_LOCALES.get(language) : null;
    }

    public static java.util.Locale parseJavaLocale(final String language) {
        return StringUtils.isNotBlank(language) ? LANG_JAVA_LOCALES.get(language) : null;
    }

    public static java.util.Locale parseJavaLocale(final Locale locale) {
        return locale != null ? LOCALES.get(locale) : null;
    }

    public static Map<String, Locale> getLangLocales() {
        return LANG_LOCALES;
    }

    public static Map<String, java.util.Locale> getLangJavaLocales() {
        return LANG_JAVA_LOCALES;
    }

    public static Map<Locale, java.util.Locale> getLocales() {
        return LOCALES;
    }

    public static Locale valueOf(final String code) {
        if (StringUtils.isNotBlank(code)) {
            for (final Locale value : Locale.getAvailableLocales()) {
                if (code.equalsIgnoreCase(value.getDisplayName())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static String getWord(final Locale currentLocale, final String key) {
        final ResourceBundle words = ResourceBundle.getBundle(DEFAULT_LOCALE_SOURCE, currentLocale);
        return words.getString(key);
    }
}
