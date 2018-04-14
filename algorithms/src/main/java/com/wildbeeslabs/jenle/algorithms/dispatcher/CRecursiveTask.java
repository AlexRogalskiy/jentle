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
package com.wildbeeslabs.jenle.algorithms.dispatcher;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom pool recursive task implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <R>
 * @param <U>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class CRecursiveTask<T, R, U extends CRecursiveTask<T, R, U>> extends RecursiveTask<R> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    private final T value;

    public CRecursiveTask(final T value) {
        this.value = value;
    }

    @Override
    protected R compute() {
        if (this.validateCondition()) {
            return CBaseDispatcher.forkJoinPool.invokeAll(this.createSubtasks()).stream().map(e -> {
                try {
                    return e.get();
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error("ERROR: cannot get recursive task execution result");
                }
                return null;
            }).map(RecursiveTask::join).reduce((x, y) -> reduceData(x, y)).get();
        }
        return this.processing(this.value);
    }

    protected abstract List<? extends Callable<U>> createSubtasks();

    protected abstract boolean validateCondition();

    protected abstract R process(final T value);

    protected abstract R reduceData(final R first, final R second);

    protected R processing(final T value) {
        LOGGER.info(String.format("The result=(%s) was processed by thread=(%s)", value, Thread.currentThread().getName()));
        return this.process(value);
    }
}
