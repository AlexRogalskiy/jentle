package com.wildbeeslabs.jentle.algorithms.number;

public class GenericType<T> {

    private T type;

    public T get() {
        return this.type;
    }

    public void set(T type) {
        this.type = type;
    }

} 
