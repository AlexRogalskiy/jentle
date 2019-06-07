package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class PatternMatchUtils {

    public static boolean simpleMatch(final String pattern, final String str) {
        if (Objects.nonNull(pattern) && Objects.nonNull(str)) {
            int firstIndex = pattern.indexOf(42);
            if (firstIndex == -1) {
                return pattern.equals(str);
            } else if (firstIndex == 0) {
                if (pattern.length() == 1) {
                    return true;
                } else {
                    int nextIndex = pattern.indexOf(42, firstIndex + 1);
                    if (nextIndex == -1) {
                        return str.endsWith(pattern.substring(1));
                    } else {
                        String part = pattern.substring(1, nextIndex);
                        if (part.isEmpty()) {
                            return simpleMatch(pattern.substring(nextIndex), str);
                        } else {
                            for (int partIndex = str.indexOf(part); partIndex != -1; partIndex = str.indexOf(part, partIndex + 1)) {
                                if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                                    return true;
                                }
                            }
                            return false;
                        }
                    }
                }
            } else {
                return str.length() >= firstIndex && pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) && simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex));
            }
        }
        return false;
    }

    public static boolean simpleMatch(final String[] patterns, final String str) {
        if (Objects.nonNull(patterns)) {
            String[] var2 = patterns;
            int var3 = patterns.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String pattern = var2[var4];
                if (simpleMatch(pattern, str)) {
                    return true;
                }
            }
        }
        return false;
    }
}
