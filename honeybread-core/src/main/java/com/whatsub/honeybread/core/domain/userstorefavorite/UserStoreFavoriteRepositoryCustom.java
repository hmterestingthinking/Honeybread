package com.whatsub.honeybread.core.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserStoreFavoriteRepositoryCustom {
    Page<Store> getStoresByUserId(final Pageable pageable, final Long userId);
}