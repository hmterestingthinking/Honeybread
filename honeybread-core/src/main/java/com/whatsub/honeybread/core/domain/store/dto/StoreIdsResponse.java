package com.whatsub.honeybread.core.domain.store.dto;

import lombok.Value;

import java.util.Collection;

@Value
public class StoreIdsResponse {
    Collection<Long> storeIds;
}
