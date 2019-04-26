package com.wildbeeslabs.jentle.algorithms.utils;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class CommonUtils {

    public static void freezeProcessing(final int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> wrapWithList(final Supplier<T> supplier) {
        return Lists.newArrayList(supplier.get());
    }
}
