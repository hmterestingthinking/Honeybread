package com.whatsub.honeybread.core.domain.store.validator;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreCategory;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreValidator {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductValidationProperties productValidationProperties;

    public void validate(final Store store) {
        checkSellerIdExistence(store.getSellerId());
        checkStoreNameExistence(store.getBasic().getName());
        checkCategoryIds(store.getCategories());
    }

    public void validate(final Store saved, final Store update) {
        checkStoreNameExistenceExcludeSelf(saved, update.getBasic().getName());
        checkCategoryIds(update.getCategories());
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

    private void checkCategoryIds(final List<StoreCategory> categories) {
        Set<Long> categoryIds = categories.stream().map(StoreCategory::getCategoryId).collect(Collectors.toSet());
        if (categoryIds.size() > productValidationProperties.getMaxCategoryCnt()) {
            throw new HoneyBreadException(ErrorCode.EXCEED_MAX_STORE_CATEGORY_CNT);
        }

        Set<Long> savedCategoryIdSet = categoryRepository.findAllById(categoryIds).stream()
                .map(BaseEntity::getId).collect(Collectors.toSet());
        if (!savedCategoryIdSet.containsAll(categoryIds)) {
            throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }
}
