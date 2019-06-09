package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.Objects;

/**
 * Utility methods related to {@link Closeable}.
 *
 * @author Yvonne Wang
 */
@Slf4j
@UtilityClass
public class Closeables {

    /**
     * Closes the given {@link Closeable}s, ignoring any thrown exceptions.
     *
     * @param closeables the {@code Closeable}s to close.
     */
    public static void closeQuietly(final Closeable... closeables) {
        for (final Closeable c : closeables) {
            closeCloseable(c);
        }
    }

    private static void closeCloseable(final Closeable c) {
        if (Objects.isNull(c)) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
            log.warn("Error occurred while closing " + c, t);
        }
    }
}
