package com.wildbeeslabs.jentle.algorithms.utils;

import java.util.Objects;

public class PlatformFeatureDetector {

    private Boolean isRunningOnAndroid = null;

    public boolean isRunningOnAndroid() {
        if (Objects.isNull(this.isRunningOnAndroid)) {
            final String name = System.getProperty("java.runtime.name");
            this.isRunningOnAndroid = (Objects.nonNull(name) && name.startsWith("Android Runtime"));
        }
        return this.isRunningOnAndroid;
    }
}
