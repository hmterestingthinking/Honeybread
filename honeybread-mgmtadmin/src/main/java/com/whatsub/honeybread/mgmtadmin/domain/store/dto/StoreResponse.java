package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.PayType;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreCategory;
import com.whatsub.honeybread.core.domain.store.StorePayMethod;
import com.whatsub.honeybread.core.domain.store.StoreStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class StoreResponse {

    long storeId;

    String uuid;

    long sellerId;

    StoreOperationResponse operation;

    StoreStatus status;

    StoreBasicResponse basic;

    BankAccountResponse bankAccount;

    List<Long> categoryIds;

    List<PayType> payMethods;

    LocalDateTime createdAt;

    LocalDateTime lastModifiedAt;

    public static StoreResponse of(Store entity) {
        return StoreResponse
                .builder()
                .storeId(entity.getId())
                .uuid(entity.getUuid())
                .sellerId(entity.getSellerId())
                .operation(StoreOperationResponse.of(entity.getOperation()))
                .status(entity.getStatus())
                .basic(StoreBasicResponse.of(entity.getBasic()))
                .bankAccount(BankAccountResponse.of(entity.getBankAccount()))
                .categoryIds(entity.getCategories()
                        .stream()
                        .map(StoreCategory::getCategoryId)
                        .collect(Collectors.toList()))
                .payMethods(entity.getPayMethods()
                        .stream()
                        .map(StorePayMethod::getPayType)
                        .collect(Collectors.toList()))
                .createdAt(entity.getCreatedAt())
                .lastModifiedAt(entity.getLastModifiedAt())
                .build();
    }
}