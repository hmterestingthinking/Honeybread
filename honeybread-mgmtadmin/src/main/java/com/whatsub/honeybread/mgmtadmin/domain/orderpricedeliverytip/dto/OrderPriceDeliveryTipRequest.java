package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import lombok.Value;

@Value
public class OrderPriceDeliveryTipRequest {

    private Long storeId;

    private Money from;

    private Money to;

    private Money tip;

    public OrderPriceDeliveryTip toEntity() {
        return OrderPriceDeliveryTip.builder()
            .storeId(this.storeId)
            .from(this.from)
            .to(this.to)
            .tip(this.tip)
            .build();
    }
}
