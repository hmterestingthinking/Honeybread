package com.whatsub.honeybread.mgmtadmin.domain.recentaddress.dto;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddress;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class RecentDeliveryAddressRequest {

    Long userId;

    String deliveryAddress;

    String searchableDeliveryAddress;

    String stateNameAddress;

    String zipCode;

    public RecentDeliveryAddress toRecentDeliveryAddress() {
        return RecentDeliveryAddress.builder()
            .zipCode(this.zipCode)
            .stateNameAddress(this.stateNameAddress)
            .searchableDeliveryAddress(this.searchableDeliveryAddress)
            .deliveryAddress(this.deliveryAddress)
            .userId(userId)
            .build();
    }
}
