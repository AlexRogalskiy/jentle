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
package com.wildbeeslabs.jentle.collections.map;

import com.wildbeeslabs.jentle.collections.interfaces.IMap;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom checked map implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <K>
 * @param <V>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CCheckedMap<K, V> extends ACMap<K, V> {

    private final Map rawMap = new HashMap();
    private final Class<? extends K> keyType;
    private final Class<? extends V> valueType;
    private final boolean strict;

    public CCheckedMap(final Map rawMap, final Class<? extends K> keyType, final Class<? extends V> valueType, boolean strict) {
        this.setRawMap(rawMap);
        this.keyType = keyType;
        this.valueType = valueType;
        this.strict = strict;
    }

    private void setRawMap(final Map rawMap) {
        this.rawMap.clear();
        if (Objects.nonNull(rawMap)) {
            this.rawMap.putAll(rawMap);
        }
    }

    private boolean acceptKey(final Object o) {
        if (Objects.isNull(o)) {
            return true;
        } else if (this.keyType.isInstance(o)) {
            return true;
        } else if (this.strict) {
            throw new ClassCastException(String.format("Class: %s cannot be cast to type=%s", o.getClass(), this.keyType.getName()));
        }
        return false;
    }

    private boolean acceptValue(final Object o) {
        if (Objects.isNull(o)) {
            return true;
        } else if (this.valueType.isInstance(o)) {
            return true;
        } else if (this.strict) {
            throw new ClassCastException(String.format("Class: %s cannot be cast to type=%s", o.getClass(), this.valueType.getName()));
        }
        return false;
    }

    private boolean acceptEntry(final Map.Entry entry) {
        return this.acceptKey(entry.getKey()) && this.acceptValue(entry.getValue());
    }

    @Override
    public Iterator<? extends K> keysIterator() {
        return new CCheckedMap.CCheckedMapIterator<K>(this.rawMap.keySet().iterator()) {
            @Override
            protected boolean accept(final Object o) {
                return acceptKey(o);
            }
        };
    }

    @Override
    public Iterator<? extends V> valuesIterator() {
        return new CCheckedMap.CCheckedMapIterator<V>(this.rawMap.values().iterator()) {
            @Override
            protected boolean accept(final Object o) {
                return acceptValue(o);
            }
        };
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new CCheckedMap.CCheckedMapIterator<Map.Entry<K, V>>(this.rawMap.entrySet().iterator()) {
            @Override
            protected boolean accept(final Object o) {
                return acceptEntry((Map.Entry) o);
            }
        };
    }

    protected abstract static class CCheckedMapIterator<T> implements Iterator<T> {

        public static final Object WAITING = new Object();
        private final Iterator it;
        private Object next = CCheckedMapIterator.WAITING;

        public CCheckedMapIterator(final Iterator it) {
            this.it = it;
        }

        public boolean hasNext() {
            if (!Objects.equals(this.next, CCheckedMapIterator.WAITING)) {
                return true;
            }
            while (this.it.hasNext()) {
                this.next = this.it.next();
                if (this.accept(this.next)) {
                    return true;
                }
            }
            this.next = CCheckedMapIterator.WAITING;
            return false;
        }

        public T next() {
            if (Objects.equals(this.next, CCheckedMapIterator.WAITING) && !this.hasNext()) {
                throw new NoSuchElementException();
            }
            assert (!Objects.equals(this.next, CCheckedMapIterator.WAITING));
            final T x = (T) this.next;
            this.next = CCheckedMapIterator.WAITING;
            return x;
        }

        public void remove() {
            this.it.remove();
        }

        protected abstract boolean accept(final Object o);
    }

    protected final class EntrySet<K, V> extends AbstractSet<Map.Entry<K, V>> {

        private final IMap<K, V> map;

        public EntrySet(final IMap<K, V> map) {
            this.map = map;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return map.iterator();
//            return new CCheckedMap.CCheckedMapIterator<Map.Entry<K, V>>(rawMap.entrySet().iterator()) {
//                @Override
//                protected boolean accept(final Object o) {
//                    return acceptEntry((Map.Entry) o);
//                }
//            };
        }

        @Override
        public int size() {
            int c = 0;
            final Iterator it = rawMap.entrySet().iterator();
            while (it.hasNext()) {
                if (acceptEntry((Map.Entry) it.next())) {
                    c++;
                }
            }
            return c;
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet<>(this);
    }

    @Override
    public V get(final Object key) {
        final Object o = this.rawMap.get(this.keyType.cast(key));
        if (this.acceptValue(o)) {
            return (V) o;
        }
        return null;
    }

    @Override
    public V put(final K key, final V value) {
        final Object old = this.rawMap.put(this.keyType.cast(key), this.valueType.cast(value));
        if (this.acceptValue(old)) {
            return (V) old;
        }
        return null;
    }

    @Override
    public V remove(final Object key) {
        final Object old = this.rawMap.remove(this.keyType.cast(key));
        if (this.acceptValue(old)) {
            return (V) old;
        }
        return null;
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.rawMap.containsKey(this.keyType.cast(key)) && this.acceptValue(this.rawMap.get(key));
    }

    @Override
    public boolean containsValue(final Object value) {
        return super.containsValue(this.valueType.cast(value));
    }

    @Override
    public int size() {
        int c = 0;
        Iterator it = this.rawMap.entrySet().iterator();
        while (it.hasNext()) {
            if (this.acceptEntry((Map.Entry) it.next())) {
                c++;
            }
        }
        return c;
    }
}
