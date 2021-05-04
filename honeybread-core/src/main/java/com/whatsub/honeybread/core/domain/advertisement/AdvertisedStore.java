package com.whatsub.honeybread.core.domain.advertisement;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "advertised_stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisedStore extends BaseEntity {

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Money winningBidPrice;

    @Builder
    private AdvertisedStore(Long storeId, Money winningBidPrice) {
        this.storeId = storeId;
        this.winningBidPrice = winningBidPrice;
    }
}
