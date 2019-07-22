package com.wildbeeslabs.jentle.collections.map.impl;

import java.util.HashMap;
import java.util.Map;

public class SmartHashMap<T1 extends Object, T2 extends Object> {
    public Map<T1, T2> keyValue;
    public Map<T2, T1> valueKey;

    public SmartHashMap() {
        this.keyValue = new HashMap<>();
        this.valueKey = new HashMap<>();
    }

    public void add(final T1 key, final T2 value) {
        this.keyValue.put(key, value);
        this.valueKey.put(value, key);
    }

    public T2 getValue(final T1 key) {
        return this.keyValue.get(key);
    }

    public T1 getKey(final T2 value) {
        return this.valueKey.get(value);
    }
}

