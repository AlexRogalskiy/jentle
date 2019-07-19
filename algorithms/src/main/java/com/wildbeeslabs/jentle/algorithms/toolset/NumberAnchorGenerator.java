package com.wildbeeslabs.jentle.algorithms.toolset;

import java.text.NumberFormat;

public class NumberAnchorGenerator implements AnchorGenerator {

    private int lastAnchorId = 0;

    public NumberAnchorGenerator(int lastAnchorId) {
        this.lastAnchorId = lastAnchorId;
    }

    public String nextAnchor(final Token token) {
        this.lastAnchorId++;
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(3);
        format.setMaximumFractionDigits(0);// issue 172
        format.setGroupingUsed(false);
        String anchorId = format.format(this.lastAnchorId);
        return "id" + anchorId;
    }
}
