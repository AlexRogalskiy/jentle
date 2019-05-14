package com.wildbeeslabs.jentle.collections.set.wrapper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Byte array wrapper
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ByteArrayWrapper {

    private final byte[] array;

    public static ByteArrayWrapper of(final byte[] array) {
        return new ByteArrayWrapper(array);
    }
}
