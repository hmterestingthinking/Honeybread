package com.whatsub.honeybread.core.domain.advertisement.repository;

import com.whatsub.honeybread.core.domain.advertisement.AdvertiseBidStore;
import com.whatsub.honeybread.core.domain.advertisement.AdvertisementBidNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertiseBidStoreRepository extends JpaRepository<AdvertiseBidStore, Long> {
    List<AdvertiseBidStore> findAllByAdvertisementBidNotice(AdvertisementBidNotice bidNotice);
}
