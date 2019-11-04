//package com.wildbeeslabs.jentle.algorithms.math;
//
//class CalculateE implements Callable<BigDecimal> {
//    final int lastIter;
//
//    public CalculateE(int lastIter) {
//        this.lastIter = lastIter;
//    }
//
//    @Override
//    public BigDecimal call() {
//        MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
//        BigDecimal result = BigDecimal.ZERO;
//        for (int i = 0; i <= lastIter; i++) {
//            BigDecimal factorial = factorial(new BigDecimal(i));
//            BigDecimal res = BigDecimal.ONE.divide(factorial, mc);
//            result = result.add(res);
//        }
//        return result;
//    }
//
//    private BigDecimal factorial(BigDecimal n) {
//        if (n.equals(BigDecimal.ZERO))
//            return BigDecimal.ONE;
//        else
//            return n.multiply(factorial(n.subtract(BigDecimal.ONE)));
//    }
//}
