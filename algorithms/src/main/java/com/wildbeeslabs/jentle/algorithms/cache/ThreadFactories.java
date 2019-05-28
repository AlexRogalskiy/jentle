package com.wildbeeslabs.jentle.algorithms.cache;

import lombok.experimental.UtilityClass;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple utility class to create thread factories.
 */
@UtilityClass
public class ThreadFactories {

    private static final ThreadFactory BACKING_FACTORY = Executors.defaultThreadFactory();

    /**
     * Create a new {@link ThreadFactory} that produces daemon threads with a given name format.
     *
     * @param fmt String format: for example foo-%d
     * @return a new {@link ThreadFactory}
     */
    public static ThreadFactory withName(final String fmt) {
        return new ThreadFactory() {
            private final AtomicLong count = new AtomicLong(0);

            @Override
            public Thread newThread(Runnable r) {
                final Thread t = BACKING_FACTORY.newThread(r);
                t.setDaemon(true);
                t.setName(String.format(fmt, count.getAndIncrement()));
                return t;
            }
        };
    }
}
