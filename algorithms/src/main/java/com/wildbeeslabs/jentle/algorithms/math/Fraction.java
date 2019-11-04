package com.wildbeeslabs.jentle.algorithms.math;

public class Fraction {
    private int num;
    private int den;

    public Fraction(int num, int den) {
        this.num = num;
        this.den = den;
    }

    public Fraction(int n) {
        this(n, 1);
    }

    public void addOn(Fraction f) {
        num = num * f.den + den * f.num;
        den *= f.den;
    }

    public static Fraction add(Fraction f1, Fraction f2) {
        int n = f1.num * f2.den + f1.den * f2.num;
        int d = f1.den * f2.den;
        return new Fraction(n, d);
    }

    @Override
    public String toString() {
        return num + "/" + den;
    }

    public static void main(String[] args) {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 3);
        System.out.print(f1 + " + " + f2 + " = ");
        System.out.println(add(f1, f2));
        f1.addOn(f2);
        System.out.println("Using addOn() changes f1 to " + f1);
    }
}
