package com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdvertisementBidNoticeResponse {
    Long id;
    AdvertisementType type;
    int maximumStoreCounts;
    Money minimumBidPrice;
    Money bidPriceUnit;
    TimePeriod period;
    AdvertisementBidNotice.Status status;

    public static AdvertisementBidNoticeResponse of(final AdvertisementBidNotice entity) {
        return new AdvertisementBidNoticeResponse(
            entity.getId(),
            entity.getType(),
            entity.getMaximumStoreCounts(),
            entity.getMinimumBidPrice(),
            entity.getBidPriceUnit(),
            entity.getPeriod(),
            entity.getStatus()
        );
    }
}
