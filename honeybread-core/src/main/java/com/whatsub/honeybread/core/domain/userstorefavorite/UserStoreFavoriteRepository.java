package com.whatsub.honeybread.core.domain.userstorefavorite;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreFavoriteRepository extends JpaRepository<UserStoreFavorite, Long> {
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
