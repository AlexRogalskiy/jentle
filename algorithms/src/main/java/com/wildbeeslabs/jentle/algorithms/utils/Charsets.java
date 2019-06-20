package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Helps dealing with Charsets.
 *
 * @since 3.3
 */
@UtilityClass
public class Charsets {

    /**
     * Returns a charset object for the given charset name.
     *
     * @param charsetName The name of the requested charset; may be a canonical name, an alias, or null. If null, return the
     *                    default charset.
     * @return A charset object for the named charset
     */
    public static Charset toCharset(final String charsetName) {
        return Objects.isNull(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    /**
     * Returns a charset object for the given charset name.
     *
     * @param charsetName        The name of the requested charset; may be a canonical name, an alias, or null.
     *                           If null, return the default charset.
     * @param defaultCharsetName the charset name to use if the requested charset is null
     * @return A charset object for the named charset
     */
    public static Charset toCharset(final String charsetName, final String defaultCharsetName) {
        return Objects.isNull(charsetName) ? Charset.forName(defaultCharsetName) : Charset.forName(charsetName);
    }
}
