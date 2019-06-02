package com.wildbeeslabs.jentle.algorithms.utils;

import java.net.URL;
import java.security.CodeSource;
import java.util.Objects;
import java.util.Optional;

public final class ClassLoaderUtils {

    private ClassLoaderUtils() {
        /* no-op */
    }

    public static ClassLoader getDefaultClassLoader() {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                return contextClassLoader;
            }
        } catch (Throwable ex) {
            /* ignore */
        }
        return ClassLoader.getSystemClassLoader();
    }

    /**
     * Get the location from which the supplied object's class was loaded.
     *
     * @param object the object for whose class the location should be retrieved
     * @return an {@code Optional} containing the URL of the class' location; never
     * {@code null} but potentially empty
     */
    public static Optional<URL> getLocation(final Object object) {
        Objects.requireNonNull(object, "object must not be null");
        // determine class loader
        ClassLoader loader = object.getClass().getClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
            while (loader != null && loader.getParent() != null) {
                loader = loader.getParent();
            }
        }
        // try finding resource by name
        if (loader != null) {
            String name = object.getClass().getName();
            name = name.replace(".", "/") + ".class";
            try {
                return Optional.ofNullable(loader.getResource(name));
            } catch (Throwable ignore) {
                /* ignore */
            }
        }
        // try protection domain
        try {
            final CodeSource codeSource = object.getClass().getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                return Optional.ofNullable(codeSource.getLocation());
            }
        } catch (SecurityException ignore) {
            /* ignore */
        }
        return Optional.empty();
    }
}
