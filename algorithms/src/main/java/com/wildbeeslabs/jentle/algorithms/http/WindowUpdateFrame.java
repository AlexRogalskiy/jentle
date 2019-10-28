//package com.wildbeeslabs.jentle.algorithms.http;
//
//public class WindowUpdateFrame extends Http2Frame {
//
//    private final int windowUpdate;
//
//    public static final int TYPE = 0x8;
//
//    public WindowUpdateFrame(int streamid, int windowUpdate) {
//        super(streamid, 0);
//        this.windowUpdate = windowUpdate;
//    }
//
//    @Override
//    public int type() {
//        return TYPE;
//    }
//
//    @Override
//    int length() {
//        return 4;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(super.toString())
//          .append(" WindowUpdate: ")
//          .append(windowUpdate);
//        return sb.toString();
//    }
//
//    public int getUpdate() {
//        return this.windowUpdate;
//    }
//
//}
