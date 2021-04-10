package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.store.dto.StoreSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

    Page<Store> getStores(Pageable pageable, StoreSearch search);

}