package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterType {
    ADJACENT_SIBLING("${1}+${2}", "next sibling"),
    AND("", "and"),
    ATTRIBUTE("[${1}]", "attribute"),
    ATTRIBUTE_VALUE("[${1}${2}${3}]", "attribute"),
    CHILD_OF("${2}>${1}", "child of"),
    CLASS(".${1}", "class"),
    DESCENDANT("${2} ${1}", "descendant of"),
    GENERAL_SIBLINGS("${1}~${2}", "siblings of"),
    ID("#${1}", "id"),
    OR(",", "or"),
    PARENT_OF("${1}>${2}", "parent of"),
    TAG("${1}", "tag");

    private final String format;
    private final String original;

    public static FilterType of(final String key) {
        for (final FilterType value : values()) {
            final String spaceless = value.original.replace(" ", "");
            if (key.equalsIgnoreCase(spaceless)) {
                return value;
            }
        }
        return null;
    }

    public String format(final String... values) {
        return String.format(this.format, values);
    }

    @Override
    public String toString() {
        return original;
    }
}
