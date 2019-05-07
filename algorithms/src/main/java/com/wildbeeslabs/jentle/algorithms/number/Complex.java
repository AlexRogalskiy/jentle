package com.wildbeeslabs.jentle.algorithms.number;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Complex {
    /**
     * Default {@link Complex} constants
     */
    public static final Complex ZERO = Complex.builder().re(0).im(0).build();
    public static final Complex ONE = Complex.builder().re(1).im(0).build();
    public static final Complex IMAGINARY = Complex.builder().re(0).im(1).build();

    private float re;
    private float im;

    public Complex plus(final Complex c) {
        return Complex.builder()
            .re(re + c.re)
            .im(im + c.im)
            .build();
    }

    public Complex minus(final Complex c) {
        return Complex.builder()
            .re(re - c.re)
            .im(im - c.im)
            .build();
    }

    public Complex times(final Complex c) {
        return Complex.builder()
            .re(re * c.re - im * c.im)
            .im(re * c.im + im * c.re)
            .build();
    }

    public Complex dividedBy(final Complex c) {
        float tmp = c.re * c.re + c.im * c.im;
        return Complex.builder()
            .re((re * c.re + im * c.im) / tmp)
            .im((im * c.re - re * c.im) / tmp)
            .build();
    }
}
