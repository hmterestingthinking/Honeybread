package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import static com.whatsub.honeybread.core.domain.ordertimedeliverytip.QOrderTimeDeliveryTip.orderTimeDeliveryTip;

public class OrderTimeDeliveryTipRepositoryImpl extends QuerydslRepositorySupport
    implements OrderTimeDeliveryTipRepositoryCustom {

    public OrderTimeDeliveryTipRepositoryImpl() {
        super(OrderTimeDeliveryTip.class);
    }

    @Override
    public Optional<OrderTimeDeliveryTip> getTipByTime(final long storeId,
                                                       final LocalTime time,
                                                       final DayOfWeek dayOfWeek) {
        return Optional.ofNullable(
            from(orderTimeDeliveryTip)
                .where(
                    eqStoreId(storeId),
                    includeTime(time),
                    containsDayOfWeek(dayOfWeek)
                ).fetchOne()
        );
    }

    private Predicate containsDayOfWeek(final DayOfWeek dayOfWeek) {
        return orderTimeDeliveryTip.deliveryTimePeriod.days.contains(dayOfWeek);
    }

    private Predicate includeTime(final LocalTime time) {
        final int minuteByMidnight = DeliveryTimePeriodUtil.convertMinuteByMidnight(time);
        final BooleanExpression between = orderTimeDeliveryTip.deliveryTimePeriod.fromMinuteByMidnight
            .loe(minuteByMidnight)
            .and(
                orderTimeDeliveryTip.deliveryTimePeriod.toMinuteByMidnight.gt(minuteByMidnight)
            );
        return between.or(orderTimeDeliveryTip.deliveryTimePeriod.isAllTheTime.isTrue());
    }

    private Predicate eqStoreId(final long storeId) {
        return orderTimeDeliveryTip.storeId.eq(storeId);
    }

}
