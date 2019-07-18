package com.wildbeeslabs.jentle.algorithms.enums;

public enum FlowStyle {
    FLOW(Boolean.TRUE), BLOCK(Boolean.FALSE), AUTO(null);

    private Boolean styleBoolean;

    private FlowStyle(Boolean flowStyle) {
        styleBoolean = flowStyle;
    }

    /*
     * Convenience for legacy constructors that took {@link Boolean} arguments since replaced by {@link FlowStyle}.
     * Introduced in v1.22 but only to support that for backwards compatibility.
     * @deprecated Since restored in v1.22.  Use the {@link FlowStyle} constants in your code instead.
     */
    @Deprecated
    public static FlowStyle fromBoolean(Boolean flowStyle) {
        return flowStyle == null ? AUTO
            : flowStyle ? FLOW
            : BLOCK;
    }

    public Boolean getStyleBoolean() {
        return styleBoolean;
    }

    @Override
    public String toString() {
        return "Flow style: '" + styleBoolean + "'";
    }
}
