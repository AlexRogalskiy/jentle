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
package com.wildbeeslabs.jentle.algorithms.predicate;

import java.util.Random;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Custom predicates implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CPredicates {

    private CPredicates() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public final static Predicate<Object> randBoolean = predicate -> {
        return new Random().nextBoolean();
    };

    public static final Predicate<String> notEmpty = (String it) -> StringUtils.isNotEmpty(it);
    public static final Predicate<String> notBlank = (String it) -> StringUtils.isNotBlank(it);

    public static <T> Predicate<T> not(final Predicate<T> t) {
        return t.negate();
    }
}
