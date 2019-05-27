//package com.wildbeeslabs.jentle.algorithms.retry;
//
//public class MyGateway {
//    private RetryCommand<String> retryCommand;
//    public MyGateway(int maxRetries) {
//        retryCommand = new RetryCommand<>(maxRetries);
//    }
//
//    // Inline create an instance of  the abstract class RetryCommand
//    // Define the body of the "command" method
//    // Execute the "run" method and return the result
//    public String getThing(final String id) {
//        return new RetryCommand<String>() {
//            public String command() {
//                return client.getThatThing(id);
//            }
//        }.run();
//    }
//
//    // Inline create an instance of  the abstract class RetryCommand
//    // Define the body of the "command" method
//    // Execute the "run" method and return the result
//    public String getThing(final String id) {
//        return new RetryCommand<String>() {
//            public String command() {
//                return client.getThatThing(id);
//            }
//        }.run();
//    }
//}
