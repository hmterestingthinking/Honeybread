package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto;

import io.swagger.annotations.ApiModel;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@ApiModel("시간별 배달팁 검색 요청")
@Value
public class OrderTimeDeliveryTipSearch {

    @NotNull(message = "time값은 null일 수 없습니다.")
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime time;

    @NotNull(message = "요일값은 null일 수 없습니다.")
    DayOfWeek dayOfWeek;

}
