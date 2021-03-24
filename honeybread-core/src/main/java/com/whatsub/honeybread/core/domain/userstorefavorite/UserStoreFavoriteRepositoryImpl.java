package com.whatsub.honeybread.core.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.support.QuerydslRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.whatsub.honeybread.core.domain.store.QStore.store;
import static com.whatsub.honeybread.core.domain.userstorefavorite.QUserStoreFavorite.userStoreFavorite;

public class UserStoreFavoriteRepositoryImpl extends QuerydslRepositorySupport implements UserStoreFavoriteRepositoryCustom {

    public UserStoreFavoriteRepositoryImpl() {
        super(UserStoreFavorite.class);
    }

    @Override
    public Page<Store> getStoresByUserId(final Pageable pageable, final Long userId) {
        return applyPagination(pageable, query -> query
                .from(store)
                .innerJoin(userStoreFavorite).on(store.id.eq(userStoreFavorite.userId)).fetchJoin()
                .where(userStoreFavorite.userId.eq(userId)));
    }
}