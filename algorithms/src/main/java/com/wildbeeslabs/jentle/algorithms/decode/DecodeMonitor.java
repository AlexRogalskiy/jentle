package com.wildbeeslabs.jentle.algorithms.decode;

/**
 * This class is used to drive how decoder/parser should deal with malformed
 * and unexpected data.
 *
 * 2 basic implementations are provided:
 * <ul>
 * <li>{@link #STRICT} return "true" on any occurrence</li>
 * <li>{@link #SILENT} ignores any problem</li>
 * </ul>
 */
public class DecodeMonitor {

    /**
     * The STRICT monitor throws an exception on every event.
     */
    public static final DecodeMonitor STRICT = new DecodeMonitor() {

        @Override
        public boolean warn(String error, String dropDesc) {
            return true;
        }

        @Override
        public boolean isListening() {
            return true;
        }
    };

    /**
     * The SILENT monitor ignore requests.
     */
    public static final DecodeMonitor SILENT = new DecodeMonitor();

    public boolean warn(String error, String dropDesc) {
        return false;
    }

    public boolean isListening() {
        return false;
    }

}
