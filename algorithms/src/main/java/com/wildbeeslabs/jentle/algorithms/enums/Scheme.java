package com.wildbeeslabs.jentle.algorithms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Scheme {
    HTTP("http"),
    HTTPS("https"),
    WS("ws"),
    WSS("wss");

    private final String value;

    @JsonCreator
    public static Scheme forValue(final String value) {
        for (final Scheme item : Scheme.values()) {
            if (item.toValue().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return this.value;
    }
}
