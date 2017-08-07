package com.wildbeeslabs.jentle.collections.exception.array;

import com.wildbeeslabs.jentle.collections.exception.InvalidDimensionException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 *
 * Custom dynamic array implementation
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class DynamicArray<T> implements IArray<T> {
    private final Logger LOGGER = LogManager.getLogger(DynamicArray.class);
    /**
     * Default array enlarge capacity coefficient
     */
    private static final double DEFAULT_ENLARGE_CAPACITY_FACTOR = 1.45;
    /**
     * Default array shrink capacity coefficient
     */
    private static final double DEFAULT_SHRINK_CAPACITY_FACTOR = 1.75;
    private int size;
    private int capacity;
    private T[] array;
    
    public DynamicArray(Class<T[]> clazz) throws InvalidDimensionException {
        this(clazz, 0);
    }
    
    public DynamicArray(Class<T[]> clazz, final int size) throws InvalidDimensionException {
        this(clazz, size, size);
    }
    
    public DynamicArray(Class<T[]> clazz, final int size, final int capacity) throws InvalidDimensionException {
        this(clazz, size, capacity, null);
    }
    
    public DynamicArray(Class<T[]> clazz, final int size, final int capacity, final T[] array) throws InvalidDimensionException {
        if(size < 0) {
            throw new InvalidDimensionException(String.format("ERROR: DynamicArray (invalid intial size=%i)", size));
        }
        this.size = size;
        this.capacity = (capacity < size ? size : capacity);
        this.array = this.newArray(clazz, this.capacity);
        if(null != array) {
            System.arraycopy(array, 0, this.array, 0, Math.min(this.size, array.length));
        }
    }
    
    public T elementAt(int index) throws IndexOutOfBoundsException {
        this.checkRange(index);
        return this.array[index];
    }
    
    public T setElementAt(T item, int index) throws IndexOutOfBoundsException {
        this.checkRange(index);
        T removed = this.array[index];
        this.array[index] = item;
        return removed;
    }
    
    public void add(T[] items) {
        if(null != items) {
            resize(items.length);
            for(T item : items) {
                add(item);
            }
        }
    }
	
    public void add(T item) {
        this.addAt(item, this.size);
    }
    
    public void addAt(T item, int index) {
        this.checkRange(index);
        this.resize(1);
        System.arraycopy(this.array, index, this.array, index + 1, this.size - index);
        this.array[index] = item;
        this.size++;
    }
    
    public boolean remove(T item) {
        int index = this.indexOf(item);
        if(index < 0) return false;
        this.removeAt(index);
        return true;
    }
    
    public T removeAt(int index) throws IndexOutOfBoundsException {
        this.checkRange(index);
        this.resize(-1);
	T removed = this.array[index];
        int itemsToMove = this.size - index - 1;
        if (itemsToMove > 0) {
            System.arraycopy(this.array, index + 1, this.array, index, itemsToMove);
        }
        this.array[size] = null;
        return removed;
    }
    
    public void resize(int delta) {
        if(delta > 0) this.enlargeCapacity(delta);
        else if(delta < 0) this.shrinkCapacity(-delta);
    }
    
    private void enlargeCapacity(int delta) {
        if((this.size + delta) > this.capacity) {
            this.capacity = (int) Math.floor((this.size + delta) * DEFAULT_ENLARGE_CAPACITY_FACTOR);
            LOGGER.info(String.format("DynamicArray (enlarged capacity=%i)", this.capacity));
            this.changeCapacity();
        }
    }
    
    private void shrinkCapacity(int delta) {
        this.size = (delta > this.size ? 0 : this.size - delta);
        if((int) Math.floor(this.size * DEFAULT_SHRINK_CAPACITY_FACTOR) < this.capacity) {
            this.capacity = (int) Math.floor(this.size * DEFAULT_ENLARGE_CAPACITY_FACTOR);
            LOGGER.info(String.format("DynamicArray (shrinked capacity=%i)", this.capacity));
            this.changeCapacity();
        }
    }
    
    private void changeCapacity() {
        T[] temp = this.newArray((Class<T[]>) this.array.getClass(), this.capacity);
        System.arraycopy(this.array, 0, temp, 0, this.size);
        this.array = temp;
    }
    
    private T[] newArray(Class<T[]> type, int size) {
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }
    
    public int indexOf(T item) {
        if(null == item) {
           for(int i = 0; i < this.size(); i++) {
               if (null == array[i]) return i;
           }
        } else {
           for(int i = 0; i < this.size(); i++) {
               if(item.equals(array[i])) return i;
           }
        }
        return -1;
    }
    
    public void clear() {
        for(int i = 0; i < this.size(); i++) {
            this.array[i] = null;
        }
        this.size = 0;
    }
    
    public boolean contains(T item) {
        return (this.indexOf(item) >= 0);
    }
    
    public void trimToSize() {
        if (this.size < this.capacity) {
            this.array = Arrays.copyOf(this.array, this.size);
            this.capacity = this.size;
        }
    }
    
    private void checkRange(int index) throws IndexOutOfBoundsException {
        if(index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: DynamicArray (index=%i is out of bounds [0, %i])", index, this.size-1));
        }
    }
    
    public Object[] toArray() {
        return Arrays.copyOf(this.array, this.size);
    }
    
    public T[] toArray(T[] items) {
        return (T[]) Arrays.copyOf(this.array, this.size, items.getClass());
    }
    
    public int size() {
        return this.size;
    }
    
    public int capacity() {
	return this.capacity;
    }
    
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int cursor = 0;

            @Override
            public T next() {
                if(!hasNext()) {
                    throw new IllegalArgumentException("ERROR: no more elements available");
                }
                T value = array[cursor];
                cursor++;
                return value;
            }

            @Override
            public boolean hasNext() {
                return (cursor < size);
            }

            @Override
            public void remove() {
                DynamicArray.this.checkRange(--cursor);
                DynamicArray.this.removeAt(cursor);
            }
        };
    }
}
