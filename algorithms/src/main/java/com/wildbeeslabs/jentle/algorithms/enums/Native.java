package com.wildbeeslabs.jentle.algorithms.enums;

public enum Native {
    ;

    private enum OS {
        // Even on Windows, the default compiler from cpptasks (gcc) uses .so as a shared lib extension
        WINDOWS("win32", "so"), LINUX("linux", "so"), MAC("darwin", "dylib"), SOLARIS("solaris", "so");
        public final String name, libExtension;

        private OS(String name, String libExtension) {
            this.name = name;
            this.libExtension = libExtension;
        }
    }

    private static String arch() {
        return System.getProperty("os.arch");
    }

    private static OS os() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Linux")) {
            return OS.LINUX;
        } else if (osName.contains("Mac")) {
            return OS.MAC;
        } else if (osName.contains("Windows")) {
            return OS.WINDOWS;
        } else if (osName.contains("Solaris") || osName.contains("SunOS")) {
            return OS.SOLARIS;
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: "
                + osName);
        }
    }
}
