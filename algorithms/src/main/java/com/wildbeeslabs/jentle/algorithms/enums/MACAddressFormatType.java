package com.wildbeeslabs.jentle.algorithms.enums;

import com.wildbeeslabs.jentle.algorithms.toolset.iface.TriConsumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum MACAddressFormatType {

    DASH_EVERY_2_DIGITS(MACAddressFormatType::line2Digits),
    COLON_EVERY_2_DIGITS(MACAddressFormatType::colon2Digits),
    DOT_EVERY_2_DIGITS(MACAddressFormatType::point2Digits),
    DOT_EVERY_4_DIGITS(MACAddressFormatType::point4Digits);

    private final TriConsumer<Integer, StringBuilder, BiFunction<Integer, Integer, Integer>> consumer;

    private static void everyDigits(final Integer i, final StringBuilder buff, final BiFunction<Integer, Integer, Integer> rand, final String chr, final Integer digits) {
        if (i % digits == 0) {
            buff.append(chr);
        }
        buff.append(Integer.toHexString(rand.apply(0, 16)));
    }

    private static void line2Digits(final Integer i, final StringBuilder buff, final BiFunction<Integer, Integer, Integer> rand) {
        everyDigits(i, buff, rand, "-", 2);
    }

    private static void colon2Digits(final Integer i, final StringBuilder buff, final BiFunction<Integer, Integer, Integer> rand) {
        everyDigits(i, buff, rand, ":", 2);
    }

    private static void point2Digits(final Integer i, final StringBuilder buff, final BiFunction<Integer, Integer, Integer> rand) {
        everyDigits(i, buff, rand, ".", 2);
    }

    private static void point4Digits(final Integer i, final StringBuilder buff, final BiFunction<Integer, Integer, Integer> rand) {
        everyDigits(i, buff, rand, ".", 4);
    }
}
