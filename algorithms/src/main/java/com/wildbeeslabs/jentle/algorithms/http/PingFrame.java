package com.wildbeeslabs.jentle.algorithms.http;

public class PingFrame extends Http2Frame {


    private final byte[] data;

    public static final int TYPE = 0x6;

    // Flags
    public static final int ACK = 0x1;

    public PingFrame(int flags, byte[] data) {
        super(0, flags);
        assert data.length == 8;
        this.data = data;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    int length() {
        return 8;
    }

    @Override
    public String flagAsString(int flag) {
        switch (flag) {
        case ACK:
            return "ACK";
        }
        return super.flagAsString(flag);
    }

    public byte[] getData() {
        return data;
    }

}
