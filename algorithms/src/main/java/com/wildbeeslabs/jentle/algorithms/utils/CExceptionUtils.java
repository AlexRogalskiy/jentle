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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Custom exception utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public class CExceptionUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CExceptionUtils.class);

    private CExceptionUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Returns the deepest cause of the supplied exception
     *
     * @param throwable the exception for which the stack trace is to be
     * returned
     * @return the deepest cause of the supplied exception
     */
    public static Throwable getDeepestThrowable(final Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable parent = throwable;
        Throwable child = throwable.getCause();
        while (Objects.nonNull(child)) {
            parent = child;
            child = parent.getCause();
        }
        return parent;
    }

    /**
     * Returns the stack trace of the supplied exception.
     *
     * @param throwable the exception for which the stack trace is to be
     * returned
     * @return the stack trace, or null if the supplied exception is null
     */
    public static String getStackTrace(final Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return null;
        }
        final ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try (final PrintWriter pw = new PrintWriter(bas)) {
            throwable.printStackTrace(pw);
        }
        return bas.toString();
    }

    /**
     * Returns whether the supplied exception is a checked exception
     *
     * @param throwable the supplied exception to check
     * @return true if supplied exception is checked, false - otherwise
     * @see java.lang.Exception
     * @see java.lang.RuntimeException
     * @see java.lang.Error
     */
    public static boolean isCheckedException(final Throwable throwable) {
        return !(throwable instanceof RuntimeException || throwable instanceof Error);
    }

    public static String stringify(final Throwable throwable) {
        final StringWriter stm = new StringWriter();
        try (final PrintWriter wrt = new PrintWriter(stm)) {
            throwable.printStackTrace(wrt);
        }
        return stm.toString();
    }

    /**
     * Locates a particular type of the supplied exception
     *
     * @param throwable the supplied exception
     * @param type the type of exception to search for
     * @return the first exception of the given type, if found, or null
     * @param <T>
     */
    public static <T extends Throwable> T findCause(final Throwable throwable, final Class<? extends T> type) {
        Throwable current = throwable;
        while (Objects.nonNull(current)) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getCause();
        }
        return null;
    }
}
