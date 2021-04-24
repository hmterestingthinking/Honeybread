package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.model.TimePeriod;
import com.whatsub.honeybread.core.domain.store.OperationStatus;
import com.whatsub.honeybread.core.domain.store.StoreOperation;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class StoreOperationRequest {

    @NotNull(message = "운영상태 정보는 필수 입력입니다.")
    OperationStatus status;

    @NotNull(message = "운영시간 정보는 필수 입력입니다.")
    TimePeriod period;

    public StoreOperation toStoreOperation() {
        return new StoreOperation(status, period);
    }
}
