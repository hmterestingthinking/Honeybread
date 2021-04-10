package com.whatsub.honeybread.core.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.StoreStatus;
import lombok.Data;

@Data
public class StoreSearch {

    private String name;

    private Long sellerId;

    private StoreStatus status;

}
