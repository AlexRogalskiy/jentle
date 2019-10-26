package com.wildbeeslabs.jentle.algorithms.http;

public class ResetFrame extends ErrorFrame {

    public static final int TYPE = 0x3;

    // See ErrorFrame for error values

    public ResetFrame(int streamid, int errorCode) {
        super(streamid, 0, errorCode);
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    int length() {
        return 4;
    }

}
