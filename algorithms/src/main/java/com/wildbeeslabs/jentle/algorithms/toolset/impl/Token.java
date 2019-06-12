package com.wildbeeslabs.jentle.algorithms.toolset.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Element that is parsed from the format pattern.
 */
@Data
@EqualsAndHashCode
@ToString
public class Token {

    /**
     * Helper method to determine if a set of tokens contain a value
     *
     * @param tokens set to look in
     * @param value  to look for
     * @return boolean <code>true</code> if contained
     */
    public static boolean containsTokenWithValue(final Token[] tokens, final Object value) {
        for (final Token token : tokens) {
            if (token.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    private final Object value;
    private int count;

    /**
     * Wraps a token around a value. A value would be something like a 'Y'.
     *
     * @param value to wrap
     */
    Token(final Object value) {
        this.value = value;
        this.count = 1;
    }

    /**
     * Wraps a token around a repeated number of a value, for example it would
     * store 'yyyy' as a value for y and a count of 4.
     *
     * @param value to wrap
     * @param count to wrap
     */
    public Token(final Object value, final int count) {
        this.value = value;
        this.count = count;
    }

    /**
     * Adds another one of the value
     */
    void increment() {
        this.count++;
    }
}
