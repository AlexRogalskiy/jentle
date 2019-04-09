package com.wildbeeslabs.jentle.algorithms.string;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Intern {
    /**
     * Default {@link ConcurrentMap} cache
     */
    private static final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

//  public static String intern(String s) {
//      String previousValue = map.putIfAbsent(s, s);
//      return previousValue == null ? s : previousValue;
//  }

    public static String intern(final String s) {
        String result = map.get(s);
        if (Objects.isNull(result)) {
            result = map.putIfAbsent(s, s);
            if (Objects.isNull(result)) {
                result = s;
            }
        }
        return result;
    }
}
