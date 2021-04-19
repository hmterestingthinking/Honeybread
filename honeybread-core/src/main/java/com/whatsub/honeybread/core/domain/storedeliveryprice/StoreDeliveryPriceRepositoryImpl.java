package com.whatsub.honeybread.core.domain.storedeliveryprice;

import com.querydsl.core.types.Predicate;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;

import java.util.List;

import static com.whatsub.honeybread.core.domain.storedeliveryprice.QStoreDeliveryPrice.storeDeliveryPrice;

public class StoreDeliveryPriceRepositoryImpl extends QuerydslRepositorySupport implements StoreDeliveryPriceRepositoryCustom {

    public StoreDeliveryPriceRepositoryImpl() {
        super(StoreDeliveryPrice.class);
    }

    @Override
    public List<StoreDeliveryPrice> getStoreDeliveryPrices(final Long storeId) {
        return from(storeDeliveryPrice)
            .where(eqStoreId(storeId))
            .fetch();
    }

    private Predicate eqStoreId(final long storeId) {
        return storeDeliveryPrice.storeId.eq(storeId);
    }


}
