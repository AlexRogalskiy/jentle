package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum StringFormatType {
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
    SORT(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::sort),
    COMPRESS(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::compress),
    REVERSE(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::reverse),
    NATIVE_TO_ASCII(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::native2Ascii),
    CAPITALIZE_WORD_FULLY(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::convertToTitleCaseWordFull),
    CAPITALIZE_TITLE(com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils::convertToTitleCaseWord),

    ESCAPE_JAVA(StringEscapeUtils::escapeJava),
    UNESCAPE_JAVA(StringEscapeUtils::unescapeJava),
    ESCAPE_ECMASCRIPT(StringEscapeUtils::escapeEcmaScript),
    UNESCAPE_ECMASCRIPT(StringEscapeUtils::unescapeEcmaScript),
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
     * Default format {@link Function} operator
     */
    private final Function<String, String> format;
}
