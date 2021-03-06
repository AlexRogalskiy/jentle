package com.wildbeeslabs.jentle.algorithms.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the known and supported Platforms that WebDriver runs on. This is pretty close to the
 * Operating System, but differs slightly, because this class is used to extract information such as
 * program locations and line endings.
 */
public enum Platform {
    /**
     * Never returned, but can be used to request a browser running on any version of Windows.
     */
    WINDOWS("") {
        @Override
        public Platform family() {
            return null;
        }
    },

    /**
     * For versions of Windows that "feel like" Windows XP. These are ones that store files in
     * "\Program Files\" and documents under "\\documents and settings\\username"
     */
    XP("Windows Server 2003", "xp", "windows", "winnt", "windows_nt", "windows nt") {
        @Override
        public Platform family() {
            return WINDOWS;
        }
    },

    /**
     * For versions of Windows that "feel like" Windows Vista.
     */
    VISTA("windows vista", "Windows Server 2008", "windows 7", "win7") {
        @Override
        public Platform family() {
            return WINDOWS;
        }
    },

    /**
     * For versions of Windows that "feel like" Windows 8.
     */
    WIN8("Windows Server 2012", "windows 8", "win8") {
        @Override
        public Platform family() {
            return WINDOWS;
        }
    },

    WIN8_1("windows 8.1", "win8.1") {
        @Override
        public Platform family() {
            return WINDOWS;
        }
    },

    WIN10("windows 10", "win10") {
        @Override
        public Platform family() {
            return WINDOWS;
        }
    },

    MAC("mac", "darwin", "macOS", "mac os x", "os x") {
        @Override
        public Platform family() {
            return null;
        }
    },

    SNOW_LEOPARD("snow leopard", "os x 10.6", "macos 10.6") {
        @Override
        public Platform family() {
            return MAC;
        }

        @Override
        public String toString() {
            return "OS X 10.6";
        }
    },

    MOUNTAIN_LION("mountain lion", "os x 10.8", "macos 10.8") {
        @Override
        public Platform family() {
            return MAC;
        }

        @Override
        public String toString() {
            return "OS X 10.8";
        }
    },

    MAVERICKS("mavericks", "os x 10.9", "macos 10.9") {
        @Override
        public Platform family() {
            return MAC;
        }

        @Override
        public String toString() {
            return "OS X 10.9";
        }
    },

    YOSEMITE("yosemite", "os x 10.10", "macos 10.10") {
        @Override
        public Platform family() {
            return MAC;
        }

        @Override
        public String toString() {
            return "OS X 10.10";
        }
    },

    EL_CAPITAN("el capitan", "os x 10.11", "macos 10.11") {
        @Override
        public Platform family() {
            return MAC;
        }

        @Override
        public String toString() {
            return "OS X 10.11";
        }
    },

    SIERRA("sierra", "os x 10.12", "macos 10.12") {
        @Override
        public Platform family() {
            return MAC;
        }

        @Override
        public String toString() {
            return "macOS 10.12";
        }
    },

    HIGH_SIERRA("high sierra", "os x 10.13", "macos 10.13") {
        @Override
        public Platform family() {
            return MAC;
        }

        @Override
        public String toString() {
            return "macOS 10.13";
        }
    },

    MOJAVE("mojave", "os x 10.14", "macos 10.14") {
        @Override
        public Platform family() {
            return MAC;
        }

        @Override
        public String toString() {
            return "macOS 10.14";
        }
    },

    /**
     * Many platforms have UNIX traits, amongst them LINUX, Solaris and BSD.
     */
    UNIX("solaris", "bsd") {
        @Override
        public Platform family() {
            return null;
        }
    },

    LINUX("linux") {
        @Override
        public Platform family() {
            return UNIX;
        }
    },

    ANDROID("android", "dalvik") {
        @Override
        public Platform family() {
            return LINUX;
        }
    },

    IOS("iOS") {
        @Override
        public Platform family() {
            return null;
        }
    },

    /**
     * Never returned, but can be used to request a browser running on any operating system.
     */
    ANY("") {
        @Override
        public Platform family() {
            return ANY;
        }

        @Override
        public boolean is(Platform compareWith) {
            return this == compareWith;
        }
    };

    private final String[] partOfOsName;
    private int minorVersion = 0;
    private int majorVersion = 0;

    Platform(final String... partOfOsName) {
        this.partOfOsName = partOfOsName;
    }

    public String[] getPartOfOsName() {
        return this.partOfOsName;
    }

    private static Platform current;

    /**
     * Get current platform (not necessarily the same as operating system).
     *
     * @return current platform
     */
    public static Platform getCurrent() {
        if (current == null) {
            current = extractFromSysProperty(System.getProperty("os.name"));

            final String version = System.getProperty("os.version", "0.0.0");
            int major = 0;
            int min = 0;

            final Pattern pattern = Pattern.compile("^(\\d+)\\.(\\d+).*");
            final Matcher matcher = pattern.matcher(version);
            if (matcher.matches()) {
                try {
                    major = Integer.parseInt(matcher.group(1));
                    min = Integer.parseInt(matcher.group(2));
                } catch (NumberFormatException e) {
                    // These things happen
                }
            }
            current.majorVersion = major;
            current.minorVersion = min;
        }
        return current;
    }

    /**
     * Extracts platforms based on system properties in Java and uses a heuristic to determine the
     * most likely operating system.  If unable to determine the operating system, it will default to
     * UNIX.
     *
     * @param osName the operating system name to determine the platform of
     * @return the most likely platform based on given operating system name
     */
    public static Platform extractFromSysProperty(final String osName) {
        return extractFromSysProperty(osName, System.getProperty("os.version"));
    }

    /**
     * Extracts platforms based on system properties in Java and uses a heuristic to determine the
     * most likely operating system.  If unable to determine the operating system, it will default to
     * UNIX.
     *
     * @param osName    the operating system name to determine the platform of
     * @param osVersion the operating system version to determine the platform of
     * @return the most likely platform based on given operating system name and version
     */
    public static Platform extractFromSysProperty(final String osName, final String osVersion) {
        final String result = osName.toLowerCase();
        // os.name for android is linux
        if ("dalvik".equalsIgnoreCase(System.getProperty("java.vm.name"))) {
            return Platform.ANDROID;
        }
        // Windows 8 can't be detected by osName alone
        if (osVersion.equals("6.2") && result.startsWith("windows nt")) {
            return WIN8;
        }
        // Windows 8 can't be detected by osName alone
        if (osVersion.equals("6.3") && result.startsWith("windows nt")) {
            return WIN8_1;
        }
        Platform mostLikely = UNIX;
        String previousMatch = null;
        for (final Platform os : Platform.values()) {
            for (String matcher : os.partOfOsName) {
                if ("".equals(matcher)) {
                    continue;
                }
                matcher = matcher.toLowerCase();
                if (os.isExactMatch(result, matcher)) {
                    return os;
                }
                if (os.isCurrentPlatform(result, matcher) && isBetterMatch(previousMatch, matcher)) {
                    previousMatch = matcher;
                    mostLikely = os;
                }
            }
        }

        // Default to assuming we're on a UNIX variant (including LINUX)
        return mostLikely;
    }

    /**
     * Gets a platform with the name matching the parameter.
     *
     * @param name the platform name
     * @return the Platform enum value matching the parameter
     */
    public static Platform fromString(String name) {
        for (Platform platform : values()) {
            if (platform.toString().equalsIgnoreCase(name)) {
                return platform;
            }
        }

        for (Platform os : Platform.values()) {
            for (String matcher : os.partOfOsName) {
                if (name.toLowerCase().equals(matcher.toLowerCase())) {
                    return os;
                }
            }
        }
        throw new IllegalArgumentException("Unrecognized platform: " + name);
    }

    /**
     * Decides whether the previous match is better or not than the current match.  If previous match
     * is null, the newer match is always better.
     *
     * @param previous the previous match
     * @param matcher  the newer match
     * @return true if newer match is better, false otherwise
     */
    private static boolean isBetterMatch(String previous, String matcher) {
        return previous == null || matcher.length() >= previous.length();
    }

    /**
     * Heuristic for comparing two platforms.  If platforms (which is not the same thing as operating
     * systems) are found to be approximately similar in nature, this will return true.  For instance
     * the LINUX platform is similar to UNIX, and will give a positive result if compared.
     *
     * @param compareWith the platform to compare with
     * @return true if platforms are approximately similar, false otherwise
     */
    public boolean is(final Platform compareWith) {
        return
            // Any platform is itself
            this == compareWith ||
                // Any platform is also ANY platform
                compareWith == ANY ||
                // And any Platform which is not a platform type belongs to the same family
                (this.family() != null && this.family().is(compareWith));
    }

    /**
     * Returns a platform that represents a family for the current platform.  For instance
     * the LINUX if a part of the UNIX family, the XP is a part of the WINDOWS family.
     *
     * @return the family platform for the current one, or {@code null} if this {@code Platform}
     * represents a platform family (such as Windows, or MacOS)
     */
    public abstract Platform family();

    private boolean isCurrentPlatform(final String osName, final String matchAgainst) {
        return osName.contains(matchAgainst);
    }

    private boolean isExactMatch(final String osName, final String matchAgainst) {
        return matchAgainst.equals(osName);
    }

    /**
     * Returns the major version of this platform.
     *
     * @return the major version of specified platform
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Returns the minor version of this platform.
     *
     * @return the minor version of specified platform
     */
    public int getMinorVersion() {
        return minorVersion;
    }

}
