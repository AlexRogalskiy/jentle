package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
public enum Verb {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    ANY_VERB("ANY");

    private final String verb;
    private static final Map<String, Verb> LOOKUP_TABLE = new HashMap<>();

    static {
        for (final Verb v : EnumSet.allOf(Verb.class)) {
            LOOKUP_TABLE.put(v.name(), v);
        }
    }

    public String toString() {
        return verb;
    }

    private static Verb getVerb(final String verbName) {
        if (Objects.nonNull(verbName)) {
            final Verb v = LOOKUP_TABLE.get(verbName.trim().toUpperCase());
            return Optional.ofNullable(v).orElse(Verb.ANY_VERB);
        }
        return Verb.ANY_VERB;
    }
}
