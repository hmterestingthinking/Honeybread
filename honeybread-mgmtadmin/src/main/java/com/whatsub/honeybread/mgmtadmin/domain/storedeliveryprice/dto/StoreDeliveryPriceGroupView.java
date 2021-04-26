package com.whatsub.honeybread.mgmtadmin.domain.storedeliveryprice.dto;

import lombok.Value;

import java.util.List;

@Value
public class StoreDeliveryPriceGroupView {

    List<StoreDeliveryPriceGroupResponse> groupByPrice;

}