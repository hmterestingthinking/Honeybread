package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

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

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_price_delivery_tips")
public class OrderPriceDeliveryTip extends BaseEntity {

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Money from;

    @Column(nullable = false)
    private Money to;

    @Column(nullable = false)
    private Money tip;

    @Builder
    private OrderPriceDeliveryTip(Long storeId, Money from, Money to, Money tip) {
        this.storeId = storeId;
        this.from = from;
        this.to = to;
        this.tip = tip;
    }

}
