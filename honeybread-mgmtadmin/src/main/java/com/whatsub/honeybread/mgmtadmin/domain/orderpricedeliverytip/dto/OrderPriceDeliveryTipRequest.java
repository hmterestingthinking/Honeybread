package com.whatsub.honeybread.mgmtadmin.domain.orderpricedeliverytip.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.orderpricedeliverytip.OrderPriceDeliveryTip;
import io.swagger.annotations.ApiModel;
import lombok.Value;

import javax.validation.constraints.NotNull;

@ApiModel("주문금액별 배달팁 등록 요청")
@Value
public class OrderPriceDeliveryTipRequest {

    @NotNull(message = "~가격 부터(fromPrice)는 Null 일 수 없습니다.")
    private Money fromPrice;

    @NotNull(message = "~가격 까지(toPrice)는 Null 일 수 없습니다.")
    private Money toPrice;

    @NotNull(message = "배달팁은 Null 일 수 없습니다.")
    private Money tip;

    public OrderPriceDeliveryTip toEntity(final Long storeId) {
            return OrderPriceDeliveryTip.builder()
                .storeId(storeId)
                .fromPrice(this.fromPrice)
                .toPrice(this.toPrice)
            .tip(this.tip)
            .build();
    }
}
