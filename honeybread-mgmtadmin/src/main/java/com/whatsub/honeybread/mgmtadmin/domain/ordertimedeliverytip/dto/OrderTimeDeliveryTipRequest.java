package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.DeliveryTimePeriod;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Value
public class OrderTimeDeliveryTipRequest {

    @NotNull(message = "from값은 null일 수 없습니다.")
    LocalTime from;

    @NotNull(message = "to값은 null일 수 없습니다.")
    LocalTime to;

    @NotNull(message = "tip은 null일 수 없습니다.")
    Money tip;

    @NotEmpty(message = "요일은 1개 이상이어야합니다.")
    List<DayOfWeek> days;

    boolean isAllTheTime;

    boolean isAllDay;

    public OrderTimeDeliveryTip toEntity(final long storeId) {
        return OrderTimeDeliveryTip.builder()
            .storeId(storeId)
            .tip(tip)
            .deliveryTimePeriod(DeliveryTimePeriod.builder()
                .to(to)
                .from(from)
                .days(days)
                .isAllTheTime(isAllTheTime)
                .isAllDay(isAllDay)
                .build()
            ).build();
    }

}
