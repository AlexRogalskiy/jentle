package com.wildbeeslabs.jentle.algorithms.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decamelizer {

    private static final Pattern CAPS = Pattern.compile("([A-Z\\d][^A-Z\\d]*)");

    public static String decamelizeMatcher(final String className) {
        if (className.length() == 0) {
            return "<custom argument matcher>";
        }

        String decamelized = decamelizeClassName(className);

        if (decamelized.length() == 0) {
            return "<" + className + ">";
        }

        return "<" + decamelized + ">";
    }

    private static String decamelizeClassName(final String className) {
        final Matcher match = CAPS.matcher(className);
        final StringBuilder deCameled = new StringBuilder();
        while (match.find()) {
            if (deCameled.length() == 0) {
                deCameled.append(match.group());
            } else {
                deCameled.append(" ");
                deCameled.append(match.group().toLowerCase());
            }
        }
        return deCameled.toString();
    }
}
