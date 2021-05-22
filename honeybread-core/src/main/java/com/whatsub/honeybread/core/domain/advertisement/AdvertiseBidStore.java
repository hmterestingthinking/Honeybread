package com.whatsub.honeybread.core.domain.advertisement;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "advertised_bid_stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertiseBidStore extends BaseEntity {

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Money bidPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_bid_notice_id")
    private AdvertisementBidNotice advertisementBidNotice;
}
