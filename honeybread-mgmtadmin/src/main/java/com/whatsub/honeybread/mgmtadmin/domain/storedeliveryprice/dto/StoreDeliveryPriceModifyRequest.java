package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import lombok.Value;

@Value
public class StoreDeliveryPriceModifyRequest {

    Money price;

    public StoreDeliveryPrice toEntity() {
        return StoreDeliveryPrice.builder()
            .price(this.price)
            .build();
    }
}
