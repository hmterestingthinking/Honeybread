package com.whatsub.honeybread.core.domain.advertisement;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "advertisement_bid_notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementBidNotice extends BaseEntity {

    @Column(nullable = false)
    private AdvertisementType type;

    @Column(nullable = false)
    private int maximumStoreCounts;

    @Column(nullable = false)
    private Money minimumBidPrice;

    @Column(nullable = false)
    private Money bidPriceUnit;

    @AttributeOverrides(
        value = {
            @AttributeOverride(name = "from", column = @Column(name = "ad_start_at", nullable = false)),
            @AttributeOverride(name = "to", column = @Column(name = "ad_end_at", nullable = false))
        }
    )
    private TimePeriod period = TimePeriod.EMPTY;
}
