package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPriceDeliveryTipRepository extends JpaRepository<OrderPriceDeliveryTip, Long> {
    boolean existsByStoreIdAndFromPriceAndToPrice(final long storeId, final Money from, final Money to);
}
