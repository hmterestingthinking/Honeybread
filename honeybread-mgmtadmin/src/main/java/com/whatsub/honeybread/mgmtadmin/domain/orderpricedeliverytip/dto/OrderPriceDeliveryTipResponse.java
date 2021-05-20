package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import lombok.Value;

@Value
public class OrderPriceDeliveryTipResponse {

    private Long storeId;

    private Money fromPrice;

    private Money toPrice;

    private Money tip;

    public static OrderPriceDeliveryTipResponse of(OrderPriceDeliveryTip orderPriceDeliveryTip) {
        return new OrderPriceDeliveryTipResponse(orderPriceDeliveryTip.getStoreId(),
                orderPriceDeliveryTip.getFromPrice(),
                orderPriceDeliveryTip.getToPrice(),
                orderPriceDeliveryTip.getTip());
    }

}
