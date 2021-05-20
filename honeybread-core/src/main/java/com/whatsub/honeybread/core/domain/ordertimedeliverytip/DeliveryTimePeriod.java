package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryTimePeriod {

    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;
    private static final LocalTime EIGHT_PM = LocalTime.of(20, 0);

    @Column(nullable = false)
    private LocalTime fromTime;

    @Column(nullable = false)
    private LocalTime toTime;

    @Column(nullable = false)
    private Integer fromMinuteByMidnight;

    @Column(nullable = false)
    private Integer toMinuteByMidnight;

    @Builder
    private DeliveryTimePeriod(final LocalTime from, final LocalTime to) {
        this.fromTime = from;
        this.toTime = to;
        this.fromMinuteByMidnight = isBeforeMidnight(from)
            ? convertMinuteByMidnight(from) - HOURS_PER_DAY * MINUTES_PER_HOUR : convertMinuteByMidnight(from);
        this.toMinuteByMidnight = isBeforeMidnight(to)
            ? convertMinuteByMidnight(to) - HOURS_PER_DAY * MINUTES_PER_HOUR : convertMinuteByMidnight(to);
    }

    public static int convertMinuteByMidnight(final LocalTime time) {
        return time.getHour() * MINUTES_PER_HOUR + time.getMinute();
    }

    private boolean isBeforeMidnight(final LocalTime time) {
        return time.isAfter(EIGHT_PM) && time.isBefore(LocalTime.MIDNIGHT.minusMinutes(1));
    }

}
