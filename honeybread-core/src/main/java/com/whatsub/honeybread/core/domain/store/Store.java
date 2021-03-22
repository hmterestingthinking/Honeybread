package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stores")
public class Store extends BaseEntity {

    private String uuid;

    private String name;

    private String tel;

    private Long seller_id;

    @Enumerated(EnumType.STRING)
    private OperationStatus operationStatus;

    private boolean workPublicHoliday;

    private LocalDateTime tempCloseStartAt;

    private LocalDateTime tempCloseEndAt;

    private String imageUrl;

    private String originCountry;

    private String introduce;

    private String announcement;

    private String businessLicenseNum;

    private String businessStoreName;

    private String zipNo;

    private String lnbrMnnm;

    private String addressDetail;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Enumerated(EnumType.STRING)
    private BankType bankType;

    private String accountNumber;

}
