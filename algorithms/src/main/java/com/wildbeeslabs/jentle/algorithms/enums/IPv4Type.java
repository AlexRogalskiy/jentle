package com.wildbeeslabs.jentle.algorithms.enums;

import com.wildbeeslabs.jentle.algorithms.toolset.impl.Range;
import lombok.Getter;

@Getter
public enum IPv4Type {
    CLASS_A(Range.of(1, 126), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255)),
    CLASS_A_LOOPBACK(Range.of(127, 127), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255)),
    CLASS_A_PRIVATE(Range.of(10, 10), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255)),
    CLASS_A_NON_PRIVATE(Range.of(1, 126), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255), false),
    CLASS_B(Range.of(128, 191), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255)),
    CLASS_B_PRIVATE(Range.of(172, 172), Range.of(16, 31), Range.of(0, 255), Range.of(0, 255)),
    CLASS_B_NON_PRIVATE(Range.of(128, 191), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255), false),
    CLASS_C(Range.of(192, 223), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255)),
    CLASS_C_PRIVATE(Range.of(192, 192), Range.of(168, 168), Range.of(0, 255), Range.of(0, 255)),
    CLASS_C_NON_PRIVATE(Range.of(192, 223), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255), false),
    CLASS_D(Range.of(224, 239), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255)),
    CLASS_E(Range.of(240, 254), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255)),
    NO_CONSTRAINT(Range.of(0, 255), Range.of(0, 255), Range.of(0, 255), Range.of(0, 255));

    private final Range<Integer>[] octets;
    private final boolean privateAllowed;

    IPv4Type(final Range<Integer> o1, final Range<Integer> o2, final Range<Integer> o3, final Range<Integer> o4, final boolean privateAllowed) {
        this.octets = new Range[4];
        this.octets[0] = o1;
        this.octets[1] = o2;
        this.octets[2] = o3;
        this.octets[3] = o4;
        this.privateAllowed = privateAllowed;
    }

    IPv4Type(final Range<Integer> o1, final Range<Integer> o2, final Range<Integer> o3, final Range<Integer> o4) {
        this(o1, o2, o3, o4, true);
    }
}
