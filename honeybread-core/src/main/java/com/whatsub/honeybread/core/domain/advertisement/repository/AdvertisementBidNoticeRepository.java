package com.whatsub.honeybread.core.domain.advertisement.repository;

import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementBidNoticeRepository
    extends AdvertisementBidNoticeRepositoryCustom, JpaRepository<AdvertisementBidNotice, Long> {
}
