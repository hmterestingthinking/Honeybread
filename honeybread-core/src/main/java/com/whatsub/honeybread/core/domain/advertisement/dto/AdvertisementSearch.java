package com.whatsub.honeybread.core.domain.advertisement.dto;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementType;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.Data;

@Data
public class AdvertisementSearch {
    private TimePeriod period;
    private AdvertisementType type;
}
