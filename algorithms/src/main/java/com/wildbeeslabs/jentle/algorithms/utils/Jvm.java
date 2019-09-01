package com.wildbeeslabs.jentle.algorithms.utils;

import java.lang.management.ManagementFactory;
import java.util.Optional;

/**
 * JVM utilities.
 */
public final class Jvm {
    /**
     * JVM exit handler.
     *
     * @see System#exit(int)
     */
    public interface ExitHandler {
        /**
         * Exits the JVM.
         *
         * @param code Exit code.
         */
        void exit(int code);
    }

    private static final String PID;

    private static volatile ExitHandler exitHandler;

    static {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();

        int index = jvmName.indexOf('@');

        if (index < 0) {
            PID = "";
        } else {
            PID = jvmName.substring(0, index);
        }
    }

    private Jvm() {
        // No-op.
    }

    /**
     * Returns the PID of the JVM process.
     *
     * @return PID or an empty string if PID couldn't be resolved.
     */
    public static String pid() {
        return PID;
    }

    /**
     * Exits the JVM by calling the {@link #setExitHandler(ExitHandler) pre-configured} {@link ExitHandler} or by calling the {@link
     * System#exit(int)} method (if handler is not defined).
     *
     * @param code Exit code.
     */
    public static void exit(int code) {
        ExitHandler handler = exitHandler().orElse(System::exit);

        handler.exit(code);
    }

    /**
     * Configures the exit handler.
     *
     * @param handler Exit handler.
     */
    public static void setExitHandler(ExitHandler handler) {
        exitHandler = handler;
    }

    /**
     * Returns an instance of {@link ExitHandler} if it was defined via {@link #setExitHandler(ExitHandler)}.
     *
     * @return Exit handler.
     */
    public static Optional<ExitHandler> exitHandler() {
        return Optional.ofNullable(exitHandler);
    }
}
