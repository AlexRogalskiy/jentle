package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Enumeration of common operating systems used for testing Java applications.
 *
 * <p>If the current operating system cannot be detected &mdash; for example,
 * if the {@code os.name} JVM system property is undefined &mdash; then none
 * of the constants defined in this enum will be considered to be the
 * {@linkplain #isCurrentOs current operating system}.
 *
 * @see #AIX
 * @see #LINUX
 * @see #MAC
 * @see #SOLARIS
 * @see #WINDOWS
 * @see #OTHER
 * @see EnabledOnOs
 * @see DisabledOnOs
 * @since 5.1
 */
@Slf4j
public enum OS {

    /**
     * IBM AIX operating system.
     */
    AIX,

    /**
     * Linux-based operating system.
     */
    LINUX,

    /**
     * Apple Macintosh operating system (e.g., macOS).
     */
    MAC,

    /**
     * Oracle Solaris operating system.
     */
    SOLARIS,

    /**
     * Microsoft Windows operating system.
     */
    WINDOWS,

    /**
     * An operating system other than {@link #AIX}, {@link #LINUX}, {@link #MAC},
     * {@link #SOLARIS}, or {@link #WINDOWS}.
     */
    OTHER;

    private static final OS CURRENT_OS = determineCurrentOs();

    private static OS determineCurrentOs() {
        String osName = System.getProperty("os.name");

        if (StringUtils.isBlank(osName)) {
            log.debug("JVM system property 'os.name' is undefined. It is therefore not possible to detect the current OS.");
            return null;
        }

        osName = osName.toLowerCase(Locale.ENGLISH);

        if (osName.contains("aix")) {
            return AIX;
        }
        if (osName.contains("linux")) {
            return LINUX;
        }
        if (osName.contains("mac")) {
            return MAC;
        }
        if (osName.contains("sunos") || osName.contains("solaris")) {
            return SOLARIS;
        }
        if (osName.contains("win")) {
            return WINDOWS;
        }
        return OTHER;
    }

    /**
     * @return {@code true} if <em>this</em> {@code OS} is known to be the
     * operating system on which the current JVM is executing
     */
    public boolean isCurrentOs() {
        return this.equals(CURRENT_OS);
    }
}
