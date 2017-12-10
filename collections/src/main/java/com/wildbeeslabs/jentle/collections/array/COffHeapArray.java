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
package com.wildbeeslabs.jentle.collections.array;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import sun.misc.Unsafe;
import static sun.nio.ch.IOStatus.normalize;

/**
 *
 * Custom off heap array implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class COffHeapArray<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    private long size;
    private long address;

    public COffHeapArray(long size) throws NoSuchFieldException, IllegalAccessException {
        this.size = size;
        address = getUnsafe().allocateMemory(size);
    }

    private Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        final Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    public void set(final T value, long address, final Class<? extends T[]> clazz) throws NoSuchFieldException, IllegalAccessException {
        this.toAddress(value, clazz);
    }

    public T get(long address, final Class<? extends T[]> clazz) throws NoSuchFieldException, IllegalAccessException {
        return this.fromAddress(address, clazz);
    }

    public long size() {
        return size;
    }

    public void freeMemory() throws NoSuchFieldException, IllegalAccessException {
        getUnsafe().freeMemory(address);
    }

    public long toAddress(final T obj, final Class<? extends T[]> clazz) throws IllegalAccessException, IllegalAccessException, NoSuchFieldException {
        T[] array = newArray(clazz, 1);
        array[0] = obj;
        long baseOffset = getUnsafe().arrayBaseOffset(array.getClass());
        return normalize(getUnsafe().getLong(array, baseOffset));
    }

    public T fromAddress(long address, final Class<? extends T[]> clazz) throws IllegalAccessException, NoSuchFieldException {
        T[] array = newArray(clazz, 1);
        array[0] = null;
        long baseOffset = getUnsafe().arrayBaseOffset(array.getClass());
        getUnsafe().putLong(array, baseOffset, address);
        return array[0];
    }

    public long sizeOf(final T object) throws IllegalAccessException, NoSuchFieldException {
        return getUnsafe().getAddress(normalize(getUnsafe().getLong(object, 4L)) + 12L);
    }

    public long sizeOf2(final T o) throws IllegalAccessException, NoSuchFieldException {
        Unsafe u = getUnsafe();
        final Set<Field> fields = new HashSet<>();
        Class c = o.getClass();
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(f);
                }
            }
            c = c.getSuperclass();
        }
        // get offset
        long maxSize = 0;
        for (final Field f : fields) {
            long offset = u.objectFieldOffset(f);
            if (offset > maxSize) {
                maxSize = offset;
            }
        }
        return ((maxSize / 8) + 1) * 8;
    }

    public T shallowCopy(final T obj, final Class<? extends T[]> clazz) throws IllegalAccessException, NoSuchFieldException {
        long sizeOfObj = sizeOf2(obj);
        long start = toAddress(obj, clazz);
        long addressOfObj = getUnsafe().allocateMemory(sizeOfObj);
        getUnsafe().copyMemory(start, addressOfObj, sizeOfObj);
        return fromAddress(addressOfObj, clazz);
    }

    private T[] newArray(final Class<? extends T[]> type, int size) {
        assert (Objects.nonNull(type));
        assert (size >= 0);
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }
}
