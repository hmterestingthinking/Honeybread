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

    @NotNull
    LocalTime from;

    @NotNull
    LocalTime to;

    @NotNull
    Money tip;

    @NotEmpty
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
