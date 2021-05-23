package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Value
public class OrderTimeDeliveryTipSearch {

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime time;

    @NotNull
    DayOfWeek dayOfWeek;

}
