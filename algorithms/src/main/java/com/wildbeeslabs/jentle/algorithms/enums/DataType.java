package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enumeration of the Redis data types.
 *
 * @author Costin Leau
 * @author Mark Paluch
 */
@Getter
@RequiredArgsConstructor
public enum DataType {

    NONE("none"),
    STRING("string"),
    LIST("list"),
    SET("set"),
    ZSET("zset"),
    HASH("hash"),
    /**
     * @since 2.2
     */
    STREAM("stream");

    private static final Map<String, DataType> codeLookup = new ConcurrentHashMap<>(7);

    static {
        for (final DataType type : EnumSet.allOf(DataType.class))
            codeLookup.put(type.code, type);

    }

    private final String code;

    /**
     * Utility method for converting an enum code to an actual enum.
     *
     * @param code enum code
     * @return actual enum corresponding to the given code
     */
    public static DataType fromCode(final String code) {
        return Optional.ofNullable(codeLookup.get(code)).orElseThrow(() -> new IllegalArgumentException("unknown data type code"));
    }
}
