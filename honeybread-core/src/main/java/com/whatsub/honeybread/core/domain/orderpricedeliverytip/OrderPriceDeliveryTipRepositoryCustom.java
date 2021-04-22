package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;

import java.util.Optional;

public interface OrderPriceDeliveryTipRepositoryCustom {
    Optional<OrderPriceDeliveryTip> getTipByIncludePrice(final long storeId, final Money target);
}
