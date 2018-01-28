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
        final PrintWriter pw = new PrintWriter(bas);
        throwable.printStackTrace(pw);
        pw.close();
        return bas.toString();
    }
}
