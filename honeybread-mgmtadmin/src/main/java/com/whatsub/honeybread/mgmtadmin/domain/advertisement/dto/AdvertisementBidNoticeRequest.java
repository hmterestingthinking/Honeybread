package com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.support.validation.ValidTimePeriod;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class AdvertisementBidNoticeRequest {
    @NotNull(message = "공고 유형은 빈 값이 올 수 없습니다.")
    AdvertisementType type;
    int maximumStoreCounts;
    Money minimumBidPrice;
    Money bidPriceUnit;
    @ValidTimePeriod
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
