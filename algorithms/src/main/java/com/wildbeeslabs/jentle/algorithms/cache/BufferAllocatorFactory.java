package com.wildbeeslabs.jentle.algorithms.cache;

public interface BufferAllocatorFactory {

    BufferAllocator getBufferAllocator(int minSize);
}
