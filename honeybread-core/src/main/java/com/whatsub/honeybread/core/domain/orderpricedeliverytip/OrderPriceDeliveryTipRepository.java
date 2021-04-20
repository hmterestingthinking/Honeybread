package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPriceDeliveryTipRepository extends JpaRepository<OrderPriceDeliveryTip, Long> {
    boolean existsByFromAndTo(Money from, Money to);
}
