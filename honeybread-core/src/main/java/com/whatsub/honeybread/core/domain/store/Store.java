package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class Store extends BaseEntity {

    // 스토어 텍스트형식 id
    @Column
    private String uuid;

    // 판매자
    @Column(nullable = false)
    private Long sellerId;

    // 현재 영업 정보
    @Embedded
    private StoreOperation operation;

    // 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;

    // 기본 정보
    @Embedded
    private StoreBasic basic;

    // 스토어 계좌 정보
    @Embedded
    private BankAccount bankAccount;

    // 카테고리
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private List<StoreCategory> categories = List.of();

    // 지원하는 결제 방식
    @ElementCollection
    @CollectionTable(name = "store_pay_methods", joinColumns = @JoinColumn(name = "store_id"))
    private List<StorePayMethod> payMethods = List.of();

    public static Store createStore(Long sellerId,
                                    StoreBasic storeBasic,
                                    BankAccount bankAccount,
                                    List<StoreCategory> categories,
                                    List<StorePayMethod> payMethods) {
        Store store = new Store();
        store.sellerId = sellerId;
        store.operation = StoreOperation.createClosedOperation();
        store.status = StoreStatus.WAITING;
        store.basic = storeBasic;
        store.bankAccount = bankAccount;
        store.categories = categories;
        store.payMethods = payMethods;
        return store;
    }

    public void updateUuid(String uuid) {
        this.uuid = uuid;
    }

}
