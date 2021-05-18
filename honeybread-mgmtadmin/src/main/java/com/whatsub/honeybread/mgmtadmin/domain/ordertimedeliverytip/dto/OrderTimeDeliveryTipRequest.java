package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.DeliveryTimePeriod;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import lombok.Value;

import java.time.LocalTime;

@Value
public class OrderTimeDeliveryTipRequest {

    LocalTime from;
    LocalTime to;
    Money tip;

    public OrderTimeDeliveryTip toEntity(long storeId) {
        return OrderTimeDeliveryTip.builder()
            .storeId(storeId)
            .tip(tip)
            .deliveryTimePeriod(DeliveryTimePeriod.builder()
                .to(to)
                .from(from)
                .build())
            .build();
    }

}
