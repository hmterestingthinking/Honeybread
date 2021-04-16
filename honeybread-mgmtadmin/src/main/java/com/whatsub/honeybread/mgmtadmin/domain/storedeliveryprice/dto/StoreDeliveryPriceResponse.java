package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import lombok.Value;

@Value
public class StoreDeliveryPriceResponse {

    Long storeId;

    String searchableDeliveryAddress;

    Money price;

    public static StoreDeliveryPriceResponse of(StoreDeliveryPrice storeDeliveryPrice) {
        return new StoreDeliveryPriceResponse(storeDeliveryPrice.getStoreId(), storeDeliveryPrice.getSearchableDeliveryAddress(), storeDeliveryPrice.getPrice());
    }
}
