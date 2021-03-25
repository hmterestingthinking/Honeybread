package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.dto.StoreResponse;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStoreFavoriteQueryService {
    private final UserStoreFavoriteRepository repository;

    public Page<StoreResponse> getStoresByUserId(final Pageable pageable, final Long userId) {
        Page<Store> storePage = repository.getStoresByUserId(pageable, userId);
        return storePage.map(StoreResponse::toDto);
    }
}