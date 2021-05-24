package com.whatsub.honeybread.core.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.StoreStatus;
import lombok.Value;

@Value
public class StoreSearch {

    String name;

    Long sellerId;

    StoreStatus status;

}
