package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.domain.store.OperationStatus;
import com.whatsub.honeybread.core.domain.store.StoreOperation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class StoreOperationResponse {

    OperationStatus status;

    TimePeriod period;

    public static StoreOperationResponse of(StoreOperation storeOperation) {
        return StoreOperationResponse
                .builder()
                .status(storeOperation.getStatus())
                .period(storeOperation.getPeriod())
                .build();
    }

}
