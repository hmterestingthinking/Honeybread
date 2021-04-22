package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class OrderPriceDeliveryTipRequest {

    @NotNull(message = "~가격 부터(fromPrice)는 Null 일 수 없습니다.")
    private Money fromPrice;

    @NotNull(message = "~가격 까지(toPrice)는 Null 일 수 없습니다.")
    private Money toPrice;

    @NotNull(message = "배달팁은 Null 일 수 없습니다.")
    private Money tip;

    public OrderPriceDeliveryTip toEntity() {
            return OrderPriceDeliveryTip.builder()
                .fromPrice(this.fromPrice)
                .toPrice(this.toPrice)
            .tip(this.tip)
            .build();
    }
}
