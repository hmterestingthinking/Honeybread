package com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisedStore;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdvertisementStoreResponse {
    Long id;
    Long storeId;
    Money winningBidPrice;

    public static AdvertisementStoreResponse of(final AdvertisedStore entity) {
        return new AdvertisementStoreResponse(
            entity.getId(),
            entity.getStoreId(),
            entity.getWinningBidPrice()
        );
    }
}
