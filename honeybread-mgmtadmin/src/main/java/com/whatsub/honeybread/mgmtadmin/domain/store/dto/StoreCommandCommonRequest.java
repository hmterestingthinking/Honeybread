package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.PayType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class StoreCommandCommonRequest {

    protected final StoreBasicRequest basic;

    protected final BankAccountRequest bankAccount;

    protected final Set<Long> categoryIds;

    protected final Set<PayType> payMethods;

}
