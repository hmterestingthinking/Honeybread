package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import com.whatsub.honeybread.core.domain.storedeliveryprice.StoreDeliveryPrice;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class StoreDeliveryPriceGroupResponse {

    Map<Integer, List<StoreDeliveryPriceResponse>> groupByPrice;

    public static StoreDeliveryPriceGroupResponse of(List<StoreDeliveryPrice> storeDeliveryPrices) {
        return new StoreDeliveryPriceGroupResponse(storeDeliveryPrices.stream()
            .map(StoreDeliveryPriceResponse::of)
            .collect(Collectors.groupingBy(storeDeliveryPriceResponse
                -> storeDeliveryPriceResponse.getPrice().getValue().intValue())));
    }
}
