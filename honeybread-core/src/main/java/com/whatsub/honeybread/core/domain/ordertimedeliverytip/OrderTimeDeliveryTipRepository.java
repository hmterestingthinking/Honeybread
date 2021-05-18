package com.whatsub.honeybread.core.domain.ordertimedeliverytip;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTimeDeliveryTipRepository extends JpaRepository<OrderTimeDeliveryTip, Long> {
    boolean existsByStoreId(final long storeId);
}
