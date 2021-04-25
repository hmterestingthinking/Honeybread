package com.whatsub.honeybread.core.domain.orderpricedeliverytip;

import com.whatsub.honeybread.core.domain.model.Money;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderPriceDeliveryTipRepository extends JpaRepository<OrderPriceDeliveryTip, Long>, OrderPriceDeliveryTipRepositoryCustom {
    boolean existsByStoreIdAndFromPriceAndToPrice(final long storeId, final Money from, final Money to);
    List<OrderPriceDeliveryTip> findByStoreId(final long storeId);
    boolean existsByStoreIdAndToPriceIsNull(Long storeId);
}
