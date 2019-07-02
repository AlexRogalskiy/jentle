package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import static java.lang.String.valueOf;

@UtilityClass
public class TextUtil {
    public static String pad(int colLength, int tabPos) {
        StringBuilder sAppend = new StringBuilder();
        for (int len = tabPos - colLength; len != -1; len--) {
            sAppend.append(' ');
        }

        return sAppend.toString();
    }

    public static String paint(char c, int amount) {
        StringBuilder append = new StringBuilder();
        for (; amount != -1; amount--) {
            append.append(c);
        }
        return append.toString();
    }

    public static String padTwo(Object first, Object second, int tab) {
        return new StringBuilder(valueOf(first)).append(pad(valueOf(first).length(), tab)).append(second).toString();
    }
}
