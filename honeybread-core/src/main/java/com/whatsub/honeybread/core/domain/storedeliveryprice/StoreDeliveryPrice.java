package com.whatsub.honeybread.core.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "store_delivery_price")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class StoreDeliveryPrice extends BaseEntity {

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String searchableDeliveryAddress;

    @Column(nullable = false)
    private Money price;

    @Builder
    private StoreDeliveryPrice(final Long storeId, final String searchableDeliveryAddress, final Money price) {
        this.storeId = storeId;
        this.searchableDeliveryAddress = searchableDeliveryAddress;
        this.price = price;
    }

    public void update(final StoreDeliveryPrice modifyEntity) {
        this.price = modifyEntity.price;
    }
}
