package com.wildbeeslabs.jentle.algorithms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

public enum In {
    HEADER,
    QUERY;

    private static Map<String, In> names = new LinkedHashMap<>();

    @JsonCreator
    public static In forValue(final String value) {
        return names.get(value.toLowerCase());
    }

    @JsonValue
    public String toValue() {
        for (final Map.Entry<String, In> entry : names.entrySet()) {
            if (entry.getValue() == this) {
                return entry.getKey();
            }
        }
        return null;
    }

    static {
        names.put("header", HEADER);
        names.put("query", QUERY);
    }
}
