package com.whatsub.honeybread.core.domain.storedeliveryprice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreDeliveryPriceRepository extends JpaRepository<StoreDeliveryPrice, Long>, StoreDeliveryPriceRepositoryCustom {
    boolean existsByStoreIdAndSearchableDeliveryAddress(final Long storeId, final String searchableDeliveryAddress);
}
