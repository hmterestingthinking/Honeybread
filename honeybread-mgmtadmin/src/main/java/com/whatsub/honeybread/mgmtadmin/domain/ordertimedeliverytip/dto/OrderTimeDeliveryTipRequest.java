package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.DeliveryTimePeriod;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Value
public class OrderTimeDeliveryTipRequest {

    @NotNull
    LocalTime from;

    @NotNull
    LocalTime to;

    @NotNull
    Money tip;

    public OrderTimeDeliveryTip toEntity(final long storeId) {
        return OrderTimeDeliveryTip.builder()
            .storeId(storeId)
            .tip(tip)
            .deliveryTimePeriod(DeliveryTimePeriod.builder()
                .to(to)
                .from(from)
                .build()
            ).build();
    }

}
