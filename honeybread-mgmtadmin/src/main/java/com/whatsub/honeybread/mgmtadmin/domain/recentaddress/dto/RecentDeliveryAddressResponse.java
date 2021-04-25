package com.whatsub.honeybread.mgmtadmin.domain.recentaddress.dto;

import com.whatsub.honeybread.core.domain.recentaddress.RecentDeliveryAddress;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class RecentDeliveryAddressResponse {

    Long userId;

    String deliveryAddress;

    String searchableDeliveryAddress;

    String stateNameAddress;

    String zipCode;

    LocalDateTime usedAt;

    public static RecentDeliveryAddressResponse of(RecentDeliveryAddress recentDeliveryAddress) {
        return RecentDeliveryAddressResponse.builder()
            .userId(recentDeliveryAddress.getUserId())
            .deliveryAddress(recentDeliveryAddress.getDeliveryAddress())
            .searchableDeliveryAddress(recentDeliveryAddress.getSearchableDeliveryAddress())
            .stateNameAddress(recentDeliveryAddress.getStateNameAddress())
            .zipCode(recentDeliveryAddress.getZipCode())
            .usedAt(recentDeliveryAddress.getUsedAt())
            .build();
    }
}
