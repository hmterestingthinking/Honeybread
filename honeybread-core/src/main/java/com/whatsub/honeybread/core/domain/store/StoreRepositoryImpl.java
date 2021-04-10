package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;

public class StoreRepositoryImpl extends QuerydslRepositorySupport implements StoreRepositoryCustom {

    public StoreRepositoryImpl() {
        super(Store.class);
    }

}
