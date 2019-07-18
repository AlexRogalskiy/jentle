package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Specification version. Currently supported 1.0 and 1.1
 */
public enum Version {
    V1_0(new Integer[]{1, 0}), V1_1(new Integer[]{1, 1});

    private Integer[] version;

    private Version(Integer[] version) {
        this.version = version;
    }

    public int major() {
        return version[0];
    }

    public int minor() {
        return version[1];
    }

    public String getRepresentation() {
        return version[0] + "." + version[1];
    }

    @Override
    public String toString() {
        return "Version: " + getRepresentation();
    }
}
