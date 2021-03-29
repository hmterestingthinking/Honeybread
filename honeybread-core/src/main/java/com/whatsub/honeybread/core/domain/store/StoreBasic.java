package com.whatsub.honeybread.core.domain.store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class StoreBasic {

    // 스토어명
    private String name;

    // 전화번호
    private String tel;

    // 대표 이미지
    private String imageUrl;

    // 주소
    @Embedded
    private Address address;

    // 안내 관련 정보
    @Embedded
    private StoreAnnouncement storeIntroduce;

    // 영업 관련 정보
    @Embedded
    private BusinessHours operationTime;

    // 사업자등록증 관련 정보
    @Embedded
    private BusinessLicense businessLicense;

}
