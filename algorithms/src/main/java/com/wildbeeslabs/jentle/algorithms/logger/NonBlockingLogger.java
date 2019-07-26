package com.wildbeeslabs.jentle.algorithms.logger;

class NonBlockingLogger {

    @SuppressWarnings("InfiniteLoopStatement")
    void start() throws InterruptedException {
        NonBlockingLogger logger = new NonBlockingLogger();
        while (true) {
            Thread.sleep(500);
            logger.log(System.nanoTime());
        }
    }

    private void log(long message) {
        new Thread(() -> {
            System.out.println(System.nanoTime() - message);
        }).start();
    }
}
