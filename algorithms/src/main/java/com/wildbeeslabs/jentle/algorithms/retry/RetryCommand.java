package com.wildbeeslabs.jentle.algorithms.retry;

public abstract class RetryCommand<T> {
    private int maxRetries;

    public RetryCommand(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    // This abstract command is the method that will be implemented 
    public abstract T command();

    public final T run() throws RuntimeException {
        try {
            return command();
        } catch (Exception e) {
            return retry();
        }
    }

    private final T retry() throws RuntimeException {
        System.out.println("FAILED - Command failed, will be retried " + maxRetries + " times.");
        int retryCounter = 0;
        while (retryCounter < maxRetries) {
            try {
                return command();
            } catch (Exception ex) {
                retryCounter++;
                System.out.println("FAILED - Command failed on retry " + retryCounter + " of " + maxRetries + " error: " + ex);
                if (retryCounter >= maxRetries) {
                    System.out.println("Max retries exceeded.");
                    break;
                }
            }
        }
        throw new RuntimeException("Command failed on all of " + maxRetries + " retries");
    }
}
