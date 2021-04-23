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
    @NotNull(message = "최소 입찰 금액은 빈 값이 올 수 없습니다.")
    Money minimumBidPrice;
    @NotNull(message = "입찰 단위는 빈 값이 올 수 없습니다.")
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
