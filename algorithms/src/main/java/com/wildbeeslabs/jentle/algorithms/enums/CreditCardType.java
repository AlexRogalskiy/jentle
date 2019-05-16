package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * Helper enumeration class to process credit cards
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Getter
@RequiredArgsConstructor
public enum CreditCardType {

    AMERICAN_EXPRESS(15, 34, 37),
    CHINA_UNION_PAY_16(16, 62),
    CHINA_UNION_PAY_17(17, 62),
    CHINA_UNION_PAY_18(18, 62),
    CHINA_UNION_PAY_19(19, 62),
    DINERS_CLUB_CARTE_BLANCHE(14, 300, 301, 302, 303, 304, 305),
    DINERS_CLUB_INTERNATIONAL(14, 309, 36, 38, 39),
    DISCOVER_16(16, 6011, 644, 645, 646, 647, 648, 649, 65),
    DISCOVER_19(19, 6011, 644, 645, 646, 647, 648, 649, 65),
    INTER_PAYMENT_16(16, 636),
    INTER_PAYMENT_19(19, 636),
    INSTA_PAYMENT(16, 637, 638, 639),
    JCB(16, 3528, 3529, 3530, 3531, 3532, 3533, 3534, 3535, 3536, 3537, 3538, 3539, 3540, 3541, 3542, 3543, 3544, 3545, 3546, 3547, 3548, 3549, 3550),
    MAESTRO_12(12, 56, 57, 58, 6),
    MAESTRO_13(13, 56, 57, 58, 6),
    MAESTRO_14(14, 56, 57, 58, 6),
    MAESTRO_15(15, 56, 57, 58, 6),
    MAESTRO_16(16, 56, 57, 58, 6),
    MAESTRO_17(17, 56, 57, 58, 6),
    MAESTRO_18(18, 56, 57, 58, 6),
    MAESTRO_19(19, 56, 57, 58, 6),
    MASTERCARD(16, 2221, 2720, 51, 52, 53, 54, 55),
    VISA_13(13, 4),
    VISA_16(16, 4),
    VISA_19(19, 4);

    private final Integer length;

    private final List<List<Integer>> prefixes;

    CreditCardType(final Integer length, final Integer... prefixes) {
        this.length = length;
        this.prefixes = unmodifiableList(Arrays.stream(Optional.ofNullable(prefixes).orElseGet(() -> new Integer[0])).map(this::fromNumber).collect(toList()));
    }

    private List<Integer> fromNumber(final int num) {
        assert num > 0 : "Number should be greate that zero";
        final List<Integer> list = new LinkedList<>();
        int tmp = num;
        while (tmp != 0) {
            list.add(0, tmp % 10);
            tmp /= 10;
        }
        return unmodifiableList(list);
    }
}
