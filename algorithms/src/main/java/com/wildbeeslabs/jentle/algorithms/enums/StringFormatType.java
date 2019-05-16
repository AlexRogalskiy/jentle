package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum StringFormatType {

    UPPER_CASE(StringUtils::upperCase),
    LOWER_CASE(StringUtils::lowerCase),
    CAPITALIZED(StringUtils::capitalize),
    UNCAPITALIZED(StringUtils::uncapitalize),
    SWAP_CASE(StringUtils::swapCase),
    NORMALIZED(StringUtils::normalizeSpace),
    NON_WHITE_SPACED(StringUtils::deleteWhitespace),
    STRIP_ACCENTS(StringUtils::stripAccents),
    STRIP_TO_EMPTY(StringUtils::stripToEmpty),
    STRIP_TO_NULL(StringUtils::stripToNull),
    STRIP(StringUtils::strip),
    TRIM_TO_EMPTY(StringUtils::trimToEmpty),
    TRIM_TO_NULL(StringUtils::trimToNull),
    TRIM(StringUtils::trim),
    CHOP(StringUtils::chop);

    private final Function<String, String> format;
}
