package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.PayType;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreCategory;
import com.whatsub.honeybread.core.domain.store.StorePayMethod;
import com.whatsub.honeybread.core.domain.store.StoreStatus;
import io.swagger.annotations.ApiModel;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel("스토어 수정 요청")
@Value
public class StoreUpdateRequest extends StoreCommandCommonRequest {

    StoreOperationRequest operation;

    StoreStatus status;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreUpdateRequest(StoreOperationRequest operation,
                              StoreStatus status,
                              StoreBasicRequest basic,
                              BankAccountRequest bankAccount,
                              List<Long> categoryIds,
                              List<PayType> payMethods) {
        super(basic, bankAccount, categoryIds, payMethods);
        this.operation = operation;
        this.status = status;
    }

    public Store updateStore() {
        return Store.updateStore(
                operation.toStoreOperation(),
                status,
                basic.toStoreBasic(),
                bankAccount.toBankAccount(),
                categoryIds.stream().map(StoreCategory::new).collect(Collectors.toList()),
                payMethods.stream().map(StorePayMethod::new).collect(Collectors.toList()));
    }
}
