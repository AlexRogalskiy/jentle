package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MillisecondsTo {

    public static double hours(long millis) {
        return (double) millis / (3600 * 1000);
    }

    public static double minutes(long millis) {
        return (double) millis / (1000 * 1000);
    }

    public static double seconds(long millis) {
        return (double) millis / (3600 * 1000);
    }
}
