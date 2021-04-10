package com.whatsub.honeybread.mgmtadmin.domain.store.dto;

import com.whatsub.honeybread.core.domain.store.PayType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class StoreCommandCommonRequest {

    @Valid
    @NotNull
    protected final StoreBasicRequest basic;

    @Valid
    @NotNull
    protected final BankAccountRequest bankAccount;

    @NotEmpty
    protected final Set<Long> categoryIds;

    @NotEmpty
    protected final Set<PayType> payMethods;

}
