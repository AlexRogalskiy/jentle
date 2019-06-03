package com.wildbeeslabs.jentle.algorithms.enums;

import com.wildbeeslabs.jentle.algorithms.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * Enumeration of Java Runtime Environment (JRE) versions.
 *
 * <p>If the current JRE version cannot be detected &mdash; for example, if the
 * {@code java.version} JVM system property is undefined &mdash; then none of
 * the constants defined in this enum will be considered to be the
 * {@linkplain #isCurrentVersion current JRE version}.
 *
 * @see #JAVA_8
 * @see #JAVA_9
 * @see #JAVA_10
 * @see #JAVA_11
 * @see #OTHER
 * @see EnabledOnJre
 * @see DisabledOnJre
 * @since 5.1
 */
@Slf4j
public enum JRE {

    /**
     * Java 8.
     */
    JAVA_8,

    /**
     * Java 9.
     */
    JAVA_9,

    /**
     * Java 10.
     */
    JAVA_10,

    /**
     * Java 11.
     */
    JAVA_11,

    /**
     * A JRE version other than {@link #JAVA_8}, {@link #JAVA_9},
     * {@link #JAVA_10}, or {@link #JAVA_11}.
     */
    OTHER;

    private static final JRE CURRENT_VERSION = determineCurrentVersion();

    private static JRE determineCurrentVersion() {
        String javaVersion = System.getProperty("java.version");
        boolean javaVersionIsBlank = StringUtils.isBlank(javaVersion);

        if (javaVersionIsBlank) {
            log.debug("JVM system property 'java.version' is undefined. It is therefore not possible to detect Java 8.");
        }

        if (!javaVersionIsBlank && javaVersion.startsWith("1.8")) {
            return JAVA_8;
        }

        try {
            final Method versionMethod = Runtime.class.getMethod("version");
            final Object version = ReflectionUtils.invokeMethod(versionMethod, null);
            final Method majorMethod = version.getClass().getMethod("major");
            int major = (int) ReflectionUtils.invokeMethod(majorMethod, version);
            switch (major) {
                case 9:
                    return JAVA_9;
                case 10:
                    return JAVA_10;
                case 11:
                    return JAVA_11;
                default:
                    return OTHER;
            }
        } catch (Exception ex) {
            log.debug("Failed to determine the current JRE version via java.lang.Runtime.Version.", ex);
        }
        return null;
    }

    /**
     * @return {@code true} if <em>this</em> {@code JRE} is known to be the
     * Java Runtime Environment version for the currently executing JVM
     */
    public boolean isCurrentVersion() {
        return this.equals(CURRENT_VERSION);
    }
}
