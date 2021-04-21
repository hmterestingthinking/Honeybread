package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import lombok.Value;

@Value
public class OrderPriceDeliveryTipRequest {

    private Long storeId;

    private Money fromPrice;

    private Money toPrice;

    private Money tip;

    public OrderPriceDeliveryTip toEntity() {
        return OrderPriceDeliveryTip.builder()
            .storeId(this.storeId)
            .fromPrice(this.fromPrice)
            .toPrice(this.toPrice)
            .tip(this.tip)
            .build();
    }
}
