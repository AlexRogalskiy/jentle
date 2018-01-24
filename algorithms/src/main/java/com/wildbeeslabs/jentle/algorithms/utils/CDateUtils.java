/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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
package com.wildbeeslabs.jentle.algorithms.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Custom date utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class CDateUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CDateUtils.class);
    /**
     * Default file character encoding
     */
    public static final Charset DEFAULT_FILE_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    private CDateUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static enum TimeZoneOffsetBase {

        GMT, UTC
    }

    public static List<String> getTimeZoneGMTList() {
        return getTimeZoneList(CDateUtils.TimeZoneOffsetBase.GMT);
    }

    public static List<String> getTimeZoneUTCList() {
        return getTimeZoneList(CDateUtils.TimeZoneOffsetBase.UTC);
    }

    private static List<String> getTimeZoneList(final TimeZoneOffsetBase base) {
        final LocalDateTime now = LocalDateTime.now();
        return ZoneId.getAvailableZoneIds().stream()
                .map(ZoneId::of)
                .sorted(new ZoneComparator())
                .map(id -> String.format("(%s%s) %s", base, getOffset(now, id), id.getId()))
                .collect(Collectors.toList());
    }

    private static String getOffset(final LocalDateTime dateTime, final ZoneId id) {
        return dateTime
                .atZone(id)
                .getOffset()
                .getId()
                .replace("Z", "+00:00");
    }

    private static class ZoneComparator implements Comparator<ZoneId> {

        @Override
        public int compare(final ZoneId zoneId1, final ZoneId zoneId2) {
            final LocalDateTime now = LocalDateTime.now();
            final ZoneOffset offset1 = now.atZone(zoneId1).getOffset();
            final ZoneOffset offset2 = now.atZone(zoneId2).getOffset();
            return offset1.compareTo(offset2);
        }
    }

    public static List<String> getTimeZoneList2(final TimeZoneOffsetBase base) {
        String[] availableZoneIds = TimeZone.getAvailableIDs();
        List<String> result = new ArrayList<>(availableZoneIds.length);
        for (final String zoneId : availableZoneIds) {
            TimeZone curTimeZone = TimeZone.getTimeZone(zoneId);
            String offset = calculateOffset(curTimeZone.getRawOffset());
            result.add(String.format("(%s%s) %s", base, offset, zoneId));
        }
        Collections.sort(result);
        return result;
    }

    private static String calculateOffset(int rawOffset) {
        if (rawOffset == 0) {
            return "+00:00";
        }
        long hours = TimeUnit.MILLISECONDS.toHours(rawOffset);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(rawOffset);
        minutes = Math.abs(minutes - TimeUnit.HOURS.toMinutes(hours));
        return String.format("%+03d:%02d", hours, Math.abs(minutes));
    }

    public static List<Date> getDatesBetween(final Date startDate, final Date endDate) {
        final List<Date> datesInRange = new ArrayList<>();
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        final Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    public static List<LocalDate> getDatesBetween2(final LocalDate startDate, final LocalDate endDate) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
    }

//    public static List<LocalDate> getDatesBetween(final LocalDate startDate, final LocalDate endDate) {
//        return startDate.datesUntil(endDate).collect(Collectors.toList());
//    }
//    public LocalDate convertToLocalDate(final Date dateToConvert) {
//        return LocalDate.ofInstant(
//                dateToConvert.toInstant(), ZoneId.systemDefault());
//    }
//
//    public LocalDateTime convertToLocalDateTime(final Date dateToConvert) {
//        return LocalDateTime.ofInstant(
//                dateToConvert.toInstant(), ZoneId.systemDefault());
//    }
    public static LocalDate convertToLocalDateViaInstant(final Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(final Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date convertToDateViaInstant(final LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static Date convertToDateViaInstant(final LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static boolean isValidDate(final String date, final String format) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
        } catch (java.text.ParseException ex) {
            LOGGER.error(String.format("ERROR: cannot parse input date=%s by format=%s, message=%s", date, format, ex.getMessage()));
        }
        return true;
    }
}
