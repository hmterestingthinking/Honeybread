package com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AdvertisementBidNoticeRequest {
    AdvertisementType type;
    int maximumStoreCounts;
    Money minimumBidPrice;
    Money bidPriceUnit;
    TimePeriod period;

    public AdvertisementBidNotice toEntity() {
        return AdvertisementBidNotice.builder()
            .type(type)
            .period(period)
            .minimumBidPrice(minimumBidPrice)
            .bidPriceUnit(minimumBidPrice)
            .maximumStoreCounts(maximumStoreCounts)
            .build();
    }
}
