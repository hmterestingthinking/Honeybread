package com.whatsub.honeybread.core.domain.userstorefavorite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStoreFavoriteRepository extends JpaRepository<UserStoreFavorite, Long> {
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    Long deleteByUserIdAndStoreId(Long userId, Long storeId);

    List<UserStoreFavorite> findByUserId(Long userId);
}
