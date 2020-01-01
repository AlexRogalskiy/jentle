package com.wildbeeslabs.jentle.collections.image;

/**
 * @since 5.0.2
 */
public final class BlockPair {

    private final ByteArray dataBytes;
    private final ByteArray errorCorrectionBytes;

    BlockPair(ByteArray data, ByteArray errorCorrection) {
        dataBytes = data;
        errorCorrectionBytes = errorCorrection;
    }

    public ByteArray getDataBytes() {
        return dataBytes;
    }

    public ByteArray getErrorCorrectionBytes() {
        return errorCorrectionBytes;
    }

}
