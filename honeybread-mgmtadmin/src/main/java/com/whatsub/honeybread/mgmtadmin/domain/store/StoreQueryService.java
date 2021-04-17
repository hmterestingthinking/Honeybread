package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.store.dto.StoreSearch;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreQueryService {

    private final StoreRepository repository;

    public StoreResponse getStoreByStoreId(long storeId) {
        Store store = repository.findById(storeId)
                .orElseThrow(() -> new HoneyBreadException(ErrorCode.STORE_NOT_FOUND));
        return StoreResponse.of(store);
    }

    public Page<StoreResponse> getStores(final Pageable pageable, final StoreSearch search) {
        return repository.getStores(pageable, search).map(StoreResponse::of);
    }

}
