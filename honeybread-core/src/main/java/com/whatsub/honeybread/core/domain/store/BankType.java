package com.whatsub.honeybread.core.domain.store;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BankType {
    NH("농협"),
    KB("국민"),
    SHINHAN("신한")
    ;

    private final String bankName;

}
