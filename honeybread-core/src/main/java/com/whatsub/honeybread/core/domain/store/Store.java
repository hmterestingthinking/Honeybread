package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stores")
public class Store extends BaseEntity {

    private String uuid;

    private String name;

    private String tel;

    private Long sellerId;

    @Enumerated(EnumType.STRING)
    private OperationStatus operationStatus;

    private boolean workPublicHoliday;

    private StoreTempClosure storeTempClosure;

    private String imageUrl;

    private String originCountry;

    private String introduce;

    private String announcement;

    private BusinessLicense business;

    private Address address;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    private BankAccount bankAccount;

    @OneToMany(mappedBy = "store")
    private List<StoreCategory> categories;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorePayMethod> payMethods;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreOperation> operations;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreHoliday> holidays;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreBreakTime> breakTimes;

}
