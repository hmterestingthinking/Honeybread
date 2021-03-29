package com.whatsub.honeybread.core.domain.store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class BusinessHours {

    // 운영시간
    private String businessHour;

    // 휴무일
    private String holiday;

    // 휴게 시간
    private String breakTime;

}
