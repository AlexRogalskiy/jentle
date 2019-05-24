package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.StringTokenizer;

/**
 * Custom jvm utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@UtilityClass
public class CJvmUtils {

    private static final Version VERSION = parseVersion(System.getProperty("java.specification.version"));
    public static final boolean IS_JAVA9_COMPATIBLE = VERSION.isJava9Compatible();

    // Package private for testing
    public static Version parseVersion(final String versionString) {
        final StringTokenizer st = new StringTokenizer(versionString, ".");
        int majorVersion = Integer.parseInt(st.nextToken());
        int minorVersion;
        if (st.hasMoreTokens())
            minorVersion = Integer.parseInt(st.nextToken());
        else
            minorVersion = 0;
        return new Version(majorVersion, minorVersion);
    }

    public static boolean isIbmJdk() {
        return System.getProperty("java.vendor").contains("IBM");
    }

    @Getter
    @RequiredArgsConstructor
    public static class Version {
        public final int majorVersion;
        public final int minorVersion;

        // Package private for testing
        boolean isJava9Compatible() {
            return this.majorVersion >= 9;
        }
    }
}
