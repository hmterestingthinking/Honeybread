package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.common.util.Sha256Utils;
import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreCreateRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long create(final StoreCreateRequest storeCreateRequest) {
        checkSellerIdExistence(storeCreateRequest.getSellerId());
        checkStoreNameExistence(storeCreateRequest.getBasic().getName());
        checkCategoryIdsExistence(storeCreateRequest.getCategoryIds());

        Store saved = storeRepository.save(storeCreateRequest.toEntity());
        saved.updateUuid(Sha256Utils.generate(saved.getId(), saved.getBasic().getName()));
        return saved.getId();
    }

    @Transactional
    public void update(final long storeId, final StoreUpdateRequest storeUpdateRequest) {
        Store savedStore = getSavedStore(storeId);
        checkStoreNameExistenceExcludeSelf(savedStore, storeUpdateRequest.getBasic().getName());
        checkCategoryIdsExistence(storeUpdateRequest.getCategoryIds());

        savedStore.update(storeUpdateRequest.toEntity());
    }

    private Store getSavedStore(final long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new HoneyBreadException(ErrorCode.STORE_NOT_FOUND));
    }

    private void checkSellerIdExistence(final long sellerId) {
        if (!userRepository.existsById(sellerId)) {
            throw new HoneyBreadException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private void checkStoreNameExistence(final String name) {
        if (storeRepository.existsByBasicName(name)) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME);
        }
    }

    private void checkStoreNameExistenceExcludeSelf(final Store savedStore, final String name) {
        if (!savedStore.getBasic().getName().equals(name)) {
            checkStoreNameExistence(name);
        }
    }

    private void checkCategoryIdsExistence(final Set<Long> categoryIds) {
        Set<Long> savedCategoryIdSet = categoryRepository.findAllById(categoryIds).stream()
                .map(BaseEntity::getId).collect(Collectors.toSet());
        if (!savedCategoryIdSet.containsAll(categoryIds)) {
            throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

}
