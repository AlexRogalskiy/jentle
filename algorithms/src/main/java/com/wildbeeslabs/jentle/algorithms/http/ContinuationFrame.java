//package com.wildbeeslabs.jentle.algorithms.http;
//
//import jdk.incubator.http.internal.common.ByteBufferReference;
//import jdk.incubator.http.internal.common.Utils;
//
//public class ContinuationFrame extends HeaderFrame {
//
//    public static final int TYPE = 0x9;
//
//    public ContinuationFrame(int streamid, int flags, ByteBufferReference[] headerBlocks) {
//        super(streamid, flags, headerBlocks);
//    }
//
//    public ContinuationFrame(int streamid, ByteBufferReference headersBlock) {
//        this(streamid, 0, new ByteBufferReference[]{headersBlock});
//    }
//
//    @Override
//    public int type() {
//        return TYPE;
//    }
//
//    @Override
//    int length() {
//        return headerLength;
//    }
//
//}
