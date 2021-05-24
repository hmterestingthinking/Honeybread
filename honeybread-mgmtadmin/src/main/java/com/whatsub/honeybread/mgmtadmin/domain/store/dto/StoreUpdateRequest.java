package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

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
    @NotNull(message = "운영 정보는 필수 입력입니다.")
    StoreOperationRequest operation;

    @NotNull(message = "상태 정보는 필수 입력입니다.")
    StoreStatus status;

    public StoreUpdateRequest(final StoreOperationRequest operation,
                              final StoreStatus status,
                              final StoreBasicRequest basic,
                              final BankAccountRequest bankAccount,
                              final Set<Long> categoryIds,
                              final Set<PayType> payMethods) {
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
