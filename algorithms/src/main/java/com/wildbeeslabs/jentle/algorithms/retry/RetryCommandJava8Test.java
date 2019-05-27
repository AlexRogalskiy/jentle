//package com.wildbeeslabs.jentle.algorithms.retry;
//
//public class RetryCommandJava8Test extends TestCase {
//
//    public String SUCCESS = "success";
//    public int MAXRETRIES = 3;
//    public int SECONDSTOWAIT = 0;
//    RetryCommandJava8<String> retryCommandJava8;
//
//    public void testRetryCommandShouldNotRetryCommandWhenSuccessful() {
//        retryCommandJava8 = new RetryCommandJava8<>(MAXRETRIES, SECONDSTOWAIT);
//
//        String result = retryCommandJava8.run(() -> SUCCESS);
//
//        assertEquals(SUCCESS, result);
//        assertEquals(0, retryCommand.getRetryCounter());
//    }
//
//    public void testRetryCommandShouldRetryOnceThenSucceedWhenFailsOnFirstCallButSucceedsOnFirstRetry() {
//        retryCommand = new RetryCommandJava8<>(MAXRETRIES, SECONDSTOWAIT);
//
//        String result = retryCommandJava8.run(() -> {
//            if (retryCommand.getRetryCounter() == 0) throw new RuntimeException("Command Failed");
//            else return SUCCESS;
//        });
//
//        assertEquals(SUCCESS, result);
//        assertEquals(1, retryCommand.getRetryCounter());
//    }
//
//    public void testRetryCommandShouldThrowExceptionWhenMaxRetriesIsReached() {
//        retryCommandJava8 = new RetryCommandJava8<>(MAXRETRIES, SECONDSTOWAIT);
//
//        try {
//            retryCommand.run(() -> {throw new RuntimeException("Failed");});
//            fail("Should throw exception when max retries is reached");
//        } catch (Exception e) { }
//    }
//}
