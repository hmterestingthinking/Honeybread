package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementBidNoticeSearch;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementBidNoticeRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdvertisementBidNoticeQueryService {
    private final AdvertisementBidNoticeRepository repository;

    public Page<AdvertisementBidNoticeResponse> getAdvertisementBidNotices(
        final Pageable pageable,
        final AdvertisementBidNoticeSearch search
    ) {
        return repository.findAll(pageable, search)
            .map(AdvertisementBidNoticeResponse::of);
    }

    public AdvertisementBidNoticeResponse getAdvertisementBidNotice(final Long id) {
        return repository.findById(id)
            .map(AdvertisementBidNoticeResponse::of)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.ADVERTISEMENT_BID_NOTICE_NOT_FOUND));
    }
}
