package com.wildbeeslabs.jentle.algorithms.enums;

public enum ProjectionType {

    TAG, ATTRIBUTES, ATTRIBUTE, CLASSES, CLASS, ID, HTML, TEXT, ALL;

    public static ProjectionType of(String source) {
        for (ProjectionType type : values()) {
            if (source.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return null;
    }


}
