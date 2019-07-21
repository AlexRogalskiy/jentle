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
package com.wildbeeslabs.jentle.collections.map.impl;

import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CopyOnWriteMap<K, V> extends AbstractCopyOnWriteMap<K, V, Map<K, V>> {

    private static final long serialVersionUID = 7935514534647505917L;

    /**
     * Get a {@link Builder} for a CopyOnWriteMap instance.
     *
     * @param <K> key type
     * @param <V> value type
     * @return a fresh builder
     */
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }

    /**
     * Build a {@link CopyOnWriteMap} and specify all the options.
     *
     * @param <K> key type
     * @param <V> value type
     */
    @NoArgsConstructor
    public static class Builder<K, V> {
        private View.Type viewType = View.Type.STABLE;
        private final Map<K, V> initialValues = new HashMap<K, V>();

        /**
         * Views are stable (fixed in time) and unmodifiable.
         */
        public Builder<K, V> stableViews() {
            viewType = View.Type.STABLE;
            return this;
        }

        /**
         * Views are live (reflecting concurrent updates) and mutator methods are supported.
         */
        public Builder<K, V> addAll(final Map<? extends K, ? extends V> values) {
            initialValues.putAll(values);
            return this;
        }

        /**
         * Views are live (reflecting concurrent updates) and mutator methods are supported.
         */
        public Builder<K, V> liveViews() {
            viewType = View.Type.LIVE;
            return this;
        }

        public CopyOnWriteMap<K, V> newHashMap() {
            return new Hash<K, V>(initialValues, viewType);
        }

        public CopyOnWriteMap<K, V> newLinkedMap() {
            return new Linked<K, V>(initialValues, viewType);
        }
    }

    /**
     * <p> Creates a new {@link CopyOnWriteMap} with an underlying {@link HashMap}. </p>
     *
     * <p>This map has {@link View.Type#STABLE stable} views.</p>
     */
    public static <K, V> CopyOnWriteMap<K, V> newHashMap() {
        Builder<K, V> builder = builder();
        return builder.newHashMap();
    }

    /**
     * <p> Creates a new {@link CopyOnWriteMap} with an underlying {@link HashMap} using the supplied map as the initial values. </p>
     *
     * <p>This map has {@link View.Type#STABLE stable} views.</p>
     */
    public static <K, V> CopyOnWriteMap<K, V> newHashMap(final Map<? extends K, ? extends V> map) {
        Builder<K, V> builder = builder();
        return builder.addAll(map).newHashMap();
    }

    /**
     * <p>Creates a new {@link CopyOnWriteMap} with an underlying {@link java.util.LinkedHashMap}. Iterators for this map will be return
     * elements in insertion order. </p>
     *
     * <p>This map has {@link View.Type#STABLE stable} views.</p>
     */
    public static <K, V> CopyOnWriteMap<K, V> newLinkedMap() {
        Builder<K, V> builder = builder();
        return builder.newLinkedMap();
    }

    /**
     * <p>Creates a new {@link CopyOnWriteMap} with an underlying {@link java.util.LinkedHashMap} using the supplied map as the initial
     * values. Iterators for this map will be return elements in insertion order. </p>
     *
     * <p>This map has {@link View.Type#STABLE stable} views.</p>
     */
    public static <K, V> CopyOnWriteMap<K, V> newLinkedMap(final Map<? extends K, ? extends V> map) {
        Builder<K, V> builder = builder();
        return builder.addAll(map).newLinkedMap();
    }

    //
    // constructors
    //

    /**
     * Create a new {@link CopyOnWriteMap} with the supplied {@link Map} to initialize the values.
     *
     * @param map the initial map to initialize with
     */
    protected CopyOnWriteMap(final Map<? extends K, ? extends V> map) {
        this(map, View.Type.LIVE);
    }

    /**
     * Create a new empty {@link CopyOnWriteMap}.
     */
    protected CopyOnWriteMap() {
        this(Collections.<K, V>emptyMap(), View.Type.LIVE);
    }

    /**
     * Create a new {@link CopyOnWriteMap} with the supplied {@link Map} to initialize the values. This map may be optionally modified using
     * any of the key, entry or value views
     *
     * @param map the initial map to initialize with
     */
    protected CopyOnWriteMap(final Map<? extends K, ? extends V> map, final View.Type viewType) {
        super(map, viewType);
    }

    /**
     * Create a new empty {@link CopyOnWriteMap}. This map may be optionally modified using any of the key, entry or value views
     */
    protected CopyOnWriteMap(final View.Type viewType) {
        super(Collections.<K, V>emptyMap(), viewType);
    }

    @Override
    // @GuardedBy("internal-lock")
    protected abstract <N extends Map<? extends K, ? extends V>> Map<K, V> copy(N map);

    //
    // inner classes
    //

    /**
     * Uses {@link HashMap} instances as its internal storage.
     */
    static class Hash<K, V> extends CopyOnWriteMap<K, V> {
        private static final long serialVersionUID = 5221824943734164497L;

        Hash(final Map<? extends K, ? extends V> map, final View.Type viewType) {
            super(map, viewType);
        }

        @Override
        public <N extends Map<? extends K, ? extends V>> Map<K, V> copy(final N map) {
            return new HashMap<K, V>(map);
        }
    }

    /**
     * Uses {@link java.util.LinkedHashMap} instances as its internal storage.
     */
    static class Linked<K, V> extends CopyOnWriteMap<K, V> {
        private static final long serialVersionUID = -8659999465009072124L;

        Linked(final Map<? extends K, ? extends V> map, final View.Type viewType) {
            super(map, viewType);
        }

        @Override
        public <N extends Map<? extends K, ? extends V>> Map<K, V> copy(final N map) {
            return new LinkedHashMap<K, V>(map);
        }
    }
}
