package com.whatsub.honeybread.core.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.infra.MoneyConverter;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "`store-delivery-price`")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class StoreDeliveryPrice extends BaseEntity {

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String searchableDeliveryAddress;

    @Convert(converter = MoneyConverter.class)
    @Column(nullable = false)
    private Money price;

    @Builder
    public StoreDeliveryPrice(final Long storeId, final String searchableDeliveryAddress, final Money price) {
        this.storeId = storeId;
        this.searchableDeliveryAddress = searchableDeliveryAddress;
        this.price = price;
    }
}
