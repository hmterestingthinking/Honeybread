package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.common.util.Sha256Utils;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.store.validator.StoreValidator;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreCreateRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreValidator storeValidator;

    @Transactional
    public Long create(final StoreCreateRequest storeCreateRequest) {
        Store store = storeCreateRequest.toEntity();
        storeValidator.validate(store);

        Store saved = storeRepository.save(store);
        saved.updateUuid(Sha256Utils.generate(saved.getId(), saved.getCreatedAt()));
        return saved.getId();
    }

    @Transactional
    public void update(final long storeId, final StoreUpdateRequest storeUpdateRequest) {
        Store saved = getSavedStore(storeId);
        Store updateStore = storeUpdateRequest.toEntity();
        storeValidator.validate(saved, updateStore);

        saved.update(updateStore);
    }

    private Store getSavedStore(final long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new HoneyBreadException(ErrorCode.STORE_NOT_FOUND));
    }

}
