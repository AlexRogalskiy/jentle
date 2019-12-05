package com.wildbeeslabs.jentle.algorithms.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * Simple enumerated constants for downscaling (scaling to smaller image size)--since we have various options
 * for what algorithm to use. Not general-purpose, applies only to methods used in ImageUtil. Types constants
 * can be looked up using {@link #forString(String, DownscaleQuality)} and the corresponding string
 * for the quality
 */
// made a separate class only to reduce size of ImageUtil
public class DownscaleQuality {
    /**
     * Internal map string type to DQ instance
     */
    private static Map constList;

    /**
     * Highest-quality downscaling; probably slowest as well.
     */
    public static final DownscaleQuality HIGH_QUALITY = addConstant("HIGH");

    /**
     * Low-quality, but not worst quality
     */
    public static final DownscaleQuality LOW_QUALITY = addConstant("MED");

    /**
     * Low quality, but very fast.
     */
    public static final DownscaleQuality FAST = addConstant("LOW");

    /**
     * One step, fast, but should be better than low-quality.
     */
    public static final DownscaleQuality AREA = addConstant("AREA");

    private final String type;

    /**
     * Create and add constant instance
     *
     * @param type Unique string for the instance
     * @return The constant for that type
     */
    private static DownscaleQuality addConstant(String type) {
        init();
        if (constList.containsKey(type)) {
            throw new RuntimeException("Type strings for DownscaleQuality should be unique; " + type +
                " is declared twice");
        }
        final DownscaleQuality q = new DownscaleQuality(type);
        constList.put(type, q);
        return q;
    }

    private static void init() {
        if (constList == null) constList = new HashMap();
    }

    private DownscaleQuality(String type) {
        this.type = type;
    }

    public String asString() {
        return type;
    }

    /**
     * Retrieves the DownscaleQuality instance for the corresponding string.
     *
     * @param type The string describing the quality, e.g. HIGH
     * @param dflt Default value to use if not found
     * @return The constant quality instance for the type, or the default if not found.
     */
    public static DownscaleQuality forString(String type, DownscaleQuality dflt) {
        DownscaleQuality q = (DownscaleQuality) constList.get(type);

        return q == null ? dflt : q;
    }
}