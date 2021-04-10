package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.domain.store.OperationStatus;
import com.whatsub.honeybread.core.domain.store.StoreOperation;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class StoreOperationRequest {

    @NotNull
    OperationStatus status;

    @NotNull
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
