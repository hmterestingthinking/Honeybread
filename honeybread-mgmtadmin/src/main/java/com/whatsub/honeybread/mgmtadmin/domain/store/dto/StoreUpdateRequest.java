package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.PayType;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreCategory;
import com.whatsub.honeybread.core.domain.store.StorePayMethod;
import com.whatsub.honeybread.core.domain.store.StoreStatus;
import io.swagger.annotations.ApiModel;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@ApiModel("스토어 수정 요청")
@Value
public class StoreUpdateRequest extends StoreCommandCommonRequest {

    @Valid
    @NotNull
    StoreOperationRequest operation;

    @NotNull
    StoreStatus status;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreUpdateRequest(StoreOperationRequest operation,
                              StoreStatus status,
                              StoreBasicRequest basic,
                              BankAccountRequest bankAccount,
                              Set<Long> categoryIds,
                              Set<PayType> payMethods) {
        super(basic, bankAccount, categoryIds, payMethods);
        this.operation = operation;
        this.status = status;
    }

    public Store toEntity() {
        return Store.builder()
                .operation(operation.toStoreOperation())
                .status(status)
                .storeBasic(basic.toStoreBasic())
                .bankAccount(bankAccount.toBankAccount())
                .categories(categoryIds.stream().map(StoreCategory::new).collect(Collectors.toList()))
                .payMethods(payMethods.stream().map(StorePayMethod::new).collect(Collectors.toList()))
                .build();
    }
}
