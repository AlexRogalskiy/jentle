/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.utils;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import lombok.experimental.UtilityClass;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Custom version utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@UtilityClass
public class CVersionUtils {

    private final static Pattern V_SEP = Pattern.compile("[-_./;:]");

    public Version version() {
        return Version.unknownVersion();
    }

    /**
     * Helper method that will try to load version information for specified
     * class. Implementation is as follows:
     * <p>
     * First, tries to load version info from a class named
     * "PackageVersion" in the same package as the class.
     * <p>
     * If no version information is found, {@link Version#unknownVersion()} is returned.
     */
    public static Version versionFor(final Class<?> cls) {
        Version version = packageVersionFor(cls);
        return Optional.ofNullable(version).orElseGet(() -> Version.unknownVersion());
    }

    /**
     * Loads version information by introspecting a class named
     * "PackageVersion" in the same package as the given class.
     * <p>
     * If the class could not be found or does not have a public
     * static Version field named "VERSION", returns null.
     */
    public static Version packageVersionFor(final Class<?> cls) {
        Version v = null;
        try {
            final String versionInfoClassName = cls.getPackage().getName() + ".PackageVersion";
            final Class<?> vClass = Class.forName(versionInfoClassName, true, cls.getClassLoader());
            // However, if class exists, it better work correctly, no swallowing exceptions
            try {
                v = ((Versioned) vClass.getDeclaredConstructor().newInstance()).version();
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to get Versioned out of " + vClass);
            }
        } catch (Exception e) {
        }
        return Optional.ofNullable(v).orElseGet(() -> Version.unknownVersion());
    }

    /**
     * Will attempt to load the maven version for the given groupId and
     * artifactId.  Maven puts a pom.properties file in
     * META-INF/maven/groupId/artifactId, containing the groupId,
     * artifactId and version of the library.
     *
     * @param cl         the ClassLoader to load the pom.properties file from
     * @param groupId    the groupId of the library
     * @param artifactId the artifactId of the library
     * @return The version
     */
    @SuppressWarnings("resource")
    public static Version mavenVersionFor(final ClassLoader cl, final String groupId, final String artifactId) {
        final InputStream pomProperties = cl.getResourceAsStream("META-INF/maven/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/pom.properties");
        if (pomProperties != null) {
            try {
                final Properties props = new Properties();
                props.load(pomProperties);
                String versionStr = props.getProperty("version");
                String pomPropertiesArtifactId = props.getProperty("artifactId");
                String pomPropertiesGroupId = props.getProperty("groupId");
                return parseVersion(versionStr, pomPropertiesGroupId, pomPropertiesArtifactId);
            } catch (IOException e) {
            } finally {
                _close(pomProperties);
            }
        }
        return Version.unknownVersion();
    }

    /**
     * Method used by <code>PackageVersion</code> classes to decode version injected by Maven build.
     */
    public static Version parseVersion(String s, final String groupId, final String artifactId) {
        if (s != null && (s = s.trim()).length() > 0) {
            String[] parts = V_SEP.split(s);
            return new Version(parseVersionPart(parts[0]),
                (parts.length > 1) ? parseVersionPart(parts[1]) : 0,
                (parts.length > 2) ? parseVersionPart(parts[2]) : 0,
                (parts.length > 3) ? parts[3] : null,
                groupId, artifactId);
        }
        return Version.unknownVersion();
    }

    protected static int parseVersionPart(final String s) {
        int number = 0;
        for (int i = 0, len = s.length(); i < len; ++i) {
            char c = s.charAt(i);
            if (c > '9' || c < '0') break;
            number = (number * 10) + (c - '0');
        }
        return number;
    }

    private final static void _close(final Closeable c) {
        try {
            c.close();
        } catch (IOException e) {
        }
    }
}
