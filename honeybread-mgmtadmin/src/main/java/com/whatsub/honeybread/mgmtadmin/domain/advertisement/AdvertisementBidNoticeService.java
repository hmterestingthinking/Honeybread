package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementBidNoticeRepository;
import com.whatsub.honeybread.core.domain.advertisement.validator.AdvertiseBidNoticeValidator;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
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

    @Transactional
    public Long create(final AdvertisementBidNoticeRequest request) {
        final AdvertisementBidNotice entity = request.toEntity();
        validator.validate(entity);
        return repository.save(entity).getId();
    }

    @Transactional
    public void update(final Long id, final AdvertisementBidNoticeRequest request) {
        final AdvertisementBidNotice entity = findAdvertiseBidNotice(id);
        validateProgress(entity);
        entity.update(request.toEntity());
        validator.validate(entity);
    }

    @Transactional
    public void delete(final Long id) {
        final AdvertisementBidNotice entity = findAdvertiseBidNotice(id);
        validateProgress(entity);
        repository.delete(entity);
    }

    @Transactional
    public void close(final Long id) {
        final AdvertisementBidNotice entity = findAdvertiseBidNotice(id);
        entity.close();
    }

    private AdvertisementBidNotice findAdvertiseBidNotice(final Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new HoneyBreadException(ErrorCode.ADVERTISEMENT_BID_NOTICE_NOT_FOUND));
    }

    private void validateProgress(final AdvertisementBidNotice entity) {
        if (entity.isProcess()) {
            throw new HoneyBreadException(ErrorCode.ADVERTISEMENT_BID_NOTICE_CANNOT_REGIST);
        }
    }
}
