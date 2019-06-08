package com.wildbeeslabs.jentle.algorithms.utils;

/**
 * Utility methods to generate annotated types and to convert between them.
 */
@SuppressWarnings("units")

// TODO: add fromTo methods for all useful unit combinations.
public class UnitsTools {
    // Acceleration
    public static final int mPERs2 = 1;

    // Angle
    public static final double rad = 1;
    public static final double deg = 1;

    public static double toRadians(double angdeg) {
        return Math.toRadians(angdeg);
    }

    public static double toDegrees(double angrad) {
        return Math.toDegrees(angrad);
    }

    // Area
    public static final int mm2 = 1;
    public static final int m2 = 1;
    public static final int km2 = 1;

    // Current
    public static final int A = 1;

    // Luminance
    public static final int cd = 1;

    // Lengths
    public static final int mm = 1;
    public static final int m = 1;
    public static final int km = 1;

    public static int fromMilliMeterToMeter(int mm) {
        return mm / 1000;
    }

    public static int fromMeterToMilliMeter(int m) {
        return m * 1000;
    }

    public static int fromMeterToKiloMeter(int m) {
        return m / 1000;
    }

    public static int fromKiloMeterToMeter(int km) {
        return km * 1000;
    }

    // Mass
    public static final int g = 1;
    public static final int kg = 1;

    public static int fromGramToKiloGram(int g) {
        return g / 1000;
    }

    public static int fromKiloGramToGram(int kg) {
        return kg * 1000;
    }

    // Speed
    public static final int mPERs = 1;
    public static final int kmPERh = 1;

    public static double fromMeterPerSecondToKiloMeterPerHour(double mps) {
        return mps * 3.6d;
    }

    public static double fromKiloMeterPerHourToMeterPerSecond(double kmph) {
        return kmph / 3.6d;
    }

    // Substance
    public static final int mol = 1;

    // Temperature
    public static final int K = 1;
    public static final int C = 1;

    public static int fromKelvinToCelsius(int k) {
        return k - (int) 273.15;
    }

    public static int fromCelsiusToKelvin(int c) {
        return c + (int) 273.15;
    }

    // Time
    public static final int s = 1;
    public static final int min = 1;
    public static final int h = 1;

    public static int fromSecondToMinute(int s) {
        return s / 60;
    }

    public static int fromMinuteToSecond(int min) {
        return min * 60;
    }

    public static int fromMinuteToHour(int min) {
        return min / 60;
    }

    public static int fromHourToMinute(int h) {
        return h * 60;
    }
}
