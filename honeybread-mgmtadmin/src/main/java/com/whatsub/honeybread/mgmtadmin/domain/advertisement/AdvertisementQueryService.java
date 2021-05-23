package com.whatsub.honeybread.mgmtadmin.domain.advertisement;

import com.whatsub.honeybread.core.domain.advertisement.dto.AdvertisementSearch;
import com.whatsub.honeybread.core.domain.advertisement.repository.AdvertisementRepository;
import com.whatsub.honeybread.mgmtadmin.domain.advertisement.dto.AdvertisementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementQueryService {
    private final AdvertisementRepository repository;

    public Page<AdvertisementResponse> getAdvertisements(final Pageable pageable, final AdvertisementSearch search) {
        return repository.findAll(pageable, search)
            .map(AdvertisementResponse::of);
    }
}
