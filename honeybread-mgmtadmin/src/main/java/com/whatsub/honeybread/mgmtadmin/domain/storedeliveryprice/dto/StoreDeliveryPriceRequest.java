package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class StoreDeliveryPriceRequest {

    @NotNull(message = "Store Id는 NULL일 수 없습니다.")
    Long storeId;

    @NotBlank(message = "주소는 빈 값이 올 수 없습니다.")
    String searchableDeliveryAddress;

    @NotNull(message = "가격은 NULL일 수 없습니다.")
    Money price;

    public StoreDeliveryPrice toEntity() {
        return StoreDeliveryPrice.builder()
            .price(this.price)
            .searchableDeliveryAddress(this.searchableDeliveryAddress)
            .storeId(this.storeId)
            .build();
    }
}
