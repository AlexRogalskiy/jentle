package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScalarStyle {
    DOUBLE_QUOTED('"'),
    SINGLE_QUOTED('\''),
    LITERAL('|'),
    FOLDED('>'),
    PLAIN(null);

    private final Character styleChar;

    @Override
    public String toString() {
        return "Scalar style: '" + this.styleChar + "'";
    }

    public static ScalarStyle createStyle(final Character style) {
        if (style == null) {
            return PLAIN;
        }
        switch (style) {
            case '"':
                return DOUBLE_QUOTED;
            case '\'':
                return SINGLE_QUOTED;
            case '|':
                return LITERAL;
            case '>':
                return FOLDED;
            default:
                return null;
            //throw new YAMLException("Unknown scalar style character: " + style);
        }
    }
}
