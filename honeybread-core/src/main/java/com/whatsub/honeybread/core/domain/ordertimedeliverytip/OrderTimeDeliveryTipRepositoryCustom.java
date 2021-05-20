package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import java.time.LocalTime;
import java.util.Optional;

public interface OrderTimeDeliveryTipRepositoryCustom {
    Optional<OrderTimeDeliveryTip> getTipByTime(final long storeId, final LocalTime time);
}
