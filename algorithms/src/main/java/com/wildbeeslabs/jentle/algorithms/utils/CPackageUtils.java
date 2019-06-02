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

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

@UtilityClass
public class CPackageUtils {

    /**
     * Default package name
     */
    public static final String DEFAULT_PACKAGE_NAME = "";

    /**
     * Compiled {@code "\."} pattern used to split canonical package (and type) names.
     */
    private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

    /**
     * Assert that the supplied package name is valid in terms of Java syntax.
     *
     * <p>Note: this method does not actually verify if the named package exists in the classpath.
     *
     * <p>The default package is represented by an empty string ({@code ""}).
     *
     * @param packageName the package name to validate
     * @throws PreconditionViolationException if the supplied package name is
     *                                        {@code null}, contains only whitespace, or contains parts that are not
     *                                        valid in terms of Java syntax (e.g., containing keywords such as
     *                                        {@code void}, {@code import}, etc.)
     * @see SourceVersion#isName(CharSequence)
     */
    public static boolean isValidPackageName(final String packageName) {
        Objects.requireNonNull(packageName, "package name must not be null");
        if (packageName.equals(DEFAULT_PACKAGE_NAME)) {
            return true;
        }
        assert StringUtils.isNotBlank(packageName) : "package name must not contain only whitespace";
        return Arrays.stream(DOT_PATTERN.split(packageName, -1)).allMatch(SourceVersion::isName);
    }

    /**
     * Get the value of the specified attribute name, specified as a string,
     * or an empty {@link Optional} if the attribute was not found. The attribute
     * name is case-insensitive.
     *
     * <p>This method also returns an empty {@link Optional} value holder
     * if any exception is caught while loading the manifest file via the
     * JAR file of the specified type.
     *
     * @param type the type to get the attribute for
     * @param name the attribute name as a string
     * @return an {@code Optional} containing the attribute value; never
     * {@code null} but potentially empty
     * @throws PreconditionViolationException if the supplied type is
     *                                        {@code null} or the specified name is blank
     * @see Manifest#getMainAttributes()
     */
    public static Optional<String> getAttribute(final Class<?> type, final String name) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(name, "name must not be blank");
        try {
            final CodeSource codeSource = type.getProtectionDomain().getCodeSource();
            final URL jarUrl = codeSource.getLocation();
            try (final JarFile jarFile = new JarFile(new File(jarUrl.toURI()))) {
                final Manifest manifest = jarFile.getManifest();
                final Attributes mainAttributes = manifest.getMainAttributes();
                return Optional.ofNullable(mainAttributes.getValue(name));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
