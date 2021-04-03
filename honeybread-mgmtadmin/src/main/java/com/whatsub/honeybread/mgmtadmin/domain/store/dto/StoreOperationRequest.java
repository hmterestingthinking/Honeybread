package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.domain.store.OperationStatus;
import com.whatsub.honeybread.core.domain.store.StoreOperation;
import lombok.Value;

@Value
public class StoreOperationRequest {

    OperationStatus status;

    TimePeriod period;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreOperationRequest(OperationStatus status, TimePeriod period) {
        this.status = status;
        this.period = period;
    }

    public StoreOperation toStoreOperation() {
        return new StoreOperation(status, period);
    }
}
