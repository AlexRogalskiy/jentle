package com.wildbeeslabs.jentle.algorithms.utils;

import opennlp.tools.util.ObjectStream;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class ObjectStreamUtils {

    /**
     * Creates an {@link ObjectStream} form an array.
     *
     * @param <T>
     * @param array
     * @return the object stream over the array elements
     */
    @SafeVarargs

    public static <T> ObjectStream<T> createObjectStream(final T... array) {

        return new ObjectStream<T>() {

            private int index = 0;

            public T read() {
                if (index < array.length)
                    return array[index++];
                else
                    return null;
            }

            public void reset() {
                index = 0;
            }

            public void close() {
            }
        };
    }

    /**
     * Creates an {@link ObjectStream} form a collection.
     *
     * @param <T>
     * @param collection
     * @return the object stream over the collection elements
     */
    public static <T> ObjectStream<T> createObjectStream(final Collection<T> collection) {

        return new ObjectStream<T>() {

            private Iterator<T> iterator = collection.iterator();

            public T read() {
                if (iterator.hasNext())
                    return iterator.next();
                else
                    return null;
            }

            public void reset() {
                iterator = collection.iterator();
            }

            public void close() {
            }
        };
    }

    /**
     * Creates a single concatenated ObjectStream from multiple individual
     * ObjectStreams with the same type.
     *
     * @param streams
     * @return
     */
    public static <T> ObjectStream<T> concatenateObjectStream(final Collection<ObjectStream<T>> streams) {

        // We may want to skip null streams instead of throwing a
        for (ObjectStream<T> stream : streams) {
            if (stream == null) {
                throw new NullPointerException("stream cannot be null");
            }
        }

        return new ObjectStream<T>() {

            private Iterator<ObjectStream<T>> iterator = streams.iterator();
            private ObjectStream<T> currentStream = iterator.next();

            @Override
            public T read() throws IOException {
                T object = null;

                while (currentStream != null && object == null) {
                    object = currentStream.read();
                    if (object == null) {
                        currentStream = (iterator.hasNext()) ? iterator.next() : null;
                    }
                }
                return object;
            }

            @Override
            public void reset() throws IOException, UnsupportedOperationException {
                for (ObjectStream<T> stream : streams) {
                    stream.reset();
                }
                iterator = streams.iterator();
            }

            @Override
            public void close() throws IOException {
                for (ObjectStream<T> stream : streams) {
                    stream.close();
                }
            }

        };

    }

    /**
     * Creates a single concatenated ObjectStream from multiple individual
     * ObjectStreams with the same type.
     *
     * @param streams
     * @return
     */
    @SafeVarargs
    public static <T> ObjectStream<T> concatenateObjectStream(final ObjectStream<T>... streams) {

        for (ObjectStream<T> stream : streams) {
            if (stream == null) {
                throw new NullPointerException("stream cannot be null");
            }
        }

        return new ObjectStream<T>() {

            private int streamIndex = 0;

            public T read() throws IOException {

                T object = null;

                while (streamIndex < streams.length && object == null) {
                    object = streams[streamIndex].read();

                    if (object == null)
                        streamIndex++;
                }

                return object;
            }

            public void reset() throws IOException, UnsupportedOperationException {
                streamIndex = 0;

                for (ObjectStream<T> stream : streams) {
                    stream.reset();
                }
            }

            public void close() throws IOException {

                for (ObjectStream<T> stream : streams) {
                    stream.close();
                }
            }
        };
    }
}
