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
    private static final Map<String, Verb> lookup = new HashMap<>();

    static {
        for (final Verb v : EnumSet.allOf(Verb.class)) {
            lookup.put(v.name(), v);
        }
    }

    public String toString() {
        return verb;
    }

    private static Verb getVerb(final String verbName) {
        if (Objects.nonNull(verbName)) {
            final Verb v = lookup.get(verbName.trim().toUpperCase());
            return Optional.ofNullable(v).orElse(Verb.ANY_VERB);
        }
        return Verb.ANY_VERB;
    }
}
