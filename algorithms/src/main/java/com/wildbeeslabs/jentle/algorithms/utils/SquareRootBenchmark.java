package com.wildbeeslabs.jentle.algorithms.utils;

public class SquareRootBenchmark {
    public static class Benchmark1 extends SimpleBenchmark {
        private static final int ARRAY_SIZE = 10000;
        long[] trials = new long[ARRAY_SIZE];

        @Override
        protected void setUp() throws Exception {
            Random r = new Random();
            for (int i = 0; i < ARRAY_SIZE; i++) {
                trials[i] = Math.abs(r.nextLong());
            }
        }


        public int timeInternet(int reps) {
            int trues = 0;
            for (int i = 0; i < reps; i++) {
                for (int j = 0; j < ARRAY_SIZE; j++) {
                    if (SquareRootAlgs.isPerfectSquareInternet(trials[j])) trues++;
                }
            }

            return trues;
        }

        public int timeDurron(int reps) {
            int trues = 0;
            for (int i = 0; i < reps; i++) {
                for (int j = 0; j < ARRAY_SIZE; j++) {
                    if (SquareRootAlgs.isPerfectSquareDurron(trials[j])) trues++;
                }
            }

            return trues;
        }

        public int timeDurronTwo(int reps) {
            int trues = 0;
            for (int i = 0; i < reps; i++) {
                for (int j = 0; j < ARRAY_SIZE; j++) {
                    if (SquareRootAlgs.isPerfectSquareDurronTwo(trials[j])) trues++;
                }
            }

            return trues;
        }
    }

    public static void main(String... args) {
        Runner.main(Benchmark1.class, args);
    }
}
