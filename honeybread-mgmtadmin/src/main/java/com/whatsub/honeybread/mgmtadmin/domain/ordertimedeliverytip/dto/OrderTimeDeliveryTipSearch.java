package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Value
public class OrderTimeDeliveryTipSearch {

    @NotNull(message = "time값은 null일 수 없습니다.")
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime time;

    @NotNull(message = "요일값은 null일 수 없습니다.")
    DayOfWeek dayOfWeek;

}
