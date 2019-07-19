package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class RegexUtils {

    /**
     * Default html umlaut symbols
     */
    public static final String[] HTML_UMLAUTS = {
        "&Agrave;", //c0
        "&Aacute;", //c1
        "&Acirc;",  //c2
        "&Atilde;", // c3
        "&Auml;", //c4
        "&Aring;", //c5
        "&AElig;",//c6
        "&Ccedil;",//c7
        "&Egrave;",//c8
        "&Eacute;",//c9
        "&Ecirc;",//ca
        "&Euml;",//cb
        "&Igrave;",//cc
        "&Iacute;",//cd
        "&Icirc;",//ce
        "&Iuml;",//cf
        "&ETH;",//d0
        "&Ntilde;",//d1
        "&Ograve;",//d2
        "&Oacute;",//d3
        "&Ocirc;",//d4
        "&Otilde;",//d5
        "&Ouml;",//d6
        "&times;",//d7
        "&Oslash;",//d8
        "&Ugrave;",//d9
        "&Uacute;",//da
        "&Ucirc;",// db
        "&Uuml;",//   dc
        "&Yacute;",// dd
        "&THORN;",//   de
        "&szlig;",// df
        "&agrave;",//  e0
        "&aacute;",// e1
        "&acirc;",//  e2
        "&atilde;",//e3
        "&auml;",//    e4
        "&aring;", // e5
        "&aelig;",//   e6
        "&ccedil;",// e7
        "&egrave;",//   e8
        "&eacute;",// e9
        "&ecirc;",//    ea
        "&euml;",// eb
        "&igrave;",// ec
        "&iacute;",//ed
        "&icirc;",//   ee
        "&iuml;",// ef
        "&eth;",//  f0
        "&ntilde;",// f1
        "&ograve;",//   f2
        "&oacute;",//f3
        "&ocirc;",//   f4
        "&otilde;",//f5
        "&ouml;",//    f6
        "&divide;",//f7
        "&oslash;",//f8
        "&ugrave;",//f9
        "&uacute;",//  fa
        "&ucirc;",// fb
        "&uuml;",//  fc
        "&yacute;",// fd
        "&thorn;",//   fe
        "&yuml;"// ff
    };

    /**
     * Default boolean regular expression
     */
    public static final Pattern DEFAULT_BOOL_REGEX = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");

    /**
     * The regular expression is taken from the 1.2 specification but '_'s are
     * added to keep backwards compatibility
     */
    public static final Pattern DEFAULT_FLOAT_REGEX = Pattern.compile("^([-+]?(\\.[0-9]+|[0-9_]+(\\.[0-9_]*)?)([eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
    public static final Pattern DEFAULT_INT_REGEX = Pattern.compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
    public static final Pattern DEFAULT_MERGE_REGEX = Pattern.compile("^(?:<<)$");
    public static final Pattern DEFAULT_NULL_REGEX = Pattern.compile("^(?:~|null|Null|NULL| )$");
    public static final Pattern DEFAULT_EMPTY_REGEX = Pattern.compile("^$");
    public static final Pattern DEFAULT_TIMESTAMP_REGEX = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
    public static final Pattern DEFAULT_VALUE_REGEX = Pattern.compile("^(?:=)$");
    public static final Pattern DEFAULT_YAML_REGEX = Pattern.compile("^(?:!|&|\\*)$");
}
