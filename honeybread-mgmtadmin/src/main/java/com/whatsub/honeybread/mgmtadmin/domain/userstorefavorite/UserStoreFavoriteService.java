package com.whatsub.honeybread.mgmtadmin.domain.userstorefavorite;

import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavorite;
import com.whatsub.honeybread.core.domain.userstorefavorite.UserStoreFavoriteRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStoreFavoriteService {

    private final UserStoreFavoriteRepository userStoreFavoriteRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Long create(Long userId, Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new HoneyBreadException(ErrorCode.STORE_NOT_FOUND);
        }
        if (userStoreFavoriteRepository.existsByUserIdAndStoreId(userId, storeId)) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_USER_STORE_FAVORITE);
        }
        UserStoreFavorite save = userStoreFavoriteRepository.save(new UserStoreFavorite(userId, storeId));
        return save.getId();
    }

}