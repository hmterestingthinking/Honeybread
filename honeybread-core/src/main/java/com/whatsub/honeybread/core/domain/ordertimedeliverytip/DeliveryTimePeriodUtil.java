package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import java.time.LocalTime;

public class DeliveryTimePeriodUtil {

    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;

    public static int convertMinuteByMidnight(final LocalTime time) {
        final int minute = time.getHour() * MINUTES_PER_HOUR + time.getMinute();
        return isBeforeMidnight(time) ? minute - (HOURS_PER_DAY * MINUTES_PER_HOUR) : minute;
    }

    private static boolean isBeforeMidnight(final LocalTime time) {
        return time.isAfter(LocalTime.NOON) && time.isBefore(LocalTime.MIDNIGHT.minusMinutes(1));
    }

}
