package com.whatsub.honeybread.core.domain.store;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.whatsub.honeybread.core.domain.store.dto.StoreSearch;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.whatsub.honeybread.core.domain.store.QStore.store;

public class StoreRepositoryImpl extends QuerydslRepositorySupport implements StoreRepositoryCustom {

    public StoreRepositoryImpl() {
        super(Store.class);
    }

    @Override
    public Page<Store> getStores(final Pageable pageable, final StoreSearch search) {
        return applyPagination(pageable, query -> query.from(store)
                .where(likeName(search.getName()),
                        equalSellerId(search.getSellerId()),
                        equalStoreStatus(search.getStatus())));
    }

    private BooleanExpression likeName(String name) {
        return StringUtils.isBlank(name) ? null : store.basic.name.like(name + "%");
    }

    private BooleanExpression equalSellerId(Long sellerId) {
        return sellerId == null ? null : store.sellerId.eq(sellerId);
    }

    private BooleanExpression equalStoreStatus(StoreStatus status) {
        return status == null ? null : store.status.eq(status);
    }

}
