package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.dto.StoreIdsResponse;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavorite;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStoreFavoriteQueryService {
    private final UserStoreFavoriteRepository repository;

    public StoreIdsResponse getStoresByUserId(final Long userId) {
        List<Long> storeIds = repository.findByUserId(userId).stream()
                .map(UserStoreFavorite::getStoreId)
                .collect(Collectors.toList());
        return new StoreIdsResponse(storeIds);
    }
}