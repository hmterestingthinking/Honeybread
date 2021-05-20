package com.whatsub.honeybread.mgmtadmin.domain.ordertimedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.DeliveryTimePeriod;
import com.whatsub.honeybread.core.domain.ordertimedeliverytip.OrderTimeDeliveryTip;
import lombok.Value;

@Value
public class OrderTimeDeliveryTipResponse {

    Long storeId;
    DeliveryTimePeriod deliveryTimePeriod;
    Money tip;

    public static OrderTimeDeliveryTipResponse of(final OrderTimeDeliveryTip orderTimeDeliveryTip) {
        return new OrderTimeDeliveryTipResponse(orderTimeDeliveryTip.getStoreId(),
            orderTimeDeliveryTip.getDeliveryTimePeriod(),
            orderTimeDeliveryTip.getTip()
        );
    }

}
