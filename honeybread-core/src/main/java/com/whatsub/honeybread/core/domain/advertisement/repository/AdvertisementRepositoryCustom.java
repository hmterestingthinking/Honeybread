package com.whatsub.honeybread.core.domain.advertisement.repository;

import com.whatsub.honeybread.core.domain.advertisement.Advertisement;
import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdvertisementRepositoryCustom {
    Page<Advertisement> findAll(Pageable pageable, AdvertisementSearch search);
}
