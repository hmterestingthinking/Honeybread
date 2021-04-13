package com.whatsub.honeybread.core.domain.advertisement.dto;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.Data;

@Data
public class AdvertisementBidNoticeSearch {
    private AdvertisementBidNotice.Status status;
    private TimePeriod period;
    private AdvertisementType type;
}
