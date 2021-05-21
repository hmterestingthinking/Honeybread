package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import com.querydsl.core.types.Predicate;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;

import java.time.LocalTime;
import java.util.Optional;

import static com.whatsub.honeybread.core.domain.ordertimedeliverytip.QOrderTimeDeliveryTip.orderTimeDeliveryTip;

public class OrderTimeDeliveryTipRepositoryImpl extends QuerydslRepositorySupport
    implements OrderTimeDeliveryTipRepositoryCustom {

    public OrderTimeDeliveryTipRepositoryImpl() {
        super(OrderTimeDeliveryTip.class);
    }

    @Override
    public Optional<OrderTimeDeliveryTip> getTipByTime(final long storeId, final LocalTime time) {
        return Optional.ofNullable(
            from(orderTimeDeliveryTip)
                .where(
                    eqStoreId(storeId),
                    includeTime(time)
                ).fetchOne()
        );
    }

    private Predicate includeTime(final LocalTime time) {
        final int minuteByMidnight = DeliveryTimePeriod.convertMinuteByMidnight(time);
        return orderTimeDeliveryTip.deliveryTimePeriod.fromMinuteByMidnight
            .loe(minuteByMidnight)
            .and(
                orderTimeDeliveryTip.deliveryTimePeriod.toMinuteByMidnight.gt(minuteByMidnight)
            );
    }

    private Predicate eqStoreId(final long storeId) {
        return orderTimeDeliveryTip.storeId.eq(storeId);
    }
}
