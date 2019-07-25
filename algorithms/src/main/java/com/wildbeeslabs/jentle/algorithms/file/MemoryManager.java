package com.wildbeeslabs.jentle.algorithms.file;

public enum MemoryManager {
    ;
    private static final Runtime RUNTIME = Runtime.getRuntime();

    private enum RuntimeHandler {
        INSTANCE
    }

    private enum OnCloseHandler {
        INSTANCE;

        {
            RUNTIME.addShutdownHook(new Thread(() -> {
                // ReflectionUtils.getAllSuperTypes();
                // for (String dir : toDeleteList) {
                //
                // // TODO no idea why the // is needed. Appears to be a bug in the JVM.
                // System.out.println("Deleting " + dir.replaceAll("/", "//"));
                // IOTools.deleteDir(dir);
                // }
            }));
        }
    }
}
