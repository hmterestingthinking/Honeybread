package com.whatsub.honeybread.core.domain.storedeliveryprice;

import java.util.List;

public interface StoreDeliveryPriceRepositoryCustom  {
    List<StoreDeliveryPrice> getStoreDeliveryPrices(final Long storeId);
}
