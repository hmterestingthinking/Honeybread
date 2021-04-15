package com.whatsub.honeybread.core.domain.storedeliveryprice;

import com.whatsub.honeybread.core.domain.model.Money;

import java.util.List;
import java.util.Map;

public interface StoreDeliveryPriceRepositoryCustom  {
    Map<Money, List<StoreDeliveryPrice>> getStoreDeliveryPrices(final Long storeId);
}
