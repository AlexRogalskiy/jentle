/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
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
package com.wildbeeslabs.jentle.algorithms.logger;

import ch.qos.logback.classic.Level;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

/**
 * Logging helper utilities implementation
 */
public class LoggingHelper {

    /**
     * Returns {@link ImmutableCollection} of {@link Level}
     *
     * @return {@link ImmutableCollection} of {@link Level}
     */
    public static ImmutableCollection<Level> getLevels() {
        return ImmutableList.<Level>builder()
            .add(Level.ALL)
            .add(Level.TRACE)
            .add(Level.DEBUG)
            .add(Level.INFO)
            .add(Level.WARN)
            .add(Level.ERROR)
            .add(Level.OFF)
            .build();
    }

    public static Level fromLevel(final String levelString) {
        return getLevels()
            .stream()
            .filter(level -> level.toString().equalsIgnoreCase(levelString))
            .findFirst()
            .orElse(null);
    }
}
