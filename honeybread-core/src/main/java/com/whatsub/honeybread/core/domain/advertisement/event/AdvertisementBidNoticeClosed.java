package com.whatsub.honeybread.core.domain.advertisement.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdvertisementBidNoticeClosed {
    private final Long bidNoticeId;
}
