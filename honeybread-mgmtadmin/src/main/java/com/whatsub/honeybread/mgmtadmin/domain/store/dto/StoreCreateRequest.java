package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.PayType;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreCategory;
import com.whatsub.honeybread.core.domain.store.StorePayMethod;
import io.swagger.annotations.ApiModel;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;
import java.util.stream.Collectors;

@ApiModel("스토어 등록 요청")
@Value
public class StoreCreateRequest extends StoreCommandCommonRequest {

    @NotNull(message = "판매자 정보는 필수 입력입니다.")
    Long sellerId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreCreateRequest(final Long sellerId,
                              final StoreBasicRequest basic,
                              final BankAccountRequest bankAccount,
                              final Set<Long> categoryIds,
                              final Set<PayType> payMethods) {
        super(basic, bankAccount, categoryIds, payMethods);
        this.sellerId = sellerId;
    }

    public Store toEntity() {
        return Store.builder()
                .sellerId(sellerId)
                .storeBasic(basic.toStoreBasic())
                .bankAccount(bankAccount.toBankAccount())
                .categories(categoryIds.stream().map(StoreCategory::new).collect(Collectors.toList()))
                .payMethods(payMethods.stream().map(StorePayMethod::new).collect(Collectors.toList()))
                .build();
    }

}
