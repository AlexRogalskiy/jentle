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
package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.collections.interfaces.IListLike;
import com.wildbeeslabs.jentle.collections.list.node.ACNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.AbstractSequentialList;
import java.util.Comparator;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom abstract list-like implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <E>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ACListLike<T, E extends ACNode<T>> extends AbstractSequentialList<T> implements IListLike<T, E> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected E first;
    protected int size;
    protected final Comparator<? super T> cmp;

    public ACListLike() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACListLike(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public ACListLike(final IListLike<T, E> source) {
        this(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ACListLike(final IListLike<T, E> source, final Comparator<? super T> cmp) {
        this.first = null;
        this.size = 0;
        this.cmp = cmp;
    }

    @Override
    public void setRoot(final Optional<? extends T> value) {
        this.setRoot(this.createNode(value));
    }

    protected void setRoot(final E node) {
        this.first = node;
    }

    @Override
    public E getRoot() {
//        if (this.isEmpty()) {
//            return null;
//        }
        return this.first;
    }

    protected Logger getLogger() {
        return this.LOGGER;
    }

    protected abstract E createNode(final Optional<? extends T> value);
}
