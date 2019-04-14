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

import com.wildbeeslabs.jentle.collections.interfaces.list.IBaseList;
import com.wildbeeslabs.jentle.collections.interfaces.service.IVisitor;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.Collection;
import java.util.Objects;
import java.util.Comparator;
import java.util.Queue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract base list implementation
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
public abstract class ACBaseList<T, E extends ACListNode<T, E>> extends ACListLike<T, E> implements IBaseList<T, E> {

    public ACBaseList() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACBaseList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public ACBaseList(final IBaseList<T, E> source) {
        super(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACBaseList(final IBaseList<T, E> source, final Comparator<? super T> cmp) {
        super(source, cmp);
    }

    protected E getTail(final E root) {
        if (Objects.isNull(this.getRoot())) {
            return null;
        }
        E node = root;
        while (node.hasNext()) {
            node = node.getNext();
        }
        return node;
    }

    public void traverse(final E root, final ObjectOutputStream stream) throws IOException {
        if (Objects.isNull(this.getRoot())) {
            return;
        }
        E node = root;
        while (Objects.nonNull(node)) {
            stream.writeObject(node.getData());
            node = node.getNext();
        }
    }

    @Override
    public Queue<T> toQueue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void iterator(final IVisitor<? extends T> visitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
