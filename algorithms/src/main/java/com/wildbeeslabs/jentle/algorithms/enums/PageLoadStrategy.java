package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PageLoadStrategy {
    NONE("none"),
    EAGER("eager"),
    NORMAL("normal");

    private final String text;

    @Override
    public String toString() {
        return String.valueOf(text);
    }

    public static PageLoadStrategy fromString(final String text) {
        if (text != null) {
            for (final PageLoadStrategy b : PageLoadStrategy.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
