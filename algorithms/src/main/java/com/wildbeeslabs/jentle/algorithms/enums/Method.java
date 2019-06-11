package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Method {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false),
    PATCH(true),
    HEAD(false),
    OPTIONS(false),
    TRACE(false);

    private final boolean hasBody;
}
