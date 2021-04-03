package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.store.PayType;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreCategory;
import com.whatsub.honeybread.core.domain.store.StorePayMethod;
import io.swagger.annotations.ApiModel;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel("스토어 등록 요청")
@Value
public class StoreCreateRequest extends StoreCommandCommonRequest {

    Long sellerId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreCreateRequest(Long sellerId,
                              StoreBasicRequest basic,
                              BankAccountRequest bankAccount,
                              List<Long> categoryIds,
                              List<PayType> payMethods) {
        super(basic, bankAccount, categoryIds, payMethods);
        this.sellerId = sellerId;
    }

    public Store createStore() {
        return Store.createStore(
                sellerId,
                basic.toStoreBasic(),
                bankAccount.toBankAccount(),
                categoryIds.stream().map(StoreCategory::new).collect(Collectors.toList()),
                payMethods.stream().map(StorePayMethod::new).collect(Collectors.toList()));
    }

}
