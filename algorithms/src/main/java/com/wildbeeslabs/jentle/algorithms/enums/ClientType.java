package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClientType {
    WEB("W", "web"),
    PORTAL("P", "portal"),
    DESKTOP("D", "desktop"),
    REST_API("R", "rest");

    private final String id;
    private final String configPath;

    public static ClientType fromId(final String id) {
        if ("W".equals(id)) {
            return WEB;
        } else if ("D".equals(id)) {
            return DESKTOP;
        } else if ("P".equals(id)) {
            return PORTAL;
        } else {
            return null;
        }
    }
}
