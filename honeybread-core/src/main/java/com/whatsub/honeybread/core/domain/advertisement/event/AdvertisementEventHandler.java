package com.whatsub.honeybread.core.domain.advertisement.event;

import com.whatsub.honeybread.core.domain.advertisement.AdvertiseBidStore;
import com.whatsub.honeybread.core.domain.advertisement.Advertisement;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertiseBidStoreRepository;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementBidNoticeRepository;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdvertisementEventHandler {
    private final AdvertisementBidNoticeRepository bidNoticeRepository;
    private final AdvertiseBidStoreRepository bidStoreRepository;
    private final AdvertisementRepository advertisementRepository;

    @EventListener
    public void bidNoticeClosed(final AdvertisementBidNoticeClosed e) {
        final Long bidNoticeId = e.getBidNoticeId();
        final AdvertisementBidNotice entity = bidNoticeRepository.findById(bidNoticeId)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.ADVERTISEMENT_BID_NOTICE_NOT_FOUND));
        final List<AdvertiseBidStore> advertiseBidStores = bidStoreRepository.findAllByAdvertisementBidNotice(entity);
        final Advertisement advertisement = Advertisement.create(entity, advertiseBidStores);
        advertisementRepository.save(advertisement);
    }

}
