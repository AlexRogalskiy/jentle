/*
 * The MIT License
 *
 * Copyright 2019 WildBees Labs, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.enums;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;

import java.util.function.Function;

/**
 * String converter type {@link Enum}
 */
@Getter
@RequiredArgsConstructor
public enum StringConverterType {
    UPPER_CASE(StringUtils::upperCase),
    LOWER_CASE(StringUtils::lowerCase),
    CAPITALIZE(StringUtils::capitalize),
    CAPITALIZE_FULLY(WordUtils::capitalizeFully),
    UN_CAPITALIZE(StringUtils::uncapitalize),
    SWAP_CASE(StringUtils::swapCase),
    NORMALIZED(StringUtils::normalizeSpace),
    NON_WHITE_SPACE(StringUtils::deleteWhitespace),
    STRIP_ACCENTS(StringUtils::stripAccents),
    STRIP_TO_EMPTY(StringUtils::stripToEmpty),
    STRIP_TO_NULL(StringUtils::stripToNull),
    STRIP(StringUtils::strip),
    TRIM_TO_EMPTY(StringUtils::trimToEmpty),
    TRIM_TO_NULL(StringUtils::trimToNull),
    TRIM(StringUtils::trim),
    CHOP(StringUtils::chop),

    EMPTY_TO_NULL(Strings::emptyToNull),
    NULL_TO_EMPTY(Strings::nullToEmpty),

    SORT(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::sort),
    COMPRESS(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::compress),
    REVERSE(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::reverse),
    NATIVE_TO_ASCII(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::native2Ascii),
    CAPITALIZE_WORD_FULLY(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::convertToTitleCaseWordFull),
    CAPITALIZE_TITLE(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::convertToTitleCaseWord),

    ESCAPE_JAVA(StringEscapeUtils::escapeJava),
    UNESCAPE_JAVA(StringEscapeUtils::unescapeJava),
    ESCAPE_ECMA_SCRIPT(StringEscapeUtils::escapeEcmaScript),
    UNESCAPE_ECMA_SCRIPT(StringEscapeUtils::unescapeEcmaScript),
    ESCAPE_JSON(StringEscapeUtils::escapeJson),
    UNESCAPE_JSON(StringEscapeUtils::unescapeJson),
    ESCAPE_HTML(StringEscapeUtils::escapeHtml4),
    UNESCAPE_HTML(StringEscapeUtils::unescapeHtml4),
    ESCAPE_XML(StringEscapeUtils::escapeXml11),
    UNESCAPE_XML(StringEscapeUtils::unescapeXml),
    ESCAPE_CSV(StringEscapeUtils::escapeCsv),
    UNESCAPE_CSV(StringEscapeUtils::unescapeCsv),
    UNESCAPE_XSI(StringEscapeUtils::unescapeXSI),
    ESCAPE_XSI(StringEscapeUtils::escapeXSI);

    /**
     * String {@link Function} converter operator
     */
    private final Function<String, String> converter;
}
