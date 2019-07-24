package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum LockDescriptorNameType {
    ENTITY("E"),
    CUSTOM("C");

    private final String id;

    public static LockDescriptorNameType fromId(final String id) {
        for (final LockDescriptorNameType type : LockDescriptorNameType.values()) {
            if (Objects.equals(id, type.getId()))
                return type;
        }
        return null;
    }
}
