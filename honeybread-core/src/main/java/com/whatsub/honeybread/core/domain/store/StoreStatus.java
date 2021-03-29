package com.whatsub.honeybread.core.domain.store;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreStatus {
    WAITING("입점대기"),
    REJECTED("입점반려"),
    ACTIVATED("정상"),
    DEACTIVATED("비활성"),
    WITHDRAW("탈퇴");

    private final String description;
}
