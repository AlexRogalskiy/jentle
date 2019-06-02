package com.wildbeeslabs.jentle.algorithms.enums;

import com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils;
import com.wildbeeslabs.jentle.algorithms.utils.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum StringPredicateType {
    IS_CONTAIN_ISO_CHAR(StringUtils::containsIsoControlCharacter),
    IS_NOT_CONTAIN_ISO_CHAR(StringUtils::doesNotContainIsoControlCharacter),
    IS_PALINDROME(CStringUtils::isPalindrome),
    IS_PERMUTATION_PALINDROME(CStringUtils::isPermutationOfPalindrome),
    IS_UNIQUE(CStringUtils::isUnique),
    IS_LOWER_CASE(org.apache.commons.lang3.StringUtils::isAllLowerCase),
    IS_UPPER_CASE(org.apache.commons.lang3.StringUtils::isAllUpperCase),
    IS_ALPHA(org.apache.commons.lang3.StringUtils::isAlpha),
    IS_ALPHA_NUMERIC(org.apache.commons.lang3.StringUtils::isAlphanumericSpace),
    IS_ALPHA_NUMERIC_SPACE(org.apache.commons.lang3.StringUtils::isAlphanumericSpace),
    IS_EMPTY(org.apache.commons.lang3.StringUtils::isEmpty),
    IS_ALPHA_SPACE(org.apache.commons.lang3.StringUtils::isAlphaSpace),
    IS_BLANK(org.apache.commons.lang3.StringUtils::isBlank),
    IS_NOT_BLANK(org.apache.commons.lang3.StringUtils::isNotBlank),
    IS_ASCII_PRINTABLE(org.apache.commons.lang3.StringUtils::isAsciiPrintable),
    IS_MIXED_CASE(org.apache.commons.lang3.StringUtils::isMixedCase),
    IS_NUMERIC(org.apache.commons.lang3.StringUtils::isNumeric),
    IS_NUMERIC_SPACE(org.apache.commons.lang3.StringUtils::isNumericSpace),
    IS_WHITESPACE(org.apache.commons.lang3.StringUtils::isWhitespace);


    /**
     * Default validator {@link Predicate} operator
     */
    private final Predicate<String> format;
}
