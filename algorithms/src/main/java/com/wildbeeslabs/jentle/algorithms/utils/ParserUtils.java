package com.wildbeeslabs.jentle.algorithms.utils;

import com.wildbeeslabs.jentle.algorithms.exception.BadOperationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.apache.commons.lang3.StringUtils.trim;

@Slf4j
@UtilityClass
public class ParserUtils {

    /**
     * Default named group regex
     */
    public static final String DEFAULT_NAMED_GROUP_REGEX = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>";
    /**
     * Default name group {@link Pattern}
     */
    public static final Pattern DEFAULT_NAMED_GROUP_PATTERN = Pattern.compile(DEFAULT_NAMED_GROUP_REGEX);

    public static Optional<String> getIfAnyMatch(final String content, final List<String> regexList) {
        return Optional.ofNullable(regexList)
            .orElseGet(Collections::emptyList)
            .stream()
            .map(Pattern::compile)
            .map(pattern -> pattern.matcher(content))
            .filter(Matcher::find)
            .map(Matcher::group)
            .findFirst();
    }

    public static Optional<String> extractAbove(final String content, final String delimiter) {
        Objects.requireNonNull(content, "Content should not be null");
        Objects.requireNonNull(delimiter, "Delimiter should not be null");

        int i = content.indexOf(delimiter);
        if (i != -1) {
            return Optional.of(trim(content.substring(0, i)));
        }
        return Optional.empty();
    }

//    public static Optional<String> getRFC822Email(final String email) {
//        Objects.requireNonNull(email, "Email address should not be null");
//        try {
//            final String address = new InternetAddress(email, true).getAddress();
//            return Optional.ofNullable(address);
//        } catch (AddressException e) {
//            return Optional.empty();
//        }
//    }

//    public static InputStream toInputStream(final MimeMessage message) {
//        Objects.requireNonNull(message, "Mime message should not be null");
//        try {
//            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            message.writeTo(bos);
//            return new ByteArrayInputStream(bos.toByteArray());
//        } catch (Exception e) {
//            BadOperationException.throwBadOperation(String.format("Cannot process message = {%s}", message), e);
//        }
//        return null;
//    }

    public static String sanitize(final String content) {
        return sanitize(content, Whitelist.none());
    }

    public static String sanitize(final String content, final Whitelist whiteList) {
        return Jsoup.clean(content, Whitelist.none());
    }

    public static Set<String> getNamedGroups(final String regex) {
        final Set<String> namedGroups = new TreeSet<>();
        final Matcher m = DEFAULT_NAMED_GROUP_PATTERN.matcher(regex);
        while (m.find()) {
            namedGroups.add(m.group(1));
        }
        return namedGroups;
    }

    public static Map<String, String> getNamedGroupsMatch(final String content, final List<String> regexList) {
        final Map<String, String> result = new HashMap<>();
        Optional.ofNullable(regexList)
            .orElseGet(Collections::emptyList)
            .stream()
            .map(regex -> getNamedGroupsMatch(content, regex))
            .forEach(result::putAll);
        return result;
    }

    public Map<String, String> getNamedGroupsMatch(final String content, final String regex) {
        final Map<String, String> result = new HashMap<>();
        final Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            getNamedGroups(regex).forEach(name -> result.putIfAbsent(name, matcher.group(name)));
        }
        return result;
    }

    public static boolean anyMatch(final String value, final List<String> regexList) {
        return Optional.ofNullable(regexList)
            .orElseGet(Collections::emptyList)
            .stream()
            .anyMatch(s -> buildRegex(s).matcher(value).find());
    }

    public static Pattern buildRegex(final String regex) {
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            BadOperationException.throwBadOperation(String.format("ERROR: cannot process input regex = {%s}", regex), e);
        }
        return pattern;
    }
}
