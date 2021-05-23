package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryTimePeriod {

    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;
    private static final List<DayOfWeek> ALL_DAYS = List.of(DayOfWeek.values());

    @Column(nullable = false)
    private LocalTime fromTime;

    @Column(nullable = false)
    private LocalTime toTime;

    @Column(nullable = false)
    private Integer fromMinuteByMidnight;

    @Column(nullable = false)
    private Integer toMinuteByMidnight;

    @ElementCollection
    @CollectionTable(name = "order_time_delivery_tip_day_of_weeks", joinColumns = @JoinColumn(name = "store_id"))
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> days;

    private boolean isAllTheTime;

    @Builder
    private DeliveryTimePeriod(final LocalTime from,
                               final LocalTime to,
                               final List<DayOfWeek> days,
                               final boolean isAllTheTime,
                               final boolean isAllDay) {
        this.fromTime = from;
        this.toTime = to;
        this.days = isAllDay ? ALL_DAYS : days;
        this.isAllTheTime = isAllTheTime;
        this.fromMinuteByMidnight = isBeforeMidnight(from)
            ? convertMinuteByMidnight(from) - HOURS_PER_DAY * MINUTES_PER_HOUR : convertMinuteByMidnight(from);
        this.toMinuteByMidnight = isBeforeMidnight(to)
            ? convertMinuteByMidnight(to) - HOURS_PER_DAY * MINUTES_PER_HOUR : convertMinuteByMidnight(to);
    }

    public static int convertMinuteByMidnight(final LocalTime time) {
        return time.getHour() * MINUTES_PER_HOUR + time.getMinute();
    }

    private boolean isBeforeMidnight(final LocalTime time) {
        return time.isAfter(LocalTime.NOON) && time.isBefore(LocalTime.MIDNIGHT.minusMinutes(1));
    }

}
