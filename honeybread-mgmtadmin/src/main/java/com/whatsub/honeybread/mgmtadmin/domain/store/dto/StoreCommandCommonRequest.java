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
    @NotNull(message = "기본 정보는 필수 입력입니다.")
    protected final StoreBasicRequest basic;

    @Valid
    @NotNull(message = "계좌 정보는 필수 입력입니다.")
    protected final BankAccountRequest bankAccount;

    @NotEmpty(message = "카테고리는 최소 1개 필수 입력입니다.")
    protected final Set<Long> categoryIds;

    @NotEmpty(message = "결제방식은 최소 1개 필수 입력입니다.")
    protected final Set<PayType> payMethods;

}
