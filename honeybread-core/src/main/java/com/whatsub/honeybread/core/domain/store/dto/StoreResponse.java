package com.whatsub.honeybread.core.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.Store;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreResponse {
    Long storeId;

    public static StoreResponse toDto(Store store) {
        return new StoreResponse(store.getId());
    }
}
