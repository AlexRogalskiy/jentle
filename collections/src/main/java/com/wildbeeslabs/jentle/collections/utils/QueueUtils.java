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
package com.wildbeeslabs.jentle.collections.utils;

import com.wildbeeslabs.jentle.collections.queue.impl.OneQueue;
import com.wildbeeslabs.jentle.collections.queue.impl.ZeroQueue;
import lombok.experimental.UtilityClass;

import java.util.Queue;
import java.util.function.Supplier;

@UtilityClass
public class QueueUtils {

    @SuppressWarnings("rawtypes")
    static final Supplier ZERO_SUPPLIER = ZeroQueue::new;
    @SuppressWarnings("rawtypes")
    static final Supplier ONE_SUPPLIER = OneQueue::new;

    /**
     * A {@link Supplier} for an empty immutable {@link Queue}, to be used as a placeholder
     * in methods that require a Queue when one doesn't expect to store any data in said
     * Queue.
     *
     * @param <T> the reified {@link Queue} generic type
     * @return an immutable empty {@link Queue} {@link Supplier}
     */
    @SuppressWarnings("unchecked")
    public static <T> Supplier<Queue<T>> empty() {
        return ZERO_SUPPLIER;
    }

    /**
     *
     * @param <T> the reified {@link Queue} generic type
     * @return a bounded {@link Queue} {@link Supplier}
     */
    @SuppressWarnings("unchecked")
    public static <T> Supplier<Queue<T>> one() {
        return ONE_SUPPLIER;
    }
}
