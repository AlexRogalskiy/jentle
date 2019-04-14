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
package com.wildbeeslabs.jentle.collections.map;

import com.wildbeeslabs.jentle.collections.interfaces.tree.IMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.Set;

/**
 * Custom abstract map implementation
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ACMap<K, V> extends AbstractMap<K, V> implements IMap<K, V> {//IBaseMap<K, V>

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected <T> T convert(final Object obj, final Class<? extends T> clazz) {
        try {
            return clazz.cast(obj);
        } catch (ClassCastException ex) {
            log.error(String.format("ERROR: cannot convert obj=(%s) to clazz=(%s)", String.valueOf(obj), String.valueOf(clazz)));
            return null;
        }
    }
}
