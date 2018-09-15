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
package com.wildbeeslabs.jentle.collections.tree.node;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract trie node implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ACTrieNode<T, U extends ACTrieNode<T, U>> extends ACBaseTrieNode<T, U> {

    protected final Map<T, U> childs = new LinkedHashMap<>();

    public ACTrieNode() {
        this(null);
    }

    public ACTrieNode(final T data) {
        this(null, Boolean.FALSE);
    }

    public ACTrieNode(final T data, boolean terminated) {
        super(data, terminated);
    }

    public boolean hasChild(final T data) {
        return this.childs.containsKey(data);
    }

    public U getChild(final T data) {
        return this.childs.get(data);
    }

    public void setChilds(final List<U> childs) {
        this.childs.clear();
        if (Objects.nonNull(childs)) {
            this.childs.putAll(childs.stream().collect(Collectors.toMap(key -> key.getData(), value -> value)));
        }
    }

    public void addChild(final U child) {
        if (Objects.nonNull(child)) {
            this.childs.put(child.getData(), child);
        }
    }

    public void removeChild(final U child) {
        if (Objects.nonNull(child)) {
            this.childs.remove(child.getData());
        }
    }
}
