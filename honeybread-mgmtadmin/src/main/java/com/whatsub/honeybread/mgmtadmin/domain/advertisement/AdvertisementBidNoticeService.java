package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementBidNoticeRepository;
import com.whatsub.honeybread.core.domain.advertisement.validator.AdvertiseBidNoticeValidator;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementBidNoticeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdvertisementBidNoticeService {
    private final AdvertisementBidNoticeRepository repository;
    private final AdvertiseBidNoticeValidator validator;

    // 등록
    @Transactional
    public Long create(AdvertisementBidNoticeRequest request) {
        AdvertisementBidNotice entity = request.toEntity();
        validator.validate(entity);
        return repository.save(entity).getId();
    }

    // 수정

    // 삭제

    // 종료 (마감)
}
