package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class StoreDeliveryPriceModifyRequest {

    @NotNull(message = "가격은 NULL일 수 없습니다.")
    Money price;

    @JsonCreator
    public StoreDeliveryPriceModifyRequest(Money price) {
        this.price = price;
    }

    public StoreDeliveryPrice toEntity() {
        return StoreDeliveryPrice.builder()
            .price(this.price)
            .build();
    }
}
