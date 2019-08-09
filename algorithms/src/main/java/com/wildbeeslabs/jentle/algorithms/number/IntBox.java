package com.wildbeeslabs.jentle.algorithms.number;

public final /*inline*/ class IntBox implements Comparable<IntBox> {
    private final int value;

    public IntBox(final int value) {
        this.value = value;
    }

    public IntBox() {
        this(0);
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public int compareTo(IntBox o) {
        return Integer.compare(value, o.value);
    }

    public IntBox add(IntBox box) {
        return IntBox.valueOf(value + box.value);
    }

    public IntBox subtract(IntBox box) {
        return IntBox.valueOf(value - box.value);
    }

    public IntBox multiply(IntBox box) {
        return IntBox.valueOf(value * box.value);
    }

    public IntBox divide(IntBox box) {
        return IntBox.valueOf(value / box.value);
    }

    public static IntBox valueOf(int value) {
        return new IntBox(value);
    }

    public static IntBox zero() {
        return new IntBox(0);
    }

    public int intValue() {
        return value;
    }

    public IntBox increment() {
        return valueOf(value + 1);
    }

    private static IntBox sum(IntBox n) {
        IntBox sum = zero();
        for (IntBox i = zero(); i.compareTo(n) < 0; i = i.add(IntBox.valueOf(1))) {
            sum = sum.add(i);
        }
        return sum;
    }

    public static void main(String[] args) {
    /*
    for(var box: IntStream.range(0, 10).mapToObj(IntBox::valueOf).collect(toList())) {
      System.out.println(box);
    }
    
    Comparable<IntBox> c = IntBox.valueOf(42);
    System.out.println("comparable " + c.compareTo(IntBox.valueOf(17)));
    
    System.out.println(sum(IntBox.valueOf(10)));
    */
        for (int i = 0; i < 100_000; i++) {
            sum(IntBox.valueOf(i));
        }
    }
}
