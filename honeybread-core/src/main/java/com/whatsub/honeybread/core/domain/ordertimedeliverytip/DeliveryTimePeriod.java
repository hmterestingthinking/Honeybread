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

    @Column(nullable = false)
    private LocalTime fromTime;

    @Column(nullable = false)
    private LocalTime toTime;

    @Column(nullable = false)
    private Integer fromMinuteByMidnight;

    @Column(nullable = false)
    private Integer toMinuteAtMidnight;

    @Builder
    private DeliveryTimePeriod(final LocalTime from, final LocalTime to) {
        this.fromTime = from;
        this.toTime = to;
        this.fromMinuteByMidnight = convertMinuteByMidnight(from) - HOURS_PER_DAY * MINUTES_PER_HOUR;
        this.toMinuteAtMidnight = convertMinuteByMidnight(to) - HOURS_PER_DAY * MINUTES_PER_HOUR;
    }

    private int convertMinuteByMidnight(final LocalTime time) {
        return time.getHour() * MINUTES_PER_HOUR + time.getMinute();
    }

}
