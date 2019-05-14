package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class CDecodeUtils {

    public static String decode(final byte[] bytes) {
        return new String(Base64.getDecoder().decode(bytes));
    }

    public static String[] decodeMultiple(final byte[]... bytes) {
        final String[] result = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = decode(bytes[i]);
        }
        return result;
    }

    public static byte[] encode(final String string) {
        return (Objects.isNull(string) ? null : Base64.getEncoder().encode(string.getBytes()));
    }

    public static Map<byte[], byte[]> encodeMap(final Map<String, byte[]> map) {
        return Optional.ofNullable(map).orElseGet(Collections::emptyMap).entrySet().stream().collect(Collectors.toMap(e -> encode(e.getKey()), e -> e.getValue()));
    }

    public static Map<String, byte[]> decodeMap(final Map<byte[], byte[]> map) {
        return Optional.ofNullable(map).orElseGet(Collections::emptyMap).entrySet().stream().collect(Collectors.toMap(e -> decode(e.getKey()), e -> e.getValue()));
    }
}
