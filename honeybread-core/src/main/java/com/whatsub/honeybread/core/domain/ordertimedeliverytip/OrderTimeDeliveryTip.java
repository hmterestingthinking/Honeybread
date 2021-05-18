package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import com.whatsub.honeybread.core.domain.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_time_delivery_tips")
public class OrderTimeDeliveryTip extends BaseEntity {

    @Column(nullable = false)
    private Long storeId;

    @Embedded
    private DeliveryTimePeriod deliveryTimePeriod;

    @Column(nullable = false)
    private Money tip;

    @Builder
    private OrderTimeDeliveryTip(final Long storeId, final DeliveryTimePeriod deliveryTimePeriod, final Money tip) {
        this.storeId = storeId;
        this.deliveryTimePeriod = deliveryTimePeriod;
        this.tip = tip;
    }

}
