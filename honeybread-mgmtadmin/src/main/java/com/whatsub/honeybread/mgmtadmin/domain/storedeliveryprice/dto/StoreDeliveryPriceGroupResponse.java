package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import lombok.Value;

import java.util.List;

@Value
public class StoreDeliveryPriceGroupResponse {

    int price;
    List<StoreDeliveryPriceResponse> locations;

}
