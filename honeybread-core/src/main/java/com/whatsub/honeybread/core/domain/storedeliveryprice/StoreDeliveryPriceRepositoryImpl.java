package com.whatsub.honeybread.core.domain.storedeliveryprice;

import com.querydsl.core.types.Predicate;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.whatsub.honeybread.core.domain.storedeliveryprice.QStoreDeliveryPrice.storeDeliveryPrice;

public class StoreDeliveryPriceRepositoryImpl extends QuerydslRepositorySupport implements StoreDeliveryPriceRepositoryCustom {

    public StoreDeliveryPriceRepositoryImpl() {
        super(StoreDeliveryPrice.class);
    }

    @Override
    public Map<Money, List<StoreDeliveryPrice>> getStoreDeliveryPrices(final Long storeId) {
        return from(storeDeliveryPrice)
            .where(equalsStoreId(storeId))
            .transform(groupBy(storeDeliveryPrice.price).as(list(storeDeliveryPrice)));
    }

    private Predicate equalsStoreId(final Long storeId) {
        return storeDeliveryPrice.storeId.eq(storeId);
    }


}
