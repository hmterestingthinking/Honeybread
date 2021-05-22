package com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto;

import com.whatsub.honeybread.core.domain.advertisement.Advertisement;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AdvertisementResponse {
    Long id;
    AdvertisementType type;
    int maximumStoreCounts;
    TimePeriod period;
    List<AdvertisementStoreResponse> advertisedStores;

    public static AdvertisementResponse of(final Advertisement entity) {
        return new AdvertisementResponse(
            entity.getId(),
            entity.getType(),
            entity.getMaximumStoreCounts(),
            entity.getPeriod(),
            entity.getAdvertisedStores().stream()
                .map(AdvertisementStoreResponse::of)
                .collect(Collectors.toList())
        );
    }
}
