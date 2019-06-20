package com.wildbeeslabs.jentle.algorithms.toolset;

import com.wildbeeslabs.jentle.algorithms.utils.CValidationUtils;
import com.wildbeeslabs.jentle.algorithms.utils.StringUtils;

import java.util.Objects;

/**
 * Value object to represent a Version consisting of major, minor and bugfix part.
 *
 * @author Oliver Gierke
 */
public class Version implements Comparable<Version> {

    private static final String VERSION_PARSE_ERROR = "Invalid version string! Could not parse segment %s within %s.";

    private final int major;
    private final int minor;
    private final int bugfix;
    private final int build;

    /**
     * Creates a new {@link Version} from the given integer values. At least one value has to be given but a maximum of 4.
     *
     * @param parts must not be {@literal null} or empty.
     */
    public Version(final int... parts) {
        CValidationUtils.notNull(parts, "Parts must not be null!");
        CValidationUtils.isTrue(parts.length > 0 && parts.length < 5, String.format("Invalid parts length. 0 < %s < 5", parts.length));

        this.major = parts[0];
        this.minor = parts.length > 1 ? parts[1] : 0;
        this.bugfix = parts.length > 2 ? parts[2] : 0;
        this.build = parts.length > 3 ? parts[3] : 0;

        CValidationUtils.isTrue(major >= 0, "Major version must be greater or equal zero!");
        CValidationUtils.isTrue(minor >= 0, "Minor version must be greater or equal zero!");
        CValidationUtils.isTrue(bugfix >= 0, "Bugfix version must be greater or equal zero!");
        CValidationUtils.isTrue(build >= 0, "Build version must be greater or equal zero!");
    }

    /**
     * Parses the given string representation of a version into a {@link Version} object.
     *
     * @param version must not be {@literal null} or empty.
     * @return returns version
     */
    public static Version parse(String version) {
        CValidationUtils.isTrue(StringUtils.isNotBlank(version), "Version must not be null o empty!");

        String[] parts = version.trim().split("\\.");
        int[] intParts = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {

            String input = i == parts.length - 1 ? parts[i].replaceAll("\\D.*", "") : parts[i];

            if (StringUtils.isNotBlank(input)) {
                try {
                    intParts[i] = Integer.parseInt(input);
                } catch (IllegalArgumentException o_O) {
                    throw new IllegalArgumentException(String.format(VERSION_PARSE_ERROR, input, version), o_O);
                }
            }
        }

        return new Version(intParts);
    }

    /**
     * Returns whether the current {@link Version} is greater (newer) than the given one.
     *
     * @param version - candidate version
     * @return true or false based on version comparison
     */
    public boolean isGreaterThan(Version version) {
        return compareTo(version) > 0;
    }

    /**
     * Returns whether the current {@link Version} is greater (newer) or the same as the given one.
     *
     * @param version - candidate version
     * @return true or false based on version comparison
     */
    public boolean isGreaterThanOrEqualTo(Version version) {
        return compareTo(version) >= 0;
    }

    /**
     * Returns whether the current {@link Version} is the same as the given one.
     *
     * @param version - candidate version
     * @return true or false based on version comparison
     */
    public boolean is(final Version version) {
        return equals(version);
    }

    /**
     * Returns whether the current {@link Version} is less (older) than the given one.
     *
     * @param version - candidate version
     * @return true or false based on version comparison
     */
    public boolean isLessThan(Version version) {
        return compareTo(version) < 0;
    }

    /**
     * Returns whether the current {@link Version} is less (older) or equal to the current one.
     *
     * @param version - candidate version
     * @return true or false based on version comparison
     */
    public boolean isLessThanOrEqualTo(Version version) {
        return compareTo(version) <= 0;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Version that) {
        if (Objects.isNull(that)) {
            return 1;
        }
        if (this.major != that.major) {
            return this.major - that.major;
        }
        if (this.minor != that.minor) {
            return this.minor - that.minor;
        }
        if (this.bugfix != that.bugfix) {
            return this.bugfix - that.bugfix;
        }
        if (this.build != that.build) {
            return this.build - that.build;
        }
        return 0;
    }
}
