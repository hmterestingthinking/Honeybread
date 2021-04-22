package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.querydsl.core.types.Predicate;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;

import java.util.Optional;

import static com.whatsub.honeybread.core.domain.orderpricedeliverytip.QOrderPriceDeliveryTip.orderPriceDeliveryTip;

public class OrderPriceDeliveryTipRepositoryImpl extends QuerydslRepositorySupport implements OrderPriceDeliveryTipRepositoryCustom {

    public OrderPriceDeliveryTipRepositoryImpl() {
        super(OrderPriceDeliveryTip.class);
    }

    @Override
    public Optional<OrderPriceDeliveryTip> getTipByIncludePrice(final long storeId, final Money target) {
        return Optional.ofNullable(
            from(orderPriceDeliveryTip)
                .where(
                    eqStoreId(storeId),
                    includePrice(target))
                .fetchOne());
    }

    private Predicate includePrice(final Money price) {
        return orderPriceDeliveryTip.fromPrice.loe(price).and(orderPriceDeliveryTip.toPrice.gt(price))
            .or(orderPriceDeliveryTip.fromPrice.loe(price).and(orderPriceDeliveryTip.toPrice.isNull()));
    }

    private Predicate eqStoreId(final long storeId) {
        return orderPriceDeliveryTip.storeId.eq(storeId);
    }

}
