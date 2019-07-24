package com.wildbeeslabs.jentle.algorithms.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class TimeZones {

    public static final String NAME = "cuba_TimeZones";

    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static final Pattern AD_HOC_TZ_PATTERN = Pattern.compile("GMT[+-].+");

    /**
     * Not recommended for use. Will be removed in a future version
     * Converts date between time zones.
     *
     * @param srcDate     date
     * @param srcTimeZone source time zone
     * @param dstTimeZone destination time zone
     * @return date in destination time zone, or null if the source date is null
     */
    public Date convert(final Date srcDate, TimeZone srcTimeZone, TimeZone dstTimeZone) {
        if (srcDate == null)
            return null;
        CValidationUtils.notNull(srcTimeZone, "srcTimeZone is null");
        CValidationUtils.notNull(dstTimeZone, "dstTimeZone is null");

        int srcOffset = srcTimeZone.getOffset(srcDate.getTime());
        int dstOffset = dstTimeZone.getOffset(srcDate.getTime());
        return new Date(srcDate.getTime() - srcOffset + dstOffset);
    }

    /**
     * @return string representing the offset of the given time zone from GMT
     */
    public String getDisplayOffset(final TimeZone timeZone) {
        if (timeZone == null)
            return "";

        int offsetSec = timeZone.getOffset(new Date().getTime()) / 1000;
        int offsetHours = offsetSec / 3600;
        int offsetMins = (offsetSec % 3600) / 60;
        String str = StringUtils.leftPad(String.valueOf(Math.abs(offsetHours)), 2, '0')
            + ":" + StringUtils.leftPad(String.valueOf(Math.abs(offsetMins)), 2, '0');
        String sign = offsetHours >= 0 ? "+" : "-";
        return "GMT" + sign + str;
    }

    /**
     * @return long string representation of the given time zone
     */
    public String getDisplayNameLong(final TimeZone timeZone) {
        if (timeZone == null)
            return "";
        String name = timeZone.getID();
        if (AD_HOC_TZ_PATTERN.matcher(name).matches())
            return name;
        else
            return name + " (" + getDisplayOffset(timeZone) + ")";
    }

    /**
     * @return short string representation of the given time zone
     */
    public String getDisplayNameShort(final TimeZone timeZone) {
        if (timeZone == null)
            return "";

        boolean dst = timeZone.inDaylightTime(new Date());
        String name = timeZone.getDisplayName(dst, TimeZone.SHORT);

        if (AD_HOC_TZ_PATTERN.matcher(name).matches())
            return name;
        else
            return name + " (" + getDisplayOffset(timeZone) + ")";
    }
}
