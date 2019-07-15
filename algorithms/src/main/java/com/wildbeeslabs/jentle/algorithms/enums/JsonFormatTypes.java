package com.wildbeeslabs.jentle.algorithms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum JsonFormatTypes {
    STRING,
    NUMBER,
    INTEGER,
    BOOLEAN,
    OBJECT,
    ARRAY,
    NULL,
    ANY;

    private static final Map<String, JsonFormatTypes> _byLCName = new HashMap<>();

    static {
        for (final JsonFormatTypes t : values()) {
            _byLCName.put(t.name().toLowerCase(), t);
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static JsonFormatTypes forValue(String s) {
        return _byLCName.get(s);
    }
}
