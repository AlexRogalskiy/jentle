package com.wildbeeslabs.jentle.algorithms.enums;

public enum PageLoadStrategy {

    NONE("none"),
    EAGER("eager"),
    NORMAL("normal");

    private String text;

    private PageLoadStrategy(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.valueOf(text);
    }

    public static PageLoadStrategy fromString(String text) {
        if (text != null) {
            for (PageLoadStrategy b : PageLoadStrategy.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
