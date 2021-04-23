package com.whatsub.honeybread.core.domain.advertisement;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.domain.model.TimePeriod;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "advertisement_bid_notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementBidNotice extends BaseEntity {

    public enum Status {
        OPEN, // 생성
        PROGRESS, // 진행중
        CLOSED, // 종료
        ;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    @Builder
    private AdvertisementBidNotice(
        AdvertisementType type,
        int maximumStoreCounts,
        Money minimumBidPrice,
        Money bidPriceUnit,
        TimePeriod period
    ) {
        this.type = type;
        this.maximumStoreCounts = maximumStoreCounts;
        this.minimumBidPrice = minimumBidPrice;
        this.bidPriceUnit = bidPriceUnit;
        this.period = period;
    }

    public void update(AdvertisementBidNotice entity) {
        this.type = entity.getType();
        this.maximumStoreCounts = entity.getMaximumStoreCounts();
        this.minimumBidPrice = entity.getMinimumBidPrice();
        this.minimumBidPrice = entity.getMinimumBidPrice();
        this.bidPriceUnit = entity.getBidPriceUnit();
        this.period = entity.getPeriod();
    }

    public void close() {
        this.status = Status.CLOSED;
    }

    public boolean isProcess() {
        return this.status == Status.PROGRESS;
    }
}
