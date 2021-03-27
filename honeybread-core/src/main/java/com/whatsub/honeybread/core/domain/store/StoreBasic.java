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
public class StoreBasic {

    // 스토어명
    private String name;

    // 전화번호
    private String tel;

    // 대표 이미지
    private String imageUrl;

    // 원산지
    private String originCountry;

    // 소개글
    private String introduce;

    // 알림글
    private String announcement;

    // 사업자등록자번호
    private String businessLicenseNumber;

    // 스토어 계좌
    private BankAccount bankAccount;

    // 주소
    private Address address;

    // 운영시간
    private String operationTime;

    // 휴무일
    private String holiday;

    // 휴게 시간
    private String breakTime;
}
